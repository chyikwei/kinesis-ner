package com.chyikwei.app.ner;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class EntiityExtractorTest {

  @Ignore
  @Test
  public void testStanfordSingleSentenceAnnotate() throws Exception {
    final String sentence = "Jim bought 300 shares of Acme Corp. in 2006.\n";
    EntiityExtractor ner = new EntiityExtractor();
    List<Pair<String, String>> entities = ner.annotate(sentence);

    assertEquals(4, entities.size());

    // entity 1: Jim
    assertEquals("PERSON", entities.get(0).getLeft());
    assertEquals("Jim", entities.get(0).getRight());

    // entity 2: 300
    assertEquals("NUMBER", entities.get(1).getLeft());
    assertEquals("300", entities.get(1).getRight());

    // entity 3: Acme Corp.
    assertEquals("ORGANIZATION", entities.get(2).getLeft());
    assertEquals("Acme Corp.", entities.get(2).getRight());

    // entity 4: "2006"
    assertEquals("DATE", entities.get(3).getLeft());
    assertEquals("2006", entities.get(3).getRight());

  }

}