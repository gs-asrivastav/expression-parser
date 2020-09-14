package org.asri.expression.beans;

import org.asri.expression.parser.ExpressionValidation;

import java.io.Serializable;
import java.util.Objects;

import static org.asri.expression.beans.ExpressionConstants.SPACE;

public class ExpressionNode implements Serializable {
  private static final long serialVersionUID = 8859454197219386477L;
  private String value;
  private ExpressionNode left;
  private ExpressionNode right;

  public ExpressionNode(String value) {
    this.value = value;
  }

  public boolean isLeafNode() {
    return Objects.isNull(left) && Objects.isNull(right);
  }

  public String getValue() {
    return ExpressionValidation.actualValue(this.value);
  }

  public void setValue(String value) {
    this.value = value;
  }

  public ExpressionNode getLeft() {
    return left;
  }

  public void setLeft(ExpressionNode left) {
    this.left = left;
  }

  public ExpressionNode getRight() {
    return right;
  }

  public void setRight(ExpressionNode right) {
    this.right = right;
  }

  @Override
  public String toString() {
    StringBuilder expression = new StringBuilder();
    // Left
    if (Objects.nonNull(this.left)) {
      expression.append(this.left.toString()).append(SPACE);
    }
    // Current Node
    expression.append(this.getValue()).append(SPACE);
    // Right
    if (Objects.nonNull(this.right)) {
      expression.append(this.right.toString());
    }
    return expression.toString().trim();
  }
}
