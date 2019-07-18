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

package com.github.drstefanfriedrich.f2blib.impl;

import com.github.drstefanfriedrich.f2blib.FunctionEvaluationKernel;
import com.github.drstefanfriedrich.f2blib.ast.*;
import org.junit.Test;

import static com.github.drstefanfriedrich.f2blib.util.TestUtil.closeTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class F2BLibImplTest extends AbstractF2BLibImplTest {

    private static final String FUNCTION_1 = "function Func;\n" +
            "begin\n" +
            "    f_1 := 2 * x_1;\n" +
            "end";

    private static final String FUNCTION_2 = "function Func;\n" +
            "begin\n" +
            "    f_1 := 4 * x_1 + 100;\n" +
            "end";

    @Test
    public void cacheWorks() {

        FunctionDefinition fd = new FunctionDefinition("f", new FunctionBody(new FunctionsWrapper(new Function(0, new Variable(0)))));
        double[] y = new double[1];

        when(parserMock.parse("someFakeDefinition")).thenReturn(fd);
        when(generatorMock.generateAndInstantiate(fd)).thenReturn(new FunctionEvaluationAsset());

        underTest.load("someFakeDefinition");

        underTest.eval(FunctionEvaluationAsset.class.getName(), new double[0], new double[0], y);

        assertThat(y[0], closeTo(1.234));
    }

    @Test
    public void unknownFunction() {

        exception.expect(IllegalArgumentException.class);

        underTest.eval("unknown", null, null, null);
    }

    @Test
    public void loadSameFunctionTwice() {
        double[] y = new double[1];

        FunctionEvaluationKernel fek = new F2BLibAssembler().create();

        for (int i = 0; i <= 1; i++) {

            fek.load(FUNCTION_1);
            fek.eval("Func", new double[0], new double[]{1.63}, y);

            assertThat(y[0], closeTo(3.26));
        }
    }

    @Test
    public void loadDifferentFunctionTwice() {
        double[] y = new double[1];

        FunctionEvaluationKernel fek = new F2BLibAssembler().create();

        fek.load(FUNCTION_1);
        fek.eval("Func", new double[0], new double[]{1.63}, y);

        assertThat(y[0], closeTo(3.26));

        fek.load(FUNCTION_2);
        fek.eval("Func", new double[0], new double[]{1.63}, y);

        assertThat(y[0], closeTo(106.52));
    }

    @Test
    public void removeAndRemoveTwice() {

        FunctionDefinition fd = new FunctionDefinition("f", new FunctionBody(new FunctionsWrapper(new Function(0, new Variable(0)))));
        double[] y = new double[1];

        when(parserMock.parse("someFakeDefinition")).thenReturn(fd);
        when(generatorMock.generateAndInstantiate(fd)).thenReturn(new FunctionEvaluationAsset());

        underTest.load("someFakeDefinition");

        assertThat(underTest.remove(FunctionEvaluationAsset.class.getName()), is(true));
        assertThat(underTest.remove(FunctionEvaluationAsset.class.getName()), is(false));
    }

    @Test
    public void list() {

        FunctionDefinition fd = new FunctionDefinition("f", new FunctionBody(new FunctionsWrapper(new Function(0, new Variable(0)))));
        double[] y = new double[1];

        when(parserMock.parse("someFakeDefinition")).thenReturn(fd);
        when(generatorMock.generateAndInstantiate(fd)).thenReturn(new FunctionEvaluationAsset());

        underTest.load("someFakeDefinition");

        assertThat(underTest.list().size(), is(1));
        assertThat(underTest.list(), hasItem(FunctionEvaluationAsset.class.getName()));
    }

}
