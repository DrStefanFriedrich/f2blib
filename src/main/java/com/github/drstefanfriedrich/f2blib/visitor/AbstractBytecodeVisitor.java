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

import com.github.drstefanfriedrich.f2blib.ast.Arcosh;
import com.github.drstefanfriedrich.f2blib.ast.Arsinh;
import com.github.drstefanfriedrich.f2blib.ast.Artanh;
import com.github.drstefanfriedrich.f2blib.ast.UnaryExpression;
import com.github.drstefanfriedrich.f2blib.exception.BytecodeGenerationException;
import com.github.drstefanfriedrich.f2blib.impl.FunctionEvaluation;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;

import static java.lang.String.format;
import static org.objectweb.asm.Opcodes.*;

/**
 * Base class for the byte code visitor. This class is used to keep the actual visitor
 * a little bit cleaner.
 */
public abstract class AbstractBytecodeVisitor implements BytecodeVisitor {

    public static final String ARSINH = "ARSINH";

    public static final String ARCOSH = "ARCOSH";

    public static final String ARTANH = "ARTANH";

    public static final String ARSINH_TYPE = "Lorg/apache/commons/math3/analysis/function/Asinh;";

    public static final String ARCOSH_TYPE = "Lorg/apache/commons/math3/analysis/function/Acosh;";

    public static final String ARTANH_TYPE = "Lorg/apache/commons/math3/analysis/function/Atanh;";

    public static final String ARSINH_BIN_CLASS = "org/apache/commons/math3/analysis/function/Asinh";

    public static final String ARCOSH_BIN_CLASS = "org/apache/commons/math3/analysis/function/Acosh";

    public static final String ARTANH_BIN_CLASS = "org/apache/commons/math3/analysis/function/Atanh";

    private static final String INIT_TYPE = "<init>";

    private static final String VALUE_METHOD_NAME = "value";

    final LocalVariables localVariables;

    final SpecialFunctionsUsage specialFunctionsUsage;

    final StackDepthVisitor stackDepthVisitor;

    private final ClassWriter cw = new ClassWriter(0);

    final MethodVisitor evalMethod = cw.visitMethod(ACC_PUBLIC, "eval", "([D[D[D)V", null, null);

    String className;

    AbstractBytecodeVisitor(LocalVariables localVariables, SpecialFunctionsUsage specialFunctionsUsage,
                            StackDepthVisitor stackDepthVisitor) {
        this.localVariables = localVariables;
        this.specialFunctionsUsage = specialFunctionsUsage;
        this.stackDepthVisitor = stackDepthVisitor;
    }

    @Override
    public Class<? extends FunctionEvaluation> generate() {

        byte[] bytecode = generateBytecode();

        writeClassFileToDisk(bytecode);

        @SuppressWarnings("unchecked")
        Class<? extends FunctionEvaluation> result = (Class<? extends FunctionEvaluation>) new ClassLoader() {
            Class<?> defineClass(byte[] bytes) {
                return super.defineClass(className, bytes, 0, bytes.length);
            }
        }.defineClass(bytecode);

        return result;
    }

    private void writeClassFileToDisk(byte[] bytecode) {
        if (!debuggingModeEnabled()) {
            return;
        }

        // For better debugging purposes write the class to disk
        try (FileOutputStream fos = new FileOutputStream(className + ".class")) {
            fos.write(bytecode);
        } catch (IOException e) {
            throw new BytecodeGenerationException("Could not write class file to disk", e);
        }
    }

    private boolean debuggingModeEnabled() {
        return Boolean.valueOf(System.getProperty("com.github.drstefanfriedrich.f2blib.debugging", Boolean.FALSE.toString()));
    }

    private byte[] generateBytecode() {
        cw.visitEnd();
        return cw.toByteArray();
    }

    void generateClassHeader() {
        cw.visit(52, ACC_PUBLIC + ACC_SUPER,
                className.replaceAll("\\.", "/"), null, "java/lang/Object",
                new String[]{FunctionEvaluation.class.getName().replaceAll("\\.", "/")});
    }

    void generateStaticFields() {

        FieldVisitor fv;

        if (specialFunctionsUsage.isArsinhUsed()) {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, ARSINH, ARSINH_TYPE, null, null);
            fv.visitEnd();
        }

