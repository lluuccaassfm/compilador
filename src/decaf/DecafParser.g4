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

field_decl: (type ID | type ID LCOLCHETE int_literal RCOLCHETE) ((VIRG field_decl)*)? PONTOVIRG;

method_decl: (type | 'void') ID LPARENTESE ((type ID) ((VIRG type ID)*)?)? RPARENTESE block;

block: LCURLY (var_decl)* (statement)* RCURLY;

var_decl: (type ID) ((VIRG type ID)*)? PONTOVIRG;

statement: location ASSIGN_OP expr PONTOVIRG |
            method_call PONTOVIRG |
            IF LPARENTESE expr RPARENTESE block (ELSE block)? |
            FOR ID SINAL_IGUAL expr VIRG expr block |
            RETURN (expr)? PONTOVIRG |
            BREAK PONTOVIRG |
            CONTINUE PONTOVIRG |
            block;

expr: location | method_call | literal | expr bin_op expr | MENOS_OP expr | SINAL_EXCLAMACAO expr | LPARENTESE expr RPARENTESE;

type: INTEGER | BOOLEAN ;

location: ID | ID LCOLCHETE expr RCOLCHETE;

method_name: ID;

method_call: method_name LPARENTESE RPARENTESE;

callout_arg: expr | STRING;

literal:  int_literal | CHAR | BOOLEANCONDITION;

int_literal: NUMBER | HEXLIT;

bin_op: ARITH_OP | REL_OP | EQ_OP | COND_OP;


