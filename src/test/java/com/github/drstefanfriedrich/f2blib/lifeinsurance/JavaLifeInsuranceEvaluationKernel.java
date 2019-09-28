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

package com.github.drstefanfriedrich.f2blib.lifeinsurance;

import com.github.drstefanfriedrich.f2blib.FunctionEvaluationKernel;

import java.util.Set;

/**
 * Implementation of the life insurance example in pure Java code to compare
 * the bytecode generated by the Java compiler with the bytecode generated by
 * F2BLib.
 */
class JavaLifeInsuranceEvaluationKernel implements FunctionEvaluationKernel {

    @Override
    public void load(String functionDefinition) {
    }

    @Override
    public void eval(String functionName, double[] p, double[] x, double[] y) {
        double sum, prod;

        double V = 1 / (1 + p[4]);

        sum = 0;
        for (int k = (int) Math.round(p[0]); k <= 101 - Math.round(p[1]); k++) {
            prod = 1;
            for (int l = (int) Math.round(p[1]); l <= Math.round(p[1]) + k - 1; l++) {
                prod *= 1 - p[5 + l];
            }
            sum += (Math.pow(V, Math.round(p[0])) - Math.pow(V, (k + 1))) * p[5 + (int) Math.round(p[1]) + k] * prod;
        }
        double A = sum;

        sum = 0;
        for (int k = 0; k <= Math.round(p[0]) - 1; k++) {
            prod = 1;
            for (int l = (int) Math.round(p[1]); l <= Math.round(p[1]) + k - 1; l++) {
                prod *= 1 - p[5 + l];
            }
            sum += (1 - Math.pow(V, (k + 1))) * p[5 + (int) Math.round(p[1]) + k] * prod;
        }
        double B = sum;

        sum = 0;
        for (int k = (int) Math.round(p[0]); k <= 101 - Math.round(p[1]); k++) {
            prod = 1;
            for (int l = (int) Math.round(p[1]); l <= Math.round(p[1]) + k - 1; l++) {
                prod *= 1 - p[5 + l];
            }
            sum += p[5 + (int) Math.round(p[1]) + k] * prod;
        }
        double C = (1 - Math.pow(V, Math.round(p[0]))) * sum;

        sum = 0;
        for (int k = 0; k <= Math.round(p[0]) - 1; k++) {
            prod = 1;
            for (int l = (int) Math.round(p[1]); l <= Math.round(p[1]) + k - 1; l++) {
                prod *= 1 - p[5 + l];
            }
            sum += Math.pow(V, k + 1) * p[5 + (int) Math.round(p[1]) + k] * prod;
        }
        double D = (1 - V) * sum;

        y[0] = 1 / A * ((B + C) * p[2] - D * p[3]);
    }

    @Override
    public boolean remove(String functionName) {
        return false;
    }

    @Override
    public Set<String> list() {
        return null;
    }

    // To make the unit test happy
    @Override
    public String print(String functionName) {
        return "function com.github.drstefanfriedrich.f2blib.lifeinsurance.LifeInsuranceFormula;\n" +
                "begin\n" +
                "    V := 1 / (1 + p_5);\n" +
                "    A := sum_{k = round p_1}^{101 - round p_2}((V ^ (round p_1) - V ^ ((k + 1))) * (p_{6 + round p_2 + k} * prod_{l = round p_2}^{round p_2 + k - 1}(1 - p_{6 + l})));\n" +
                "    B := sum_{k = 0}^{round p_1 - 1}((1 - V ^ ((k + 1))) * (p_{6 + round p_2 + k} * prod_{l = round p_2}^{round p_2 + k - 1}(1 - p_{6 + l})));\n" +
                "    C := (1 - V ^ (round p_1)) * sum_{k = round p_1}^{101 - round p_2}((p_{6 + round p_2 + k} * prod_{l = round p_2}^{round p_2 + k - 1}(1 - p_{6 + l})));\n" +
                "    D := (1 - V) * sum_{k = 0}^{round p_1 - 1}(V ^ ((k + 1)) * (p_{6 + round p_2 + k} * prod_{l = round p_2}^{round p_2 + k - 1}(1 - p_{6 + l})));\n" +
                "    f_1 := 1 / A * ((B + C) * p_3 - D * p_4);\n" +
                "end\n";
    }

}