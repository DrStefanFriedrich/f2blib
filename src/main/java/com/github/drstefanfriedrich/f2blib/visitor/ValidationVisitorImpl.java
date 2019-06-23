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

import com.github.drstefanfriedrich.f2blib.ast.NoOp;
import com.github.drstefanfriedrich.f2blib.ast.*;
import com.github.drstefanfriedrich.f2blib.exception.BytecodeGenerationException;

import java.util.List;
import java.util.TreeSet;

import static java.lang.String.format;

/**
 * Validates a given abstract syntax tree (AST).
 */
public class ValidationVisitorImpl extends AbstractVisitor implements ValidationVisitor {

    private final LocalVariablesImpl localVariables = new LocalVariablesImpl();

    private final SpecialFunctionsUsageImpl specialFunctionsUsage = new SpecialFunctionsUsageImpl();

    @Override
    public LocalVariables getLocalVariables() {
        return localVariables;
    }

    @Override
    public SpecialFunctionsUsage getSpecialFunctionsUsage() {
        return specialFunctionsUsage;
    }

    @Override
    public Void visitFunctionDefinition(FunctionDefinition functionDefinition) {

        super.visitFunctionDefinition(functionDefinition);
        localVariables.finalizeLocalVariables();
        return null;
    }

    @Override
    public Void visitFunctionsWrapper(FunctionsWrapper functionsWrapper) {

        checkFunctionsAndCalcArrayLength(functionsWrapper.getFunctions());

        super.visitFunctionsWrapper(functionsWrapper);

        return null;
    }

    /**
     * Checks whether the functions are defined in a row without gaps.
     * Example for a correct function definition: f_1, f_2, f_3; returned
     * will be 3.
     * Example for a wrong function definition: f_1, f_1, f_2; 0 will be
     * returned.
     * Example for a wrong function definition: f_1, f_3; 0 will be returned.
     */
    private void checkFunctionsAndCalcArrayLength(List<Function> functions) {

        TreeSet<Integer> indexes = new TreeSet<>();

        for (Function f : functions) {

            int index = f.getIndex();
            if (indexes.contains(index)) {
                throw new BytecodeGenerationException(format("Functions must not be defined twice: f_%d already defined", index));
            }

            indexes.add(index);
        }

        int size = indexes.size();
        if (size == 0) {
            throw new BytecodeGenerationException("Empty function definition is not allowed");
        }

        int firstIndex = indexes.higher(-1);
        int lastIndex = indexes.lower(size);

        if (firstIndex != 0 || lastIndex != size - 1) {
            throw new BytecodeGenerationException("Functions must be defined consecutively; gaps are not allowed");
        }
    }

    @Override
    public Void visitParameter(Parameter parameter) {
        localVariables.addIndexToParameterIndexes(parameter.getIndex());
        return null;
    }

    @Override
    public Void visitVariable(Variable variable) {
        localVariables.addIndexToVariableIndexes(variable.getIndex());
        return null;
    }

    @Override
    public Void visitArsinh(Arsinh arsinh) {
        specialFunctionsUsage.setArsinhUsed();
        return super.visitArsinh(arsinh);
    }

    @Override
    public Void visitArcosh(Arcosh arcosh) {
        specialFunctionsUsage.setArcoshUsed();
        return super.visitArcosh(arcosh);
    }

    @Override
    public Void visitArtanh(Artanh artanh) {
        specialFunctionsUsage.setArtanhUsed();
        return super.visitArtanh(artanh);
    }

    @Override
    public Void visitNoOp(NoOp noOp) {
        return null;
    }

}
