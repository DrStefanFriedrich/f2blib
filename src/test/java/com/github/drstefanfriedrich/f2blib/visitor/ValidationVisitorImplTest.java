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
import com.github.drstefanfriedrich.f2blib.exception.BytecodeGenerationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class ValidationVisitorImplTest {

    private ValidationVisitorImpl underTest;

    private FunctionDefinition createFunctionDefinition(Function... functions) {
        return new FunctionDefinition("ValidationVisitorFunctionTest", new FunctionBody(new FunctionsWrapper(functions)));
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        underTest = new ValidationVisitorImpl();
    }

    @Test
    public void emptyFunctionDefinition() {

        FunctionDefinition fd = createFunctionDefinition();

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

        assertThat(localVariables.getMaxLocals(), is(15));
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

        assertThat(underTest.getSpecialFunctionsUsage().isArsinhUsed(), is(true));
    }

    @Test
    public void arsinhNotPresent() {

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Arcosh(new Int(1))),
                new Function(1, Constant.PI));

        fd.accept(underTest);

        assertThat(underTest.getSpecialFunctionsUsage().isArsinhUsed(), is(false));
    }

    @Test
    public void arcoshPresent() {

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Int(1)),
                new Function(1, new Arcosh(Constant.PI)));

        fd.accept(underTest);

        assertThat(underTest.getSpecialFunctionsUsage().isArcoshUsed(), is(true));
    }

    @Test
    public void arcoshNotPresent() {

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Int(1)),
                new Function(1, new Arsinh(Constant.PI)));

        fd.accept(underTest);

        assertThat(underTest.getSpecialFunctionsUsage().isArcoshUsed(), is(false));
    }

    @Test
    public void artanhPresent() {

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Artanh(new Variable(0))),
                new Function(1, Constant.PI));

        fd.accept(underTest);

        assertThat(underTest.getSpecialFunctionsUsage().isArtanhUsed(), is(true));
    }

    @Test
    public void artanhNotPresent() {

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Tanh(new Variable(0))),
                new Function(1, Constant.PI));

        fd.accept(underTest);

        assertThat(underTest.getSpecialFunctionsUsage().isArtanhUsed(), is(false));
    }

    @Test
    public void parametersAndVariables() {

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Parameter(4)),
                new Function(1, new Variable(6)));

        fd.accept(underTest);

        LocalVariables localVariables = underTest.getLocalVariables();

        assertThat(localVariables.getMaxLocals(), is(15));
        assertThat(localVariables.getIndexForForLoopEnd(), is(4));
        assertThat(localVariables.getIndexForForLoopStep(), is(5));
        assertThat(localVariables.getMarkovShiftOffset(), is(6));
        assertThat(localVariables.getMarkovShiftM(), is(7));
        assertThat(localVariables.getMarkovShiftN(), is(8));
        assertThat(localVariables.getMarkovShiftStart(), is(9));
        assertThat(localVariables.getMarkovShiftEnd(), is(10));
    }

    @Test
    public void forLoop() {

        FunctionDefinition fd = new FunctionDefinition("x.y.T", new FunctionBody(new ForLoop("j", new Int(0), new Int(1), new Int(2),
                new FunctionsWrapper(new Function(0, new IntVar("j"))))));

        fd.accept(underTest);

        assertThat(true, is(true));
    }

    @Test
    public void forLoopWithWrongInnerVariable() {

        FunctionDefinition fd = new FunctionDefinition("x.y.T", new FunctionBody(new ForLoop("k", new Int(0), new Int(1), new Int(2),
                new FunctionsWrapper(new Function(0, new IntVar("l"))))));

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("The variable 'l' is not defined");

        fd.accept(underTest);
    }

    @Test
    public void forLoopWithTooLongVariableName() {

        FunctionDefinition fd = new FunctionDefinition("x.y.T", new FunctionBody(new ForLoop("kk", new Int(0), new Int(1), new Int(2),
                new FunctionsWrapper(new Function(0, new IntVar("kk"))))));

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("The variable name 'kk' is not allowed.");

        fd.accept(underTest);
    }

    @Test
    public void forLoopWithCapitalLetterVariableName() {

        FunctionDefinition fd = new FunctionDefinition("x.y.T", new FunctionBody(new ForLoop("K", new Int(0), new Int(1), new Int(2),
                new FunctionsWrapper(new Function(0, new IntVar("K"))))));

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("The variable name 'K' is not allowed.");

        fd.accept(underTest);
    }

    @Test
    public void sum() {

        FunctionDefinition fd = new FunctionDefinition("x.y.T", new FunctionBody(new FunctionsWrapper(
                new Function(0, new Sum(new IntVar("k"), "k", new Int(2),
                        new Faculty(new Int(5)))))));

        fd.accept(underTest);

        assertTrue(true);
    }

    @Test
    public void sumWithWrongInnerVariableName() {

        FunctionDefinition fd = new FunctionDefinition("x.y.T", new FunctionBody(new FunctionsWrapper(
                new Function(0, new Sum(new IntVar("l"), "k", new Int(2),
                        new Faculty(new Int(5)))))));

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("The variable 'l' is not defined");

        fd.accept(underTest);
    }

    @Test
    public void prod() {

        FunctionDefinition fd = new FunctionDefinition("x.y.T", new FunctionBody(new FunctionsWrapper(
                new Function(0, new Prod(new IntVar("k"), "k", new Int(2),
                        new Faculty(new Int(5)))))));

        fd.accept(underTest);

        assertTrue(true);
    }

    @Test
    public void prodWithWrongInnerVariableName() {

        FunctionDefinition fd = new FunctionDefinition("x.y.T", new FunctionBody(new FunctionsWrapper(
                new Function(0, new Prod(new IntVar("l"), "k", new Int(2),
                        new Faculty(new Int(5)))))));

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("The variable 'l' is not defined");

        fd.accept(underTest);
    }

    @Test
    public void nestedSumAndProd() {

        FunctionDefinition fd = new FunctionDefinition("x.y.T", new FunctionBody(new FunctionsWrapper(
                new Function(0, new Prod(new Sum(new Multiplication(new IntVar("k"),
                        new IntVar("l")), "l", new IntVar("k"), new Int(1000)),
                        "k", new Int(2), new Faculty(new Int(5)))))));

        fd.accept(underTest);

        assertTrue(true);
    }

    @Test
    public void nestedSumAndProdAndWrongIntVarInStartIndex() {

        FunctionDefinition fd = new FunctionDefinition("x.y.T", new FunctionBody(new FunctionsWrapper(
                new Function(0, new Prod(new Sum(new Multiplication(new IntVar("k"),
                        new IntVar("l")), "l", new IntVar("k"), new Int(1000)),
                        "k", new IntVar("k"), new Faculty(new Int(5)))))));

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("The variable 'k' is not defined");

        fd.accept(underTest);
    }

    @Test
    public void nestedSumAndProdAndWrongIntVarInOuterPart() {

        FunctionDefinition fd = new FunctionDefinition("x.y.T", new FunctionBody(new FunctionsWrapper(
                new Function(0, new Prod(new Sum(new Multiplication(new IntVar("k"),
                        new IntVar("l")), "l", new IntVar("k"), new Int(1000)),
                        "k", new IntVar("l"), new Faculty(new Int(5)))))));

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("The variable 'l' is not defined");

        fd.accept(underTest);
    }

    @Test
    public void auxVariable() {

        FunctionDefinition fd = new FunctionDefinition("x.y.T", new FunctionBody(new FunctionsWrapper(
                new AuxiliaryVariable(new AuxVar("I"), new Int(2)),
                new Function(0, new Prod(new IntVar("k"), "k", new Int(2),
                        new Faculty(new Int(5)))))));

        fd.accept(underTest);

        assertTrue(true);
    }

    @Test
    public void auxVariableReferencesOtherAuxVar() {

        FunctionDefinition fd = new FunctionDefinition("x.y.T", new FunctionBody(new FunctionsWrapper(
                new AuxiliaryVariable(new AuxVar("I"), new AuxVar("K")),
                new Function(0, new Prod(new IntVar("k"), "k", new Int(2),
                        new Faculty(new Int(5)))))));

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("On the right hand side of an auxiliary variable no other auxiliary variables are allowed");

        fd.accept(underTest);
    }

    @Test
    public void auxVariableTooLong() {

        FunctionDefinition fd = new FunctionDefinition("x.y.T", new FunctionBody(new FunctionsWrapper(
                new AuxiliaryVariable(new AuxVar("II"), new Int(1)),
                new Function(0, new Prod(new IntVar("k"), "k", new Int(2),
                        new Faculty(new Int(5)))))));

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("The variable name 'II' is not allowed.");

        fd.accept(underTest);
    }

    @Test
    public void auxVariableNoCapitalLetter() {

        FunctionDefinition fd = new FunctionDefinition("x.y.T", new FunctionBody(new FunctionsWrapper(
                new AuxiliaryVariable(new AuxVar("i"), new Int(1)),
                new Function(0, new Prod(new IntVar("k"), "k", new Int(2),
                        new Faculty(new Int(5)))))));

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("The variable name 'i' is not allowed.");

        fd.accept(underTest);
    }

}
