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
import org.f2blib.visitor.Visitor;

import java.util.Objects;

/**
 * {@link FunctionBody} models the actual function definition in form of mathematical
 * expressions or (in the future) for loops and the like.
 */
public class FunctionBody {

    private final Functions functions;

    public FunctionBody(Functions functions) {
        this.functions = functions;
    }

    public Functions getFunctions() {
        return functions;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("functions", functions)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionBody that = (FunctionBody) o;
        return Objects.equals(functions, that.functions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functions);
    }

    public void accept(Visitor visitor) {
        visitor.visitFunctionBody(this);
    }

}
