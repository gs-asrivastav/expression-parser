package org.asri.expression.error;

public enum ExpressionParserError {
  PRECEDENCE_MAP_EMPTY("You have to define the priority of each operator that you except in an expression"),
  EXPRESSION_EMPTY("You cannot expect us to parse an empty/null expression"),
  INVALID_EXPRESSION("Failed to parse the input expression, please re-check your expression");

  private final String hint;

  ExpressionParserError(String hint) {
    this.hint = hint;
  }

  public String getHint() {
    return hint;
  }
}
