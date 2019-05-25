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

package org.f2blib.generator;

import org.f2blib.impl.FunctionEvaluation;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class FunctionEvaluationBytecodeGeneratorTest {

    private final FunctionEvaluationBytecodeGenerator underTest = new FunctionEvaluationBytecodeGenerator();

    @Test
    public void returnNull() {

        Class<? extends FunctionEvaluation> clazz = underTest.generateClass(null);

        assertThat(clazz, notNullValue());
    }

}
