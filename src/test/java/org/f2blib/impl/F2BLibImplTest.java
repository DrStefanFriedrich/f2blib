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

import org.f2blib.ast.*;
import org.junit.Test;

import static org.f2blib.util.TestUtil.closeTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class F2BLibImplTest extends AbstractF2BLibImplTest {

    @Test
    public void cacheWorks() {

        FunctionDefinition fd = new FunctionDefinition("f", new FunctionBody(new Functions(new Function(0, new Variable(0)))));
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

}
