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

import com.github.drstefanfriedrich.f2blib.ast.Neg;
import com.github.drstefanfriedrich.f2blib.ast.NoOp;
import com.github.drstefanfriedrich.f2blib.ast.Pos;
import com.github.drstefanfriedrich.f2blib.ast.Sqrt;
import com.github.drstefanfriedrich.f2blib.ast.*;

/**
 * A {@link Visitor} that returns the operator precedence of each node. Operator
 * precedence is the order in which operators will be evaluated or applied. 0
 * means the highest binding or coupling.
 */
public class PrecedenceVisitor implements Visitor {

    public static final String EXCEPTION_MESSAGE = "This method must not be called on the PrecedenceVisitor class";

    @Override
    public Integer visitAbs(Abs abs) {
        return 4;
    }

    @Override
    public Integer visitAddition(Addition addition) {
        return 6;
    }

    @Override
    public Integer visitArccos(Arccos arccos) {
        return 4;
    }

    @Override
    public Integer visitArcosh(Arcosh arcosh) {
        return 4;
    }

    @Override
    public Integer visitArcsin(Arcsin arcsin) {
        return 4;
    }

    @Override
    public Integer visitArctan(Arctan arctan) {
        return 4;
    }

    @Override
    public Integer visitArsinh(Arsinh arsinh) {
        return 4;
    }

    @Override
    public Integer visitArtanh(Artanh artanh) {
        return 4;
    }

    @Override
    public Integer visitBinomial(Binomial binomial) {
        return 4;
    }

    @Override
    public Integer visitConstant(Constant constant) {
        return 0;
    }

    @Override
    public Integer visitCos(Cos cos) {
        return 4;
    }

    @Override
    public Integer visitCosh(Cosh cosh) {
        return 4;
    }

    @Override
    public Integer visitDivision(Division division) {
        return 5;
    }

    @Override
    public Integer visitExp(Exp exp) {
        return 4;
    }

    @Override
    public Integer visitFaculty(Faculty faculty) {
        return 2;
    }

    @Override
    public Integer visitFunction(Function function) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public Integer visitFunctionBody(FunctionBody functionBody) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public Integer visitFunctionDefinition(FunctionDefinition functionDefinition) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public Integer visitFunctionsWrapper(FunctionsWrapper functionsWrapper) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public Integer visitInt(Int i) {
        return 0;
    }

    @Override
    public Integer visitLn(Ln ln) {
        return 4;
    }

    @Override
    public Integer visitMultiplication(Multiplication multiplication) {
        return 5;
    }

    @Override
    public Integer visitParameter(Parameter parameter) {
        return 0;
    }

    @Override
    public Integer visitParenthesis(Parenthesis parenthesis) {
        return 4;
    }

    @Override
    public Integer visitPower(Power power) {
        return 1;
    }

    @Override
    public Integer visitRound(Round round) {
        return 4;
    }

    @Override
    public Integer visitSin(Sin sin) {
        return 4;
    }

    @Override
    public Integer visitSinh(Sinh sinh) {
        return 4;
    }

    @Override
    public Integer visitSubtraction(Subtraction subtraction) {
        return 6;
    }

    @Override
    public Integer visitTan(Tan tan) {
        return 4;
    }

    @Override
    public Integer visitTanh(Tanh tanh) {
        return 4;
    }

    @Override
    public Integer visitVariable(Variable variable) {
        return 0;
    }

    @Override
    public Integer visitNeg(Neg neg) {
        return 4;
    }

    @Override
    public Integer visitPos(Pos pos) {
        return 4;
    }

    @Override
    public Integer visitDoub(Doub doub) {
        return 0;
    }

    @Override
    public Integer visitSqrt(Sqrt sqrt) {
        return 4;
    }

    @Override
    public Integer visitNoOp(NoOp noOp) {
        throw new IllegalStateException("visitNoOp must not be called on the PrecedenceVisitor");
    }

}
