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

package com.github.drstefanfriedrich.f2blib.example;

import org.objectweb.asm.util.ASMifier;

public class Dumper {

    public static void main(String[] args) throws Exception {
        ASMifier.main(new String[]{/* Example.class.getCanonicalName() */});
    }

}
