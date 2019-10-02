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

statement: location assign_op expr PONTOVIRG |
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

method_call: method_name LPARENTESE ((expr) ((VIRG expr)+)?)? RPARENTESE | CALLOUT LPARENTESE STRING (VIRG callout_arg ((VIRG callout_arg)*)?)? RPARENTESE;

callout_arg: expr | STRING;

literal:  int_literal | CHAR | BOOLEANCONDITION;

int_literal: NUMBER | HEXLIT;

bin_op: arith_op | rel_op | eq_op | cond_op;

assign_op: SINAL_IGUAL | SINAL_MAIS_IGUAL | SINAL_MENOS_IGUAL ;

arith_op: MAIS_OP | MENOS_OP | MULT_OP | DIV_OP | PORC_OP;

rel_op: MAIOR_REL_OP | MENOR_REL_OP | MAIOR_IGUAL_REL_OP | MENOR_IGUAL_REL_OP;

eq_op: IGUAL_EQ_OP | DIFERENTE_EQ_OP ;

cond_op: AND_COND_OP | OR_COND_OP ;


