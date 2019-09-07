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

import java.io.PrintWriter;
import java.io.StringWriter;

import static java.lang.String.format;

/**
 * {@link Visitor} that pretty prints an abstract syntax tree when it is applied to it.
 */
public class PrettyPrintVisitor implements Visitor {

    private final StringWriter out = new StringWriter();

    private final PrintWriter pw = new PrintWriter(out);

    private final SymbolVisitor symbolVisitor = new SymbolVisitor();

    private final PrecedenceVisitor precedenceVisitor = new PrecedenceVisitor();

    /*
     * Indentation depth
     */
    private int depth;

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
        unaryExpression.acceptExpression(this);
        pw.print(")");

    }

    private void printWithSpace(UnaryExpression unaryExpression) {

        String symbol = unaryExpression.accept(symbolVisitor);


        pw.print(symbol + " ");
        unaryExpression.acceptExpression(this);
    }

    private void printUnaryExpression(UnaryExpression unaryExpression) {

        int precedenceThis = unaryExpression.accept(precedenceVisitor);
        int precedenceInner = unaryExpression.acceptExpression(precedenceVisitor);

        if (precedenceThis < precedenceInner) {

            printWithParenthesis(unaryExpression);

        } else {

            printWithSpace(unaryExpression);
        }
    }

    private void printBinaryExpression(BinaryExpression binaryExpression) {

        String symbol = binaryExpression.accept(symbolVisitor);
        int precedenceThis = binaryExpression.accept(precedenceVisitor);
        int precedenceLeft = binaryExpression.acceptLeft(precedenceVisitor);
        int precedenceRight = binaryExpression.acceptRight(precedenceVisitor);

        if (precedenceThis < precedenceLeft) {

            pw.print("(");
            binaryExpression.acceptLeft(this);
            pw.print(") " + symbol + " ");

        } else {

            binaryExpression.acceptLeft(this);
            pw.print(" " + symbol + " ");
        }

        if (precedenceThis < precedenceRight) {

            pw.print("(");
            binaryExpression.acceptRight(this);
            pw.print(")");

        } else {

            binaryExpression.acceptRight(this);
        }
    }

    @Override
    public Void visit(Neg neg) {

        int precedenceThis = neg.accept(precedenceVisitor);
        int precedenceInner = neg.acceptExpression(precedenceVisitor);

        if (precedenceThis < precedenceInner) {

            printWithParenthesis(neg);
        } else {
            pw.print((String) neg.accept(symbolVisitor));
            neg.acceptExpression(this);
        }
        return null;
    }

    @Override
    public Void visit(Pos pos) {

        int precedenceThis = pos.accept(precedenceVisitor);
        int precedenceInner = pos.acceptExpression(precedenceVisitor);

        if (precedenceThis < precedenceInner) {

            printWithParenthesis(pos);
        } else {
            pos.acceptExpression(this);
        }
        return null;
    }

    @Override
    public Void visit(Faculty faculty) {

        String symbol = faculty.accept(symbolVisitor);
        int precedenceThis = faculty.accept(precedenceVisitor);
        int precedenceInner = faculty.acceptExpression(precedenceVisitor);

        if (precedenceThis < precedenceInner) {

            pw.print("(");
            faculty.acceptExpression(this);
            pw.print(")" + symbol);

        } else {

            faculty.acceptExpression(this);
            pw.print(symbol);
        }
        return null;
    }

    @Override
    public Void visit(Parenthesis parenthesis) {

        int precedenceThis = parenthesis.accept(precedenceVisitor);
        int precedenceInner = parenthesis.acceptExpression(precedenceVisitor);

        if (precedenceThis < precedenceInner) {

            printWithParenthesis(parenthesis);

        } else {
            parenthesis.acceptExpression(this);
        }
        return null;
    }

    @Override
    public Void visit(Abs abs) {
        printUnaryExpression(abs);
        return null;
    }

    @Override
    public Void visit(Addition addition) {
        printBinaryExpression(addition);
        return null;
    }

    @Override
    public Void visit(Arccos arccos) {
        printUnaryExpression(arccos);
        return null;
    }

    @Override
    public Void visit(Arcosh arcosh) {
        printUnaryExpression(arcosh);
        return null;
    }

    @Override
    public Void visit(Arcsin arcsin) {
        printUnaryExpression(arcsin);
        return null;
    }

    @Override
    public Void visit(Arctan arctan) {
        printUnaryExpression(arctan);
        return null;
    }

    @Override
    public Void visit(Arsinh arsinh) {
        printUnaryExpression(arsinh);
        return null;
    }

    @Override
    public Void visit(Artanh artanh) {
        printUnaryExpression(artanh);
        return null;
    }

    @Override
    public Void visit(Binomial binomial) {

        String symbol = binomial.accept(symbolVisitor);

        pw.print(symbol + "(");
        binomial.acceptN(this);
        pw.print(", ");
        binomial.acceptK(this);
        pw.print(")");
        return null;
    }

    @Override
    public Void visit(Constant constant) {
        switch (constant) {
            case E:
                pw.print("euler");
                break;
            case PI:
                pw.print("pi");
                break;
            case BOLTZMANN:
                pw.print("boltzmann");
                break;
            default:
                throw new IllegalArgumentException(format("Unknown enum %s", constant.toString()));
        }
        return null;
    }

    @Override
    public Void visit(Cos cos) {
        printUnaryExpression(cos);
        return null;
    }

    @Override
    public Void visit(Cosh cosh) {
        printUnaryExpression(cosh);
        return null;
    }

    @Override
    public Void visit(Division division) {
        printBinaryExpression(division);
        return null;
    }

    @Override
    public Void visit(Exp exp) {
        printUnaryExpression(exp);
        return null;
    }

    @Override
    public Void visit(FunctionDefinition functionDefinition) {
        pw.println("function " + functionDefinition.getName() + ";");
        functionDefinition.getFunctionBody().accept(this);
        return null;
    }

    @Override
    public Void visit(FunctionBody functionBody) {
        pw.println("begin");

        depth++;

        if (functionBody.isForLoop()) {
            functionBody.getForLoop().accept(this);
        } else {
            functionBody.getFunctionsWrapper().accept(this);
        }

        depth--;

        pw.println("end");
        return null;
    }

    @Override
    public Void visit(FunctionsWrapper functionsWrapper) {
        functionsWrapper.getAuxiliaryVariables().stream()
                .forEach(av -> av.accept(this));
        functionsWrapper.getFunctions().stream()
                .forEach(f -> f.accept(this));
        functionsWrapper.acceptMarkovShift(this);
        return null;
    }

    private String spaces() {
        int numberOfSpaces = 4 * depth;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfSpaces; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    @Override
    public Void visit(Function function) {
        String spaces = spaces();

        pw.print(spaces + "f_" + (function.getIndex() + 1) + " := ");
        function.acceptExpression(this);
        pw.println(";");
        return null;
    }

    @Override
    public <T> T visit(ForLoop forLoop) {
        String spaces = spaces();

        pw.print(spaces + "for " + forLoop.getVariableName());
        pw.print(" from ");
        forLoop.acceptStart(this);
        pw.print(" to ");
        forLoop.acceptEnd(this);
        pw.print(" step ");
        forLoop.acceptStep(this);
        pw.println(';');
        pw.println(spaces + "begin");

        depth++;

        forLoop.acceptFunctionsWrapper(this);

        depth--;

        pw.println(spaces + "end");

        return null;
    }

    @Override
    public Void visit(Int i) {
        pw.print(i.getValue());
        return null;
    }

    @Override
    public Void visit(Doub doub) {
        pw.print(doub.getValue());
        return null;
    }

    @Override
    public Void visit(Ln ln) {
        printUnaryExpression(ln);
        return null;
    }

    @Override
    public Void visit(Multiplication multiplication) {
        printBinaryExpression(multiplication);
        return null;
    }

    @Override
    public Void visit(Power power) {
        printBinaryExpression(power);
        return null;
    }

    @Override
    public Void visit(Round round) {
        printUnaryExpression(round);
        return null;
    }

    @Override
    public Void visit(Sin sin) {
        printUnaryExpression(sin);
        return null;
    }

    @Override
    public Void visit(Sinh sinh) {
        printUnaryExpression(sinh);
        return null;
    }

    @Override
    public Void visit(Subtraction subtraction) {
        printBinaryExpression(subtraction);
        return null;
    }

    @Override
    public Void visit(Tan tan) {
        printUnaryExpression(tan);
        return null;
    }

    @Override
    public Void visit(Tanh tanh) {
        printUnaryExpression(tanh);
        return null;
    }

    @Override
    public Void visit(Variable variable) {
        if (variable.getIndexExpression() == null) {
            pw.print("x_" + (variable.getIndex() + 1));
        } else {
            pw.print("x_{");
            variable.getIndexExpression().accept(this);
            pw.print("}");
        }
        return null;
    }

    @Override
    public Void visit(Parameter parameter) {
        if (parameter.getIndexExpression() == null) {
            pw.print("p_" + (parameter.getIndex() + 1));
        } else {
            pw.print("p_{");
            parameter.getIndexExpression().accept(this);
            pw.print("}");
        }
        return null;
    }

    @Override
    public Void visit(Sqrt sqrt) {
        printUnaryExpression(sqrt);
        return null;
    }

    @Override
    public <T> T visit(NoOp noOp) {
        throw new IllegalStateException("visit must not be called on the PrettyPrintVisitor");
    }

    @Override
    public Void visit(IntVar intVar) {
        pw.print(intVar.getVariableName());
        return null;
    }

    @Override
    public Void visit(MarkovShift markovShift) {
        String spaces = spaces();

        String symbol = markovShift.accept(symbolVisitor);
        pw.print(spaces + symbol + "(");
        markovShift.getOffset().accept(this);
        pw.println(");");

        return null;
    }

    @Override
    public Void visit(Sum sum) {

        String symbol = sum.accept(symbolVisitor);

        pw.print(symbol);
        pw.print("_{");
        pw.print(sum.getVariableName() + " = ");
        sum.acceptStart(this);
        pw.print("}^{");
        sum.acceptEnd(this);
        pw.print("}(");
        sum.acceptInner(this);
        pw.print(")");

        return null;
    }

    @Override
    public Void visit(Prod prod) {

        String symbol = prod.accept(symbolVisitor);

        pw.print(symbol);
        pw.print("_{");
        pw.print(prod.getVariableName() + " = ");
        prod.acceptStart(this);
        pw.print("}^{");
        prod.acceptEnd(this);
        pw.print("}(");
        prod.acceptInner(this);
        pw.print(")");

        return null;
    }

    @Override
    public Void visit(AuxVar auxVar) {
        pw.print(auxVar.getVariableName());
        return null;
    }

    @Override
    public Void visit(AuxiliaryVariable auxiliaryVariable) {
        String spaces = spaces();

        pw.print(spaces);
        auxiliaryVariable.acceptAuxVar(this);
        pw.print(" := ");
        auxiliaryVariable.acceptInner(this);
        pw.println(";");
        return null;
    }

}
