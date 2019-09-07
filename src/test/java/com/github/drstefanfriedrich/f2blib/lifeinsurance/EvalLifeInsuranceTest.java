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

public class EvalLifeInsuranceTest extends AbstractLifeInsuranceTest {

    private final LifeInsuranceCalculator lifeInsuranceCalculator = new LifeInsuranceCalculator(
            new FunctionEvaluationFactory().get("eval").create());

    @Override
    protected LifeInsuranceCalculator getLifeInsuranceCalculator() {
        return lifeInsuranceCalculator;
    }

}
