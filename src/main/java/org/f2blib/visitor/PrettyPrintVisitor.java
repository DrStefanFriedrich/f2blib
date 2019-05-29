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
    public void visitAbs(Abs abs) {
        printUnaryExpression("abc", abs);
    }

    @Override
    public void visitAddition(Addition addition) {
        printBinaryExpression("+", addition);
    }

    @Override
    public void visitArccos(Arccos arccos) {
        printUnaryExpression("arccos", arccos);
    }

    @Override
    public void visitArcosh(Arcosh arcosh) {
        printUnaryExpression("arcosh", arcosh);
    }

    @Override
    public void visitArcsin(Arcsin arcsin) {
        printUnaryExpression("arcsin", arcsin);
    }

    @Override
    public void visitArctan(Arctan arctan) {
        printUnaryExpression("arctan", arctan);
    }

    @Override
    public void visitArsinh(Arsinh arsinh) {
        printUnaryExpression("arsinh", arsinh);
    }

    @Override
    public void visitArtanh(Artanh artanh) {
        printUnaryExpression("artanh", artanh);
    }

    @Override
    public void visitBinomial(Binomial binomial) {
        pw.print("binomial(");
        binomial.getK().accept(this);
        pw.print(", ");
        binomial.getN().accept(this);
        pw.print(")");
    }

    @Override
    public void visitConstant(Constant constant) {
        pw.print(constant);
    }

    @Override
    public void visitCos(Cos cos) {
        printUnaryExpression("cos", cos);
    }

    @Override
    public void visitCosh(Cosh cosh) {
        printUnaryExpression("cosh", cosh);
    }

    @Override
    public void visitDivision(Division division) {
        printBinaryExpression("/", division);
    }

    @Override
    public void visitExp(Exp exp) {
        printUnaryExpression("exp", exp);
    }

    @Override
    public void visitFaculty(Faculty faculty) {

        if (faculty.precedence() < faculty.getIntExpression().precedence()) {

            pw.print("(");
            faculty.getIntExpression().accept(this);
            pw.print(")!");

        } else {

            faculty.getIntExpression().accept(this);
            pw.print("!");
        }
    }

    @Override
    public void visitFunction(Function function) {
        pw.print("    f_" + (function.getIndex() + 1) + " := ");
        function.getExpression().accept(this);
        pw.println(";");
    }

    @Override
    public void visitFunctionBody(FunctionBody functionBody) {
        pw.println("begin");
        functionBody.getFunctions().accept(this);
        pw.println("end");
    }

    @Override
    public void visitFunctionDefinition(FunctionDefinition functionDefinition) {
        pw.println("function " + functionDefinition.getName() + ";");
        functionDefinition.getFunctionBody().accept(this);
    }

    @Override
    public void visitFunctions(Functions functions) {
        functions.getFunctions().stream()
                .forEach(f -> f.accept(this));
    }

    @Override
    public void visitInt(Int i) {
        pw.print(i.getValue());
    }

    @Override
    public void visitLaguerre(Laguerre laguerre) {
        pw.print("laguerre(");
        laguerre.getN().accept(this);
        pw.print(", ");
        laguerre.getExpression().accept(this);
        pw.print(")");
    }

    @Override
    public void visitLegendre(Legendre legendre) {
        pw.print("legendre(");
        legendre.getN().accept(this);
        pw.print(", ");
        legendre.getExpression().accept(this);
        pw.print(")");
    }

    @Override
    public void visitLn(Ln ln) {
        printUnaryExpression("ln", ln);
    }

    @Override
    public void visitMultiplication(Multiplication multiplication) {
        printBinaryExpression("*", multiplication);
    }

    @Override
    public void visitParameter(Parameter parameter) {
        pw.print("p_" + (parameter.getIndex() + 1));
    }

    @Override
    public void visitParenthesis(Parenthesis parenthesis) {

        if (parenthesis.precedence() < parenthesis.getExpression().precedence()) {

            printWithParenthesis("", parenthesis.getExpression());

        } else {
            parenthesis.getExpression().accept(this);
        }
    }

    @Override
    public void visitPower(Power power) {
        printBinaryExpression("^", power);
    }

    @Override
    public void visitRound(Round round) {
        printUnaryExpression("round", round);
    }

    @Override
    public void visitSin(Sin sin) {
        printUnaryExpression("sin", sin);
    }

    @Override
    public void visitSinh(Sinh sinh) {
        printUnaryExpression("sinh", sinh);
    }

    @Override
    public void visitSubtraction(Subtraction subtraction) {
        printBinaryExpression("-", subtraction);
    }

    @Override
    public void visitTan(Tan tan) {
        printUnaryExpression("tan", tan);
    }

    @Override
    public void visitTanh(Tanh tanh) {
        printUnaryExpression("tanh", tanh);
    }

    @Override
    public void visitVariable(Variable variable) {
        pw.print("x_" + (variable.getIndex() + 1));
    }

    @Override
    public void visitNeg(Neg neg) {

        if (neg.precedence() < neg.getExpression().precedence()) {

            printWithParenthesis("-", neg.getExpression());
        } else {
            pw.print("-");
            neg.getExpression().accept(this);
        }
    }

    @Override
    public void visitPos(Pos pos) {

        if (pos.precedence() < pos.getExpression().precedence()) {

            printWithParenthesis("+", pos.getExpression());
        } else {
            pos.getExpression().accept(this);
        }
    }

}
