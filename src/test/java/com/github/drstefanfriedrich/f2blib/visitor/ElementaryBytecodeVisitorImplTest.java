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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static com.github.drstefanfriedrich.f2blib.util.TestUtil.closeTo;

/**
 * Some very elementary tests of the {@link BytecodeVisitorImpl}, mainly to show
 * correctness of the generated Java bytecode.
 */
public class ElementaryBytecodeVisitorImplTest extends AbstractBytecodeVisitorImplTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void test1() {

        FunctionDefinition fd = createFunctionDefinition("Test1", new Function(0, new Int(3)));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[0], y);

        assertThat(y[0], closeTo(3));
    }

    @Test
    public void test2() {

        FunctionDefinition fd = createFunctionDefinition("Test2", new Function(0, Constant.PI));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[0], y);

        assertThat(y[0], closeTo(Math.PI));
    }

    @Test
    public void test3() {

        FunctionDefinition fd = createFunctionDefinition("Test3", new Function(0, new Variable(0)));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[]{3.24}, y);

        assertThat(y[0], closeTo(3.24));
    }

    @Test
    public void test4() {

        FunctionDefinition fd = createFunctionDefinition("Test4", new Function(0, new Sin(new Int(3))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[0], y);

        assertThat(y[0], closeTo(0.1411200080598672));
    }

    @Test
    public void test5() {

        FunctionDefinition fd = createFunctionDefinition("Test5", new Function(0, new Variable(10)));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[11], y);

        assertThat(y[0], closeTo(0));
    }

    @Test
    public void test6() {

        FunctionDefinition fd = createFunctionDefinition("Test6", new Function(0,
                new Addition(new Variable(0), new Variable(1))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[]{3.31, 4.19}, y);

        assertThat(y[0], closeTo(7.5));
    }

    @Test
    public void test7() {

        FunctionDefinition fd = createFunctionDefinition("Test7",
                new Function(0, new Variable(0)),
                new Function(1, new Variable(1)));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[2];

        fe.eval(new double[0], new double[]{3.31, 4.19}, y);

        assertThat(y[0], closeTo(3.31));
        assertThat(y[1], closeTo(4.19));
    }

    @Test
    public void test8() {

        FunctionDefinition fd = createFunctionDefinition("Test8",
                new Function(0, new Addition(new Variable(0), new Variable(1))),
                new Function(1, new Subtraction(new Variable(0), new Variable(1))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[2];

        fe.eval(new double[0], new double[]{3.31, 4.19}, y);

        assertThat(y[0], closeTo(7.5));
        assertThat(y[1], closeTo(-0.88));
    }

    @Test
    public void test9() {

        FunctionDefinition fd = createFunctionDefinition("Test9",
                new Function(0, new Variable(2)),
                new Function(1, new Variable(4)));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[2];

        fe.eval(new double[0], new double[]{1, 2, 3, 4, 5}, y);

        assertThat(y[0], closeTo(3));
        assertThat(y[1], closeTo(5));
    }

    @Test
    public void test10() {

        FunctionDefinition fd = createFunctionDefinition("Test10",
                new Function(0, new Addition(new Variable(4), new Parameter(2))),
                new Function(1, new Addition(new Parameter(2), new Parameter(6))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[2];

        fe.eval(new double[]{1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5}, new double[]{1, 2, 3, 4, 5}, y);

        assertThat(y[0], closeTo(8.5));
        assertThat(y[1], closeTo(11));
    }

    @Test
    public void test11() {

        FunctionDefinition fd = createFunctionDefinition("Test11",
                new Function(0, new Sin(new Cos(new Variable(0)))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[]{3.31, 4.19}, y);

        assertThat(y[0], closeTo(-0.8337433605077962));
    }

    @Test
    public void test12() {

        FunctionDefinition fd = createFunctionDefinition("Test12",
                new Function(0, new Addition(new Sin(new Addition(new Variable(0), new Variable(1))),
                        new Cos(new Multiplication(new Parameter(0), new Parameter(1))))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[]{1.2, 2.3}, new double[]{3.31, 4.19}, y);

        assertThat(y[0], closeTo(0.009927266565406323));
    }

    @Test
    public void test13() {

        FunctionDefinition fd = createFunctionDefinition("Test13", new Function(0, Constant.BOLTZMANN));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[0], y);

        assertThat(y[0], closeTo(1.38064852e-23));
    }

    @Test
    public void test14() {

        FunctionDefinition fd = createFunctionDefinition("Test14", new Function(0, new Sin(new Variable(0))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[]{2.44}, y);

        assertThat(y[0], closeTo(0.6454349983343708));
    }

    @Test
    public void test15() {

        FunctionDefinition fd = createFunctionDefinition("Test15", new Function(0, new Arsinh(new Variable(0))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[]{0}, y);

        assertThat(y[0], closeTo(0));
    }

    @Test
    public void test16() {

        FunctionDefinition fd = createFunctionDefinition("Test16", new Function(0, new Neg(new Variable(0))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[]{6.71}, y);

        assertThat(y[0], closeTo(-6.71));
    }

    @Test
    public void test17() {

        FunctionDefinition fd = createFunctionDefinition("Test17", new Function(0, new Pos(new Variable(0))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[]{6.71}, y);

        assertThat(y[0], closeTo(6.71));
    }

    @Test
    public void test18() {

        FunctionDefinition fd = createFunctionDefinition("Test18",
                new Function(0, new Power(new Int(3), new Int(4))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[0], y);

        assertThat(y[0], closeTo(81));
    }

    @Test
    public void test19() {

        FunctionDefinition fd = createFunctionDefinition("Test19", new Function(0, new Round(Constant.E)));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[0], y);

        assertThat(y[0], closeTo(3));
    }

    @Test
    public void test20() {

        FunctionDefinition fd = createFunctionDefinition("Test20", new Function(0, new Int(1000000000)));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[0], y);

        assertThat(y[0], closeTo(1000000000));
    }

    @Test
    public void test21() {

        FunctionDefinition fd = createFunctionDefinition("Test21", new Function(0, new Doub(1.23456789)));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[0], y);

        assertThat(y[0], closeTo(1.23456789));
    }

    @Test
    public void test22() {

        FunctionDefinition fd = createFunctionDefinition("Test22", new Function(0, new Faculty(new Int(5))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[0], y);

        assertThat(y[0], closeTo(120));
    }

    @Test
    public void test23() {

        FunctionDefinition fd = createFunctionDefinition("Test23", new Function(0, new Binomial(new Int(9), new Int(3))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[0], y);

        assertThat(y[0], closeTo(84));
    }

    @Test
    public void test24() {

        FunctionDefinition fd = createFunctionDefinition("Test24", new Function(0, new Sqrt(new Doub(9d))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[0], y);

        assertThat(y[0], closeTo(3));
    }

    @Test
    public void test25() {

        FunctionDefinition fd = forLoop();
        FunctionEvaluation fe = generateClass(fd);

        double[] p = new double[]{10, 20, 2};
        double[] x = new double[]{1.1};
        double[] y = new double[1];

        fe.eval(p, x, y);

        assertThat(y[0], closeTo(20));
    }

    @Test
    public void test26() {

        FunctionDefinition fd = forLoop();
        FunctionEvaluation fe = generateClass(fd);

        double[] p = new double[]{20, 10, -2};
        double[] x = new double[]{1.1};
        double[] y = new double[1];

        fe.eval(p, x, y);

        assertThat(y[0], closeTo(10));
    }

    @Test
    public void test27() {

        FunctionDefinition fd = forLoop();
        FunctionEvaluation fe = generateClass(fd);

        double[] p = new double[]{15, 15, 0};
        double[] x = new double[]{1.1};
        double[] y = new double[1];

        fe.eval(p, x, y);

        assertThat(y[0], closeTo(15));
    }

    @Test
    public void test28() {

        FunctionDefinition fd = forLoop();
        FunctionEvaluation fe = generateClass(fd);

        double[] p = new double[]{15, 30, 0};
        double[] x = new double[]{1.1};
        double[] y = new double[1];

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("step must not be 0");

        fe.eval(p, x, y);
    }

    private FunctionDefinition forLoop() {
        return new FunctionDefinition("ForLoop", new FunctionBody(new ForLoop("k", 0, 1, 2,
                new FunctionsWrapper(new Function(0, new ForVar("k"))))));
    }

}
