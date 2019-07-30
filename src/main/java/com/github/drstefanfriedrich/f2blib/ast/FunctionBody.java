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

import com.github.drstefanfriedrich.f2blib.visitor.Visitor;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Objects;

/**
 * A {@link FunctionBody} models the actual function definition in form of
 * mathematical expressions or for loops and the like.
 */
public final class FunctionBody implements Serializable, ASTElement {

    private final FunctionsWrapper functionsWrapper;

    private final ForLoop forLoop;

    public FunctionBody(FunctionsWrapper functionsWrapper) {
        this.functionsWrapper = functionsWrapper;
        this.forLoop = null;
    }

    public FunctionBody(ForLoop forLoop) {
        this.forLoop = forLoop;
        functionsWrapper = null;
    }

    /*
     * We violate the Law of Demeter in order to have a more fluent coding style
     * later in the visitors (i.e. Java Streams)
     */
    public FunctionsWrapper getFunctionsWrapper() {
        return functionsWrapper;
    }

    public ForLoop getForLoop() {
        return forLoop;
    }

    public boolean isForLoop() {
        return forLoop != null;
    }

    public <T> T acceptForLoop(Visitor visitor) {
        return forLoop.accept(visitor);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("functionsWrapper", functionsWrapper)
                .add("forLoop", forLoop)
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
        FunctionBody that = (FunctionBody) o;
        return Objects.equals(functionsWrapper, that.functionsWrapper) &&
                Objects.equals(forLoop, that.forLoop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionsWrapper, forLoop);
    }

    @Override
    public <T> T accept(Visitor visitor) {
        return visitor.visitFunctionBody(this);
    }

}
