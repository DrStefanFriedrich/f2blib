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

    private final static ServiceLoader<FunctionEvaluationProvider> serviceLoader = ServiceLoader.load(FunctionEvaluationProvider.class);

    public static FunctionEvaluationProvider get(String kernelIdentifier) {

        Iterator<FunctionEvaluationProvider> iter = getIterator();
        while (iter.hasNext()) {

            FunctionEvaluationProvider provider = iter.next();

            if (provider.getKernelIdentifier().equals(kernelIdentifier)) {
                return provider;
            }
        }

        throw new RuntimeException("TODO SF not found");
    }

    public static FunctionEvaluationProvider get() {

        return getIterator().next();
    }

    private static Iterator<FunctionEvaluationProvider> getIterator() {

        if (!serviceLoader.iterator().hasNext()) {
            throw new RuntimeException("TODO SF Service not found");
        }

        return serviceLoader.iterator();
    }

}
