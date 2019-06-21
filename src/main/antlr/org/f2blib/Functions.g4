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

// parse: function_definition EOF;

function_definition: FUNCTION className=class_name ';'
BEGIN
    function_body
END;

function_body: single_valued_functions # singleValuedFunctions;

single_valued_functions: single_valued_function*;

single_valued_function: FUNC f=INDEX DEFINE expression SEMI;

expression:
    variable # var |
    parameter # param |
    LPAREN expression RPAREN # parenthesis |
    expression POWER expression # power |
    expression PLUS expression # plus |
    expression MINUS expression # minus |
    expression TIMES expression # times |
    expression DIVIDE expression # divide |
    ABS LPAREN expression RPAREN # abs |
    ROUND LPAREN expression RPAREN # round |
    EXP LPAREN expression RPAREN # exp |
    LN LPAREN expression RPAREN # ln |
    SIN LPAREN expression RPAREN # sin |
    COS LPAREN expression RPAREN # cos |
    TAN LPAREN expression RPAREN # tan |
    ARCSIN LPAREN expression RPAREN # arcsin |
    ARCCOS LPAREN expression RPAREN # arccos |
    ARCTAN LPAREN expression RPAREN # arctan |
    SINH LPAREN expression RPAREN # sinh |
    COSH LPAREN expression RPAREN # cosh |
    TANH LPAREN expression RPAREN # tanh |
    ARSINH LPAREN expression RPAREN # arsinh |
    ARCOSH LPAREN expression RPAREN # arcosh |
    ARTANH LPAREN expression RPAREN # artanh |
    FACULTY LPAREN integer RPAREN # faculty |
    BINOMIAL LPAREN integer ',' integer RPAREN # binomial |
//    NEG expression |
//    POS expression |
    integer # int |
    constant # const;

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
BINOMIAL: 'binomial';
FACULTY: 'faculty';

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
