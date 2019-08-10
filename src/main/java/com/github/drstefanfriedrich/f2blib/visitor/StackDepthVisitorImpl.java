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

import static java.lang.Math.max;

public class StackDepthVisitorImpl implements StackDepthVisitor {

    private int maxStackDepth;

    @Override
    public int getMaxStackDepth() {
        return maxStackDepth;
    }

    @Override
    public Void visit(FunctionDefinition functionDefinition) {
        maxStackDepth = functionDefinition.getFunctionBody().accept(this);
        return null;
    }

    @Override
    public Integer visit(FunctionBody functionBody) {
        if (functionBody.isForLoop()) {
            return functionBody.getForLoop().accept(this);
        } else {
            return functionBody.getFunctionsWrapper().accept(this);
        }
    }

    @Override
    public Integer visit(FunctionsWrapper functionsWrapper) {
        return max(
                functionsWrapper.getFunctions().stream()
                        .map(f -> ((Integer) f.accept(this)))
                        .max(Comparator.naturalOrder())
                        .orElseThrow(() -> new IllegalStateException("Empty function not allowed")),
                (int) functionsWrapper.acceptMarkovShift(this).orElse(0)
        );
    }

    @Override
    public Integer visit(Function function) {
        return 2 + (Integer) function.acceptExpression(this);
    }

    @Override
    public Integer visit(Abs abs) {
        return abs.acceptExpression(this);
    }

    @Override
    public Integer visit(Addition addition) {
        return ((Integer) addition.acceptLeft(this)) + ((Integer) addition.acceptRight(this));
    }

    @Override
    public Integer visit(Arccos arccos) {
        return arccos.acceptExpression(this);
    }

    @Override
    public Integer visit(Arcosh arcosh) {
        return 1 + (Integer) arcosh.acceptExpression(this);
    }

    @Override
    public Integer visit(Arcsin arcsin) {
        return arcsin.acceptExpression(this);
    }

    @Override
    public Integer visit(Arctan arctan) {
        return arctan.acceptExpression(this);
    }

    @Override
    public Integer visit(Arsinh arsinh) {
        return 1 + (Integer) arsinh.acceptExpression(this);
    }

    @Override
    public Integer visit(Artanh artanh) {
        return 1 + (Integer) artanh.acceptExpression(this);
    }

    @Override
    public Integer visit(Binomial binomial) {
        return ((Integer) binomial.acceptN(this)) + ((Integer) binomial.acceptK(this));
    }

    @Override
    public Integer visit(Constant constant) {
        return 2;
    }

    @Override
    public Integer visit(Cos cos) {
        return cos.acceptExpression(this);
    }

    @Override
    public Integer visit(Cosh cosh) {
        return cosh.acceptExpression(this);
    }

    @Override
    public Integer visit(Division division) {
        return ((Integer) division.acceptLeft(this)) + ((Integer) division.acceptRight(this));
    }

    @Override
    public Integer visit(Exp exp) {
        return exp.acceptExpression(this);
    }

    @Override
    public Integer visit(Faculty faculty) {
        return faculty.acceptExpression(this);
    }

    @Override
    public Integer visit(Int i) {
        return 2;
    }

    @Override
    public Integer visit(Ln ln) {
        return ln.acceptExpression(this);
    }

    @Override
    public Integer visit(Multiplication multiplication) {
        return ((Integer) multiplication.acceptLeft(this)) + ((Integer) multiplication.acceptRight(this));
    }

    @Override
    public Integer visit(Parameter parameter) {
        return 2 + (parameter.getIndexExpression() == null ? 0 : (Integer) parameter.getIndexExpression().accept(this));
    }

    @Override
    public Integer visit(Parenthesis parenthesis) {
        return parenthesis.acceptExpression(this);
    }

    @Override
    public Integer visit(Power power) {
        return ((Integer) power.acceptLeft(this)) + ((Integer) power.acceptRight(this));
    }

    @Override
    public Integer visit(Round round) {
        return round.acceptExpression(this);
    }

    @Override
    public Integer visit(Sin sin) {
        return sin.acceptExpression(this);
    }

    @Override
    public Integer visit(Sinh sinh) {
        return sinh.acceptExpression(this);
    }

    @Override
    public Integer visit(Subtraction subtraction) {
        return ((Integer) subtraction.acceptLeft(this)) + ((Integer) subtraction.acceptRight(this));
    }

    @Override
    public Integer visit(Tan tan) {
        return tan.acceptExpression(this);
    }

    @Override
    public Integer visit(Tanh tanh) {
        return tanh.acceptExpression(this);
    }

    @Override
    public Integer visit(Variable variable) {
        return 2 + (variable.getIndexExpression() == null ? 0 : (Integer) variable.getIndexExpression().accept(this));
    }

    @Override
    public Integer visit(Neg neg) {
        return neg.acceptExpression(this);
    }

    @Override
    public Integer visit(Pos pos) {
        return pos.acceptExpression(this);
    }

    @Override
    public Integer visit(Doub doub) {
        return 2;
    }

    @Override
    public Integer visit(Sqrt sqrt) {
        return sqrt.acceptExpression(this);
    }

    @Override
    public Integer visit(NoOp noOp) {
        return 0;
    }

    @Override
    public Integer visit(ForLoop forLoop) {
        return 2 + (Integer) forLoop.acceptFunctionsWrapper(this) +
                Collections.max(Arrays.<Integer>asList(forLoop.acceptStart(this),
                        forLoop.acceptEnd(this), forLoop.acceptStep(this)));
    }

    @Override
    public Integer visit(ForVar forVar) {
        return 2;
    }

    @Override
    public Integer visit(MarkovShift markovShift) {
        return 5;
    }

}
