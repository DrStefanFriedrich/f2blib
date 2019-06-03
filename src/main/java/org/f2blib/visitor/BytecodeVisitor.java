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

import static org.objectweb.asm.Opcodes.*;

public class BytecodeVisitor extends AbstractBytecodeVisitor {

    public BytecodeVisitor(BytecodeNavigator navi) {
        super(navi);
    }

    @Override
    public Void visitFunctionDefinition(FunctionDefinition functionDefinition) {

        className = functionDefinition.getName();

        generateClassHeader();
        generateStaticFields();
        generateStaticInitializers();
        generateDefaultConstructor();

        functionDefinition.getFunctionBody().accept(this);

        return null;
    }

    @Override
    public Void visitFunctionBody(FunctionBody functionBody) {

        evalMethod.visitCode();

        prepareParameters();
        prepareVariables();

        functionBody.getFunctions().accept(this);

        evalMethod.visitMaxs(navi.getMaxStack(), navi.getMaxLocals());
        evalMethod.visitInsn(RETURN);
        evalMethod.visitEnd();

        return null;
    }

    @Override
    public Void visitFunction(Function function) {

        int index = function.getIndex();

        evalMethod.visitVarInsn(ALOAD, 3);
        evalMethod.visitIntInsn(SIPUSH, index); // TODO SF index aufspalten in hi/lo

        // Visiting the function expression pushes the result value on the stack
        function.getExpression().accept(this);

        evalMethod.visitInsn(DASTORE);

        return null;
    }

    @Override
    public Void visitConstant(Constant constant) {
        switch (constant) {
            case PI:
                evalMethod.visitFieldInsn(GETSTATIC, "java/lang/Math", "PI", "D");
                break;
            case E:
                evalMethod.visitFieldInsn(GETSTATIC, "java/lang/Math", "E", "D");
                break;
            case BOLTZMANN:
                evalMethod.visitFieldInsn(GETSTATIC, className.replaceAll("\\.", "/"), "BOLTZMANN", "D");
                break;
            default:
                throw new RuntimeException("TODO SF");
        }
        return null;
    }

    @Override
    public Void visitParameter(Parameter parameter) {
        evalMethod.visitVarInsn(DLOAD, navi.getIndexForParameter(parameter));
        return null;
    }

    @Override
    public Void visitVariable(Variable variable) {
        evalMethod.visitVarInsn(DLOAD, navi.getIndexForVariable(variable));
        return null;
    }

    @Override
    public Void visitAbs(Abs abs) {
        visitUnaryExpression(abs, "java/lang/Math", "abs");
        return null;
    }

    @Override
    public Void visitArccos(Arccos arccos) {
        visitUnaryExpression(arccos, "java/lang/Math", "acos");
        return null;
    }

    @Override
    public Void visitArcosh(Arcosh arcosh) {
        visitSpecialUnaryExpression(arcosh);
        return null;
    }

    @Override
    public Void visitArcsin(Arcsin arcsin) {
        visitUnaryExpression(arcsin, "java/lang/Math", "asin");
        return null;
    }

    @Override
    public Void visitArctan(Arctan arctan) {
        visitUnaryExpression(arctan, "java/lang/Math", "atan");
        return null;
    }

    @Override
    public Void visitArsinh(Arsinh arsinh) {
        visitSpecialUnaryExpression(arsinh);
        return null;
    }

    @Override
    public Void visitArtanh(Artanh artanh) {
        visitSpecialUnaryExpression(artanh);
        return null;
    }

    @Override
    public Void visitCos(Cos cos) {
        visitUnaryExpression(cos, "java/lang/Math", "cos");
        return null;
    }

    @Override
    public Void visitCosh(Cosh cosh) {
        visitUnaryExpression(cosh, "java/lang/Math", "cosh");
        return null;
    }

    @Override
    public Void visitExp(Exp exp) {
        visitUnaryExpression(exp, "java/lang/Math", "exp");
        return null;
    }

    @Override
    public Void visitLn(Ln ln) {
        visitUnaryExpression(ln, "java/lang/Math", "log");
        return null;
    }

    @Override
    public Void visitParenthesis(Parenthesis parenthesis) {
        parenthesis.getExpression().accept(this);
        return null;
    }

    @Override
    public Void visitSin(Sin sin) {
        visitUnaryExpression(sin, "java/lang/Math", "sin");
        return null;
    }

    @Override
    public Void visitSinh(Sinh sinh) {
        visitUnaryExpression(sinh, "java/lang/Math", "sinh");
        return null;
    }

    @Override
    public Void visitTan(Tan tan) {
        visitUnaryExpression(tan, "java/lang/Math", "tan");
        return null;
    }

    @Override
    public Void visitTanh(Tanh tanh) {
        visitUnaryExpression(tanh, "java/lang/Math", "tanh");
        return null;
    }

    @Override
    public Void visitNeg(Neg neg) {

        neg.getExpression().accept(this);
        evalMethod.visitInsn(DNEG);

        return null;
    }

    @Override
    public Void visitPos(Pos pos) {
        pos.getExpression().accept(this);
        return null;
    }

    @Override
    public Void visitAddition(Addition addition) {
        addition.getLeft().accept(this);
        addition.getRight().accept(this);
        evalMethod.visitInsn(DADD);
        return null;
    }

    @Override
    public Void visitSubtraction(Subtraction subtraction) {
        subtraction.getLeft().accept(this);
        subtraction.getRight().accept(this);
        evalMethod.visitInsn(DSUB);
        return null;
    }

    @Override
    public Void visitMultiplication(Multiplication multiplication) {
        multiplication.getLeft().accept(this);
        multiplication.getRight().accept(this);
        evalMethod.visitInsn(DMUL);
        return null;
    }

    @Override
    public Void visitDivision(Division division) {
        division.getLeft().accept(this);
        division.getRight().accept(this);
        evalMethod.visitInsn(DDIV);
        return null;
    }

    @Override
    public Void visitBinomial(Binomial binomial) {
        throw new RuntimeException("TODO SF");
    }

    @Override
    public Void visitFaculty(Faculty faculty) {
        throw new RuntimeException("TODO SF");
    }

    @Override
    public Void visitInt(Int i) {
        int hi = i.getValue() & 0xFFFF0000;
        int lo = i.getValue() & 0x0000FFFF;
        evalMethod.visitIntInsn(SIPUSH, hi);
        evalMethod.visitIntInsn(SIPUSH, lo);
        evalMethod.visitInsn(IOR);
        evalMethod.visitInsn(I2D);
        return null;
    }

    @Override
    public Void visitDoub(Doub doub) {
        // TODO SF Create a constant in the constant pool
        return null;
    }

    @Override
    public Void visitLaguerre(Laguerre laguerre) {
        throw new RuntimeException("TODO SF");
    }

    @Override
    public Void visitLegendre(Legendre legendre) {
        throw new RuntimeException("TODO SF");
    }

    @Override
    public Void visitPower(Power power) {
        power.getLeft().accept(this);
        power.getRight().accept(this);
        evalMethod.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "pow", "(DD)D", false);
        return null;
    }

    @Override
    public Void visitRound(Round round) {
        // Visiting the expression pushes the result (of type double) on the stack
        round.getExpression().accept(this);
        evalMethod.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "round", "(D)J", false);
        evalMethod.visitInsn(L2D);
        return null;
    }

}
