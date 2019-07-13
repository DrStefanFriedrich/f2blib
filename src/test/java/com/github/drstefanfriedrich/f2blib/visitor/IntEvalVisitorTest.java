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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for {@link IntEvalVisitor}.
 */
public class IntEvalVisitorTest {

    private final double[] x = new double[]{1};

    private final double[] p = new double[]{1};

    private final EvalVisitor evalVisitor = new EvalVisitor(x, p, 1);

    private final IntEvalVisitor underTest = new IntEvalVisitor(evalVisitor);

    @Test
    public void binomial() {

        int result = new Binomial(new Int(10), new Int(5)).accept(underTest);

        assertThat(result, is(252));
    }

    @Test
    public void faculty() {

        int result = new Faculty(new Int(5)).accept(underTest);

        assertThat(result, is(120));
    }

    @Test
    public void integer() {

        int result = new Int(5).accept(underTest);

        assertThat(result, is(5));
    }

    @Test
    public void round() {

        int result = new Round(new Doub(2.2)).accept(underTest);

        assertThat(result, is(2));
    }

}
