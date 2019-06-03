package org.f2blib.visitor;

import org.f2blib.ast.Parameter;
import org.f2blib.ast.Variable;

import java.util.Iterator;
import java.util.Map;

/**
 * A simple helper class to ease navigation to variables, parameters, and to
 * calculate the maximum stack frame size and the number of local variables.
 */
public interface BytecodeNavigator {

    int getLengthResultArray();

    int getLengthVariables();

    int getLengthParameters();

    int getMaxStack();

    int getMaxLocals();

    int getIndexForVariable(Variable variable);

    int getIndexForParameter(Parameter parameter);

    Iterator<Map.Entry<Integer, Integer>> parameterIndexIterator();

    Iterator<Map.Entry<Integer, Integer>> variableIndexIterator();

}
