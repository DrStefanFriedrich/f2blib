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
import org.junit.Before;
import org.junit.Test;

import static com.github.drstefanfriedrich.f2blib.ast.ASTTest.FUNCTION_NAME;
import static com.github.drstefanfriedrich.f2blib.ast.ASTTest.createFunctionDefinition;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests of the stack depth calculation.
 */
public class StackDepthVisitorImplTest {

    private StackDepthVisitorImpl underTest;

    @Before
    public void setup() {
        underTest = new StackDepthVisitorImpl();
    }

    @Test
    public void emptyFunctionDefinition() {

        FunctionDefinition fd = new FunctionDefinition(FUNCTION_NAME,
                new FunctionBody(new FunctionsWrapper(new Function(0, NoOp.get()))));

        fd.accept(underTest);

        assertThat(underTest.getMaxStackDepth(), is(2));
    }

    @Test
    public void constantFunctionDefinition() {

        FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME, Constant.PI);

        fd.accept(underTest);

        assertThat(underTest.getMaxStackDepth(), is(4));
    }

    @Test
    public void minimalFunctionDefinition() {

        FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME, new Variable(0));

        fd.accept(underTest);

        assertThat(underTest.getMaxStackDepth(), is(4));
    }

    @Test
    public void littleBitMoreComplexFunctionDefinition() {

        FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME,
                new Sin(new Addition(new Variable(0), new Parameter(0))));

        fd.accept(underTest);

        assertThat(underTest.getMaxStackDepth(), is(6));
    }

    @Test
    public void normalFunctionDefinition() {

        FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME,
                new Sin(new Multiplication(new Arctan(new Sinh(new Parameter(5))),
                        new Addition(new Pos(new Power(new Variable(1), new Faculty(new Int(10)))),
                                new Parameter(0)))));

        fd.accept(underTest);

        assertThat(underTest.getMaxStackDepth(), is(10));
    }

    @Test
    public void functionWithForLoop() {

        FunctionDefinition fd = new FunctionDefinition("ForLoop", new FunctionBody(new ForLoop("k", new Int(0), new Int(1), new Int(2),
                new FunctionsWrapper(new Function(0, new IntVar("k"))))));

        fd.accept(underTest);

        assertThat(underTest.getMaxStackDepth(), is(8));
    }

    @Test
    public void functionWithForLoopAndMarkovShift() {

        FunctionDefinition fd = new FunctionDefinition("ForLoop", new FunctionBody(new ForLoop("k", new Int(0), new Int(1), new Int(2),
                new FunctionsWrapper(new MarkovShift(new Int(0)), new Function(0, new IntVar("k"))))));

        fd.accept(underTest);

        assertThat(underTest.getMaxStackDepth(), is(9));
    }

    @Test
    public void variableWithIntExpression() {

        FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME,
                new Sin(new Variable(new Addition(new Int(0), new Int(1)))));

        fd.accept(underTest);

        assertThat(underTest.getMaxStackDepth(), is(8));
    }

    @Test
    public void parameterWithIntExpression() {

        FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME,
                new Sin(new Parameter(new Addition(new Int(0), new Int(1)))));

        fd.accept(underTest);

        assertThat(underTest.getMaxStackDepth(), is(8));
    }

    @Test
    public void sumAndProd() {

        FunctionDefinition fd = createFunctionDefinition(FUNCTION_NAME,
                new Prod(new Sum(new Multiplication(new IntVar("k"), new IntVar("l")),
                        "l", new Int(2), new Int(3)),
                        "k",
                        new Sum(new IntVar("l"), "l", new Int(1), new Int(5)),
                        new Subtraction(new Prod(new IntVar("l"), "l", new Int(1),
                                new Int(4)), new Int(8))));

        fd.accept(underTest);

        assertThat(underTest.getMaxStackDepth(), is(12));
    }

}
