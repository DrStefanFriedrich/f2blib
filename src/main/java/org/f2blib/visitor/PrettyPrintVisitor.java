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

    private final SymbolVisitor symbolVisitor = new SymbolVisitor();

    private final PrecedenceVisitor precedenceVisitor = new PrecedenceVisitor();

    public String getString() {
        return out.getBuffer().toString();
    }

    /*
     * Print the given symbol followed by '(', followed by the element, followed
     * by ')'.
     */
    private void printWithParenthesis(UnaryExpression unaryExpression) {

        String symbol = unaryExpression.accept(symbolVisitor);

        pw.print(symbol + "(");
        unaryExpression.getExpression().accept(this);
        pw.print(")");

    }

    private void printWithSpace(UnaryExpression unaryExpression) {

        String symbol = unaryExpression.accept(symbolVisitor);


        pw.print(symbol + " ");
        unaryExpression.getExpression().accept(this);
    }

    private void printUnaryExpression(UnaryExpression unaryExpression) {

        int precedenceThis = unaryExpression.accept(precedenceVisitor);
        int precedenceInner = unaryExpression.getExpression().accept(precedenceVisitor);

        if (precedenceThis < precedenceInner) {

            printWithParenthesis(unaryExpression);

        } else {

            printWithSpace(unaryExpression);
        }
    }

    private void printBinaryExpression(BinaryExpression binaryExpression) {

        String symbol = binaryExpression.accept(symbolVisitor);
        int precedenceThis = binaryExpression.accept(precedenceVisitor);
        int precedenceLeft = binaryExpression.getLeft().accept(precedenceVisitor);
        int precedenceRight = binaryExpression.getRight().accept(precedenceVisitor);

        if (precedenceThis < precedenceLeft) {

            pw.print("(");
            binaryExpression.getLeft().accept(this);
            pw.print(") " + symbol + " ");

        } else {

            binaryExpression.getLeft().accept(this);
            pw.print(" " + symbol + " ");
        }

        if (precedenceThis < precedenceRight) {

            pw.print("(");
            binaryExpression.getRight().accept(this);
            pw.print(")");

        } else {

            binaryExpression.getRight().accept(this);
        }
    }

    @Override
    public Void visitNeg(Neg neg) {

        int precedenceThis = neg.accept(precedenceVisitor);
        int precedenceInner = neg.getExpression().accept(precedenceVisitor);

        if (precedenceThis < precedenceInner) {

            printWithParenthesis(neg);
        } else {
            pw.print((String) neg.accept(symbolVisitor));
            neg.getExpression().accept(this);
        }
        return null;
    }

    @Override
    public Void visitPos(Pos pos) {

        int precedenceThis = pos.accept(precedenceVisitor);
        int precedenceInner = pos.getExpression().accept(precedenceVisitor);

        if (precedenceThis < precedenceInner) {

            printWithParenthesis(pos);
        } else {
            pos.getExpression().accept(this);
        }
        return null;
    }

    @Override
    public Void visitFaculty(Faculty faculty) {

        String symbol = faculty.accept(symbolVisitor);
        int precedenceThis = faculty.accept(precedenceVisitor);
        int precedenceInner = faculty.getIntExpression().accept(precedenceVisitor);

        if (precedenceThis < precedenceInner) {

            pw.print("(");
            faculty.getIntExpression().accept(this);
            pw.print(")" + symbol);

        } else {

            faculty.getIntExpression().accept(this);
            pw.print(symbol);
        }
        return null;
    }

    @Override
    public Void visitParenthesis(Parenthesis parenthesis) {

        int precedenceThis = parenthesis.accept(precedenceVisitor);
        int precedenceInner = parenthesis.getExpression().accept(precedenceVisitor);

        if (precedenceThis < precedenceInner) {

            printWithParenthesis(parenthesis);

        } else {
            parenthesis.getExpression().accept(this);
        }
        return null;
    }

    @Override
    public Void visitAbs(Abs abs) {
        printUnaryExpression(abs);
        return null;
    }

    @Override
    public Void visitAddition(Addition addition) {
        printBinaryExpression(addition);
        return null;
    }

    @Override
    public Void visitArccos(Arccos arccos) {
        printUnaryExpression(arccos);
        return null;
    }

    @Override
    public Void visitArcosh(Arcosh arcosh) {
        printUnaryExpression(arcosh);
        return null;
    }

    @Override
    public Void visitArcsin(Arcsin arcsin) {
        printUnaryExpression(arcsin);
        return null;
    }

    @Override
    public Void visitArctan(Arctan arctan) {
        printUnaryExpression(arctan);
        return null;
    }

    @Override
    public Void visitArsinh(Arsinh arsinh) {
        printUnaryExpression(arsinh);
        return null;
    }

    @Override
    public Void visitArtanh(Artanh artanh) {
        printUnaryExpression(artanh);
        return null;
    }

    @Override
    public Void visitBinomial(Binomial binomial) {

        String symbol = binomial.accept(symbolVisitor);

        pw.print(symbol + "(");
        binomial.getN().accept(this);
        pw.print(", ");
        binomial.getK().accept(this);
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
        printUnaryExpression(cos);
        return null;
    }

    @Override
    public Void visitCosh(Cosh cosh) {
        printUnaryExpression(cosh);
        return null;
    }

    @Override
    public Void visitDivision(Division division) {
        printBinaryExpression(division);
        return null;
    }

    @Override
    public Void visitExp(Exp exp) {
        printUnaryExpression(exp);
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
    public Void visitLn(Ln ln) {
        printUnaryExpression(ln);
        return null;
    }

    @Override
    public Void visitMultiplication(Multiplication multiplication) {
        printBinaryExpression(multiplication);
        return null;
    }

    @Override
    public Void visitParameter(Parameter parameter) {
        pw.print("p_" + (parameter.getIndex() + 1));
        return null;
    }

    @Override
    public Void visitPower(Power power) {
        printBinaryExpression(power);
        return null;
    }

    @Override
    public Void visitRound(Round round) {
        printUnaryExpression(round);
        return null;
    }

    @Override
    public Void visitSin(Sin sin) {
        printUnaryExpression(sin);
        return null;
    }

    @Override
    public Void visitSinh(Sinh sinh) {
        printUnaryExpression(sinh);
        return null;
    }

    @Override
    public Void visitSubtraction(Subtraction subtraction) {
        printBinaryExpression(subtraction);
        return null;
    }

    @Override
    public Void visitTan(Tan tan) {
        printUnaryExpression(tan);
        return null;
    }

    @Override
    public Void visitTanh(Tanh tanh) {
        printUnaryExpression(tanh);
        return null;
    }

    @Override
    public Void visitVariable(Variable variable) {
        pw.print("x_" + (variable.getIndex() + 1));
        return null;
    }

    @Override
    public Void visitSqrt(Sqrt sqrt) {
        printUnaryExpression(sqrt);
        return null;
    }

}
