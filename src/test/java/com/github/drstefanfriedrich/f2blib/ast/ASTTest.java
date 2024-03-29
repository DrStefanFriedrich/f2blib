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

package com.github.drstefanfriedrich.f2blib.ast;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for abstract syntax tree classes.
 */
@RunWith(Enclosed.class)
public class ASTTest {

    private ASTTest() {
    }

    public static final String FUNCTION_NAME = "MyFunc";

    public static class ToStringTest {

        @Test
        public void functionDefinition() {

            FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME,
                    new Sin(new Multiplication(Constant.PI, new Variable(0))));

            assertThat(fd.toString(), is("FunctionDefinition{name=MyFunc, functionBody=FunctionBody{" +
                    "functionsWrapper=FunctionsWrapper{functions=[Function{index=0, expression=Sin{expression=" +
                    "Multiplication{left=PI, right=Variable{index=0, indexExpression=null}}}}], auxiliaryVariables=[], " +
                    "markovShift=Optional.empty}, forLoop=null}}"));
        }

        @Test
        public void abs() {

            assertThat(new Abs(new Int(3)).toString(), is("Abs{expression=Int{value=3}}"));
            assertThat(new Abs(new Sin(new Variable(1))).toString(),
                    is("Abs{expression=Sin{expression=Variable{index=1, indexExpression=null}}}"));
        }

        @Test
        public void addition() {

            assertThat(new Addition(new Int(3), new Variable(0)).toString(),
                    is("Addition{left=Int{value=3}, right=Variable{index=0, indexExpression=null}}"));
            assertThat(new Addition(new Addition(new Int(3), new Parameter(3)), Constant.E).toString(),
                    is("Addition{left=Addition{left=Int{value=3}, right=Parameter{index=3, " +
                            "indexExpression=null}}, right=E}"));
        }

        @Test
        public void arccos() {

            assertThat(new Arccos(new Int(3)).toString(), is("Arccos{expression=Int{value=3}}"));
            assertThat(new Arccos(new Cos(new Variable(1))).toString(),
                    is("Arccos{expression=Cos{expression=Variable{index=1, indexExpression=null}}}"));
        }

        @Test
        public void arcosh() {

            assertThat(new Arcosh(new Multiplication(new Variable(0), new Variable(0))).toString(),
                    is("Arcosh{expression=Multiplication{left=Variable{index=0, indexExpression=null}, " +
                            "right=Variable{index=0, indexExpression=null}}}"));
            assertThat(new Arcosh(new Sin(Constant.PI)).toString(), is("Arcosh{expression=Sin{expression=PI}}"));
        }

        @Test
        public void arcsin() {

            assertThat(new Arcsin(new Arcsin(new Doub(3.99))).toString(),
                    is("Arcsin{expression=Arcsin{expression=Doub{value=3.99}}}"));
            assertThat(new Division(new Int(1), new Arcsin(new Variable(1))).toString(),
                    is("Division{left=Int{value=1}, right=Arcsin{expression=Variable{index=1, indexExpression=null}}}"));
        }

        @Test
        public void arctan() {

            assertThat(new Arctan(new Variable(0)).toString(), is("Arctan{expression=Variable{index=0, " +
                    "indexExpression=null}}"));
            assertThat(new Arctan(new Variable(1)).toString(), is("Arctan{expression=Variable{index=1, " +
                    "indexExpression=null}}"));
        }

        @Test
        public void arsinh() {

            assertThat(new Arsinh(new Exp(new Int(3))).toString(),
                    is("Arsinh{expression=Exp{expression=Int{value=3}}}"));
            assertThat(new Arsinh(new Tan(new Variable(1))).toString(),
                    is("Arsinh{expression=Tan{expression=Variable{index=1, indexExpression=null}}}"));
        }

        @Test
        public void artanh() {

            assertThat(new Artanh(new Doub(4.88)).toString(), is("Artanh{expression=Doub{value=4.88}}"));
            assertThat(new Sin(new Artanh(new Sin(new Variable(1)))).toString(),
                    is("Sin{expression=Artanh{expression=Sin{expression=Variable{index=1, indexExpression=null}}}}"));
        }

        @Test
        public void binomial() {

            assertThat(new Binomial(new Int(4), new Int(3)).toString(),
                    is("Binomial{n=Int{value=4}, k=Int{value=3}}"));
            assertThat(new Faculty(new Binomial(new Int(4), new Int(3))).toString(),
                    is("Faculty{intExpression=Binomial{n=Int{value=4}, k=Int{value=3}}}"));
        }

