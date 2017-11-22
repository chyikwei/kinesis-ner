package com.chyikwei.app.model;

import java.util.UUID;

/**
 * Factory to create NewsMultiFieldEntities
 */
public class BaseMultiFieldEntitiesFactory implements MultiFieldEntitiesFactory {

  private static BaseMultiFieldEntitiesFactory instance;

  private BaseMultiFieldEntitiesFactory() {
  }

  /**
   * get instance of BaseMultiFieldEntitiesFactory
   *
   * @return BaseMultiFieldEntitiesFactory instance
   */
  public static BaseMultiFieldEntitiesFactory getInstance() {
    if (instance == null) {
      instance = new BaseMultiFieldEntitiesFactory();
    }
    return instance;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MultiFieldEntities newObject(UUID uuid) {
    return new BaseMultiFieldEntities(uuid);
  }
}
