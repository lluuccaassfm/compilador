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

ID  :
  ('a'..'z' | 'A'..'Z')+;

WS_ : (' ' | '\n' ) -> skip;

SL_COMMENT : '//' (~'\n')* '\n' -> skip;

CHAR : '\'' (ESC |ESP | [a-z] | [A-Z] | [0-9] | '['|']'|'{'|'|'|'}'|'~') '\'';
STRING : '"' (ESC|~'"')* '"';
HEXLIT : [0..9];

fragment ESC : '\\' ('n'|'"'|'t'|'\\');
fragment ESP : '\\' ([ !#-&(-/:-@\-`]);
