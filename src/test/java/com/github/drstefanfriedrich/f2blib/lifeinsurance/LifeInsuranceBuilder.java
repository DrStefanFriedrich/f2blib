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

/**
 * Builds the array of parameters needed to calculate a life insurance.
 * <p>The policy holder accepts a contract under the following conditions:</p>
 * <ol>
 * <li>At the beginning of the contract the policy holder is
 * ageAtContractBeginning years old.</li>
 * <li>The life insurance contract runs durationOfContract years.</li>
 * <li>Every year the policy holder pays a specified fee.</li>
 * <li>The effective (or net) fee will be calculated by applying an
 * expense factor.</li>
 * <li>The calculations will be done using a specified interest rate.</li>
 * <li>If the policy holder dies before the end of the contract, an amount
 * of deathPremium will be paid to the bereaved.</li>
 * <li>If the policy holder reaches the end of the contract, an amount
 * that is to be calculated, will be paid to the policy holder every year until
 * its death.</li>
 * </ol>
 * <p>The mortality tables were taken from <a href="https://www.math.
 * tugraz.at/~aistleitner/Lehre/WS2011/Finanz_Vers/FinanzVersicherung
 * 20112012/skriptum_finanz_und_versicherungsmathematik.pdf">here</a>.</p>
 */
class LifeInsuranceBuilder {

    private static final double[] MORTALITY_TABLE_MALE = {
            0.0053430, 0.0003452, 0.0002639, 0.0001984, 0.0001512, 0.0001295,
            0.0001226, 0.0001170, 0.0001106, 0.0001114, 0.0001138, 0.0001130,
            0.0001150, 0.0001428, 0.0002217, 0.0003679, 0.0005631, 0.0007744,
            0.0009476, 0.0010207, 0.0010268, 0.0010239, 0.0010166, 0.0010061,
            0.0009997, 0.0009973, 0.0009856, 0.0009627, 0.0009286, 0.0008961,
            0.0008793, 0.0008905, 0.0009343, 0.0009913, 0.0010528, 0.0011164,
            0.0011973, 0.0013159, 0.0014696, 0.0016474, 0.0018400, 0.0020376,
            0.0022378, 0.0024482, 0.0026829, 0.0029503, 0.0032555, 0.0035965,
            0.0039826, 0.0044224, 0.0049170, 0.0054561, 0.0060208, 0.0065971,
            0.0071772, 0.0077607, 0.0083568, 0.0089745, 0.0096238, 0.0103254,
            0.0111153, 0.0120316, 0.0131064, 0.0143678, 0.0158211, 0.0174507,
            0.0192473, 0.0212140, 0.0233625, 0.0257042, 0.0282537, 0.0310362,
            0.0340867, 0.0374562, 0.0412038, 0.0453985, 0.0501132, 0.0554263,
            0.0614277, 0.0682233, 0.0759351, 0.0846979, 0.0946637, 0.1053137,
            0.1161986, 0.1276845, 0.1399040, 0.1531484, 0.1676838, 0.1839279,
            0.2020555, 0.2221826, 0.2440479, 0.2672621, 0.2914275, 0.3162331,
            0.3414812, 0.3670574, 0.3928725, 0.4188563, 1.0, 1.0
    };

    private static final double[] MORTALITY_TABLE_FEMALE = {
            0.0037607, 0.0003266, 0.0002108, 0.0001318, 0.0000997, 0.0000858,
            0.0000851, 0.0000900, 0.0000926, 0.0000934, 0.0000962, 0.0001015,
            0.0001097, 0.0001293, 0.0001669, 0.0002082, 0.0002483, 0.0002988,
            0.0003345, 0.0003380, 0.0003234, 0.0003082, 0.0002973, 0.0002831,
            0.0002698, 0.0002695, 0.0002799, 0.0002906, 0.0003004, 0.0003092,
            0.0003225, 0.0003531, 0.0003966, 0.0004510, 0.0005000, 0.0005394,
            0.0005850, 0.0006533, 0.0007412, 0.0008412, 0.0009502, 0.0010633,
            0.0011789, 0.0012999, 0.0014259, 0.0015650, 0.0017304, 0.0019207,
            0.0021238, 0.0023362, 0.0025657, 0.0028073, 0.0030534, 0.0032958,
            0.0035351, 0.0037768, 0.0040245, 0.0042739, 0.0045302, 0.0048119,
            0.0051423, 0.0055330, 0.0059968, 0.0065535, 0.0072130, 0.0079820,
            0.0088790, 0.0099125, 0.0110837, 0.0124070, 0.0139145, 0.0156392,
            0.0176233, 0.0199112, 0.0225557, 0.0256061, 0.0291105, 0.0331236,
            0.0377085, 0.0429438, 0.0489131, 0.0557097, 0.0634426, 0.0722388,
            0.0822385, 0.0936000, 0.1065040, 0.1204218, 0.1353127, 0.1517418,
            0.1698118, 0.1895022, 0.2108727, 0.2338888, 0.2583034, 0.2836835,
            0.3095368, 0.3357864, 0.3624251, 0.3893823, 1.0, 1.0
    };

    /**
     * Determines the parameter array needed to calculate a life insurance.
     *
     * @param ageAtContractBeginning The age of the policy holder at the beginning of the contract in years.
     * @param durationOfContract     The duration of the life insurance contract in years.
     * @param fee                    The fee/dues paid yearly by the policy holder.
     * @param sex                    The sex of the policy holder. true means male, false means female.
     *                               Unfortunately, we have to be a little bit sexist at this point. As of
     *                               this writing, it seems there exist no mortality tables for all other
     *                               sexes.
     * @param deathPremium           The premium that will be paid to the bereaved in case the policy holder
     *                               dies before the end of the contract.
     * @param interestRate           The interest rate used during the calculation.
     * @param expenseFactor          The net fee f will be calculated as f := (1 - expenseFactor) * F.
     * @return The array of parameters.
     */
    public double[] getParameters(int ageAtContractBeginning, int durationOfContract, double fee, boolean sex,
                                  double deathPremium, double interestRate, double expenseFactor) {
        int endOfContract = ageAtContractBeginning + durationOfContract;

        if (endOfContract >= 100) {
            throw new IllegalArgumentException(String.format("ageAtContractBeginning + durationOfContract must not be" +
                    "greater or equal to 100, but is %d", endOfContract));
        }

        double[] p = new double[107];

        p[0] = durationOfContract;
        p[1] = ageAtContractBeginning;
        p[2] = (1 - expenseFactor) * fee;
        p[3] = deathPremium;
        p[4] = interestRate;

        if (sex) {
            System.arraycopy(MORTALITY_TABLE_MALE, 0, p, 5, 102);
        } else {
            System.arraycopy(MORTALITY_TABLE_FEMALE, 0, p, 5, 102);
        }

        return p;
    }

}
