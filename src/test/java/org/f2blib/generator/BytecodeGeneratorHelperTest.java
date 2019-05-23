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

import org.f2blib.FunctionEvaluation;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BytecodeGeneratorHelperTest {

    private final BytecodeGeneratorHelper underTest = new BytecodeGeneratorHelper();

    @Test
    public void emptyImplementation() throws IllegalAccessException, InstantiationException {

        Class<? extends FunctionEvaluation> clazz = underTest.generate("a.b.c.Def");
        FunctionEvaluation instance = clazz.newInstance();

        instance.eval(null, null, null);

        assertThat(true, is(true));
    }

}
