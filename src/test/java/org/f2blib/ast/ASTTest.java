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

package org.f2blib.ast;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for abstract syntax tree classes.
 */
@RunWith(Enclosed.class)
public class ASTTest {

    public static class ToStringTest {

        @Test
        public void functionDefinition() {

            FunctionDefinition fd = createFunctionDefinition("MyFunc",
                    new Sin(new Multiplication(Constant.PI, new Variable(0))));

            assertThat(fd.toString(), is("FunctionDefinition{name=MyFunc, functionBody=FunctionBody{" +
                    "functions=Functions{functions=[Function{index=0, expression=Sin{expression=" +
                    "Multiplication{left=PI, right=Variable{index=0}}}}]}}}"));
        }

        @Test
        public void abs() {

            assertThat(new Abs(new Int(3)).toString(), is("Abs{expression=Int{value=3}}"));
            assertThat(new Abs(new Sin(new Variable(1))).toString(),
                    is("Abs{expression=Sin{expression=Variable{index=1}}}"));
        }

        @Test
        public void addition() {

            assertThat(new Addition(new Int(3), new Variable(0)).toString(),
                    is("Addition{left=Int{value=3}, right=Variable{index=0}}"));
            assertThat(new Addition(new Addition(new Int(3), new Parameter(3)), Constant.E).toString(),
                    is("Addition{left=Addition{left=Int{value=3}, right=Parameter{index=3}}, right=E}"));
        }

        @Test
        public void arccos() {

            assertThat(new Arccos(new Int(3)).toString(), is("Arccos{expression=Int{value=3}}"));
            assertThat(new Arccos(new Cos(new Variable(1))).toString(),
                    is("Arccos{expression=Cos{expression=Variable{index=1}}}"));
        }

        @Test
        public void arcosh() {

            assertThat(new Arcosh(new Multiplication(new Variable(0), new Variable(0))).toString(),
                    is("Arcosh{expression=Multiplication{left=Variable{index=0}, right=Variable{index=0}}}"));
            assertThat(new Arcosh(new Sin(Constant.PI)).toString(), is("Arcosh{expression=Sin{expression=PI}}"));
        }

        @Test
        public void arcsin() {

            assertThat(new Arcsin(new Arcsin(new Doub(3.99))).toString(),
                    is("Arcsin{expression=Arcsin{expression=Doub{value=3.99}}}"));
            assertThat(new Division(new Int(1), new Arcsin(new Variable(1))).toString(),
                    is("Division{left=Int{value=1}, right=Arcsin{expression=Variable{index=1}}}"));
        }

        @Test
        public void arctan() {

            assertThat(new Arctan(new Variable(0)).toString(), is("Arctan{expression=Variable{index=0}}"));
            assertThat(new Arctan(new Variable(1)).toString(), is("Arctan{expression=Variable{index=1}}"));
        }

        @Test
        public void arsinh() {

            assertThat(new Arsinh(new Exp(new Int(3))).toString(),
                    is("Arsinh{expression=Exp{expression=Int{value=3}}}"));
            assertThat(new Arsinh(new Tan(new Variable(1))).toString(),
                    is("Arsinh{expression=Tan{expression=Variable{index=1}}}"));
        }

        @Test
        public void artanh() {

            assertThat(new Artanh(new Doub(4.88)).toString(), is("Artanh{expression=Doub{value=4.88}}"));
            assertThat(new Sin(new Artanh(new Sin(new Variable(1)))).toString(),
                    is("Sin{expression=Artanh{expression=Sin{expression=Variable{index=1}}}}"));
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
                    is("Cos{expression=Sin{expression=Variable{index=1}}}"));
        }

        @Test
        public void cosh() {

            assertThat(new Cosh(new Int(3)).toString(), is("Cosh{expression=Int{value=3}}"));
            assertThat(new Abs(new Cosh(new Variable(1))).toString(),
                    is("Abs{expression=Cosh{expression=Variable{index=1}}}"));
        }

