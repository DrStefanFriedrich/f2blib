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

import org.objectweb.asm.MethodVisitor;

/**
 * Implementation note on the array of local variables:<p>
 * <code>0: this</code><p>
 * <code>1: p[]</code><p>
 * <code>2: x[]</code><p>
 * <code>3: y[]</code><p>
 * <code>4: for loop start</code><p>
 * <code>5: for loop end</code><p>
 * <code>6: for loop step</code><p>
 * <code>7: Markov shift offset</code><p>
 * <code>8: Markov shift M</code><p>
 * <code>9: Markov shift N</code><p>
 * <code>10: Markov shift start</code><p>
 * <code>11: Markov shift end</code><p>
 */
public class LocalVariablesImpl implements LocalVariables {

    private int indexForLoopStart;

    private int indexForLoopEnd;

    private int indexForLoopStep;

    private int markovShiftOffset;

    private int markovShiftM;

    private int markovShiftN;

    private int markovShiftStart;

    private int markovShiftEnd;

    /**
     * The maximum number of local variables as needed by {@link MethodVisitor}.visitMaxs.
     */
    @Override
    public int getMaxLocals() {
        /*
         * 4: this, x[], y[], p[]
         * 3: for loop: start, end, step
         * 5: Markov shift
         */
        return 4 + 3 + 5;
    }

    /**
     * Do all the calculations to finalize object creation.
     * Long and double must be counted as two variables in the array of local variables,
     * according to the Java virtual machine specification.
     */
    void finalizeLocalVariables() {

        int localVariableIndex = 4;

        indexForLoopStart = localVariableIndex++;
        indexForLoopEnd = localVariableIndex++;
        indexForLoopStep = localVariableIndex++;

        markovShiftOffset = localVariableIndex++;
        markovShiftM = localVariableIndex++;
        markovShiftN = localVariableIndex++;
        markovShiftStart = localVariableIndex++;
        markovShiftEnd = localVariableIndex; // add ++ if you want to continue here
    }

    @Override
    public int getIndexForForLoopStart() {
        return indexForLoopStart;
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

}
