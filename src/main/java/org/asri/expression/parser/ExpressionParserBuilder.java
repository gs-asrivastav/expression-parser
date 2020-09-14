package org.asri.expression.parser;

import org.asri.expression.beans.ExpressionConstants;
import org.asri.expression.error.ExpressionParserError;
import org.asri.expression.utils.ExpressionPreChecks;
import org.asri.expression.utils.ExpressionUtilities;

import java.util.Map;
import java.util.Objects;

public class ExpressionParserBuilder {
  private final ParsedExpression parser;

  private ExpressionParserBuilder() {
    // Do Nothing
    this.parser = new ParsedExpression();
  }

  /**
   * Provide an input map of priority of the different operators that would
   * be encountered in the expression. If you do not see the need for it, you
   * can use the defaults as provided {@link org.asri.expression.beans.ExpressionConstants}
   *
   * @param precedence Input Priority Map
   * @return Builder Instance
   */
  public ExpressionParserBuilder withPrecedence(Map<String, Integer> precedence) {
    this.parser.setPrecedence(precedence);
    return this;
  }

  /**
   * You might encounter a scenario where, a particular literal might have to be
   * replace with another literal.
   * For instance, in an expression <code>(A * B) / (A - B)</code> you might want to
   * transform into <code>(10 * 4) / (10 - 4)</code> in which base your input can be a map of
   * A -> 10
   * B -> 4
   *
   * @param replacements Map of replacements needed (Optional)
   * @return Builder Instance
   */
  public ExpressionParserBuilder withReplacements(Map<String, String> replacements) {
    this.parser.setReplacements(replacements);
    return this;
  }

  /**
   * Input Expression
   *
   * @param expression Expression
   * @return Builder Instance
   */
  public ExpressionParserBuilder withExpression(String expression) {
    this.parser.setExpression(expression);
    return this;
  }

  /**
   * Main calling function, this triggers the actual parsing
   * and transformation method
   *
   * @return ParsedExpression {@link ParsedExpression}
   */
  public ParsedExpression build() {
    return this.preprocess().validate().parser.parse();
  }

  private ExpressionParserBuilder preprocess() {
    if (Objects.isNull(this.parser.getPrecedence())) {
      this.parser.setPrecedence(ExpressionConstants.DEFAULT_PRECEDENCE);
    }
    return this;
  }

  private ExpressionParserBuilder validate() {
    ExpressionPreChecks.assertTrue(ExpressionUtilities.size(this.parser.getPrecedence()) > 0, ExpressionParserError.PRECEDENCE_MAP_EMPTY);
    ExpressionPreChecks.assertTrue(ExpressionUtilities.isNotBlank(this.parser.getExpression()), ExpressionParserError.EXPRESSION_EMPTY);
    ExpressionPreChecks.assertTrue(ExpressionValidation.validate(this.parser.getExpression()), ExpressionParserError.INVALID_EXPRESSION);
    return this;
  }

  public static ExpressionParserBuilder getInstance() {
    return new ExpressionParserBuilder();
  }
}
