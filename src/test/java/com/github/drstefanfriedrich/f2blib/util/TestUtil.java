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

package com.github.drstefanfriedrich.f2blib.util;

import org.hamcrest.Matcher;
import org.hamcrest.number.IsCloseTo;

import static java.lang.Math.abs;
import static org.junit.Assume.assumeTrue;

public final class TestUtil {

    private TestUtil() {
    }

    public static Matcher<Double> closeTo(double operand) {
        return IsCloseTo.closeTo(operand, abs(operand) * 1e-8);
    }

    public static void assumePerformanceTest() {
        assumeTrue("Performance tests are deactivated", performanceTestsEnabled());
    }

    private static boolean performanceTestsEnabled() {
        return Boolean.valueOf(System.getProperty("com.github.drstefanfriedrich.f2blib.performancetest.enabled", Boolean.FALSE.toString()));
    }

}
