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
import org.f2blib.visitor.DoubleVisitor;
import org.f2blib.visitor.Visitor;

import java.util.*;

/**
 * {@link Functions} model a set of mathematical expressions. Example:<p />
 * f_1 := x_1^2<p />
 * f_2 := 3-x_2
 */
public final class Functions implements ASTElement, DoubleASTElement {

    private final List<Function> functions = new ArrayList<>();

    public Functions(List<Function> functions) {
        this.functions.addAll(functions);
    }

    public Functions(Function... functions) {
        this(Arrays.asList(functions));
    }

    public List<Function> getFunctions() {
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Functions functions1 = (Functions) o;
        return Objects.equals(functions, functions1.functions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functions);
    }

    @Override
    public Void accept(Visitor visitor) {
        return visitor.visitFunctions(this);
    }

    @Override
    public double accept(DoubleVisitor visitor) {
        return visitor.visitFunctions(this);
    }

}
