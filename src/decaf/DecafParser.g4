parser grammar DecafParser;

@header {
package decaf;
}

options
{
  language=Java;
  tokenVocab=DecafLexer;
}

program: CLASS LCURLY (field_decl)* (method_decl)* RCURLY;

field_decl: (type ID | type ID LCOLCHETE NUMBER RCOLCHETE) (VIRG field_decl)* PONTOVIRG;

method_decl: ;

type: INTEGER | BOOLEAN ;