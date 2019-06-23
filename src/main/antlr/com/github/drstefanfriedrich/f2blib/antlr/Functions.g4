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

grammar Functions;

function_definition:
    FUNCTION className = class_name SEMI
    BEGIN
        function_body
    END
;

function_body:
    single_valued_functions # singleValuedFunctions
;

single_valued_functions:
    single_valued_function*
;

single_valued_function:
    FUNC DEFINE inner = expression SEMI
;

expression:
    var = VARIABLE # var |
    param = PARAMETER # param |
    value = constant # const |
    value = integer # int |
    value = floatingPoint # doub |
    left = expression POWER right = expression # power |
    inner = integer FACULTY # faculty |
    LPAREN inner = expression RPAREN # parenthesis |
    ABS LPAREN inner = expression RPAREN # abs |
    ROUND LPAREN inner = expression RPAREN # round |
    EXP LPAREN inner = expression RPAREN # exp |
    LN LPAREN inner = expression RPAREN # ln |
    SQRT LPAREN inner = expression RPAREN # sqrt |
    SIN LPAREN inner = expression RPAREN # sin |
    COS LPAREN inner = expression RPAREN # cos |
    TAN LPAREN inner = expression RPAREN # tan |
    ARCSIN LPAREN inner = expression RPAREN # arcsin |
    ARCCOS LPAREN inner = expression RPAREN # arccos |
    ARCTAN LPAREN inner = expression RPAREN # arctan |
    SINH LPAREN inner = expression RPAREN # sinh |
    COSH LPAREN inner = expression RPAREN # cosh |
    TANH LPAREN inner = expression RPAREN # tanh |
    ARSINH LPAREN inner = expression RPAREN # arsinh |
    ARCOSH LPAREN inner = expression RPAREN # arcosh |
    ARTANH LPAREN inner = expression RPAREN # artanh |
    PLUS inner = expression # pos |
    MINUS inner = expression # neg |
    BINOMIAL LPAREN n = integer ',' k = integer RPAREN # binomial |
    left = expression TIMES right = expression # multiplication |
    left = expression DIVIDE right = expression # division |
    left = expression PLUS right = expression # addition |
    left = expression MINUS right = expression # subtraction
;

VARIABLE:
    'x_' [1-9][0-9]*
;

PARAMETER:
    'p_' [1-9][0-9]*
;

FUNC:
    'f_' [1-9][0-9]*
;

constant:
    PI | E | BOLTZMANN
;

class_name:
    IDENTIFIER ('.' IDENTIFIER)*
;

integer:
    '0' | INDEX
;

floatingPoint:
    FLOAT_LITERAL
;

FUNCTION: 'function';
BEGIN: 'begin';
END: 'end';
PARAM: 'p_';
VAR: 'x_';
SEMI: ';';
LPAREN: '(';
RPAREN: ')';
DEFINE: ':=';
PLUS: '+';
MINUS: '-';
TIMES: '*';
DIVIDE: '/';
POWER: '^';
ABS: 'abs';
ROUND: 'round';
EXP: 'exp';
LN: 'ln';
SIN: 'sin';
COS: 'cos';
TAN: 'tan';
ARCSIN: 'arcsin';
ARCCOS: 'arccos';
ARCTAN: 'arctan';
SINH: 'sinh';
COSH: 'cosh';
TANH: 'tanh';
ARSINH: 'arsinh';
ARCOSH: 'arcosh';
ARTANH: 'artanh';
BINOMIAL: 'binomial';
FACULTY: '!';
SQRT: 'sqrt';
PI: 'pi';
E: 'e';
BOLTZMANN: 'boltzmann';

IDENTIFIER:
    Letter LetterOrDigit*
;

INDEX:
    [1-9][0-9]*
;

FLOAT_LITERAL:
    (Digits '.' Digits? | '.' Digits) ExponentPart? [fFdD]? |
    Digits (ExponentPart [fFdD]? | [fFdD])
;

WS:
    [ \t\r\n\u000C]+ -> skip
;

COMMENT:
    '#' ~[\r\n]* -> skip
;

fragment Digits:
    [0-9] ([0-9_]* [0-9])?
;

fragment ExponentPart:
    [eE] [+-]? Digits
;

fragment Letter:
    [a-zA-Z$_] |
    ~[\u0000-\u007F\uD800-\uDBFF] |
    [\uD800-\uDBFF] [\uDC00-\uDFFF]
;

fragment LetterOrDigit:
    Letter | [0-9]
;
