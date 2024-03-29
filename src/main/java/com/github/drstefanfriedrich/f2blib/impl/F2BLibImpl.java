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
import com.github.drstefanfriedrich.f2blib.generator.FunctionEvaluationWrapper;
import com.github.drstefanfriedrich.f2blib.parser.FunctionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

/**
 * F2BLib implementation of a {@link FunctionEvaluationKernel}. Generates Java
 * bytecode from the supplied functions.
 */
public class F2BLibImpl extends AbstractFEKImpl {

    private static final Logger LOG = LoggerFactory.getLogger(F2BLibImpl.class);

    private final FunctionEvaluationBytecodeGenerator generator;

    F2BLibImpl(FunctionParser parser, FunctionEvaluationBytecodeGenerator generator) {
        super(parser);
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

        FunctionEvaluationWrapper wrapper = generator.generateAndInstantiate(fd);
        FunctionEvaluation instance = wrapper.getFunctionEvaluation();

        FunctionInfo fi = new FunctionInfo(instance, prettyPrint(fd), wrapper.getFunctionEvaluationValidator());

        String name = instance.getClass().getName();
        cache.put(name, fi);

        LOG.info("Function {} loaded into the kernel", name);
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

        FunctionInfo fi = cache.get(functionName);

        if (fi == null) {
            throw new IllegalArgumentException(format("Unknown function name: %s", functionName));
        }

        long start = System.nanoTime();
        FunctionEvaluation functionEvaluation = fi.getFunctionEvaluation();
        fi.getFunctionEvaluationValidator().validate(p, x, y);
        functionEvaluation.eval(p, x, y);
        long end = System.nanoTime();

        LOG.trace("Evaluation of function {} took {} ns", functionName, (end - start));
        LOG.debug("Function {} evaluated", functionName);
    }

}
