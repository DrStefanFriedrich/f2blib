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

/**
 * Tracks usage of special functions like arsinh, arcosh, and artanh.
 */
public interface SpecialFunctionsUsage {

    boolean isArsinhUsed();

    boolean isArcoshUsed();

    boolean isArtanhUsed();

}