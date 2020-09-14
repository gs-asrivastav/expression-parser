package org.asri.expression.error;

public class ExpressionParserException extends RuntimeException {
  public ExpressionParserException(String message) {
    super(message);
  }

  public ExpressionParserException(ExpressionParserError error) {
    super(error.getHint());
  }
}
