/*
 *
 */

%{
  import Lexer;
%}

%token INTEGER DEFUN TYPE IDENTIFIER PROC L_CURBRACE R_CURBRACE
%token STRUCT L_SQBRACE R_SQBRACE GOTO RETURN BREAK CONTINUE
%token IF L_BRACE R_BRACE ELSE FOR WHILE DO SWITCH FLOAT
%token STRING BOOLEAN COMMA ASSIGN STRUCT_IDENTIFIER DOT SEMICOLON
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

start: /* empty */
     | global_list
     ;

global_list: global global_list
           ;

global: function
      | struct_decl
      ;

function: DEFUN TYPE IDENTIFIER L_BRACE param_list R_BRACE codeblock
        | PROC IDENTIFIER L_BRACE param_list R_BRACE codeblock
        ;

param_list: /* empty */
          decl param_tail
          ;

param_tail: COMMA param_tail
          | decl
          ;

codeblock: L_CURBRACE stmt_list R_CURBRACE
         ;

struct_decl: STRUCT STRUCT_IDENTIFIER decl_block
           ;

decl_block: L_CURBRACE decl_list R_CURBRACE
          ;

decl_list: /* empty*/
         | decl SEMICOLON decl_list
         ;

decl: TYPE IDENTIFIER
    | TYPE L_SQBRACE R_SQBRACE IDENTIFIER
    | TYPE L_SQBRACE exp R_SQBRACE IDENTIFIER
    ;

decl_assign: decl ASSIGN exp
           ;

stmt_list: stmt stmt_list
         ;

stmt: exp SEMICOLON
    | if
    | loop
    | switch
    | decl SEMICOLON
    | decl_assign SEMICOLON
    | GOTO IDENTIFIER SEMICOLON
    | RETURN exp SEMICOLON
    | BREAK SEMICOLON
    | CONTINUE SEMICOLON
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
