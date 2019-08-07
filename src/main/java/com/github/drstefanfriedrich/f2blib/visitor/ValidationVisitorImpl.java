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

import java.util.List;
import java.util.TreeSet;

import static java.lang.String.format;

/**
 * Validates a given abstract syntax tree (AST).
 */
public class ValidationVisitorImpl extends BaseVisitor implements ValidationVisitor {

    private final LocalVariablesImpl localVariables = new LocalVariablesImpl();

    private final SpecialFunctionsUsageImpl specialFunctionsUsage = new SpecialFunctionsUsageImpl();

    private String forVariableName;

    @Override
    public LocalVariables getLocalVariables() {
        return localVariables;
    }

    @Override
    public SpecialFunctionsUsage getSpecialFunctionsUsage() {
        return specialFunctionsUsage;
    }

    @Override
    public Void visit(FunctionDefinition functionDefinition) {

        super.visit(functionDefinition);
        localVariables.finalizeLocalVariables();
        return null;
    }

    @Override
    public Void visit(FunctionsWrapper functionsWrapper) {

        checkFunctionsAndCalcArrayLength(functionsWrapper.getFunctions());

        super.visit(functionsWrapper);

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
    public Void visit(Arsinh arsinh) {
        specialFunctionsUsage.setArsinhUsed();
        return super.visit(arsinh);
    }

    @Override
    public Void visit(Arcosh arcosh) {
        specialFunctionsUsage.setArcoshUsed();
        return super.visit(arcosh);
    }

    @Override
    public Void visit(Artanh artanh) {
        specialFunctionsUsage.setArtanhUsed();
        return super.visit(artanh);
    }

    @Override
    public Void visit(ForLoop forLoop) {

        forVariableName = forLoop.getVariableName();
        super.visit(forLoop);
        forVariableName = null;

        return null;
    }

    @Override
    public Void visit(ForVar forVar) {

        String forVarName = forVar.getVariableName();

        if (!forVarName.equals(forVariableName)) {
            throw new BytecodeGenerationException(format("For loop variable %s does not match variable used in body: %s",
                    forVariableName, forVarName));
        }

        return null;
    }

}
