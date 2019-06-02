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

package org.f2blib.ast;

import java.io.Serializable;

/**
 * Models a mathematical expression. For example, an expression can be evaluated,
 * differentiated, pretty printed, or converted to JVM bytecode.
 */
public interface Expression extends Serializable, ASTElement {

    /**
     * Operator precedence is the order in which operators will be evaluated or
     * applied.
     *
     * @return The precedence, 0 meaning highest binding or coupling, monotonically
     * increasing.
     */
    int precedence();

}