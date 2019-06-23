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

package com.github.drstefanfriedrich.f2blib.visitor;

import com.github.drstefanfriedrich.f2blib.impl.FunctionEvaluation;

/**
 * Abstraction to convert an abstract syntax tree to Java bytecode. Mainly used
 * to decouple the system and to improve testability.
 */
public interface BytecodeVisitor extends Visitor {

    Class<? extends FunctionEvaluation> generate();

}
