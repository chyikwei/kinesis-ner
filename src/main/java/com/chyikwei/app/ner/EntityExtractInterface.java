package com.chyikwei.app.ner;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface EntityExtractInterface {
  public List<Pair<String, String>> annotate(String text);
}
