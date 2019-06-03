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

package org.f2blib.example;

import org.objectweb.asm.*;

public class ExampleVisitorDump implements Opcodes {

    public static byte[] dump () throws Exception {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(52, ACC_PUBLIC + ACC_SUPER, "org/f2blib/example/ExampleVisitor", null, "org/f2blib/visitor/AbstractVisitor", null);

        cw.visitInnerClass("org/f2blib/example/ExampleVisitor$1", null, null, ACC_STATIC + ACC_SYNTHETIC);

        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "ARSINH", "Lorg/apache/commons/math3/analysis/function/Asinh;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "ARCOSH", "Lorg/apache/commons/math3/analysis/function/Acosh;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "ARTANH", "Lorg/apache/commons/math3/analysis/function/Atanh;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "BOLTZMANN", "D", null, new Double("1.38064852E-23"));
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "x", "[D", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "y", "[D", null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "org/f2blib/visitor/AbstractVisitor", "<init>", "()V", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitIntInsn(BIPUSH, 10);
            mv.visitIntInsn(NEWARRAY, T_DOUBLE);
            mv.visitFieldInsn(PUTFIELD, "org/f2blib/example/ExampleVisitor", "x", "[D");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitIntInsn(BIPUSH, 10);
            mv.visitIntInsn(NEWARRAY, T_DOUBLE);
            mv.visitFieldInsn(PUTFIELD, "org/f2blib/example/ExampleVisitor", "y", "[D");
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "foobar", "()V", null, null);
            mv.visitCode();
            mv.visitIntInsn(BIPUSH, 12);
            mv.visitVarInsn(ISTORE, 1);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitInsn(I2D);
            mv.visitVarInsn(DSTORE, 2);
            mv.visitIntInsn(SIPUSH, 333);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
            mv.visitVarInsn(ASTORE, 4);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
            mv.visitInsn(I2D);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
            mv.visitVarInsn(ASTORE, 5);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 6);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "visitSin", "(Lorg/f2blib/ast/Sin;)Ljava/lang/Double;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/f2blib/ast/Sin", "getExpression", "()Lorg/f2blib/ast/Expression;", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEINTERFACE, "org/f2blib/ast/Expression", "accept", "(Lorg/f2blib/visitor/Visitor;)Ljava/lang/Object;", true);
            mv.visitTypeInsn(CHECKCAST, "java/lang/Number");
            mv.visitMethodInsn(INVOKESPECIAL, "org/f2blib/example/ExampleVisitor", "toDouble", "(Ljava/lang/Number;)D", false);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "sin", "(D)D", false);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "visitAddition", "(Lorg/f2blib/ast/Addition;)Ljava/lang/Double;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/f2blib/ast/Addition", "getLeft", "()Lorg/f2blib/ast/Expression;", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEINTERFACE, "org/f2blib/ast/Expression", "accept", "(Lorg/f2blib/visitor/Visitor;)Ljava/lang/Object;", true);
            mv.visitTypeInsn(CHECKCAST, "java/lang/Number");
            mv.visitMethodInsn(INVOKESPECIAL, "org/f2blib/example/ExampleVisitor", "toDouble", "(Ljava/lang/Number;)D", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/f2blib/ast/Addition", "getRight", "()Lorg/f2blib/ast/Expression;", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEINTERFACE, "org/f2blib/ast/Expression", "accept", "(Lorg/f2blib/visitor/Visitor;)Ljava/lang/Object;", true);
            mv.visitTypeInsn(CHECKCAST, "java/lang/Number");
            mv.visitMethodInsn(INVOKESPECIAL, "org/f2blib/example/ExampleVisitor", "toDouble", "(Ljava/lang/Number;)D", false);
            mv.visitInsn(DADD);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(5, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "visitVariable", "(Lorg/f2blib/ast/Variable;)Ljava/lang/Double;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, "org/f2blib/example/ExampleVisitor", "x", "[D");
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/f2blib/ast/Variable", "getIndex", "()I", false);
            mv.visitInsn(DALOAD);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "visitConstant", "(Lorg/f2blib/ast/Constant;)Ljava/lang/Double;", null, null);
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, "org/f2blib/example/ExampleVisitor$1", "$SwitchMap$org$f2blib$ast$Constant", "[I");
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/f2blib/ast/Constant", "ordinal", "()I", false);
            mv.visitInsn(IALOAD);
            Label l0 = new Label();
            Label l1 = new Label();
            Label l2 = new Label();
            Label l3 = new Label();
            mv.visitTableSwitchInsn(1, 3, l3, new Label[] { l0, l1, l2 });
            mv.visitLabel(l0);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitLdcInsn(new Double("3.141592653589793"));
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
            mv.visitInsn(ARETURN);
            mv.visitLabel(l1);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitLdcInsn(new Double("2.718281828459045"));
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
            mv.visitInsn(ARETURN);
            mv.visitLabel(l2);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitLdcInsn(new Double("1.3806485177E10"));
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
            mv.visitInsn(ARETURN);
            mv.visitLabel(l3);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitTypeInsn(NEW, "java/lang/RuntimeException");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("TODO SF");
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/RuntimeException", "<init>", "(Ljava/lang/String;)V", false);
            mv.visitInsn(ATHROW);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PRIVATE, "toDouble", "(Ljava/lang/Number;)D", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Number", "doubleValue", "()D", false);
            mv.visitInsn(DRETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "visitVariable", "(Lorg/f2blib/ast/Variable;)Ljava/lang/Object;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/f2blib/example/ExampleVisitor", "visitVariable", "(Lorg/f2blib/ast/Variable;)Ljava/lang/Double;", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "visitSin", "(Lorg/f2blib/ast/Sin;)Ljava/lang/Object;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/f2blib/example/ExampleVisitor", "visitSin", "(Lorg/f2blib/ast/Sin;)Ljava/lang/Double;", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "visitConstant", "(Lorg/f2blib/ast/Constant;)Ljava/lang/Object;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/f2blib/example/ExampleVisitor", "visitConstant", "(Lorg/f2blib/ast/Constant;)Ljava/lang/Double;", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "visitAddition", "(Lorg/f2blib/ast/Addition;)Ljava/lang/Object;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/f2blib/example/ExampleVisitor", "visitAddition", "(Lorg/f2blib/ast/Addition;)Ljava/lang/Double;", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, "org/apache/commons/math3/analysis/function/Asinh");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "org/apache/commons/math3/analysis/function/Asinh", "<init>", "()V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/f2blib/example/ExampleVisitor", "ARSINH", "Lorg/apache/commons/math3/analysis/function/Asinh;");
            mv.visitTypeInsn(NEW, "org/apache/commons/math3/analysis/function/Acosh");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "org/apache/commons/math3/analysis/function/Acosh", "<init>", "()V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/f2blib/example/ExampleVisitor", "ARCOSH", "Lorg/apache/commons/math3/analysis/function/Acosh;");
            mv.visitTypeInsn(NEW, "org/apache/commons/math3/analysis/function/Atanh");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "org/apache/commons/math3/analysis/function/Atanh", "<init>", "()V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/f2blib/example/ExampleVisitor", "ARTANH", "Lorg/apache/commons/math3/analysis/function/Atanh;");
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 0);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }
}
