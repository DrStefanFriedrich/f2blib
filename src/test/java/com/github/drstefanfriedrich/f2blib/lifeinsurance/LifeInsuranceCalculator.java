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

import com.github.drstefanfriedrich.f2blib.FunctionEvaluationFactory;
import com.github.drstefanfriedrich.f2blib.FunctionEvaluationKernel;
import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class calculates very simple life insurances.
 */
public class LifeInsuranceCalculator {

    private final FunctionEvaluationKernel kernel = new FunctionEvaluationFactory().get().create();

    public LifeInsuranceCalculator() {
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
                            boolean sex, double interestRate) {

        LifeInsuranceBuilder lib = new LifeInsuranceBuilder();

        lib.setAgeAtContractBeginning(ageAtContractBeginning);
        lib.setDurationOfContract(durationOfContract);
        lib.setFee(fee);
        lib.setDeathPremium(deathPremium);
        lib.setSex(sex);
        lib.setInterestRate(interestRate);

        double x[] = new double[1];
        double p[] = lib.getParameters();
        double y[] = new double[1];

        kernel.eval("com.github.drstefanfriedrich.f2blib.lifeinsurance.LifeInsuranceFormula", p, x, y);

        return y[0];
    }

}
