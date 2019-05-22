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

package org.f2blib.grammar;

import org.f2blib.BytecodeGeneratingFunctionsListener;
import org.f2blib.FunctionParser;
import org.f2blib.FunctionsListener;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GrammarTest {

    private final FunctionParser parser = new FunctionParser();

    private final FunctionsListener listener = new BytecodeGeneratingFunctionsListener();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void allFails() {

        exception.expect(RuntimeException.class);

        parser.applyListener("x", listener);
    }

}
