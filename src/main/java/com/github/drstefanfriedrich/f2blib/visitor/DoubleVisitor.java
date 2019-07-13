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

public interface DoubleVisitor {

    double visitAbs(Abs abs);

    double visitAddition(Addition addition);

    double visitArccos(Arccos arccos);

    double visitArcosh(Arcosh arcosh);

    double visitArcsin(Arcsin arcsin);

    double visitArctan(Arctan arctan);

    double visitArsinh(Arsinh arsinh);

    double visitArtanh(Artanh artanh);

    double visitBinomial(Binomial binomial);

    double visitConstant(Constant constant);

    double visitCos(Cos cos);

    double visitCosh(Cosh cosh);

    double visitDivision(Division division);

    double visitExp(Exp exp);

    double visitFaculty(Faculty faculty);

    double visitFunction(Function function);

    double visitFunctionBody(FunctionBody functionBody);

    double visitFunctionDefinition(FunctionDefinition functionDefinition);

    double visitFunctionsWrapper(FunctionsWrapper functionsWrapper);

    double visitInt(Int i);

    double visitLn(Ln ln);

    double visitMultiplication(Multiplication multiplication);

    double visitParameter(Parameter parameter);

    double visitParenthesis(Parenthesis parenthesis);

    double visitPower(Power power);

    double visitRound(Round round);

    double visitSin(Sin sin);

    double visitSinh(Sinh sinh);

    double visitSubtraction(Subtraction subtraction);

    double visitTan(Tan tan);

    double visitTanh(Tanh tanh);

    double visitVariable(Variable variable);

    double visitNeg(Neg neg);

    double visitPos(Pos pos);

    double visitDoub(Doub doub);

    double visitSqrt(Sqrt sqrt);

    double visitNoOp(NoOp noOp);

    double visitForVar(ForVar forVar);

    double visitForLoop(ForLoop forLoop);

    double visitMarkovShift(MarkovShift markovShift);

}
