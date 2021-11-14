package com.example.calculator_with_variable;

public enum Operator {
  ADD("+"), SUBTRACT("-"), MULTIPLY("*"), DIVIDE("/");
  private String name;

  Operator(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
