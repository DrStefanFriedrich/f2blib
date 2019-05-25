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

package org.f2blib.generator;

import org.f2blib.PerformanceTestFunction;
import org.f2blib.impl.FunctionEvaluation;
import org.f2blib.parser.BytecodeGeneratingFunctionsListener;

public class ASMFunctionEvaluationBytecodeGenerator implements FunctionEvaluationBytecodeGenerator {


    @Override
    public Class<? extends FunctionEvaluation> generateClass(BytecodeGeneratingFunctionsListener listener) {
        // Right now return some dummy implementation
        return PerformanceTestFunction.class;
    }

}
