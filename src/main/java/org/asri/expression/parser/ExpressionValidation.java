package org.asri.expression.parser;

import org.asri.expression.beans.ExpressionConstants;
import org.asri.expression.utils.ExpressionUtilities;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User: asrivastava
 * Date: 02/04/20 5:53 PM
 */
public abstract class ExpressionValidation {
  public static final String SPACE_REPLACEMENT = "____SPACE____";
  private static final Map<String, String> OPERATION_MAP;
  private static final Map<String, String> REPLACEMENT_MAP;
  private static final Map<String, String> BRACKET_MAPPINGS;

  static List<String> OPENING_BRACKETS;
  static List<String> CLOSING_BRACKETS;
  static final List<String> OPERATORS;

  static {
    Map<String, String> temporaryMap = new HashMap<>();
    temporaryMap.put("&&", "AND");
    temporaryMap.put("||", "OR");
    temporaryMap.put(SPACE_REPLACEMENT, " ");
    OPERATION_MAP = Collections.unmodifiableMap(temporaryMap);

    temporaryMap = new HashMap<>();
    temporaryMap.put("AND", "&&");
    temporaryMap.put("OR", "||");
    temporaryMap.put("\\-", " - ");
    temporaryMap.put("\\*", " * ");
    temporaryMap.put("\\/", " / ");
    temporaryMap.put("\\^", " ^ ");
    temporaryMap.put("\\+", " + ");
    temporaryMap.put("\\(", " ( ");
    temporaryMap.put("\\)", " ) ");
    temporaryMap.put("\\]", " ] ");
    temporaryMap.put("\\[", " [ ");
    temporaryMap.put("\\}", " } ");
    temporaryMap.put("\\{", " { ");
    REPLACEMENT_MAP = Collections.unmodifiableMap(temporaryMap);

    temporaryMap = new HashMap<>();
    temporaryMap.put("}", "{");
    temporaryMap.put("]", "[");
    temporaryMap.put(")", "(");
    BRACKET_MAPPINGS = Collections.unmodifiableMap(temporaryMap);
    OPENING_BRACKETS = Collections.unmodifiableList(new ArrayList<>(BRACKET_MAPPINGS.values()));
    CLOSING_BRACKETS = Collections.unmodifiableList(new ArrayList<>(BRACKET_MAPPINGS.keySet()));

    OPERATORS = Collections.unmodifiableList(Arrays.asList("+", "-", "*", "/", "^", "&&", "||"));
  }

  private static String getCorrespondingOpen(String bracket) {
    return BRACKET_MAPPINGS.get(bracket);
  }

  /**
   * Prepare string for transformation from expression to tree
   *
   * @param expression Input Expression
   * @return Transformed Expression
   */
  static String prepare(String expression) {
    AtomicReference<String> expressionReference = new AtomicReference<>(expression);
    REPLACEMENT_MAP.forEach((key, value) -> expressionReference.set(expressionReference.get().replaceAll(key, value)));
    return expressionReference.get().replaceAll("\\s+", SPACE_REPLACEMENT);
  }

  /**
   * Method to validate brackets.
   *
   * @param expression Input Expression
   * @return Flag to denote validity.
   */
  static boolean validate(String expression) {
    if (ExpressionUtilities.isBlank(expression)) {
      return false;
    }

    Stack<String> characters = new Stack<>();
    AtomicBoolean valid = new AtomicBoolean(true);
    for (String character : expression.split(ExpressionConstants.EMPTY)) {
      if (OPENING_BRACKETS.contains(character)) {
        characters.add(character);
      } else if (CLOSING_BRACKETS.contains(character)) {
        if (!characters.isEmpty() && characters.peek().equals(getCorrespondingOpen(character))) {
          characters.pop();
        } else {
          valid.set(false);
          break;
        }
      }
    }
    return valid.get() && characters.isEmpty();
  }

  public static String actualValue(String character) {
    return OPERATION_MAP.getOrDefault(character, character);
  }

  static boolean isOperand(String character) {
    return !OPERATORS.contains(character);
  }
}
