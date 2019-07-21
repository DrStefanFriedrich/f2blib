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
import org.objectweb.asm.Label;

import static java.lang.String.format;
import static org.objectweb.asm.Opcodes.*;

/**
 * Generate bytecode for the Java virtual machine by visiting AST nodes.
 */
public class BytecodeVisitorImpl extends AbstractBytecodeVisitor {

    private static final String MATH_TYPE = "java/lang/Math";

    private static final String COMBINATORICS_UTILS = "org/apache/commons/math3/util/CombinatoricsUtils";

    private static final String ILLEGAL_ARGUMENT_EXCEPTION = "java/lang/IllegalArgumentException";

    private static final String INIT = "<init>";

    private static final String STRING_TYPE = "(Ljava/lang/String;)V";

    public BytecodeVisitorImpl(LocalVariables localVariables, SpecialFunctionsUsage specialFunctionsUsage,
                               StackDepthVisitor stackDepthVisitor) {
        super(localVariables, specialFunctionsUsage, stackDepthVisitor);
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

        if (functionBody.isForLoop()) {
            functionBody.getForLoop().accept(this);
        } else {
            functionBody.getFunctionsWrapper().accept(this);
        }

        evalMethod.visitMaxs(stackDepthVisitor.getMaxStackDepth(), localVariables.getMaxLocals());
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
        evalMethod.visitVarInsn(ALOAD, 1); // push p[] on the operand stack
        evalMethod.visitIntInsn(BIPUSH, parameter.getIndex());
        evalMethod.visitInsn(DALOAD);
        return null;
    }

    @Override
    public Void visitVariable(Variable variable) {
        evalMethod.visitVarInsn(ALOAD, 2); // push x[] on the operand stack
        evalMethod.visitIntInsn(BIPUSH, variable.getIndex());
        evalMethod.visitInsn(DALOAD);
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

        binomial.acceptN(this);
        evalMethod.visitInsn(D2I);

        binomial.acceptK(this);
        evalMethod.visitInsn(D2I);

        evalMethod.visitMethodInsn(INVOKESTATIC, COMBINATORICS_UTILS, "binomialCoefficient", "(II)J", false);
        evalMethod.visitInsn(L2D);

        return null;
    }

    @Override
    public Void visitFaculty(Faculty faculty) {

        faculty.acceptIntExpression(this);
        evalMethod.visitInsn(D2I);

        evalMethod.visitMethodInsn(INVOKESTATIC, COMBINATORICS_UTILS, "factorial", "(I)J", false);
        evalMethod.visitInsn(L2D);

        return null;
    }

    @Override
    public Void visitInt(Int i) {
        evalMethod.visitLdcInsn((double) i.getValue());
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
        functionsWrapper.acceptMarkovShift(this);
        return null;
    }

    @Override
    public Void visitSqrt(Sqrt sqrt) {
        visitUnaryExpression(sqrt, MATH_TYPE, "sqrt");
        return null;
    }

    @Override
    public Void visitNoOp(NoOp noOp) {
        throw new IllegalStateException("NoOp must not be used");
    }

    @Override
    public Void visitForLoop(ForLoop forLoop) {

        Label stepNonZero = new Label();
        Label stepPos = new Label();
        Label stepNeg = new Label();
        Label forEnd = new Label();
        Label throwException = new Label();

        // Calculate the integers on the stack and store them
        forLoop.acceptStart(this);
        evalMethod.visitInsn(D2I);
        evalMethod.visitVarInsn(ISTORE, localVariables.getIndexForForLoopStart());
        forLoop.acceptEnd(this);
        evalMethod.visitInsn(D2I);
        evalMethod.visitVarInsn(ISTORE, localVariables.getIndexForForLoopEnd());
        forLoop.acceptStep(this);
        evalMethod.visitInsn(D2I);
        evalMethod.visitVarInsn(ISTORE, localVariables.getIndexForForLoopStep());

        // Check: step != 0
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForForLoopStep());
        evalMethod.visitJumpInsn(IFNE, stepNonZero);

