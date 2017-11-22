package com.chyikwei.app.model;

import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.tuple.Pair;

public interface TextRecord {

  /**
   * UUID of text record
   *
   * @return UUID of the Text record
   */
  public UUID getUUID();

  /**
   * get all text field name and text
   *
   * @return List of (field_name, text) pairs
   */
  public List<Pair<String, String>> getTextFields();
}
