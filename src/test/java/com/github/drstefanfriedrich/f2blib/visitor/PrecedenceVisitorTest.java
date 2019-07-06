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

package com.github.drstefanfriedrich.f2blib.visitor;

import com.github.drstefanfriedrich.f2blib.ast.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Some visitor methods of the {@link PrecedenceVisitor} must not be called.
 * This will be verified in this test class.
 */
public class PrecedenceVisitorTest {

    private final PrecedenceVisitor underTest = new PrecedenceVisitor();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private void assertException() {
        exception.expect(IllegalStateException.class);
        exception.expectMessage("This method must not be called on the PrecedenceVisitor class");
    }

    @Test
    public void visitFunction() {

        assertException();

        new Function(0, new Int(1)).accept(underTest);
    }

    @Test
    public void visitFunctionBody() {

        assertException();

        new FunctionBody(new FunctionsWrapper(new Function(0, new Int(0)))).accept(underTest);
    }

    @Test
    public void visitFunctionDefinition() {

        assertException();

        new FunctionDefinition("F", new FunctionBody(new FunctionsWrapper(new Function(0, new Int(0))))).accept(underTest);
    }

    @Test
    public void visitFunctions() {

        assertException();

        new FunctionsWrapper(new Function(0, new Int(0))).accept(underTest);
    }

    @Test
    public void visitNoOp() {

        assertException();

        NoOp.get().accept(underTest);
    }

    @Test
    public void visitForLoop() {

        assertException();

        new ForLoop("k", 0, 1, 2,
                new FunctionsWrapper(new Function(0, new ForVar("k")))).accept(underTest);
    }

}
