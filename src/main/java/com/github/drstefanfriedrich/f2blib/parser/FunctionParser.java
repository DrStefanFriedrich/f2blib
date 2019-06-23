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

package com.github.drstefanfriedrich.f2blib.parser;

import com.github.drstefanfriedrich.f2blib.ast.FunctionDefinition;
import com.github.drstefanfriedrich.f2blib.ast.FunctionDefinition;

/**
 * Abstraction for parsing function definition strings to {@link FunctionDefinition}s.
 * Mainly used to decouple the system and to improve testability.
 */
public interface FunctionParser {

    /**
     * Convert a function definition to a Java object.
     *
     * @param functionDefinition The function definition as a string; must conform
     *                           to the grammar.
     * @return The Java object representing the function definition.
     */
    FunctionDefinition parse(String functionDefinition);

}