        @Test
        public void constant() {

            assertThat(Constant.E.toString(), is("E"));
            assertThat(Constant.BOLTZMANN.toString(), is("BOLTZMANN"));
        }

        @Test
        public void cos() {

            assertThat(new Cos(new Int(3)).toString(), is("Cos{expression=Int{value=3}}"));
            assertThat(new Cos(new Sin(new Variable(1))).toString(),
                    is("Cos{expression=Sin{expression=Variable{index=1, indexExpression=null}}}"));
        }

        @Test
        public void cosh() {

            assertThat(new Cosh(new Int(3)).toString(), is("Cosh{expression=Int{value=3}}"));
            assertThat(new Abs(new Cosh(new Variable(1))).toString(),
                    is("Abs{expression=Cosh{expression=Variable{index=1, indexExpression=null}}}"));
        }

        @Test
        public void division() {

            assertThat(new Division(new Parameter(0), new Parameter(1)).toString(),
                    is("Division{left=Parameter{index=0, indexExpression=null}, right=Parameter{index=1, indexExpression=null}}"));
            assertThat(new Round(new Division(new Variable(0), Constant.PI)).toString(),
                    is("Round{expression=Division{left=Variable{index=0, indexExpression=null}, right=PI}}"));
        }

        @Test
        public void doub() {

            assertThat(new Doub(1.234).toString(), is("Doub{value=1.234}"));
            assertThat(new Abs(new Doub(-2.2)).toString(), is("Abs{expression=Doub{value=-2.2}}"));
        }

        @Test
        public void exp() {

            assertThat(new Exp(new Int(3)).toString(), is("Exp{expression=Int{value=3}}"));
            assertThat(new Abs(new Exp(new Variable(1))).toString(),
                    is("Abs{expression=Exp{expression=Variable{index=1, indexExpression=null}}}"));
        }

        @Test
        public void faculty() {

            assertThat(new Faculty(new Round(new Pos(new Parameter(0)))).toString(),
                    is("Faculty{intExpression=Round{expression=Pos{expression=Parameter{index=0, indexExpression=null}}}}"));
            assertThat(new Abs(new Faculty(new Int(1))).toString(),
                    is("Abs{expression=Faculty{intExpression=Int{value=1}}}"));
        }

        @Test
        public void ln() {

            assertThat(new Ln(new Int(3)).toString(), is("Ln{expression=Int{value=3}}"));
            assertThat(new Ln(new Ln(new Variable(1))).toString(),
                    is("Ln{expression=Ln{expression=Variable{index=1, indexExpression=null}}}"));
        }

        @Test
        public void multiplication() {

            assertThat(new Multiplication(new Multiplication(new Variable(0), new Parameter(0)),
                            new Multiplication(new Variable(1), new Parameter(1))).toString(),
                    is("Multiplication{left=Multiplication{left=Variable{index=0, indexExpression=null}, " +
                            "right=Parameter{index=0, indexExpression=null}}, right=Multiplication{left=" +
                            "Variable{index=1, indexExpression=null}, right=Parameter{index=1, indexExpression=null}}}"));
            assertThat(new Tanh(new Multiplication(new Variable(1), Constant.E)).toString(),
                    is("Tanh{expression=Multiplication{left=Variable{index=1, indexExpression=null}, right=E}}"));
        }

        @Test
        public void multiplicationWithIntExpressions() {

            assertThat(new Multiplication(new Int(3), new Faculty(new Int(4))).toString(),
                    is("Multiplication{left=Int{value=3}, right=Faculty{intExpression=Int{value=4}}}"));
        }

        @Test
        public void neg() {

            assertThat(new Abs(new Neg(new Int(-3))).toString(),
                    is("Abs{expression=Neg{expression=Int{value=-3}}}"));
            assertThat(new Neg(new Sin(new Variable(1))).toString(),
                    is("Neg{expression=Sin{expression=Variable{index=1, indexExpression=null}}}"));
        }

        @Test
        public void pos() {

            assertThat(new Neg(new Pos(new Int(3))).toString(),
                    is("Neg{expression=Pos{expression=Int{value=3}}}"));
            assertThat(new Pos(new Neg(new Int(3))).toString(),
                    is("Pos{expression=Neg{expression=Int{value=3}}}"));
        }

