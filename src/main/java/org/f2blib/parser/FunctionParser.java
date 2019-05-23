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

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.f2blib.FunctionsLexer;
import org.f2blib.FunctionsListener;
import org.f2blib.FunctionsParser;

public class FunctionParser {

    public void applyListener(String functionDefinition, FunctionsListener listener) {

        FunctionsLexer lexer = new FunctionsLexer(CharStreams.fromString(functionDefinition));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FunctionsParser parser = new FunctionsParser(tokens);
        ParseTree tree = parser.parse();
        ParseTreeWalker walker = new ParseTreeWalker();

        walker.walk(listener, tree);
    }

}
