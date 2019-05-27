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

package org.f2blib.impl;

import org.f2blib.generator.FunctionEvaluationBytecodeGenerator;
import org.f2blib.parser.FunctionParser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;

public abstract class AbstractF2BLibImplTest {

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