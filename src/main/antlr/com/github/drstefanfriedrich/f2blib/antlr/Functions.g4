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
    (single_valued_functions | for_loop)
;

single_valued_functions:
    auxiliary_variable*
    single_valued_function*
    (MARKOV_SHIFT LPAREN offset = intExpression RPAREN SEMI)?
;

for_loop:
    FOR intVar = specialVariable
            FROM start = intExpression
            TO end = intExpression
            STEP step = intExpression SEMI
    BEGIN
        singleValuedFunctions = single_valued_functions
    END
;

single_valued_function:
    FUNC DEFINE inner = expression SEMI
;

auxiliary_variable:
    specialVariable DEFINE inner = expression SEMI
;

intExpression:
    variableName = specialVariable # iintVar |
    value = integer # iint |
    left = intExpression POWER right = intExpression # ipower |
    inner = intExpression FACULTY # ifaculty |
    LPAREN inner = intExpression RPAREN # iparenthesis |
    ROUND LPAREN inner = expression RPAREN # round |
    BINOMIAL LPAREN n = intExpression COMMA k = intExpression RPAREN # ibinomial |
    SUM LPAREN inner = intExpression COMMA variableName = specialVariable COMMA start = intExpression COMMA end = intExpression RPAREN # isum |
    PROD LPAREN inner = intExpression COMMA variableName = specialVariable COMMA start = intExpression COMMA end = intExpression RPAREN # iprod |
    PLUS inner = intExpression # ipos |
    MINUS inner = intExpression # ineg |
    left = intExpression TIMES right = intExpression # imultiplication |
    left = intExpression DIVIDE right = intExpression # idivision |
    left = intExpression PLUS right = intExpression # iaddition |
    left = intExpression MINUS right = intExpression # isubtraction
;

expression:
    variableName = specialVariable # intVar |
    value = integer # int |
    var = variable # var |
    param = parameter # param |
    value = constant # const |
    value = floatingPoint # doub |
    left = expression POWER right = expression # power |
    LPAREN inner = expression RPAREN # parenthesis |
    ABS LPAREN inner = expression RPAREN # abs |
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
    SUM LPAREN inner = expression COMMA variableName = specialVariable COMMA start = intExpression COMMA end = intExpression RPAREN # sum |
    PROD LPAREN inner = expression COMMA variableName = specialVariable COMMA start = intExpression COMMA end = intExpression RPAREN # prod |
    PLUS inner = expression # pos |
    MINUS inner = expression # neg |
    left = expression TIMES right = expression # multiplication |
    left = expression DIVIDE right = expression # division |
    left = expression PLUS right = expression # addition |
    left = expression MINUS right = expression # subtraction |
    iexpr = intExpression # intExpr
;

variable:
    VARIABLE |
    VAR LBRACE intExpression RBRACE
;

parameter:
    PARAMETER |
    PARAM LBRACE intExpression RBRACE
;

VARIABLE:
    VAR INDEX
;

PARAMETER:
    PARAM INDEX
;

FUNC:
    F INDEX
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
F: 'f_';
SEMI: ';';
COMMA: ',';
LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
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
FOR: 'for';
FROM: 'from';
TO: 'to';
STEP: 'step';
MARKOV_SHIFT: 'markov_shift';
SUM: 'sum';
PROD: 'prod';

specialVariable:
    IDENTIFIER
;

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
    [a-zA-Z] |
    ~[\u0000-\u007F\uD800-\uDBFF] |
    [\uD800-\uDBFF] [\uDC00-\uDFFF]
;

fragment LetterOrDigit:
    Letter | [0-9]
;
