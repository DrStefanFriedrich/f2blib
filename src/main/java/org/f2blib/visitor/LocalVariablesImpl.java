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

import org.f2blib.ast.Parameter;
import org.f2blib.ast.Variable;
import org.objectweb.asm.MethodVisitor;

import java.util.*;

/**
 * Implementation note on the array of local variables:<p>
 * <code>0: this</code><p>
 * <code>1: p[]</code><p>
 * <code>2: x[]</code><p>
 * <code>3: y[]</code><p>
 * <code>4..n: p_i</code><p>
 * <code>(n+1)..m: x_i</code>
 */
public class LocalVariablesImpl implements LocalVariables {

    private final SortedSet<Integer> parameterIndexes = new TreeSet<>();

    private final SortedSet<Integer> variableIndexes = new TreeSet<>();

    private final SortedMap<Integer, Integer> parameterIndex2LocalVariableIndex = new TreeMap<>();

    private final SortedMap<Integer, Integer> variableIndex2LocalVariableIndex = new TreeMap<>();

    void addIndexToParameterIndexes(int index) {
        parameterIndexes.add(index);
    }

    void addIndexToVariableIndexes(int index) {
        variableIndexes.add(index);
    }

    /**
     * The maximum stack size as needed by {@link MethodVisitor}.visitMaxs.
     */
    @Override
    public int getMaxStack() {
        // Open: how to calculate the stack frame depth.
        return 1000;
    }

    /**
     * The maximum number of local variables as needed by {@link MethodVisitor}.visitMaxs.
     */
    @Override
    public int getMaxLocals() {
        return 4 + 2 * parameterIndexes.size() + 2 * variableIndexes.size();
    }

    /**
     * The index in the array of local variables as needed by JVM opcodes for the given
     * variable.
     */
    @Override
    public int getIndexForVariable(Variable variable) {
        return variableIndex2LocalVariableIndex.get(variable.getIndex());
    }

    /**
     * The index in the array of local variables as needed by JVM opcodes for the given
     * parameter.
     */
    @Override
    public int getIndexForParameter(Parameter parameter) {
        return parameterIndex2LocalVariableIndex.get(parameter.getIndex());
    }

    /**
     * Do all the calculations to finalize object creation.
     * Long and double must be counted as two variables in the array of local variables.
     */
    void finalizeLocalVariables() {

        if (!parameterIndexes.isEmpty()) {
            for (int i = 0; i <= parameterIndexes.last(); i++) {
                parameterIndexes.add(i);
            }
        }

        if (!variableIndexes.isEmpty()) {
            for (int i = 0; i <= variableIndexes.last(); i++) {
                variableIndexes.add(i);
            }
        }

        int localVariableIndex = 4;

        for (Integer i : parameterIndexes) {
            parameterIndex2LocalVariableIndex.put(i, localVariableIndex);
            localVariableIndex++;
            localVariableIndex++;
        }

        for (Integer i : variableIndexes) {
            variableIndex2LocalVariableIndex.put(i, localVariableIndex);
            localVariableIndex++;
            localVariableIndex++;
        }
    }

    @Override
    public Iterator<Map.Entry<Integer, Integer>> parameterIndexIterator() {
        return parameterIndex2LocalVariableIndex.entrySet().iterator();
    }

    @Override
    public Iterator<Map.Entry<Integer, Integer>> variableIndexIterator() {
        return variableIndex2LocalVariableIndex.entrySet().iterator();
    }

}
