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
 * A {@link Visitor} that returns the mathematical symbol for each node. Example:
 * for the sine function sin will be return. For the addition, the symbol + will
 * be returned.
 */
public class SymbolVisitor implements Visitor {

    public static final String EXCEPTION_MESSAGE = "This method must not be called on the SymbolVisitor class";

    @Override
    public String visit(Abs abs) {
        return "abs";
    }

    @Override
    public String visit(Addition addition) {
        return "+";
    }

    @Override
    public String visit(Arccos arccos) {
        return "arccos";
    }

    @Override
    public String visit(Arcosh arcosh) {
        return "arcosh";
    }

    @Override
    public String visit(Arcsin arcsin) {
        return "arcsin";
    }

    @Override
    public String visit(Arctan arctan) {
        return "arctan";
    }

    @Override
    public String visit(Arsinh arsinh) {
        return "arsinh";
    }

    @Override
    public String visit(Artanh artanh) {
        return "artanh";
    }

    @Override
    public String visit(Binomial binomial) {
        return "binomial";
    }

    @Override
    public String visit(Constant constant) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visit(Cos cos) {
        return "cos";
    }

    @Override
    public String visit(Cosh cosh) {
        return "cosh";
    }

    @Override
    public String visit(Division division) {
        return "/";
    }

    @Override
    public String visit(Exp exp) {
        return "exp";
    }

    @Override
    public String visit(Faculty faculty) {
        return "!";
    }

    @Override
    public String visit(Function function) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visit(FunctionBody functionBody) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visit(FunctionDefinition functionDefinition) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visit(FunctionsWrapper functionsWrapper) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visit(Int i) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visit(Ln ln) {
        return "ln";
    }

    @Override
    public String visit(Multiplication multiplication) {
        return "*";
    }

    @Override
    public String visit(Parameter parameter) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visit(Parenthesis parenthesis) {
        return "";
    }

    @Override
    public String visit(Power power) {
        return "^";
    }

    @Override
    public String visit(Round round) {
        return "round";
    }

    @Override
    public String visit(Sin sin) {
        return "sin";
    }

    @Override
    public String visit(Sinh sinh) {
        return "sinh";
    }

    @Override
    public String visit(Subtraction subtraction) {
        return "-";
    }

    @Override
    public String visit(Tan tan) {
        return "tan";
    }

    @Override
    public String visit(Tanh tanh) {
        return "tanh";
    }

    @Override
    public String visit(Variable variable) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visit(Neg neg) {
        return "-";
    }

    @Override
    public String visit(Pos pos) {
        return "+";
    }

    @Override
    public String visit(Doub doub) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visit(Sqrt sqrt) {
        return "sqrt";
    }

    @Override
    public String visit(NoOp noOp) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public <T> T visit(ForLoop forLoop) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visit(IntVar intVar) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visit(MarkovShift markovShift) {
        return "markov_shift";
    }

    @Override
    public String visit(Sum sum) {
        return "sum";
    }

    @Override
    public String visit(Prod prod) {
        return "prod";
    }

}
