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

import org.f2blib.ast.*;

public interface Visitor {

    <T> T visitAbs(Abs abs);

    <T> T visitAddition(Addition addition);

    <T> T visitArccos(Arccos arccos);

    <T> T visitArcosh(Arcosh arcosh);

    <T> T visitArcsin(Arcsin arcsin);

    <T> T visitArctan(Arctan arctan);

    <T> T visitArsinh(Arsinh arsinh);

    <T> T visitArtanh(Artanh artanh);

    <T> T visitBinomial(Binomial binomial);

    <T> T visitConstant(Constant constant);

    <T> T visitCos(Cos cos);

    <T> T visitCosh(Cosh cosh);

    <T> T visitDivision(Division division);

    <T> T visitExp(Exp exp);

    <T> T visitFaculty(Faculty faculty);

    <T> T visitFunction(Function function);

    <T> T visitFunctionBody(FunctionBody functionBody);

    <T> T visitFunctionDefinition(FunctionDefinition functionDefinition);

    <T> T visitFunctions(Functions functions);

    <T> T visitInt(Int i);

    <T> T visitLaguerre(Laguerre laguerre);

    <T> T visitLegendre(Legendre legendre);

    <T> T visitLn(Ln ln);

    <T> T visitMultiplication(Multiplication multiplication);

    <T> T visitParameter(Parameter parameter);

    <T> T visitParenthesis(Parenthesis parenthesis);

    <T> T visitPower(Power power);

    <T> T visitRound(Round round);

    <T> T visitSin(Sin sin);

    <T> T visitSinh(Sinh sinh);

    <T> T visitSubtraction(Subtraction subtraction);

    <T> T visitTan(Tan tan);

    <T> T visitTanh(Tanh tanh);

    <T> T visitVariable(Variable variable);

    <T> T visitNeg(Neg neg);

    <T> T visitPos(Pos pos);

}
