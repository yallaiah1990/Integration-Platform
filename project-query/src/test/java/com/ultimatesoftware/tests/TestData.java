package com.ultimatesoftware.tests;

import com.ultimatesoftware.launch.mongo.fields.SequencedField;

public class TestData {

  public static final String TENANT_ID_1 = "TENANT_ID_1";
  public static final String TENANT_ID_2 = "TENANT_ID_2";
  public static final String PROJECT_ID = "PROJECT_ID";

  public static SequencedField<String>[] getTestSequencedField() {
    SequencedField<String>[] testSequencedFields = new SequencedField[1];
    testSequencedFields[0] = new SequencedField<>("sequencedValue1", 0);
    return testSequencedFields;
  }

  private TestData() {
    throw new UnsupportedOperationException();
  }
}
