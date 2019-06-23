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

import com.github.drstefanfriedrich.f2blib.generator.FunctionEvaluationBytecodeGenerator;
import com.github.drstefanfriedrich.f2blib.parser.FunctionParser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;

public abstract class AbstractF2BLibImplTest {

    public static class FunctionEvaluationAsset implements FunctionEvaluation {
        @Override
        public void eval(double[] p, double[] x, double[] y) {
            y[0] = 1.234;
        }
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    F2BLibImpl underTest;

    FunctionParser parserMock;

    FunctionEvaluationBytecodeGenerator generatorMock;

    @Before
    public void setup() {
        parserMock = mock(FunctionParser.class);
        generatorMock = mock(FunctionEvaluationBytecodeGenerator.class);

        underTest = new F2BLibImpl(parserMock, generatorMock);
    }

}
