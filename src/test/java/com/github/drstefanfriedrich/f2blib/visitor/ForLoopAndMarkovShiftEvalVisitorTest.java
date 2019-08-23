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
import com.github.drstefanfriedrich.f2blib.exception.BytecodeGenerationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.drstefanfriedrich.f2blib.util.TestUtil.closeTo;
import static org.junit.Assert.assertThat;

/**
 * Tests for for loops and the Markov shift.
 */
public class ForLoopAndMarkovShiftEvalVisitorTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private final FunctionDefinition fd = new FunctionDefinition("ForLoopEvalVisitorTestFunction", new FunctionBody(
            new ForLoop("i", new Round(new Parameter(0)), new Round(new Parameter(1)), new Round(new Parameter(2)),
                    new FunctionsWrapper(new Function(0, new IntVar("i"))))));

    private final FunctionDefinition gaußSum = new FunctionDefinition("GaußSumVisitorTestFunction", new FunctionBody(
            new ForLoop("i", new Round(new Parameter(0)), new Round(new Parameter(1)), new Round(new Parameter(2)),
                    new FunctionsWrapper(new MarkovShift(new Int(0)), new Function(0, new Addition(new Variable(0),
                            new IntVar("i")))))));

    private final FunctionDefinition markovShiftWithNegativeOffset = new FunctionDefinition("GaußSumVisitorTestFunction", new FunctionBody(
            new ForLoop("i", new Round(new Parameter(0)), new Round(new Parameter(1)), new Round(new Parameter(2)),
                    new FunctionsWrapper(new MarkovShift(new Int(-1)), new Function(0, new Addition(new Variable(1),
                            new IntVar("i")))))));

    private final FunctionDefinition markovShiftWithHugeOffset = new FunctionDefinition("GaußSumVisitorTestFunction", new FunctionBody(
            new ForLoop("i", new Round(new Parameter(0)), new Round(new Parameter(1)), new Round(new Parameter(2)),
                    new FunctionsWrapper(new MarkovShift(new Int(10)), new Function(0, new Addition(new Variable(1),
                            new IntVar("i")))))));

    private EvalVisitor evalVisitor;

    private double[] x;

    private double[] p;

    @Before
    public void setup() {
        x = new double[1];
        p = new double[3];
        evalVisitor = new EvalVisitor(x, p, 1);
    }

    @Test
    public void forLoopWithZeroStepAndUnequalStartEnd() {

        p[0] = 1;
        p[1] = 2;

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("step evaluating to 0 not allowed");

        fd.accept(evalVisitor);
    }

    @Test
    public void forLoopWithZeroStepAndEqualStartEnd() {

        fd.accept(evalVisitor);

        assertThat(evalVisitor.getResult()[0], closeTo(0));
    }

    @Test
    public void forLoopWithPositiveStep() {

        p[0] = 3;
        p[1] = 10;
        p[2] = 2;

        fd.accept(evalVisitor);

        assertThat(evalVisitor.getResult()[0], closeTo(9));
    }

    @Test
    public void forLoopWithNegativeStep() {

        p[0] = 10;
        p[1] = -6;
        p[2] = -2;

        fd.accept(evalVisitor);

        assertThat(evalVisitor.getResult()[0], closeTo(-6));
    }

    @Test
    public void forLoopWithPositiveStepAndImmediateReturn() {

        p[0] = 12;
        p[1] = 10;
        p[2] = 2;

        fd.accept(evalVisitor);

        assertThat(evalVisitor.getResult()[0], closeTo(0));
    }

    @Test
    public void forLoopWithNegativeStepAndImmediateReturn() {

        p[0] = -8;
        p[1] = -6;
        p[2] = -2;

        fd.accept(evalVisitor);

        assertThat(evalVisitor.getResult()[0], closeTo(0));
    }

    @Test
    public void gaußSum() {

        x = new double[]{0};
        p = new double[]{1, 100, 1};
        evalVisitor = new EvalVisitor(x, p, 1);

        gaußSum.accept(evalVisitor);

        assertThat(evalVisitor.getResult()[0], closeTo(5050));
    }

    @Test
    public void markovShiftWithNegativeOffset() {

        x = new double[]{0, 0};
        p = new double[]{1, 100, 1};
        evalVisitor = new EvalVisitor(x, p, 1);

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("offset must not be negative");

        markovShiftWithNegativeOffset.accept(evalVisitor);
    }

    @Test
    public void markovShiftWithHugeOffset() {

        x = new double[]{0, 0};
        p = new double[]{1, 100, 1};
        evalVisitor = new EvalVisitor(x, p, 1);

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("x.lenth - offset must be greater or equal than y.length");

        markovShiftWithHugeOffset.accept(evalVisitor);
    }

}
