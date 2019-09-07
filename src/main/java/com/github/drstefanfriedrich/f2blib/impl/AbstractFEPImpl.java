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

package com.github.drstefanfriedrich.f2blib.impl;

import com.github.drstefanfriedrich.f2blib.FunctionEvaluationKernel;
import com.github.drstefanfriedrich.f2blib.FunctionEvaluationProvider;
import com.google.common.annotations.VisibleForTesting;

abstract class AbstractFEPImpl implements FunctionEvaluationProvider {

    @Override
    public FunctionEvaluationKernel create() {
        return getBean(FunctionEvaluationKernel.class);
    }

    /*
     * Spring-like getBean method.
     */
    @VisibleForTesting
    protected final <T> T getBean(Class<T> interfaceType) {

        if (!interfaceType.equals(FunctionEvaluationKernel.class)) {
            throw new IllegalArgumentException("The only supported bean type is FunctionEvaluationKernel");
        }

        @SuppressWarnings("unchecked")
        T t = (T) constructInstance();
        return t;
    }

    protected abstract FunctionEvaluationKernel constructInstance();

}
