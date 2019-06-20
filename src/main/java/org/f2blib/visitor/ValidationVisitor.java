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

package org.f2blib.visitor;

/**
 * Abstraction layer for the validation logic. Used to decouple the system and
 * to improve testability.
 */
public interface ValidationVisitor extends Visitor {

    LocalVariables getLocalVariables();

}
