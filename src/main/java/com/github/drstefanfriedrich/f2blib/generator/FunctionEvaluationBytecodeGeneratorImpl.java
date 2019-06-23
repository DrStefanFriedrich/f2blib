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

package com.github.drstefanfriedrich.f2blib.generator;

import com.google.common.annotations.VisibleForTesting;
import com.github.drstefanfriedrich.f2blib.ast.FunctionDefinition;
import com.github.drstefanfriedrich.f2blib.exception.BytecodeGenerationException;
import com.github.drstefanfriedrich.f2blib.impl.FunctionEvaluation;
import com.github.drstefanfriedrich.f2blib.visitor.*;

import java.lang.reflect.InvocationTargetException;

public class FunctionEvaluationBytecodeGeneratorImpl implements FunctionEvaluationBytecodeGenerator {

    @Override
    public FunctionEvaluation generateAndInstantiate(FunctionDefinition functionDefinition) {

        ValidationVisitor validationVisitor = new ValidationVisitorImpl();
        LocalVariables localVariables = validationVisitor.getLocalVariables();
        SpecialFunctionsUsage specialFunctionsUsage = validationVisitor.getSpecialFunctionsUsage();

        StackDepthVisitor stackDepthVisitor = new StackDepthVisitorImpl();

        BytecodeVisitor bytecodeVisitor = new BytecodeVisitorImpl(localVariables, specialFunctionsUsage, stackDepthVisitor);

        return generateAndInstantiate(functionDefinition, validationVisitor, bytecodeVisitor, stackDepthVisitor);
    }

    @VisibleForTesting
    protected FunctionEvaluation generateAndInstantiate(FunctionDefinition functionDefinition,
                                                        ValidationVisitor validationVisitor,
                                                        BytecodeVisitor bytecodeVisitor,
                                                        StackDepthVisitor stackDepthVisitor) {
        try {

            functionDefinition.accept(validationVisitor);
            functionDefinition.accept(stackDepthVisitor);
            functionDefinition.accept(bytecodeVisitor);

            Class<? extends FunctionEvaluation> clazz = bytecodeVisitor.generate();

            return instantiate(clazz);

        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            throw new BytecodeGenerationException("Cannot instantiate class", e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getTargetException();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new BytecodeGenerationException("Exception thrown from constructor invocation", cause);
        }
    }

    @VisibleForTesting
    protected FunctionEvaluation instantiate(Class<? extends FunctionEvaluation> clazz) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        return clazz.getConstructor((Class<?>[]) null).newInstance();
    }

}