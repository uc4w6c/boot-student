package com.example.toys;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.example.toys.Ast.Environment;

public class Interpreter {
  private Ast.Environment variableEnvironment;
  private final Map<String, Ast.FunctionDefinition> functionEnvironment;
  public final Map<String, Integer> environment;

  public Interpreter() {
    this.variableEnvironment = newEnvironment(Optional.empty());
    this.functionEnvironment = new HashMap<>();
    this.environment = new HashMap<>();
  }

  private static Ast.Environment newEnvironment(Optional<Environment> next) {
    return new Ast.Environment(new HashMap<>(), next);
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
        case LESS_THAN -> lhs < rhs ? 1 : 0;
        case LESS_OR_EQUAL -> lhs <= rhs ? 1 : 0;
        case GREATER_THAN -> lhs > rhs ? 1 : 0;
        case GREATER_OR_EQUAL -> lhs >= rhs ? 1 : 0;
        case EQUAL_EQUAL -> lhs == rhs ? 1 : 0;
        case NOT_EQUAL -> lhs != rhs ? 1 : 0;
      };
    } else if (expression instanceof Ast.IntegerLiteral integer) {
      return integer.value();
    } else if (expression instanceof Ast.Identifier identifier) {
      return environment.get(identifier.name());
    } else if (expression instanceof Ast.Assignment assignment) {
      int value = interpret(assignment.expression());
      environment.put(assignment.name(), value);
      return value;
    } else if (expression instanceof Ast.IfExpression ifExpression) {
      int condition = interpret(
          ifExpression.condition()
      );
      if (condition != 0) {
        return interpret(ifExpression.thenClause());
      }
      else {
        var elseClauseOpt = ifExpression.elseClause();
        return elseClauseOpt.map(
            this::interpret
        ).orElse(1);
      }
    } else if (expression instanceof Ast.WhileExpression whileExpression) {
      while (true) {
        int condition = interpret(
            whileExpression.condition()
        );
        if (condition != 0) {
          interpret(whileExpression.body());
        } else {
          break;
        }
      }
      return 1;
    } else if (expression instanceof Ast.BlockExpression blockExpression) {
      int value = 0;
      for (var e : blockExpression.elements()) {
        value = interpret(e);
      }
      return value;
    } else {
      throw new RuntimeException("not reach here");
    }
  }

  public int callMain(Ast.Program program) {
    var topLevels = program.definitions();
    for (var topLevel : topLevels) {
      if (topLevel instanceof Ast.FunctionDefinition functionDefinition) {
        functionEnvironment.put(functionDefinition.name(), functionDefinition);
      } else {
        // グローバル変数の定義の処理(後述)
      }
    }
    var mainFunction = functionEnvironment.get("main");
    if (mainFunction != null) {
      return interpret(mainFunction.body());
    } else {
      throw new LanguageException("This program doesn't have main() function");
    }
  }
}
