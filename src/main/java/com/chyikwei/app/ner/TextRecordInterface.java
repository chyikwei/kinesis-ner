package com.chyikwei.app.ner;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.tuple.Pair;

public interface TextRecordInterface {
  public UUID getUUID();
  public List<Pair<String, String>> getTextFields();
}
