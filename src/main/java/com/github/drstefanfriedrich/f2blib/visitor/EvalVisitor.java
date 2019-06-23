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
import org.apache.commons.math3.analysis.function.Acosh;
import org.apache.commons.math3.analysis.function.Asinh;
import org.apache.commons.math3.analysis.function.Atanh;
import com.github.drstefanfriedrich.f2blib.ast.*;

import static java.lang.String.format;
import static org.apache.commons.math3.util.CombinatoricsUtils.binomialCoefficient;
import static org.apache.commons.math3.util.CombinatoricsUtils.factorial;

/**
 * A {@link Visitor} implementation that evaluates a mathematical expression
 * given as abstract syntax tree by recursively walking through the tree.
 */
public class EvalVisitor implements DoubleVisitor {

    private final double[] x;

    private final double[] p;

    private double[] y;

    private static final Asinh ARSINH = new Asinh();

    private static final Acosh ARCOSH = new Acosh();

    private static final Atanh ARTANH = new Atanh();

    EvalVisitor(double[] x, double[] p, int lengthResultArray) {
        this.x = x;
        this.p = p;
        this.y = new double[lengthResultArray];
    }

    double[] getResult() {
        return y;
    }

    @Override
    public double visitAbs(Abs abs) {
        return Math.abs(abs.acceptExpression(this));
    }

    @Override
    public double visitAddition(Addition addition) {
        return addition.acceptLeft(this) + addition.acceptRight(this);
    }

    @Override
    public double visitArccos(Arccos arccos) {
        return Math.acos(arccos.acceptExpression(this));
    }

    @Override
    public double visitArcosh(Arcosh arcosh) {
        return ARCOSH.value(arcosh.acceptExpression(this));
    }

    @Override
    public double visitArcsin(Arcsin arcsin) {
        return Math.asin(arcsin.acceptExpression(this));
    }

    @Override
    public double visitArctan(Arctan arctan) {
        return Math.atan(arctan.acceptExpression(this));
    }

    @Override
    public double visitArsinh(Arsinh arsinh) {
        return ARSINH.value(arsinh.acceptExpression(this));
    }

    @Override
    public double visitArtanh(Artanh artanh) {
        return ARTANH.value(artanh.acceptExpression(this));
    }

    @Override
    public double visitBinomial(Binomial binomial) {
        return binomialCoefficient((int) binomial.acceptN(this), (int) binomial.acceptK(this));
    }

    @Override
    public double visitFaculty(Faculty faculty) {
        return factorial((int) faculty.acceptIntExpression(this));
    }

    @Override
    public double visitConstant(Constant constant) {
        switch (constant) {
            case PI:
                return Math.PI;
            case E:
                return Math.E;
            case BOLTZMANN:
                return 1.38064852e-23;
            default:
                throw new IllegalArgumentException(format("Unrecognized constant: %s", constant.name()));
        }
    }

    @Override
    public double visitCos(Cos cos) {
        return Math.cos(cos.acceptExpression(this));
    }

    @Override
    public double visitCosh(Cosh cosh) {
        return Math.cosh(cosh.acceptExpression(this));
    }

    @Override
    public double visitDivision(Division division) {
        return division.acceptLeft(this) / division.acceptRight(this);
    }

    @Override
    public double visitExp(Exp exp) {
        return Math.exp(exp.acceptExpression(this));
    }

    @Override
    public double visitFunction(Function function) {

        int index = function.getIndex();

        y[index] = function.acceptExpression(this);

        return 0;
    }

    @Override
    public double visitFunctionBody(FunctionBody functionBody) {
        return functionBody.getFunctionsWrapper().accept(this);
    }

    @Override
    public double visitFunctionDefinition(FunctionDefinition functionDefinition) {
        return functionDefinition.getFunctionBody().accept(this);
    }

    @Override
    public double visitFunctionsWrapper(FunctionsWrapper functionsWrapper) {

        functionsWrapper.getFunctions().forEach(f -> f.accept(this));

        return 0;
    }

    @Override
    public double visitInt(Int i) {
        return i.getValue();
    }

    @Override
    public double visitDoub(Doub doub) {
        return doub.getValue();
    }

    @Override
    public double visitSqrt(Sqrt sqrt) {
        return Math.sqrt(sqrt.acceptExpression(this));
    }

    @Override
    public double visitNoOp(NoOp noOp) {
        throw new IllegalStateException("visitNoOp must not be called on the EvalVisitor");
    }

    @Override
    public double visitLn(Ln ln) {
        return Math.log(ln.acceptExpression(this));
    }

    @Override
    public double visitMultiplication(Multiplication multiplication) {
        return multiplication.acceptLeft(this) * multiplication.acceptRight(this);
    }

    @Override
    public double visitParameter(Parameter parameter) {
        return p[parameter.getIndex()];
    }

    @Override
    public double visitParenthesis(Parenthesis parenthesis) {
        return parenthesis.acceptExpression(this);
    }

    @Override
    public double visitPower(Power power) {
        return Math.pow(power.acceptLeft(this), power.acceptRight(this));
    }

    @Override
    public double visitRound(Round round) {
        return Math.round(round.acceptExpression(this));
    }

    @Override
    public double visitSin(Sin sin) {
        return Math.sin(sin.acceptExpression(this));
    }

    @Override
    public double visitSinh(Sinh sinh) {
        return Math.sinh(sinh.acceptExpression(this));
    }

    @Override
    public double visitSubtraction(Subtraction subtraction) {
        return subtraction.acceptLeft(this) - subtraction.acceptRight(this);
    }

    @Override
    public double visitTan(Tan tan) {
        return Math.tan(tan.acceptExpression(this));
    }

    @Override
    public double visitTanh(Tanh tanh) {
        return Math.tanh(tanh.acceptExpression(this));
    }

    @Override
    public double visitVariable(Variable variable) {
        return x[variable.getIndex()];
    }

    @Override
    public double visitNeg(Neg neg) {
        return -neg.acceptExpression(this);
    }

    @Override
    public double visitPos(Pos pos) {
        return pos.acceptExpression(this);
    }

}
