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
import com.github.drstefanfriedrich.f2blib.impl.FunctionEvaluation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.InvocationTargetException;

import static com.github.drstefanfriedrich.f2blib.util.TestUtil.assumePerformanceTest;
import static com.github.drstefanfriedrich.f2blib.util.TestUtil.closeTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class BytecodeVisitorImplTest extends AbstractCalculatingVisitorTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private BytecodeVisitorImpl bytecodeVisitor;

    private ValidationVisitorImpl validationVisitor;

    private StackDepthVisitorImpl stackDepthVisitor;

    @Before
    public void setup() {
        validationVisitor = new ValidationVisitorImpl();
        stackDepthVisitor = new StackDepthVisitorImpl();
    }

    @Test
    public void simpleEvaluation() {

        double[] x = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        double[] p = new double[]{0, 2, 4, 6, 8, 10, 12, 14, 16, 18};
        double[] y = new double[5];

        FunctionDefinition fd = EvalVisitorImplTest.createSampleFunction();

        fd.accept(validationVisitor);
        fd.accept(stackDepthVisitor);

        bytecodeVisitor = new BytecodeVisitorImpl(validationVisitor.getLocalVariables(),
                validationVisitor.getSpecialFunctionsUsage(), stackDepthVisitor);
        fd.accept(bytecodeVisitor);

        FunctionEvaluation functionEvaluation = instantiate(bytecodeVisitor.generate());

        functionEvaluation.eval(p, x, y);

        assertThat(y[0], closeTo(660));
        assertThat(y[1], closeTo(1.385614343926388));
        assertThat(y[2], closeTo(50.58293575689467));
        assertThat(y[3], closeTo(-3.7776177920749547));
        assertThat(y[4], closeTo(0));
    }

    @Test
    public void performance() {
        assumePerformanceTest();

        FunctionDefinition fd = EvalVisitorImplTest.createSampleFunction();

        fd.accept(validationVisitor);
        fd.accept(stackDepthVisitor);

        bytecodeVisitor = new BytecodeVisitorImpl(validationVisitor.getLocalVariables(),
                validationVisitor.getSpecialFunctionsUsage(), stackDepthVisitor);
        fd.accept(bytecodeVisitor);

        FunctionEvaluation functionEvaluation = instantiate(bytecodeVisitor.generate());

        long start = System.currentTimeMillis();

        for (int i1 = 0; i1 < 10; i1++) {
            for (int i2 = 0; i2 < 10; i2++) {
                for (int i3 = 0; i3 < 10; i3++) {
                    for (int i4 = 0; i4 < 10; i4++) {
                        for (int i5 = 0; i5 < 10; i5++) {
                            for (int i6 = 0; i6 < 10; i6++) {
                                for (int i7 = 0; i7 < 10; i7++) {

                                    double[] x = new double[]{(double) i1 / 10, 1 + (double) i2 / 10, 2 + (double) i3 / 10,
                                            3 + (double) i4 / 10, 5, 6, 7, 8, 9, 10};
                                    double[] p = new double[]{(double) i5 / 10, 1 + (double) i6 / 10,
                                            3 + (double) i7 / 10, 6, 8, 10, 12, 14, 16, 18};
                                    double[] y = new double[5];

                                    functionEvaluation.eval(p, x, y);
                                }
                            }
                        }
                    }
                }
            }
        }

        long end = System.currentTimeMillis();

        fail("Performance should always be better. That's why we fail the unit test. " +
                "Total duration (ms): " + (end - start));
    }

    @Override
    protected void assertExpressionMatches(Expression expression, double xValue, double yValue) {

        FunctionDefinition fd = ASTTest.createFunctionDefinition("BytecodeTestFunc", expression);

        fd.accept(validationVisitor);
        fd.accept(stackDepthVisitor);

        bytecodeVisitor = new BytecodeVisitorImpl(validationVisitor.getLocalVariables(),
                validationVisitor.getSpecialFunctionsUsage(), stackDepthVisitor);
        fd.accept(bytecodeVisitor);

        FunctionEvaluation functionEvaluation = instantiate(bytecodeVisitor.generate());

        double[] x = new double[]{xValue};
        double[] p = new double[]{0};
        double[] y = new double[1];

        functionEvaluation.eval(p, x, y);

        assertThat(y[0], closeTo(yValue));

    }

    @Override
    protected void assertExpressionMatches(Expression expression, Expression auxiliaryExpression, double xValue,
                                           double yValue) {

        FunctionDefinition fd = ASTTest.createFunctionDefinition("BytecodeTestFunc", expression, auxiliaryExpression);

        fd.accept(validationVisitor);
        fd.accept(stackDepthVisitor);

        bytecodeVisitor = new BytecodeVisitorImpl(validationVisitor.getLocalVariables(),
                validationVisitor.getSpecialFunctionsUsage(), stackDepthVisitor);
        fd.accept(bytecodeVisitor);

        FunctionEvaluation functionEvaluation = instantiate(bytecodeVisitor.generate());

        double[] x = new double[]{xValue};
        double[] p = new double[]{0};
        double[] y = new double[1];

        functionEvaluation.eval(p, x, y);

        assertThat(y[0], closeTo(yValue));

    }

    @Test
    public void parametersAndVariablesWithGaps() {

        testParametersAndVariablesWithGaps(100, 100);
    }

    @Test
    public void maxNumberOfParametersAndVariables() {

        testParametersAndVariablesWithGaps(128, 128);
    }

    @Test
    public void tooManyParameters() {

        exception.expect(ArrayIndexOutOfBoundsException.class);

        testParametersAndVariablesWithGaps(128, 129);
    }

    @Test
    public void tooManyVariables() {

        exception.expect(ArrayIndexOutOfBoundsException.class);

        testParametersAndVariablesWithGaps(129, 128);
    }

    private void testParametersAndVariablesWithGaps(int numberOfVariables, int numberOfParameters) {

        FunctionDefinition fd = ASTTest.createFunctionDefinition("ManyParametersAndVariables",
                new Addition(scalarProductOfParameters(numberOfParameters), scalarProductOfVariables(numberOfVariables)));

        fd.accept(validationVisitor);
        fd.accept(stackDepthVisitor);

        bytecodeVisitor = new BytecodeVisitorImpl(validationVisitor.getLocalVariables(),
                validationVisitor.getSpecialFunctionsUsage(), stackDepthVisitor);
        fd.accept(bytecodeVisitor);

        FunctionEvaluation functionEvaluation = instantiate(bytecodeVisitor.generate());

        double[] x = new double[numberOfVariables];
        double[] p = new double[numberOfParameters];
        double[] y = new double[1];

        for (int i = 0; i < numberOfVariables; i++) {
            x[i] = (double) i + 1;
        }
        for (int i = 0; i < numberOfParameters; i++) {
            p[i] = (double) i + 1;
        }

        functionEvaluation.eval(p, x, y);

        // Verify the sum formula of the young Carl Friedrich Gauß...
        assertThat(y[0], closeTo(numberOfParameters * (numberOfParameters + 1) / 2 + numberOfVariables * (numberOfVariables + 1) / 2));
        // ...he was right

    }

    private Expression scalarProductOfParameters(int numberOfParameters) {

        Expression result = new Multiplication(new Int(1), new Parameter(0));

        for (int i = 1; i < numberOfParameters; i++) {
            result = new Addition(new Multiplication(new Int(1), new Parameter(i)), result);
        }

        return result;
    }

    private Expression scalarProductOfVariables(int numberOfVariables) {

        Expression result = new Multiplication(new Int(1), new Variable(0));

        for (int i = 1; i < numberOfVariables; i++) {
            result = new Addition(new Multiplication(new Int(1), new Variable(i)), result);
        }

        return result;
    }

    private FunctionDefinition createFunctionDefinition(Function... functions) {
        return new FunctionDefinition("ValidationVisitorFunctionTest", new FunctionBody(new FunctionsWrapper(functions)));
    }

    @Test
    public void accessNonExistingParameter() throws IllegalAccessException, InstantiationException {

        double[] x = new double[]{1, 2, 3, 4, 5, 6, 7};
        double[] p = new double[]{0, 2};
        double[] y = new double[5];

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Parameter(4)),
                new Function(1, new Variable(6)));

        fd.accept(validationVisitor);
        fd.accept(stackDepthVisitor);

        bytecodeVisitor = new BytecodeVisitorImpl(validationVisitor.getLocalVariables(),
                validationVisitor.getSpecialFunctionsUsage(), stackDepthVisitor);
        fd.accept(bytecodeVisitor);

        FunctionEvaluation functionEvaluation = instantiate(bytecodeVisitor.generate());

        exception.expect(ArrayIndexOutOfBoundsException.class);
        exception.expectMessage("");

        functionEvaluation.eval(p, x, y);
    }

    @Test
    public void accessNonExistingVariable() throws IllegalAccessException, InstantiationException {

        double[] x = new double[]{1, 2, 3, 4, 5, 6, 7};
        double[] p = new double[]{0, 2};
        double[] y = new double[5];

        FunctionDefinition fd = createFunctionDefinition(new Function(0, new Parameter(0)),
                new Function(1, new Variable(10)));

        fd.accept(validationVisitor);
        fd.accept(stackDepthVisitor);

        bytecodeVisitor = new BytecodeVisitorImpl(validationVisitor.getLocalVariables(),
                validationVisitor.getSpecialFunctionsUsage(), stackDepthVisitor);
        fd.accept(bytecodeVisitor);

        FunctionEvaluation functionEvaluation = instantiate(bytecodeVisitor.generate());

        exception.expect(ArrayIndexOutOfBoundsException.class);
        exception.expectMessage("");

        functionEvaluation.eval(p, x, y);
    }

    private FunctionEvaluation instantiate(Class<? extends FunctionEvaluation> clazz) {
        try {
            return clazz.getConstructor((Class<?>[]) null).newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
