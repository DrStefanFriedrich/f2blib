#
#    Copyright (c) 2019 Stefan Friedrich
#
#    This program and the accompanying materials are made available under the
#    terms of the Eclipse Public License 2.0 which is available at
#    https://www.eclipse.org/legal/epl-2.0/
#
#    SPDX-License-Identifier: EPL-2.0
#

function org.f2blib.performance.TestFunction;
begin

    f_1 := sin(cos(x_1) * p_1) + exp(1 / (1 + x_2 ^ 2)) - p_2;

    f_2 := binomial(10, 6) + sqrt(1 + x_1 * x_1 + x_2 * x_2) - (p_1 * p_1 + p_2 * p_2);

end
