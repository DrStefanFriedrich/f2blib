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

import org.f2blib.PerformanceTestFunction;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class F2BLibImplTest extends AbstractF2BLibImplTest {

    @Test
    public void cacheWorks() {

        doReturn(PerformanceTestFunction.class).when(generatorMock).generateClass(any());

        underTest.load("");
        underTest.eval(PerformanceTestFunction.class.getName(), null, null, null);

        // Must be reached without throwing an exception
        assertTrue(true);
    }

    @Test
    public void unknownFunction() {

        exception.expect(IllegalArgumentException.class);

        underTest.eval("unknown", null, null, null);
    }

    @Test
    public void parserAndGeneratorAreBeingCalled() {

        doReturn(PerformanceTestFunction.class).when(generatorMock).generateClass(any());

        underTest.load("");

        verify(parserMock).applyListener(eq(""), any());
        verify(generatorMock).generateClass(any());
    }

    @Test
    public void invocationTargetException() {

        doReturn(ConstructorThrowing.class).when(generatorMock).generateClass(any());

        exception.expect(ArrayIndexOutOfBoundsException.class);

        underTest.load("");
    }

    public static class ConstructorThrowing implements FunctionEvaluation {

        public ConstructorThrowing() {
            throw new ArrayIndexOutOfBoundsException();
        }

        @Override
        public void eval(double[] p, double[] x, double[] y) {
        }
    }

}
