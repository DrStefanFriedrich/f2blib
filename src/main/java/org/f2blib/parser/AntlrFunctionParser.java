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
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.f2blib.FunctionsLexer;
import org.f2blib.FunctionsListener;
import org.f2blib.FunctionsParser;

public class AntlrFunctionParser implements FunctionParser {

    @Override
    public void applyListener(String functionDefinition, FunctionsListener listener) {

        FunctionsLexer lexer = new FunctionsLexer(CharStreams.fromString(functionDefinition));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FunctionsParser parser = new FunctionsParser(tokens);

        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new ParseCancellationException("line " + line + ":" + charPositionInLine + " " + msg);
            }
        });

        ParseTree tree = parser.function_definition();
        ParseTreeWalker walker = new ParseTreeWalker();

        walker.walk(listener, tree);
    }

    public void applyVisitor(String functionDefinition, FunctionsListener listener) {

        FunctionsLexer lexer = new FunctionsLexer(CharStreams.fromString(functionDefinition));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FunctionsParser parser = new FunctionsParser(tokens);

        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new ParseCancellationException("line " + line + ":" + charPositionInLine + " " + msg);
            }
        });

        // http://jakubdziworski.github.io/java/2016/04/01/antlr_visitor_vs_listener.html
        parser.function_definition();

        ParseTree tree = parser.function_definition();
        ParseTreeWalker walker = new ParseTreeWalker();

        walker.walk(listener, tree);
    }

}
