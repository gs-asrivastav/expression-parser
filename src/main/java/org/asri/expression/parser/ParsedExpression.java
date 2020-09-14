package org.asri.expression.parser;

import org.asri.expression.beans.ExpressionNode;
import org.asri.expression.error.ExpressionParserError;
import org.asri.expression.utils.ExpressionPreChecks;
import org.asri.expression.utils.ExpressionUtilities;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.asri.expression.parser.ExpressionValidation.*;

public class ParsedExpression {
  private Map<String, Integer> precedence;
  private Map<String, String> replacements;
  private String expression;
  private String[] split;
  private ExpressionNode node;

  ParsedExpression() {
    // Do nothing
  }

  public Map<String, Integer> getPrecedence() {
    return precedence;
  }

  public void setPrecedence(Map<String, Integer> precedence) {
    this.precedence = precedence;
  }

  public Map<String, String> getReplacements() {
    return replacements;
  }

  public void setReplacements(Map<String, String> replacements) {
    this.replacements = replacements;
  }

  public String getExpression() {
    return expression;
  }

  public void setExpression(String expression) {
    this.expression = expression;
  }

  public ExpressionNode getNode() {
    return node;
  }

  public String getFinalExpression() {
    return Arrays.stream(this.split).map(this::getReplacementIfAny)
        .collect(Collectors.joining(" ")).trim();
  }

  /**
   * Reference: https://stackoverflow.com/a/53705889
   *
   * @return String representation of the parsed expression
   */
  @Override
  public String toString() {
    StringBuilder repr = new StringBuilder();
    this.print(repr, "", this.node, expressionNode -> {
      List<ExpressionNode> nodes = new ArrayList<>(2);
      if (Objects.nonNull(expressionNode.getLeft())) {
        nodes.add(expressionNode.getLeft());
      }
      if (Objects.nonNull(expressionNode.getRight())) {
        nodes.add(expressionNode.getRight());
      }
      return nodes;
    }, this.node.isLeafNode());
    return repr.toString();
  }

  /**
   * Convert from Infix -> Postfix Expression
   * Convert from Postfix -> Expression Tree
   */
  protected ParsedExpression parse() {
    String cleanedExpression = ExpressionValidation.prepare(this.expression);
    this.split = cleanedExpression.split(ExpressionValidation.SPACE_REPLACEMENT);
    this.node = this.tree(this.postfix().toArray(new String[0]));
    return this;
  }

  private List<String> postfix() {

    List<String> postfix = new LinkedList<>();
    Stack<String> operator = new Stack<>();
    String popped;

    for (String get : this.split) {
      if (isOperand(get) && !isOpenOrClosingBrace(get)) {
        postfix.add(get);
      } else if (CLOSING_BRACKETS.contains(get)) {
        popped = operator.pop();
        while (!OPENING_BRACKETS.contains(popped)) {
          postfix.add(popped);
          popped = operator.pop();
        }
      } else {
        while (!operator.isEmpty() && !OPENING_BRACKETS.contains(get) && getPrecedence(operator.peek()) >= getPrecedence(get)) {
          postfix.add(operator.pop());
        }
        operator.push(get);
      }
    }
    // pop any remaining operator
    while (!operator.isEmpty())
      postfix.add(operator.pop());

    return postfix;
  }

  private ExpressionNode tree(String[] postfix) {
    Stack<ExpressionNode> stack = new Stack<>();
    // Traverse through every character of
    for (String character : postfix) {
      // If operand, simply push into stack
      if (isOperand(character)) {
        stack.push(new ExpressionNode(character));
      } else {
        ExpressionPreChecks.assertTrue(ExpressionUtilities.size(stack) >= 2, ExpressionParserError.INVALID_EXPRESSION);
        ExpressionNode expressionNode = new ExpressionNode(character);
        expressionNode.setRight(stack.pop());
        expressionNode.setLeft(stack.pop());
        // Add this subexpression to stack
        stack.push(expressionNode);
      }
    }
    return stack.peek();
  }

  private String getReplacementIfAny(String literal) {
    if (Objects.nonNull(this.replacements)) {
      return this.replacements.getOrDefault(literal, literal);
    }
    return literal;
  }

  private void print(StringBuilder repr, String prefix,
                     ExpressionNode node, Function<ExpressionNode, List<ExpressionNode>> getChildrenFunc,
                     boolean isTail) {
    String nodeName = node.getValue();
    String nodeConnection = isTail ? "└── " : "├── ";
    repr.append(prefix).append(nodeConnection).append(nodeName).append("\n");
    List<ExpressionNode> children = getChildrenFunc.apply(node);
    for (ExpressionNode child : children) {
      String newPrefix = prefix + (isTail ? "    " : "│   ");
      this.print(repr, newPrefix, child, getChildrenFunc, child.isLeafNode());
    }
  }

  private int getPrecedence(String op) {
    return precedence.getOrDefault(op, 0);
  }

  private static boolean isOpenOrClosingBrace(String character) {
    return OPENING_BRACKETS.contains(character) || CLOSING_BRACKETS.contains(character);
  }
}
