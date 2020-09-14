package org.asri.expression.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public abstract class ExpressionUtilities {
  private ExpressionUtilities() {
    // Do Nothing
  }

  public static boolean isBlank(String value) {
    return !isNotBlank(value);
  }

  public static boolean isNotBlank(String value) {
    return Objects.nonNull(value) && value.trim().length() > 0;
  }

  public static int size(Collection<?> collection) {
    if (Objects.isNull(collection)) {
      return 0;
    } else {
      return collection.size();
    }
  }

  public static int size(Map<?, ?> collection) {
    if (Objects.isNull(collection)) {
      return 0;
    } else {
      return collection.size();
    }
  }
}
