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

import org.junit.Test;

/**
 * Test elementary grammar syntax.
 */
public class SyntaxTest extends AbstractParserTest {

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
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := x_1 + x_2;\n" +
                "    f_2 := p_1 + p_2;\n" +
                END);
    }

    @Test
    public void simpleFunctionWithComments() {
        assertGrammar("" +
                "# This is a function definition!\n" +
                FUNCTION_XYZ_START +
                BEGIN +
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
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_20 := p_5 + p_1 + p_20;\n" +
                "    f_1 := x_1 + x_10;\n" +
                END);
    }

    @Test
    public void definedTwice() {
        // The grammar can't recognize this; must be done by a validator
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_123 := p_5 + p_1 + p_20;\n" +
                "    f_123 := x_1 + x_10;\n" +
                END);
    }

    @Test
    public void indexInFunctionNotANumber() {
        assertWrongGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := p_5 + p_1 + p_20;\n" +
                "    f_u := x_1 + x_10;\n" +
                END);
    }

    @Test
    public void indexInVariableNotANumber() {
        assertWrongGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := p_5 + p_1 + p_20;\n" +
                "    f_u := x_a + x_10;\n" +
                END);
    }

    @Test
    public void indexInParameterNotANumber() {
        assertWrongGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := p_x + p_1 + p_20;\n" +
                "    f_u := x_1 + x_10;\n" +
                END);
    }

    @Test
    public void parenthesis() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := (x_1 + x_2)*73;\n" +
                END);
    }

    @Test
    public void unaryPlus() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := +x_1;\n" +
                END);
    }

    @Test
    public void absoluteValue() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := abs(x_1);\n" +
                END);
    }

    @Test
    public void absoluteValueWithoutParenthesis() {
        // Only pretty printing works this way
        assertWrongGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := abs x_1;\n" +
                END);
    }

    @Test
    public void coshWithoutArgument() {
        assertWrongGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := cosh();\n" +
                END);
    }

    @Test
    public void multipleParenthesis() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := (sin(cosh(cos((x_1)))));\n" +
                END);
    }

    @Test
    public void missingClosingParenthesis() {
        assertWrongGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := (sin(cosh(cos((x_1))));\n" +
                END);
    }

    @Test
    public void additionalStartingParenthesis() {
        assertWrongGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := (sin((cosh(cos((x_1)))));\n" +
                END);
    }

    @Test
    public void pi() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := 3*pi+7;\n" +
                END);
    }

}