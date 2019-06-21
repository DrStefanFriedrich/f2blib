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
        abs.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visitAddition(Addition addition) {
        addition.acceptLeft(this);
        addition.acceptRight(this);
        return null;
    }

    @Override
    public <T> T visitArccos(Arccos arccos) {
        arccos.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visitArcosh(Arcosh arcosh) {
        arcosh.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visitArcsin(Arcsin arcsin) {
        arcsin.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visitArctan(Arctan arctan) {
        arctan.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visitArsinh(Arsinh arsinh) {
        arsinh.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visitArtanh(Artanh artanh) {
        artanh.acceptExpression(this);
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
        cos.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visitCosh(Cosh cosh) {
        cosh.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visitDivision(Division division) {
        division.acceptLeft(this);
        division.acceptRight(this);
        return null;
    }

    @Override
    public <T> T visitExp(Exp exp) {
        exp.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visitFaculty(Faculty faculty) {
        faculty.getIntExpression().accept(this);
        return null;
    }

    @Override
    public <T> T visitFunction(Function function) {
        function.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visitFunctionBody(FunctionBody functionBody) {
        functionBody.getFunctionsWrapper().accept(this);
        return null;
    }

    @Override
    public <T> T visitFunctionDefinition(FunctionDefinition functionDefinition) {
        functionDefinition.getFunctionBody().accept(this);
        return null;
    }

    @Override
    public <T> T visitFunctionsWrapper(FunctionsWrapper functionsWrapper) {
        functionsWrapper.getFunctions().forEach(f -> f.accept(this));
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
        ln.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visitMultiplication(Multiplication multiplication) {
        multiplication.acceptLeft(this);
        multiplication.acceptRight(this);
        return null;
    }

    @Override
    public <T> T visitParameter(Parameter parameter) {
        return null;
    }

    @Override
    public <T> T visitParenthesis(Parenthesis parenthesis) {
        parenthesis.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visitPower(Power power) {
        power.acceptLeft(this);
        power.acceptRight(this);
        return null;
    }

    @Override
    public <T> T visitRound(Round round) {
        round.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visitSin(Sin sin) {
        sin.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visitSinh(Sinh sinh) {
        sinh.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visitSubtraction(Subtraction subtraction) {
        subtraction.acceptLeft(this);
        subtraction.acceptRight(this);
        return null;
    }

    @Override
    public <T> T visitTan(Tan tan) {
        tan.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visitTanh(Tanh tanh) {
        tanh.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visitVariable(Variable variable) {
        return null;
    }

    @Override
    public <T> T visitNeg(Neg neg) {
        neg.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visitPos(Pos pos) {
        pos.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visitSqrt(Sqrt sqrt) {
        sqrt.acceptExpression(this);
        return null;
    }

}