        @Test
        public void division() {

            assertThat(new Division(new Parameter(0), new Parameter(1)).toString(),
                    is("Division{left=Parameter{index=0}, right=Parameter{index=1}}"));
            assertThat(new Round(new Division(new Variable(0), Constant.PI)).toString(),
                    is("Round{expression=Division{left=Variable{index=0}, right=PI}}"));
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
                    is("Abs{expression=Exp{expression=Variable{index=1}}}"));
        }

        @Test
        public void faculty() {

            assertThat(new Faculty(new Round(new Pos(new Parameter(0)))).toString(),
                    is("Faculty{intExpression=Round{expression=Pos{expression=Parameter{index=0}}}}"));
            assertThat(new Abs(new Faculty(new Int(1))).toString(),
                    is("Abs{expression=Faculty{intExpression=Int{value=1}}}"));
        }

        @Test
        public void ln() {

            assertThat(new Ln(new Int(3)).toString(), is("Ln{expression=Int{value=3}}"));
            assertThat(new Ln(new Ln(new Variable(1))).toString(),
                    is("Ln{expression=Ln{expression=Variable{index=1}}}"));
        }

        @Test
        public void multiplication() {

            assertThat(new Multiplication(new Multiplication(new Variable(0), new Parameter(0)),
                            new Multiplication(new Variable(1), new Parameter(1))).toString(),
                    is("Multiplication{left=Multiplication{left=Variable{index=0}, right=Parameter{index=0}}, " +
                            "right=Multiplication{left=Variable{index=1}, right=Parameter{index=1}}}"));
            assertThat(new Tanh(new Multiplication(new Variable(1), Constant.E)).toString(),
                    is("Tanh{expression=Multiplication{left=Variable{index=1}, right=E}}"));
        }

        @Test
        public void neg() {

            assertThat(new Abs(new Neg(new Int(-3))).toString(),
                    is("Abs{expression=Neg{expression=Int{value=-3}}}"));
            assertThat(new Neg(new Sin(new Variable(1))).toString(),
                    is("Neg{expression=Sin{expression=Variable{index=1}}}"));
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
                    is("Abs{expression=Parenthesis{expression=Variable{index=1}}}"));
        }

        @Test
        public void power() {

            assertThat(new Power(new Variable(0), new Parameter(0)).toString(),
                    is("Power{left=Variable{index=0}, right=Parameter{index=0}}"));
            assertThat(new Abs(new Power(new Variable(0), new Parameter(0))).toString(),
                    is("Abs{expression=Power{left=Variable{index=0}, right=Parameter{index=0}}}"));
        }

        @Test
        public void round() {

            assertThat(new Round(new Int(3)).toString(), is("Round{expression=Int{value=3}}"));
            assertThat(new Round(new Round(new Variable(1))).toString(),
                    is("Round{expression=Round{expression=Variable{index=1}}}"));
        }

        @Test
        public void sinh() {

            assertThat(new Sinh(new Int(3)).toString(), is("Sinh{expression=Int{value=3}}"));
            assertThat(new Abs(new Sinh(new Variable(1))).toString(),
                    is("Abs{expression=Sinh{expression=Variable{index=1}}}"));
        }

        @Test
        public void sqrt() {

            assertThat(new Sqrt(new Int(3)).toString(), is("Sqrt{expression=Int{value=3}}"));
            assertThat(new Sqrt(new Abs(new Variable(1))).toString(),
                    is("Sqrt{expression=Abs{expression=Variable{index=1}}}"));
        }

        @Test
        public void subtraction() {

            assertThat(new Subtraction(new Variable(0), new Variable(0)).toString(),
                    is("Subtraction{left=Variable{index=0}, right=Variable{index=0}}"));
            assertThat(new Abs(new Subtraction(new Variable(1), Constant.BOLTZMANN)).toString(),
                    is("Abs{expression=Subtraction{left=Variable{index=1}, right=BOLTZMANN}}"));
        }

        @Test
        public void tan() {

            assertThat(new Tan(new Int(3)).toString(), is("Tan{expression=Int{value=3}}"));
            assertThat(new Abs(new Tan(new Variable(1))).toString(),
                    is("Abs{expression=Tan{expression=Variable{index=1}}}"));
        }

        @Test
        public void tanh() {

            assertThat(new Tanh(new Int(3)).toString(), is("Tanh{expression=Int{value=3}}"));
            assertThat(new Abs(new Tanh(new Variable(1))).toString(),
                    is("Abs{expression=Tanh{expression=Variable{index=1}}}"));
        }

    }

    public static class EqualsAndHashCodeTest {

        @Test
        public void equalityByValue() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc",
                    new Sin(new Multiplication(Constant.PI, new Variable(0))));

            FunctionDefinition fd2 = createFunctionDefinition("MyFunc",
                    new Sin(new Multiplication(Constant.PI, new Variable(0))));

            assertThat(fd1.equals(fd2), is(true));

            assertThat(fd1.hashCode(), is(fd2.hashCode()));
        }

        @Test
        public void unequal() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc",
                    new Sin(new Multiplication(new Variable(0), Constant.PI)));

            FunctionDefinition fd2 = createFunctionDefinition("MyFunc",
                    new Sin(new Multiplication(Constant.PI, new Variable(0))));

            assertThat(fd1.equals(fd2), is(false));

            assertThat(fd1.hashCode(), is(not(fd2.hashCode())));
        }

        @Test
        public void abs() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Abs(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Abs(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Abs(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void addition() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Addition(innerExpression1(), innerExpression2()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Addition(innerExpression1(), innerExpression2()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Addition(innerExpression2(), innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void arccos() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Arccos(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Arccos(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Arccos(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void arcosh() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Arcosh(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Arcosh(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Arcosh(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void arcsin() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Arcsin(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Arcsin(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Arcsin(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void arctan() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Arctan(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Arctan(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Arctan(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void arsinh() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Arsinh(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Arsinh(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Arsinh(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void artanh() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Artanh(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Artanh(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Artanh(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void binomial() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Binomial(new Int(4), new Int(3)));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Binomial(new Int(4), new Int(3)));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Binomial(new Int(5), new Int(3)));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void constant() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", Constant.BOLTZMANN);
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", Constant.BOLTZMANN);
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", Constant.E);

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void cos() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Cos(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Cos(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Cos(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void cosh() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Cosh(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Cosh(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Cosh(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void doub() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Doub(5.674));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Doub(5.674));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Doub(5.6741));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void exp() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Exp(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Exp(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Exp(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void ln() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Ln(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Ln(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Ln(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void neg() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Neg(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Neg(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Neg(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void pos() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Pos(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Pos(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Pos(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void parenthesis() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Parenthesis(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Parenthesis(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Parenthesis(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void sin() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Sin(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Sin(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Sin(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void sinh() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Sinh(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Sinh(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Sinh(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void sqrt() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Sqrt(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Sqrt(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Sqrt(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void tan() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Tan(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Tan(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Tan(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void tanh() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Tanh(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Tanh(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Tanh(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void variable() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Variable(0));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Variable(0));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Variable(1));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void parameter() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Parameter(0));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Parameter(0));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Parameter(1));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void integer() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Int(1));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Int(1));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Int(2));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void faculty() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Faculty(new Int(5)));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Faculty(new Int(5)));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Faculty(new Int(6)));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void round() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Round(innerExpression1()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Round(innerExpression1()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Round(innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void division() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Division(innerExpression1(), innerExpression2()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Division(innerExpression1(), innerExpression2()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Division(innerExpression2(), innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void multiplication() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Multiplication(innerExpression1(), innerExpression2()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Multiplication(innerExpression1(), innerExpression2()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Multiplication(innerExpression2(), innerExpression2()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void subtraction() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Subtraction(innerExpression1(), innerExpression2()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Subtraction(innerExpression1(), innerExpression2()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Subtraction(innerExpression2(), innerExpression1()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

        @Test
        public void power() {

            FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Power(innerExpression1(), innerExpression2()));
            FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Power(innerExpression1(), innerExpression2()));
            FunctionDefinition fd3 = createFunctionDefinition("MyFunc", new Power(innerExpression2(), innerExpression1()));

            assertThat(fd1.equals(fd2), is(true));
            assertThat(fd1.equals(fd3), is(false));
            assertThat(fd1.hashCode(), is(fd2.hashCode()));
            assertThat(fd1.hashCode(), is(not(fd3.hashCode())));
        }

    }

    private static Expression innerExpression1() {
        return new Power(new Sin(new Variable(0)), new Addition(new Exp(new Parameter(0)), new Parameter(1)));
    }

    private static Expression innerExpression2() {
        return new Power(new Sin(new Variable(0)), new Addition(new Exp(new Parameter(0)), new Parameter(0)));
    }

    public static FunctionDefinition createFunctionDefinition(String name, Expression expression) {

        List<Function> list = new ArrayList<>();
        Function f = new Function(0, expression);
        list.add(f);

        FunctionDefinition fd = new FunctionDefinition("MyFunc", new FunctionBody(new Functions(list)));

        return fd;
    }

}
