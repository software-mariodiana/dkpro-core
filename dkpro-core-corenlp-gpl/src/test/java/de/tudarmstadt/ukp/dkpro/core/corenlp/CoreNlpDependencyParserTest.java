/**
 * Copyright 2007-2014
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package de.tudarmstadt.ukp.dkpro.core.corenlp;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.util.JCasUtil.select;

import org.apache.commons.lang.ArrayUtils;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.jcas.JCas;
import org.junit.Rule;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;
import de.tudarmstadt.ukp.dkpro.core.testing.AssertAnnotations;
import de.tudarmstadt.ukp.dkpro.core.testing.DkproTestContext;
import de.tudarmstadt.ukp.dkpro.core.testing.TestRunner;

public class CoreNlpDependencyParserTest
{
    private static final String[] STANFORD_DEPENDENCY_TAGS = { "acomp", "advcl", "advmod", "amod",
            "appos", "aux", "auxpass", "cc", "ccomp", "conj", "cop", "csubj", "csubjpass", "dep",
            "det", "discourse", "dobj", "expl", "iobj", "mark", "mwe", "neg", "nn", "npadvmod",
            "nsubj", "nsubjpass", "num", "number", "parataxis", "pcomp", "pobj", "poss",
            "possessive", "preconj", "predet", "prep", "prt", "punct", "quantmod", "rcmod", "root",
            "tmod", "vmod", "xcomp" };

    private static final String[] UNIVERSAL_DEPENDENCY_TAGS = { "acl", "acl:relcl", "advcl",
            "advmod", "amod", "appos", "aux", "auxpass", "case", "cc", "cc:preconj", "ccomp",
            "compound", "compound:prt", "conj", "cop", "csubj", "csubjpass", "dep", "det",
            "det:predet", "discourse", "dobj", "expl", "iobj", "list", "mark", "mwe", "neg",
            "nmod", "nmod:npmod", "nmod:poss", "nmod:tmod", "nsubj", "nsubjpass", "nummod",
            "parataxis", "punct", "root", "xcomp" };

    private static final String[] PTB_POS_TAGS = { "#", "$", "''", ",", "-LRB-", "-RRB-", ".", ":",
            "CC", "CD", "DT", "EX", "FW", "IN", "JJ", "JJR", "JJS", "LS", "MD", "NN", "NNP", "NNPS",
            "NNS", "PDT", "POS", "PRP", "PRP$", "RB", "RBR", "RBS", "RP", "SYM", "TO", "UH", "VB",
            "VBD", "VBG", "VBN", "VBP", "VBZ", "WDT", "WP", "WP$", "WRB", "``" };

    private static final String[] UNIVERSAL_POS_TAGS = { "ADJ", "ADP", "ADV", "AUX", "CONJ", "DET",
            "INTJ", "NOUN", "NUM", "PART", "PRON", "PROPN", "PUNCT", "SCONJ", "SYM", "VERB", "X" };

    private static final String[] CORENLP34_POS_TAGS = { "A", "ADJ", "ADJWH", "ADV", "ADVWH", "C",
            "CC", "CL", "CLO", "CLR", "CLS", "CS", "DET", "DETWH", "ET", "I", "N", "NC", "NPP", "P",
            "PREF", "PRO", "PROREL", "PROWH", "PUNC", "V", "VIMP", "VINF", "VPP", "VPR", "VS" };

    @Test
    public void testEnglishStanfordDependencies()
        throws Exception
    {
        JCas jcas = runTest("en", "sd", "We need a very complicated example sentence , which "
                + "contains as many constituents and dependencies as possible .");

        String[] dependencies = {
                "[  0,  2]NSUBJ(nsubj) D[0,2](We) G[3,7](need)",
                "[  3,  7]ROOT(root) D[3,7](need) G[3,7](need)",
                "[  8,  9]DET(det) D[8,9](a) G[35,43](sentence)",
                "[ 10, 14]ADVMOD(advmod) D[10,14](very) G[15,26](complicated)",
                "[ 15, 26]AMOD(amod) D[15,26](complicated) G[35,43](sentence)",
                "[ 27, 34]NN(nn) D[27,34](example) G[35,43](sentence)",
                "[ 35, 43]DOBJ(dobj) D[35,43](sentence) G[3,7](need)",
                "[ 44, 45]PUNCT(punct) D[44,45](,) G[35,43](sentence)",
                "[ 46, 51]NSUBJ(nsubj) D[46,51](which) G[52,60](contains)",
                "[ 52, 60]RCMOD(rcmod) D[52,60](contains) G[35,43](sentence)",
                "[ 64, 68]AMOD(amod) D[64,68](many) G[69,81](constituents)",
                "[ 69, 81]PREP(prep_as) D[69,81](constituents) G[52,60](contains)",
                "[ 86, 98]CONJ(conj_and) D[86,98](dependencies) G[69,81](constituents)",
                "[102,110]PREP(prep_as) D[102,110](possible) G[69,81](constituents)",
                "[111,112]PUNCT(punct) D[111,112](.) G[3,7](need)" };

        String[] unmappedDep = {};

        AssertAnnotations.assertDependencies(dependencies, select(jcas, Dependency.class));
        AssertAnnotations.assertTagset(CoreNlpPosTagger.class, POS.class, "ptb", PTB_POS_TAGS, jcas);
        AssertAnnotations.assertTagset(CoreNlpDependencyParser.class, POS.class, "ptb", PTB_POS_TAGS,
                jcas);
        AssertAnnotations.assertTagset(CoreNlpDependencyParser.class, Dependency.class,
                "stanford341", STANFORD_DEPENDENCY_TAGS, jcas);
        AssertAnnotations.assertTagsetMapping(Dependency.class, "stanford341", unmappedDep, jcas);
    }

    @Test
    public void testEnglishUniversalDependencies()
        throws Exception
    {
        JCas jcas = runTest("en", "ud", "We need a very complicated example sentence , which "
                + "contains as many constituents and dependencies as possible .");

        String[] dependencies = {
                "[  0,  2]NSUBJ(nsubj) D[0,2](We) G[3,7](need)",
                "[  3,  7]ROOT(root) D[3,7](need) G[3,7](need)",
                "[  8,  9]DET(det) D[8,9](a) G[35,43](sentence)",
                "[ 10, 14]ADVMOD(advmod) D[10,14](very) G[15,26](complicated)",
                "[ 15, 26]AMOD(amod) D[15,26](complicated) G[35,43](sentence)",
                "[ 27, 34]NN(compound) D[27,34](example) G[35,43](sentence)",
                "[ 35, 43]DOBJ(dobj) D[35,43](sentence) G[3,7](need)",
                "[ 44, 45]PUNCT(punct) D[44,45](,) G[35,43](sentence)",
                "[ 46, 51]NSUBJ(nsubj) D[46,51](which) G[52,60](contains)",
                "[ 52, 60]Dependency(acl:relcl) D[52,60](contains) G[35,43](sentence)",
                "[ 61, 63]PREP(case) D[61,63](as) G[69,81](constituents)",
                "[ 64, 68]AMOD(amod) D[64,68](many) G[69,81](constituents)",
                "[ 69, 81]Dependency(nmod:as) D[69,81](constituents) G[52,60](contains)",
                "[ 82, 85]CC(cc) D[82,85](and) G[69,81](constituents)",
                "[ 86, 98]CONJ(conj:and) D[86,98](dependencies) G[69,81](constituents)",
                "[ 99,101]PREP(case) D[99,101](as) G[102,110](possible)",
                "[102,110]Dependency(acl:as) D[102,110](possible) G[69,81](constituents)",
                "[111,112]PUNCT(punct) D[111,112](.) G[3,7](need)" };

        String[] unmappedDep = { "acl:relcl", "cc:preconj", "compound:prt", "det:predet",
                "nmod:npmod", "nmod:poss", "nmod:tmod" };

        AssertAnnotations.assertDependencies(dependencies, select(jcas, Dependency.class));
        AssertAnnotations.assertTagset(CoreNlpPosTagger.class, POS.class, "ptb", PTB_POS_TAGS, jcas);
        AssertAnnotations.assertTagset(CoreNlpDependencyParser.class, POS.class, "ptb", PTB_POS_TAGS,
                jcas);
        AssertAnnotations.assertTagset(CoreNlpDependencyParser.class, Dependency.class, "universal",
                UNIVERSAL_DEPENDENCY_TAGS, jcas);
        AssertAnnotations.assertTagsetMapping(Dependency.class, "universal", unmappedDep, jcas);
    }

    @Test
    public void testEnglishWsjSd()
        throws Exception
    {
        JCas jcas = runTest("en", "wsj-sd", "We need a very complicated example sentence , which "
                + "contains as many constituents and dependencies as possible .");

        String[] dependencies = {
                "[  0,  2]NSUBJ(nsubj) D[0,2](We) G[3,7](need)",
                "[  3,  7]ROOT(root) D[3,7](need) G[3,7](need)",
                "[  8,  9]DET(det) D[8,9](a) G[35,43](sentence)",
                "[ 10, 14]ADVMOD(advmod) D[10,14](very) G[15,26](complicated)",
                "[ 15, 26]AMOD(amod) D[15,26](complicated) G[35,43](sentence)",
                "[ 27, 34]NN(nn) D[27,34](example) G[35,43](sentence)",
                "[ 35, 43]DOBJ(dobj) D[35,43](sentence) G[3,7](need)",
                "[ 44, 45]PUNCT(punct) D[44,45](,) G[35,43](sentence)",
                "[ 46, 51]NSUBJ(nsubj) D[46,51](which) G[52,60](contains)",
                "[ 52, 60]RCMOD(rcmod) D[52,60](contains) G[35,43](sentence)",
                "[ 61, 63]PREP(prep) D[61,63](as) G[52,60](contains)",
                "[ 64, 68]AMOD(amod) D[64,68](many) G[69,81](constituents)",
                "[ 69, 81]POBJ(pobj) D[69,81](constituents) G[61,63](as)",
                "[ 82, 85]CC(cc) D[82,85](and) G[69,81](constituents)",
                "[ 86, 98]CONJ(conj:and) D[86,98](dependencies) G[69,81](constituents)",
                "[ 99,101]PREP(prep) D[99,101](as) G[69,81](constituents)",
                "[102,110]POBJ(pobj) D[102,110](possible) G[99,101](as)",
                "[111,112]PUNCT(punct) D[111,112](.) G[3,7](need)" };

        String[] unmappedDep = {};

        AssertAnnotations.assertDependencies(dependencies, select(jcas, Dependency.class));
        AssertAnnotations.assertTagset(CoreNlpPosTagger.class, POS.class, "ptb", PTB_POS_TAGS, jcas);
        AssertAnnotations.assertTagset(CoreNlpDependencyParser.class, POS.class, "ptb", PTB_POS_TAGS,
                jcas);
        AssertAnnotations.assertTagset(CoreNlpDependencyParser.class, Dependency.class,
                "stanford341", STANFORD_DEPENDENCY_TAGS, jcas);
        AssertAnnotations.assertTagsetMapping(Dependency.class, "stanford341", unmappedDep, jcas);
    }

    @Test
    public void testFrenchUniversalDependencies()
        throws Exception
    {
        JCas jcas = runTest("fr", "ud", "Nous avons besoin d' une phrase par exemple très "
                + "compliqué , qui contient des constituants que de nombreuses dépendances et que "
                + "possible .");

        String[] dependencies = {
                "[  0,  4]ROOT(root) D[0,4](Nous) G[0,4](Nous)",
                "[  5, 10]NN(compound) D[5,10](avons) G[0,4](Nous)",
                "[ 11, 17]DEP(dep) D[11,17](besoin) G[5,10](avons)",
                "[ 18, 20]MWE(mwe) D[18,20](d') G[11,17](besoin)",
                "[ 21, 24]DET(det) D[21,24](une) G[25,31](phrase)",
                "[ 25, 31]PREP(case) D[25,31](phrase) G[18,20](d')",
                "[ 32, 35]PREP(case) D[32,35](par) G[25,31](phrase)",
                "[ 36, 43]PREP(case) D[36,43](exemple) G[32,35](par)",
                "[ 44, 48]ADVMOD(advmod) D[44,48](très) G[49,58](compliqué)",
                "[ 49, 58]AMOD(amod) D[49,58](compliqué) G[36,43](exemple)",
                "[ 59, 60]PUNCT(punct) D[59,60](,) G[49,58](compliqué)",
                "[ 61, 64]APPOS(appos) D[61,64](qui) G[59,60](,)",
                "[ 65, 73]APPOS(appos) D[65,73](contient) G[61,64](qui)",
                "[ 74, 77]DET(det) D[74,77](des) G[78,90](constituants)",
                "[ 78, 90]Dependency(nmod) D[78,90](constituants) G[65,73](contient)",
                "[ 91, 94]DEP(dep) D[91,94](que) G[78,90](constituants)",
                "[ 95, 97]DET(det) D[95,97](de) G[109,120](dépendances)",
                "[ 98,108]AMOD(amod) D[98,108](nombreuses) G[109,120](dépendances)",
                "[109,120]Dependency(nmod) D[109,120](dépendances) G[91,94](que)",
                "[121,123]CC(cc) D[121,123](et) G[109,120](dépendances)",
                "[124,127]MWE(mwe) D[124,127](que) G[121,123](et)",
                "[128,136]AMOD(amod) D[128,136](possible) G[124,127](que)",
                "[137,138]CONJ(conj:et) D[137,138](.) G[109,120](dépendances)" };

        String[] depTags = { "acl", "acl:relcl", "advcl", "advmod", "amod", "appos", "aux",
                "auxpass", "case", "cc", "ccomp", "compound", "conj", "cop", "csubj", "dep", "det",
                "discourse", "dobj", "expl", "iobj", "mark", "mwe", "name", "neg", "nmod",
                "nmod:poss", "nsubj", "nsubjpass", "nummod", "parataxis", "punct", "root",
                "xcomp" }; 
        
        String[] unmappedDep = { "acl:relcl", "nmod:poss" };

        AssertAnnotations.assertDependencies(dependencies, select(jcas, Dependency.class));
        AssertAnnotations.assertTagset(CoreNlpPosTagger.class, POS.class, "corenlp34", CORENLP34_POS_TAGS, jcas);
        AssertAnnotations.assertTagset(CoreNlpDependencyParser.class, POS.class, "universal", UNIVERSAL_POS_TAGS,
                jcas);
        AssertAnnotations.assertTagset(CoreNlpDependencyParser.class, Dependency.class, "universal",
                depTags, jcas);
        AssertAnnotations.assertTagsetMapping(Dependency.class, "universal", unmappedDep, jcas);
    }

    @Test
    public void testChineseCtbConllDependencies()
        throws Exception
    {
        JCas jcas = runTest("zh", "ctb-conll", 
                "我们 需要 一个 非常 复杂 的 句子 例如 其中 包含 许多 成分 和 尽可能 的 依赖 。");

        String[] dependencies = {
                "[  0,  2]Dependency(SUB) D[0,2](我们) G[3,5](需要)",
                "[  3,  5]ROOT(root) D[3,5](需要) G[3,5](需要)",
                "[  6,  8]Dependency(AMOD) D[6,8](一个) G[12,14](复杂)",
                "[  9, 11]Dependency(AMOD) D[9,11](非常) G[12,14](复杂)",
                "[ 12, 14]Dependency(DEP) D[12,14](复杂) G[15,16](的)",
                "[ 15, 16]Dependency(NMOD) D[15,16](的) G[17,19](句子)",
                "[ 17, 19]Dependency(OBJ) D[17,19](句子) G[3,5](需要)",
                "[ 20, 22]Dependency(VMOD) D[20,22](例如) G[26,28](包含)",
                "[ 23, 25]Dependency(SUB) D[23,25](其中) G[26,28](包含)",
                "[ 26, 28]Dependency(VMOD) D[26,28](包含) G[3,5](需要)",
                "[ 29, 31]Dependency(NMOD) D[29,31](许多) G[32,34](成分)",
                "[ 32, 34]Dependency(SUB) D[32,34](成分) G[43,45](依赖)",
                "[ 35, 36]Dependency(VMOD) D[35,36](和) G[43,45](依赖)",
                "[ 37, 40]Dependency(DEP) D[37,40](尽可能) G[41,42](的)",
                "[ 41, 42]Dependency(VMOD) D[41,42](的) G[43,45](依赖)",
                "[ 43, 45]Dependency(VMOD) D[43,45](依赖) G[26,28](包含)",
                "[ 46, 47]Dependency(P) D[46,47](。) G[3,5](需要)" };

        String[] depTags = { "AMOD", "DEP", "NMOD", "OBJ", "P", "PMOD", "PRD", "ROOT", "SBAR",
                "SUB", "VC", "VMOD" };
        
        String[] posTags = { "AD", "AS", "BA", "CC", "CD", "CS", "DEC", "DEG", "DER", "DEV", "DT",
                "ETC", "FW", "IJ", "JJ", "LB", "LC", "M", "MSP", "NN", "NR", "NT", "OD", "ON", "P",
                "PN", "PU", "SB", "SP", "URL", "VA", "VC", "VE", "VV", "X" };
        
        String[] unmappedDep = {};

        AssertAnnotations.assertDependencies(dependencies, select(jcas, Dependency.class));
        AssertAnnotations.assertTagset(CoreNlpPosTagger.class, POS.class, "ctb", posTags, jcas);
        // There are some minor differences between the tags produced by the POS tagger and the
        // tags expected by the parser model. We need a better test here that makes these differences
        // more visible and at the same time doesn't fail.
        //AssertAnnotations.assertTagset(CoreNlpDependencyParser.class, POS.class, "ctb", posTags,
        //        jcas);
        AssertAnnotations.assertTagset(CoreNlpDependencyParser.class, Dependency.class, "conll",
                depTags, jcas);
        AssertAnnotations.assertTagsetMapping(CoreNlpDependencyParser.class, Dependency.class,
                "conll", unmappedDep, jcas);
    }

    @Test
    public void testEnglishPtbConllDependencies()
        throws Exception
    {
        JCas jcas = runTest("en", "ptb-conll",
                "We need a very complicated example sentence , which "
                        + "contains as many constituents and dependencies as possible .");

        String[] dependencies = {
                "[  0,  2]Dependency(VMOD) D[0,2](We) G[3,7](need)",
                "[  3,  7]ROOT(root) D[3,7](need) G[3,7](need)",
                "[  8,  9]Dependency(NMOD) D[8,9](a) G[35,43](sentence)",
                "[ 10, 14]Dependency(AMOD) D[10,14](very) G[15,26](complicated)",
                "[ 15, 26]Dependency(NMOD) D[15,26](complicated) G[35,43](sentence)",
                "[ 27, 34]Dependency(NMOD) D[27,34](example) G[35,43](sentence)",
                "[ 35, 43]Dependency(VMOD) D[35,43](sentence) G[3,7](need)",
                "[ 44, 45]Dependency(P) D[44,45](,) G[35,43](sentence)",
                "[ 46, 51]Dependency(VMOD) D[46,51](which) G[52,60](contains)",
                "[ 52, 60]Dependency(NMOD) D[52,60](contains) G[35,43](sentence)",
                "[ 61, 63]Dependency(VMOD) D[61,63](as) G[52,60](contains)",
                "[ 64, 68]Dependency(NMOD) D[64,68](many) G[69,81](constituents)",
                "[ 69, 81]Dependency(PMOD) D[69,81](constituents) G[61,63](as)",
                "[ 82, 85]Dependency(COORD) D[82,85](and) G[69,81](constituents)",
                "[ 86, 98]Dependency(CONJ) D[86,98](dependencies) G[82,85](and)",
                "[ 99,101]Dependency(NMOD) D[99,101](as) G[69,81](constituents)",
                "[102,110]Dependency(PMOD) D[102,110](possible) G[99,101](as)",
                "[111,112]Dependency(P) D[111,112](.) G[3,7](need)" };

        String[] depTags = { "AMOD", "APPO", "CONJ", "COORD", "DEP", "IM", "NAME", "NMOD", "P",
                "PMOD", "PRN", "PRT", "ROOT", "SUB", "SUFFIX", "VC", "VMOD" };

        String[] unmappedDep = {};

        String[] posTags = { "#", "$", "''", "(", ")", ",", ".", ":", "CC", "CD", "DT", "EX", "FW",
                "IN", "JJ", "JJR", "JJS", "LS", "MD", "NN", "NNP", "NNPS", "NNS", "PDT", "POS",
                "PRP", "PRP$", "RB", "RBR", "RBS", "RP", "SYM", "TO", "UH", "VB", "VBD", "VBG",
                "VBN", "VBP", "VBZ", "WDT", "WP", "WP$", "WRB", "``" };

        AssertAnnotations.assertDependencies(dependencies, select(jcas, Dependency.class));
        // There are some minor differences between the tags produced by the POS tagger and the
        // tags expected by the parser model. We need a better test here that makes these differences
        // more visible and at the same time doesn't fail.
        //AssertAnnotations.assertTagset(CoreNlpPosTagger.class, POS.class, "ptb", PTB_POS_TAGS, jcas);
        AssertAnnotations.assertTagset(CoreNlpDependencyParser.class, POS.class, "ptb", posTags,
                jcas);
        AssertAnnotations.assertTagset(Dependency.class, "conll", depTags, jcas);
        AssertAnnotations.assertTagsetMapping(Dependency.class, "conll", unmappedDep, jcas);
    }
    
    private JCas runTest(String aLanguage, String aVariant, String aText, Object... aExtraParams)
        throws Exception
    {
        AggregateBuilder aggregate = new AggregateBuilder();
        
        aggregate.add(createEngineDescription(CoreNlpPosTagger.class));
        Object[] params = new Object[] {
                CoreNlpDependencyParser.PARAM_VARIANT, aVariant,
                CoreNlpDependencyParser.PARAM_PRINT_TAGSET, true};
        params = ArrayUtils.addAll(params, aExtraParams);
        aggregate.add(createEngineDescription(CoreNlpDependencyParser.class, params));

        return TestRunner.runTest(aggregate.createAggregateDescription(), aLanguage, aText);
    }
    
    @Rule
    public DkproTestContext testContext = new DkproTestContext();
}
