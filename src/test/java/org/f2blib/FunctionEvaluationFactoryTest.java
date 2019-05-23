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

package org.f2blib;

import org.f2blib.impl.F2BLibAssembler;
import org.f2blib.impl.F2BLibImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class FunctionEvaluationFactoryTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void defaultImplementation() {

        FunctionEvaluationProvider provider = FunctionEvaluationFactory.get();

        assertThat(provider, notNullValue());

        FunctionEvaluationKernel kernel = provider.create();

        assertThat(kernel, notNullValue());
    }

    @Test
    public void implementationByName() {

        FunctionEvaluationProvider provider = FunctionEvaluationFactory.get("f2blib");

        assertThat(provider, notNullValue());
        assertThat(provider, instanceOf(F2BLibAssembler.class));

        FunctionEvaluationKernel kernel = provider.create();

        assertThat(kernel, notNullValue());
    }

    @Test
    public void unknownImplementation() {

        exception.expect(RuntimeException.class);

        FunctionEvaluationProvider provider = FunctionEvaluationFactory.get("x");
    }

}
