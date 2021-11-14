package com.example.calculator_with_variable;

import java.util.HashMap;
import java.util.Map;

public class Interpreter {
  public final Map<String, Integer> environment;

  public Interpreter() {
    this.environment = new HashMap<>();
  }

  public int interpret(Ast.Expression expression) {
    if (expression instanceof Ast.BinaryExpression binaryExpression) {
      var lhs = interpret(binaryExpression.lhs());
      var rhs = interpret(binaryExpression.rhs());
      return switch(binaryExpression.operator()) {
        case ADD ->  lhs + rhs;
        case SUBTRACT -> lhs - rhs;
        case MULTIPLY -> lhs * rhs;
        case DIVIDE -> lhs / rhs;
      };
    } else if (expression instanceof Ast.IntegerLiteral integer) {
      return integer.value();
    } else if (expression instanceof Ast.Identifier identifier) {
      return environment.get(identifier.name());
    } else if (expression instanceof Ast.Assignment assignment) {
      int value = interpret(assignment.expression());
      environment.put(assignment.name(), value);
      return value;
    } else {
      throw new RuntimeException("not reach here");
    }
  }
}
