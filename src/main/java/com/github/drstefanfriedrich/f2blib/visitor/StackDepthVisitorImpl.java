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

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class StackDepthVisitorImpl implements StackDepthVisitor {

    private int maxStackDepth;

    @Override
    public int getMaxStackDepth() {
        return maxStackDepth;
    }

    @Override
    public Void visitFunctionDefinition(FunctionDefinition functionDefinition) {
        maxStackDepth = functionDefinition.getFunctionBody().accept(this);
        return null;
    }

    @Override
    public Integer visitFunctionBody(FunctionBody functionBody) {
        if (functionBody.isForLoop()) {
            return functionBody.getForLoop().accept(this);
        } else {
            return functionBody.getFunctionsWrapper().accept(this);
        }
    }

    @Override
    public Integer visitFunctionsWrapper(FunctionsWrapper functionsWrapper) {
        return functionsWrapper.getFunctions().stream()
                .map(f -> ((Integer) f.accept(this)))
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new IllegalStateException("Empty function not allowed"));
    }

    @Override
    public Integer visitFunction(Function function) {
        return 2 + (Integer) function.acceptExpression(this);
    }

    @Override
    public Integer visitAbs(Abs abs) {
        return abs.acceptExpression(this);
    }

    @Override
    public Integer visitAddition(Addition addition) {
        return ((Integer) addition.acceptLeft(this)) + ((Integer) addition.acceptRight(this));
    }

    @Override
    public Integer visitArccos(Arccos arccos) {
        return arccos.acceptExpression(this);
    }

    @Override
    public Integer visitArcosh(Arcosh arcosh) {
        return 1 + (Integer) arcosh.acceptExpression(this);
    }

    @Override
    public Integer visitArcsin(Arcsin arcsin) {
        return arcsin.acceptExpression(this);
    }

    @Override
    public Integer visitArctan(Arctan arctan) {
        return arctan.acceptExpression(this);
    }

    @Override
    public Integer visitArsinh(Arsinh arsinh) {
        return 1 + (Integer) arsinh.acceptExpression(this);
    }

    @Override
    public Integer visitArtanh(Artanh artanh) {
        return 1 + (Integer) artanh.acceptExpression(this);
    }

    @Override
    public Integer visitBinomial(Binomial binomial) {
        return ((Integer) binomial.acceptN(this)) + ((Integer) binomial.acceptK(this));
    }

    @Override
    public Integer visitConstant(Constant constant) {
        return 2;
    }

    @Override
    public Integer visitCos(Cos cos) {
        return cos.acceptExpression(this);
    }

    @Override
    public Integer visitCosh(Cosh cosh) {
        return cosh.acceptExpression(this);
    }

    @Override
    public Integer visitDivision(Division division) {
        return ((Integer) division.acceptLeft(this)) + ((Integer) division.acceptRight(this));
    }

    @Override
    public Integer visitExp(Exp exp) {
        return exp.acceptExpression(this);
    }

    @Override
    public Integer visitFaculty(Faculty faculty) {
        return faculty.acceptIntExpression(this);
    }

    @Override
    public Integer visitInt(Int i) {
        return 2;
    }

    @Override
    public Integer visitLn(Ln ln) {
        return ln.acceptExpression(this);
    }

    @Override
    public Integer visitMultiplication(Multiplication multiplication) {
        return ((Integer) multiplication.acceptLeft(this)) + ((Integer) multiplication.acceptRight(this));
    }

    @Override
    public Integer visitParameter(Parameter parameter) {
        return 2;
    }

    @Override
    public Integer visitParenthesis(Parenthesis parenthesis) {
        return parenthesis.acceptExpression(this);
    }

    @Override
    public Integer visitPower(Power power) {
        return ((Integer) power.acceptLeft(this)) + ((Integer) power.acceptRight(this));
    }

    @Override
    public Integer visitRound(Round round) {
        return round.acceptExpression(this);
    }

    @Override
    public Integer visitSin(Sin sin) {
        return sin.acceptExpression(this);
    }

    @Override
    public Integer visitSinh(Sinh sinh) {
        return sinh.acceptExpression(this);
    }

    @Override
    public Integer visitSubtraction(Subtraction subtraction) {
        return ((Integer) subtraction.acceptLeft(this)) + ((Integer) subtraction.acceptRight(this));
    }

    @Override
    public Integer visitTan(Tan tan) {
        return tan.acceptExpression(this);
    }

    @Override
    public Integer visitTanh(Tanh tanh) {
        return tanh.acceptExpression(this);
    }

    @Override
    public Integer visitVariable(Variable variable) {
        return 2;
    }

    @Override
    public Integer visitNeg(Neg neg) {
        return neg.acceptExpression(this);
    }

    @Override
    public Integer visitPos(Pos pos) {
        return pos.acceptExpression(this);
    }

    @Override
    public Integer visitDoub(Doub doub) {
        return 2;
    }

    @Override
    public Integer visitSqrt(Sqrt sqrt) {
        return sqrt.acceptExpression(this);
    }

    @Override
    public Integer visitNoOp(NoOp noOp) {
        return 0;
    }

    @Override
    public Integer visitForLoop(ForLoop forLoop) {
        return 2 + (Integer) forLoop.acceptFunctionsWrapper(this) +
                Collections.max(Arrays.<Integer>asList(forLoop.acceptStart(this),
                        forLoop.acceptEnd(this), forLoop.acceptStep(this)));
    }

    @Override
    public Integer visitForVar(ForVar forVar) {
        return 2;
    }

}
