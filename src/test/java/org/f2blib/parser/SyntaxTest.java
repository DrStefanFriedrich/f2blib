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

package org.f2blib.parser;

import org.f2blib.FunctionsListener;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertTrue;

/**
 * Test elementary grammar syntax.
 */
public class SyntaxTest {

    private final FunctionParser parser = new AntlrFunctionParser();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private FunctionsListener listener;

    private void assertGrammar(String functionDefinition) {
        parser.applyListener(functionDefinition, listener);
        assertTrue(true);
    }

    private void assertWrongGrammar(String functionDefinition) {
        exception.expect(RuntimeException.class);
        parser.applyListener(functionDefinition, listener);
        assertTrue("The parser wrongly recognized this rule and did not throw an exception", false);
    }

    @Before
    public void setup() {
        listener = new BytecodeGeneratingFunctionsListener();
    }

    @Test
    public void wrongFunctionDefinition() {
        assertWrongGrammar("y");
    }

    @Test
    public void oneLineCommentEmpty() {
        assertGrammar("#\nfunction x;begin f_1:=x_1; end");
    }

    @Test
    public void oneLineCommentEmptyWithNewLine() {
        assertGrammar("#\nfunction f;begin f_1:=x_1; end");
    }

    @Test
    public void oneLineCommentNonEmpty() {
        assertGrammar("# Hello, world!\nfunction f;begin f_8:=x_8; end");
    }

    @Test
    public void oneLineCommentNonEmptyWithNewLine() {
        assertGrammar("# Hello, world!\nfunction f;begin f_8:=x_8; end");
    }

    @Test
    public void twoLineCommentEmpty() {
        assertGrammar("#\r\n#\nfunction x;begin f_1:=x_4; end");
    }

    @Test
    public void twoLineCommentEmptyWithNewLine() {
        assertGrammar("#\n\r#\nfunction abc;begin f_1:=x_1; end");
    }

    @Test
    public void twoLineCommentNonEmpty() {
        assertGrammar("# Hello, world!\n# Hello, again!\nfunction f;begin f_1:=x_1^x_2; end");
    }

    @Test
    public void twoLineCommentNonEmptyWithNewLine() {
        assertGrammar("# Hello, world!\n#x\n\nfunction f;begin f_8:=x_8; end");
    }

    @Test
    public void simpleFunction() {
        assertGrammar("" +
                "function a.b.c.Xyz;\n" +
                "begin\n" +
                "    f_1 := x_1 + x_2;\n" +
                "    f_2 := p_1 + p_2;\n" +
                "end");
    }

    @Test
    public void simpleFunctionWithComments() {
        assertGrammar("" +
                "# This is a function definition!\n" +
                "function a.b.c.Xyz;\n" +
                "begin\n" +
                "    # Some more comments\n" +
                "    f_1 := x_1 + x_2;\n" +
                "  # Comment\r\r\n\n" +
                "    f_2 := p_1 + p_2;\n" +
                "    # Comment at the end\n" +
                "end # Comment after a 'statement'\n" +
                "# One more comment\n\n");
    }

    @Test
    public void functionWithDiscontinuousVariables() {
        assertGrammar("" +
                "function a.b.c.Xyz;\n" +
                "begin\n" +
                "    f_20 := p_5 + p_1 + p_20;\n" +
                "    f_1 := x_1 + x_10;\n" +
                "end");
    }

    @Test
    public void definedTwice() {
        assertWrongGrammar("" +
                "function a.b.c.Xyz;\n" +
                "begin\n" +
                "    f_123 := p_5 + p_1 + p_20;\n" +
                "    f_123 := x_1 + x_10;\n" +
                "end");
    }

    @Test
    public void indexInFunctionNotANumber() {
        assertWrongGrammar("" +
                "function a.b.c.Xyz;\n" +
                "begin\n" +
                "    f_1 := p_5 + p_1 + p_20;\n" +
                "    f_u := x_1 + x_10;\n" +
                "end");
    }

    @Test
    public void indexInVariableNotANumber() {
        assertWrongGrammar("" +
                "function a.b.c.Xyz;\n" +
                "begin\n" +
                "    f_1 := p_5 + p_1 + p_20;\n" +
                "    f_u := x_a + x_10;\n" +
                "end");
    }

    @Test
    public void indexInParameterNotANumber() {
        assertWrongGrammar("" +
                "function a.b.c.Xyz;\n" +
                "begin\n" +
                "    f_1 := p_x + p_1 + p_20;\n" +
                "    f_u := x_1 + x_10;\n" +
                "end");
    }

    @Test
    public void variablePrecendence() {
        assertGrammar("" +
                "function a.b.c.Xyz;\n" +
                "begin\n" +
                "    f_ 1 := x_ 1 + pi + e;\n" +
                "end");
    }

    @Test
    public void parenthesis() {
        assertGrammar("" +
                "function a.b.c.Xyz;\n" +
                "begin\n" +
                "    f_1 := (x_1 + x_2)*73;\n" +
                "end");
    }

    @Test
    public void unaryPlus() {
        assertGrammar("" +
                "function a.b.c.Xyz;\n" +
                "begin\n" +
                "    f_1 := +x_1;\n" +
                "end");
    }

    @Test
    public void absoluteValue() {
        assertGrammar("" +
                "function a.b.c.Xyz;\n" +
                "begin\n" +
                "    f_1 := abs(x_1);\n" +
                "end");
    }

    @Test
    public void laguerre() {
        assertGrammar("" +
                "function a.b.c.Xyz;\n" +
                "begin\n" +
                "    f_1 := laguerre(7, x_1);\n" +
                "end");
    }

    @Test
    public void pi() {
        assertGrammar("" +
                "function a.b.c.Xyz;\n" +
                "begin\n" +
                "    f_1 := 3*pi+7;\n" +
                "end");
    }

}