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

%type <obj> global_list global exp_list exp_tail
%type <obj> function struct_decl decl_list if else tuple_value
%type <obj> codeblock stmt_list stmt decl decl_assign assign exp
%type <obj> reference structref arrayref param_list decl_block
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


global_list: /* empty */ { 
           $$ = null; 
    }
    | global global_list { 
        StatementList list = new StatementList(); 
        list.add((NodeAST) $1);
        if ($2 != null) list.addAll((NodeList) $2);
        $$ = list;
    }
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
        if(isIdentifierExist($3)) {
          String msg = String.format("identifier <%s> already in use\n", $3);
          System.err.println(msg);
        }

        func.setArguments((NodeList) $5);
        func.setStatements((StatementList) $7);

        funcTable.pushSymbol($3, $2);        

        $$ = func;
    }
    | PROC IDENTIFIER L_BRACE param_list R_BRACE codeblock {
        if(isIdentifierExist($2)) {
          String msg = String.format("identifier <%s> already in use\n", $2);
          System.err.println(msg);
        }
        FunctionNode func = new FunctionNode($2, BOHelper.getType("void"));
        func.setStatements((StatementList) $6);

        funcTable.pushSymbol($2, "void");
    
        $$ = func;
    }
    ;

param_list: /* empty */ { $$ = null; }
          | decl { $$ = null; }
          | decl param_tail { $$ = null; }
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

              StructDeclarationNode node = new StructDeclarationNode($2, (StatementList) $3);
              $$ = node;
           }
           ;

decl_block: L_CURBRACE decl_list R_CURBRACE {
            $$ = $2;
          }
          ;

