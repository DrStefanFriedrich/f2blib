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

package com.github.drstefanfriedrich.f2blib;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.drstefanfriedrich.f2blib.util.TestUtil.closeTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * A integration test for various expressions.
 */
public abstract class AbstractExpressionTest {

    private static final String FUNCTION = "function ExpressionTest;\n" +
            "begin\n" +
            "    f_1 := EXPR;\n" +
            "end\n";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private final FunctionEvaluationKernel kernel = instantiateKernel();

    protected abstract FunctionEvaluationKernel instantiateKernel();

    private void assertExpression(String expression, double value) {

        String function = FUNCTION.replace("EXPR", expression);
        kernel.load(function);

        double[] x = new double[]{0, 1, 2};
        double[] p = new double[]{-1, -2, -3};
        double[] y = new double[1];

        kernel.eval("ExpressionTest", p, x, y);

        assertThat(y[0], closeTo(value));
    }

    private void assertWrongExpression(String expression) {

        String function = FUNCTION.replace("EXPR", expression);
        exception.expect(ParseCancellationException.class);
        kernel.load(function);
    }

    @Test
    public void expressionTest01() {
        assertExpression("sin(1)*3+1", 3.524412952);
    }

    @Test
    public void expressionTest02() {
        assertExpression("round(sin(1)*3+1)", 4);
    }

    @Test
    public void expressionTest03() {
        assertExpression("3*pi+1", 10.42477796);
    }

    @Test
    public void expressionTest04() {
        assertExpression("3*4^tan(x_2)", 25.98807648);
    }

    @Test
    public void expressionTest05() {
        assertExpression("x_1*p_1+x_2*p_2+x_3*p_3", -8);
    }

    @Test
    public void expressionTest06() {
        assertExpression("ln(euler)", 1);
    }

    @Test
    public void expressionTest07() {
        assertExpression("sin(2*pi*1000+pi/2)", 1);
    }

    @Test
    public void expressionTest08() {
        assertExpression("exp(1)", Math.E);
    }

    @Test
    public void expressionTest09() {
        assertExpression("euler^1", Math.E);
    }

    @Test
    public void expressionTest10() {
        assertExpression("euler^5!", 1.304180878e52);
    }

    @Test
    public void expressionTest11() {
        assertExpression("5!^euler", 448539.4005);
    }

    @Test
    public void expressionTest12() {
        assertWrongExpression("(euler^1)!");
    }

    @Test
    public void expressionTest13() {
        assertWrongExpression("5*(!^euler)");
    }

    @Test
    public void expressionTest14() {
        assertExpression("2^2!", 4);
    }

    @Test
    public void expressionTest15() {
        assertExpression("2^(2!)", 4);
    }

    @Test
    public void expressionTest16() {
        assertWrongExpression("2^sin(1)!");
    }

    @Test
    public void expressionTest17() {
        assertExpression("2^3*sin(2)", 7.274379408);
    }

    @Test
    public void expressionTest18() {
        assertExpression("2^sin(2)*3", 5.63439195);
    }

    @Test
    public void expressionTest19() {
        assertWrongExpression("x_{sin(1)}");
    }

    @Test
    public void expressionTest20() {
        assertExpression("p_{round(sin(x_3))}", -1);
    }

    @Test
    public void expressionTest21() {
        assertExpression("10/2", 5);
    }

    @Test
    public void expressionTest22() {
        assertExpression("1000 - (-1000)", 2000);
    }

    @Test
    public void expressionTest23() {
        assertExpression("-1", -1);
    }

    @Test
    public void expressionTest24() {
        assertExpression("+1", 1);
    }

    @Test
    public void expressionTest25() {
        assertExpression("round(10)", 10);
    }

    @Test
    public void expressionTest26() {
        assertWrongExpression("sum_{k=1}^{100}(k)");
    }

    @Test
    public void expressionTest27() {
        assertExpression("sum(k,k,1,100)", 5050);
    }

    @Test
    public void expressionTest28() {
        assertWrongExpression("prod_{k=1}^{5}(k)");
    }

    @Test
    public void expressionTest29() {
        assertExpression("prod(k,k,1,5)", 120);
    }

    @Test
    public void expressionTest30() {
        assertWrongExpression("sum_{k=1}^{4}(prod_{l=1}^{k}(l))");
    }

    @Test
    public void expressionTest31() {
        assertExpression("sum(prod(l,l,1,k),k,1,4)", 33);
    }

    @Test
    public void expressionTest32() {
        // Adapted example from life insurance
        assertExpression("sum((1-(1/2)^(k+1)) * (6 + 2 + k) * prod(1 - (6 + l), l, 2, 2 + k - 1), k, 0, 1)", -43.25);
    }

    @Test
    public void expressionTest33() {
        assertExpression("sum(sum(k+l,l,1,3),k,1,3)", 36);
    }

    @Test
    public void expressionTest34() {
        assertExpression("1", 1);
    }

    @Test
    public void expressionTest35() {
        assertExpression("(2^2)!", 24);
    }

    @Test
    public void expressionTest36() {
        assertExpression("prod(prod(n+m,m,n-1,n+1),n,2,3)", 12600);
    }

    @Test
    public void expressionTest37() {
        assertExpression("1/2", 0.5);
    }

    @Test
    public void expressionTest38() {
        assertExpression("1/2 * 2", 1);
    }

    @Test
    public void expressionTest39() {
        assertExpression("2*1/2", 1);
    }

    @Test
    public void expressionTest40() {
        assertExpression("(-1)^3/3!", -1 / (double) 6);
    }

}
