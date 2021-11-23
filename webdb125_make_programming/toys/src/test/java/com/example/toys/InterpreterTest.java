package com.example.toys;

import java.util.List;
import java.util.Optional;

import com.example.toys.Ast.TopLevel;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.example.toys.Ast.Block;
import static com.example.toys.Ast.DefineFunction;
import static com.example.toys.Ast.If;
import static com.example.toys.Ast.call;
import static com.example.toys.Ast.integer;
import static com.example.toys.Ast.lessThan;
import static com.example.toys.Ast.multiply;
import static com.example.toys.Ast.subtract;
import static com.example.toys.Ast.symbol;
import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {
  private Interpreter interpreter = new Interpreter();

  @Nested
  class callMain {
    @Test
    void factorial() {
      List<TopLevel> topLevels = List.of(
        /*
         * define main() {
         *   fact(5);
         * }
         */
        DefineFunction("main", List.of(),
            Block(call("fact", integer(5)))
        ),
        /*
         * define factorial(n) {
         *   if (n < 2) {
         *     1;
         *   } else {
         *     n * fact(n - 1);
         *   }
         * }
         */
        DefineFunction("fact", List.of("n"),
            Block(
                If(
                    lessThan(symbol("n"), integer(2)),
                    integer(1),
                    Optional.of((Ast.Expression)multiply(
                        symbol("n"),
                        call("fact",
                            subtract(
                                symbol("n"), integer(1)))
                    ))
                )
            )
        )
      );
      int result = interpreter.callMain(
          new Ast.Program(topLevels)
      ).asInt().value();
      assertEquals(120, result);
    }
  }
}
