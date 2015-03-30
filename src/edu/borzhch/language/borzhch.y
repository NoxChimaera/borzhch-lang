/*
 *
 */

%{
  import java.io.IOException;
  import java.io.Reader;
  import edu.borzhch.ast.*;
  import edu.borzhch.helpers.*;
  import edu.borzhch.SymTable;
  
%}

%token <sval> TYPE IDENTIFIER
%token <ival> INTEGER 
%token DEFUN PROC L_CURBRACE R_CURBRACE
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

%type <obj> global_list global 
%type <obj> function struct_decl
%type <obj> codeblock stmt_list stmt decl decl_assign assign exp
%%

start: 
     init global_list { 
        TreeAST.setRoot((NodeAST) $2); 
     }
     ;

init: /* empty */ {
        topTable = new SymTable(null);
        funcTable = new SymTable(null);
        structTable = new SymTable(null);
    }
    ;

global_list: 
    global global_list { 
        StatementList list = new StatementList(); 
        list.add((NodeAST) $1);
        if ($2 != null) list.addAll((NodeList) $2);
        $$ = list;
    }
    | /* empty */ { $$ = null; }
    ;

global: function { $$ = $1; }
    | struct_decl { $$ = null; }
    ;

openblock: L_CURBRACE {
            topTable = new SymTable(topTable);
         }
         ;

endblock: R_CURBRACE {
            SymTable oldTable = topTable;
            topTable = oldTable.getPrevious();
            oldTable.setPrevious(null);
            oldTable.clear();
        }
        ;
function:
    DEFUN TYPE IDENTIFIER L_BRACE param_list R_BRACE codeblock {
        FunctionNode func = null;

        if (isTypeExist($2)) {
            func = new FunctionNode($3, $2);
        } else {
            String msg = String.format("unknown type <%s>\n", $2);
            System.err.println(msg);
        }

        func.setStatements((StatementList) $7);

        funcTable.pushSymbol($3, $2);        

        $$ = func;
    }
    | PROC IDENTIFIER L_BRACE param_list R_BRACE codeblock {
        FunctionNode func = new FunctionNode($2, BOHelper.getType("void"));
        func.setStatements((StatementList) $6);

        funcTable.pushSymbol($2, "void");
    
        $$ = func;
    }
    ;

param_list: /* empty */
          | decl
          | decl param_tail
          ;

param_tail: COMMA decl param_tail
          | COMMA decl
          ;

codeblock: 
    openblock stmt_list endblock { $$ = $2; }
    ;

struct_decl: STRUCT IDENTIFIER decl_block {
              if(isIdentifierExist($2)) {
                String msg = String.format("identifier <%s> already in use\n", $2);
                System.err.println(msg);
              }
              structTable.pushSymbol($2, "ref");
           }
           ;

decl_block: L_CURBRACE decl_list R_CURBRACE
          ;

decl_list: /* empty*/
         | decl SEMICOLON decl_list
         ;

decl: TYPE IDENTIFIER { 
        if(isIdentifierExist($2)) {
          String msg = String.format("identifier <%s> already in use\n", $2);
          System.err.println(msg);
        }
        if(!isTypeExist($1)) {
          String msg = String.format("unknown type <%s>\n", $1);
          System.err.println(msg);
        }
        DeclarationNode decl = new DeclarationNode($2, BOHelper.getType($1));
        topTable.pushSymbol($2, $1);
        $$ = decl;  
    }
    | IDENTIFIER IDENTIFIER {
        if(isIdentifierExist($2)) {
          String msg = String.format("identifier <%s> already in use\n", $2);
          System.err.println(msg);
        }
        if(!isTypeExist($1)) {
          String msg = String.format("unknown type <%s>\n", $1);
          System.err.println(msg);
        }

        DeclarationNode decl = new DeclarationNode($2, $1);

        topTable.pushSymbol($2, $1);

        $$ = decl;
    }
    | TYPE L_SQBRACE R_SQBRACE IDENTIFIER { 
        if(isIdentifierExist($4)) {
          String msg = String.format("identifier <%s> already in use\n", $4);
          System.err.println(msg);
        }
        if(!isTypeExist($1)) {
          String msg = String.format("unknown type <%s>\n", $1);
          System.err.println(msg);
        }
        
        topTable.pushSymbol($4, "ref");

        $$ = null;
    }
    | TYPE L_SQBRACE exp R_SQBRACE IDENTIFIER { 
        if(isIdentifierExist($5)) {
          String msg = String.format("identifier <%s> already in use\n", $5);
          System.err.println(msg);
        }
        if(!isTypeExist($1)) {
          String msg = String.format("unknown type <%s>\n", $1);
          System.err.println(msg);
        }
        
        topTable.pushSymbol($5, "ref");
        
        $$ = null;
    }
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

stmt_list: /* empty */ { $$ = null; }
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

exp: exp ADD_ARITHM exp { $$ = null; }
   | exp MUL_ARITHM exp { $$ = null; }
   | exp POW exp { $$ = null; }
   | exp EQ exp { $$ = null; }
   | exp MORELESS exp { $$ = null; }
   | exp AND exp { $$ = null; }
   | exp OR exp { $$ = null; }
   | exp XOR exp { $$ = null; }

    /*###################################
     *# THIS GUYS ARE CAUSING TROUBLES  #
     *###################################
     */
   | L_BRACE exp R_BRACE { $$ = null; }
   | L_BRACE TYPE R_BRACE exp { $$ = null; }

   | NOT exp { $$ = null; }
   | UNAR_ARITHM exp { $$ = null; }
   | IDENTIFIER INCR { $$ = null; }
   | NEW IDENTIFIER { $$ = null; }
   | IDENTIFIER L_BRACE exp_list R_BRACE { $$ = null; }
   | reference { $$ = null; }
   | tuple_value { $$ = null; }
   | IDENTIFIER { $$ = null; }
   | INTEGER { $$ = new IntegerNode($1); }
   | FLOAT { $$ = null; }
   | STRING { $$ = null; }
   | BOOLEAN { $$ = null; }
   ;

reference: structref
         | arrayref
         ;

structref: IDENTIFIER DOT IDENTIFIER
         | IDENTIFIER DOT structref
         ;

arrayref: IDENTIFIER L_SQBRACE exp R_SQBRACE
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

private SymTable topTable = null;
private SymTable funcTable = null;
private SymTable structTable = null;

private boolean isTypeExist(String type) {
  Boolean result = false;

  result = BOHelper.isType(type);
  if(!result)
    result = structTable.findSymbol(type);

  return result;
}

private boolean isIdentifierExist(String identifier) {
  boolean result = false;

  result = topTable.findSymbol(identifier);
  if(!result) result = funcTable.findSymbol(identifier);
  if(!result) result = structTable.findSymbol(identifier);

  return result;
}

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
