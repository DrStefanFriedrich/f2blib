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

package com.github.drstefanfriedrich.f2blib.visitor;

import com.github.drstefanfriedrich.f2blib.ast.*;
import org.junit.Test;

public abstract class AbstractCalculatingVisitorTest {

    protected abstract void assertExpressionMatches(Expression expression, double xValue, double yValue);

    protected abstract void assertExpressionMatches(Expression expression, Expression auxiliaryExpression,
                                                    double xValue, double yValue);

    @Test
    public void abs() {
        assertExpressionMatches(new Abs(new Variable(0)), 1.11, 1.11);
        assertExpressionMatches(new Abs(new Variable(0)), -1.11, 1.11);
        assertExpressionMatches(new Abs(new Int(-3)), 0, 3);
    }

    @Test
    public void addition() {
        assertExpressionMatches(new Addition(new Variable(0), Constant.PI), 1.11, 4.251592653589793);
        assertExpressionMatches(new Addition(new Variable(0), Constant.PI), -1.11, 2.031592653589793);
        assertExpressionMatches(new Addition(new Int(2), new Int(3)), 0, 5);
        assertExpressionMatches(new Addition(new Int(2), Constant.PI), 0, 5.141592653589793);
        assertExpressionMatches(new Addition(Constant.PI, new Int(2)), 0, 5.141592653589793);
    }

    @Test
    public void arccos() {
        assertExpressionMatches(new Arccos(new Variable(0)), 0.5, 1.0471975511965979);
        assertExpressionMatches(new Arccos(new Variable(0)), -0.25, 1.8234765819369754);
        assertExpressionMatches(new Arccos(new Int(0)), 0, 1.5707963267948966);
    }

    @Test
    public void arcosh() {
        assertExpressionMatches(new Arcosh(new Variable(0)), 1.5, 0.9624236501192069);
        assertExpressionMatches(new Arcosh(new Variable(0)), 2, 1.3169578969248166);
        assertExpressionMatches(new Arcosh(new Int(1)), 0, 0);
    }

    @Test
    public void arcsin() {
        assertExpressionMatches(new Arcsin(new Variable(0)), 0.2, 0.2013579207903308);
        assertExpressionMatches(new Arcsin(new Variable(0)), -0.2, -0.2013579207903308);
        assertExpressionMatches(new Arcsin(new Int(0)), 0, 0);
    }

    @Test
    public void arctan() {
        assertExpressionMatches(new Arctan(new Variable(0)), 1.11, 0.837483712611627);
        assertExpressionMatches(new Arctan(new Variable(0)), -1.11, -0.837483712611627);
        assertExpressionMatches(new Arctan(new Int(0)), 0, 0);
    }

    @Test
    public void arsinh() {
        assertExpressionMatches(new Arsinh(new Variable(0)), 1.11, 0.9570569496795586);
        assertExpressionMatches(new Arsinh(new Variable(0)), -1.11, -0.9570569496795586);
        assertExpressionMatches(new Arsinh(new Int(0)), 0, 0);
    }

    @Test
    public void artanh() {
        assertExpressionMatches(new Artanh(new Variable(0)), 0.1, 0.10033534773107558);
        assertExpressionMatches(new Artanh(new Variable(0)), -0.1, -0.10033534773107558);
        assertExpressionMatches(new Artanh(new Int(0)), 0, 0);
    }

    @Test
    public void binomial() {
        assertExpressionMatches(new Binomial(new Int(5), new Int(4)), 1.11, 5);
        assertExpressionMatches(new Binomial(new Int(5), new Int(4)), -1.11, 5);
    }

    @Test
    public void constant() {
        assertExpressionMatches(Constant.PI, 1.11, Math.PI);
        assertExpressionMatches(Constant.E, -1.11, Math.E);
    }

    @Test
    public void cos() {
        assertExpressionMatches(new Cos(new Variable(0)), 1.11, 0.4446615167417068);
        assertExpressionMatches(new Cos(new Variable(0)), -1.11, 0.4446615167417068);
        assertExpressionMatches(new Cos(new Int(0)), 0, 1);
    }

    @Test
    public void cosh() {
        assertExpressionMatches(new Cosh(new Variable(0)), 1.11, 1.6819586777554325);
        assertExpressionMatches(new Cosh(new Variable(0)), -1.11, 1.6819586777554325);
        assertExpressionMatches(new Cosh(new Int(0)), 0, 1);
    }

