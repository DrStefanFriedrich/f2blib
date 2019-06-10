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

import com.google.common.annotations.VisibleForTesting;

import java.util.Iterator;
import java.util.ServiceLoader;

import static java.lang.String.format;

class FunctionEvaluationFactory {

    private final Iterable<FunctionEvaluationProvider> serviceLoader = ServiceLoader.load(FunctionEvaluationProvider.class);

    @VisibleForTesting
    Iterable<FunctionEvaluationProvider> getServiceLoader() {
        return serviceLoader;
    }

    FunctionEvaluationProvider get() {
        return getIterator().next();
    }

    FunctionEvaluationProvider get(String kernelIdentifier) {

        Iterator<FunctionEvaluationProvider> iter = getIterator();
        while (iter.hasNext()) {

            FunctionEvaluationProvider provider = iter.next();

            if (provider.getKernelIdentifier().equals(kernelIdentifier)) {
                return provider;
            }
        }

        throw new IllegalArgumentException(format("Provider not found: %s", kernelIdentifier));
    }

    private Iterator<FunctionEvaluationProvider> getIterator() {
        if (!getServiceLoader().iterator().hasNext()) {
            throw new IllegalStateException("No provider at all found for type FunctionEvaluationProvider");
        }

        return getServiceLoader().iterator();
    }

}