        @Test
        public void parenthesis() {

            assertThat(new Parenthesis(new Parenthesis(new Int(3))).toString(),
                    is("Parenthesis{expression=Parenthesis{expression=Int{value=3}}}"));
            assertThat(new Abs(new Parenthesis(new Variable(1))).toString(),
                    is("Abs{expression=Parenthesis{expression=Variable{index=1, indexExpression=null}}}"));
        }

        @Test
        public void power() {

            assertThat(new Power(new Variable(0), new Parameter(0)).toString(),
                    is("Power{left=Variable{index=0, indexExpression=null}, right=Parameter{index=0, " +
                            "indexExpression=null}}"));
            assertThat(new Abs(new Power(new Variable(0), new Parameter(0))).toString(),
                    is("Abs{expression=Power{left=Variable{index=0, indexExpression=null}, right=" +
                            "Parameter{index=0, indexExpression=null}}}"));
        }

        @Test
        public void round() {

            assertThat(new Round(new Int(3)).toString(), is("Round{expression=Int{value=3}}"));
            assertThat(new Round(new Round(new Variable(1))).toString(),
                    is("Round{expression=Round{expression=Variable{index=1, indexExpression=null}}}"));
        }

        @Test
        public void sinh() {

            assertThat(new Sinh(new Int(3)).toString(), is("Sinh{expression=Int{value=3}}"));
            assertThat(new Abs(new Sinh(new Variable(1))).toString(),
                    is("Abs{expression=Sinh{expression=Variable{index=1, indexExpression=null}}}"));
        }

        @Test
        public void sqrt() {

            assertThat(new Sqrt(new Int(3)).toString(), is("Sqrt{expression=Int{value=3}}"));
            assertThat(new Sqrt(new Abs(new Variable(1))).toString(),
                    is("Sqrt{expression=Abs{expression=Variable{index=1, indexExpression=null}}}"));
        }

        @Test
        public void subtraction() {

            assertThat(new Subtraction(new Variable(0), new Variable(0)).toString(),
                    is("Subtraction{left=Variable{index=0, indexExpression=null}, " +
                            "right=Variable{index=0, indexExpression=null}}"));
            assertThat(new Abs(new Subtraction(new Variable(1), Constant.BOLTZMANN)).toString(),
                    is("Abs{expression=Subtraction{left=Variable{index=1, indexExpression=null}, right=BOLTZMANN}}"));
        }

        @Test
        public void tan() {

            assertThat(new Tan(new Int(3)).toString(), is("Tan{expression=Int{value=3}}"));
            assertThat(new Abs(new Tan(new Variable(1))).toString(),
                    is("Abs{expression=Tan{expression=Variable{index=1, indexExpression=null}}}"));
        }

        @Test
        public void tanh() {

            assertThat(new Tanh(new Int(3)).toString(), is("Tanh{expression=Int{value=3}}"));
            assertThat(new Abs(new Tanh(new Variable(1))).toString(),
                    is("Abs{expression=Tanh{expression=Variable{index=1, indexExpression=null}}}"));
        }

        @Test
        public void forVar() {

            assertThat(new IntVar("k").toString(), is("IntVar{variableName=k}"));
        }

        @Test
        public void forLoop() {

            assertThat(new ForLoop("k", new Round(new Parameter(0)), new Round(new Parameter(1)),
                            new Round(new Parameter(2)), new FunctionsWrapper(new Function(0, new Variable(1)))).toString(),
                    is("ForLoop{variableName=k, start=Round{expression=Parameter{index=0, indexExpression=null}}, " +
                            "end=Round{expression=Parameter{index=1, indexExpression=null}}, step=Round{expression=" +
                            "Parameter{index=2, indexExpression=null}}, functionsWrapper=FunctionsWrapper{functions=[" +
                            "Function{index=0, expression=Variable{index=1, indexExpression=null}}], " +
                            "auxiliaryVariables=[], markovShift=Optional.empty}}"));
        }

        @Test
        public void markovShift() {

            assertThat(new ForLoop("k", new Round(new Parameter(0)), new Round(new Parameter(1)),
                            new Round(new Parameter(2)), new FunctionsWrapper(
                            new MarkovShift(new Int(0)), new Function(0, new Variable(1)))).toString(),
                    is("ForLoop{variableName=k, start=Round{expression=Parameter{index=0, indexExpression=null}}, " +
                            "end=Round{expression=Parameter{index=1, indexExpression=null}}, step=Round{expression=" +
                            "Parameter{index=2, indexExpression=null}}, functionsWrapper=FunctionsWrapper{functions=" +
                            "[Function{index=0, expression=Variable{index=1, indexExpression=null}}], " +
                            "auxiliaryVariables=[], markovShift=Optional[MarkovShift{offset=Int{value=0}}]}}"));
        }

