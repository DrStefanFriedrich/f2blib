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
 * Empty {@link Visitor} implementation useful as base class for visitors visiting
 * only a sub set of elements.
 */
public abstract class AbstractVisitor implements Visitor {

    @Override
    public <T> T visitAbs(Abs abs) {
        abs.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitAddition(Addition addition) {
        addition.getLeft().accept(this);
        addition.getRight().accept(this);
        return null;
    }

    @Override
    public <T> T visitArccos(Arccos arccos) {
        arccos.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitArcosh(Arcosh arcosh) {
        arcosh.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitArcsin(Arcsin arcsin) {
        arcsin.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitArctan(Arctan arctan) {
        arctan.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitArsinh(Arsinh arsinh) {
        arsinh.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitArtanh(Artanh artanh) {
        artanh.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitBinomial(Binomial binomial) {
        binomial.getN().accept(this);
        binomial.getK().accept(this);
        return null;
    }

    @Override
    public <T> T visitConstant(Constant constant) {
        return null;
    }

    @Override
    public <T> T visitCos(Cos cos) {
        cos.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitCosh(Cosh cosh) {
        cosh.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitDivision(Division division) {
        division.getLeft().accept(this);
        division.getRight().accept(this);
        return null;
    }

    @Override
    public <T> T visitExp(Exp exp) {
        exp.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitFaculty(Faculty faculty) {
        faculty.getIntExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitFunction(Function function) {
        function.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitFunctionBody(FunctionBody functionBody) {
        functionBody.getFunctions().accept(this);
        return null;
    }

    @Override
    public <T> T visitFunctionDefinition(FunctionDefinition functionDefinition) {
        functionDefinition.getFunctionBody().accept(this);
        return null;
    }

    @Override
    public <T> T visitFunctions(Functions functions) {
        functions.getFunctions().forEach(f -> f.accept(this));
        return null;
    }

    @Override
    public <T> T visitInt(Int i) {
        return null;
    }

    @Override
    public <T> T visitDoub(Doub doub) {
        return null;
    }

    @Override
    public <T> T visitLn(Ln ln) {
        ln.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitMultiplication(Multiplication multiplication) {
        multiplication.getLeft().accept(this);
        multiplication.getRight().accept(this);
        return null;
    }

    @Override
    public <T> T visitParameter(Parameter parameter) {
        return null;
    }

    @Override
    public <T> T visitParenthesis(Parenthesis parenthesis) {
        parenthesis.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitPower(Power power) {
        power.getLeft().accept(this);
        power.getRight().accept(this);
        return null;
    }

    @Override
    public <T> T visitRound(Round round) {
        round.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitSin(Sin sin) {
        sin.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitSinh(Sinh sinh) {
        sinh.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitSubtraction(Subtraction subtraction) {
        subtraction.getLeft().accept(this);
        subtraction.getRight().accept(this);
        return null;
    }

    @Override
    public <T> T visitTan(Tan tan) {
        tan.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitTanh(Tanh tanh) {
        tanh.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitVariable(Variable variable) {
        return null;
    }

    @Override
    public <T> T visitNeg(Neg neg) {
        neg.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitPos(Pos pos) {
        pos.getExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitSqrt(Sqrt sqrt) {
        sqrt.getExpression().accept(this);
        return null;
    }

}
