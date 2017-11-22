package com.chyikwei.app.model;

import com.chyikwei.app.model.TextRecord;

/***
 * Factory to create TextRecord
 */
public interface TextRecordFactory {

  /**
   * Parse text record from json string
   *
   * @param jsonInput json string
   * @return TextRecord
   */
  public TextRecord fromJson(final String jsonInput);
}
