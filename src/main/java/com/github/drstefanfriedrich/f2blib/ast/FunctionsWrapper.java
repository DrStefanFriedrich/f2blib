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
import java.util.*;

/**
 * {@link FunctionsWrapper} model a set of mathematical expressions. Example:<p>
 * <code>f_1 := x_1^2</code><p>
 * <code>f_2 := 3-x_2</code>
 */
public final class FunctionsWrapper implements Serializable, ASTElement {

    private final List<AuxiliaryVariable> auxiliaryVariables = new ArrayList<>();

    private final List<Function> functions = new ArrayList<>();

    private final transient Optional<MarkovShift> markovShift;

    public FunctionsWrapper(List<AuxiliaryVariable> auxiliaryVariables, List<Function> functions,
                            MarkovShift markovShift) {
        this.auxiliaryVariables.addAll(auxiliaryVariables);
        this.functions.addAll(functions);
        this.markovShift = Optional.ofNullable(markovShift);
    }

    public FunctionsWrapper(List<Function> functions) {
        this(new ArrayList<>(), functions, null);
    }

    public FunctionsWrapper(Function... functions) {
        this(Arrays.asList(functions));
    }

    public FunctionsWrapper(AuxiliaryVariable auxVar, Function... functions) {
        this(Arrays.asList(auxVar), Arrays.asList(functions), null);
    }

    public FunctionsWrapper(MarkovShift markovShift, Function... functions) {
        this(Collections.emptyList(), Arrays.asList(functions), markovShift);
    }

    /*
     * We violate the Law of Demeter in order to have a more fluent coding style
     * later in the visitors (i.e. Java Streams)
     */
    public List<Function> getFunctions() {
        return functions;
    }

    public List<AuxiliaryVariable> getAuxiliaryVariables() {
        return auxiliaryVariables;
    }

    public <T> Optional<T> acceptMarkovShift(Visitor visitor) {
        return markovShift.map(ms -> ms.accept(visitor));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("functions", functions)
                .add("auxiliaryVariables", auxiliaryVariables)
                .add("markovShift", markovShift)
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
        FunctionsWrapper that = (FunctionsWrapper) o;
        return Objects.equals(functions, that.functions) &&
                Objects.equals(markovShift, that.markovShift) &&
                Objects.equals(auxiliaryVariables, that.auxiliaryVariables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functions, markovShift, auxiliaryVariables);
    }

    @Override
    public <T> T accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
