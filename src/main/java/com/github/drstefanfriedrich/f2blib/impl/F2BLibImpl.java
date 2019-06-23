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

import com.github.drstefanfriedrich.f2blib.FunctionEvaluationKernel;
import com.github.drstefanfriedrich.f2blib.ast.FunctionDefinition;
import com.github.drstefanfriedrich.f2blib.generator.FunctionEvaluationBytecodeGenerator;
import com.github.drstefanfriedrich.f2blib.parser.FunctionParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

/**
 * F2BLib implementation of a {@link FunctionEvaluationKernel}.
 */
public class F2BLibImpl implements FunctionEvaluationKernel {

    private final Map<String, FunctionEvaluation> cache = new ConcurrentHashMap<>();

    private final FunctionParser parser;

    private final FunctionEvaluationBytecodeGenerator generator;

    F2BLibImpl(FunctionParser parser, FunctionEvaluationBytecodeGenerator generator) {
        this.parser = parser;
        this.generator = generator;
    }

    /**
     * Load a new function definition into the kernel.
     *
     * @param functionDefinition The function definition as defined by the grammar.
     */
    @Override
    public void load(String functionDefinition) {

        FunctionDefinition fd = parser.parse(functionDefinition);

        FunctionEvaluation instance = generator.generateAndInstantiate(fd);

        cache.put(instance.getClass().getName(), instance);

    }

    /**
     * Evaluate a function.
     *
     * @param functionName The name of the function.
     * @param p            The parameters of the function.
     * @param x            The variables of the function.
     * @param y            The vector which stores the result.
     */
    @Override
    public void eval(String functionName, double[] p, double[] x, double[] y) {

        FunctionEvaluation functionEvaluation = cache.get(functionName);

        if (functionEvaluation == null) {
            throw new IllegalArgumentException(format("Unknown function name: %s", functionName));
        }

        functionEvaluation.eval(p, x, y);
    }

}
