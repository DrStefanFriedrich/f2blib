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

package org.f2blib.impl;

import com.google.common.annotations.VisibleForTesting;
import org.f2blib.FunctionEvaluationKernel;
import org.f2blib.FunctionEvaluationProvider;

/**
 * Separate construction of a system from using it.
 */
public class F2BLibAssembler implements FunctionEvaluationProvider {

    @Override
    public FunctionEvaluationKernel create() {
        return getBean(FunctionEvaluationKernel.class);
    }

    /*
     * Spring-like getBean method.
     */
    private <T> T getBean(Class<T> interfaceType) {

        if (!interfaceType.equals(FunctionEvaluationKernel.class)) {
            throw new IllegalArgumentException("The only supported bean type is FunctionEvaluationKernel");
        }

        @SuppressWarnings("unchecked")
        T t = (T) constructF2BLibImpl();
        return t;
    }

    @VisibleForTesting
    protected FunctionEvaluationKernel constructF2BLibImpl() {
        return new F2BLibImpl(null, null);
    }

    @Override
    public String getKernelIdentifier() {
        return "f2blib";
    }

}