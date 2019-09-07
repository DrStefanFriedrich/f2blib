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

package com.github.drstefanfriedrich.f2blib.impl;

import com.github.drstefanfriedrich.f2blib.FunctionEvaluationKernel;
import com.github.drstefanfriedrich.f2blib.parser.AntlrFunctionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Construct an instance of {@link EvalImpl}.
 */
public class EvalAssembler extends AbstractFEPImpl {

    private static final Logger LOG = LoggerFactory.getLogger(EvalAssembler.class);

    protected FunctionEvaluationKernel constructInstance() {
        EvalImpl eval = new EvalImpl(new AntlrFunctionParser());
        LOG.info("F2BLib started successfully");
        return eval;
    }

    @Override
    public String getKernelIdentifier() {
        return "eval";
    }

}
