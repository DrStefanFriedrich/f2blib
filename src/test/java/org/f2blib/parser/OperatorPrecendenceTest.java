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

package org.f2blib.parser;

import org.f2blib.FunctionsListener;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for operator precendence.
 */
public class OperatorPrecendenceTest {

    private final FunctionParser parser = new AntlrFunctionParser();

    private FunctionsListener listener;

    @Before
    public void setup() {
        listener = new BytecodeGeneratingFunctionsListener();
    }

    @Test
    public void multiplicationOveraddition() {
    }

}
