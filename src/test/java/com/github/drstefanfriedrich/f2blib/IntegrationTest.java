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

import org.junit.Test;

import static com.github.drstefanfriedrich.f2blib.util.TestUtil.closeTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Some very simple integration tests for the project.
 */
public class IntegrationTest {

    private final FunctionEvaluationKernel kernel = new FunctionEvaluationFactory().get().create();

    private static final String FUNCTION = "function some.packagename.SomeClassName;" +
            "begin" +
            "    f_1 := p_1 * sin(x_1) + x_2;" +
            "    f_2 := ln(p_2) + exp(x_2);" +
            "end";

    private static final String FOR_LOOP = "function some.packagename.ForLoop;" +
            "begin" +
            "    for i from round(p_1) to round(p_2) step round(p_3);" +
            "    begin" +
            "        f_1 := x_1 * i;" +
            "    end\n" +
            "end";

    private static final String FOR_LOOP_INT_EXPR = "function some.packagename.ForLoopIntExpr;" +
            "begin" +
            "    for z from 2^2 to 5! step 3;" +
            "    begin" +
            "        f_1 := x_1 * z;" +
            "    end\n" +
            "end";

    private static final String GAUSS_SUM = "function some.packagename.GaußSum;" +
            "begin" +
            "    for i from round(p_1) to round(p_2) step round(p_3);" +
            "    begin" +
            "        f_1 := x_1 + i;" +
            "        markov_shift(0);" +
            "    end\n" +
            "end";

    private static final String GAUSS_SUM_USING_SUM = "function some.packagename.GaußSumUsingSum;" +
            "begin" +
            "    f_1 := sum(k, k, 1, 100);" +
            "end";

    private static final String INDEXED_PARAMS_AND_VARS = "function some.packagename.IndexedParameter;" +
            "begin" +
            "    for i from round(p_1) to round(p_2) step round(p_3);" +
            "    begin" +
            "        f_1 := p_{i + 1 + round(sin(x_{i * i + 1}))};" +
            "    end\n" +
            "end";

    @Test
    public void functionFromReadme() {

        kernel.load(FUNCTION);

        double[] x = new double[]{2.51, 1.28};
        double[] p = new double[]{-1.45, 8.27};
        double[] y = new double[2];

        kernel.eval("some.packagename.SomeClassName", p, x, y);

        assertThat(y[0], closeTo(0.423875168));
        assertThat(y[1], closeTo(5.709274235));
    }

    @Test
    public void simpleForLoop() {

        kernel.load(FOR_LOOP);

        double[] x = new double[]{5.5};
        double[] p = new double[]{1, 10, 2};
        double[] y = new double[1];

        kernel.eval("some.packagename.ForLoop", p, x, y);

        assertThat(y[0], closeTo(49.5));
    }

    @Test
    public void simpleForLoopIntExpr() {

        kernel.load(FOR_LOOP_INT_EXPR);

        double[] x = new double[]{2.5};
        double[] p = new double[]{};
        double[] y = new double[1];

        kernel.eval("some.packagename.ForLoopIntExpr", p, x, y);

        assertThat(y[0], closeTo(295));
    }

    /**
     * An (abused) example for the markov_shift.
     */
    @Test
    public void gaußSum() {

        kernel.load(GAUSS_SUM);

        double[] x = new double[]{0};
        double[] p = new double[]{1, 100, 1};
        double[] y = new double[1];

        kernel.eval("some.packagename.GaußSum", p, x, y);

        assertThat(y[0], closeTo(5050));
    }

    @Test
    public void gaußSumUsingSum() {

        kernel.load(GAUSS_SUM_USING_SUM);

        double[] x = new double[]{};
        double[] p = new double[]{};
        double[] y = new double[1];

        kernel.eval("some.packagename.GaußSumUsingSum", p, x, y);

        assertThat(y[0], closeTo(5050));
    }

    @Test
    public void indexedParamsAndVars() {

        kernel.load(INDEXED_PARAMS_AND_VARS);

        double[] x = new double[]{0, 0};
        double[] p = new double[]{0, 1, 1, 1};
        double[] y = new double[1];

        kernel.eval("some.packagename.IndexedParameter", p, x, y);

        assertThat(y[0], closeTo(1));
    }

}