    @Test
    public void division() {
        assertExpressionMatches(new Division(new Variable(0), Constant.PI), 1.11, 0.3533239736640077);
        assertExpressionMatches(new Division(new Variable(0), Constant.PI), -1.11, -0.3533239736640077);
        assertExpressionMatches(new Division(new Int(10), Constant.PI), -1.11, 3.183098861837907);
        assertExpressionMatches(new Division(Constant.PI, new Int(4)), -1.11, 0.7853981633974483);
        assertExpressionMatches(new Division(new Int(10), new Int(5)), -1.11, 2);
    }

    @Test
    public void doub() {
        assertExpressionMatches(new Doub(3.663), 1.11, 3.663);
        assertExpressionMatches(new Doub(3.663), -1.11, 3.663);
    }

    @Test
    public void exp() {
        assertExpressionMatches(new Exp(new Variable(0)), 1.11, 3.034358394435676);
        assertExpressionMatches(new Exp(new Variable(0)), -1.11, 0.32955896107518906);
        assertExpressionMatches(new Exp(new Int(0)), 0, 1);
    }

    @Test
    public void faculty() {
        assertExpressionMatches(new Faculty(new Int(5)), 1.11, 120);
        assertExpressionMatches(new Faculty(new Int(5)), -1.11, 120);
    }

    @Test
    public void integer() {
        assertExpressionMatches(new Int(6), 1.11, 6);
        assertExpressionMatches(new Int(6), -1.11, 6);
    }

    @Test
    public void ln() {
        assertExpressionMatches(new Ln(new Variable(0)), 1.11, 0.10436001532424286);
        assertExpressionMatches(new Ln(new Variable(0)), 2.22, 0.7975071958841882);
        assertExpressionMatches(new Ln(new Int(1)), 0, 0);
    }

    @Test
    public void multiplication() {
        assertExpressionMatches(new Multiplication(new Variable(0), new Doub(5.71)), 1.11, 6.3381);
        assertExpressionMatches(new Multiplication(new Variable(0), new Doub(5.71)), -1.11, -6.3381);
        assertExpressionMatches(new Multiplication(new Int(2), new Doub(5.71)), -1.11, 11.42);
        assertExpressionMatches(new Multiplication(new Variable(0), new Int(5)), -1.11, -5.55);
        assertExpressionMatches(new Multiplication(new Int(3), new Int(6)), -1.11, 18);
    }

    @Test
    public void neg() {
        assertExpressionMatches(new Neg(new Variable(0)), 1.11, -1.11);
        assertExpressionMatches(new Neg(new Variable(0)), -1.11, 1.11);
        assertExpressionMatches(new Neg(new Int(1)), 0, -1);
    }

    @Test
    public void pos() {
        assertExpressionMatches(new Pos(new Variable(0)), 1.11, 1.11);
        assertExpressionMatches(new Pos(new Variable(0)), -1.11, -1.11);
        assertExpressionMatches(new Pos(new Int(-1)), 0, -1);
    }

    @Test
    public void parenthesis() {
        assertExpressionMatches(new Parenthesis(new Variable(0)), 1.11, 1.11);
        assertExpressionMatches(new Parenthesis(new Variable(0)), -1.11, -1.11);
        assertExpressionMatches(new Parenthesis(new Int(1)), 0, 1);
    }

    @Test
    public void power() {
        assertExpressionMatches(new Power(new Variable(0), new Variable(0)), 1.11, 1.1228157771398957);
        assertExpressionMatches(new Power(new Variable(0), new Variable(0)), 2.23, 5.980256952007954);
        assertExpressionMatches(new Power(new Int(1), new Variable(0)), 2.23, 1);
        assertExpressionMatches(new Power(new Variable(0), new Int(1)), 2.23, 2.23);
        assertExpressionMatches(new Power(new Int(2), new Int(2)), 0, 4);
    }

    @Test
    public void round() {
        assertExpressionMatches(new Round(new Variable(0)), 1.11, 1);
        assertExpressionMatches(new Round(new Variable(0)), -7.56, -8);
    }

    @Test
    public void sin() {
        assertExpressionMatches(new Sin(new Variable(0)), 1.11, 0.8956986856800476);
        assertExpressionMatches(new Sin(new Variable(0)), -1.11, -0.8956986856800476);
        assertExpressionMatches(new Sin(new Int(0)), 0, 0);
    }

    @Test
    public void sinh() {
        assertExpressionMatches(new Sinh(new Variable(0)), 1.11, 1.3523997166802433);
        assertExpressionMatches(new Sinh(new Variable(0)), -1.11, -1.3523997166802433);
        assertExpressionMatches(new Sinh(new Int(0)), 0, 0);
    }

    @Test
    public void sqrt() {
        assertExpressionMatches(new Sqrt(new Variable(0)), 1.11, 1.0535653752852738);
        assertExpressionMatches(new Sqrt(new Variable(0)), 4, 2);
        assertExpressionMatches(new Sqrt(new Int(4)), 0, 2);
    }

