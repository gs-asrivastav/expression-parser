import org.asri.expression.beans.ExpressionConstants;
import org.asri.expression.error.ExpressionParserException;
import org.asri.expression.parser.ExpressionParserBuilder;
import org.asri.expression.parser.ParsedExpression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TestParsedExpression {
  @Test
  public void testSimpleExpression() {
    Map<String, String> replacements = new HashMap<>();
    replacements.put("C", "200");
    replacements.put("CC", "90");
    replacements.put("CCC", "1");
    replacements.put("A", "15");
    replacements.put("B", "-1");
    ParsedExpression build = ExpressionParserBuilder.getInstance()
        .withPrecedence(ExpressionConstants.DEFAULT_PRECEDENCE)
        .withExpression("(A + B) / (C + CC + CCC)")
        .withReplacements(replacements)
        .build();
    Assertions.assertEquals("( 15 + -1 ) / ( 200 + 90 + 1 )", build.getFinalExpression());
    Assertions.assertEquals("/", build.getNode().getValue());
  }

  /**
   * ( >> { >> [
   */
  @Test
  public void testMultipleBrackets() {
    ParsedExpression build = ExpressionParserBuilder.getInstance()
        .withPrecedence(ExpressionConstants.DEFAULT_PRECEDENCE)
        .withExpression("(A + B * [C + D * {E + F}])")
        .build();
    Assertions.assertEquals("*", build.getNode().getValue());
  }

  @Test
  public void testInvalidInput() {
    Assertions.assertThrows(ExpressionParserException.class, () -> ExpressionParserBuilder.getInstance()
        .withPrecedence(ExpressionConstants.DEFAULT_PRECEDENCE)
        .withExpression("A++")
        .build());
  }
}