        // step = 0:
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForForLoopStart());
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForForLoopEnd());
        evalMethod.visitJumpInsn(IF_ICMPNE, throwException);
        forLoop.acceptFunctionsWrapper(this);
        evalMethod.visitJumpInsn(GOTO, forEnd);

        evalMethod.visitLabel(throwException);
        evalMethod.visitTypeInsn(NEW, ILLEGAL_ARGUMENT_EXCEPTION);
        evalMethod.visitInsn(DUP);
        evalMethod.visitLdcInsn("step must not be 0");
        evalMethod.visitMethodInsn(INVOKESPECIAL, ILLEGAL_ARGUMENT_EXCEPTION, INIT, STRING_TYPE,
                false);
        evalMethod.visitInsn(ATHROW);

        // Check: step < 0
        evalMethod.visitLabel(stepNonZero);
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForForLoopStep());
        evalMethod.visitJumpInsn(IFLT, stepNeg);

        evalMethod.visitLabel(stepPos);
        // step > 0:
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForForLoopStart());
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForForLoopEnd());
        evalMethod.visitJumpInsn(IF_ICMPGT, forEnd);
        forLoop.acceptFunctionsWrapper(this);
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForForLoopStart());
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForForLoopStep());
        evalMethod.visitInsn(IADD);
        evalMethod.visitVarInsn(ISTORE, localVariables.getIndexForForLoopStart());
        evalMethod.visitJumpInsn(GOTO, stepPos);

        evalMethod.visitLabel(stepNeg);
        // step < 0:
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForForLoopStart());
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForForLoopEnd());
        evalMethod.visitJumpInsn(IF_ICMPLT, forEnd);
        forLoop.acceptFunctionsWrapper(this);
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForForLoopStart());
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForForLoopStep());
        evalMethod.visitInsn(IADD);
        evalMethod.visitVarInsn(ISTORE, localVariables.getIndexForForLoopStart());
        evalMethod.visitJumpInsn(GOTO, stepNeg);

        evalMethod.visitLabel(forEnd);

        return null;
    }

    @Override
    public Void visitForVar(ForVar forVar) {
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForForLoopStart());
        evalMethod.visitInsn(I2D);
        return null;
    }

    /*
     * For a better understanding we refer to EvalVisitor.visitMarkovShift.
     */
    @Override
    public Void visitMarkovShift(MarkovShift markovShift) {

        Label offsetGeZero = new Label();
        Label nMinusOffsetGeM = new Label();
        Label forLoopMove = new Label();
        Label forLoopMoveEnd = new Label();
        Label forLoopCopy = new Label();
        Label forLoopCopyEnd = new Label();

        // Calculate 'offset' and store it into a local variable
        evalMethod.visitLdcInsn(markovShift.getOffset());
        evalMethod.visitVarInsn(ISTORE, localVariables.getMarkovShiftOffset());

        // Calculate 'm' and store it into a local variable
        evalMethod.visitVarInsn(ALOAD, 3); // push y[] on the operand stack
        evalMethod.visitInsn(ARRAYLENGTH);
        evalMethod.visitVarInsn(ISTORE, localVariables.getMarkovShiftM());

        // Calculate 'n' and store it into a local variable
        evalMethod.visitVarInsn(ALOAD, 2); // push x[] on the operand stack
        evalMethod.visitInsn(ARRAYLENGTH);
        evalMethod.visitVarInsn(ISTORE, localVariables.getMarkovShiftN());

        // Check 'offset < 0'
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftOffset());
        evalMethod.visitJumpInsn(IFGE, offsetGeZero);
        evalMethod.visitTypeInsn(NEW, ILLEGAL_ARGUMENT_EXCEPTION);
        evalMethod.visitInsn(DUP);
        evalMethod.visitLdcInsn("offset must not be negative");
        evalMethod.visitMethodInsn(INVOKESPECIAL, ILLEGAL_ARGUMENT_EXCEPTION, INIT, STRING_TYPE, false);
        evalMethod.visitInsn(ATHROW);

        // Check 'n - offset < m'
        evalMethod.visitLabel(offsetGeZero);
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftN());
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftOffset());
        evalMethod.visitInsn(ISUB);
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftM());
        evalMethod.visitJumpInsn(IF_ICMPGE, nMinusOffsetGeM);
        evalMethod.visitTypeInsn(NEW, ILLEGAL_ARGUMENT_EXCEPTION);
        evalMethod.visitInsn(DUP);
        evalMethod.visitLdcInsn("x.lenth - offset must be greater or equal than y.length");
        evalMethod.visitMethodInsn(INVOKESPECIAL, ILLEGAL_ARGUMENT_EXCEPTION, INIT, STRING_TYPE, false);
        evalMethod.visitInsn(ATHROW);

        // 'Move to the right'
        evalMethod.visitLabel(nMinusOffsetGeM);
        // Calculate 'start = n - 1'
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftN());
        evalMethod.visitInsn(ICONST_1);
        evalMethod.visitInsn(ISUB);
        evalMethod.visitVarInsn(ISTORE, localVariables.getMarkovShiftStart());
        // Calculate 'end = offset + m'
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftOffset());
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftM());
        evalMethod.visitInsn(IADD);
        evalMethod.visitVarInsn(ISTORE, localVariables.getMarkovShiftEnd());
        // for ...
        evalMethod.visitLabel(forLoopMove);
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftStart());
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftEnd());
        evalMethod.visitJumpInsn(IF_ICMPLT, forLoopMoveEnd);
        // x[start] = x[start - m]
        evalMethod.visitVarInsn(ALOAD, 2); // push x[] on the operand stack
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftStart());
        evalMethod.visitVarInsn(ALOAD, 2); // push x[] on the operand stack
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftStart());
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftM());
        evalMethod.visitInsn(ISUB);
        evalMethod.visitInsn(DALOAD);
        evalMethod.visitInsn(DASTORE);
        // Calculate 'start = start - 1'
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftStart());
        evalMethod.visitInsn(ICONST_1);
        evalMethod.visitInsn(ISUB);
        evalMethod.visitVarInsn(ISTORE, localVariables.getMarkovShiftStart());
        evalMethod.visitJumpInsn(GOTO, forLoopMove);

        evalMethod.visitLabel(forLoopMoveEnd);

        // Copy f into x
        // Calculate 'start = offset'
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftOffset());
        evalMethod.visitVarInsn(ISTORE, localVariables.getMarkovShiftStart());
        // Calculate 'end = offset + m - 1'
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftOffset());
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftM());
        evalMethod.visitInsn(IADD);
        evalMethod.visitInsn(ICONST_1);
        evalMethod.visitInsn(ISUB);
        evalMethod.visitVarInsn(ISTORE, localVariables.getMarkovShiftEnd());
        // for ...
        evalMethod.visitLabel(forLoopCopy);
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftStart());
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftEnd());
        evalMethod.visitJumpInsn(IF_ICMPGT, forLoopCopyEnd);
        // x[start] = y[start - offset]
        evalMethod.visitVarInsn(ALOAD, 2); // push x[] on the operand stack
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftStart());
        evalMethod.visitVarInsn(ALOAD, 3); // push y[] on the operand stack
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftStart());
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftOffset());
        evalMethod.visitInsn(ISUB);
        evalMethod.visitInsn(DALOAD);
        evalMethod.visitInsn(DASTORE);
        // Calculate 'start = start + 1'
        evalMethod.visitVarInsn(ILOAD, localVariables.getMarkovShiftStart());
        evalMethod.visitInsn(ICONST_1);
        evalMethod.visitInsn(IADD);
        evalMethod.visitVarInsn(ISTORE, localVariables.getMarkovShiftStart());
        evalMethod.visitJumpInsn(GOTO, forLoopCopy);

        evalMethod.visitLabel(forLoopCopyEnd);

        return null;
    }

}
