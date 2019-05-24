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

import org.f2blib.FunctionEvaluationKernel;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class F2BLibAssemblerTest {

    private final F2BLibAssembler underTest = new F2BLibAssembler();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void correctImplementation() {

        FunctionEvaluationKernel kernel = underTest.create();

        assertThat(kernel, instanceOf(F2BLibImpl.class));
    }

    @Test
    public void correctKernelIdentifier() {

        assertThat(underTest.getKernelIdentifier(), is("f2blib"));
    }

    @Test
    public void wrongInternalType() {

        F2BLibAssembler assembler = new F2BLibAssemblerAsset();

        exception.expect(IllegalArgumentException.class);

        assembler.create();
    }

    private static class F2BLibAssemblerAsset extends F2BLibAssembler {

        @Override
        public FunctionEvaluationKernel create() {
            getBean(FunctionEvaluation.class);
            return null;
        }

    }

}
