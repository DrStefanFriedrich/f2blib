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

package org.f2blib;

import java.util.Iterator;
import java.util.ServiceLoader;

public final class FunctionEvaluationFactory {

    private FunctionEvaluationFactory() {
    }

    private final static ServiceLoader<FunctionEvaluationKernel> serviceLoader = ServiceLoader.load(FunctionEvaluationKernel.class);

    public static FunctionEvaluationKernel get(String kernelIdentifier) {

        Iterator<FunctionEvaluationKernel> iter = getIterator();
        while (iter.hasNext()) {

            FunctionEvaluationKernel kernel = iter.next();

            if (kernel.getKernelIdentifier().equals(kernelIdentifier)) {
                return kernel;
            }
        }

        throw new RuntimeException("TODO SF not found");
    }

    public static FunctionEvaluationKernel get() {

        return getIterator().next();
    }

    private static Iterator<FunctionEvaluationKernel> getIterator() {

        if (!serviceLoader.iterator().hasNext()) {
            throw new RuntimeException("TODO SF Service not found");
        }

        return serviceLoader.iterator();
    }

}
