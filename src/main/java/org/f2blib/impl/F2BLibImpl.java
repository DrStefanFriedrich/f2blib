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

import org.f2blib.FunctionEvaluationKernel;
import org.f2blib.generator.FunctionEvaluationBytecodeGenerator;
import org.f2blib.impl.FunctionEvaluation;
import org.f2blib.parser.BytecodeGeneratingFunctionsListener;
import org.f2blib.parser.FunctionParser;

import java.util.HashMap;
import java.util.Map;

/**
 * The main entry point.
 */
public class F2BLibImpl implements FunctionEvaluationKernel {

    private final Map<String, FunctionEvaluation> cache = new HashMap<>();

    private final FunctionParser parser;

    private final FunctionEvaluationBytecodeGenerator generator;

    public F2BLibImpl(FunctionParser parser, FunctionEvaluationBytecodeGenerator generator) {
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
        try {

            BytecodeGeneratingFunctionsListener listener = new BytecodeGeneratingFunctionsListener();

            parser.applyListener(functionDefinition, listener);

            Class<? extends FunctionEvaluation> clazz = generator.generateClass(listener);

            // TODO SF Replace by class Constructor
            FunctionEvaluation instance = clazz.newInstance();

            cache.put(clazz.getName(), instance);

        } catch (InstantiationException e) {
            e.printStackTrace(); // TODO SF
        } catch (IllegalAccessException e) {
            e.printStackTrace(); // TODO SF
        }

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
            throw new RuntimeException("TODO SF: Unknown function definition: " + functionName);
        }

        functionEvaluation.eval(p, x, y);
    }

}