    @Test
    public void subtraction() {
        assertExpressionMatches(new Subtraction(new Variable(0), new Doub(8.1)), 1.11, -6.989999999999999);
        assertExpressionMatches(new Subtraction(new Variable(0), new Doub(8.1)), -1.11, -9.209999999999999);
        assertExpressionMatches(new Subtraction(new Int(2), new Doub(8.1)), -1.11, -6.1);
        assertExpressionMatches(new Subtraction(new Doub(8.1), new Int(4)), -1.11, 4.1);
        assertExpressionMatches(new Subtraction(new Int(8), new Int(4)), -1.11, 4);
    }

    @Test
    public void tan() {
        assertExpressionMatches(new Tan(new Variable(0)), 1.11, 2.014338214476828);
        assertExpressionMatches(new Tan(new Variable(0)), -1.11, -2.014338214476828);
        assertExpressionMatches(new Tan(new Int(0)), -1.11, 0);
    }

    @Test
    public void tanh() {
        assertExpressionMatches(new Tanh(new Variable(0)), 1.11, 0.8040623914048921);
        assertExpressionMatches(new Tanh(new Variable(0)), -1.11, -0.8040623914048921);
        assertExpressionMatches(new Tanh(new Int(0)), -1.11, 0);
    }

    @Test
    public void variableWithIntExpression() {
        assertExpressionMatches(new Variable(new Faculty(new Int(0))), 1, 1);
        assertExpressionMatches(new Addition(new Variable(new Faculty(new Int(0))), new Int(5)), 0, 5);
    }

    @Test
    public void parameterWithIntExpression() {
        assertExpressionMatches(new Parameter(new Faculty(new Int(0))), 0, 0);
        assertExpressionMatches(new Addition(new Parameter(new Faculty(new Int(0))), new Int(5)), 0, 5);
    }

    @Test
    public void sumSimple() {
        assertExpressionMatches(new Sum(new Power(new IntVar("k"), new IntVar("k")), "k",
                new Int(1), new Int(5)), 0, 3413);
    }

    @Test
    public void sumComplex() {
        assertExpressionMatches(new Sum(new Sin(new IntVar("k")), "k",
                        new Power(new Int(3), new Int(3)),
                        new Addition(new Power(new Int(3), new Int(3)), new Int(1))),
                0, 1.227281717);
    }

    @Test
    public void sumCornerCase() {
        assertExpressionMatches(new Sum(new Power(new IntVar("k"), new IntVar("k")), "k",
                new Int(5), new Int(5)), 0, 3125);
    }

    @Test
    public void sumEmpty() {
        assertExpressionMatches(new Sum(new Power(new IntVar("k"), new IntVar("k")), "k",
                new Int(10), new Int(5)), 0, 0);
    }

    @Test
    public void prodSimple() {
        assertExpressionMatches(new Prod(new Power(new IntVar("k"), new IntVar("k")), "k",
                new Int(1), new Int(5)), 0, 86400000);
    }

    @Test
    public void prodComplex() {
        assertExpressionMatches(new Prod(new Sin(new IntVar("k")), "k",
                        new Power(new Int(3), new Int(3)),
                        new Addition(new Power(new Int(3), new Int(3)), new Int(1))),
                0, 0.259087774);
    }

    @Test
    public void prodCornerCase() {
        assertExpressionMatches(new Prod(new Power(new IntVar("k"), new IntVar("k")), "k",
                new Int(5), new Int(5)), 0, 3125);
    }

    @Test
    public void prodEmpty() {
        assertExpressionMatches(new Prod(new Power(new IntVar("k"), new IntVar("k")), "k",
                new Int(10), new Int(5)), 0, 1);
    }

    @Test
    public void nestedSumAndProduct() {
        assertExpressionMatches(new Prod(new Sum(new Multiplication(new IntVar("k"), new IntVar("l")),
                        "l", new Int(2), new Int(3)),
                        "k",
                        new Sum(new IntVar("l"), "l", new Int(1), new Int(5)),
                        new Subtraction(new Prod(new IntVar("l"), "l", new Int(1),
                                new Int(4)), new Int(8))),
                0, 6000);
    }

    @Test
    public void auxVar() {
        assertExpressionMatches(new Multiplication(new AuxVar("I"), new AuxVar("I")),
                new Multiplication(new Variable(0), new Variable((0))), 4, 256);
    }

    @Test
    public void auxVarWithInt() {
        assertExpressionMatches(new Multiplication(new AuxVar("I"), new AuxVar("I")),
                new Multiplication(new Int(10), new Int(10)), 4, 10000);
    }

}
