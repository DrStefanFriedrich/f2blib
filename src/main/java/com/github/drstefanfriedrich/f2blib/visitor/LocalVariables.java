package com.github.drstefanfriedrich.f2blib.visitor;

import com.github.drstefanfriedrich.f2blib.ast.Parameter;
import com.github.drstefanfriedrich.f2blib.ast.Variable;

import java.util.Iterator;
import java.util.Map;

/**
 * Models the array of local variables as specified by the Java virtual machine
 * specification. It is a simple helper class to ease navigation to variables
 * and parameters, and to calculate the maximum stack frame size and the number
 * of local variables.
 */
public interface LocalVariables {

    int getMaxLocals();

    int getIndexForVariable(Variable variable);

    int getIndexForParameter(Parameter parameter);

    Iterator<Map.Entry<Integer, Integer>> parameterIndexIterator();

    Iterator<Map.Entry<Integer, Integer>> variableIndexIterator();

    int getIndexForForLoopStart();

    int getIndexForForLoopEnd();

    int getIndexForForLoopStep();

}
