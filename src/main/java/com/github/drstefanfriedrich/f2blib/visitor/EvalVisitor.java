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
import com.github.drstefanfriedrich.f2blib.exception.BytecodeGenerationException;
import org.apache.commons.math3.analysis.function.Acosh;
import org.apache.commons.math3.analysis.function.Asinh;
import org.apache.commons.math3.analysis.function.Atanh;

import static java.lang.String.format;
import static org.apache.commons.math3.util.CombinatoricsUtils.binomialCoefficient;
import static org.apache.commons.math3.util.CombinatoricsUtils.factorial;

/**
 * A {@link Visitor} implementation that evaluates a mathematical expression
 * given as abstract syntax tree by recursively walking through the tree.
 */
public class EvalVisitor extends BaseVisitor {

    private final double[] x;

    private final double[] p;

    private double[] y;

    private static final Asinh ARSINH = new Asinh();

    private static final Acosh ARCOSH = new Acosh();

    private static final Atanh ARTANH = new Atanh();

    /*
     * The current value of the for loop variable.
     */
    private int forVarValue;

    EvalVisitor(double[] x, double[] p, int lengthResultArray) {
        this.x = x;
        this.p = p;
        this.y = new double[lengthResultArray];
    }

    double[] getResult() {
        return y;
    }

    @Override
    public Double visit(Abs abs) {
        return Math.abs((Double) abs.acceptExpression(this));
    }

    @Override
    public Double visit(Addition addition) {
        return ((Double) addition.acceptLeft(this)) + ((Double) addition.acceptRight(this));
    }

    @Override
    public Double visit(Arccos arccos) {
        return Math.acos(arccos.acceptExpression(this));
    }

    @Override
    public Double visit(Arcosh arcosh) {
        return ARCOSH.value((Double) arcosh.acceptExpression(this));
    }

    @Override
    public Double visit(Arcsin arcsin) {
        return Math.asin(arcsin.acceptExpression(this));
    }

    @Override
    public Double visit(Arctan arctan) {
        return Math.atan(arctan.acceptExpression(this));
    }

    @Override
    public Double visit(Arsinh arsinh) {
        return ARSINH.value((Double) arsinh.acceptExpression(this));
    }

    @Override
    public Double visit(Artanh artanh) {
        return ARTANH.value((Double) artanh.acceptExpression(this));
    }

    @Override
    public Double visit(Binomial binomial) {
        return (double) binomialCoefficient(((Double) binomial.acceptN(this)).intValue(), ((Double) binomial.acceptK(this)).intValue());
    }

    @Override
    public Double visit(Faculty faculty) {
        return (double) factorial(((Double) faculty.acceptExpression(this)).intValue());
    }

