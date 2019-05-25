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

parse: function_definition EOF;

function_definition: FUNCTION className=class_name ';'
BEGIN
    function_body
END;

function_body: for_loop | single_valued_functions;

single_valued_functions: single_valued_function*;

// TODO SF Improve FOR_I
for_loop: FOR_I startValue=integer ':' endValue=integer SEMI
BEGIN
    single_valued_function*
END;

single_valued_function: FUNC f=INDEX DEFINE expression SEMI;

// TODO SF Check operator precendence
// TODO SF NEG, POS
expression:
    variable |
    parameter |
    LPAREN arg=expression RPAREN |
    larg=expression POWER rarg=expression |
    larg=expression TIMES rarg=expression |
    larg=expression DIVIDE rarg=expression |
    larg=expression PLUS rarg=expression |
    larg=expression MINUS rarg=expression |
    ABS LPAREN arg=expression RPAREN |
    ROUND LPAREN arg=expression RPAREN |
    EXP LPAREN arg=expression RPAREN |
    LN LPAREN arg=expression RPAREN |
    SIN LPAREN arg=expression RPAREN |
    COS LPAREN arg=expression RPAREN |
    TAN LPAREN arg=expression RPAREN |
    ARCSIN LPAREN arg=expression RPAREN |
    ARCCOS LPAREN arg=expression RPAREN |
    ARCTAN LPAREN arg=expression RPAREN |
    SINH LPAREN arg=expression RPAREN |
    COSH LPAREN arg=expression RPAREN |
    TANH LPAREN arg=expression RPAREN |
    ARSINH LPAREN arg=expression RPAREN |
    ARCOSH LPAREN arg=expression RPAREN |
    ARTANH LPAREN arg=expression RPAREN |
    FACULTY LPAREN iarg=integer_expression RPAREN |
    LAGUERRE LPAREN order=integer_expression ',' arg=expression RPAREN |
    LEGENDRE LPAREN order=integer_expression ',' arg=expression RPAREN |
    BINOMIAL LPAREN top=integer_expression ',' down=integer_expression RPAREN |
//    NEG expression |
//    POS expression |
    integer |
    constant;

integer_expression: integer | 'i';

variable: VAR x=INDEX;

parameter: PARAM p=INDEX;

constant: 'pi' | 'e' | 'boltzmann';

FUNCTION: 'function';
BEGIN: 'begin';
END: 'end';
FOR_I: 'for i :=';
FUNC: 'f_';
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
// NEG: '-';
// POS: '+';
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
LAGUERRE: 'laguerre';
LEGENDRE: 'legendre';
BINOMIAL: 'binomial';
FACULTY: 'faculty';

// TODO SF Improve class name and remove JAVALETTER, JAVALETTERORDIGIT
class_name:	FULLY_QUALIFIED_CLASS_NAME;

FULLY_QUALIFIED_CLASS_NAME: [a-zA-Z\\.]+;

fragment JAVALETTER:
        [a-zA-Z$_] // these are the "java letters" below 0x7F
    | // covers all characters above 0x7F which are not a surrogate
        ~[\u0000-\u007F\uD800-\uDBFF]
        { Character.isJavaIdentifierStart(_input.LA(-1)) }?
    | // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
        [\uD800-\uDBFF] [\uDC00-\uDFFF]
        { Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1))) }?;

fragment JAVALETTERORDIGIT:
        [a-zA-Z0-9$_] // these are the "java letters or digits" below 0x7F
    | // covers all characters above 0x7F which are not a surrogate
        ~[\u0000-\u007F\uD800-\uDBFF]
        { Character.isJavaIdentifierPart(_input.LA(-1)) }?
    | // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
        [\uD800-\uDBFF] [\uDC00-\uDFFF]
        { Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1))) }?;

INDEX: [1-9][0-9]*;

integer: '0' | INDEX;

WS: [ \t\r\n\u000C]+ -> skip;

COMMENT: '#' ~[\r\n]* -> skip;
