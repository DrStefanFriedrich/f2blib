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

package com.github.drstefanfriedrich.f2blib.ast;

import com.google.common.base.MoreObjects;
import com.github.drstefanfriedrich.f2blib.visitor.DoubleVisitor;
import com.github.drstefanfriedrich.f2blib.visitor.Visitor;

import java.util.*;

/**
 * {@link FunctionsWrapper} model a set of mathematical expressions. Example:<p>
 * <code>f_1 := x_1^2</code><p>
 * <code>f_2 := 3-x_2</code>
 */
public final class FunctionsWrapper implements ASTElement, DoubleASTElement {

    private final List<Function> functions = new ArrayList<>();

    public FunctionsWrapper(List<Function> functions) {
        this.functions.addAll(functions);
    }

    public FunctionsWrapper(Function... functions) {
        this(Arrays.asList(functions));
    }

    /*
     * We violate the Law of Demeter in order to have a more fluent coding style
     * later in the visitors (i.e. Java Streams)
     */
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
        FunctionsWrapper functionsWrapper = (FunctionsWrapper) o;
        return Objects.equals(functions, functionsWrapper.functions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functions);
    }

    @Override
    public <T> T accept(Visitor visitor) {
        return visitor.visitFunctionsWrapper(this);
    }

    @Override
    public double accept(DoubleVisitor visitor) {
        return visitor.visitFunctionsWrapper(this);
    }

}
