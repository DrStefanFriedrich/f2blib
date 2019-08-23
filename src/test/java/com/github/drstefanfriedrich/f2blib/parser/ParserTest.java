/*****************************************************************************
 *
 * Copyright (c) 2019 Stefan Friedrich
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package com.github.drstefanfriedrich.f2blib.parser;

import com.github.drstefanfriedrich.f2blib.ast.*;
import org.junit.Test;

public class ParserTest extends AbstractParserTest {

    @Test
    public void abs() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := +abs(p_2);\n" +
                END, new Pos(new Abs(new Parameter(1))));
    }

    @Test
    public void addition() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := 1 + 2 + 3;\n" +
                END, new Addition(new Addition(new Int(1), new Int(2)), new Int(3)));
    }

    @Test
    public void additionAndMultiplication() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := 4 + 5 * 6;\n" +
                END, new Addition(new Int(4), new Multiplication(new Int(5), new Int(6))));
    }

    @Test
    public void additionAndMultiplicationTheOtherWayAround() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := 4 * 5 + 6;\n" +
                END, new Addition(new Multiplication(new Int(4), new Int(5)), new Int(6)));
    }

    @Test
    public void additionAndNeg() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := -1 + 2 + 3;\n" +
                END, new Addition(new Addition(new Neg(new Int(1)), new Int(2)), new Int(3)));
    }

    @Test
    public void arccos() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := arccos(3);\n" +
                END, new Arccos(new Int(3)));
    }

    @Test
    public void arcosh() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := arcosh(3.9*pi);\n" +
                END, new Arcosh(new Multiplication(new Doub(3.9), Constant.PI)));
    }

    @Test
    public void arcsin() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := -arcsin(x_1)^arccos(x_2);\n" +
                END, new Neg(new Power(new Arcsin(new Variable(0)), new Arccos(new Variable(1)))));
    }

    @Test
    public void arctan() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := +arctan(x_1);\n" +
                END, new Pos(new Arctan(new Variable(0))));
    }

    @Test
    public void arctanAndFaculty() {
        /* Does not compile: new Faculty(new Arctan(new Variable(0))) */
        assertWrongAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := arctan(x_1)!;\n" +
                END, "line: 3, column: 22, message: extraneous input '!' expecting ';'");
    }

    @Test
    public void arsinh() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := arsinh(p_10);\n" +
                END, new Arsinh(new Parameter(9)));
    }

    @Test
    public void artanh() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := artanh(3^(4!));\n" +
                END, new Artanh(new Power(new Int(3), new Parenthesis(new Faculty(new Int(4))))));
    }

    @Test
    public void binomial() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := binomial(5,4);\n" +
                END, new Binomial(new Int(5), new Int(4)));
    }

    @Test
    public void binomialAnsSin() {
        /* Does not compile: new Binomial(new Int(5), new Sin(new Doub(1.2))) */
        assertWrongAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := binomial(5,sin(1.2));\n" +
                END, "line: 3, column: 22, message: extraneous input 'sin' expecting");
    }

    @Test
    public void constant() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := boltzmann;\n" +
                END, Constant.BOLTZMANN);
    }

    @Test
    public void cos() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := cos(p_2);\n" +
                END, new Cos(new Parameter(1)));
    }

    @Test
    public void cosh() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := cosh(cosh(cosh(e)));\n" +
                END, new Cosh(new Cosh(new Cosh(Constant.E))));
    }

    @Test
    public void division() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := x_1 / x_2 / x_3;\n" +
                END, new Division(new Division(new Variable(0), new Variable(1)), new Variable(2)));
    }

    @Test
    public void divisionAndParenthesis() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := x_1 / (x_2 / x_3);\n" +
                END, new Division(new Variable(0), new Parenthesis(new Division(new Variable(1), new Variable(2)))));
    }

    @Test
    public void divisionAndParenthesisTheOtherWayAround() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := (x_1 / x_2) / x_3;\n" +
                END, new Division(new Parenthesis(new Division(new Variable(0), new Variable(1))), new Variable(2)));
    }

    @Test
    public void doub() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := 2.9^.5f;\n" +
                END, new Power(new Doub(2.9), new Doub(0.5)));
    }

    @Test
    public void doubWithExponentialNotation() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := 1.25e5;\n" +
                END, new Doub(125000d));
    }

    @Test
    public void exp() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := exp(1);\n" +
                END, new Exp(new Int(1)));
    }

    @Test
    public void faculty() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := 3!^(4!);\n" +
                END, new Power(new Faculty(new Int(3)), new Parenthesis(new Faculty(new Int(4)))));
    }

    @Test
    public void facultyWithDoubleParenthesis() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := (3!)^(4!);\n" +
                END, new Power(new Parenthesis(new Faculty(new Int(3))), new Parenthesis(new Faculty(new Int(4)))));
    }

    @Test
    public void facultyAndPower() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := 3!^4!!;\n" +
                END, new Faculty(new Faculty(new Power(new Faculty(new Int(3)), new Int(4)))));
    }

    @Test
    public void negInteger() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := -123;\n" +
                END, new Neg(new Int(123)));
    }

    @Test
    public void posInteger() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := +123;\n" +
                END, new Pos(new Int(123)));
    }

    @Test
    public void integerWithoutSign() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := 123;\n" +
                END, new Int(123));
    }

    @Test
    public void ln() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := ln(x_1);\n" +
                END, new Ln(new Variable(0)));
    }

    @Test
    public void multiplication() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := x_1 * x_2;\n" +
                END, new Multiplication(new Variable(0), new Variable(1)));
    }

    @Test
    public void multiplicationTwoTimes() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := x_1 * x_2 * x_3;\n" +
                END, new Multiplication(new Multiplication(new Variable(0), new Variable(1)), new Variable(2)));
    }

    @Test
    public void negAndPos() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := -+--x_1;\n" +
                END, new Neg(new Pos(new Neg(new Neg(new Variable(0))))));
    }

    @Test
    public void negAndPosWithParenthesis() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := -(+(--x_1));\n" +
                END, new Neg(new Parenthesis(new Pos(new Parenthesis(new Neg(new Neg(new Variable(0))))))));
    }

    @Test
    public void parameter() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := +p_1;\n" +
                END, new Pos(new Parameter(0)));
    }

    @Test
    public void wrongParameter() {
        assertWrongAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := +p_0;\n" +
                END, "line: 3, column: 14, message: missing '{' at '0', offendingSymbol");
    }

    @Test
    public void negativeParameter() {
        assertWrongAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := p_-1;\n" +
                END, "line: 3, column: 13, message: missing '{' at '-', offendingSymbol");
    }

    @Test
    public void parenthesis() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := (sin(cosh(cos((x_1)))));\n" +
                END, new Parenthesis(new Sin(new Cosh(new Cos(new Parenthesis(new Variable(0)))))));
    }

    @Test
    public void pos() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := +x_1;\n" +
                END, new Pos(new Variable(0)));
    }

    @Test
    public void power() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := +x_1^x_2;\n" +
                END, new Pos(new Power(new Variable(0), new Variable(1))));
    }

    @Test
    public void round() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := round(p_2);\n" +
                END, new Round(new Parameter(1)));
    }

    @Test
    public void sin() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := sin(+x_1);\n" +
                END, new Sin(new Pos(new Variable(0))));
    }

    @Test
    public void sinh() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := sinh(++x_1);\n" +
                END, new Sinh(new Pos(new Pos(new Variable(0)))));
    }

    @Test
    public void sqrt() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := sqrt(x_1);\n" +
                END, new Sqrt(new Variable(0)));
    }

    @Test
    public void subtraction() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := x_1-p_1;\n" +
                END, new Subtraction(new Variable(0), new Parameter(0)));
    }

    @Test
    public void subtractionWithNeg() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := -x_1-p_1;\n" +
                END, new Subtraction(new Neg(new Variable(0)), new Parameter(0)));
    }

    @Test
    public void subtractionWithDoubleNeg() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := -x_1--p_1;\n" +
                END, new Subtraction(new Neg(new Variable(0)), new Neg(new Parameter(0))));
    }

    @Test
    public void subtractionWithDoubleNegAndParenthesis() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := -x_1-(-p_1);\n" +
                END, new Subtraction(new Neg(new Variable(0)), new Parenthesis(new Neg(new Parameter(0)))));
    }

    @Test
    public void subtractionAndAddition() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := x_1 - x_2 + x_3;\n" +
                END, new Subtraction(new Variable(0), new Addition(new Variable(1), new Variable(2))));
    }

    @Test
    public void subtractionAndAdditionTheOtherWayAround() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := x_1 + x_2 - x_3;\n" +
                END, new Subtraction(new Addition(new Variable(0), new Variable(1)), new Variable(2)));
    }

    @Test
    public void tan() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := tan(arctan(x_1));\n" +
                END, new Tan(new Arctan(new Variable(0))));
    }

    @Test
    public void tanh() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := artanh(tanh(x_1));\n" +
                END, new Artanh(new Tanh(new Variable(0))));
    }

    @Test
    public void forLoopAndForVar() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "for i from round(p_1) to round(p_2) step round(p_3);" +
                BEGIN +
                "    f_1 := i;\n" +
                END +
                END, new FunctionDefinition("a.b.c.Xyz", new FunctionBody(new ForLoop("i",
                new Round(new Parameter(0)), new Round(new Parameter(1)), new Round(new Parameter(2)),
                new FunctionsWrapper(new Function(0, new IntVar("i")))))));
    }

    @Test
    public void normalFunctionAndForLoop() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := x_1;\n" +
                END +
                BEGIN +
                "for i from round(p_1) to round(p_2) step round(p_3);" +
                BEGIN +
                "    f_1 := x_1;\n" +
                END +
                END, new FunctionDefinition("a.b.c.Xyz", new FunctionBody(new FunctionsWrapper(new Function(0, new Variable(0))))));
    }

    @Test
    public void forLoopAndNormalFunctionOutsideForLoop() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "for i from 0 to 10 step 3;" +
                BEGIN +
                "    f_1 := x_1;\n" +
                END +
                END +
                BEGIN +
                "    f_1 := x_1;\n" +
                END, new FunctionDefinition("a.b.c.Xyz", new FunctionBody(new ForLoop("i", new Int(0), new Int(10), new Int(3),
                new FunctionsWrapper(new Function(0, new Variable(0)))))));
    }

    @Test
    public void markovShift() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := artanh(tanh(x_1));\n" +
                "    markov_shift(0);\n" +
                END, new FunctionDefinition("a.b.c.Xyz", new FunctionBody(new FunctionsWrapper(
                new MarkovShift(new Int(0)), new Function(0, new Artanh(new Tanh(new Variable(0))))))));
    }

    @Test
    public void markovShiftBeforeFunctionDefinition() {
        assertWrongGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    markov_shift(0);\n" +
                "    f_1 := artanh(tanh(x_1));\n" +
                END);
    }

    @Test
    public void markovShiftNonInteger() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := artanh(tanh(x_1));\n" +
                "    markov_shift(round(p_1));\n" +
                END);
    }

    @Test
    public void forLoopAndMarkovShift() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "for i from 1 to 2 step 3;" +
                BEGIN +
                "    f_1 := i;\n" +
                "    markov_shift(1);\n" +
                END +
                END, new FunctionDefinition("a.b.c.Xyz", new FunctionBody(new ForLoop("i", new Int(1), new Int(2), new Int(3),
                new FunctionsWrapper(new MarkovShift(new Int(1)), new Function(0, new IntVar("i")))))));
    }

    @Test
    public void parameterWithIntExpression() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := p_{i!};\n" +
                END, new Parameter(new Faculty(new IntVar("i"))));
    }

    @Test
    public void variableWithIntExpression() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := x_{i!};\n" +
                END, new Variable(new Faculty(new IntVar("i"))));
    }

    @Test
    public void mixedIntAndDouble1() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := 3 * 4 + sin(x_1);\n" +
                END, new Addition(new Multiplication(new Int(3), new Int(4)), new Sin(new Variable(0))));
    }

    @Test
    public void mixedIntAndDouble2() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := sin(x_1) + 3 * 4;\n" +
                END, new Addition(new Sin(new Variable(0)), new Multiplication(new Int(3), new Int(4))));
    }

    @Test
    public void facultyAndMultiplication() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := 3 * 3 ! * 3;\n" +
                END, new Multiplication(new Int(3), new Multiplication(new Faculty(new Int(3)), new Int(3))));
    }

    @Test
    public void powerWithIntExpressions() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := 3!^4!!;\n" +
                END, new Faculty(new Faculty(new Power(new Faculty(new Int(3)), new Int(4)))));
    }

    @Test
    public void powerWithDoubleExpressions() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := sin(x_1)^cos(x_2);\n" +
                END, new Power(new Sin(new Variable(0)), new Cos(new Variable(1))));
    }

    @Test
    public void powerWithDoubleAndIntExpressions() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := 3 + 4^sin(x_1) + 5;\n" +
                END, new Addition(new Addition(new Int(3), new Power(new Int(4), new Sin(new Variable(0)))), new Int(5)));
    }

    @Test
    public void sum() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := sum(u, u, 1, 10);\n" +
                END, new Sum(new IntVar("u"),"u", new Int(1), new Int(10)));
    }

    @Test
    public void prod() {
        assertAST("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := prod(u, u, 1, 10);\n" +
                END, new Prod(new IntVar("u"),"u", new Int(1), new Int(10)));
    }

}
