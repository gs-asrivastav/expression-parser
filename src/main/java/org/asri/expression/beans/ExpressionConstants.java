package org.asri.expression.beans;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class ExpressionConstants {
  public static final String SPACE = " ";
  public static final String EMPTY = "";
  public static final Map<String, Integer> DEFAULT_PRECEDENCE;

  static {
    DEFAULT_PRECEDENCE = Collections.unmodifiableMap(getDefaultPrecedenceMap());
  }

  private static Map<String, Integer> getDefaultPrecedenceMap() {
    Map<String, Integer> precedence = new HashMap<>();
    precedence.put("(", 1);
    precedence.put(")", 1);

    precedence.put("{", 2);
    precedence.put("}", 2);

    precedence.put("[", 3);
    precedence.put("]", 3);

    precedence.put("^", 4);
    precedence.put("&&", 4);
    precedence.put("||", 4);

    precedence.put("/", 5);
    precedence.put("*", 6);
    precedence.put("+", 7);
    precedence.put("-", 8);

    return precedence;
  }
}
