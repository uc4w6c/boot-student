package com.example.calculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.example.calculator.Ast.add;
import static com.example.calculator.Ast.divide;
import static com.example.calculator.Ast.integer;
import static com.example.calculator.Ast.multiply;
import static com.example.calculator.Ast.subtract;
import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {
  private Interpreter interpreter = new Interpreter();

  @Test
  @DisplayName("10 + 20 = 30")
  void test10Plus20() {
    Ast.Expression e = add(integer(10), integer(20));
    assertEquals(30, interpreter.interpret(e));
  }

  @Test
  @DisplayName("10 - 20 = -10")
  void test10Minus20() {
    Ast.Expression e = subtract(integer(10), integer(20));
    assertEquals(-10, interpreter.interpret(e));
  }

  @Test
  @DisplayName("10 * 20 = 200")
  void test10Multiply20() {
    Ast.Expression e = multiply(integer(10), integer(20));
    assertEquals(200, interpreter.interpret(e));
  }

  @Test
  @DisplayName("20 / 10 = 2")
  void test20Divide10() {
    Ast.Expression e = divide(integer(20), integer(10));
    assertEquals(2, interpreter.interpret(e));
  }

  /*
  @Test
  @DisplayName("(1 + 3) * (3 + 4) = 28")
  void simpleExpressionIsParsed() {
    var expression= Parsers.expression().parse(
        Input.of("(1 + 3) * (3 + 4)")
    ).getResult();
    assertEquals(
        28,
        interpreter.interpret(expression)
    );
  }
   */
}
