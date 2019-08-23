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

    private static final String DOUBLE_BIFUNC = "(DD)D";

    public BytecodeVisitorImpl(LocalVariables localVariables, SpecialFunctionsUsage specialFunctionsUsage,
                               StackDepthVisitor stackDepthVisitor) {
        super(localVariables, specialFunctionsUsage, stackDepthVisitor);
    }

    @Override
    public Void visit(FunctionDefinition functionDefinition) {

        className = functionDefinition.getName();

        generateClassHeader();
        generateDefaultConstructor();

        functionDefinition.getFunctionBody().accept(this);

        generateStaticFields();
        generateStaticInitializers();

        return null;
    }

    @Override
    public Void visit(FunctionBody functionBody) {

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
    public Void visit(Function function) {

        int index = function.getIndex();
        evalMethod.visitVarInsn(ALOAD, 3); // push y[] on the operand stack
        evalMethod.visitIntInsn(BIPUSH, index);

        // Visiting the function expression pushes the result value on the stack
        function.acceptExpression(this);

        if (!function.evaluatesToDouble()) {
            evalMethod.visitInsn(I2D);
        }

        evalMethod.visitInsn(DASTORE);

        return null;
    }

    @Override
    public Void visit(Constant constant) {
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
    public Void visit(Parameter parameter) {
        if (parameter.getIndexExpression() == null) {
            evalMethod.visitVarInsn(ALOAD, 1); // push p[] on the operand stack
            evalMethod.visitIntInsn(BIPUSH, parameter.getIndex());
            evalMethod.visitInsn(DALOAD);
        } else {
            evalMethod.visitVarInsn(ALOAD, 1); // push p[] on the operand stack
            parameter.getIndexExpression().accept(this);
            evalMethod.visitInsn(ICONST_M1);
            evalMethod.visitInsn(IADD);
            evalMethod.visitInsn(DALOAD);
        }
        return null;
    }

    @Override
    public Void visit(Variable variable) {
        if (variable.getIndexExpression() == null) {
            evalMethod.visitVarInsn(ALOAD, 2); // push x[] on the operand stack
            evalMethod.visitIntInsn(BIPUSH, variable.getIndex());
            evalMethod.visitInsn(DALOAD);
        } else {
            evalMethod.visitVarInsn(ALOAD, 2); // push x[] on the operand stack
            variable.getIndexExpression().accept(this);
            evalMethod.visitInsn(ICONST_M1);
            evalMethod.visitInsn(IADD);
            evalMethod.visitInsn(DALOAD);
        }
        return null;
    }

    @Override
    public Void visit(Abs abs) {
        visitUnaryExpression(abs, MATH_TYPE, "abs");
        return null;
    }

    @Override
    public Void visit(Arccos arccos) {
        visitUnaryExpression(arccos, MATH_TYPE, "acos");
        return null;
    }

    @Override
    public Void visit(Arcsin arcsin) {
        visitUnaryExpression(arcsin, MATH_TYPE, "asin");
        return null;
    }

    @Override
    public Void visit(Arctan arctan) {
        visitUnaryExpression(arctan, MATH_TYPE, "atan");
        return null;
    }

    @Override
    public Void visit(Arsinh arsinh) {
        visitSpecialUnaryExpression(arsinh);
        return null;
    }

    @Override
    public Void visit(Arcosh arcosh) {
        visitSpecialUnaryExpression(arcosh);
        return null;
    }

    @Override
    public Void visit(Artanh artanh) {
        visitSpecialUnaryExpression(artanh);
        return null;
    }

    @Override
    public Void visit(Cos cos) {
        visitUnaryExpression(cos, MATH_TYPE, "cos");
        return null;
    }

    @Override
    public Void visit(Cosh cosh) {
        visitUnaryExpression(cosh, MATH_TYPE, "cosh");
        return null;
    }

    @Override
    public Void visit(Exp exp) {
        visitUnaryExpression(exp, MATH_TYPE, "exp");
        return null;
    }

    @Override
    public Void visit(Ln ln) {
        visitUnaryExpression(ln, MATH_TYPE, "log");
        return null;
    }

    @Override
    public Void visit(Parenthesis parenthesis) {
        parenthesis.acceptExpression(this);
        return null;
    }

    @Override
    public Void visit(Sin sin) {
        visitUnaryExpression(sin, MATH_TYPE, "sin");
        return null;
    }

    @Override
    public Void visit(Sinh sinh) {
        visitUnaryExpression(sinh, MATH_TYPE, "sinh");
        return null;
    }

    @Override
    public Void visit(Tan tan) {
        visitUnaryExpression(tan, MATH_TYPE, "tan");
        return null;
    }

    @Override
    public Void visit(Tanh tanh) {
        visitUnaryExpression(tanh, MATH_TYPE, "tanh");
        return null;
    }

    @Override
    public Void visit(Neg neg) {

        neg.acceptExpression(this);

        if (neg.evaluatesToDouble()) {
            evalMethod.visitInsn(DNEG);
        } else {
            evalMethod.visitInsn(INEG);
        }

        return null;
    }

    @Override
    public Void visit(Pos pos) {
        pos.acceptExpression(this);
        return null;
    }

    @Override
    public Void visit(Addition addition) {

        if (addition.leftEvaluatesToDouble() && addition.rightEvaluatesToDouble()) {

            addition.acceptLeft(this);
            addition.acceptRight(this);
            evalMethod.visitInsn(DADD);

        } else if (!addition.leftEvaluatesToDouble() && addition.rightEvaluatesToDouble()) {

            addition.acceptLeft(this);
            evalMethod.visitInsn(I2D);
            addition.acceptRight(this);
            evalMethod.visitInsn(DADD);

        } else if (addition.leftEvaluatesToDouble() && !addition.rightEvaluatesToDouble()) {

            addition.acceptLeft(this);
            addition.acceptRight(this);
            evalMethod.visitInsn(I2D);
            evalMethod.visitInsn(DADD);

        } else { // !binaryExpression.leftEvaluatesToDouble() && !binaryExpression.rightEvaluatesToDouble()

            addition.acceptLeft(this);
            addition.acceptRight(this);
            evalMethod.visitInsn(IADD);

        }

        return null;
    }

    @Override
    public Void visit(Subtraction subtraction) {

        if (subtraction.leftEvaluatesToDouble() && subtraction.rightEvaluatesToDouble()) {

            subtraction.acceptLeft(this);
            subtraction.acceptRight(this);
            evalMethod.visitInsn(DSUB);

        } else if (!subtraction.leftEvaluatesToDouble() && subtraction.rightEvaluatesToDouble()) {

            subtraction.acceptLeft(this);
            evalMethod.visitInsn(I2D);
            subtraction.acceptRight(this);
            evalMethod.visitInsn(DSUB);

        } else if (subtraction.leftEvaluatesToDouble() && !subtraction.rightEvaluatesToDouble()) {

            subtraction.acceptLeft(this);
            subtraction.acceptRight(this);
            evalMethod.visitInsn(I2D);
            evalMethod.visitInsn(DSUB);

        } else { // !binaryExpression.leftEvaluatesToDouble() && !binaryExpression.rightEvaluatesToDouble()

            subtraction.acceptLeft(this);
            subtraction.acceptRight(this);
            evalMethod.visitInsn(ISUB);

        }

        return null;
    }

    @Override
    public Void visit(Multiplication multiplication) {

        if (multiplication.leftEvaluatesToDouble() && multiplication.rightEvaluatesToDouble()) {

            multiplication.acceptLeft(this);
            multiplication.acceptRight(this);
            evalMethod.visitInsn(DMUL);

        } else if (!multiplication.leftEvaluatesToDouble() && multiplication.rightEvaluatesToDouble()) {

            multiplication.acceptLeft(this);
            evalMethod.visitInsn(I2D);
            multiplication.acceptRight(this);
            evalMethod.visitInsn(DMUL);

        } else if (multiplication.leftEvaluatesToDouble() && !multiplication.rightEvaluatesToDouble()) {

            multiplication.acceptLeft(this);
            multiplication.acceptRight(this);
            evalMethod.visitInsn(I2D);
            evalMethod.visitInsn(DMUL);

        } else { // !binaryExpression.leftEvaluatesToDouble() && !binaryExpression.rightEvaluatesToDouble()

            multiplication.acceptLeft(this);
            multiplication.acceptRight(this);
            evalMethod.visitInsn(IMUL);

        }

        return null;
    }

    @Override
    public Void visit(Division division) {

        if (division.leftEvaluatesToDouble() && division.rightEvaluatesToDouble()) {

            division.acceptLeft(this);
            division.acceptRight(this);
            evalMethod.visitInsn(DDIV);

        } else if (!division.leftEvaluatesToDouble() && division.rightEvaluatesToDouble()) {

            division.acceptLeft(this);
            evalMethod.visitInsn(I2D);
            division.acceptRight(this);
            evalMethod.visitInsn(DDIV);

        } else if (division.leftEvaluatesToDouble() && !division.rightEvaluatesToDouble()) {

            division.acceptLeft(this);
            division.acceptRight(this);
            evalMethod.visitInsn(I2D);
            evalMethod.visitInsn(DDIV);

        } else { // !binaryExpression.leftEvaluatesToDouble() && !binaryExpression.rightEvaluatesToDouble()

            division.acceptLeft(this);
            division.acceptRight(this);
            evalMethod.visitInsn(IDIV);

        }

        return null;
    }

    @Override
    public Void visit(Binomial binomial) {

        binomial.acceptN(this);
        binomial.acceptK(this);

        evalMethod.visitMethodInsn(INVOKESTATIC, COMBINATORICS_UTILS, "binomialCoefficient", "(II)J", false);
        evalMethod.visitInsn(L2I);

        return null;
    }

    @Override
    public Void visit(Faculty faculty) {

        faculty.acceptExpression(this);

        evalMethod.visitMethodInsn(INVOKESTATIC, COMBINATORICS_UTILS, "factorial", "(I)J", false);
        evalMethod.visitInsn(L2I);

        return null;
    }

    @Override
    public Void visit(Int i) {
        evalMethod.visitLdcInsn(i.getValue());
        return null;
    }

    @Override
    public Void visit(Doub doub) {
        evalMethod.visitLdcInsn(doub.getValue());
        return null;
    }

    @Override
    public Void visit(Power power) {

        if (power.leftEvaluatesToDouble() && power.rightEvaluatesToDouble()) {

            power.acceptLeft(this);
            power.acceptRight(this);
            evalMethod.visitMethodInsn(INVOKESTATIC, MATH_TYPE, "pow", DOUBLE_BIFUNC, false);

        } else if (!power.leftEvaluatesToDouble() && power.rightEvaluatesToDouble()) {

            power.acceptLeft(this);
            evalMethod.visitInsn(I2D);
            power.acceptRight(this);
            evalMethod.visitMethodInsn(INVOKESTATIC, MATH_TYPE, "pow", DOUBLE_BIFUNC, false);

        } else if (power.leftEvaluatesToDouble() && !power.rightEvaluatesToDouble()) {

            power.acceptLeft(this);
            power.acceptRight(this);
            evalMethod.visitInsn(I2D);
            evalMethod.visitMethodInsn(INVOKESTATIC, MATH_TYPE, "pow", DOUBLE_BIFUNC, false);

        } else { // !binaryExpression.leftEvaluatesToDouble() && !binaryExpression.rightEvaluatesToDouble()

            power.acceptLeft(this);
            evalMethod.visitInsn(I2D);
            power.acceptRight(this);
            evalMethod.visitInsn(I2D);
            evalMethod.visitMethodInsn(INVOKESTATIC, MATH_TYPE, "pow", DOUBLE_BIFUNC, false);
            evalMethod.visitInsn(D2I);

        }

        return null;
    }

    @Override
    public Void visit(Round round) {
        // Visiting the expression pushes the result (of type double or int) on the stack
        round.acceptExpression(this);
        if (!round.expressionEvaluatesToDouble()) {
            evalMethod.visitInsn(I2D);
        }
        evalMethod.visitMethodInsn(INVOKESTATIC, MATH_TYPE, "round", "(D)J", false);
        evalMethod.visitInsn(L2I);
        return null;
    }

    @Override
    public Void visit(FunctionsWrapper functionsWrapper) {
        functionsWrapper.getFunctions().forEach(f -> f.accept(this));
        functionsWrapper.acceptMarkovShift(this);
        return null;
    }

    @Override
    public Void visit(Sqrt sqrt) {
        visitUnaryExpression(sqrt, MATH_TYPE, "sqrt");
        return null;
    }

    @Override
    public Void visit(NoOp noOp) {
        throw new IllegalStateException("NoOp must not be used");
    }

    @Override
    public Void visit(ForLoop forLoop) {

        IntVar intVar = new IntVar(forLoop.getVariableName());

        Label stepNonZero = new Label();
        Label stepPos = new Label();
        Label stepNeg = new Label();
        Label forEnd = new Label();
        Label throwException = new Label();

        // Calculate the integers on the stack and store them
        forLoop.acceptStart(this);
        evalMethod.visitVarInsn(ISTORE, localVariables.getIndexForIntVar(intVar));
        forLoop.acceptEnd(this);
        evalMethod.visitVarInsn(ISTORE, localVariables.getIndexForForLoopEnd());
        forLoop.acceptStep(this);
        evalMethod.visitVarInsn(ISTORE, localVariables.getIndexForForLoopStep());

        // Check: step != 0
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForForLoopStep());
        evalMethod.visitJumpInsn(IFNE, stepNonZero);

        // step = 0:
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForIntVar(intVar));
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
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForIntVar(intVar));
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForForLoopEnd());
        evalMethod.visitJumpInsn(IF_ICMPGT, forEnd);
        forLoop.acceptFunctionsWrapper(this);
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForIntVar(intVar));
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForForLoopStep());
        evalMethod.visitInsn(IADD);
        evalMethod.visitVarInsn(ISTORE, localVariables.getIndexForIntVar(intVar));
        evalMethod.visitJumpInsn(GOTO, stepPos);

        evalMethod.visitLabel(stepNeg);
        // step < 0:
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForIntVar(intVar));
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForForLoopEnd());
        evalMethod.visitJumpInsn(IF_ICMPLT, forEnd);
        forLoop.acceptFunctionsWrapper(this);
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForIntVar(intVar));
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForForLoopStep());
        evalMethod.visitInsn(IADD);
        evalMethod.visitVarInsn(ISTORE, localVariables.getIndexForIntVar(intVar));
        evalMethod.visitJumpInsn(GOTO, stepNeg);

        evalMethod.visitLabel(forEnd);

        return null;
    }

    @Override
    public Void visit(IntVar intVar) {
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForIntVar(intVar));
        return null;
    }

    /*
     * For a better understanding we refer to EvalVisitor.visit.
     */
    @Override
    public Void visit(MarkovShift markovShift) {

        Label offsetGeZero = new Label();
        Label nMinusOffsetGeM = new Label();
        Label forLoopMove = new Label();
        Label forLoopMoveEnd = new Label();
        Label forLoopCopy = new Label();
        Label forLoopCopyEnd = new Label();

        // Calculate 'offset' and store it into a local variable
        markovShift.getOffset().accept(this);
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

    @Override
    public Void visit(Sum sum) {

        Label loop = new Label();
        Label end = new Label();

        boolean evaluatesToDouble = sum.evaluatesToDouble();
        IntVar intVar = new IntVar(sum.getVariableName());
        int sumIndex = localVariables.getSumIndex();

        sum.acceptStart(this);
        evalMethod.visitVarInsn(ISTORE, localVariables.getIndexForIntVar(intVar));
        sum.acceptEnd(this);
        if (evaluatesToDouble) {
            evalMethod.visitInsn(DCONST_0);
            evalMethod.visitVarInsn(DSTORE, sumIndex);
        } else {
            evalMethod.visitInsn(ICONST_0);
            evalMethod.visitVarInsn(ISTORE, sumIndex);
        }

        evalMethod.visitLabel(loop);
        evalMethod.visitInsn(DUP);
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForIntVar(intVar));
        evalMethod.visitJumpInsn(IF_ICMPLT, end);
        sum.acceptInner(this);
        if (evaluatesToDouble) {
            evalMethod.visitVarInsn(DLOAD, sumIndex);
            evalMethod.visitInsn(DADD);
            evalMethod.visitVarInsn(DSTORE, sumIndex);
        } else {
            evalMethod.visitVarInsn(ILOAD, sumIndex);
            evalMethod.visitInsn(IADD);
            evalMethod.visitVarInsn(ISTORE, sumIndex);
        }
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForIntVar(intVar));
        evalMethod.visitInsn(ICONST_1);
        evalMethod.visitInsn(IADD);
        evalMethod.visitVarInsn(ISTORE, localVariables.getIndexForIntVar(intVar));
        evalMethod.visitJumpInsn(GOTO, loop);

        evalMethod.visitLabel(end);
        evalMethod.visitInsn(POP);
        if (evaluatesToDouble) {
            evalMethod.visitVarInsn(DLOAD, sumIndex);
        } else {
            evalMethod.visitVarInsn(ILOAD, sumIndex);
        }

        return null;
    }

    @Override
    public Void visit(Prod prod) {

        Label loop = new Label();
        Label end = new Label();

        boolean evaluatesToDouble = prod.evaluatesToDouble();
        IntVar intVar = new IntVar(prod.getVariableName());
        int prodIndex = localVariables.getProdIndex();

        prod.acceptStart(this);
        evalMethod.visitVarInsn(ISTORE, localVariables.getIndexForIntVar(intVar));
        prod.acceptEnd(this);
        if (evaluatesToDouble) {
            evalMethod.visitInsn(DCONST_1);
            evalMethod.visitVarInsn(DSTORE, prodIndex);
        } else {
            evalMethod.visitInsn(ICONST_1);
            evalMethod.visitVarInsn(ISTORE, prodIndex);
        }

        evalMethod.visitLabel(loop);
        evalMethod.visitInsn(DUP);
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForIntVar(intVar));
        evalMethod.visitJumpInsn(IF_ICMPLT, end);
        prod.acceptInner(this);
        if (evaluatesToDouble) {
            evalMethod.visitVarInsn(DLOAD, prodIndex);
            evalMethod.visitInsn(DMUL);
            evalMethod.visitVarInsn(DSTORE, prodIndex);
        } else {
            evalMethod.visitVarInsn(ILOAD, prodIndex);
            evalMethod.visitInsn(IMUL);
            evalMethod.visitVarInsn(ISTORE, prodIndex);
        }
        evalMethod.visitVarInsn(ILOAD, localVariables.getIndexForIntVar(intVar));
        evalMethod.visitInsn(ICONST_1);
        evalMethod.visitInsn(IADD);
        evalMethod.visitVarInsn(ISTORE, localVariables.getIndexForIntVar(intVar));
        evalMethod.visitJumpInsn(GOTO, loop);

        evalMethod.visitLabel(end);
        evalMethod.visitInsn(POP);
        if (evaluatesToDouble) {
            evalMethod.visitVarInsn(DLOAD, prodIndex);
        } else {
            evalMethod.visitVarInsn(ILOAD, prodIndex);
        }

        return null;
    }

}