    @Override
    public Double visit(Constant constant) {
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
    public Double visit(Cos cos) {
        return Math.cos(cos.acceptExpression(this));
    }

    @Override
    public Double visit(Cosh cosh) {
        return Math.cosh(cosh.acceptExpression(this));
    }

    @Override
    public Double visit(Division division) {
        return ((Double) division.acceptLeft(this)) / ((Double) division.acceptRight(this));
    }

    @Override
    public Double visit(Exp exp) {
        return Math.exp(exp.acceptExpression(this));
    }

    @Override
    public Double visit(FunctionDefinition functionDefinition) {
        return functionDefinition.getFunctionBody().accept(this);
    }

    @Override
    public Double visit(FunctionBody functionBody) {

        if (functionBody.isForLoop()) {
            return functionBody.getForLoop().accept(this);
        } else {
            return functionBody.getFunctionsWrapper().accept(this);
        }
    }

    @Override
    public Double visit(FunctionsWrapper functionsWrapper) {

        functionsWrapper.getFunctions().forEach(f -> f.accept(this));

        functionsWrapper.acceptMarkovShift(this);

        return 0d;
    }

    @Override
    public Double visit(ForLoop forLoop) {

        int start = ((Double) forLoop.acceptStart(this)).intValue();
        int end = ((Double) forLoop.acceptEnd(this)).intValue();
        int step = ((Double) forLoop.acceptStep(this)).intValue();

        if (step == 0) {

            if (start != end) {
                throw new BytecodeGenerationException("step evaluating to 0 not allowed");
            }

            forLoop.acceptFunctionsWrapper(this);

        } else if (step > 0) {

            for (int i = start; i <= end; i += step) {
                forVarValue = i;
                forLoop.acceptFunctionsWrapper(this);
            }

        } else {

            step = -step;
            for (int i = start; i >= end; i -= step) {
                forVarValue = i;
                forLoop.acceptFunctionsWrapper(this);
            }
        }

        return 0d;
    }

    @Override
    public Double visit(Function function) {

        int index = function.getIndex();

        y[index] = function.acceptExpression(this);

        return 0d;
    }

    @Override
    public Double visit(Int i) {
        return (double) i.getValue();
    }

    @Override
    public Double visit(Doub doub) {
        return doub.getValue();
    }

    @Override
    public Double visit(Sqrt sqrt) {
        return Math.sqrt(sqrt.acceptExpression(this));
    }

    @Override
    public Double visit(NoOp noOp) {
        throw new IllegalStateException("visit must not be called on the EvalVisitor");
    }

    @Override
    public Double visit(Ln ln) {
        return Math.log(ln.acceptExpression(this));
    }

    @Override
    public Double visit(Multiplication multiplication) {
        return ((Double) multiplication.acceptLeft(this)) * ((Double) multiplication.acceptRight(this));
    }

    @Override
    public Double visit(Parenthesis parenthesis) {
        return parenthesis.acceptExpression(this);
    }

    @Override
    public Double visit(Power power) {
        return Math.pow(power.acceptLeft(this), power.acceptRight(this));
    }

    @Override
    public Double visit(Round round) {
        return (double) Math.round((Double) round.acceptExpression(this));
    }

    @Override
    public Double visit(Sin sin) {
        return Math.sin(sin.acceptExpression(this));
    }

    @Override
    public Double visit(Sinh sinh) {
        return Math.sinh(sinh.acceptExpression(this));
    }

    @Override
    public Double visit(Subtraction subtraction) {
        return ((Double) subtraction.acceptLeft(this)) - ((Double) subtraction.acceptRight(this));
    }

    @Override
    public Double visit(Tan tan) {
        return Math.tan(tan.acceptExpression(this));
    }

    @Override
    public Double visit(Tanh tanh) {
        return Math.tanh(tanh.acceptExpression(this));
    }

    @Override
    public Double visit(Variable variable) {
        if (variable.getIndexExpression() == null) {
            return x[variable.getIndex()];
        } else {
            return x[((Double) variable.getIndexExpression().accept(this)).intValue() - 1];
        }
    }

    @Override
    public Double visit(Parameter parameter) {
        if (parameter.getIndexExpression() == null) {
            return p[parameter.getIndex()];
        } else {
            return p[((Double) parameter.getIndexExpression().accept(this)).intValue() - 1];
        }
    }

    @Override
    public Double visit(Neg neg) {
        return -(Double) neg.acceptExpression(this);
    }

    @Override
    public Double visit(Pos pos) {
        return pos.acceptExpression(this);
    }

    @Override
    public Double visit(ForVar forVar) {
        return (double) forVarValue;
    }

    @Override
    public Double visit(MarkovShift markovShift) {

        int offset = ((Double) markovShift.getOffset().accept(this)).intValue();
        int m = y.length;
        int n = x.length;

        if (offset < 0) {
            throw new IllegalArgumentException("offset must not be negative");
        }
        if (n - offset < m) {
            throw new IllegalArgumentException("x.lenth - offset must be greater or equal than y.length");
        }

        // Move to the right
        for (int i = n - 1; i >= offset + m; i--) {
            x[i] = x[i - m];
        }

        // Copy f into x
        for (int i = offset; i <= offset + m - 1; i++) {
            x[i] = y[i - offset];
        }

        return 0d;
    }

}
