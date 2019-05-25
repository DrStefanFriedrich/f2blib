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

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.f2blib.FunctionsBaseListener;
import org.f2blib.FunctionsParser;

import java.util.HashSet;
import java.util.Set;

public class BytecodeGeneratingFunctionsListener extends FunctionsBaseListener {

    private final Set<String> functionIndexes = new HashSet<>();

    @Override
    public void enterSingle_valued_function(FunctionsParser.Single_valued_functionContext ctx) {

        String index = ctx.f.getText();

        if (functionIndexes.contains(index)) {
            throw new ParseCancellationException("Function indexes must be unique: f_" + index);
        } else {
            functionIndexes.add(index);
        }
    }

    @Override
    public void enterFor_loop(FunctionsParser.For_loopContext ctx) {

        int startValue = Integer.parseInt(ctx.startValue.getText());
        int endValue = Integer.parseInt(ctx.endValue.getText());

        if (startValue > endValue) {
            throw new ParseCancellationException("startValue in for loop must not be greater than endValue");
        }
    }

}
