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

package org.f2blib.ast;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for abstract syntax tree classes.
 */
public class ASTTest {

    @Test
    public void toStringTest() {

        FunctionDefinition fd = createFunctionDefinition("MyFunc",
                new Sin(new Multiplication(Constant.PI, new Variable(0))));

        assertThat(fd.toString(), is("FunctionDefinition{name=MyFunc, functionBody=FunctionBody{" +
                "functions=Functions{functions=[Function{index=0, expression=Sin{expression=" +
                "Multiplication{left=PI, right=Variable{index=0}}}}]}}}"));
    }

    @Test
    public void equalityByValue() {

        FunctionDefinition fd1 = createFunctionDefinition("MyFunc",
                new Sin(new Multiplication(Constant.PI, new Variable(0))));

        FunctionDefinition fd2 = createFunctionDefinition("MyFunc",
                new Sin(new Multiplication(Constant.PI, new Variable(0))));

        assertThat(fd1.equals(fd2), is(true));

        assertThat(fd1.hashCode(), is(fd2.hashCode()));
    }

    @Test
    public void unequal() {

        FunctionDefinition fd1 = createFunctionDefinition("MyFunc",
                new Sin(new Multiplication(new Variable(0), Constant.PI)));

        FunctionDefinition fd2 = createFunctionDefinition("MyFunc",
                new Sin(new Multiplication(Constant.PI, new Variable(0))));

        assertThat(fd1.equals(fd2), is(false));

        assertThat(fd1.hashCode(), is(not(fd2.hashCode())));
    }

    @Test
    public void abs() {

        FunctionDefinition fd1 = createFunctionDefinition("MyFunc", new Abs(new Variable(0)));
        FunctionDefinition fd2 = createFunctionDefinition("MyFunc", new Abs(new Variable(0)));

        assertThat(fd1.equals(fd2), is(true));
        assertThat(fd1.toString(), is("FunctionDefinition{name=MyFunc, functionBody=FunctionBody{functions=" +
                "Functions{functions=[Function{index=0, expression=Abs{expression=Variable{index=0}}}]}}}"));
    }

    private FunctionDefinition createFunctionDefinition(String name, Expression expression) {

        Set<Function> set = new HashSet<>();
        Function f = new Function(0, expression);
        set.add(f);

        FunctionDefinition fd = new FunctionDefinition("MyFunc", new FunctionBody(new Functions(set)));

        return fd;
    }

}
