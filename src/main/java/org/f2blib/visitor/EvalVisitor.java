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

public class EvalVisitor implements Visitor {

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

    private double toDouble(Number number) {
        return number.doubleValue();
    }

    @Override
    public Double visitAbs(Abs abs) {
        return Math.abs(toDouble(abs.getExpression().accept(this)));
    }

    @Override
    public Double visitAddition(Addition addition) {
        return (toDouble(addition.getLeft().accept(this))) + (toDouble(addition.getRight().accept(this)));
    }

    @Override
    public Double visitArccos(Arccos arccos) {
        return Math.acos(toDouble(arccos.getExpression().accept(this)));
    }

    @Override
    public Double visitArcosh(Arcosh arcosh) {
        return ARCOSH.value(toDouble(arcosh.getExpression().accept(this)));
    }

    @Override
    public Double visitArcsin(Arcsin arcsin) {
        return Math.asin(toDouble(arcsin.getExpression().accept(this)));
    }

    @Override
    public Double visitArctan(Arctan arctan) {
        return Math.atan(toDouble(arctan.getExpression().accept(this)));
    }

    @Override
    public Double visitArsinh(Arsinh arsinh) {
        return ARSINH.value(toDouble(arsinh.getExpression().accept(this)));
    }

    @Override
    public Double visitArtanh(Artanh artanh) {
        return ARTANH.value(toDouble(artanh.getExpression().accept(this)));
    }

    @Override
    public Long visitBinomial(Binomial binomial) {
        return binomialCoefficient((int) binomial.getN().accept(this), (int) binomial.getK().accept(this));
    }

    @Override
    public Double visitConstant(Constant constant) {
        switch (constant) {
            case PI:
                return Math.PI;
            case E:
                return Math.E;
            case BOLTZMANN:
                return 1.38064852e10 - 23;
            default:
                throw new RuntimeException("TODO SF");
        }
    }

    @Override
    public Double visitCos(Cos cos) {
        return Math.cos(toDouble(cos.getExpression().accept(this)));
    }

    @Override
    public Double visitCosh(Cosh cosh) {
        return Math.cosh(toDouble(cosh.getExpression().accept(this)));
    }

    @Override
    public Double visitDivision(Division division) {
        return (toDouble(division.getLeft().accept(this))) / (toDouble(division.getRight().accept(this)));
    }

    @Override
    public Double visitExp(Exp exp) {
        return Math.exp(toDouble(exp.getExpression().accept(this)));
    }

    @Override
    public Long visitFaculty(Faculty faculty) {
        return factorial(faculty.getIntExpression().accept(this));
    }

    @Override
    public Void visitFunction(Function function) {

        int index = function.getIndex();
        Expression expression = function.getExpression();

        y[index] = toDouble(expression.accept(this));

        return null;
    }

    @Override
    public Void visitFunctionBody(FunctionBody functionBody) {
        return functionBody.getFunctions().accept(this);
    }

    @Override
    public Void visitFunctionDefinition(FunctionDefinition functionDefinition) {
        return functionDefinition.getFunctionBody().accept(this);
    }

    @Override
    public Void visitFunctions(Functions functions) {

        functions.getFunctions().stream().forEach(f -> f.accept(this));

        return null;
    }

    @Override
    public Integer visitInt(Int i) {
        return i.getValue();
    }

    @Override
    public Void visitLaguerre(Laguerre laguerre) {
        throw new RuntimeException("TODO SF Not implemented yet");
    }

    @Override
    public Void visitLegendre(Legendre legendre) {
        throw new RuntimeException("TODO SF Not implemented yet");
    }

    @Override
    public Double visitLn(Ln ln) {
        return Math.log(toDouble(ln.getExpression().accept(this)));
    }

    @Override
    public Double visitMultiplication(Multiplication multiplication) {
        return (toDouble(multiplication.getLeft().accept(this))) * (toDouble(multiplication.getRight().accept(this)));
    }

    @Override
    public Double visitParameter(Parameter parameter) {
        return p[parameter.getIndex()];
    }

    @Override
    public Double visitParenthesis(Parenthesis parenthesis) {
        return toDouble(parenthesis.getExpression().accept(this));
    }

    @Override
    public Double visitPower(Power power) {
        return Math.pow(toDouble(power.getLeft().accept(this)), toDouble(power.getRight().accept(this)));
    }

    @Override
    public Long visitRound(Round round) {
        return Math.round(toDouble(round.getExpression().accept(this)));
    }

    @Override
    public Double visitSin(Sin sin) {
        return Math.sin(toDouble(sin.getExpression().accept(this)));
    }

    @Override
    public Double visitSinh(Sinh sinh) {
        return Math.sinh(toDouble(sinh.getExpression().accept(this)));
    }

    @Override
    public Double visitSubtraction(Subtraction subtraction) {
        return (toDouble(subtraction.getLeft().accept(this))) - (toDouble(subtraction.getRight().accept(this)));
    }

    @Override
    public Double visitTan(Tan tan) {
        return Math.tan(toDouble(tan.getExpression().accept(this)));
    }

    @Override
    public Double visitTanh(Tanh tanh) {
        return Math.tanh(toDouble(tanh.getExpression().accept(this)));
    }

    @Override
    public Double visitVariable(Variable variable) {
        return x[variable.getIndex()];
    }

    @Override
    public Double visitNeg(Neg neg) {
        return -toDouble(neg.getExpression().accept(this));
    }

    @Override
    public Double visitPos(Pos pos) {
        return toDouble(pos.getExpression().accept(this));
    }

}
