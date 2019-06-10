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

import org.f2blib.ast.Function;
import org.f2blib.ast.FunctionBody;
import org.f2blib.ast.FunctionDefinition;
import org.f2blib.ast.Functions;
import org.f2blib.exception.BytecodeGenerationException;
import org.f2blib.impl.FunctionEvaluation;
import org.junit.Before;

import java.util.Arrays;
import java.util.HashSet;

public abstract class AbstractBytecodeVisitorImplTest {

    private BytecodeVisitorImpl bytecodeVisitor;

    private ValidationVisitorImpl validationVisitor;

    @Before
    public void setup() {
        validationVisitor = new ValidationVisitorImpl();
    }

    FunctionDefinition createFunctionDefinition(String functionName, Function... functions) {
        return new FunctionDefinition(functionName, new FunctionBody(new Functions(new HashSet<>(Arrays.asList(functions)))));
    }

    FunctionEvaluation generateClass(FunctionDefinition fd) {
        try {

            fd.accept(validationVisitor);

            bytecodeVisitor = new BytecodeVisitorImpl(validationVisitor.getLocalVariables());
            fd.accept(bytecodeVisitor);

            return bytecodeVisitor.generate().newInstance();

        } catch (InstantiationException | IllegalAccessException e) {
            throw new BytecodeGenerationException("Error in instantiating class", e);
        }
    }

}
