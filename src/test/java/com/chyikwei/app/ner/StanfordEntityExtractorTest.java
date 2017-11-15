package com.chyikwei.app.ner;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class StanfordEntityExtractorTest {

  @Ignore("temporary ignored to speed up test")
  @Test
  public void testSingleSentenceAnnotate() throws Exception {
    final String sentence = "Jim bought 300 shares of Acme Corp. in 2006.\n";
    final int numEntities = 4;

    EntityExtractInterface ner = StanfordEntityExtractor.getInstance();
    List<Entity> entities = ner.annotate(sentence);

    assertEquals(numEntities, entities.size());

    List<Entity> actuals = Arrays.asList(
        new Entity("PERSON", "Jim"),
        new Entity("NUMBER", "300"),
        new Entity("ORGANIZATION", "Acme Corp."),
        new Entity("DATE", "2006")
    );

    for (int i=0; i < numEntities; i++) {
      assertEquals(actuals.get(i), entities.get(i));
    }
  }

}