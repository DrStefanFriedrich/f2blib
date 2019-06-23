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

package com.github.drstefanfriedrich.f2blib.generator;

import com.github.drstefanfriedrich.f2blib.ast.*;
import com.github.drstefanfriedrich.f2blib.exception.BytecodeGenerationException;
import com.github.drstefanfriedrich.f2blib.impl.FunctionEvaluation;
import com.github.drstefanfriedrich.f2blib.util.TestUtil;
import com.github.drstefanfriedrich.f2blib.visitor.BytecodeVisitor;
import com.github.drstefanfriedrich.f2blib.visitor.StackDepthVisitor;
import com.github.drstefanfriedrich.f2blib.visitor.ValidationVisitor;
import com.github.drstefanfriedrich.f2blib.ast.*;
import com.github.drstefanfriedrich.f2blib.exception.BytecodeGenerationException;
import com.github.drstefanfriedrich.f2blib.impl.FunctionEvaluation;
import com.github.drstefanfriedrich.f2blib.visitor.BytecodeVisitor;
import com.github.drstefanfriedrich.f2blib.visitor.StackDepthVisitor;
import com.github.drstefanfriedrich.f2blib.visitor.ValidationVisitor;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.InvocationTargetException;

import static java.lang.String.format;
import static com.github.drstefanfriedrich.f2blib.generator.FunctionEvaluationBytecodeGeneratorImplTest.FunctionEvaluationBytecodeGeneratorImplThrowing.ExceptionType.*;
import static com.github.drstefanfriedrich.f2blib.util.TestUtil.closeTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;

public class FunctionEvaluationBytecodeGeneratorImplTest {

    public static final String CANNOT_INSTANTIATE_CLASS = "Cannot instantiate class";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private FunctionEvaluationBytecodeGeneratorImplThrowing underTest;

    private final FunctionDefinition functionDefinition = new FunctionDefinition("FuncName",
            new FunctionBody(new FunctionsWrapper(new Function(0, new Sin(new Variable(0))))));

    private ValidationVisitor validationVisitorMocked;

    private BytecodeVisitor bytecodeVisitorMocked;

    private StackDepthVisitor stackDepthVisitorMocked;

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
                    throw new IllegalArgumentException(format("Unrecognized ExceptionType: %s", exceptionType.name()));
            }
        }

    }

    @Before
    public void setup() {
        validationVisitorMocked = mock(ValidationVisitor.class);
        bytecodeVisitorMocked = mock(BytecodeVisitor.class);
        stackDepthVisitorMocked = mock(StackDepthVisitor.class);
    }

    @Test
    public void integration() {

        FunctionEvaluationBytecodeGenerator integrationTest = new FunctionEvaluationBytecodeGeneratorImpl();

        double[] y = new double[1];

        FunctionEvaluation fe = integrationTest.generateAndInstantiate(functionDefinition);

        fe.eval(new double[0], new double[]{Math.PI / 2}, y);

        MatcherAssert.assertThat(y[0], TestUtil.closeTo(1));
    }

    @Test
    public void noSuchMethodExceptionThrown() {

        underTest = new FunctionEvaluationBytecodeGeneratorImplThrowing(NO_SUCH_METHOD_EXCEPTION);

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage(CANNOT_INSTANTIATE_CLASS);
        exception.expectCause(instanceOf(NoSuchMethodException.class));

        underTest.generateAndInstantiate(functionDefinition, validationVisitorMocked, bytecodeVisitorMocked,
                stackDepthVisitorMocked);
    }

    @Test
    public void illegalAccessExceptionThrown() {

        underTest = new FunctionEvaluationBytecodeGeneratorImplThrowing(ILLEGAL_ACCESS_EXCEPTION);

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage(CANNOT_INSTANTIATE_CLASS);
        exception.expectCause(instanceOf(IllegalAccessException.class));

        underTest.generateAndInstantiate(functionDefinition, validationVisitorMocked, bytecodeVisitorMocked,
                stackDepthVisitorMocked);
    }

    @Test
    public void instantiationExceptionThrown() {

        underTest = new FunctionEvaluationBytecodeGeneratorImplThrowing(INSTANTIATION_EXCEPTION);

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage(CANNOT_INSTANTIATE_CLASS);
        exception.expectCause(instanceOf(InstantiationException.class));

        underTest.generateAndInstantiate(functionDefinition, validationVisitorMocked, bytecodeVisitorMocked,
                stackDepthVisitorMocked);
    }

    @Test
    public void invocationTargetExceptionWithWrappedRuntimeExceptionThrown() {

        underTest = new FunctionEvaluationBytecodeGeneratorImplThrowing(INVOCATION_TARGET_EXCEPTION_WITH_WRAPPED_RTE);

        exception.expect(ArrayIndexOutOfBoundsException.class);

        underTest.generateAndInstantiate(functionDefinition, validationVisitorMocked, bytecodeVisitorMocked,
                stackDepthVisitorMocked);
    }

    @Test
    public void invocationTargetExceptionWithoutWrappedRuntimeExceptionThrown() {

        underTest = new FunctionEvaluationBytecodeGeneratorImplThrowing(INVOCATION_TARGET_EXCEPTION_WITHOUT_WRAPPED_RTE);

        exception.expect(BytecodeGenerationException.class);
        exception.expectMessage("Exception thrown from constructor invocation");
        exception.expectCause(instanceOf(Exception.class));

        underTest.generateAndInstantiate(functionDefinition, validationVisitorMocked, bytecodeVisitorMocked,
                stackDepthVisitorMocked);
    }

}
