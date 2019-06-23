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

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import com.github.drstefanfriedrich.f2blib.antlr.FunctionsLexer;
import com.github.drstefanfriedrich.f2blib.antlr.FunctionsParser;
import com.github.drstefanfriedrich.f2blib.ast.FunctionDefinition;

import static java.lang.String.format;

public class AntlrFunctionParser implements FunctionParser {

    @Override
    public FunctionDefinition parse(String functionDefinition) {

        FunctionsLexer lexer = new FunctionsLexer(CharStreams.fromString(functionDefinition));
        lexer.removeErrorListeners();

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        FunctionsParser parser = new FunctionsParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new ParseCancellationException(format("line: %d, column: %d, message: %s, offendingSymbol: %s, exception: %s",
                        line, charPositionInLine, msg, offendingSymbol, e));
            }
        });

        return (FunctionDefinition) new AntlrVisitor().visitFunction_definition(parser.function_definition());
    }

}
