lexer grammar DecafLexer;

@header {
package decaf;
}

options
{
  language=Java;
}

tokens
{
  TK_class
}

IF: 'if';
BOOLEAN: 'boolean';
CALLOUT: 'callout';
CLASS: 'class Program';
ELSE: 'else';
INTEGER: 'int';
RETURN: 'return';
VOID: 'void';
FOR: 'for';
BREAK: 'break';
CONTINUE: 'continue';

LCURLY : '{';
RCURLY : '}';

LPARENTESE : '(';
RPARENTESE : ')';

LCOLCHETE : '[';
RCOLCHETE : ']';

BOOLEANCONDITION: 'true' | 'false';

ID: ('a'..'z' | 'A'..'Z' | '_' )+ (INT)* ID?;

WS_ : (' ' | '\n' | '\t' | '\\\\')+ -> skip;

SL_COMMENT : '//' (~'\n')* '\n' -> skip;

CHAR: '\'' (ESC | PORC_OP | ESPECIALASK | [a-z] | [A-Z] | INT | '['|']'|'{'|'|'|'}'|'~') '\'';

STRING: '"' ( ESC | ID | PONTUACAO | ESPECIALASK | PORC_OP)+ '"';

HEXLIT: INT ('x'([a-fA-F]|INT)+)?;

NUMBER: (INT)+;

INTEGER_LITERAL: (NUMBER | HEXLIT)+ ;

DOISPONTOS: ':';
VIRG: ',';
PONTOVIRG: ';';
MENOS_OP: '-';
MAIS_OP: '+';
MULT_OP: '*';
DIV_OP: '/';
PORC_OP: '%';
MAIOR_REL_OP: '>';
MENOR_REL_OP: '<';
MAIOR_IGUAL_REL_OP: '>=';
MENOR_IGUAL_REL_OP: '<=';
IGUAL_EQ_OP: '==';
DIFERENTE_EQ_OP: '!=';
SINAL_EXCLAMACAO: '!';
SINAL_IGUAL: '=';
SINAL_MAIS_IGUAL: '+=';
SINAL_MENOS_IGUAL: '-=';
AND_COND_OP: '&&';
OR_COND_OP: '||';

fragment ESC : '\\' ('n'|'"'|'t'|'\\');
fragment ESPECIALASK : '\\' ([ !#-&(-/:-@\-`]);
fragment INT : [0-9];
fragment PONTUACAO: ( '.' | '?' | ',' | ';' | ' ' | ':' | ESPECIAL );
fragment ESPECIAL: '\\' ( '\'' | '\"' | '\\' );
