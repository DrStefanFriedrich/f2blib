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
import com.github.drstefanfriedrich.f2blib.ast.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Some visitor methods of the {@link SymbolVisitor} must not be called. This
 * will be verified in this test class.
 */
public class SymbolVisitorTest {

    private final SymbolVisitor underTest = new SymbolVisitor();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private void assertException() {
        exception.expect(IllegalStateException.class);
        exception.expectMessage("This method must not be called on the SymbolVisitor class");
    }

    @Test
    public void visitConstant() {

        assertException();

        Constant.E.accept(underTest);
    }

    @Test
    public void visitFunction() {

        assertException();

        new Function(0, new Int(1)).accept(underTest);
    }

    @Test
    public void visitFunctionBody() {

        assertException();

        new FunctionBody(new FunctionsWrapper(new Function(0, new Int(1)))).accept(underTest);
    }

    @Test
    public void visitFunctionDefinition() {

        assertException();

        new FunctionDefinition("F", new FunctionBody(new FunctionsWrapper(new Function(0, new Int(1))))).accept(underTest);
    }

    @Test
    public void visitFunctions() {

        assertException();

        new FunctionsWrapper(new Function(0, new Int(3))).accept(underTest);
    }

    @Test
    public void visitInt() {

        assertException();

        new Int(1).accept(underTest);
    }

    @Test
    public void visitParameter() {

        assertException();

        new Parameter(0).accept(underTest);
    }

    @Test
    public void visitVariable() {

        assertException();

        new Variable(0).accept(underTest);
    }

    @Test
    public void visitDoub() {

        assertException();

        new Doub(1.11).accept(underTest);
    }

}
