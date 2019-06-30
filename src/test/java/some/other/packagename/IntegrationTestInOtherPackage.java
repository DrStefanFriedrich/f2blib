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

package some.other.packagename;

import com.github.drstefanfriedrich.f2blib.FunctionEvaluationFactory;
import com.github.drstefanfriedrich.f2blib.FunctionEvaluationKernel;
import com.github.drstefanfriedrich.f2blib.util.TestUtil;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

/**
 * Some very simple integration tests for the project.
 */
public class IntegrationTestInOtherPackage {


    private final FunctionEvaluationKernel kernel = new FunctionEvaluationFactory().get().create();

    private static final String FUNCTION = "function some.packagename.SomeClassName;" +
            "begin" +
            "    f_1 := p_1 * sin(x_1) + x_2;" +
            "    f_2 := ln(p_2) + exp(x_2);" +
            "end";

    @Test
    public void functionFromReadme() {

        kernel.load(FUNCTION);

        double[] x = new double[]{2.51, 1.28};
        double[] p = new double[]{-1.45, 8.27};
        double[] y = new double[2];

        kernel.eval("some.packagename.SomeClassName", p, x, y);

        MatcherAssert.assertThat(y[0], TestUtil.closeTo(0.423875168));
        MatcherAssert.assertThat(y[1], TestUtil.closeTo(5.709274235));
    }

}
