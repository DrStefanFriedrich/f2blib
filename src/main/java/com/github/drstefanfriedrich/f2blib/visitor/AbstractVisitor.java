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

import com.github.drstefanfriedrich.f2blib.ast.Function;
import com.github.drstefanfriedrich.f2blib.ast.FunctionBody;
import com.github.drstefanfriedrich.f2blib.ast.FunctionDefinition;
import com.github.drstefanfriedrich.f2blib.ast.FunctionsWrapper;

/**
 * A {@link Visitor} implementing some basic functionality.
 */
public abstract class AbstractVisitor implements Visitor {

    protected final static String UNSUPPORTED_OPERATION = "Unsupported operation";

    @Override
    public <T> T visit(FunctionDefinition functionDefinition) {
        functionDefinition.getFunctionBody().accept(this);
        return null;
    }

    @Override
    public <T> T visit(FunctionBody functionBody) {

        if (functionBody.isForLoop()) {
            functionBody.getForLoop().accept(this);
        } else {
            functionBody.getFunctionsWrapper().accept(this);
        }

        return null;
    }

    @Override
    public <T> T visit(FunctionsWrapper functionsWrapper) {
        functionsWrapper.getFunctions().forEach(f -> f.accept(this));
        functionsWrapper.acceptMarkovShift(this);
        return null;
    }

    @Override
    public <T> T visit(Function function) {
        function.acceptExpression(this);
        return null;
    }

}
