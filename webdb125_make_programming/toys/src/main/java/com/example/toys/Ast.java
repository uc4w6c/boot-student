package com.example.toys;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Ast {
  public static BinaryExpression add(
      Expression lhs, Expression rhs
  ) {
    return new BinaryExpression(Operator.ADD, lhs, rhs);
  }

  public static BinaryExpression subtract(
      Expression lhs, Expression rhs
  ) {
    return new BinaryExpression(Operator.SUBTRACT, lhs, rhs);
  }

  public static BinaryExpression multiply(
      Expression lhs, Expression rhs
  ) {
    return new BinaryExpression(Operator.MULTIPLY, lhs, rhs);
  }

  public static BinaryExpression divide(
      Expression lhs, Expression rhs
  ) {
    return new BinaryExpression(Operator.DIVIDE, lhs, rhs);
  }

  public static IntegerLiteral integer(
      int value
  ) {
    return new IntegerLiteral(value);
  }

  public static Identifier identifier(String name) {
    return new Identifier(name);
  }

  public static Assignment assignment(
      String name, Expression expression
  ) {
    return new Assignment(name, expression);
  }

  public static BlockExpression Block(
      Expression... elements
  ) {
    return new BlockExpression(
        Arrays.asList(elements)
    );
  }

  public static WhileExpression While(
      Expression condition, Expression body
  ) {
    return new WhileExpression(condition, body);
  }

  public static IfExpression If(
      Expression condition,
      Expression thenClause,
      Optional<Expression> elseClause
  ) {
    return new IfExpression(
        condition,
        thenClause,
        elseClause
    );
  }

  public static IfExpression If(
      Expression condition,
      Expression thenClause
  ) {
    return If(condition, thenClause, Optional.empty());
  }

  /** Expression */
  sealed public interface Expression permits
      BinaryExpression, Assignment, 
      IntegerLiteral, Identifier,
      BlockExpression, WhileExpression,
      IfExpression, FunctionCall {}

  public static final record BinaryExpression(
      Operator operator, Expression lhs, Expression rhs
  ) implements Expression {}

  public static final record IntegerLiteral(
      int value
  ) implements Expression {}

  public static final record Assignment(
      String name, Expression expression
  ) implements Expression {}

  public static final record Identifier(
      String name
  ) implements Expression {}

  public static final record BlockExpression(
      List<Expression> elements
  ) implements Expression {}

  public static final record WhileExpression(
      Expression condition, Expression body
  ) implements Expression {}

  public static final record IfExpression(
      Expression condition,
      Expression thenClause,
      Optional<Expression> elseClause
  ) implements Expression {}

  public static final record FunctionCall(
      String name, List<Expression> args
  ) implements Expression {}

  /** TopLevel */
  sealed public interface TopLevel permits
      GlobalVariableDefinition, FunctionDefinition {}

  public static final record GlobalVariableDefinition(
      String name, Expression expression
  ) implements TopLevel {}

  public static final record FunctionDefinition(
      String name, List<String> args, Expression body
  ) implements TopLevel {}

  public final static record Program(List<TopLevel> definitions) {}

  /** Environment */
  public static final record Environment(
      Map<String, Integer> bindings,
      Optional<Environment> next
  ) {
    public Optional<Map<String, Integer>> findBinding(String name) {
      if (bindings.get(name) != null) {
        return Optional.of(bindings);
      }
      if (next.isPresent()) {
        return next.get().findBinding(name);
      } else {
        return Optional.empty();
      }
    }
  }
}
