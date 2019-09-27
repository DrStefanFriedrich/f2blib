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

import static java.lang.String.format;

class FunctionEvaluationValidatorImpl implements FunctionEvaluationValidator {

    private final int sizeP;
    private final int sizeX;
    private final int sizeY;
    private final boolean atLeastOneVariableIsIntExpression;
    private final boolean atLeastOneParameterIsIntExpression;

    public FunctionEvaluationValidatorImpl(int sizeP, int sizeX, int sizeY, boolean atLeastOneParameterIsIntExpression,
                                           boolean atLeastOneVariableIsIntExpression) {
        this.sizeP = sizeP;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.atLeastOneParameterIsIntExpression = atLeastOneParameterIsIntExpression;
        this.atLeastOneVariableIsIntExpression = atLeastOneVariableIsIntExpression;
    }

    @Override
    public void validate(double[] p, double[] x, double[] y) {

        int actualP = p.length;
        int actualX = x.length;
        int actualY = y.length;

        if (sizeP != -1 && sizeP > actualP && !atLeastOneParameterIsIntExpression) {
            throw new IllegalArgumentException(format("p must have at least size %d, but was %d", sizeP, actualP));
        }
        if (sizeX != -1 && sizeX > actualX && !atLeastOneVariableIsIntExpression) {
            throw new IllegalArgumentException(format("x must have at least size %d, but was %d", sizeX, actualX));
        }
        if (sizeY != actualY) {
            throw new IllegalArgumentException(format("y must have size %d, but was %d", sizeY, actualY));
        }
    }

}
