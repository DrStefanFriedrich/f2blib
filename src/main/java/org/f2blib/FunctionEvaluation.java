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

package org.f2blib;

/**
 * Models real-valued functions f_p: IR^n -> IR^m, p in IR^k. This is just
 * syntactic sugar for f: IR^k x IR^n -> IR^m.
 */
@FunctionalInterface
public interface FunctionEvaluation {

    /**
     * Evaluates the given real-valued function. The contract for the function
     * evaluation is as follows: the function depends on p and x only. During
     * function evaluation y must not be considered. The only purpose of y is
     * to store the result.
     *
     * @param p The parameters of the function. p in IR^k
     * @param x The variable of the function. x in IR^n
     * @param y The result of the function evaluation. y in IR^m
     */
    void eval(double[] p, double[] x, double[] y);

}
