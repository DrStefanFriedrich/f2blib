#
#    Copyright (c) 2019 Stefan Friedrich
#
#    This program and the accompanying materials are made available under the
#    terms of the Eclipse Public License 2.0 which is available at
#    https://www.eclipse.org/legal/epl-2.0/
#
#    SPDX-License-Identifier: EPL-2.0
#

function com.github.drstefanfriedrich.f2blib.lifeinsurance.LifeInsuranceFormula;
begin

    # n = durationOfContract = round(p_1)
    # i = interestRate = p_5
    # v = 1 / (1 + i)
    # x = ageAtContractBeginning = round(p_2)
    # fee = p_3
    # deathPremium = p_4
    # q_l = p_{6 + l}

    V := 1 / (1 + p_5);

    # E can't be calculated and must be substituted
    # For ease of identifying E, we use ((E))
    # E := p_{6 + round(p_2) + k} * prod(1 - p_{6 + l}, l, round(p_2), round(p_2) + k - 1);

    A := sum((V^round(p_1) - V^(k+1)) * ((
                                            p_{6 + round(p_2) + k} * prod(1 - p_{6 + l}, l, round(p_2), round(p_2) + k - 1)
                                        )) , k, round(p_1), 101 - round(p_2));

    B := sum((1-V^(k+1)) * ((
                               p_{6 + round(p_2) + k} * prod(1 - p_{6 + l}, l, round(p_2), round(p_2) + k - 1)
                           )), k, 0, round(p_1) - 1);

    C := (1 - V^round(p_1)) * sum(((
                                      p_{6 + round(p_2) + k} * prod(1 - p_{6 + l}, l, round(p_2), round(p_2) + k - 1)
                                  )), k, round(p_1), 101 - round(p_2));

    D := (1 - V) * sum(V^(k+1) * ((
                                     p_{6 + round(p_2) + k} * prod(1 - p_{6 + l}, l, round(p_2), round(p_2) + k - 1)
                                 )), k, 0, round(p_1) - 1);

    f_1 := 1 / A * ( (B + C) * p_3 - D * p_4 );

end
