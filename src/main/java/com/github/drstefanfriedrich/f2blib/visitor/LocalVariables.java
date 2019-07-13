package com.github.drstefanfriedrich.f2blib.visitor;

/**
 * Models the array of local variables as specified by the Java virtual machine
 * specification. It is a simple helper class to ease navigation to variables
 * and parameters, and to calculate the maximum stack frame size and the number
 * of local variables.
 */
public interface LocalVariables {

    int getMaxLocals();

    int getIndexForForLoopStart();

    int getIndexForForLoopEnd();

    int getIndexForForLoopStep();

    int getMarkovShiftOffset();

    int getMarkovShiftM();

    int getMarkovShiftN();

    int getMarkovShiftStart();

    int getMarkovShiftEnd();

}
