package com.chyikwei.app.ner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class NewsTextRecord implements TextRecordInterface {
  public static final String UUID_FIELD_NAME = "uuid";
  public static final String TITLE_FIELD_NAME = "title";
  public static final String TEXT_FIELD_NAME = "text";
  private final UUID uuid;
  private final String title;
  private final String text;

  public NewsTextRecord(final UUID uid, final String titleStr, final String textStr) {
    this.uuid = uid;
    this.title = titleStr;
    this.text = textStr;
  }

  @Override
  public UUID getUUID() {
        return uuid;
    }


  public static NewsTextRecord fromJson(final String jsonInput) {
    Map<String, String> map = new Gson().fromJson(
        jsonInput, new TypeToken<HashMap<String, String>>() {} .getType());
    return new NewsTextRecord(UUID.fromString(map.get(UUID_FIELD_NAME)),
        map.get(TITLE_FIELD_NAME),
        map.get(TEXT_FIELD_NAME));
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof NewsTextRecord) {
      NewsTextRecord t = (NewsTextRecord) obj;
      return this.uuid.equals(t.uuid)
          && this.title.equals(t.title)
          && this.text.equals(t.text);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 1;
    hash = hash * 17 + uuid.hashCode();
    hash = hash * 31 + title.hashCode();
    hash = hash * 13 + text.hashCode();
    return hash;
  }

  @Override
  public List<Pair<String, String>> getTextFields() {
    List<Pair<String, String>> textList = new ArrayList<>();
    textList.add(ImmutablePair.of(TITLE_FIELD_NAME, title));
    textList.add(ImmutablePair.of(TEXT_FIELD_NAME, text));
    return textList;
  }
}
