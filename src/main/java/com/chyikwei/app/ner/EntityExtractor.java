package com.chyikwei.app.ner;

import com.chyikwei.app.model.Entity;

import java.util.List;

public interface EntityExtractor {
  public List<Entity> annotate(String text);
}
