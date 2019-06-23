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

package com.github.drstefanfriedrich.f2blib.generator;

import com.github.drstefanfriedrich.f2blib.ast.FunctionDefinition;
import com.github.drstefanfriedrich.f2blib.impl.FunctionEvaluation;

/**
 * Abstraction layer to decouple the system and to improve testability.
 */
public interface FunctionEvaluationBytecodeGenerator {

    FunctionEvaluation generateAndInstantiate(FunctionDefinition functionDefinition);

}
