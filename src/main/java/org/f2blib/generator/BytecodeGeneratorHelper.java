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

package org.f2blib.generator;

import org.f2blib.FunctionEvaluation;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.*;

public class BytecodeGeneratorHelper {

    public Class<? extends FunctionEvaluation> generate(String className) {

        byte[] bytecode = generateBytecode(className);

        return (Class<? extends FunctionEvaluation>) new ClassLoader() {
            public Class<?> defineClass(byte[] bytes) {
                return super.defineClass(className, bytes, 0, bytes.length);
            }
        }.defineClass(bytecode);
    }

    private byte[] generateBytecode(String className) {

        ClassWriter cw = new ClassWriter(0);

        generateClassHeader(className, cw);
        generateDefaultConstructor(cw);
        generateEmptyImplementation(cw);

        cw.visitEnd();

        return cw.toByteArray();
    }

    private void generateClassHeader(String className, ClassWriter cw) {

        cw.visit(52, ACC_PUBLIC + ACC_SUPER,
                className.replaceAll("\\.", "/"), null, "java/lang/Object",
                new String[]{FunctionEvaluation.class.getName().replaceAll("\\.", "/")});

    }

    private void generateDefaultConstructor(ClassWriter cw) {

        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

    }

    private void generateEmptyImplementation(ClassWriter cw) {

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "eval", "([D[D[D)V", null, null);
        mv.visitCode();
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 4);
        mv.visitEnd();

    }

}
