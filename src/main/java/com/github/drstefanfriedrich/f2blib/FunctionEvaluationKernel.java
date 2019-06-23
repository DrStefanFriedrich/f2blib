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

package com.github.drstefanfriedrich.f2blib;

/**
 * Handles mathematical functions. This interface defines a kernel (i.e. a
 * library interface) for the evaluation of mathematical functions. The functions
 * can be specified according to a grammar and be loaded into the kernel. Then a
 * simple call of the eval method starts the calculation. By functions we mean
 * real-valued functions <code>f_p: IR^n -&gt; IR^m, p in IR^k</code>.
 * This is just syntactic sugar for <code>f: IR^k x IR^n -&gt; IR^m</code>.
 */
public interface FunctionEvaluationKernel {

    /**
     * Load a new function definition into the kernel.
     *
     * @param functionDefinition The function definition as defined by the grammar.
     */
    void load(String functionDefinition);

    /**
     * Evaluates the given real-valued function. The contract for the function
     * evaluation is as follows: the function depends on <code>p</code> and
     * <code>x</code> only. During function evaluation <code>y</code> must not be
     * considered. The only purpose of <code>y</code> is to store the result.
     *
     * @param functionName The name of the function to evaluate.
     * @param p            The parameters of the function. p in IR^k
     * @param x            The variable of the function. x in IR^n
     * @param y            The result of the function evaluation. y in IR^m
     */
    void eval(String functionName, double[] p, double[] x, double[] y);

}
