package org.asri.expression.utils;

import org.asri.expression.error.ExpressionParserError;
import org.asri.expression.error.ExpressionParserException;

import java.util.Objects;

public abstract class ExpressionPreChecks {
  private ExpressionPreChecks() {
    // Do Nothing
  }

  public static void assertTrue(boolean condition, ExpressionParserError error) {
    if (!condition) {
      throw new ExpressionParserException(error);
    }
  }

  public static void assertFalse(boolean condition, ExpressionParserError error) {
    assertTrue(!condition, error);
  }

  public static void assertNotNull(Object value, ExpressionParserError error) {
    assertTrue(Objects.nonNull(value), error);
  }

  public static void assertNull(Object value, ExpressionParserError error) {
    assertTrue(Objects.isNull(value), error);
  }
}
