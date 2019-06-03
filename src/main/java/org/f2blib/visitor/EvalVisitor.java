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

import org.apache.commons.math3.analysis.function.Acosh;
import org.apache.commons.math3.analysis.function.Asinh;
import org.apache.commons.math3.analysis.function.Atanh;
import org.f2blib.ast.*;

import static org.apache.commons.math3.util.CombinatoricsUtils.binomialCoefficient;
import static org.apache.commons.math3.util.CombinatoricsUtils.factorial;

public class EvalVisitor implements DoubleVisitor {

    private final double[] x;

    private final double[] p;

    private double[] y;

    private static final Asinh ARSINH = new Asinh();

    private static final Acosh ARCOSH = new Acosh();

    private static final Atanh ARTANH = new Atanh();

    public EvalVisitor(double[] x, double[] p, int lengthResultArray) {
        this.x = x;
        this.p = p;
        this.y = new double[lengthResultArray];
    }

    public double[] getResult() {
        return y;
    }

    @Override
    public double visitAbs(Abs abs) {
        return Math.abs(abs.getExpression().accept(this));
    }

    @Override
    public double visitAddition(Addition addition) {
        return addition.getLeft().accept(this) + addition.getRight().accept(this);
    }

    @Override
    public double visitArccos(Arccos arccos) {
        return Math.acos(arccos.getExpression().accept(this));
    }

    @Override
    public double visitArcosh(Arcosh arcosh) {
        return ARCOSH.value(arcosh.getExpression().accept(this));
    }

    @Override
    public double visitArcsin(Arcsin arcsin) {
        return Math.asin(arcsin.getExpression().accept(this));
    }

    @Override
    public double visitArctan(Arctan arctan) {
        return Math.atan(arctan.getExpression().accept(this));
    }

    @Override
    public double visitArsinh(Arsinh arsinh) {
        return ARSINH.value(arsinh.getExpression().accept(this));
    }

    @Override
    public double visitArtanh(Artanh artanh) {
        return ARTANH.value(artanh.getExpression().accept(this));
    }

    @Override
    public double visitBinomial(Binomial binomial) {
        return binomialCoefficient((int) binomial.getN().accept(this), (int) binomial.getK().accept(this));
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
                throw new RuntimeException("TODO SF");
        }
    }

    @Override
    public double visitCos(Cos cos) {
        return Math.cos(cos.getExpression().accept(this));
    }

    @Override
    public double visitCosh(Cosh cosh) {
        return Math.cosh(cosh.getExpression().accept(this));
    }

    @Override
    public double visitDivision(Division division) {
        return division.getLeft().accept(this) / division.getRight().accept(this);
    }

    @Override
    public double visitExp(Exp exp) {
        return Math.exp(exp.getExpression().accept(this));
    }

    @Override
    public double visitFaculty(Faculty faculty) {
        return factorial((int)faculty.getIntExpression().accept(this));
    }

    @Override
    public double visitFunction(Function function) {

        int index = function.getIndex();
        Expression expression = function.getExpression();

        y[index] = expression.accept(this);

        return 0;
    }

    @Override
    public double visitFunctionBody(FunctionBody functionBody) {
        return functionBody.getFunctions().accept(this);
    }

    @Override
    public double visitFunctionDefinition(FunctionDefinition functionDefinition) {
        return functionDefinition.getFunctionBody().accept(this);
    }

    @Override
    public double visitFunctions(Functions functions) {

        functions.getFunctions().stream().forEach(f -> f.accept(this));

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
    public double visitLaguerre(Laguerre laguerre) {
        throw new RuntimeException("TODO SF Not implemented yet");
    }

    @Override
    public double visitLegendre(Legendre legendre) {
        throw new RuntimeException("TODO SF Not implemented yet");
    }

    @Override
    public double visitLn(Ln ln) {
        return Math.log(ln.getExpression().accept(this));
    }

    @Override
    public double visitMultiplication(Multiplication multiplication) {
        return multiplication.getLeft().accept(this) * multiplication.getRight().accept(this);
    }

    @Override
    public double visitParameter(Parameter parameter) {
        return p[parameter.getIndex()];
    }

    @Override
    public double visitParenthesis(Parenthesis parenthesis) {
        return parenthesis.getExpression().accept(this);
    }

    @Override
    public double visitPower(Power power) {
        return Math.pow(power.getLeft().accept(this), power.getRight().accept(this));
    }

    @Override
    public double visitRound(Round round) {
        return Math.round(round.getExpression().accept(this));
    }

    @Override
    public double visitSin(Sin sin) {
        return Math.sin(sin.getExpression().accept(this));
    }

    @Override
    public double visitSinh(Sinh sinh) {
        return Math.sinh(sinh.getExpression().accept(this));
    }

    @Override
    public double visitSubtraction(Subtraction subtraction) {
        return subtraction.getLeft().accept(this) - subtraction.getRight().accept(this);
    }

    @Override
    public double visitTan(Tan tan) {
        return Math.tan(tan.getExpression().accept(this));
    }

    @Override
    public double visitTanh(Tanh tanh) {
        return Math.tanh(tanh.getExpression().accept(this));
    }

    @Override
    public double visitVariable(Variable variable) {
        return x[variable.getIndex()];
    }

    @Override
    public double visitNeg(Neg neg) {
        return -neg.getExpression().accept(this);
    }

    @Override
    public double visitPos(Pos pos) {
        return pos.getExpression().accept(this);
    }

}
