package com.chyikwei.app.model;

public class NewsTextRecordFactory implements TextRecordFactory {

  private static NewsTextRecordFactory instance;

  private NewsTextRecordFactory() {
  }

  /**
   * get instance of NewsTextRecordFactory
   *
   * @return NewsTextRecordFactory instance
   */
  public static NewsTextRecordFactory getInstance() {
    if (instance == null) {
      instance = new NewsTextRecordFactory();
    }
    return instance;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public TextRecord fromJson(String jsonInput) {
    return NewsTextRecord.fromJson(jsonInput);
  }
}
