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

import com.github.drstefanfriedrich.f2blib.ast.Binomial;
import com.github.drstefanfriedrich.f2blib.ast.Faculty;
import com.github.drstefanfriedrich.f2blib.ast.Int;
import com.github.drstefanfriedrich.f2blib.ast.Round;

/**
 * A visitor evaluating integer expressions.
 */
public class IntEvalVisitor extends AbstractVisitor {

    private final EvalVisitor reference;

    public IntEvalVisitor(EvalVisitor parent) {
        this.reference = parent;
    }

    @Override
    public Integer visitBinomial(Binomial binomial) {
        return (int) Math.round(binomial.accept(reference));
    }

    @Override
    public Integer visitFaculty(Faculty faculty) {
        return (int) Math.round(faculty.accept(reference));
    }

    @Override
    public Integer visitInt(Int i) {
        return (int) i.getValue().longValue();
    }

    @Override
    public Integer visitRound(Round round) {
        return (int) Math.round(round.accept(reference));
    }

}
