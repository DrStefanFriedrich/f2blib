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

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Abstract base class for variables or parameters like x_1 or p_1.
 */
public abstract class IndexedExpression implements Expression {

    /**
     * The index starts counting with 0. Caution: in mathematical notation, like
     * in x_1 or p_1 we start counting with 1.
     */
    private final int index;

    public IndexedExpression(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("index", index)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IndexedExpression that = (IndexedExpression) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

}
