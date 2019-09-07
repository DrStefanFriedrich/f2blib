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
    public void euler() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := euler;\n" +
                END);
    }

    @Test
    public void variableE() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := e;\n" +
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

    @Test
    public void forLoop() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "for i from round(p_1) to round(p_2) step round(p_3);\n" +
                BEGIN +
                "    f_1 := pi + i;\n" +
                END +
                END);
    }

    @Test
    public void forLoopOtherVariableName() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "for k from round(p_1) to round(p_2) step round(p_3);\n" +
                BEGIN +
                "    f_1 := pi + k;\n" +
                END +
                END);
    }

    @Test
    public void forLoopWithoutRound() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "for k from 1 to 20 step 10;\n" +
                BEGIN +
                "    f_1 := pi + k;\n" +
                END +
                END);
    }

    @Test
    public void forLoopMissingSemicolon() {
        assertWrongGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "for i from round(p_1) to round(p_2) step round(p_3)\n" +
                BEGIN +
                "    f_1 := pi + i;\n" +
                END +
                END);
    }

    @Test
    public void forLoopWithComplexExpressions() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "for i from 2^4 to 10! step round(5*sin(2));\n" +
                BEGIN +
                "    f_1 := pi + i;\n" +
                END +
                END);
    }

    @Test
    public void nestedForLoop() {
        assertWrongGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "for i from round(p_1) to round(p_2) step round(p_3);\n" +
                BEGIN +
                "for i from round(p_1) to round(p_2) step round(p_3);\n" +
                BEGIN +
                "    f_1 := pi + i;\n" +
                END +
                END +
                END);
    }

    @Test
    public void normalFunctionAndForLoop() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := pi + i;\n" +
                END +
                BEGIN +
                "for i from round(p_1) to round(p_2) step round(p_3);\n" +
                BEGIN +
                "    f_1 := pi + i;\n" +
                END +
                END);
    }

    @Test
    public void forLoopAndNormalFunctionInSideForLoop() {
        assertWrongGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "for i from round(p_1) to round(p_2) step round(p_3);\n" +
                BEGIN +
                "    f_1 := pi + i;\n" +
                END +
                BEGIN +
                "    f_1 := pi + i;\n" +
                END +
                END);
    }

    @Test
    public void forLoopAndNormalFunctionOutSideForLoop() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "for i from round(p_1) to round(p_2) step round(p_3);\n" +
                BEGIN +
                "    f_1 := pi + i;\n" +
                END +
                END +
                BEGIN +
                "    f_1 := pi + i;\n" +
                END);
    }

    @Test
    public void parametersWithSimpleIntExpression() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "for i from round(p_1) to round(p_2) step round(p_3);\n" +
                BEGIN +
                "    f_1 := p_{3};\n" +
                END +
                END);
    }

    @Test
    public void parametersWithComplexIntExpression() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "for i from round(p_1) to round(p_2) step round(p_3);\n" +
                BEGIN +
                "    f_1 := p_{binomial(3!,i!)+4};\n" +
                END +
                END);
    }

    @Test
    public void parametersWithWrongIntExpression() {
        assertWrongGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "for i from round(p_1) to round(p_2) step round(p_3);\n" +
                BEGIN +
                "    f_1 := p_{sin(3)};\n" +
                END +
                END);
    }

    @Test
    public void variablesWithSimpleIntExpression() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "for i from round(p_1) to round(p_2) step round(p_3);\n" +
                BEGIN +
                "    f_1 := x_{3};\n" +
                END +
                END);
    }

    @Test
    public void variablesWithComplexIntExpression() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "for i from round(p_1) to round(p_2) step round(p_3);\n" +
                BEGIN +
                "    f_1 := x_{binomial(3!,i!)+4};\n" +
                END +
                END);
    }

    @Test
    public void variablesWithWrongIntExpression() {
        assertWrongGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "for i from round(p_1) to round(p_2) step round(p_3);\n" +
                BEGIN +
                "    f_1 := x_{sin(3)};\n" +
                END +
                END);
    }

    @Test
    public void variablesWithRoundIntExpression() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "for i from round(p_1) to round(p_2) step round(p_3);\n" +
                BEGIN +
                "    f_1 := x_{round(sin(3))};\n" +
                END +
                END);
    }

    @Test
    public void sum() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := sum(u*u, u, 1, 10);\n" +
                END);
    }

    @Test
    public void sumWithExpression() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := sum(sqrt(u), u, 1, 10);\n" +
                END);
    }

    @Test
    public void prod() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := prod(u*u, u, 1, 10);\n" +
                END);
    }

    @Test
    public void prodWithExpression() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := prod(sqrt(u), u, 1, 10);\n" +
                END);
    }

    @Test
    public void complexSumAndProd() {
        assertGrammar("" +
                FUNCTION_XYZ_START +
                BEGIN +
                "    f_1 := prod(sqrt(u) + sum(sin(k), k, 1, 10), u, sum(k^2, k, 1, 5), prod(p, p, 1, 10));\n" +
                END);
    }

}
