package com.example.calculator_with_variable;

import com.example.calculator_with_variable.Ast;
import com.example.calculator_with_variable.Interpreter;
import com.example.calculator_with_variable.Ast.Expression;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.calculator_with_variable.Ast.add;
import static com.example.calculator_with_variable.Ast.assignment;
import static com.example.calculator_with_variable.Ast.divide;
import static com.example.calculator_with_variable.Ast.identifier;
import static com.example.calculator_with_variable.Ast.integer;
import static com.example.calculator_with_variable.Ast.multiply;
import static com.example.calculator_with_variable.Ast.subtract;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

  @Test
  void testAssignment() {
    Expression e = assignment("a", add(integer(10), integer(10)));
    assertEquals(20 ,interpreter.interpret(e));
    assertEquals(20, interpreter.environment.get("a").intValue());
  }

  @Test
  public void testIdentifierAfterAssignment() {
    Expression e1 = assignment("a", add(integer(10), integer(10)));
    assertEquals(20 ,interpreter.interpret(e1));
    Expression e2 = add(identifier("a"), integer(10));
    assertEquals(30 ,interpreter.interpret(e2));
  }

  @Test
  public void testIncrement() {
    Expression e1 = assignment("a", integer(10));
    assertEquals(10 ,interpreter.interpret(e1));
    Expression e2 = assignment("a", add(identifier("a"), integer(1)));
    assertEquals(11 ,interpreter.interpret(e2));
    assertEquals(11, interpreter.environment.get("a").intValue());
  }
}
