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

import org.f2blib.ast.Arcosh;
import org.f2blib.ast.Arsinh;
import org.f2blib.ast.Artanh;
import org.f2blib.ast.UnaryExpression;
import org.f2blib.impl.FunctionEvaluation;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.RETURN;

public abstract class AbstractBytecodeVisitor extends AbstractVisitor {

    protected final ClassWriter cw = new ClassWriter(0);

    protected final MethodVisitor evalMethod = cw.visitMethod(ACC_PUBLIC, "eval", "([D[D[D)V", null, null);

    protected String className;

    protected final BytecodeNavigator navi;

    public AbstractBytecodeVisitor(BytecodeNavigator navi) {
        this.navi = navi;
    }

    public Class<? extends FunctionEvaluation> generate() throws FileNotFoundException, IOException {

        byte[] bytecode = generateBytecode();

        // TODO SF Remove this
        // For better debugging purposes write the class to disk
        try (FileOutputStream fos = new FileOutputStream(className + ".class");) {
            fos.write(bytecode);
        }

        return (Class<? extends FunctionEvaluation>) new ClassLoader() {
            public Class<?> defineClass(byte[] bytes) {
                return super.defineClass(className, bytes, 0, bytes.length);
            }
        }.defineClass(bytecode);
    }

    private byte[] generateBytecode() {
        cw.visitEnd();
        return cw.toByteArray();
    }

    protected void generateClassHeader() {
        cw.visit(52, ACC_PUBLIC + ACC_SUPER,
                className.replaceAll("\\.", "/"), null, "java/lang/Object",
                new String[]{FunctionEvaluation.class.getName().replaceAll("\\.", "/")});
    }

    protected void generateStaticFields() {

        FieldVisitor fv;

        fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "ARSINH", "Lorg/apache/commons/math3/analysis/function/Asinh;", null, null);
        fv.visitEnd();

        fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "ARCOSH", "Lorg/apache/commons/math3/analysis/function/Acosh;", null, null);
        fv.visitEnd();

        fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "ARTANH", "Lorg/apache/commons/math3/analysis/function/Atanh;", null, null);
        fv.visitEnd();

        fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "BOLTZMANN", "D", null, new Double("1.38064852E-23"));
        fv.visitEnd();
    }

    protected void generateStaticInitializers() {

        MethodVisitor mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
        mv.visitCode();
        mv.visitTypeInsn(NEW, "org/apache/commons/math3/analysis/function/Asinh");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "org/apache/commons/math3/analysis/function/Asinh", "<init>", "()V", false);
        mv.visitFieldInsn(PUTSTATIC, className.replaceAll("\\.", "/"), "ARSINH", "Lorg/apache/commons/math3/analysis/function/Asinh;");
        mv.visitTypeInsn(NEW, "org/apache/commons/math3/analysis/function/Acosh");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "org/apache/commons/math3/analysis/function/Acosh", "<init>", "()V", false);
        mv.visitFieldInsn(PUTSTATIC, className.replaceAll("\\.", "/"), "ARCOSH", "Lorg/apache/commons/math3/analysis/function/Acosh;");
        mv.visitTypeInsn(NEW, "org/apache/commons/math3/analysis/function/Atanh");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "org/apache/commons/math3/analysis/function/Atanh", "<init>", "()V", false);
        mv.visitFieldInsn(PUTSTATIC, className.replaceAll("\\.", "/"), "ARTANH", "Lorg/apache/commons/math3/analysis/function/Atanh;");
        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 0);
        mv.visitEnd();
    }

    protected void generateDefaultConstructor() {

        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);

        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    protected void prepareParameters() {

        navi.parameterIndexIterator().forEachRemaining(entry -> {
            Integer parameterIndex = entry.getKey();
            Integer indexLocalVariables = entry.getValue();

            evalMethod.visitVarInsn(ALOAD, 1);
            evalMethod.visitIntInsn(BIPUSH, parameterIndex); // TODO SF
            evalMethod.visitInsn(DALOAD);
            evalMethod.visitVarInsn(DSTORE, indexLocalVariables); // TODO SF
        });
    }

    protected void prepareVariables() {

        navi.variableIndexIterator().forEachRemaining(entry -> {
            Integer variableIndex = entry.getKey();
            Integer indexLocalVariables = entry.getValue();

            evalMethod.visitVarInsn(ALOAD, 2);
            evalMethod.visitIntInsn(BIPUSH, variableIndex);  // TODO SF
            evalMethod.visitInsn(DALOAD);
            evalMethod.visitVarInsn(DSTORE, indexLocalVariables); // TODO SF
        });
    }

    protected void visitUnaryExpression(UnaryExpression element, String binaryClassName, String methodName) {

        // Visiting the expression pushes the result (of type double) on the stack
        element.getExpression().accept(this);

        evalMethod.visitMethodInsn(INVOKESTATIC, binaryClassName, methodName, "(D)D", false);
    }

    protected void visitSpecialUnaryExpression(UnaryExpression element) {

        if (element instanceof Arsinh) {

            evalMethod.visitFieldInsn(GETSTATIC, className.replaceAll("\\.", "/"), "ARSINH", "Lorg/apache/commons/math3/analysis/function/Asinh;");
            // Visiting the expression pushes the result (of type double) on the stack
            element.getExpression().accept(this);
            evalMethod.visitMethodInsn(INVOKEVIRTUAL, className.replaceAll("\\.", "/"), "value", "(D)D", false);

        } else if (element instanceof Arcosh) {

            evalMethod.visitFieldInsn(GETSTATIC, className.replaceAll("\\.", "/"), "ARCOSH", "Lorg/apache/commons/math3/analysis/function/Asinh;");
            // Visiting the expression pushes the result (of type double) on the stack
            element.getExpression().accept(this);
            evalMethod.visitMethodInsn(INVOKEVIRTUAL, className.replaceAll("\\.", "/"), "value", "(D)D", false);

        } else if (element instanceof Artanh) {

            evalMethod.visitFieldInsn(GETSTATIC, className.replaceAll("\\.", "/"), "ARTANH", "Lorg/apache/commons/math3/analysis/function/Asinh;");
            // Visiting the expression pushes the result (of type double) on the stack
            element.getExpression().accept(this);
            evalMethod.visitMethodInsn(INVOKEVIRTUAL, className.replaceAll("\\.", "/"), "value", "(D)D", false);

        } else {
            throw new RuntimeException("TODO SF");
        }
    }

}
