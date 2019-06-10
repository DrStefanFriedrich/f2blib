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

import org.f2blib.ast.Expression;
import org.f2blib.ast.FunctionDefinition;
import org.f2blib.impl.FunctionEvaluation;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.f2blib.ast.ASTTest.createFunctionDefinition;
import static org.f2blib.util.TestUtil.assumePerformanceTest;
import static org.f2blib.util.TestUtil.closeTo;
import static org.f2blib.visitor.EvalVisitorTest.createSampleFunction;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class BytecodeVisitorImplTest extends AbstractCalculatingVisitorTest {

    private BytecodeVisitorImpl bytecodeVisitor;

    private ValidationVisitorImpl validationVisitor;

    @Before
    public void setup() {
        validationVisitor = new ValidationVisitorImpl();
    }

    @Test
    public void simpleEvaluation() throws IllegalAccessException, InstantiationException, IOException {

        double[] x = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        double[] p = new double[]{0, 2, 4, 6, 8, 10, 12, 14, 16, 18};
        double[] y = new double[5];

        FunctionDefinition fd = createSampleFunction();

        fd.accept(validationVisitor);

        bytecodeVisitor = new BytecodeVisitorImpl(validationVisitor.getLocalVariables());
        fd.accept(bytecodeVisitor);

        FunctionEvaluation functionEvaluation = bytecodeVisitor.generate().newInstance();

        functionEvaluation.eval(p, x, y);

        assertThat(y[0], closeTo(660));
        assertThat(y[1], closeTo(1.385614343926388));
        assertThat(y[2], closeTo(50.58293575689467));
        assertThat(y[3], closeTo(-3.7776177920749547));
        assertThat(y[4], closeTo(0));
    }

    @Test
    public void performance() throws IllegalAccessException, InstantiationException, IOException {
        assumePerformanceTest();

        FunctionDefinition fd = createSampleFunction();

        fd.accept(validationVisitor);

        bytecodeVisitor = new BytecodeVisitorImpl(validationVisitor.getLocalVariables());
        fd.accept(bytecodeVisitor);

        FunctionEvaluation functionEvaluation = bytecodeVisitor.generate().newInstance();

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

        fail("Performance should always be better. That's why we fail the unit test\n\n" +
                "The execution took (ms): " + (end - start));
    }

    @Override
    protected void assertExpressionMatches(Expression expression, double xValue, double yValue) {
        try {

            FunctionDefinition fd = createFunctionDefinition("BytecodeTestFunc", expression);

            fd.accept(validationVisitor);

            bytecodeVisitor = new BytecodeVisitorImpl(validationVisitor.getLocalVariables());
            fd.accept(bytecodeVisitor);

            FunctionEvaluation functionEvaluation = bytecodeVisitor.generate().newInstance();

            double[] x = new double[]{xValue};
            double[] p = new double[]{};
            double[] y = new double[1];

            functionEvaluation.eval(p, x, y);

            assertThat(y[0], closeTo(yValue));

        } catch (IllegalAccessException | InstantiationException e) {
            throw new InternalError(e);
        }
    }

}