decl_list: /* empty*/ {
          $$ = null;
         }
         | decl SEMICOLON decl_list {
          StatementList node = new StatementList();
          node.add((NodeAST) $1);
          node.addAll((NodeList) $3);
          $$ = node;
         }
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

        topTable.pushSymbol($2, $1);
        
        DeclarationNode decl = new DeclarationNode($2, BOHelper.getType($1));
        $$ = decl;  
    }
    | IDENTIFIER IDENTIFIER {
        if(!isTypeExist($1)) {
          String msg = String.format("unknown type <%s>\n", $1);
          System.err.println(msg);
        }
        if(isIdentifierExist($2)) {
          String msg = String.format("identifier <%s> already in use\n", $2);
          System.err.println(msg);
        }

        topTable.pushSymbol($2, $1);

        DeclarationNode decl = new DeclarationNode($2, $1);
        $$ = decl;
    }
    | TYPE L_SQBRACE R_SQBRACE IDENTIFIER { 
        if(!isTypeExist($1)) {
          String msg = String.format("unknown type <%s>\n", $1);
          System.err.println(msg);
        }
        if(isIdentifierExist($4)) {
          String msg = String.format("identifier <%s> already in use\n", $4);
          System.err.println(msg);
        }
        
        topTable.pushSymbol($4, "ref");
        
        DeclarationNode decl = new DeclarationNode($4, BOHelper.getType("ref"));
        $$ = decl;
    }
    | TYPE L_SQBRACE exp R_SQBRACE IDENTIFIER { 
        if(!isTypeExist($1)) {
          String msg = String.format("unknown type <%s>\n", $1);
          System.err.println(msg);
        }
        if(isIdentifierExist($5)) {
          String msg = String.format("identifier <%s> already in use\n", $5);
          System.err.println(msg);
        }
        
        topTable.pushSymbol($5, "ref");

        DeclarationNode decl = new DeclarationNode($5, BOHelper.getType("ref"));
        $$ = decl;
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
         | if stmt_list { 
            StatementList node = new StatementList();
            node.add((NodeAST) $1);
            node.addAll((NodeList) $2);
            $$ = node; 
         }
         | loop stmt_list { $$ = null; }
         | switch stmt_list { $$ = null; }
         ;

stmt: decl            { $$ = $1; }
    | decl_assign     { $$ = $1; }
    | assign          { $$ = null; }
    | GOTO IDENTIFIER { 
      if(!isIdentifierExist($2)) {
        String msg = String.format("identifier <%s> not declared\n", $2);
        System.err.println(msg);
      }
      $$ = null; 
    }
    | RETURN exp      { $$ = null; }
    | BREAK           { $$ = null; }
    | CONTINUE        { $$ = null; }
    ;

assign: structref ASSIGN exp
      | IDENTIFIER ASSIGN exp {
        if(!isIdentifierExist($1)) {
          String msg = String.format("identifier <%s> not declared\n", $1);
          System.err.println(msg);
        }
      }
      | arrayref ASSIGN exp
      ;

if: IF L_BRACE exp R_BRACE codeblock %prec IFX else {
    IfNode node = new IfNode((NodeAST) $3, (StatementList) $5, (IfNode) $6);
    $$ = node;
  }
  ;

else: /* empty */ {
      $$ = null;
    }
    | ELSE if {
      $$ = (IfNode) $2;
    }
    | ELSE codeblock {
      IfNode node = new IfNode(null, (StatementList) $2, null);
      $$ = node;
    }
    ;

loop: FOR L_BRACE decl_assign SEMICOLON exp SEMICOLON exp R_BRACE codeblock
    | WHILE L_BRACE exp R_BRACE codeblock
    | DO codeblock WHILE L_BRACE exp R_BRACE SEMICOLON
    ;

switch: SWITCH L_BRACE exp R_BRACE codeblock ELSE codeblock
      | SWITCH L_BRACE exp R_BRACE codeblock 
      ;

exp: 
   exp ADD_ARITHM exp     { $$ = new ArOpNode((NodeAST) $1, (NodeAST) $3, $2); }
   | exp MUL_ARITHM exp   { $$ = new ArOpNode((NodeAST) $1, (NodeAST) $3, $2); }
   | exp POW exp          { $$ = new ArOpNode((NodeAST) $1, (NodeAST) $3, "**"); }
   | exp EQ exp           { $$ = new CmpOpNode((NodeAST) $1, (NodeAST) $3, $2); }
   | exp MORELESS exp     { $$ = new CmpOpNode((NodeAST) $1, (NodeAST) $3, $2); }
   | exp AND exp          { $$ = new LogOpNode((NodeAST) $1, (NodeAST) $3, "a"); }
   | exp OR exp           { $$ = new LogOpNode((NodeAST) $1, (NodeAST) $3, "o"); }
   | exp XOR exp          { $$ = new LogOpNode((NodeAST) $1, (NodeAST) $3, "x"); }
   | L_BRACE exp R_BRACE  { $$ = $2; }
   | NOT exp              { $$ = new UnOpNode((NodeAST) $2, "n"); }

   | ADD_ARITHM exp %prec UN_ARITHM { $$ = new UnOpNode((NodeAST) $2, $1); }

   /*| AR_MINUS exp %prec UN_MINUS { $$ = new UnOpNode((NodeAST) $2, "-"); }
   | AR_PLUS exp %prec UN_PLUS { $$ = new UnOpNode((NodeAST) $2, "+"); }*/

   | IDENTIFIER INCR  { 
      if(!isIdentifierExist($1)) {
        String msg = String.format("identifier <%s> not declared\n", $1);
        System.err.println(msg);
      }
      $$ = new PostOpNode(new VariableNode($1), $2); 
   }
   | NEW IDENTIFIER   { 
      if(!isTypeExist($2)) {
        String msg = String.format("unknown type <%s>\n", $2);
        System.err.println(msg);
      }
      $$ = new NewObjectNode($2); 
   }
   | reference        { $$ = $1; }
   | tuple_value      { $$ = $1; }
   | IDENTIFIER       { 
      if(!isIdentifierExist($1)) {
        String msg = String.format("identifier <%s> not declared\n", $1);
        System.err.println(msg);
      }
      $$ = new VariableNode($1);
   }
   | INTEGER          { $$ = new IntegerNode($1); }
   | FLOAT            { $$ = new FloatNode((float)$1); }
   | STRING           { $$ = new StringNode($1); }
   | BOOLEAN          { $$ = new BooleanNode($1); }
   ;

reference: 
    structref { $$ = $1; }
    | arrayref { $$ = $1; }
    ;

structref: 
    IDENTIFIER DOT IDENTIFIER {
        if(!isIdentifierExist($1)) {
          String msg = String.format("identifier <%s> not declared\n", $1);
          System.err.println(msg);
        }
        if(!isIdentifierExist($3)) {
          String msg = String.format("identifier <%s> not declared\n", $3);
          System.err.println(msg);
        }
        DotOpNode dot = new DotOpNode((VariableNode) new VariableNode($1), (NodeAST) new VariableNode($3));
        $$ = dot;
    }
    | IDENTIFIER DOT structref {
        if(!isIdentifierExist($1)) {
          String msg = String.format("identifier <%s> not declared\n", $1);
          System.err.println(msg);
        }
        DotOpNode dot = new DotOpNode((VariableNode) new VariableNode($1), (NodeAST) $3);
        $$ = dot;
    }
    ;

arrayref: 
    IDENTIFIER L_SQBRACE exp R_SQBRACE {
        if(!isIdentifierExist($1)) {
          String msg = String.format("identifier <%s> not declared\n", $1);
          System.err.println(msg);
        }
        $$ = new ArrayElementNode(
            new VariableNode($1),
            (NodeAST) $3
        );
    }
    ;

tuple_value: L_CURBRACE exp_list R_CURBRACE {
            TupleNode node = new TupleNode((StatementList) $2);
            $$ = node;
           }
           ;

exp_list: /* empty */ {
          $$ = null;
        }
        | exp {
          $$ = $1;
        }
        | exp exp_tail {
          StatementList node = new StatementList();
          node.add((NodeAST) $1);
          node.addAll((NodeList) $2);
          $$ = node;
        }
        ;

exp_tail: COMMA exp exp_tail {
          StatementList node = new StatementList();
          node.add((NodeAST) $2);
          node.addAll((NodeList) $3);
          $$ = node;
        }
        | COMMA exp {
          $$ = $2;
        }
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
