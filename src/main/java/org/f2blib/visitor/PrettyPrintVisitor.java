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

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * {@link Visitor} that pretty prints an abstract syntax tree when it is applied to it.
 */
public class PrettyPrintVisitor implements Visitor {

    private final StringWriter out = new StringWriter();

    private final PrintWriter pw = new PrintWriter(out);

    public String getString() {
        return out.getBuffer().toString();
    }

    /*
     * Print the given symbol followed by '(', followed by the element, followed
     * by ')'.
     */
    private void printWithParenthesis(String symbol, ASTElement element) {

        pw.print(symbol + "(");
        element.accept(this);
        pw.print(")");

    }

    private void printWithSpace(String symbol, ASTElement element) {
        pw.print(symbol + " ");
        element.accept(this);
    }

    private void printUnaryExpression(String symbol, UnaryExpression unaryExpression) {

        if (unaryExpression.precedence() < unaryExpression.getExpression().precedence()) {

            printWithParenthesis(symbol, unaryExpression.getExpression());

        } else {

            printWithSpace(symbol, unaryExpression.getExpression());
        }
    }

    private void printBinaryExpression(String symbol, BinaryExpression binaryExpression) {

        if (binaryExpression.precedence() < binaryExpression.getLeft().precedence()) {

            pw.print("(");
            binaryExpression.getLeft().accept(this);
            pw.print(") " + symbol + " ");

        } else {

            binaryExpression.getLeft().accept(this);
            pw.print(" " + symbol + " ");
        }

        if (binaryExpression.precedence() < binaryExpression.getRight().precedence()) {

            pw.print("(");
            binaryExpression.getRight().accept(this);
            pw.print(")");

        } else {

            binaryExpression.getRight().accept(this);
        }
    }

    @Override
    public Void visitAbs(Abs abs) {
        printUnaryExpression("abc", abs);
        return null;
    }

    @Override
    public Void visitAddition(Addition addition) {
        printBinaryExpression("+", addition);
        return null;
    }

    @Override
    public Void visitArccos(Arccos arccos) {
        printUnaryExpression("arccos", arccos);
        return null;
    }

    @Override
    public Void visitArcosh(Arcosh arcosh) {
        printUnaryExpression("arcosh", arcosh);
        return null;
    }

    @Override
    public Void visitArcsin(Arcsin arcsin) {
        printUnaryExpression("arcsin", arcsin);
        return null;
    }

    @Override
    public Void visitArctan(Arctan arctan) {
        printUnaryExpression("arctan", arctan);
        return null;
    }

    @Override
    public Void visitArsinh(Arsinh arsinh) {
        printUnaryExpression("arsinh", arsinh);
        return null;
    }

    @Override
    public Void visitArtanh(Artanh artanh) {
        printUnaryExpression("artanh", artanh);
        return null;
    }

    @Override
    public Void visitBinomial(Binomial binomial) {
        pw.print("binomial(");
        binomial.getK().accept(this);
        pw.print(", ");
        binomial.getN().accept(this);
        pw.print(")");
        return null;
    }

    @Override
    public Void visitConstant(Constant constant) {
        pw.print(constant);
        return null;
    }

    @Override
    public Void visitCos(Cos cos) {
        printUnaryExpression("cos", cos);
        return null;
    }

    @Override
    public Void visitCosh(Cosh cosh) {
        printUnaryExpression("cosh", cosh);
        return null;
    }

    @Override
    public Void visitDivision(Division division) {
        printBinaryExpression("/", division);
        return null;
    }

    @Override
    public Void visitExp(Exp exp) {
        printUnaryExpression("exp", exp);
        return null;
    }

    @Override
    public Void visitFaculty(Faculty faculty) {

        if (faculty.precedence() < faculty.getIntExpression().precedence()) {

            pw.print("(");
            faculty.getIntExpression().accept(this);
            pw.print(")!");

        } else {

            faculty.getIntExpression().accept(this);
            pw.print("!");
        }
        return null;
    }

    @Override
    public Void visitFunction(Function function) {
        pw.print("    f_" + (function.getIndex() + 1) + " := ");
        function.getExpression().accept(this);
        pw.println(";");
        return null;
    }

    @Override
    public Void visitFunctionBody(FunctionBody functionBody) {
        pw.println("begin");
        functionBody.getFunctions().accept(this);
        pw.println("end");
        return null;
    }

    @Override
    public Void visitFunctionDefinition(FunctionDefinition functionDefinition) {
        pw.println("function " + functionDefinition.getName() + ";");
        functionDefinition.getFunctionBody().accept(this);
        return null;
    }

    @Override
    public Void visitFunctions(Functions functions) {
        functions.getFunctions().stream()
                .forEach(f -> f.accept(this));
        return null;
    }

    @Override
    public Void visitInt(Int i) {
        pw.print(i.getValue());
        return null;
    }

    @Override
    public Void visitDoub(Doub doub) {
        pw.print(doub.getValue());
        return null;
    }

    @Override
    public Void visitLaguerre(Laguerre laguerre) {
        pw.print("laguerre(");
        laguerre.getN().accept(this);
        pw.print(", ");
        laguerre.getExpression().accept(this);
        pw.print(")");
        return null;
    }

    @Override
    public Void visitLegendre(Legendre legendre) {
        pw.print("legendre(");
        legendre.getN().accept(this);
        pw.print(", ");
        legendre.getExpression().accept(this);
        pw.print(")");
        return null;
    }

    @Override
    public Void visitLn(Ln ln) {
        printUnaryExpression("ln", ln);
        return null;
    }

    @Override
    public Void visitMultiplication(Multiplication multiplication) {
        printBinaryExpression("*", multiplication);
        return null;
    }

    @Override
    public Void visitParameter(Parameter parameter) {
        pw.print("p_" + (parameter.getIndex() + 1));
        return null;
    }

    @Override
    public Void visitParenthesis(Parenthesis parenthesis) {

        if (parenthesis.precedence() < parenthesis.getExpression().precedence()) {

            printWithParenthesis("", parenthesis.getExpression());

        } else {
            parenthesis.getExpression().accept(this);
        }
        return null;
    }

    @Override
    public Void visitPower(Power power) {
        printBinaryExpression("^", power);
        return null;
    }

    @Override
    public Void visitRound(Round round) {
        printUnaryExpression("round", round);
        return null;
    }

    @Override
    public Void visitSin(Sin sin) {
        printUnaryExpression("sin", sin);
        return null;
    }

    @Override
    public Void visitSinh(Sinh sinh) {
        printUnaryExpression("sinh", sinh);
        return null;
    }

    @Override
    public Void visitSubtraction(Subtraction subtraction) {
        printBinaryExpression("-", subtraction);
        return null;
    }

    @Override
    public Void visitTan(Tan tan) {
        printUnaryExpression("tan", tan);
        return null;
    }

    @Override
    public Void visitTanh(Tanh tanh) {
        printUnaryExpression("tanh", tanh);
        return null;
    }

    @Override
    public Void visitVariable(Variable variable) {
        pw.print("x_" + (variable.getIndex() + 1));
        return null;
    }

    @Override
    public Void visitNeg(Neg neg) {

        if (neg.precedence() < neg.getExpression().precedence()) {

            printWithParenthesis("-", neg.getExpression());
        } else {
            pw.print("-");
            neg.getExpression().accept(this);
        }
        return null;
    }

    @Override
    public Void visitPos(Pos pos) {

        if (pos.precedence() < pos.getExpression().precedence()) {

            printWithParenthesis("+", pos.getExpression());
        } else {
            pos.getExpression().accept(this);
        }
        return null;
    }

}
