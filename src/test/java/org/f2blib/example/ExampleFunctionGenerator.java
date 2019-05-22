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

public class ExampleFunctionGenerator implements Opcodes {

    public static byte[] dump() throws Exception {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(52, ACC_PUBLIC + ACC_SUPER, "org/f2blib/ExampleFunction", null, "java/lang/Object", new String[]{"org/f2blib/FunctionEvaluation"});

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "eval", "([D[D[D)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitInsn(ICONST_0);
            mv.visitInsn(DALOAD);
            mv.visitVarInsn(DSTORE, 4);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitInsn(ICONST_0);
            mv.visitInsn(DALOAD);
            mv.visitVarInsn(DSTORE, 6);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitInsn(ICONST_1);
            mv.visitInsn(DALOAD);
            mv.visitVarInsn(DSTORE, 8);
            mv.visitVarInsn(DLOAD, 8);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "cos", "(D)D", false);
            mv.visitVarInsn(DSTORE, 10);
            mv.visitVarInsn(DLOAD, 6);
            mv.visitVarInsn(DLOAD, 10);
            mv.visitInsn(DMUL);
            mv.visitVarInsn(DSTORE, 12);
            mv.visitVarInsn(DLOAD, 4);
            mv.visitVarInsn(DLOAD, 12);
            mv.visitInsn(DMUL);
            mv.visitVarInsn(DSTORE, 14);
            mv.visitVarInsn(DLOAD, 8);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "sin", "(D)D", false);
            mv.visitVarInsn(DSTORE, 16);
            mv.visitVarInsn(DLOAD, 6);
            mv.visitVarInsn(DLOAD, 16);
            mv.visitInsn(DMUL);
            mv.visitVarInsn(DSTORE, 18);
            mv.visitVarInsn(DLOAD, 4);
            mv.visitVarInsn(DLOAD, 18);
            mv.visitInsn(DMUL);
            mv.visitVarInsn(DSTORE, 20);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitInsn(ICONST_0);
            mv.visitVarInsn(DLOAD, 14);
            mv.visitInsn(DASTORE);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitInsn(ICONST_1);
            mv.visitVarInsn(DLOAD, 20);
            mv.visitInsn(DASTORE);
            mv.visitInsn(RETURN);
            mv.visitMaxs(4, 22);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }
}