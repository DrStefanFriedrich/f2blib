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

import com.github.drstefanfriedrich.f2blib.ast.*;

public interface Visitor {

    <T> T visit(Abs abs);

    <T> T visit(Addition addition);

    <T> T visit(Arccos arccos);

    <T> T visit(Arcosh arcosh);

    <T> T visit(Arcsin arcsin);

    <T> T visit(Arctan arctan);

    <T> T visit(Arsinh arsinh);

    <T> T visit(Artanh artanh);

    <T> T visit(Binomial binomial);

    <T> T visit(Constant constant);

    <T> T visit(Cos cos);

    <T> T visit(Cosh cosh);

    <T> T visit(Division division);

    <T> T visit(Exp exp);

    <T> T visit(Faculty faculty);

    <T> T visit(Function function);

    <T> T visit(FunctionBody functionBody);

    <T> T visit(FunctionDefinition functionDefinition);

    <T> T visit(FunctionsWrapper functionsWrapper);

    <T> T visit(Int i);

    <T> T visit(Ln ln);

    <T> T visit(Multiplication multiplication);

    <T> T visit(Parameter parameter);

    <T> T visit(Parenthesis parenthesis);

    <T> T visit(Power power);

    <T> T visit(Round round);

    <T> T visit(Sin sin);

    <T> T visit(Sinh sinh);

    <T> T visit(Subtraction subtraction);

    <T> T visit(Tan tan);

    <T> T visit(Tanh tanh);

    <T> T visit(Variable variable);

    <T> T visit(Neg neg);

    <T> T visit(Pos pos);

    <T> T visit(Doub doub);

    <T> T visit(Sqrt sqrt);

    <T> T visit(NoOp noOp);

    <T> T visit(ForLoop forLoop);

    <T> T visit(ForVar forVar);

    <T> T visit(MarkovShift markovShift);

}
