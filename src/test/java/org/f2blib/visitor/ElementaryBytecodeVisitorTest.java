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
import org.f2blib.impl.FunctionEvaluation;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Some very elementary tests of the {@link BytecodeVisitor}, mainly to show
 * correctness of the generated Java Bytecode.
 */
public class ElementaryBytecodeVisitorTest extends AbstractBytecodeVisitorTest {

    @Test
    public void test1() {

        FunctionDefinition fd = createFunctionDefinition("Test1", new Function(0, new Int(3)));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[0], y);

        assertThat((double) 3, is(y[0]));
    }

    @Test
    public void test2() {

        FunctionDefinition fd = createFunctionDefinition("Test2", new Function(0, Constant.PI));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[0], y);

        assertThat(Math.PI, is(y[0]));
    }

    @Test
    public void test3() {

        FunctionDefinition fd = createFunctionDefinition("Test3", new Function(0, new Variable(0)));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[]{3.24}, y);

        assertThat(3.24, is(y[0]));
    }

    @Test
    public void test4() {

        FunctionDefinition fd = createFunctionDefinition("Test4", new Function(0, new Sin(new Int(3))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[0], y);

        assertThat(0.1411200080598672, is(y[0]));
    }

    @Test
    public void test5() {

        FunctionDefinition fd = createFunctionDefinition("Test5", new Function(0, new Variable(10)));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[11], y);

        assertThat(0d, is(y[0]));
    }

    @Test
    public void test6() {

        FunctionDefinition fd = createFunctionDefinition("Test6", new Function(0,
                new Addition(new Variable(0), new Variable(1))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[]{3.31, 4.19}, y);

        assertThat(7.5d, is(y[0]));
    }

    @Test
    public void test7() {

        FunctionDefinition fd = createFunctionDefinition("Test7",
                new Function(0, new Variable(0)),
                new Function(1, new Variable(1)));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[2];

        fe.eval(new double[0], new double[]{3.31, 4.19}, y);

        assertThat(3.31d, is(y[0]));
        assertThat(4.19d, is(y[1]));
    }

    @Test
    public void test8() {

        FunctionDefinition fd = createFunctionDefinition("Test8",
                new Function(0, new Addition(new Variable(0), new Variable(1))),
                new Function(1, new Subtraction(new Variable(0), new Variable(1))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[2];

        fe.eval(new double[0], new double[]{3.31, 4.19}, y);

        assertThat(7.5d, is(y[0]));
        assertThat(-0.8800000000000003, is(y[1]));
    }

    @Test
    public void test9() {

        FunctionDefinition fd = createFunctionDefinition("Test9",
                new Function(0, new Variable(2)),
                new Function(1, new Variable(4)));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[2];

        fe.eval(new double[0], new double[]{1, 2, 3, 4, 5}, y);

        assertThat(3d, is(y[0]));
        assertThat(5d, is(y[1]));
    }

    @Test
    public void test10() {

        FunctionDefinition fd = createFunctionDefinition("Test10",
                new Function(0, new Addition(new Variable(4), new Parameter(2))),
                new Function(1, new Addition(new Parameter(2), new Parameter(6))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[2];

        fe.eval(new double[]{1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5}, new double[]{1, 2, 3, 4, 5}, y);

        assertThat(8.5d, is(y[0]));
        assertThat(11d, is(y[1]));
    }

    @Test
    public void test11() {

        FunctionDefinition fd = createFunctionDefinition("Test11",
                new Function(0, new Sin(new Cos(new Variable(0)))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[]{3.31, 4.19}, y);

        assertThat(-0.8337433605077962, is(y[0]));
    }

    @Test
    public void test12() {

        FunctionDefinition fd = createFunctionDefinition("Test12",
                new Function(0, new Addition(new Sin(new Addition(new Variable(0), new Variable(1))),
                        new Cos(new Multiplication(new Parameter(0), new Parameter(1))))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[]{1.2, 2.3}, new double[]{3.31, 4.19}, y);

        assertThat(0.009927266565406323, is(y[0]));
    }

    @Test
    public void test13() {

        FunctionDefinition fd = createFunctionDefinition("Test13", new Function(0, Constant.BOLTZMANN));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[0], y);

        assertThat(1.38064852e-23, is(y[0]));
    }

    @Test
    public void test14() {

        FunctionDefinition fd = createFunctionDefinition("Test14", new Function(0, new Sin(new Variable(0))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[]{2.44}, y);

        assertThat(0.6454349983343708, is(y[0]));
    }

    @Test
    public void test15() {

        FunctionDefinition fd = createFunctionDefinition("Test15", new Function(0, new Arsinh(new Variable(0))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[]{0}, y);

        assertThat(0d, is(y[0]));
    }

    @Test
    public void test16() {

        FunctionDefinition fd = createFunctionDefinition("Test16", new Function(0, new Neg(new Variable(0))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[]{6.71}, y);

        assertThat(-6.71d, is(y[0]));
    }

    @Test
    public void test17() {

        FunctionDefinition fd = createFunctionDefinition("Test17", new Function(0, new Pos(new Variable(0))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[]{6.71}, y);

        assertThat(6.71d, is(y[0]));
    }

    @Test
    public void test18() {

        FunctionDefinition fd = createFunctionDefinition("Test18",
                new Function(0, new Power(new Int(3), new Int(4))));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[0], y);

        assertThat(81d, is(y[0]));
    }

    @Test
    public void test19() {

        FunctionDefinition fd = createFunctionDefinition("Test19", new Function(0, new Round(Constant.E)));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[0], y);

        assertThat(3d, is(y[0]));
    }

    @Test
    @Ignore("TODO SF")
    public void test20() {

        FunctionDefinition fd = createFunctionDefinition("Test20", new Function(0, new Int(1000000000)));
        FunctionEvaluation fe = generateClass(fd);

        double[] y = new double[1];

        fe.eval(new double[0], new double[0], y);

        assertThat(y[0], is(1000000000));
    }

}
