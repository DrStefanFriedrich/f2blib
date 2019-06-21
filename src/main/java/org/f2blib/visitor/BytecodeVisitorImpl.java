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

import static java.lang.String.format;
import static org.objectweb.asm.Opcodes.*;

/**
 * Generate bytecode for the Java virtual machine by visiting AST nodes.
 */
public class BytecodeVisitorImpl extends AbstractBytecodeVisitor {

    public static final String MATH_TYPE = "java/lang/Math";

    public BytecodeVisitorImpl(LocalVariables localVariables, SpecialFunctionsUsage specialFunctionsUsage) {
        super(localVariables, specialFunctionsUsage);
    }

    @Override
    public Void visitFunctionDefinition(FunctionDefinition functionDefinition) {

        className = functionDefinition.getName();

        generateClassHeader();
        generateDefaultConstructor();

        functionDefinition.getFunctionBody().accept(this);

        generateStaticFields();
        generateStaticInitializers();

        return null;
    }

    @Override
    public Void visitFunctionBody(FunctionBody functionBody) {

        evalMethod.visitCode();

        prepareParameters();
        prepareVariables();

        functionBody.getFunctionsWrapper().accept(this);

        evalMethod.visitMaxs(localVariables.getMaxStack(), localVariables.getMaxLocals());
        evalMethod.visitInsn(RETURN);
        evalMethod.visitEnd();

        return null;
    }

    @Override
    public Void visitFunction(Function function) {

        int index = function.getIndex();

        evalMethod.visitVarInsn(ALOAD, 3); // push y[] on the operand stack
        evalMethod.visitIntInsn(BIPUSH, index);

        // Visiting the function expression pushes the result value on the stack
        function.acceptExpression(this);

        evalMethod.visitInsn(DASTORE);

        return null;
    }

    @Override
    public Void visitConstant(Constant constant) {
        switch (constant) {
            case PI:
                evalMethod.visitFieldInsn(GETSTATIC, MATH_TYPE, "PI", "D");
                break;
            case E:
                evalMethod.visitFieldInsn(GETSTATIC, MATH_TYPE, "E", "D");
                break;
            case BOLTZMANN:
                evalMethod.visitLdcInsn(1.38064852e-23);
                break;
            default:
                throw new IllegalStateException(format("Unrecognized constant: %s", constant.name()));
        }
        return null;
    }

    @Override
    public Void visitParameter(Parameter parameter) {
        evalMethod.visitVarInsn(DLOAD, localVariables.getIndexForParameter(parameter));
        return null;
    }

    @Override
    public Void visitVariable(Variable variable) {
        evalMethod.visitVarInsn(DLOAD, localVariables.getIndexForVariable(variable));
        return null;
    }

    @Override
    public Void visitAbs(Abs abs) {
        visitUnaryExpression(abs, MATH_TYPE, "abs");
        return null;
    }

    @Override
    public Void visitArccos(Arccos arccos) {
        visitUnaryExpression(arccos, MATH_TYPE, "acos");
        return null;
    }

    @Override
    public Void visitArcsin(Arcsin arcsin) {
        visitUnaryExpression(arcsin, MATH_TYPE, "asin");
        return null;
    }

    @Override
    public Void visitArctan(Arctan arctan) {
        visitUnaryExpression(arctan, MATH_TYPE, "atan");
        return null;
    }

    @Override
    public Void visitArsinh(Arsinh arsinh) {
        visitSpecialUnaryExpression(arsinh);
        return null;
    }

    @Override
    public Void visitArcosh(Arcosh arcosh) {
        visitSpecialUnaryExpression(arcosh);
        return null;
    }

    @Override
    public Void visitArtanh(Artanh artanh) {
        visitSpecialUnaryExpression(artanh);
        return null;
    }

    @Override
    public Void visitCos(Cos cos) {
        visitUnaryExpression(cos, MATH_TYPE, "cos");
        return null;
    }

    @Override
    public Void visitCosh(Cosh cosh) {
        visitUnaryExpression(cosh, MATH_TYPE, "cosh");
        return null;
    }

