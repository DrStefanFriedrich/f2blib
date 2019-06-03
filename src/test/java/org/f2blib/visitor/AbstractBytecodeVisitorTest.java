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
import org.f2blib.impl.FunctionEvaluation;
import org.junit.Before;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public abstract class AbstractBytecodeVisitorTest {

    protected BytecodeVisitor bytecodeVisitor;

    private ValidationVisitor validationVisitor;

    @Before
    public void setup() {
        validationVisitor = new ValidationVisitor();
    }

    protected FunctionDefinition createFunctionDefinition(String functionName, Function... functions) {
        return new FunctionDefinition(functionName, new FunctionBody(new Functions(new HashSet<>(Arrays.asList(functions)))));
    }

    protected FunctionEvaluation generateClass(FunctionDefinition fd) {
        try {

            fd.accept(validationVisitor);

            bytecodeVisitor = new BytecodeVisitor(validationVisitor.getBytecodeNavigator());
            fd.accept(bytecodeVisitor);

            return bytecodeVisitor.generate().newInstance();

        } catch (InstantiationException | IllegalAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
