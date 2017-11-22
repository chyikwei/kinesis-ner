package com.chyikwei.app.ner;

import java.util.List;

public interface EntityExtractInterface {
  public List<Entity> annotate(String text);
}
