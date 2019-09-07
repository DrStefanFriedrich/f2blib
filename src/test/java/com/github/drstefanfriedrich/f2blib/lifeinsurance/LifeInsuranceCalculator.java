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
import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;

import java.io.IOException;
import java.io.InputStream;

import static java.lang.String.format;

/**
 * This class calculates very simple life insurances.
 */
public class LifeInsuranceCalculator {

    private static final String LIFE_INSURANCE_FORMULA = "com.github.drstefanfriedrich.f2blib.lifeinsurance.LifeInsuranceFormula";
    private final FunctionEvaluationKernel kernel;
    private final LifeInsuranceBuilder lib = new LifeInsuranceBuilder();

    public LifeInsuranceCalculator(FunctionEvaluationKernel kernel) {
        this.kernel = kernel;
        kernel.load(readLifeInsuranceFormula());
    }

    private String readLifeInsuranceFormula() {
        try {
            return new ByteSource() {
                @Override
                public InputStream openStream() {
                    return getClass().getResourceAsStream("/com/github/drstefanfriedrich/f2blib/lifeinsurance/" +
                            "LifeInsuranceFormula.f");
                }
            }.asCharSource(Charsets.UTF_8).read();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Calculates a life insurance with the given parameters. The method is thread-safe.
     */
    public double calculate(int ageAtContractBeginning, int durationOfContract, double fee, double deathPremium,
                            boolean sex, double interestRate, double expenseFactor) {

        double x[] = new double[0];
        double p[] = lib.getParameters(ageAtContractBeginning, durationOfContract, fee, sex, deathPremium,
                interestRate, expenseFactor);
        double y[] = new double[1];

        kernel.eval(LIFE_INSURANCE_FORMULA, p, x, y);

        return y[0];
    }

    public String prettyPrint() {
        return kernel.print(LIFE_INSURANCE_FORMULA);
    }

}
