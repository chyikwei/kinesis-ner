package com.chyikwei.app.model;

import java.util.UUID;

/**
 * Factory to create new MultiFieldEntities
 */
public interface MultiFieldEntitiesFactory {

  /**
   * Create new instance of MultiFieldEntities
   *
   * @param uuid
   * @return
   */
  public MultiFieldEntities newObject(UUID uuid);
}
