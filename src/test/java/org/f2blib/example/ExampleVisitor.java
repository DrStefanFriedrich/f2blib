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

package org.f2blib.example;

import org.apache.commons.math3.analysis.function.Acosh;
import org.apache.commons.math3.analysis.function.Asinh;
import org.apache.commons.math3.analysis.function.Atanh;
import org.f2blib.ast.*;
import org.f2blib.exception.BytecodeGenerationException;
import org.f2blib.visitor.AbstractVisitor;

public class ExampleVisitor extends AbstractVisitor {

    private static final Asinh ARSINH = new Asinh();

    private static final Acosh ARCOSH = new Acosh();

    private static final Atanh ARTANH = new Atanh();

    private static final double BOLTZMANN = 1.38064852e-23;

    private final double[] x = new double[10];

    private final double[] y = new double[10];

    public void foobar() {
        int i = 12;
        double d = i;
        Integer i2 = 333;
        Double d2 = Double.valueOf(i2);
    }

    public Double visitSin(Sin sin) {
        return Math.sin(toDouble(sin.acceptExpression(this)));
    }

    public Double visitAddition(Addition addition) {
        return (toDouble(addition.acceptLeft(this))) + (toDouble(addition.acceptRight(this)));
    }

    public Double visitVariable(Variable variable) {
        return x[variable.getIndex()];
    }

    public Double visitConstant(Constant constant) {
        switch (constant) {
            case PI:
                return Math.PI;
            case E:
                return Math.E;
            case BOLTZMANN:
                return 1.38064852e10 - 23;
            default:
                throw new BytecodeGenerationException("Unrecognized constant " + constant.name());
        }
    }

    private double toDouble(Number number) {
        return number.doubleValue();
    }

    public double visitArsinh() {
        return ARSINH.value(3.0d);
    }

}
