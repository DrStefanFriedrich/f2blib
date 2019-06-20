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

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.f2blib.antlr.FunctionsBaseVisitor;
import org.f2blib.antlr.FunctionsLexer;
import org.f2blib.antlr.FunctionsParser;
import org.f2blib.ast.FunctionBody;
import org.f2blib.ast.FunctionDefinition;

import static java.lang.String.format;

public class AntlrFunctionParser implements FunctionParser {

    @Override
    public FunctionDefinition parse(String functionDefinition) {

        FunctionsLexer lexer = new FunctionsLexer(CharStreams.fromString(functionDefinition));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FunctionsParser parser = new FunctionsParser(tokens);

        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new ParseCancellationException(format("line %d: %d %s", line, charPositionInLine, msg));
            }
        });

        return new FunctionDefinitionVisitor().visit(parser.function_definition());
    }

    private static final class FunctionDefinitionVisitor extends FunctionsBaseVisitor<FunctionDefinition> {
        @Override
        public FunctionDefinition visitFunction_definition(FunctionsParser.Function_definitionContext ctx) {

            FunctionBodyVisitor functionBodyVisitor = new FunctionBodyVisitor();
            String className = ctx.class_name().getText();
            return new FunctionDefinition(className, ctx.function_body().accept(functionBodyVisitor));
        }
    }

    private static final class FunctionBodyVisitor extends FunctionsBaseVisitor<FunctionBody> {

    }

}