        @Test
        public void variableWithIntExpression() {

            assertThat(new Variable(new Binomial(new IntVar("i"), new Int(10))).toString(),
                    is("Variable{index=-1, indexExpression=Binomial{n=IntVar{variableName=i}, k=Int{value=10}}}"));
            assertThat(new Variable(new Faculty(new Addition(new IntVar("i"), new Int(1)))).toString(),
                    is("Variable{index=-1, indexExpression=Faculty{intExpression=Addition{left=" +
                            "IntVar{variableName=i}, right=Int{value=1}}}}"));
        }

        @Test
        public void parameterWithIntExpression() {

            assertThat(new Parameter(new Binomial(new IntVar("i"), new Int(10))).toString(),
                    is("Parameter{index=-1, indexExpression=Binomial{n=IntVar{variableName=i}, k=Int{value=10}}}"));
            assertThat(new Parameter(new Faculty(new Addition(new IntVar("i"), new Int(1)))).toString(),
                    is("Parameter{index=-1, indexExpression=Faculty{intExpression=Addition{left=" +
                            "IntVar{variableName=i}, right=Int{value=1}}}}"));
        }

        @Test
        public void markovShiftWithIntExpression() {

            assertThat(new MarkovShift(new Faculty(new IntVar("i"))).toString(),
                    is("MarkovShift{offset=Faculty{intExpression=IntVar{variableName=i}}}"));
        }

        @Test
        public void sum() {

            assertThat(new Sum(new IntVar("i"), "i", new Int(1), new Int(10)).toString(),
                    is("Sum{inner=IntVar{variableName=i}, variableName=i, start=Int{value=1}, end=Int{value=10}}"));
        }

        @Test
        public void prod() {

            assertThat(new Prod(new IntVar("i"), "i", new Int(1), new Int(10)).toString(),
                    is("Prod{inner=IntVar{variableName=i}, variableName=i, start=Int{value=1}, end=Int{value=10}}"));
        }

        @Test
        public void auxiliaryVariable() {

            assertThat(new AuxiliaryVariable(new AuxVar("O"), new Multiplication(new AuxVar("O"), new Int(5))).toString(),
                    is("AuxiliaryVariable{auxVar=AuxVar{variableName=O}, inner=Multiplication{left=AuxVar{variableName=O}, right=Int{value=5}}}"));
        }

        @Test
        public void auxiliaryVariableWithMixedExpression() {

            assertThat(new AuxiliaryVariable(new AuxVar("O"), new Power(new AuxVar("O"), new Int(5))).toString(),
                    is("AuxiliaryVariable{auxVar=AuxVar{variableName=O}, inner=Power{left=AuxVar{variableName=O}, right=Int{value=5}}}"));
        }

        @Test
        public void auxiliaryVariableWithIntExpression() {

            assertThat(new AuxiliaryVariable(new AuxVar("O"), new Power(new Int(2), new Int(5))).toString(),
                    is("AuxiliaryVariable{auxVar=AuxVar{variableName=O}, inner=Power{left=Int{value=2}, right=Int{value=5}}}"));
        }

