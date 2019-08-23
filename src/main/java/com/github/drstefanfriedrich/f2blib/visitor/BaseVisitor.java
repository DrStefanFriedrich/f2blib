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
 * Empty {@link Visitor} implementation useful as base class for visitors visiting
 * only a sub set of elements.
 */
public class BaseVisitor extends AbstractVisitor {

    @Override
    public <T> T visit(Abs abs) {
        abs.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Addition addition) {
        addition.acceptLeft(this);
        addition.acceptRight(this);
        return null;
    }

    @Override
    public <T> T visit(Arccos arccos) {
        arccos.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Arcosh arcosh) {
        arcosh.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Arcsin arcsin) {
        arcsin.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Arctan arctan) {
        arctan.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Arsinh arsinh) {
        arsinh.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Artanh artanh) {
        artanh.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Binomial binomial) {
        binomial.acceptN(this);
        binomial.acceptK(this);
        return null;
    }

    @Override
    public <T> T visit(Constant constant) {
        return null;
    }

    @Override
    public <T> T visit(Cos cos) {
        cos.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Cosh cosh) {
        cosh.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Division division) {
        division.acceptLeft(this);
        division.acceptRight(this);
        return null;
    }

    @Override
    public <T> T visit(Exp exp) {
        exp.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Faculty faculty) {
        faculty.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Int i) {
        return null;
    }

    @Override
    public <T> T visit(Doub doub) {
        return null;
    }

    @Override
    public <T> T visit(Ln ln) {
        ln.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Multiplication multiplication) {
        multiplication.acceptLeft(this);
        multiplication.acceptRight(this);
        return null;
    }

    @Override
    public <T> T visit(Parameter parameter) {
        IntExpression indexExpression = parameter.getIndexExpression();
        if (indexExpression != null) {
            indexExpression.accept(this);
        }
        return null;
    }

    @Override
    public <T> T visit(Parenthesis parenthesis) {
        parenthesis.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Power power) {
        power.acceptLeft(this);
        power.acceptRight(this);
        return null;
    }

    @Override
    public <T> T visit(Round round) {
        round.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Sin sin) {
        sin.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Sinh sinh) {
        sinh.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Subtraction subtraction) {
        subtraction.acceptLeft(this);
        subtraction.acceptRight(this);
        return null;
    }

    @Override
    public <T> T visit(Tan tan) {
        tan.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Tanh tanh) {
        tanh.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Variable variable) {
        IntExpression indexExpression = variable.getIndexExpression();
        if (indexExpression != null) {
            indexExpression.accept(this);
        }
        return null;
    }

    @Override
    public <T> T visit(Neg neg) {
        neg.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Pos pos) {
        pos.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(Sqrt sqrt) {
        sqrt.acceptExpression(this);
        return null;
    }

    @Override
    public <T> T visit(ForLoop forLoop) {
        forLoop.acceptStart(this);
        forLoop.acceptEnd(this);
        forLoop.acceptStep(this);
        forLoop.acceptFunctionsWrapper(this);
        return null;
    }

    @Override
    public <T> T visit(IntVar intVar) {
        return null;
    }

    @Override
    public <T> T visit(NoOp noOp) {
        return null;
    }

    @Override
    public <T> T visit(MarkovShift markovShift) {
        markovShift.getOffset().accept(this);
        return null;
    }

    @Override
    public <T> T visit(Sum sum) {
        sum.acceptInner(this);
        sum.acceptStart(this);
        sum.acceptEnd(this);
        return null;
    }

    @Override
    public <T> T visit(Prod prod) {
        prod.acceptInner(this);
        prod.acceptStart(this);
        prod.acceptEnd(this);
        return null;
    }

}
