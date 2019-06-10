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

package org.f2blib.visitor;

import org.f2blib.ast.*;

/**
 * A {@link Visitor} that return the operator precedence of each node. Operator
 * precedence is the order in which operators will be evaluated or applied. 0
 * means the highest binding or coupling.
 */
public class PrecedenceVisitor implements Visitor {

    @Override
    public Integer visitAbs(Abs abs) {
        return 1;
    }

    @Override
    public Integer visitAddition(Addition addition) {
        return 3;
    }

    @Override
    public Integer visitArccos(Arccos arccos) {
        return 1;
    }

    @Override
    public Integer visitArcosh(Arcosh arcosh) {
        return 1;
    }

    @Override
    public Integer visitArcsin(Arcsin arcsin) {
        return 1;
    }

    @Override
    public Integer visitArctan(Arctan arctan) {
        return 1;
    }

    @Override
    public Integer visitArsinh(Arsinh arsinh) {
        return 1;
    }

    @Override
    public Integer visitArtanh(Artanh artanh) {
        return 1;
    }

    @Override
    public Integer visitBinomial(Binomial binomial) {
        return 1;
    }

    @Override
    public Integer visitConstant(Constant constant) {
        return 0;
    }

    @Override
    public Integer visitCos(Cos cos) {
        return 1;
    }

    @Override
    public Integer visitCosh(Cosh cosh) {
        return 1;
    }

    @Override
    public Integer visitDivision(Division division) {
        return 2;
    }

    @Override
    public Integer visitExp(Exp exp) {
        return 1;
    }

    @Override
    public Integer visitFaculty(Faculty faculty) {
        return 1;
    }

    @Override
    public Integer visitFunction(Function function) {
        throw new IllegalStateException("This method must not be called on the PrecedenceVisitor class");
    }

    @Override
    public Integer visitFunctionBody(FunctionBody functionBody) {
        throw new IllegalStateException("This method must not be called on the PrecedenceVisitor class");
    }

    @Override
    public Integer visitFunctionDefinition(FunctionDefinition functionDefinition) {
        throw new IllegalStateException("This method must not be called on the PrecedenceVisitor class");
    }

    @Override
    public Integer visitFunctions(Functions functions) {
        throw new IllegalStateException("This method must not be called on the PrecedenceVisitor class");
    }

    @Override
    public Integer visitInt(Int i) {
        return 0;
    }

    @Override
    public Integer visitLn(Ln ln) {
        return 1;
    }

    @Override
    public Integer visitMultiplication(Multiplication multiplication) {
        return 2;
    }

    @Override
    public Integer visitParameter(Parameter parameter) {
        return 0;
    }

    @Override
    public Integer visitParenthesis(Parenthesis parenthesis) {
        return 0;
    }

    @Override
    public Integer visitPower(Power power) {
        return 1;
    }

    @Override
    public Integer visitRound(Round round) {
        return 1;
    }

    @Override
    public Integer visitSin(Sin sin) {
        return 1;
    }

    @Override
    public Integer visitSinh(Sinh sinh) {
        return 1;
    }

    @Override
    public Integer visitSubtraction(Subtraction subtraction) {
        return 3;
    }

    @Override
    public Integer visitTan(Tan tan) {
        return 1;
    }

    @Override
    public Integer visitTanh(Tanh tanh) {
        return 1;
    }

    @Override
    public Integer visitVariable(Variable variable) {
        return 0;
    }

    @Override
    public Integer visitNeg(Neg neg) {
        return 1;
    }

    @Override
    public Integer visitPos(Pos pos) {
        return 1;
    }

    @Override
    public Integer visitDoub(Doub doub) {
        return 0;
    }

    @Override
    public Integer visitSqrt(Sqrt sqrt) {
        return 1;
    }

}
