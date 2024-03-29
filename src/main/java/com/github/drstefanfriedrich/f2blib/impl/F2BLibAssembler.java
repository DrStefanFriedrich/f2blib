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
import com.github.drstefanfriedrich.f2blib.generator.FunctionEvaluationBytecodeGeneratorImpl;
import com.github.drstefanfriedrich.f2blib.parser.AntlrFunctionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Separate construction of a system from using it.
 */
public class F2BLibAssembler extends AbstractFEPImpl {

    private static final Logger LOG = LoggerFactory.getLogger(F2BLibAssembler.class);

    protected FunctionEvaluationKernel constructInstance() {
        F2BLibImpl f2BLib = new F2BLibImpl(new AntlrFunctionParser(), new FunctionEvaluationBytecodeGeneratorImpl());
        LOG.info("F2BLib started successfully");
        return f2BLib;
    }

    @Override
    public String getKernelIdentifier() {
        return "f2blib";
    }

}
