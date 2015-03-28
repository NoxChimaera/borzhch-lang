/*
 *
 */

%{
  import java.io.IOException;
  import java.io.Reader;
%}

%token INTEGER DEFUN TYPE IDENTIFIER PROC L_CURBRACE R_CURBRACE
%token STRUCT L_SQBRACE R_SQBRACE GOTO RETURN BREAK CONTINUE
%token IF L_BRACE R_BRACE ELSE FOR WHILE DO SWITCH FLOAT NEW
%token STRING BOOLEAN COMMA ASSIGN IDENTIFIER DOT SEMICOLON
%token CASE TUPLE INCLUDE
%right UNAR_ARITHM NOT
%left INCR
%right POW
%left MUL_ARITHM
%left ADD_ARITHM
%left XOR
%left AND OR
%left MORELESS EQ
%nonassoc IFX

%%

start: global_list
     ;

global_list: global global_list
					 | /* empty */
           ;

global: function
      | struct_decl
      ;

function: DEFUN TYPE IDENTIFIER L_BRACE param_list R_BRACE codeblock
        | PROC IDENTIFIER L_BRACE param_list R_BRACE codeblock
        ;

param_list: /* empty */
          | decl param_tail
          ;

param_tail: COMMA param_tail
          | decl
          ;

codeblock: L_CURBRACE stmt_list R_CURBRACE
         ;

struct_decl: STRUCT IDENTIFIER decl_block
           ;

decl_block: L_CURBRACE decl_list R_CURBRACE
          ;

decl_list: /* empty*/
         | decl SEMICOLON decl_list
         ;

decl: TYPE IDENTIFIER
    | IDENTIFIER IDENTIFIER
    | TYPE L_SQBRACE R_SQBRACE IDENTIFIER
    | TYPE L_SQBRACE exp R_SQBRACE IDENTIFIER
    ;

decl_assign: decl ASSIGN exp
           ;

stmt_list: /* empty */
				 | stmt SEMICOLON stmt_list
         ;

stmt: exp
    | if
    | loop
    | switch
    | decl
    | decl_assign
    | GOTO IDENTIFIER
    | RETURN exp
    | BREAK
    | CONTINUE
    ;

if: IF L_BRACE exp R_BRACE codeblock %prec IFX else
  ;

else: /* empty */
    | ELSE if
    | ELSE codeblock
    ;

loop: FOR L_BRACE decl_assign SEMICOLON exp SEMICOLON exp R_BRACE codeblock
    | WHILE L_BRACE exp R_BRACE codeblock
    | DO codeblock WHILE L_BRACE exp R_BRACE SEMICOLON
    ;

switch: SWITCH L_BRACE exp R_BRACE codeblock ELSE codeblock
      | SWITCH L_BRACE exp R_BRACE codeblock 
      ;

exp: exp ADD_ARITHM exp
   | exp MUL_ARITHM exp
   | exp POW exp
   | exp EQ exp
   | exp MORELESS exp
   | exp AND exp
   | exp OR exp
   | exp XOR exp
   | L_BRACE exp R_BRACE
   | NOT exp
   | UNAR_ARITHM exp
   | IDENTIFIER INCR
   | NEW IDENTIFIER
   | reference
   | tuple_value
   | INTEGER
   | FLOAT
   | STRING
   | BOOLEAN
   ;

reference: structref
         | arrayref
         ;

structref: IDENTIFIER DOT IDENTIFIER
         | IDENTIFIER DOT structref
         ;

arrayref: IDENTIFIER L_SQBRACE exp R_SQBRACE
        ;

tuple_value: L_CURBRACE exp R_CURBRACE
           ;

%%

private Lexer lexer;

private int yylex() {
  int yyl_return = -1;
  try {
    yyl_return = lexer.yylex();
  }
  catch(IOException e) {
    System.err.println("IO error :" + e);
  }
  return yyl_return;
}

public void yyerror(String error) {
  System.err.println("Error: " + error);
}

public Parser(Reader r, boolean debug) {
  lexer = new Lexer(r);
  yydebug = debug;
}
