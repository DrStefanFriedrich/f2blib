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

import org.f2blib.ast.*;
import org.f2blib.exception.BytecodeGenerationException;
import org.f2blib.impl.FunctionEvaluation;
import org.f2blib.visitor.BytecodeVisitor;
import org.f2blib.visitor.ValidationVisitor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.InvocationTargetException;

import static java.lang.String.format;
import static org.f2blib.generator.FunctionEvaluationBytecodeGeneratorImplTest.FunctionEvaluationBytecodeGeneratorImplThrowing.ExceptionType.*;
import static org.f2blib.util.TestUtil.closeTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;

public class FunctionEvaluationBytecodeGeneratorImplTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private FunctionEvaluationBytecodeGeneratorImplThrowing underTest;

    private final FunctionDefinition functionDefinition = new FunctionDefinition("FuncName",
            new FunctionBody(new Functions(new Function(0, new Sin(new Variable(0))))));

    private ValidationVisitor validationVisitorMocked;

    private BytecodeVisitor bytecodeVisitorMocked;


    public static class FunctionEvaluationBytecodeGeneratorImplThrowing extends FunctionEvaluationBytecodeGeneratorImpl {

        private final ExceptionType exceptionType;

        public enum ExceptionType {
            NO_SUCH_METHOD_EXCEPTION,
            ILLEGAL_ACCESS_EXCEPTION,
            INVOCATION_TARGET_EXCEPTION_WITH_WRAPPED_RTE,
            INVOCATION_TARGET_EXCEPTION_WITHOUT_WRAPPED_RTE,
            INSTANTIATION_EXCEPTION;
        }

        public FunctionEvaluationBytecodeGeneratorImplThrowing(ExceptionType exceptionType) {
            this.exceptionType = exceptionType;
        }

        @Override
        public FunctionEvaluation generateAndInstantiate(FunctionDefinition functionDefinition, ValidationVisitor validationVisitor, BytecodeVisitor bytecodeVisitor) {
            return super.generateAndInstantiate(functionDefinition, validationVisitor, bytecodeVisitor);
        }

        @Override
        protected FunctionEvaluation instantiate(Class<? extends FunctionEvaluation> clazz) throws NoSuchMethodException,
                IllegalAccessException, InvocationTargetException, InstantiationException {
            switch (exceptionType) {
                case INSTANTIATION_EXCEPTION:
                    throw new InstantiationException();
                case ILLEGAL_ACCESS_EXCEPTION:
                    throw new IllegalAccessException();
                case INVOCATION_TARGET_EXCEPTION_WITH_WRAPPED_RTE:
                    throw new InvocationTargetException(new ArrayIndexOutOfBoundsException());
                case INVOCATION_TARGET_EXCEPTION_WITHOUT_WRAPPED_RTE:
                    throw new InvocationTargetException(new Exception());
                case NO_SUCH_METHOD_EXCEPTION:
                    throw new NoSuchMethodException();
                default:
                    throw new RuntimeException(format("Unrecognized ExceptionType: %s", exceptionType.name()));
            }
        }

    }

    @Before
    public void setup() {
        validationVisitorMocked = mock(ValidationVisitor.class);
        bytecodeVisitorMocked = mock(BytecodeVisitor.class);
    }

    @Test
    public void integration() {

        FunctionEvaluationBytecodeGenerator integrationTest = new FunctionEvaluationBytecodeGeneratorImpl();

        double[] y = new double[1];

        FunctionEvaluation fe = integrationTest.generateAndInstantiate(functionDefinition);

        fe.eval(new double[0], new double[]{Math.PI / 2}, y);

        assertThat(y[0], closeTo(1));
    }

    @Test
    public void noSuchMethodExceptionThrown() {

        underTest = new FunctionEvaluationBytecodeGeneratorImplThrowing(NO_SUCH_METHOD_EXCEPTION);

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("Cannot instantiate class");
        exception.expectCause(instanceOf(NoSuchMethodException.class));

        underTest.generateAndInstantiate(functionDefinition, validationVisitorMocked, bytecodeVisitorMocked);
    }

    @Test
    public void illegalAccessExceptionThrown() {

        underTest = new FunctionEvaluationBytecodeGeneratorImplThrowing(ILLEGAL_ACCESS_EXCEPTION);

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("Cannot instantiate class");
        exception.expectCause(instanceOf(IllegalAccessException.class));

        underTest.generateAndInstantiate(functionDefinition, validationVisitorMocked, bytecodeVisitorMocked);
    }

    @Test
    public void instantiationExceptionThrown() {

        underTest = new FunctionEvaluationBytecodeGeneratorImplThrowing(INSTANTIATION_EXCEPTION);

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("Cannot instantiate class");
        exception.expectCause(instanceOf(InstantiationException.class));

        underTest.generateAndInstantiate(functionDefinition, validationVisitorMocked, bytecodeVisitorMocked);
    }

    @Test
    public void invocationTargetExceptionWithWrappedRuntimeExceptionThrown() {

        underTest = new FunctionEvaluationBytecodeGeneratorImplThrowing(INVOCATION_TARGET_EXCEPTION_WITH_WRAPPED_RTE);

        exception.expect(ArrayIndexOutOfBoundsException.class);

        underTest.generateAndInstantiate(functionDefinition, validationVisitorMocked, bytecodeVisitorMocked);
    }

    @Test
    public void invocationTargetExceptionWithoutWrappedRuntimeExceptionThrown() {

        underTest = new FunctionEvaluationBytecodeGeneratorImplThrowing(INVOCATION_TARGET_EXCEPTION_WITHOUT_WRAPPED_RTE);

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("Exception thrown from constructor invocation");
        exception.expectCause(instanceOf(Exception.class));

        underTest.generateAndInstantiate(functionDefinition, validationVisitorMocked, bytecodeVisitorMocked);
    }

}
