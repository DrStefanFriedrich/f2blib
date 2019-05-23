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

public interface FunctionEvaluationProvider {

    /**
     * Create an instance, fully configured and ready to use.
     *
     * @return The instance to be created.
     */
    FunctionEvaluationKernel create();

    /**
     * Returns some "unique" identifier for this kernel.
     *
     * @return The identifier.
     */
    String getKernelIdentifier();

}