        if (specialFunctionsUsage.isArcoshUsed()) {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, ARCOSH, ARCOSH_TYPE, null, null);
            fv.visitEnd();
        }

        if (specialFunctionsUsage.isArtanhUsed()) {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, ARTANH, ARTANH_TYPE, null, null);
            fv.visitEnd();
        }
    }

    void generateStaticInitializers() {

        MethodVisitor mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
        mv.visitCode();

        if (specialFunctionsUsage.isArsinhUsed()) {
            mv.visitTypeInsn(NEW, ARSINH_BIN_CLASS);
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, ARSINH_BIN_CLASS, INIT_TYPE, "()V", false);
            mv.visitFieldInsn(PUTSTATIC, className.replaceAll("\\.", "/"), ARSINH, ARSINH_TYPE);
        }

        if (specialFunctionsUsage.isArcoshUsed()) {
            mv.visitTypeInsn(NEW, ARCOSH_BIN_CLASS);
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, ARCOSH_BIN_CLASS, INIT_TYPE, "()V", false);
            mv.visitFieldInsn(PUTSTATIC, className.replaceAll("\\.", "/"), ARCOSH, ARCOSH_TYPE);
        }

        if (specialFunctionsUsage.isArtanhUsed()) {
            mv.visitTypeInsn(NEW, ARTANH_BIN_CLASS);
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, ARTANH_BIN_CLASS, INIT_TYPE, "()V", false);
            mv.visitFieldInsn(PUTSTATIC, className.replaceAll("\\.", "/"), ARTANH, ARTANH_TYPE);
        }

        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 0);
        mv.visitEnd();
    }

    void generateDefaultConstructor() {

        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, INIT_TYPE, "()V", null, null);

        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", INIT_TYPE, "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    void prepareParameters() {

        localVariables.parameterIndexIterator().forEachRemaining(entry -> {
            Integer parameterIndex = entry.getKey();
            Integer indexLocalVariables = entry.getValue();

            evalMethod.visitVarInsn(ALOAD, 1); // push p[] on the operand stack
            evalMethod.visitIntInsn(BIPUSH, parameterIndex);
            evalMethod.visitInsn(DALOAD);
            evalMethod.visitVarInsn(DSTORE, indexLocalVariables);
        });
    }

    void prepareVariables() {

        localVariables.variableIndexIterator().forEachRemaining(entry -> {
            Integer variableIndex = entry.getKey();
            Integer indexLocalVariables = entry.getValue();

            evalMethod.visitVarInsn(ALOAD, 2); // push x[] on the operand stack
            evalMethod.visitIntInsn(BIPUSH, variableIndex);
            evalMethod.visitInsn(DALOAD);
            evalMethod.visitVarInsn(DSTORE, indexLocalVariables);
        });
    }

    void visitUnaryExpression(UnaryExpression element, String binaryClassName, String methodName) {

        // Visiting the expression pushes the result (of type double) on the stack
        element.acceptExpression(this);

        evalMethod.visitMethodInsn(INVOKESTATIC, binaryClassName, methodName, "(D)D", false);
    }

    void visitSpecialUnaryExpression(UnaryExpression element) {

        if (element.getClass() == Arsinh.class) {

            evalMethod.visitFieldInsn(GETSTATIC, className.replaceAll("\\.", "/"), ARSINH, ARSINH_TYPE);
            // Visiting the expression pushes the result (of type double) on the stack
            element.acceptExpression(this);
            evalMethod.visitMethodInsn(INVOKEVIRTUAL, ARSINH_BIN_CLASS, VALUE_METHOD_NAME, "(D)D", false);

        } else if (element.getClass() == Arcosh.class) {

            evalMethod.visitFieldInsn(GETSTATIC, className.replaceAll("\\.", "/"), ARCOSH, ARCOSH_TYPE);
            // Visiting the expression pushes the result (of type double) on the stack
            element.acceptExpression(this);
            evalMethod.visitMethodInsn(INVOKEVIRTUAL, ARCOSH_BIN_CLASS, VALUE_METHOD_NAME, "(D)D", false);

        } else if (element.getClass() == Artanh.class) {

            evalMethod.visitFieldInsn(GETSTATIC, className.replaceAll("\\.", "/"), ARTANH, ARTANH_TYPE);
            // Visiting the expression pushes the result (of type double) on the stack
            element.acceptExpression(this);
            evalMethod.visitMethodInsn(INVOKEVIRTUAL, ARTANH_BIN_CLASS, VALUE_METHOD_NAME, "(D)D", false);

        } else {
            throw new BytecodeGenerationException(format("Unrecognized special unary expression: %s", element.getClass().getName()));
        }
    }

}
