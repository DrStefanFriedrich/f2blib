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

package org.f2blib.impl;

/**
 * Models real-valued functions <code>f_p: IR^n -&gt; IR^m, p in IR^k</code>.
 * This is just syntactic sugar for <code>f: IR^k x IR^n -&gt; IR^m</code>.
 */
@FunctionalInterface
public interface FunctionEvaluation {

    /**
     * Evaluates the given real-valued function. The contract for the function
     * evaluation is as follows: the function depends on <code>p</code> and
     * <code>x</code> only. During function evaluation <code>y</code> must not be
     * considered. The only purpose of <code>y</code> is to store the result.
     *
     * @param p The parameters of the function. <code>p in IR^k</code>
     * @param x The variable of the function. <code>x in IR^n</code>
     * @param y The result of the function evaluation. <code>y in IR^m</code>
     */
    void eval(double[] p, double[] x, double[] y);

}
