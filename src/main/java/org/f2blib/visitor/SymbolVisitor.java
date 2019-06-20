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

/**
 * A {@link Visitor} that returns the mathematical symbol for each node. Example:
 * for the sine function sin will be return. For the addition, the symbol + will
 * be returned.
 */
public class SymbolVisitor implements Visitor {

    public static final String EXCEPTION_MESSAGE = "This method must not be called on the SymbolVisitor class";

    @Override
    public String visitAbs(Abs abs) {
        return "abs";
    }

    @Override
    public String visitAddition(Addition addition) {
        return "+";
    }

    @Override
    public String visitArccos(Arccos arccos) {
        return "arccos";
    }

    @Override
    public String visitArcosh(Arcosh arcosh) {
        return "arcosh";
    }

    @Override
    public String visitArcsin(Arcsin arcsin) {
        return "arcsin";
    }

    @Override
    public String visitArctan(Arctan arctan) {
        return "arctan";
    }

    @Override
    public String visitArsinh(Arsinh arsinh) {
        return "arsinh";
    }

    @Override
    public String visitArtanh(Artanh artanh) {
        return "artanh";
    }

    @Override
    public String visitBinomial(Binomial binomial) {
        return "binomial";
    }

    @Override
    public String visitConstant(Constant constant) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visitCos(Cos cos) {
        return "cos";
    }

    @Override
    public String visitCosh(Cosh cosh) {
        return "cosh";
    }

    @Override
    public String visitDivision(Division division) {
        return "/";
    }

    @Override
    public String visitExp(Exp exp) {
        return "exp";
    }

    @Override
    public String visitFaculty(Faculty faculty) {
        return "!";
    }

    @Override
    public String visitFunction(Function function) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visitFunctionBody(FunctionBody functionBody) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visitFunctionDefinition(FunctionDefinition functionDefinition) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visitFunctions(Functions functions) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visitInt(Int i) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visitLn(Ln ln) {
        return "ln";
    }

    @Override
    public String visitMultiplication(Multiplication multiplication) {
        return "*";
    }

    @Override
    public String visitParameter(Parameter parameter) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visitParenthesis(Parenthesis parenthesis) {
        return "";
    }

    @Override
    public String visitPower(Power power) {
        return "^";
    }

    @Override
    public String visitRound(Round round) {
        return "round";
    }

    @Override
    public String visitSin(Sin sin) {
        return "sin";
    }

    @Override
    public String visitSinh(Sinh sinh) {
        return "sinh";
    }

    @Override
    public String visitSubtraction(Subtraction subtraction) {
        return "-";
    }

    @Override
    public String visitTan(Tan tan) {
        return "tan";
    }

    @Override
    public String visitTanh(Tanh tanh) {
        return "tanh";
    }

    @Override
    public String visitVariable(Variable variable) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visitNeg(Neg neg) {
        return "-";
    }

    @Override
    public String visitPos(Pos pos) {
        return "+";
    }

    @Override
    public String visitDoub(Doub doub) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public String visitSqrt(Sqrt sqrt) {
        return "sqrt";
    }

}
