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
import org.junit.Test;

import static com.github.drstefanfriedrich.f2blib.util.TestUtil.closeTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Some test cases for life insurances.
 */
public abstract class AbstractLifeInsuranceTest {

    protected abstract LifeInsuranceCalculator getLifeInsuranceCalculator();

    @Test
    public void someLifeInsuranceTests() {
        double annuity;
        LifeInsuranceCalculator lifeInsuranceCalculator = getLifeInsuranceCalculator();

        assertThat(lifeInsuranceCalculator.prettyPrint(), is("" +
                "function com.github.drstefanfriedrich.f2blib.lifeinsurance.LifeInsuranceFormula;\n" +
                "begin\n" +
                "    V := 1 / (1 + p_5);\n" +
                "    A := sum_{k = round p_1}^{101 - round p_2}((V ^ (round p_1) - V ^ ((k + 1))) * (p_{6 + round p_2 + k} * prod_{l = round p_2}^{round p_2 + k - 1}(1 - p_{6 + l})));\n" +
                "    B := sum_{k = 0}^{round p_1 - 1}((1 - V ^ ((k + 1))) * (p_{6 + round p_2 + k} * prod_{l = round p_2}^{round p_2 + k - 1}(1 - p_{6 + l})));\n" +
                "    C := (1 - V ^ (round p_1)) * sum_{k = round p_1}^{101 - round p_2}((p_{6 + round p_2 + k} * prod_{l = round p_2}^{round p_2 + k - 1}(1 - p_{6 + l})));\n" +
                "    D := (1 - V) * sum_{k = 0}^{round p_1 - 1}(V ^ ((k + 1)) * (p_{6 + round p_2 + k} * prod_{l = round p_2}^{round p_2 + k - 1}(1 - p_{6 + l})));\n" +
                "    f_1 := 1 / A * ((B + C) * p_3 - D * p_4);\n" +
                "end\n"));

        annuity = lifeInsuranceCalculator.calculate(25, 40, 10000, 25000, true, 0.025, 0.075);
        assertThat(annuity, closeTo(55529.48235792289));

        // Females live a little bit longer, so under the same conditions they get a little bit less
        annuity = lifeInsuranceCalculator.calculate(25, 40, 10000, 25000, false, 0.025, 0.075);
        assertThat(annuity, closeTo(43876.233869921605));

        // If we start earlier and pay longer, we get more
        annuity = lifeInsuranceCalculator.calculate(20, 45, 10000, 25000, true, 0.025, 0.075);
        assertThat(annuity, closeTo(67673.26356315358));

        // If we retire earlier, we get less
        annuity = lifeInsuranceCalculator.calculate(25, 35, 10000, 25000, true, 0.025, 0.075);
        assertThat(annuity, closeTo(36374.287262406964));

        // Higher death premium leads to less annuity
        annuity = lifeInsuranceCalculator.calculate(25, 40, 10000, 50000, true, 0.025, 0.075);
        assertThat(annuity, closeTo(55013.47388326927));

        // Higher interest rate leads to higher annuity
        annuity = lifeInsuranceCalculator.calculate(25, 40, 10000, 25000, true, 0.035, 0.075);
        assertThat(annuity, closeTo(76652.63693380302));

        // Higher yearly fee leads to higher annuity
        annuity = lifeInsuranceCalculator.calculate(25, 40, 12000, 25000, true, 0.025, 0.075);
        assertThat(annuity, closeTo(66738.5805244382));

        // Lower expenses lead to highter annuity
        annuity = lifeInsuranceCalculator.calculate(25, 40, 10000, 25000, true, 0.025, 0.055);
        assertThat(annuity, closeTo(56741.276754302926));

        // Higher expenses lead to lower annuity
        annuity = lifeInsuranceCalculator.calculate(25, 40, 10000, 25000, true, 0.025, 0.095);
        assertThat(annuity, closeTo(54317.68796154286));

        // One last example
        annuity = lifeInsuranceCalculator.calculate(30, 30, 12000, 30000, true, 0.015, 0.08);
        assertThat(annuity, closeTo(26262.440967765837));
    }

}
