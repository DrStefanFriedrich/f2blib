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

import org.f2blib.ast.*;

import java.util.Set;
import java.util.TreeSet;

/**
 * Validates a given abstract syntax tree (AST).
 */
public class ValidationVisitor extends AbstractVisitor {

    private final BytecodeNavigatorImpl navi = new BytecodeNavigatorImpl();

    public BytecodeNavigator getBytecodeNavigator() {
        return navi;
    }

    @Override
    public Void visitFunctionDefinition(FunctionDefinition functionDefinition) {

        super.visitFunctionDefinition(functionDefinition);
        navi.finalizeNavigator();
        return null;
    }

    @Override
    public Void visitFunctions(Functions functions) {

        checkFunctionsAndCalcArrayLength(functions.getFunctions());

        functions.getFunctions().stream().forEach(f -> f.accept(this));

        return null;
    }

    /**
     * Checks whether the functions are defined in a row without gaps.
     * Example for a correct function definition: f_1, f_2, f_3; returned
     * will be 3.
     * Example for a wrong function definition: f_1, f_1, f_2; 0 will be
     * returned.
     * Example for a wrong function definition: f_1, f_3; 0 will be returned.
     *
     * @return An array of the correct length.
     */
    private void checkFunctionsAndCalcArrayLength(Set<Function> functions) {

        TreeSet<Integer> indexes = new TreeSet<>();

        for (Function f : functions) {

            int index = f.getIndex();
            if (indexes.contains(index)) {
                throw new RuntimeException("TODO SF");
            }

            indexes.add(index);
        }

        int size = indexes.size();
        int firstIndex = indexes.higher(-1);
        int lastIndex = indexes.lower(size);

        if (firstIndex != 0 || lastIndex != size - 1) {
            throw new RuntimeException("TDOO SF");
        }

        if (size == 0) {
            throw new RuntimeException("TODO SF");
        }

        navi.setLengthResultArray(size);
    }

    @Override
    public Void visitParameter(Parameter parameter) {
        navi.addIndexToParameterIndexes(parameter.getIndex());
        return null;
    }

    @Override
    public Void visitVariable(Variable variable) {
        navi.addIndexToVariableIndexes(variable.getIndex());
        return null;
    }

    // TODO SF Alle durchwandern, nicht leere Implementierung verwenden!

}
