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

package com.github.drstefanfriedrich.f2blib.impl;

import com.github.drstefanfriedrich.f2blib.visitor.FunctionEvaluationValidator;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertThat;

public class FunctionInfoTest {

    private static final String PRETTY_PRINTED_1 = "pp1";

    private static final String PRETTY_PRINTED_2 = "pp2";

    private static final FunctionEvaluation fe1 = new FunctionEvaluation() {
        @Override
        public void eval(double[] p, double[] x, double[] y) {
        }
    };

    private static final FunctionEvaluation fe2 = new FunctionEvaluation() {
        @Override
        public void eval(double[] p, double[] x, double[] y) {
        }
    };

    private static final FunctionEvaluationValidator fev = new FunctionEvaluationValidator() {
        @Override
        public void validate(double[] p, double[] x, double[] y) throws IllegalArgumentException {
        }
    };

    @Test
    public void equalsAndHashCodeTest() {

        FunctionInfo functionInfo11 = new FunctionInfo(fe1, PRETTY_PRINTED_1, fev);
        FunctionInfo functionInfo12 = new FunctionInfo(fe1, PRETTY_PRINTED_2, fev);
        FunctionInfo functionInfo21 = new FunctionInfo(fe2, PRETTY_PRINTED_1, fev);
        FunctionInfo functionInfo22 = new FunctionInfo(fe2, PRETTY_PRINTED_2, fev);

        assertThat(functionInfo11.equals(functionInfo11), is(true));
        assertThat(functionInfo11.equals(functionInfo12), is(false));
        assertThat(functionInfo21.equals(functionInfo11), is(false));
        assertThat(functionInfo12.equals(functionInfo11), is(false));
        assertThat(functionInfo22.equals(functionInfo22), is(true));

        assertThat(functionInfo11.hashCode(), is(functionInfo11.hashCode()));
    }

    @Test
    public void toStringTest() {

        FunctionInfo functionInfo = new FunctionInfo(fe1, PRETTY_PRINTED_1, fev);

        assertThat(functionInfo.toString(), startsWith("FunctionInfo{functionEvaluation=com.github.drstefanfriedrich.f2blib.impl.FunctionInfoTest$"));
    }

}
