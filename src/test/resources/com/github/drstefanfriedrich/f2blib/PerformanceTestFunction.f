#
#    Copyright (c) 2019 Stefan Friedrich
#
#    This program and the accompanying materials are made available under the
#    terms of the Eclipse Public License 2.0 which is available at
#    https://www.eclipse.org/legal/epl-2.0/
#
#    SPDX-License-Identifier: EPL-2.0
#

function com.github.drstefanfriedrich.f2blib.performance.TestFunction;
begin

      A := 1+2+3+4+5+6+7+8+9+10;
      B := abs(sin(1)+sin(2)+sin(3)+sin(4)+sin(5)+sin(6)+sin(7)+sin(8)+sin(9)+sin(10))^
           abs(cos(1)+cos(2)+cos(3)+cos(4)+(5)+cos(6)+cos(7)+cos(8)+cos(9)+cos(10));
      C := (abs(x_1) + abs(x_2))^(abs(p_1)+abs(p_2));
      D := round(abs(x_1)+abs(x_2)+abs(p_1)+abs(p_2))!;
      E := prod(sin(k)*cos(k),k,1,10000);
      F := sum(sin(k)+cos(k),k,1,10000);

    f_1 := sin(cos(x_1) * p_1) + exp(1 / (1 + x_2 ^ 2)) - p_2;

    f_2 := binomial(10, 6) + sqrt(1 + x_1 * x_1 + x_2 * x_2) - (p_1 * p_1 + p_2 * p_2);

    f_3 := sum(binomial(20, k) * (-1)^k/k! * x_1^k, k, 0, 20);

    f_4 := sum(binomial(30, k) * binomial(30 + k, k) * ((x_1 - 1) / 2)^k, k, 0, 30);

end
