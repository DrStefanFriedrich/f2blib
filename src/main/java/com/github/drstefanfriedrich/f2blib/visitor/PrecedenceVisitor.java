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

import com.github.drstefanfriedrich.f2blib.ast.*;

/**
 * A {@link Visitor} that returns the operator precedence of each node. Operator
 * precedence is the order in which operators will be evaluated or applied. 0
 * means the highest binding or coupling.
 */
public class PrecedenceVisitor implements Visitor {

    public static final String EXCEPTION_MESSAGE = "This method must not be called on the PrecedenceVisitor class";

    @Override
    public Integer visit(Abs abs) {
        return 4;
    }

    @Override
    public Integer visit(Addition addition) {
        return 6;
    }

    @Override
    public Integer visit(Arccos arccos) {
        return 4;
    }

    @Override
    public Integer visit(Arcosh arcosh) {
        return 4;
    }

    @Override
    public Integer visit(Arcsin arcsin) {
        return 4;
    }

    @Override
    public Integer visit(Arctan arctan) {
        return 4;
    }

    @Override
    public Integer visit(Arsinh arsinh) {
        return 4;
    }

    @Override
    public Integer visit(Artanh artanh) {
        return 4;
    }

    @Override
    public Integer visit(Binomial binomial) {
        return 4;
    }

    @Override
    public Integer visit(Constant constant) {
        return 0;
    }

    @Override
    public Integer visit(Cos cos) {
        return 4;
    }

    @Override
    public Integer visit(Cosh cosh) {
        return 4;
    }

    @Override
    public Integer visit(Division division) {
        return 5;
    }

    @Override
    public Integer visit(Exp exp) {
        return 4;
    }

    @Override
    public Integer visit(Faculty faculty) {
        return 2;
    }

    @Override
    public Integer visit(Function function) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public Integer visit(FunctionBody functionBody) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public Integer visit(FunctionDefinition functionDefinition) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public Integer visit(FunctionsWrapper functionsWrapper) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public Integer visit(Int i) {
        return 0;
    }

    @Override
    public Integer visit(Ln ln) {
        return 4;
    }

    @Override
    public Integer visit(Multiplication multiplication) {
        return 5;
    }

    @Override
    public Integer visit(Parameter parameter) {
        return 0;
    }

    @Override
    public Integer visit(Parenthesis parenthesis) {
        return 4;
    }

    @Override
    public Integer visit(Power power) {
        return 1;
    }

    @Override
    public Integer visit(Round round) {
        return 4;
    }

    @Override
    public Integer visit(Sin sin) {
        return 4;
    }

    @Override
    public Integer visit(Sinh sinh) {
        return 4;
    }

    @Override
    public Integer visit(Subtraction subtraction) {
        return 6;
    }

    @Override
    public Integer visit(Tan tan) {
        return 4;
    }

    @Override
    public Integer visit(Tanh tanh) {
        return 4;
    }

    @Override
    public Integer visit(Variable variable) {
        return 0;
    }

    @Override
    public Integer visit(Neg neg) {
        return 4;
    }

    @Override
    public Integer visit(Pos pos) {
        return 4;
    }

    @Override
    public Integer visit(Doub doub) {
        return 0;
    }

    @Override
    public Integer visit(Sqrt sqrt) {
        return 4;
    }

    @Override
    public Integer visit(NoOp noOp) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public <T> T visit(ForLoop forLoop) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public Integer visit(IntVar intVar) {
        return 0;
    }

    @Override
    public <T> T visit(MarkovShift markovShift) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public Integer visit(Sum sum) {
        return 4;
    }

    @Override
    public Integer visit(Prod prod) {
        return 4;
    }

    @Override
    public Integer visit(AuxVar auxVar) {
        return 0;
    }

    @Override
    public <T> T visit(AuxiliaryVariable auxiliaryVariable) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

}
