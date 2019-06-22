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

package org.f2blib.visitor;

import org.f2blib.ast.*;
import org.junit.Before;
import org.junit.Test;

import static org.f2blib.ast.ASTTest.FUNCTION_NAME;
import static org.f2blib.ast.ASTTest.createFunctionDefinition;
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

}
