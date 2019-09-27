package com.github.drstefanfriedrich.f2blib.visitor;

/**
 * Validate a {@link com.github.drstefanfriedrich.f2blib.impl.FunctionEvaluation} invocation.
 */
public interface FunctionEvaluationValidator {

    /**
     * Validate the given function evaluation.
     *
     * @param p The parameters of the function. <code>p in IR^k</code>
     * @param x The variable of the function. <code>x in IR^n</code>
     * @param y The result of the function evaluation. <code>y in IR^m</code>
     * @throws IllegalArgumentException If the function invocation is illegal.
     */
    void validate(double[] p, double[] x, double[] y);

}
