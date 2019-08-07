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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static com.github.drstefanfriedrich.f2blib.util.TestUtil.assumePerformanceTest;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static com.github.drstefanfriedrich.f2blib.util.TestUtil.closeTo;

public class EvalVisitorTest extends AbstractCalculatingVisitorTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private EvalVisitor evalVisitor;

    private double[] x;

    @Before
    public void setup() {
        x = new double[1];
        double[] p = new double[1];
        evalVisitor = new EvalVisitor(x, p, 1);
    }

    public static FunctionDefinition createSampleFunction() {

        List<Function> functions = new ArrayList<>();

        // Scalar product
        Function f1 = new Function(0, new Addition(new Multiplication(new Variable(0), new Parameter(0)),
                new Addition(new Multiplication(new Variable(1), new Parameter(1)),
                        new Addition(new Multiplication(new Variable(2), new Parameter(2)),
                                new Addition(new Multiplication(new Variable(3), new Parameter(3)),
                                        new Addition(new Multiplication(new Variable(4), new Parameter(4)),
                                                new Addition(new Multiplication(new Variable(5), new Parameter(5)),
                                                        new Addition(new Multiplication(new Variable(6), new Parameter(6)),
                                                                new Addition(new Multiplication(new Variable(7), new Parameter(7)),
                                                                        new Addition(new Multiplication(new Variable(8), new Parameter(8)),
                                                                                new Addition(new Multiplication(new Variable(9), new Parameter(9)), new Int(0))))))))))));

        Function f2 = new Function(1, new Division(new Multiplication(new Multiplication(new Parameter(1),
                new Parameter(2)), new Exp(new Division(new Int(1), new Addition(new Addition(new Int(1),
                new Multiplication(new Variable(2), new Variable(2))),
                new Multiplication(new Variable(3), new Variable(3)))))),
                new Addition(new Addition(new Int(1), new Multiplication(new Variable(0),
                        new Variable(0))), new Multiplication(new Variable(1), new Variable(1)))));

        Function f3 = new Function(2, new Neg(new Addition(Constant.PI, new Multiplication(
                new Cos(new Ln(new Addition(new Int(1), new Abs(new Variable(7))))),
                new Multiplication(new Addition(new Sinh(Constant.E), new Tan(new Multiplication(
                        new Addition(new Parameter(5), new Parameter(5)),
                        new Addition(new Parameter(5), new Parameter(5))))),
                        new Parenthesis(new Variable(9)))))));

        Function f4 = new Function(3, new Division(new Multiplication(new Multiplication(new Parameter(1),
                new Parameter(2)), new Ln(new Division(new Int(1), new Addition(new Addition(new Int(1),
                new Subtraction(new Variable(2), new Variable(2))),
                new Multiplication(new Variable(3), new Variable(3)))))),
                new Addition(new Addition(new Int(1), new Division(new Variable(0),
                        new Variable(0))), new Multiplication(new Variable(1), new Variable(1)))));

        Function f5 = new Function(4, new Division(new Multiplication(new Multiplication(new Parameter(1),
                new Parameter(2)), new Round(new Division(new Int(1), new Addition(new Addition(new Int(1),
                new Subtraction(new Variable(2), new Variable(2))),
                new Multiplication(new Variable(3), new Variable(3)))))),
                new Subtraction(new Addition(new Int(1), new Division(new Variable(0),
                        new Variable(0))), new Multiplication(new Variable(1), new Variable(1)))));

        functions.add(f1);
        functions.add(f2);
        functions.add(f3);
        functions.add(f4);
        functions.add(f5);

        return new FunctionDefinition("MyFunc", new FunctionBody(new FunctionsWrapper(functions)));
    }

    @Test
    public void simpleEvaluation() {

        double[] xArray = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        double[] pArray = new double[]{0, 2, 4, 6, 8, 10, 12, 14, 16, 18};
        evalVisitor = new EvalVisitor(xArray, pArray, 5);

        FunctionDefinition fd = createSampleFunction();

        fd.accept(evalVisitor);

        assertThat(evalVisitor.getResult()[0], closeTo(660));
        assertThat(evalVisitor.getResult()[1], closeTo(1.385614343926388));
        assertThat(evalVisitor.getResult()[2], closeTo(50.58293575689467));
        assertThat(evalVisitor.getResult()[3], closeTo(-3.7776177920749547));
        assertThat(evalVisitor.getResult()[4], closeTo(0));
    }

    @Test
    public void evalNoOp() {

        evalVisitor = new EvalVisitor(null, null, 0);

        FunctionDefinition fd = ASTTest.createFunctionDefinition("NoOp", NoOp.get());

        exception.expect(IllegalStateException.class);
        exception.expectMessage("visit must not be called on the EvalVisitor");

        fd.accept(evalVisitor);
    }

    @Test
    public void performance() {
        assumePerformanceTest();

        FunctionDefinition fd = createSampleFunction();

        long start = System.currentTimeMillis();

        for (int i1 = 0; i1 < 10; i1++) {
            for (int i2 = 0; i2 < 10; i2++) {
                for (int i3 = 0; i3 < 10; i3++) {
                    for (int i4 = 0; i4 < 10; i4++) {
                        for (int i5 = 0; i5 < 10; i5++) {
                            for (int i6 = 0; i6 < 10; i6++) {
                                for (int i7 = 0; i7 < 10; i7++) {

                                    double[] xArray = new double[]{(double) i1 / 10, 1 + (double) i2 / 10, 2 + (double) i3 / 10,
                                            3 + (double) i4 / 10, 5, 6, 7, 8, 9, 10};
                                    double[] pArray = new double[]{(double) i5 / 10, 1 + (double) i6 / 10,
                                            3 + (double) i7 / 10, 6, 8, 10, 12, 14, 16, 18};
                                    evalVisitor = new EvalVisitor(xArray, pArray, 5);

                                    fd.accept(evalVisitor);
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

    protected void assertExpressionMatches(Expression expression, double xValue, double yValue) {

        FunctionDefinition fd = ASTTest.createFunctionDefinition("EvalTestFunc", expression);

        x[0] = xValue;
        fd.accept(evalVisitor);
        double y = evalVisitor.getResult()[0];

        assertThat(y, closeTo(yValue));
    }

    @Test
    public void tooLessVariables() {

        double[] xArray = new double[]{1};
        double[] pArray = new double[]{1};
        evalVisitor = new EvalVisitor(xArray, pArray, 1);

        FunctionDefinition fd = new FunctionDefinition("TooLessVariables", new FunctionBody(new FunctionsWrapper(new Function(0, new Variable(10)))));

        exception.expect(ArrayIndexOutOfBoundsException.class);
        exception.expectMessage("");

        fd.accept(evalVisitor);
    }

}
