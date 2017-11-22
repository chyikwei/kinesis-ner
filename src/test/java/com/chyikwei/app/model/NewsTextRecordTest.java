package com.chyikwei.app.model;

import com.google.gson.Gson;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class NewsTextRecordTest {

  @Test
  public void testCreateRecordFromJson() throws Exception {
    NewsTextRecord t = new NewsTextRecord(UUID.randomUUID(), "Test Title", "Test text");
    String jsonStr = (new Gson()).toJson(t);
    NewsTextRecord t2 = NewsTextRecord.fromJson(jsonStr);
    assertEquals(t, t2);
  }

  @Test
  public void testGetTextFields() throws Exception {
    NewsTextRecord t = new NewsTextRecord(UUID.randomUUID(), "Test Title", "Test text");
    List<Pair<String, String>> tupleList = t.getTextFields();

    assertEquals(NewsTextRecord.TITLE_FIELD_NAME, tupleList.get(0).getLeft());
    assertEquals("Test Title", tupleList.get(0).getRight());
    assertEquals(NewsTextRecord.TEXT_FIELD_NAME, tupleList.get(1).getLeft());
    assertEquals("Test text", tupleList.get(1).getRight());
  }
}