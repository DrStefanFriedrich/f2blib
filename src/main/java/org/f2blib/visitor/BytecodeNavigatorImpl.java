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
 * Implementation note on the array of local variables:<p />
 * 0: this
 * 1: p[]
 * 2: x[]
 * 3: y[]
 * 4..n: p_i
 * (n+1)..m: x_i
 */
class BytecodeNavigatorImpl implements BytecodeNavigator {

    private int lengthResultArray;

    private final SortedSet<Integer> parameterIndexes = new TreeSet<>();

    private final SortedSet<Integer> variableIndexes = new TreeSet<>();

    private final SortedMap<Integer, Integer> parameterIndex2LocalVariableIndex = new TreeMap<>();

    private final SortedMap<Integer, Integer> variableIndex2LocalVariableIndex = new TreeMap<>();

    public void setLengthResultArray(int lengthResultArray) {
        this.lengthResultArray = lengthResultArray;
    }

    public void addIndexToParameterIndexes(int index) {
        parameterIndexes.add(index);
    }

    public void addIndexToVariableIndexes(int index) {
        variableIndexes.add(index);
    }

    /**
     * Not needed right now.
     */
    @Override
    public int getLengthResultArray() {
        return lengthResultArray;
    }

    /**
     * Not needed right now.
     */
    @Override
    public int getLengthVariables() {
        return variableIndexes.stream().max(Comparator.naturalOrder()).get() + 1;
    }

    /**
     * Not needed right now.
     */
    @Override
    public int getLengthParameters() {
        return parameterIndexes.stream().max(Comparator.naturalOrder()).get() + 1;
    }

    /**
     * The maximum stack size as needed by {@link MethodVisitor}.visitMaxs.
     */
    @Override
    public int getMaxStack() {
        // Open: how to calculate the stack frame depth.
        return 1000;
        // throw new RuntimeException("TODO SF Not implemented yet");
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
    public void finalizeNavigator() {

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