    @Override
    public Void visitExp(Exp exp) {
        visitUnaryExpression(exp, MATH_TYPE, "exp");
        return null;
    }

    @Override
    public Void visitLn(Ln ln) {
        visitUnaryExpression(ln, MATH_TYPE, "log");
        return null;
    }

    @Override
    public Void visitParenthesis(Parenthesis parenthesis) {
        parenthesis.acceptExpression(this);
        return null;
    }

    @Override
    public Void visitSin(Sin sin) {
        visitUnaryExpression(sin, MATH_TYPE, "sin");
        return null;
    }

    @Override
    public Void visitSinh(Sinh sinh) {
        visitUnaryExpression(sinh, MATH_TYPE, "sinh");
        return null;
    }

    @Override
    public Void visitTan(Tan tan) {
        visitUnaryExpression(tan, MATH_TYPE, "tan");
        return null;
    }

    @Override
    public Void visitTanh(Tanh tanh) {
        visitUnaryExpression(tanh, MATH_TYPE, "tanh");
        return null;
    }

    @Override
    public Void visitNeg(Neg neg) {

        neg.acceptExpression(this);
        evalMethod.visitInsn(DNEG);

        return null;
    }

    @Override
    public Void visitPos(Pos pos) {
        pos.acceptExpression(this);
        return null;
    }

    @Override
    public Void visitAddition(Addition addition) {
        addition.acceptLeft(this);
        addition.acceptRight(this);
        evalMethod.visitInsn(DADD);
        return null;
    }

    @Override
    public Void visitSubtraction(Subtraction subtraction) {
        subtraction.acceptLeft(this);
        subtraction.acceptRight(this);
        evalMethod.visitInsn(DSUB);
        return null;
    }

    @Override
    public Void visitMultiplication(Multiplication multiplication) {
        multiplication.acceptLeft(this);
        multiplication.acceptRight(this);
        evalMethod.visitInsn(DMUL);
        return null;
    }

    @Override
    public Void visitDivision(Division division) {
        division.acceptLeft(this);
        division.acceptRight(this);
        evalMethod.visitInsn(DDIV);
        return null;
    }

    @Override
    public Void visitBinomial(Binomial binomial) {

        binomial.getN().accept(this);
        evalMethod.visitInsn(D2I);

        binomial.getK().accept(this);
        evalMethod.visitInsn(D2I);

        evalMethod.visitMethodInsn(INVOKESTATIC, "org/apache/commons/math3/util/CombinatoricsUtils", "binomialCoefficient", "(II)J", false);
        evalMethod.visitInsn(L2D);

        return null;
    }

    @Override
    public Void visitFaculty(Faculty faculty) {

        faculty.getIntExpression().accept(this);
        evalMethod.visitInsn(D2I);

        evalMethod.visitMethodInsn(INVOKESTATIC, "org/apache/commons/math3/util/CombinatoricsUtils", "factorial", "(I)J", false);
        evalMethod.visitInsn(L2D);

        return null;
    }

    @Override
    public Void visitInt(Int i) {
        evalMethod.visitLdcInsn((double)i.getValue());
        return null;
    }

    @Override
    public Void visitDoub(Doub doub) {
        evalMethod.visitLdcInsn(doub.getValue());
        return null;
    }

    @Override
    public Void visitPower(Power power) {
        power.acceptLeft(this);
        power.acceptRight(this);
        evalMethod.visitMethodInsn(INVOKESTATIC, MATH_TYPE, "pow", "(DD)D", false);
        return null;
    }

    @Override
    public Void visitRound(Round round) {
        // Visiting the expression pushes the result (of type double) on the stack
        round.acceptExpression(this);
        evalMethod.visitMethodInsn(INVOKESTATIC, MATH_TYPE, "round", "(D)J", false);
        evalMethod.visitInsn(L2D);
        return null;
    }

    @Override
    public Void visitFunctionsWrapper(FunctionsWrapper functionsWrapper) {
        functionsWrapper.getFunctions().forEach(f -> f.accept(this));
        return null;
    }

    @Override
    public Void visitSqrt(Sqrt sqrt) {
        visitUnaryExpression(sqrt, MATH_TYPE, "sqrt");
        return null;
    }

}