        @Test
        public void expressionFromLifeInsuranceFormula() {

            assertThat(new AuxiliaryVariable(new AuxVar("A"), new Subtraction(new Power(new AuxVar("V"),
                            new Round(new Parameter(0))), new Power(new AuxVar("V"),
                            new Addition(new IntVar("k"), new Int(1))))).toString(),
                    is("AuxiliaryVariable{auxVar=AuxVar{variableName=A}, inner=Subtraction{left=Power{left=" +
                            "AuxVar{variableName=V}, right=Round{expression=Parameter{index=0, indexExpression=null}}}," +
                            " right=Power{left=AuxVar{variableName=V}, right=Addition{left=IntVar{variableName=k}," +
                            " right=Int{value=1}}}}}"));
        }

    }

    public static class EqualsAndHashCodeTest {

        @Test
        public void equalityByValue() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME,
                    new Sin(new Multiplication(Constant.PI, new Variable(0))));

            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME,
                    new Sin(new Multiplication(Constant.PI, new Variable(0))));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void unequal() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME,
                    new Sin(new Multiplication(new Variable(0), Constant.PI)));

            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME,
                    new Sin(new Multiplication(Constant.PI, new Variable(0))));

            assertThat(fd1.equals(fd2), is(false));
        }

        @Test
        public void abs() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Abs(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Abs(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Abs(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void addition() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Addition(innerExpression1(), innerExpression2()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Addition(innerExpression1(), innerExpression2()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Addition(innerExpression2(), innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void arccos() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Arccos(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Arccos(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Arccos(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void arcosh() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Arcosh(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Arcosh(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Arcosh(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void arcsin() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Arcsin(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Arcsin(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Arcsin(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void arctan() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Arctan(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Arctan(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Arctan(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void arsinh() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Arsinh(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Arsinh(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Arsinh(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void artanh() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Artanh(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Artanh(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Artanh(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void binomial() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Binomial(new Int(4), new Int(3)));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Binomial(new Int(4), new Int(3)));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Binomial(new Int(5), new Int(3)));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void constant() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, Constant.BOLTZMANN);
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, Constant.BOLTZMANN);
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, Constant.E);

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void cos() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Cos(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Cos(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Cos(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void cosh() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Cosh(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Cosh(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Cosh(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void doub() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Doub(5.674));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Doub(5.674));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Doub(5.6741));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void exp() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Exp(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Exp(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Exp(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void ln() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Ln(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Ln(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Ln(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void neg() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Neg(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Neg(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Neg(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void pos() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Pos(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Pos(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Pos(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void parenthesis() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Parenthesis(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Parenthesis(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Parenthesis(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void sin() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Sin(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Sin(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Sin(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void sinh() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Sinh(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Sinh(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Sinh(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void sqrt() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Sqrt(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Sqrt(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Sqrt(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void tan() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Tan(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Tan(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Tan(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void tanh() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Tanh(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Tanh(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Tanh(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void variable() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Variable(0));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Variable(0));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Variable(1));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void parameter() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Parameter(0));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Parameter(0));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Parameter(1));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void integer() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Int(1));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Int(1));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Int(2));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void faculty() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Faculty(new Int(5)));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Faculty(new Int(5)));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Faculty(new Int(6)));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void round() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Round(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Round(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Round(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void division() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Division(innerExpression1(), innerExpression2()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Division(innerExpression1(), innerExpression2()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Division(innerExpression2(), innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void multiplication() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Multiplication(innerExpression1(), innerExpression2()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Multiplication(innerExpression1(), innerExpression2()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Multiplication(innerExpression2(), innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void subtraction() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Subtraction(innerExpression1(), innerExpression2()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Subtraction(innerExpression1(), innerExpression2()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Subtraction(innerExpression2(), innerExpression1()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void power() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Power(innerExpression1(), innerExpression2()));
            FunctionDefinition fd2 = createFunctionDefinition(FUNCTION_NAME, new Power(innerExpression1(), innerExpression2()));
            FunctionDefinition fd3 = createFunctionDefinition(FUNCTION_NAME, new Power(innerExpression2(), innerExpression1()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void equalsByIdenticalObjectsForUnaryExpression() {

            Sin sin = new Sin(new Variable(0));

            assertThat(sin.equals(sin), is(true));
        }

        @Test
        public void unequalToNullForUnaryExpression() {

            Sin sin = new Sin(new Variable(0));

            assertThat(sin.equals(null), is(false));
        }

        @Test
        public void unequalToDifferentTypeForUnaryExpression() {

            Sin sin = new Sin(new Variable(0));
            FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME, new Cos(new Variable(0)));

            assertThat(sin.equals(fd), is(false));
        }

        @Test
        public void equalsByIdenticalObjectsForFunctionBody() {

            FunctionBody fb = new FunctionBody(new FunctionsWrapper(new Function(0, new Variable(0))));

            assertThat(fb.equals(fb), is(true));
        }

        @Test
        public void unequalToNullForFunctionBody() {

            FunctionBody fb = new FunctionBody(new FunctionsWrapper(new Function(0, new Variable(0))));

            assertThat(fb.equals(null), is(false));
        }

        @Test
        public void unequalToDifferentTypeForFunctionBody() {

            FunctionBody fb = new FunctionBody(new FunctionsWrapper(new Function(0, new Variable(0))));
            FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME, new Cos(new Variable(0)));

            assertThat(fb.equals(fd), is(false));
        }

        @Test
        public void equalsByIdenticalObjectsForFunctionDefinition() {

            FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME, new Sin(new Variable(0)));

            assertThat(fd.equals(fd), is(true));
        }

        @Test
        public void unequalToNullForFunctionDefinition() {

            FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME, new Sin(new Variable(0)));

            assertThat(fd.equals(null), is(false));
        }

        @Test
        public void unequalToDifferentTypeForFunctionDefinition() {

            FunctionDefinition fd1 = createFunctionDefinition(FUNCTION_NAME, new Sin(new Variable(0)));
            Sin sin = new Sin(new Variable(0));

            assertThat(fd1.equals(sin), is(false));
        }

        @Test
        public void equalsByIdenticalObjectsForFunctionsWrapper() {

            FunctionsWrapper fw = new FunctionsWrapper(new Function(0, new Variable(0)));

            assertThat(fw.equals(fw), is(true));
        }

        @Test
        public void unequalToNullForFunctionsWrapper() {

            FunctionsWrapper fw = new FunctionsWrapper(new Function(0, new Variable(0)));

            assertThat(fw.equals(null), is(false));
        }

        @Test
        public void unequalToDifferentTypeForFunctionsWrapper() {

            FunctionsWrapper fw = new FunctionsWrapper(new Function(0, new Variable(0)));
            FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME, new Cos(new Variable(0)));

            assertThat(fw.equals(fd), is(false));
        }

        @Test
        public void equalsByIdenticalObjectsForFunction() {

            Function f = new Function(0, new Variable(0));

            assertThat(f.equals(f), is(true));
        }

        @Test
        public void unequalToNullForFunction() {

            Function f = new Function(0, new Variable(0));

            assertThat(f.equals(null), is(false));
        }

        @Test
        public void unequalToDifferentTypeForFunction() {

            Function f = new Function(0, new Variable(0));
            FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME, new Cos(new Variable(0)));

            assertThat(f.equals(fd), is(false));
        }

        @Test
        public void equalsByIdenticalObjectsForFaculty() {

            Faculty fac = new Faculty(new Int(3));

            assertThat(fac.equals(fac), is(true));
        }

        @Test
        public void unequalToNullForFaculty() {

            Faculty fac = new Faculty(new Int(3));

            assertThat(fac.equals(null), is(false));
        }

        @Test
        public void unequalToDifferentTypeForFaculty() {

            Faculty fac = new Faculty(new Int(3));
            FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME, new Cos(new Variable(0)));

            assertThat(fac.equals(fd), is(false));
        }

        @Test
        public void equalsByIdenticalObjectsForBinomial() {

            Binomial bin = new Binomial(new Int(5), new Int(3));

            assertThat(bin.equals(bin), is(true));
        }

        @Test
        public void unequalToNullForBinomial() {

            Binomial bin = new Binomial(new Int(5), new Int(3));

            assertThat(bin.equals(null), is(false));
        }

        @Test
        public void unequalToDifferentTypeForBinomial() {

            Binomial bin = new Binomial(new Int(5), new Int(3));
            FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME, new Cos(new Variable(0)));

            assertThat(bin.equals(fd), is(false));
        }

        @Test
        public void equalsByIdenticalObjectsForBinaryExpression() {

            Addition add = new Addition(new Variable(0), new Parameter(0));

            assertThat(add.equals(add), is(true));
        }

        @Test
        public void unequalToNullForBinaryExpression() {

            Addition add = new Addition(new Variable(0), new Parameter(0));

            assertThat(add.equals(null), is(false));
        }

        @Test
        public void unequalToDifferentTypeForBinaryExpression() {

            Addition add = new Addition(new Variable(0), new Parameter(0));
            FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME, new Cos(new Variable(0)));

            assertThat(add.equals(fd), is(false));
        }

        @Test
        public void equalsByIdenticalObjectsForIndexedExpression() {

            Variable var = new Variable(0);

            assertThat(var.equals(var), is(true));
        }

        @Test
        public void unequalToNullForIndexedExpression() {

            Variable var = new Variable(0);

            assertThat(var.equals(null), is(false));
        }

        @Test
        public void unequalToDifferentTypeForIndexedExpression() {

            Variable var = new Variable(0);
            FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME, new Cos(new Variable(0)));

            assertThat(var.equals(fd), is(false));
        }

        @Test
        public void equalsByIdenticalObjectsForSingleValuedExpression() {

            Int i = new Int(10);

            assertThat(i.equals(i), is(true));
        }

        @Test
        public void unequalToNullForSingleValuedExpression() {

            Int i = new Int(10);

            assertThat(i.equals(null), is(false));
        }

        @Test
        public void unequalToDifferentTypeForSingleValuedExpression() {

            Int i = new Int(10);
            FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME, new Cos(new Variable(0)));

            assertThat(i.equals(fd), is(false));
        }

        private static Expression innerExpression1() {
            return new Power(new Sin(new Variable(0)), new Addition(new Exp(new Parameter(0)), new Parameter(1)));
        }

        private static Expression innerExpression2() {
            return new Power(new Sin(new Variable(0)), new Addition(new Exp(new Parameter(0)), new Parameter(0)));
        }

        @Test
        public void forVar() {

            IntVar fv1 = new IntVar("k");
            IntVar fv2 = new IntVar("k");
            IntVar fv3 = new IntVar("i");

            assertThat(fv1.equals(fv2), is(true));
            assertThat(fv1.equals(fv3), is(false));
            assertThat(fv1.equals(null), is(false));
            assertThat(fv1.equals(fv1), is(true));
            assertThat(fv1.hashCode(), is(fv2.hashCode()));
        }

        @Test
        public void forLoop() {

            ForLoop fl1 = new ForLoop("i", new Int(1), new Int(2), new Int(3), new FunctionsWrapper(new Function(0, new Variable(0))));
            ForLoop fl2 = new ForLoop("i", new Int(1), new Int(2), new Int(3), new FunctionsWrapper(new Function(0, new Variable(0))));
            ForLoop fl3 = new ForLoop("j", new Int(1), new Int(2), new Int(3), new FunctionsWrapper(new Function(0, new Variable(1))));
            ForLoop fl4 = new ForLoop("i", new Int(2), new Int(2), new Int(3), new FunctionsWrapper(new Function(0, new Variable(0))));
            ForLoop fl5 = new ForLoop("i", new Int(1), new Int(3), new Int(3), new FunctionsWrapper(new Function(0, new Variable(0))));
            ForLoop fl6 = new ForLoop("i", new Int(1), new Int(2), new Int(4), new FunctionsWrapper(new Function(0, new Variable(0))));
            ForLoop fl7 = new ForLoop("i", new Int(1), new Int(2), new Int(3), new FunctionsWrapper(new Function(1, new Variable(0))));
            FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME, new Cos(new Variable(0)));

            assertThat(fl1.equals(fl2), is(true));
            assertThat(fl1.equals(fl3), is(false));
            assertThat(fl1.equals(null), is(false));
            assertThat(fl1.equals(fl1), is(true));
            assertThat(fl1.equals(fd), is(false));
            assertThat(fl1.equals(fl4), is(false));
            assertThat(fl1.equals(fl5), is(false));
            assertThat(fl1.equals(fl6), is(false));
            assertThat(fl1.equals(fl7), is(false));

            assertThat(fl1.hashCode(), is(fl2.hashCode()));
        }

        @Test
        public void markovShift() {

            MarkovShift ms1 = new MarkovShift(new Int(0));
            MarkovShift ms2 = new MarkovShift(new Int(0));
            MarkovShift ms3 = new MarkovShift(new Int(1));
            FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME, new Cos(new Variable(0)));

            assertThat(ms1.equals(ms2), is(true));
            assertThat(ms1.equals(ms3), is(false));
            assertThat(ms1.equals(null), is(false));
            assertThat(ms1.equals(ms1), is(true));
            assertThat(ms1.equals(fd), is(false));

            assertThat(ms1.hashCode(), is(ms2.hashCode()));
        }

        @Test
        public void parameterWithIntExpression() {

            Variable v1 = new Variable(new Faculty(new IntVar("i")));
            Variable v2 = new Variable(new Faculty(new IntVar("i")));
            Variable v3 = new Variable(new Faculty(new IntVar("j")));
            Variable v4 = new Variable(10);

            assertThat(v1.equals(v2), is(true));
            assertThat(v1.equals(v3), is(false));
            assertThat(v1.equals(v4), is(false));
            assertThat(v1.equals(null), is(false));
            assertThat(v1.equals(v1), is(true));

            assertThat(v1.hashCode(), is(v2.hashCode()));
        }

        @Test
        public void variableWithIntExpression() {

            Parameter p1 = new Parameter(new Faculty(new IntVar("i")));
            Parameter p2 = new Parameter(new Faculty(new IntVar("i")));
            Parameter p3 = new Parameter(new Faculty(new IntVar("j")));
            Parameter p4 = new Parameter(10);

            assertThat(p1.equals(p2), is(true));
            assertThat(p1.equals(p3), is(false));
            assertThat(p1.equals(p4), is(false));
            assertThat(p1.equals(null), is(false));
            assertThat(p1.equals(p1), is(true));

            assertThat(p1.hashCode(), is(p2.hashCode()));
        }

        @Test
        public void sum() {

            Sum s1 = new Sum(new IntVar("z"), "z", new Int(1), new Int(10));
            Sum s2 = new Sum(new IntVar("z"), "z", new Int(2), new Int(10));
            Sum s3 = new Sum(new IntVar("z"), "z", new Int(1), new Int(11));
            Sum s4 = new Sum(new IntVar("l"), "l", new Int(1), new Int(10));
            Sum s5 = new Sum(new IntVar("z"), "z", new Int(1), new Int(10));
            Prod p1 = new Prod(new IntVar("z"), "z", new Int(1), new Int(10));

            assertThat(s1.equals(s1), is(true));
            assertThat(s1.equals(s5), is(true));
            assertThat(s1.equals(s2), is(false));
            assertThat(s1.equals(s3), is(false));
            assertThat(s1.equals(s4), is(false));
            assertThat(s1.equals(p1), is(false));

            assertThat(s1.hashCode(), is(s5.hashCode()));
        }

        @Test
        public void prod() {

            Prod p1 = new Prod(new IntVar("z"), "z", new Int(1), new Int(10));
            Prod p2 = new Prod(new IntVar("z"), "z", new Int(2), new Int(10));
            Prod p3 = new Prod(new IntVar("z"), "z", new Int(1), new Int(11));
            Prod p4 = new Prod(new IntVar("l"), "l", new Int(1), new Int(10));
            Prod p5 = new Prod(new IntVar("z"), "z", new Int(1), new Int(10));
            Sum s1 = new Sum(new IntVar("z"), "z", new Int(1), new Int(10));

            assertThat(p1.equals(p1), is(true));
            assertThat(p1.equals(p5), is(true));
            assertThat(p1.equals(p2), is(false));
            assertThat(p1.equals(p3), is(false));
            assertThat(p1.equals(p4), is(false));
            assertThat(p1.equals(s1), is(false));

            assertThat(p1.hashCode(), is(p5.hashCode()));
        }

        @Test
        public void auxVar() {

            AuxVar a1 = new AuxVar("K");
            AuxVar a2 = new AuxVar("K");
            AuxVar a3 = new AuxVar("L");
            IntVar i1 = new IntVar("K");

            assertThat(a1.equals(a1), is(true));
            assertThat(a1.equals(a2), is(true));
            assertThat(a1.equals(a3), is(false));
            assertThat(a1.equals(i1), is(false));

            assertThat(a1.hashCode(), is(a2.hashCode()));
        }

        @Test
        public void auxiliaryVariable() {

            AuxiliaryVariable av1 = new AuxiliaryVariable(new AuxVar("K"), new Variable(0));
            AuxiliaryVariable av2 = new AuxiliaryVariable(new AuxVar("K"), new Variable(0));
            AuxiliaryVariable av3 = new AuxiliaryVariable(new AuxVar("L"), new Variable(0));
            AuxiliaryVariable av4 = new AuxiliaryVariable(new AuxVar("K"), new Variable(1));
            Sum s1 = new Sum(new IntVar("z"), "z", new Int(1), new Int(10));

            assertThat(av1.equals(av1), is(true));
            assertThat(av1.equals(av2), is(true));
            assertThat(av1.equals(av3), is(false));
            assertThat(av1.equals(av4), is(false));
            assertThat(av1.equals(s1), is(false));
        }

    }

    public static FunctionDefinition createFunctionDefinition(String name, Expression expression) {

        List<Function> list = new ArrayList<>();
        Function f = new Function(0, expression);
        list.add(f);

        return new FunctionDefinition(name, new FunctionBody(new FunctionsWrapper(list)));
    }

    public static FunctionDefinition createFunctionDefinition(String name, Expression expression, Expression aux) {

        List<Function> functions = new ArrayList<>();
        Function f = new Function(0, expression);
        functions.add(f);

        List<AuxiliaryVariable> auxVars = new ArrayList<>();
        AuxiliaryVariable av = new AuxiliaryVariable(new AuxVar("I"), aux);
        auxVars.add(av);

        return new FunctionDefinition(name, new FunctionBody(new FunctionsWrapper(auxVars, functions, null)));
    }

}
