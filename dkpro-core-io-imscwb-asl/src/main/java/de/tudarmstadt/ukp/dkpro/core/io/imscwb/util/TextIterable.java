/*******************************************************************************
 * Copyright 2011
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universit√§t Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.tudarmstadt.ukp.dkpro.core.io.imscwb.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.tudarmstadt.ukp.dkpro.core.api.io.ResourceCollectionReaderBase.Resource;
import de.tudarmstadt.ukp.dkpro.core.api.resources.ResourceUtils;

public class TextIterable
    implements Iterable<CorpusText>, Iterator<CorpusText>
{

    private final static Pattern TITLE_PATTERN = Pattern.compile("\"(.+)\"");
    private final static int BUFFER_SIZE = 100;
    private final Queue<Resource> fileQueue;
    private BufferedReader reader;
    private final String encoding;
    private final Queue<CorpusText> texts;
    private Resource currentResource;

    public TextIterable(Collection<Resource> files, String encoding)
    {
        this.encoding = encoding;
        this.texts = new LinkedList<CorpusText>();
        this.fileQueue = new LinkedList<Resource>(files);
        try {
            this.reader = getReader();
            fillTextQueue(BUFFER_SIZE);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Resource getCurrentResource() {
        return currentResource;
    }

    @Override
    public Iterator<CorpusText> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        if (texts.isEmpty()) {
            try {
                fillTextQueue(BUFFER_SIZE);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return !texts.isEmpty();
    }

    @Override
    public CorpusText next(){
        return texts.poll();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    private void fillTextQueue(int bufferSize) throws IOException {

        // check if reader is still valid
        if (reader == null) {
            return;
        }

        String line;
        boolean insideSentence = false;
        CorpusText text = null;
        CorpusSentence currentSentence = null;

        while (texts.size() < bufferSize && (line = reader.readLine()) != null)
        {
            if (line.startsWith("<text ")) {
                String title = getTitle(line);
                text = new CorpusText(title);
            }
            if (line.equals("<s>")) {
                insideSentence = true;
                currentSentence = new CorpusSentence();
                continue;
            }

            if (line.equals("</s>")) {
                if (text == null) {
                    throw new IOException("Inside sentence without text.");
                }
                text.addSentence(currentSentence);
                currentSentence = null;
                insideSentence = false;
            }

            if (line.equals("</text>")) {
                texts.add(text);
                text = null;
                insideSentence = false;
            }

            if (insideSentence && currentSentence != null) {
                TabTokenizer tokenizer = new TabTokenizer(line);

                for (int i=0; i<3; i++) {
                    if (!tokenizer.hasNext()) {
                        throw new IOException("Ill-formed line: " + line);
                    }
                    switch (i) {
                        case 0 : currentSentence.addToken(tokenizer.next()); break;
                        case 1 : currentSentence.addPOS(tokenizer.next()); break;
                        case 2 : currentSentence.addLemma(tokenizer.next()); break;
                    }
                }
            }
        }

        if (!reader.ready()) {
            reader = getReader();
        }
    }

    private String getTitle(String line) {
        Matcher m = TITLE_PATTERN.matcher(line);

        if (m.find()) {
            return line.substring(m.start(1), m.end(1));
        }
        else {
            return "";
        }
    }

    private BufferedReader getReader() throws FileNotFoundException, IOException {
        BufferedReader r = null;
        if (!fileQueue.isEmpty()) {
            currentResource = fileQueue.poll();
            
            InputStream resolvedStream = ResourceUtils.resolveCompressedInputStream(
                    currentResource.getInputStream(),
                    currentResource.getPath()
            );
                
            r = new BufferedReader(new InputStreamReader(resolvedStream, encoding));
        }
        return r;
    }
}