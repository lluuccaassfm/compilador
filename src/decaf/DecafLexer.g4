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

LCURLY : '{';
RCURLY : '}';

BOOLEAN: 'true' | 'false';

TOKENS: 'boolean' | 'callout' | 'class' | 'else' | 'if' | 'int' | 'return' | 'void' | 'for' | 'break' | 'continue' | ',' ;

ID: ([a-z] | [A-Z] | '_' )+ (NUM)*;

WS_ : (' ' | '\n' | '\t' | '\\\\')+ -> skip;

SL_COMMENT : '//' (~'\n')* '\n' -> skip;

CHAR: '\'' (ESC | ESP | [a-z] | [A-Z] | NUM | '['|']'|'{'|'|'|'}'|'~') '\'';

STRING: '\"' (ESC| ~'"')* '\"';

HEXLIT: NUM+ ('x'([a-zA-Z]|NUM)*);

NUMBER: (NUM)+;

OP: '+' | '-' | '*' | '<' | '<=' | '!=' | '&&';

fragment ESC : '\\' ('n'|'"'|'t'|'\\');
fragment ESP : '\\' ([ !#-&(-/:-@\-`]);
fragment NUM : [0-9];
