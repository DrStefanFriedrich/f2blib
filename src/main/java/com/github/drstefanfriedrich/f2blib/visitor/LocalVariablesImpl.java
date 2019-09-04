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

import com.github.drstefanfriedrich.f2blib.ast.AuxVar;
import com.github.drstefanfriedrich.f2blib.ast.IntVar;
import org.objectweb.asm.MethodVisitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Comparator.naturalOrder;

/**
 * Implementation note on the array of local variables:<p>
 * <code>0: this</code><p>
 * <code>1: p[]</code><p>
 * <code>2: x[]</code><p>
 * <code>3: y[]</code><p>
 * <code>4: for loop end</code><p>
 * <code>5: for loop step</code><p>
 * <code>6: Markov shift offset</code><p>
 * <code>7: Markov shift M</code><p>
 * <code>8: Markov shift N</code><p>
 * <code>9: Markov shift start</code><p>
 * <code>10: Markov shift end</code><p>
 * <code>11: sum variable</code><p>
 * <code>13: prod variable</code><p>
 * <code>14, ...: auxiliary variables</code>
 */
public class LocalVariablesImpl implements LocalVariables {

    private int indexForLoopEnd;

    private int indexForLoopStep;

    private int markovShiftOffset;

    private int markovShiftM;

    private int markovShiftN;

    private int markovShiftStart;

    private int markovShiftEnd;

    private int sumIndex;

    private int prodIndex;

    private final Map<IntVar, Integer> intVariable2Index = new HashMap<>();

    private final Set<AuxVar> tmpAuxVars = new HashSet<>();

    private final Map<AuxVar, Integer> auxVar2Index = new HashMap<>();

    /**
     * The maximum number of local variables as needed by {@link MethodVisitor}.visitMaxs.
     */
    @Override
    public int getMaxLocals() {
        /*
         * 4: this, x[], y[], p[]
         * 2: for loop: end, step
         * 5: Markov shift
         * 4: sum, prod (double)
         */
        return 4 + 2 + 5 + 2 * 2 + intVariable2Index.size() + 2 * auxVar2Index.size();
    }

    /**
     * Do all the calculations to finalize object creation.
     * Long and double must be counted as two variables in the array of local variables,
     * according to the Java virtual machine specification.
     */
    void finalizeLocalVariables() {

        int localVariableIndex = 4;

        indexForLoopEnd = localVariableIndex++;
        indexForLoopStep = localVariableIndex++;

        markovShiftOffset = localVariableIndex++;
        markovShiftM = localVariableIndex++;
        markovShiftN = localVariableIndex++;
        markovShiftStart = localVariableIndex++;
        markovShiftEnd = localVariableIndex++;
        sumIndex = localVariableIndex++;
        localVariableIndex++;
        prodIndex = localVariableIndex++;
        localVariableIndex++;

        for (AuxVar av : tmpAuxVars) {
            auxVar2Index.put(av, localVariableIndex++);
            localVariableIndex++;
        }
    }

    @Override
    public int getIndexForIntVar(IntVar intVar) {
        return intVariable2Index.get(intVar);
    }

    public void calculateNewIndexForIntVar(String variableName) {

        IntVar intVar = new IntVar(variableName);
        if (intVariable2Index.containsKey(intVar)) {
            return;
        }

        int nextIndex = intVariable2Index.values().stream().max(naturalOrder()).orElse(14) + 1;

        intVariable2Index.put(intVar, nextIndex);
    }

    @Override
    public int getIndexForForLoopEnd() {
        return indexForLoopEnd;
    }

    @Override
    public int getIndexForForLoopStep() {
        return indexForLoopStep;
    }

    @Override
    public int getMarkovShiftOffset() {
        return markovShiftOffset;
    }

    @Override
    public int getMarkovShiftM() {
        return markovShiftM;
    }

    @Override
    public int getMarkovShiftN() {
        return markovShiftN;
    }

    @Override
    public int getMarkovShiftStart() {
        return markovShiftStart;
    }

    @Override
    public int getMarkovShiftEnd() {
        return markovShiftEnd;
    }

    @Override
    public int getSumIndex() {
        return sumIndex;
    }

    @Override
    public int getProdIndex() {
        return prodIndex;
    }

    @Override
    public int getIndexForAuxVar(AuxVar auxVar) {
        return auxVar2Index.get(auxVar);
    }

    public void addAuxVariable(AuxVar auxVar) {
        tmpAuxVars.add(auxVar);
    }

}
