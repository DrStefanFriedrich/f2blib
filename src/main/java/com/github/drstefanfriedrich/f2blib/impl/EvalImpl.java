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
import com.github.drstefanfriedrich.f2blib.parser.FunctionParser;
import com.github.drstefanfriedrich.f2blib.visitor.EvalVisitorImpl;
import com.github.drstefanfriedrich.f2blib.visitor.ValidationVisitor;
import com.github.drstefanfriedrich.f2blib.visitor.ValidationVisitorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

/**
 * Implementation of {@link FunctionEvaluationKernel} using {@link EvalVisitorImpl}.
 */
public class EvalImpl extends AbstractFEKImpl {

    private static final Logger LOG = LoggerFactory.getLogger(EvalImpl.class);

    EvalImpl(FunctionParser parser) {
        super(parser);
    }

    @Override
    public void load(String functionDefinition) {

        FunctionDefinition fd = parser.parse(functionDefinition);

        ValidationVisitor validationVisitor = new ValidationVisitorImpl();
        fd.accept(validationVisitor);

        FunctionInfo fi = new FunctionInfo(fd, prettyPrint(fd), validationVisitor.getFunctionEvaluationValidator());

        String name = fd.getName();
        cache.put(name, fi);

        LOG.info("Function {} loaded into the kernel", name);
    }

    @Override
    public void eval(String functionName, double[] p, double[] x, double[] y) {

        FunctionInfo fi = cache.get(functionName);

        if (fi == null) {
            throw new IllegalArgumentException(format("Unknown function name: %s", functionName));
        }

        long start = System.nanoTime();

        FunctionDefinition fd = fi.getFunctionDefinition();
        fi.getFunctionEvaluationValidator().validate(p, x, y);

        EvalVisitorImpl evalVisitorImpl = new EvalVisitorImpl(x, p, y.length);
        fd.accept(evalVisitorImpl);
        double[] result = evalVisitorImpl.getResult();

        System.arraycopy(result, 0, y, 0, y.length);

        long end = System.nanoTime();

        LOG.trace("Evaluation of function {} took {} ns", functionName, (end - start));
        LOG.debug("Function {} evaluated", functionName);
    }

}
