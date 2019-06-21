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

public class SpecialFunctionsUsageImpl implements SpecialFunctionsUsage {

    private boolean arsinhUsed;

    private boolean arcoshUsed;

    private boolean artanhUsed;

    @Override
    public boolean isArsinhUsed() {
        return arsinhUsed;
    }

    @Override
    public boolean isArcoshUsed() {
        return arcoshUsed;
    }

    @Override
    public boolean isArtanhUsed() {
        return artanhUsed;
    }

    public void setArsinhUsed() {
        this.arsinhUsed = true;
    }

    public void setArcoshUsed() {
        this.arcoshUsed = true;
    }

    public void setArtanhUsed() {
        this.artanhUsed = true;
    }

}
