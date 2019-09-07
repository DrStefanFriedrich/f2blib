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
 * <code>11: IntVar</code><p>
 * <code>12: summation for the IntVar</code><p>
 * <code>14: multiplication for the IntVar</code>
 * <code>16: ...</code>
 * <code>...: AuxVar's</code><p>
 */
public class LocalVariablesImpl implements LocalVariables {

    private static final int INDEX_FOR_LOOP_END = 4;

    private static final int INDEX_FOR_LOOP_STEP = 5;

    private static final int MARKOV_SHIFT_OFFSET = 6;

    private static final int MARKOV_SHIFT_M = 7;

    private static final int MARKOV_SHIFT_N = 8;

    private static final int MARKOV_SHIFT_START = 9;

    private static final int MARKOV_SHIFT_END = 10;

    private final Set<IntVar> tmpIntVars = new HashSet<>();
    private final Map<IntVar, Integer> intVariable2Index = new HashMap<>();
    private final Map<IntVar, Integer> intVariable2SumIndex = new HashMap<>();
    private final Map<IntVar, Integer> intVariable2ProdIndex = new HashMap<>();

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
         */
        return 4 + 2 + 5 + intVariable2Index.size() + 2 * auxVar2Index.size() +
                2 * intVariable2SumIndex.size() + 2 * intVariable2ProdIndex.size();
    }

    /**
     * Do all the calculations to finalize object creation.
     * Long and double must be counted as two variables in the array of local variables,
     * according to the Java virtual machine specification.
     */
    void finalizeLocalVariables() {

        int nextLocalVariableIndex = 11;

        for (IntVar iv : tmpIntVars) {
            intVariable2Index.put(iv, nextLocalVariableIndex++);
            intVariable2SumIndex.put(iv, nextLocalVariableIndex++);
            nextLocalVariableIndex++;
            intVariable2ProdIndex.put(iv, nextLocalVariableIndex++);
            nextLocalVariableIndex++;
        }

        for (AuxVar av : tmpAuxVars) {
            auxVar2Index.put(av, nextLocalVariableIndex++);
            nextLocalVariableIndex++;
        }
    }

    @Override
    public int getIndexForIntVar(IntVar intVar) {
        return intVariable2Index.get(intVar);
    }

    public void addIntVar(String variableName) {
        IntVar intVar = new IntVar(variableName);
        tmpIntVars.add(intVar);
    }

    @Override
    public int getIndexForForLoopEnd() {
        return INDEX_FOR_LOOP_END;
    }

    @Override
    public int getIndexForForLoopStep() {
        return INDEX_FOR_LOOP_STEP;
    }

    @Override
    public int getMarkovShiftOffset() {
        return MARKOV_SHIFT_OFFSET;
    }

    @Override
    public int getMarkovShiftM() {
        return MARKOV_SHIFT_M;
    }

    @Override
    public int getMarkovShiftN() {
        return MARKOV_SHIFT_N;
    }

    @Override
    public int getMarkovShiftStart() {
        return MARKOV_SHIFT_START;
    }

    @Override
    public int getMarkovShiftEnd() {
        return MARKOV_SHIFT_END;
    }

    @Override
    public int getSumIndex(IntVar intVar) {
        return intVariable2SumIndex.get(intVar);
    }

    @Override
    public int getProdIndex(IntVar intVar) {
        return intVariable2ProdIndex.get(intVar);
    }

    @Override
    public int getIndexForAuxVar(AuxVar auxVar) {
        return auxVar2Index.get(auxVar);
    }

    public void addAuxVariable(AuxVar auxVar) {
        tmpAuxVars.add(auxVar);
    }

}
