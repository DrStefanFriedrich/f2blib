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

package org.f2blib.visitor;

import org.f2blib.ast.*;
import org.f2blib.exception.BytecodeGenerationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ValidationVisitorImplTest {

    private ValidationVisitorImpl underTest;

    private FunctionDefinition createFunctionDefinition(Function... functions) {
        return new FunctionDefinition("ValidationVisitorFunctionTest", new FunctionBody(new Functions(functions)));
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        underTest = new ValidationVisitorImpl();
    }

    @Test
    public void emptyFunctionDefinition() {

        FunctionDefinition fd = createFunctionDefinition(new Function[0]);

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("Empty function definition is not allowed");

        fd.accept(underTest);
    }

    @Test
    public void correctFunctionDefinition() {

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Int(1)),
                new Function(1, Constant.PI));

        fd.accept(underTest);

        LocalVariables localVariables = underTest.getLocalVariables();

        assertThat(localVariables.parameterIndexIterator().hasNext(), is(false));
        assertThat(localVariables.variableIndexIterator().hasNext(), is(false));
        assertThat(localVariables.getMaxLocals(), is(4));
        assertThat(localVariables.getMaxStack(), is(1000));
    }

    @Test
    public void functionDefinedTwiceExactlyTheSame() {

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Int(1)),
                new Function(1, Constant.PI),
                new Function(1, Constant.PI),
                new Function(2, Constant.BOLTZMANN));

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("Functions must not be defined twice: f_1 already defined");

        fd.accept(underTest);
    }

    @Test
    public void functionDefinedTwiceDifferently() {

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Int(1)),
                new Function(1, Constant.PI),
                new Function(1, Constant.E),
                new Function(2, Constant.BOLTZMANN));

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("Functions must not be defined twice: f_1 already defined");

        fd.accept(underTest);
    }

    @Test
    public void functionDefinitionInTheMiddleMissing() {

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Int(1)),
                new Function(1, Constant.PI),
                new Function(3, Constant.PI));

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("Functions must be defined consecutively; gaps are not allowed");

        fd.accept(underTest);
    }

    @Test
    public void functionDefinitionAtTheBeginningMissing() {

        FunctionDefinition fd = createFunctionDefinition(new Function(1, new Int(1)),
                new Function(2, Constant.PI),
                new Function(3, Constant.PI));

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("Functions must be defined consecutively; gaps are not allowed");

        fd.accept(underTest);
    }

    @Test
    public void arsinhPresent() {

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Arsinh(new Int(1))),
                new Function(1, Constant.PI));

        fd.accept(underTest);

        assertThat(underTest.getLocalVariables().isArsinhUsed(), is(true));
    }

    @Test
    public void arsinhNotPresent() {

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Arcosh(new Int(1))),
                new Function(1, Constant.PI));

        fd.accept(underTest);

        assertThat(underTest.getLocalVariables().isArsinhUsed(), is(false));
    }

    @Test
    public void arcoshPresent() {

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Int(1)),
                new Function(1, new Arcosh(Constant.PI)));

        fd.accept(underTest);

        assertThat(underTest.getLocalVariables().isArcoshUsed(), is(true));
    }

    @Test
    public void arcoshNotPresent() {

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Int(1)),
                new Function(1, new Arsinh(Constant.PI)));

        fd.accept(underTest);

        assertThat(underTest.getLocalVariables().isArcoshUsed(), is(false));
    }

    @Test
    public void artanhPresent() {

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Artanh(new Variable(0))),
                new Function(1, Constant.PI));

        fd.accept(underTest);

        assertThat(underTest.getLocalVariables().isArtanhUsed(), is(true));
    }

    @Test
    public void artanhNotPresent() {

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Tanh(new Variable(0))),
                new Function(1, Constant.PI));

        fd.accept(underTest);

        assertThat(underTest.getLocalVariables().isArtanhUsed(), is(false));
    }

    @Test
    public void parametersAndVariables() {

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Parameter(4)),
                new Function(1, new Variable(6)));

        fd.accept(underTest);

        LocalVariables localVariables = underTest.getLocalVariables();

        assertThat(localVariables.getMaxLocals(), is(28));
        assertThat(localVariables.getIndexForVariable(new Variable(0)), is(14));
        assertThat(localVariables.getIndexForVariable(new Variable(1)), is(16));
        assertThat(localVariables.getIndexForVariable(new Variable(2)), is(18));
        assertThat(localVariables.getIndexForVariable(new Variable(3)), is(20));
        assertThat(localVariables.getIndexForVariable(new Variable(4)), is(22));
        assertThat(localVariables.getIndexForVariable(new Variable(5)), is(24));
        assertThat(localVariables.getIndexForVariable(new Variable(6)), is(26));
        assertThat(localVariables.getIndexForParameter(new Parameter(0)), is(4));
        assertThat(localVariables.getIndexForParameter(new Parameter(1)), is(6));
        assertThat(localVariables.getIndexForParameter(new Parameter(2)), is(8));
        assertThat(localVariables.getIndexForParameter(new Parameter(3)), is(10));
        assertThat(localVariables.getIndexForParameter(new Parameter(4)), is(12));
    }

    @Test
    public void accessNonExistingParameter() {

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Parameter(4)),
                new Function(1, new Variable(6)));

        fd.accept(underTest);

        LocalVariables localVariables = underTest.getLocalVariables();

        exception.expect(NullPointerException.class);

        localVariables.getIndexForParameter(new Parameter(10));
    }

    @Test
    public void accessNonExistingVariable() {

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Parameter(4)),
                new Function(1, new Variable(6)));

        fd.accept(underTest);

        LocalVariables localVariables = underTest.getLocalVariables();

        exception.expect(NullPointerException.class);

        localVariables.getIndexForVariable(new Variable(10));
    }

}
