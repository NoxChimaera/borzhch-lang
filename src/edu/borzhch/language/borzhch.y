/*
 *
 */

%{
  import java.io.IOException;
  import java.io.Reader;
  import edu.borzhch.ast.*;
  import edu.borzhch.helpers.*;
%}

%token <sval> TYPE IDENTIFIER
%token <ival> INTEGER 
%token <dval> FLOAT
%token DEFUN PROC L_CURBRACE R_CURBRACE
%token STRUCT L_SQBRACE R_SQBRACE GOTO RETURN BREAK CONTINUE
%token IF L_BRACE R_BRACE ELSE FOR WHILE DO SWITCH NEW
%token <sval> STRING 
%token <ival> BOOLEAN 
%token COMMA ASSIGN DOT SEMICOLON
%token CASE TUPLE INCLUDE UN_MINUS UN_PLUS
%token <sval> INCR MUL_ARITHM ADD_ARITHM MORELESS EQ

%nonassoc IFX

%left MORELESS EQ
%left AND OR
%left XOR
%left ADD_ARITHM
%left MUL_ARITHM
%right POW
%left INCR
%left UN_ARITHM NOT

%type <obj> global_list global 
%type <obj> function struct_decl
%type <obj> codeblock stmt_list stmt decl decl_assign assign exp
%type <obj> reference structref arrayref 
%%

start: 
    global_list { TreeAST.setRoot((NodeAST) $1); }
    ;


global_list: 
    global global_list { 
        StatementList list = new StatementList(); 
        list.add((NodeAST) $1);
        if ($2 != null) list.addAll((NodeList) $2);
        $$ = list;
    }
    | { $$ = null; }
    ;

global: function { $$ = $1; }
    | struct_decl { $$ = null; }
    ;

function: 
    DEFUN TYPE IDENTIFIER L_BRACE param_list R_BRACE codeblock {
        FunctionNode func;
        if (BOHelper.isType($2)) {
            func = new FunctionNode($3, BOHelper.getType($2));
        } else {
            // TODO: Find in sometable
            func = new FunctionNode($3, $2);
        }
        func.setStatements((StatementList) $7);
        $$ = func;
    }
    | PROC IDENTIFIER L_BRACE param_list R_BRACE codeblock
    ;

param_list: /* empty */
          | decl
          | decl param_tail
          ;

param_tail: COMMA decl param_tail
          | COMMA decl
          ;

codeblock: 
    L_CURBRACE stmt_list R_CURBRACE { $$ = $2; }
    ;

struct_decl: STRUCT IDENTIFIER decl_block
           ;

decl_block: L_CURBRACE decl_list R_CURBRACE
          ;

decl_list: /* empty*/
         | decl SEMICOLON decl_list
         ;

decl: TYPE IDENTIFIER { 
        DeclarationNode decl = new DeclarationNode($2, BOHelper.getType($1));
        $$ = decl;  
    }
    | IDENTIFIER IDENTIFIER {
        DeclarationNode decl = new DeclarationNode($2, $1);
        $$ = decl;
    }
    | TYPE L_SQBRACE R_SQBRACE IDENTIFIER { $$ = null; }
    | TYPE L_SQBRACE exp R_SQBRACE IDENTIFIER { $$ = null; }
    ;

decl_assign: decl ASSIGN exp {
        DeclarationNode decl = (DeclarationNode) $1;
        AssignNode an = new AssignNode(decl.getName(), (NodeAST) $3);

        StatementList list = new StatementList();
        list.add(decl);
        list.add(an);
        $$ = list;
    }
    ;

stmt_list: 
    /* empty */ { $$ = null; }
    | stmt SEMICOLON stmt_list { 
        StatementList list = new StatementList();
        list.add((NodeAST) $1);
        if ($3 != null) list.addAll((StatementList) $3);
    }
    | if stmt_list { $$ = null; }
    | loop stmt_list { $$ = null; }
    | switch stmt_list { $$ = null; }
    ;

stmt: decl { $$ = $1; }
    | decl_assign { $$ = $1; }
    | assign { $$ = null; }
    | GOTO IDENTIFIER { $$ = null; }
    | RETURN exp { $$ = null; }
    | BREAK { $$ = null; }
    | CONTINUE { $$ = null; }
    ;

assign: structref ASSIGN exp
      | IDENTIFIER ASSIGN exp
      | arrayref ASSIGN exp
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

exp: 
    exp ADD_ARITHM exp { $$ = new ArOpNode((NodeAST) $1, (NodeAST) $3, $2); }
   | exp MUL_ARITHM exp { $$ = new ArOpNode((NodeAST) $1, (NodeAST) $3, $2); }
   | exp POW exp { $$ = new ArOpNode((NodeAST) $1, (NodeAST) $3, "**"); }
   | exp EQ exp { $$ = new CmpOpNode((NodeAST) $1, (NodeAST) $3, $2); }
   | exp MORELESS exp { $$ = new CmpOpNode((NodeAST) $1, (NodeAST) $3, $2); }
   | exp AND exp { $$ = new LogOpNode((NodeAST) $1, (NodeAST) $3, "a"); }
   | exp OR exp { $$ = new LogOpNode((NodeAST) $1, (NodeAST) $3, "o"); }
   | exp XOR exp { $$ = new LogOpNode((NodeAST) $1, (NodeAST) $3, "x"); }
   | L_BRACE exp R_BRACE { $$ = $2; }
   | NOT exp { $$ = new UnOpNode((NodeAST) $2, "n"); }

   | ADD_ARITHM exp %prec UN_ARITHM { $$ = new UnOpNode((NodeAST) $2, $1); }

   /*| AR_MINUS exp %prec UN_MINUS { $$ = new UnOpNode((NodeAST) $2, "-"); }
   | AR_PLUS exp %prec UN_PLUS { $$ = new UnOpNode((NodeAST) $2, "+"); }*/

   | IDENTIFIER INCR { $$ = new PostOpNode(new VariableNode($1), $2); }
   | NEW IDENTIFIER { $$ = new NewObjectNode($2); }
   | reference { $$ = $1; }
   | tuple_value { $$ = null; }
   | IDENTIFIER { $$ = new VariableNode($1); }
   | INTEGER { $$ = new IntegerNode($1); }
   | FLOAT { $$ = new FloatNode((float)$1); }
   | STRING { $$ = new StringNode($1); }
   | BOOLEAN { $$ = new BooleanNode($1); }
   ;

reference: 
    structref { $$ = $1; }
    | arrayref { $$ = $1; }
    ;

structref: 
    IDENTIFIER DOT IDENTIFIER {
        DotOpNode dot = new DotOpNode((VariableNode) new VariableNode($1), (NodeAST) new VariableNode($3));
        $$ = dot;
    }
    | IDENTIFIER DOT structref {
        DotOpNode dot = new DotOpNode((VariableNode) new VariableNode($1), (NodeAST) $3);
        $$ = dot;
    }
    ;

arrayref: 
    IDENTIFIER L_SQBRACE exp R_SQBRACE {
        $$ = new ArrayElementNode(
            new VariableNode($1),
            (NodeAST) $3
        );
    }
    ;

tuple_value: L_CURBRACE exp_list R_CURBRACE
           ;

exp_list: /* empty */
          | exp
          | exp exp_tail
          ;

exp_tail: COMMA exp exp_tail
        | COMMA exp
        ;

%%

private Lexer lexer;

private int yylex() {
  int yyl_return = -1;
  try {
    yylval = new ParserVal(0);
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
  lexer = new Lexer(r, this);
  yydebug = debug;
}
