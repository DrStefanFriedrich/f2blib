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
import com.github.drstefanfriedrich.f2blib.visitor.PrettyPrintVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

abstract class AbstractFEKImpl implements FunctionEvaluationKernel {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractFEKImpl.class);

    protected final Map<String, FunctionInfo> cache = new ConcurrentHashMap<>();

    protected final FunctionParser parser;

    AbstractFEKImpl(FunctionParser parser) {
        this.parser = parser;
    }

    @Override
    public void load(String functionDefinition) {

    }

    @Override
    public void eval(String functionName, double[] p, double[] x, double[] y) {

    }

    @Override
    public boolean remove(String functionName) {

        FunctionInfo fi = cache.remove(functionName);

        if (fi == null) {
            LOG.debug("Function {} not found for removal", functionName);
            return false;
        } else {
            LOG.debug("Function {} removed", functionName);
            return true;
        }
    }

    @Override
    public Set<String> list() {

        HashSet<String> result = new HashSet<>(cache.keySet());
        LOG.debug("The kernel contains {} entries", result.size());
        return result;
    }

    @Override
    public String print(String functionName) {

        FunctionInfo fi = cache.get(functionName);

        if (fi == null) {
            throw new IllegalArgumentException(format("Unknown function name: %s", functionName));
        }

        String prettyPrintedFunction = fi.getPrettyPrintedFunction();

        LOG.debug("Function {} pretty printed", functionName);
        return prettyPrintedFunction;
    }

    protected String prettyPrint(FunctionDefinition functionDefinition) {
        PrettyPrintVisitor ppv = new PrettyPrintVisitor();
        functionDefinition.accept(ppv);
        return ppv.getString();
    }

}
