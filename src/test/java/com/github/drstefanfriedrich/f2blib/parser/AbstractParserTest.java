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

package com.github.drstefanfriedrich.f2blib.parser;

import com.github.drstefanfriedrich.f2blib.ast.FunctionDefinition;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import com.github.drstefanfriedrich.f2blib.ast.Expression;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public abstract class AbstractParserTest {

    protected static final String FUNCTION_XYZ_START = "function a.b.c.Xyz;\n";

    protected static final String BEGIN = "begin\n";

    protected static final String END = "end\n";

    public static final String NO_FUNCTION_SPECIFIED = "No function specified";

    protected final FunctionParser parser = new AntlrFunctionParser();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    protected void assertGrammar(String functionDefinition) {

        parser.parse(functionDefinition);

        assertTrue(true);
    }

    protected void assertWrongGrammar(String functionDefinition) {

        exception.expect(ParseCancellationException.class);

        parser.parse(functionDefinition);

        fail("The parser wrongly recognized this rule and did not throw an exception");
    }

    protected void assertAST(String functionDefinition, Expression expression) {

        Expression ex = parser.parse(functionDefinition).getFunctionBody()
                .getFunctionsWrapper().getFunctions().stream()
                .findFirst().orElseThrow(() -> new IllegalStateException(NO_FUNCTION_SPECIFIED))
                .getExpression();

        assertThat(ex, is(expression));
    }

    protected void assertWrongAST(String functionDefinition, String exceptionMessage) {

        exception.expect(ParseCancellationException.class);
        exception.expectMessage(startsWith(exceptionMessage));

        parser.parse(functionDefinition).getFunctionBody()
                .getFunctionsWrapper().getFunctions().stream()
                .findFirst().orElseThrow(() -> new IllegalStateException(NO_FUNCTION_SPECIFIED))
                .getExpression();
    }

    protected void assertAST(String functionDefinition, FunctionDefinition functionDefinitionToCheck) {

        FunctionDefinition fd = parser.parse(functionDefinition);

        assertThat(fd, is(functionDefinitionToCheck));
    }

}
