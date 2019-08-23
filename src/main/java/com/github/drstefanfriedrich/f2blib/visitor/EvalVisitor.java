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

import java.util.HashMap;
import java.util.Map;

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
     * Maps each integer variable to the current value
     */
    private final Map<IntVar, Integer> intVariable2Value = new HashMap<>();

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
        Number n = binomial.acceptN(this);
        Number k = binomial.acceptK(this);
        return (double) binomialCoefficient(n.intValue(), k.intValue());
    }

    @Override
    public Double visit(Faculty faculty) {
        Number n = faculty.acceptExpression(this);
        return (double) factorial(n.intValue());
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

        IntVar intVar = new IntVar(forLoop.getVariableName());

        Number startNumber = forLoop.acceptStart(this);
        Number endNumber = forLoop.acceptEnd(this);
        Number stepNumber = forLoop.acceptStep(this);

        int start = startNumber.intValue();
        int end = endNumber.intValue();
        int step = stepNumber.intValue();

        if (step == 0) {

            if (start != end) {
                throw new BytecodeGenerationException("step evaluating to 0 not allowed");
            }

            intVariable2Value.put(intVar, start);
            forLoop.acceptFunctionsWrapper(this);
            intVariable2Value.remove(intVar);

        } else if (step > 0) {

            for (int i = start; i <= end; i += step) {
                intVariable2Value.put(intVar, i);
                forLoop.acceptFunctionsWrapper(this);
            }
            intVariable2Value.remove(intVar);

        } else {

            step = -step;
            for (int i = start; i >= end; i -= step) {
                intVariable2Value.put(intVar, i);
                forLoop.acceptFunctionsWrapper(this);
            }
            intVariable2Value.remove(intVar);
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
            Number index = variable.getIndexExpression().accept(this);
            return x[index.intValue() - 1];
        }
    }

    @Override
    public Double visit(Parameter parameter) {
        if (parameter.getIndexExpression() == null) {
            return p[parameter.getIndex()];
        } else {
            Number index = parameter.getIndexExpression().accept(this);
            return p[index.intValue() - 1];
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
    public Double visit(IntVar intVar) {
        return intVariable2Value.get(intVar).doubleValue();
    }

    @Override
    public Double visit(MarkovShift markovShift) {

        Number offsetNumber = markovShift.getOffset().accept(this);
        int offset = offsetNumber.intValue();
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
        System.arraycopy(y, 0, x, offset, m);

        return 0d;
    }

    @Override
    public Double visit(Sum sum) {

        double s = 0;
        IntVar intVar = new IntVar(sum.getVariableName());

        Number startNumber = sum.acceptStart(this);
        Number endNumber = sum.acceptEnd(this);

        int start = startNumber.intValue();
        int end = endNumber.intValue();

        if (start > end) {
            return s;
        }

        for (int i = start; i <= end; i++) {
            intVariable2Value.put(intVar, i);
            Number res = sum.acceptInner(this);
            s += res.doubleValue();
        }
        intVariable2Value.remove(intVar);

        return s;
    }

    @Override
    public Double visit(Prod prod) {

        double p = 1;
        IntVar intVar = new IntVar(prod.getVariableName());

        Number startNumber = prod.acceptStart(this);
        Number endNumber = prod.acceptEnd(this);

        int start = startNumber.intValue();
        int end = endNumber.intValue();

        if (start > end) {
            return p;
        }

        for (int i = start; i <= end; i++) {
            intVariable2Value.put(intVar, i);
            Number res = prod.acceptInner(this);
            p *= res.doubleValue();
        }
        intVariable2Value.remove(intVar);

        return p;
    }

}
