---
layout: page-fullwidth
title: "List of components contained in DKPro Core 1.7.0 (ASL/GPL)"

docs-gpl: "apidocs/index.html?de/tudarmstadt/ukp/dkpro/core/"
docs-asl: "apidocs/index.html?de/tudarmstadt/ukp/dkpro/core/"
---

Further information on models for particular components can be found in the [list of models](models.html)

## Coreference
  * [StanfordCoreferenceResolver]({{ page.docs-gpl }}stanfordnlp/StanfordCoreferenceResolver.html)

## Chunker
  * [OpenNlpChunker]({{ page.docs-asl }}opennlp/OpenNlpChunker.html)
  * [TreeTaggerChunker]({{ page.docs-asl }}treetagger/TreeTaggerChunker.html)

## Decompounding
  * [CompoundAnnotator]({{ page.docs-asl }}decompounding/uima/annotator/CompoundAnnotator.html)

## Dictionary Annotation
  * [DictionaryAnnotator]({{ page.docs-asl }}dictionaryannotator/DictionaryAnnotator.html)
  * [SemanticFieldAnnotator]({{ page.docs-asl }}dictionaryannotator/semantictagging/SemanticFieldAnnotator.html)

## Language Identification
  * [Token ngram-based (Character n-grams)]({{ page.docs-asl }}langdetect/LangDetectLanguageIdentifier.html)
  * [LangDect (Token ngram-based)]({{ page.docs-asl }}langdect/LanguageDetector.html)
  * [LanguageIdentifier (TextCat)]({{ page.docs-asl }}textcat/LanguageIdentifier.html)

## Lemmatization
  * [ClearNlpLemmatizer]({{ page.docs-asl }}clearnlp/ClearNlpLemmatizer.html)
  * [GateLemmatizer]({{ page.docs-gpl }}gate/GateLemmatizer.html)
  * [LanguageToolLemmatizer]({{ page.docs-asl }}languagetool/LanguageToolLemmatizer.html)
  * [MateLemmatizer]({{ page.docs-gpl }}matetools/MateLemmatizer.html)
  * [StanfordLemmatizer]({{ page.docs-gpl }}stanfordnlp/StanfordLemmatizer.html)
  * [MorphaLemmatizer]({{ page.docs-asl }}morpha/MorphaStemmer.html)
  * [TreeTaggerPosLemmaTT4J]({{ page.docs-asl }}treetagger/TreeTaggerPosLemmaTT4J.html)

## Morphological Annotation
  * [MateMorphTagger]({{ page.docs-gpl }}matetools/MateMorphTagger.html)
  * [SfstAnnotator]({{ page.docs-gpl }}sfst/SfstAnnotator.html)

## Named Entity Recognition
  * [OpenNlpNameFinder]({{ page.docs-asl }}opennlp/OpenNlpNameFinder.html)
  * [StanfordNamedEntityRecognizer]({{ page.docs-gpl }}stanfordnlp/StanfordNamedEntityRecognizer.html)

## Parsing
  * [BerkeleyParser (constituents)]({{ page.docs-gpl }}berkeleyparser/BerkeleyParser.html)
  * [ClearNlpDependencyParser (dependencies)]({{ page.docs-asl }}clearnlp/ClearNlpDependencyParser.html)
  * [OpenNlpParser (constituents)]({{ page.docs-asl }}opennlp/OpenNlpParser.html)
  * [MaltParser (dependencies)]({{ page.docs-asl }}maltparser/MaltParser.html)
  * [MateParser (dependencies)]({{ page.docs-gpl }}matetools/MateParser.html) 
  * [MstParser (dependencies)]({{ page.docs-asl }}mstparser/MstParser.html)
  * [StanfordParser (constituents, dependencies for some languages)]({{ page.docs-gpl }}stanfordnlp/StanfordParser.html)

## POS Tagging
  * [ArktweetTagger]({{ page.docs-gpl }}arktools/ArktweetTagger.html)
  * [ClearNlpPosTagger]({{ page.docs-asl }}clearnlp/ClearNlpPosTagger.html)
  * [HunPostTagger]({{ page.docs-asl }}hunpos/HunPosTagger.html)
  * [MatePosTagger]({{ page.docs-gpl }}matetools/MatePosTagger.html)
  * [MeCabTagger]({{ page.docs-asl }}mecab/MeCabTagger.html)
  * [OpenNlpPosTagger]({{ page.docs-asl }}opennlp/OpenNlpPosTagger.html)
  * [StanfordPosTagger]({{ page.docs-gpl }}stanfordnlp/StanfordPosTagger.html)
  * [TreeTaggerPosTagger]({{ page.docs-asl }}treetagger/TreeTaggerPosTagger.html)

## Segmentation / Tokenization
  * [BreakIteratorSegmenter]({{ page.docs-asl }}tokit/BreakIteratorSegmenter.html)
  * [ClearNlpSegmenter]({{ page.docs-asl }}clearnlp/ClearNlpSegmenter.html)
  * [LanguageToolSegmenter]({{ page.docs-asl }}languagetool/LanguageToolSegmenter.html)
  * [OpenNlpSegmenter]({{ page.docs-asl }}opennlp/OpenNlpSegmenter.html)
  * [StanfordSegmenter]({{ page.docs-gpl }}stanfordnlp/StanfordSegmenter.html)

## Semantic Role Labeling
  * [ClearNlpSemanticRoleLabeler]({{ page.docs-asl }}clearnlp/ClearNlpSemanticRoleLabeler.html)

## Spell/grammar Checking
  * [LanguageToolChecker]({{ page.docs-asl }}languagetool/LanguageToolChecker.html)
  * [NorvigSpellingCorrector]({{ page.docs-asl }}norvig/NorvigSpellingCorrector.html)
  * [SpellChecker (Jazzy)]({{ page.docs-asl }}jazzy/SpellChecker.html)

## Stemming
  * [SnowballStemmer]({{ page.docs-asl }}snowball/SnowballStemmer.html)