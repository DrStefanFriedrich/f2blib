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

import org.f2blib.impl.FunctionEvaluation;

public class ExampleFunction implements FunctionEvaluation {

    /**
     * Polar to cartesian coordinates
     */
    @Override
    public void eval(double[] p, double[] x, double[] y) {
        // p_0 = alpha; scaling factor
        // x_1 = r
        // x_2 = theta

        double p_1 = p[0];

        double x_1 = x[0];
        double x_2 = x[1];

        double var_1 = Math.cos(x_2);
        double var_2 = x_1 * var_1;
        double var_3 = p_1 * var_2;
        double var_4 = Math.sin(x_2);
        double var_5 = x_1 * var_4;
        double var_6 = p_1 * var_5;

        y[0] = var_3;
        y[1] = var_6;

    }

}
