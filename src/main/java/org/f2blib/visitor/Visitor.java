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

    void visitAbs(Abs abs);

    void visitAddition(Addition addition);

    void visitArccos(Arccos arccos);

    void visitArcosh(Arcosh arcosh);

    void visitArcsin(Arcsin arcsin);

    void visitArctan(Arctan arctan);

    void visitArsinh(Arsinh arsinh);

    void visitArtanh(Artanh artanh);

    void visitBinomial(Binomial binomial);

    void visitConstant(Constant constant);

    void visitCos(Cos cos);

    void visitCosh(Cosh cosh);

    void visitDivision(Division division);

    void visitExp(Exp exp);

    void visitFaculty(Faculty faculty);

    void visitFunction(Function function);

    void visitFunctionBody(FunctionBody functionBody);

    void visitFunctionDefinition(FunctionDefinition functionDefinition);

    void visitFunctions(Functions functions);

    void visitInt(Int i);

    void visitLaguerre(Laguerre laguerre);

    void visitLegendre(Legendre legendre);

    void visitLn(Ln ln);

    void visitMultiplication(Multiplication multiplication);

    void visitParameter(Parameter parameter);

    void visitParenthesis(Parenthesis parenthesis);

    void visitPower(Power power);

    void visitRound(Round round);

    void visitSin(Sin sin);

    void visitSinh(Sinh sinh);

    void visitSubtraction(Subtraction subtraction);

    void visitTan(Tan tan);

    void visitTanh(Tanh tanh);

    void visitVariable(Variable variable);

}
