/*
 * Anserini: A Lucene toolkit for replicable information retrieval research
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.anserini.integration;

import io.anserini.collection.TrecCollection;
import io.anserini.index.IndexArgs;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TrecEndToEndTest extends EndToEndTest {
  @Override
  protected IndexArgs getIndexArgs() {
    IndexArgs indexArgs = createDefaultIndexArgs();

    indexArgs.input = "src/test/resources/sample_docs/trec/collection2";
    indexArgs.collectionClass = TrecCollection.class.getSimpleName();

    return indexArgs;
  }

  @Override
  protected void setCheckIndexGroundTruth() {
    docCount = 3;
    referenceDocs.put("TREC_DOC_1", Map.of(
        "contents", "This is head very simple text",
        "raw", "<HEAD>This is head</HEAD>\n" +
            "<TEXT>\n" +
            "very simple\n" +
            "text\n" +
            "</TEXT>"));
    referenceDocs.put("WSJ_1", Map.of(
        "contents", "head text 01/30/03 content",
        "raw", "<HL>\n" +
            "head text\n" +
            "</HL>\n" +
            "<DATE>\n" +
            "01/30/03\n" +
            "</DATE>\n" +
            "<LP>\n" +
            "content\n" +
            "</LP>\n" +
            "<TEXT>\n" +
            "</TEXT>"));
    referenceDocs.put("DOC222", Map.of(
        "contents", "HEAD simple enough text text text",
        "raw", "<HEAD>HEAD</HEAD>\n" +
            "<TEXT>\n" +
            "simple\n" +
            "enough\n" +
            "text\n" +
            "text\n" +
            "text\n" +
            "</TEXT>"));

    referenceDocTokens.put("TREC_DOC_1", Map.of(
        "contents", Arrays.asList(new String[]{null, null, "head", "veri", "simpl", "text"})));
    referenceDocTokens.put("WSJ_1", Map.of(
        "contents", List.of("head", "text", "01", "30", "03", "content")));
    referenceDocTokens.put("DOC222", Map.of(
        "contents", List.of("head", "simpl", "enough", "text", "text", "text")));

    fieldNormStatusTotalFields = 1;  // text
    termIndexStatusTermCount = 12;   // Note that standard analyzer ignores stopwords; includes docids.
    termIndexStatusTotFreq = 17;
    storedFieldStatusTotalDocCounts = 3;
    // 16 positions for text fields, plus 1 for each document because of id
    termIndexStatusTotPos = 16 + storedFieldStatusTotalDocCounts;
    storedFieldStatusTotFields = 9;  // 3 docs * (1 id + 1 text + 1 raw)
  }

  @Override
  protected void setSearchGroundTruth() {
    topicReader = "Trec";
    topicFile = "src/test/resources/sample_topics/Trec";

    testQueries.put("bm25", createDefaultSearchArgs().bm25());
    referenceRunOutput.put("bm25", new String[]{
        "1 Q0 DOC222 1 0.343200 Anserini",
        "1 Q0 TREC_DOC_1 2 0.333400 Anserini",
        "1 Q0 WSJ_1 3 0.068700 Anserini"});

    testQueries.put("qld", createDefaultSearchArgs().qld());
    referenceRunOutput.put("qld", new String[]{
        "1 Q0 DOC222 1 0.002500 Anserini",
        "1 Q0 TREC_DOC_1 2 0.001700 Anserini",
        "1 Q0 WSJ_1 3 0.000000 Anserini"});

    testQueries.put("qljm", createDefaultSearchArgs().qljm());
    referenceRunOutput.put("qljm", new String[]{
        "1 Q0 DOC222 1 4.872300 Anserini",
        "1 Q0 TREC_DOC_1 2 4.619100 Anserini",
        "1 Q0 WSJ_1 3 1.658200 Anserini"});

    testQueries.put("inl2", createDefaultSearchArgs().inl2());
    referenceRunOutput.put("inl2", new String[]{
        "1 Q0 TREC_DOC_1 1 0.133200 Anserini",
        "1 Q0 DOC222 2 0.126100 Anserini",
        "1 Q0 WSJ_1 3 0.021100 Anserini"});

    testQueries.put("spl", createDefaultSearchArgs().spl());
    referenceRunOutput.put("spl", new String[]{
        "1 Q0 DOC222 1 0.446100 Anserini",
        "1 Q0 TREC_DOC_1 2 0.355000 Anserini",
        "1 Q0 WSJ_1 3 0.115900 Anserini"});

    testQueries.put("f2exp", createDefaultSearchArgs().f2exp());
    referenceRunOutput.put("f2exp", new String[]{
        "1 Q0 DOC222 1 1.434700 Anserini",
        "1 Q0 TREC_DOC_1 2 1.269600 Anserini",
        "1 Q0 WSJ_1 3 0.536200 Anserini"});

    testQueries.put("f2log", createDefaultSearchArgs().f2log());
    referenceRunOutput.put("f2log", new String[]{
        "1 Q0 DOC222 1 0.548500 Anserini",
        "1 Q0 TREC_DOC_1 2 0.523100 Anserini",
        "1 Q0 WSJ_1 3 0.139500 Anserini"});
  }
}
