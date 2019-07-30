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
 * A integration test for various expressions, integer expressions as well as
 * double expression.
 */
public class ExpressionTest {

    private static final String FUNCTION = "function ExpressionTest;\n" +
            "begin\n" +
            "    f_1 := EXPR;\n" +
            "end\n";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private final FunctionEvaluationKernel kernel = new FunctionEvaluationFactory().get().create();

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
    public void variousExpressions() {
        assertExpression("sin(1)*3+1", 3.524412952);
        assertExpression("round(sin(1)*3+1)", 4);
        assertExpression("3*pi+1", 10.42477796);
        assertExpression("3*4^tan(x_2)", 25.98807648);
        assertExpression("x_1*p_1+x_2*p_2+x_3*p_3", -8);
        assertExpression("ln(e)", 1);
        assertExpression("sin(2*pi*1000+pi/2)", 1);
        assertExpression("exp(1)", Math.E);
        assertExpression("e^1", Math.E);
        assertExpression("e^5!", 1.304180878e52);
        assertExpression("5!^e", 448539.4005);
        assertWrongExpression("(e^1)!");
        assertWrongExpression("5*(!^e)");
        assertExpression("2^2!", 24);
        assertExpression("2^(2!)", 4);
        assertWrongExpression("2^sin(1)!");
        assertExpression("2^3*sin(2)", 7.274379408);
        assertExpression("2^sin(2)*3", 5.63439195);
        assertWrongExpression("x_{sin(1)}");
        assertExpression("p_{round(sin(x_3)}", -1);
    }

}
