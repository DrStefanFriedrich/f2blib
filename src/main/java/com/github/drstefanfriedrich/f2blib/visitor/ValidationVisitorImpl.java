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

import java.util.*;

import static java.lang.String.format;
import static java.util.Comparator.naturalOrder;

/**
 * Validates a given abstract syntax tree (AST).
 */
public class ValidationVisitorImpl extends BaseVisitor implements ValidationVisitor {

    private static final Set<String> ALLOWED_INT_VAR_NAMES = new HashSet<>();
    private static final String VAR_NOT_ALLOWED = "The variable name '%s' is not allowed.";

    static {
        ALLOWED_INT_VAR_NAMES.add("a");
        ALLOWED_INT_VAR_NAMES.add("b");
        ALLOWED_INT_VAR_NAMES.add("c");
        ALLOWED_INT_VAR_NAMES.add("d");
        ALLOWED_INT_VAR_NAMES.add("e");
        // 'f' is reserved for functions
        ALLOWED_INT_VAR_NAMES.add("g");
        ALLOWED_INT_VAR_NAMES.add("h");
        ALLOWED_INT_VAR_NAMES.add("i");
        ALLOWED_INT_VAR_NAMES.add("j");
        ALLOWED_INT_VAR_NAMES.add("k");
        ALLOWED_INT_VAR_NAMES.add("l");
        ALLOWED_INT_VAR_NAMES.add("m");
        ALLOWED_INT_VAR_NAMES.add("n");
        ALLOWED_INT_VAR_NAMES.add("o");
        // 'p' is reserved for parameter names
        ALLOWED_INT_VAR_NAMES.add("q");
        ALLOWED_INT_VAR_NAMES.add("r");
        ALLOWED_INT_VAR_NAMES.add("s");
        ALLOWED_INT_VAR_NAMES.add("t");
        ALLOWED_INT_VAR_NAMES.add("u");
        ALLOWED_INT_VAR_NAMES.add("v");
        ALLOWED_INT_VAR_NAMES.add("w");
        // 'x' is reserved for variable names
        ALLOWED_INT_VAR_NAMES.add("y");
        ALLOWED_INT_VAR_NAMES.add("z");
    }

    private final LocalVariablesImpl localVariables = new LocalVariablesImpl();
    private final SpecialFunctionsUsageImpl specialFunctionsUsage = new SpecialFunctionsUsageImpl();
    private final SortedSet<Integer> tmpParameterIndexes = new TreeSet<>();
    private final SortedSet<Integer> tmpVariableIndexes = new TreeSet<>();
    private boolean atLeastOneParameterIsIntExpression = false;
    private boolean atLeastOneVariableIsIntExpression = false;
    private int sizeY;

    /*
     * The integer variables that are currently (i.e. at the moment of execution) in scope.
     */
    private final Set<String> intVariablesInScope = new HashSet<>();

    private void checkIntVariableName(String variableName) {
        if (!ALLOWED_INT_VAR_NAMES.contains(variableName)) {
            throw new BytecodeGenerationException(format(VAR_NOT_ALLOWED, variableName));
        }
    }

    private void checkAuxVariableName(String variableName) {
        if (variableName.length() != 1) {
            throw new BytecodeGenerationException(format(VAR_NOT_ALLOWED, variableName));
        }
        if (Character.isLowerCase(variableName.charAt(0))) {
            throw new BytecodeGenerationException(format(VAR_NOT_ALLOWED, variableName));
        }
    }

    private void putIntVariableInScope(String variableName) {
        if (intVariablesInScope.contains(variableName)) {
            throw new BytecodeGenerationException(format("The variable '%s' is already in use in an outer scope", variableName));
        }
        intVariablesInScope.add(variableName);
        localVariables.addIntVar(variableName);
    }

    private void removeIntVariableFromScope(String variableName) {
        intVariablesInScope.remove(variableName);
    }

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

        sizeY = size;
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

        String variableName = forLoop.getVariableName();
        checkIntVariableName(variableName);

        forLoop.acceptStart(this);
        forLoop.acceptEnd(this);
        forLoop.acceptStep(this);

        putIntVariableInScope(variableName);
        forLoop.acceptFunctionsWrapper(this);
        removeIntVariableFromScope(variableName);

        return null;
    }

    @Override
    public Void visit(IntVar intVar) {

        String variableName = intVar.getVariableName();

        if (!intVariablesInScope.contains(variableName)) {
            throw new BytecodeGenerationException(format("The variable '%s' is not defined", variableName));
        }

        return null;
    }

    @Override
    public Void visit(AuxVar auxVar) {

        String variableName = auxVar.getVariableName();
        checkAuxVariableName(variableName);

        // Formerly, we had a check here to avoid the use of auxiliary variables on the right hand side of another
        // auxiliary variable. We think this is not necessary. Care must be taken to avoid recursive evaluation.
        // Also, auxiliary variables will be evaluated in order of occurrence in the function definition.

        localVariables.addAuxVariable(auxVar);

        return null;
    }

    @Override
    public Void visit(Sum sum) {

        String variableName = sum.getVariableName();
        checkIntVariableName(variableName);

        sum.acceptStart(this);
        sum.acceptEnd(this);

        putIntVariableInScope(variableName);
        sum.acceptInner(this);
        removeIntVariableFromScope(variableName);

        return null;
    }

    @Override
    public Void visit(Prod prod) {

        String variableName = prod.getVariableName();
        checkIntVariableName(variableName);

        prod.acceptStart(this);
        prod.acceptEnd(this);

        putIntVariableInScope(variableName);
        prod.acceptInner(this);
        removeIntVariableFromScope(variableName);

        return null;
    }

    @Override
    public Void visit(AuxiliaryVariable auxiliaryVariable) {

        auxiliaryVariable.acceptAuxVar(this);
        auxiliaryVariable.acceptInner(this);

        return null;
    }

    @Override
    public Void visit(Parameter parameter) {
        if (parameter.getIndexExpression() == null) {
            tmpParameterIndexes.add(parameter.getIndex());
        } else {
            atLeastOneParameterIsIntExpression = true;
        }
        return null;
    }

    @Override
    public <T> T visit(Variable variable) {
        if (variable.getIndexExpression() == null) {
            tmpVariableIndexes.add(variable.getIndex());
        } else {
            atLeastOneVariableIsIntExpression = true;
        }
        return null;
    }

    @Override
    public FunctionEvaluationValidator getFunctionEvaluationValidator() {

        int sizeX = tmpVariableIndexes.stream().max(naturalOrder()).orElse(-1);
        int sizeP = tmpParameterIndexes.stream().max(naturalOrder()).orElse(-1);

        return new FunctionEvaluationValidatorImpl(sizeP, sizeX, sizeY, atLeastOneParameterIsIntExpression,
                atLeastOneVariableIsIntExpression);
    }

}
