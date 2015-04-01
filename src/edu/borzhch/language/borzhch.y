/*
 *
 */

%{
  import java.io.IOException;
  import java.io.Reader;
  import edu.borzhch.ast.*;
  import edu.borzhch.helpers.*;
  import edu.borzhch.SymTable;
  import edu.borzhch.constants.*;
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

%left OR
%left AND 
%left XOR
%left EQ
%left MORELESS
%left ADD_ARITHM
%left MUL_ARITHM
%right POW
%left UN_ARITHM NOT
%right INCR DOT

%type <obj> global_list global exp_list exp_tail
%type <obj> function struct_decl decl_list if else tuple_value
%type <obj> codeblock stmt_list stmt decl decl_assign assign exp
%type <obj> reference structref arrayref param_list decl_block idref
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
        FunctionNode func = new FunctionNode($2, BOType.VOID);
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
        
        DeclarationNode node = new DeclarationNode($2, BOHelper.getType($1));
        node.type(BOHelper.getType($1));
        $$ = node;  
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

decl_assign: 
    decl ASSIGN exp {
        DeclarationNode decl = (DeclarationNode) $1;
        String name = decl.getName();

        AssignNode an = new AssignNode(new VariableNode(name, topTable.getSymbolType(name)), 
                (NodeAST) val_peek(0).obj);

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
            $$ = list;
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

assign: 
    structref ASSIGN exp
    | IDENTIFIER ASSIGN exp {
        if(!isIdentifierExist($1)) {
          String msg = String.format("identifier <%s> not declared\n", $1);
          System.err.println(msg);
        }
        AssignNode an = new AssignNode(new VariableNode($1, structTable.getSymbolType($1)), 
            (NodeAST) $3);
        $$ = an;
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
   exp ADD_ARITHM exp { 
        NodeAST l = (NodeAST) $1;
        NodeAST r = (NodeAST) $3;
        BOType infer = InferenceTypeTable.inferType(l.type(), r.type());
        if (BOType.VOID == infer) {
            yyerror(ErrorHelper.incompatibleTypes(r.type(), l.type()));
        }
        ArOpNode node = new ArOpNode(l, r, $2);
        node.type(infer);
        $$ = node;
    }
    | exp MUL_ARITHM exp { 
        NodeAST l = (NodeAST) $1;
        NodeAST r = (NodeAST) $3;
        BOType infer = InferenceTypeTable.inferType(l.type(), r.type());
        if (BOType.VOID == infer) {
            yyerror(ErrorHelper.incompatibleTypes(r.type(), l.type()));
        }
        ArOpNode node = new ArOpNode(l, r, $2);
        node.type(infer);
        $$ = node;
    }
    | exp POW exp { 
        NodeAST l = (NodeAST) $1;
        NodeAST r = (NodeAST) $3;
        if (!BOHelper.isNumber(l.type())) {
            yyerror(ErrorHelper.incompatibleTypes(l.type(), BOType.FLOAT));
        } else
        if (!BOHelper.isNumber(r.type())) {
            yyerror(ErrorHelper.incompatibleTypes(l.type(), BOType.FLOAT));
        }

        ArOpNode node = new ArOpNode(l, r, "**");
       node.type(BOType.FLOAT);
        $$ = node;
    }
    | exp EQ exp {          
        NodeAST l = (NodeAST) $1;
        NodeAST r = (NodeAST) $3;     
        BOType infer = InferenceTypeTable.inferType(l.type(), r.type());
        if (BOType.VOID == infer) {
            yyerror(ErrorHelper.incompatibleTypes(r.type(), l.type()));
        }
        CmpOpNode node = new CmpOpNode(l, r, $2);
        node.type(BOType.BOOL);
        $$ = node;
    }
    | exp MORELESS exp {         
        NodeAST l = (NodeAST) $1;
        NodeAST r = (NodeAST) $3;     
        BOType infer = InferenceTypeTable.inferType(l.type(), r.type());
        if (BOType.VOID == infer) {
            yyerror(ErrorHelper.incompatibleTypes(r.type(), l.type()));
        } else
        if (!BOHelper.isNumber(infer)) {
            yyerror(ErrorHelper.incompatibleTypes(r.type(), "number"));
        }
        CmpOpNode node = new CmpOpNode(l, r, $2);
        node.type(BOType.BOOL);
        $$ = node;
    }
    | exp AND exp {
        NodeAST l = (NodeAST) $1;
        NodeAST r = (NodeAST) $3; 
        BOType infer = InferenceTypeTable.inferType(l.type(), r.type());
        if (BOType.BOOL != infer) {
            yyerror(ErrorHelper.incompatibleTypes(infer, BOType.BOOL));
        }
        LogOpNode node = new LogOpNode(l, r, "and");
        node.type(BOType.BOOL);
        $$ = node;
    }
    | exp OR exp {
        NodeAST l = (NodeAST) $1;
        NodeAST r = (NodeAST) $3; 
        BOType infer = InferenceTypeTable.inferType(l.type(), r.type());
        if (BOType.BOOL != infer) {
            yyerror(ErrorHelper.incompatibleTypes(infer, BOType.BOOL));
        }
        LogOpNode node = new LogOpNode(l, r, "or");
        node.type(BOType.BOOL);
        $$ = node;
    }
    | exp XOR exp {
        NodeAST l = (NodeAST) $1;
        NodeAST r = (NodeAST) $3; 
        BOType infer = InferenceTypeTable.inferType(l.type(), r.type());
        if (BOType.BOOL != infer) {
            yyerror(ErrorHelper.incompatibleTypes(infer, BOType.BOOL));
        }
        LogOpNode node = new LogOpNode(l, r, "xor");
        node.type(BOType.BOOL);
        $$ = node;
    }
   | L_BRACE exp R_BRACE  { $$ = $2; }
   | NOT exp {
        NodeAST r = (NodeAST) $2; 
        if (BOType.BOOL != r.type()) {
            yyerror(ErrorHelper.incompatibleTypes(r.type(), BOType.BOOL));
        }
        UnOpNode node = new UnOpNode(r, "not");
        node.type(BOType.BOOL);
        $$ = node;
    }
    | ADD_ARITHM exp %prec UN_ARITHM NOT exp {
        NodeAST e = (NodeAST) $2; 
        if (!BOHelper.isNumber(e.type())) {
            yyerror(ErrorHelper.incompatibleTypes(e.type(), "number"));
        }
        UnOpNode node = new UnOpNode(e, $1);
        node.type(e.type());
        $$ = node;
    }
    | IDENTIFIER INCR  { 
        if(!isIdentifierExist($1)) {
            String msg = String.format("identifier <%s> not declared\n", $1);
            System.err.println(msg);
        }
        $$ = new PostOpNode(new VariableNode($1, topTable.getSymbolType($1)), $2); 
        ((NodeAST) $$).type(BOType.INT);
        ((PostOpNode) $$).setPush(true);
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
    | idref 						{ $$ = $1; }
    | INTEGER          { $$ = new IntegerNode($1); }
    | FLOAT            { $$ = new FloatNode((float)$1); }
    | STRING           { $$ = new StringNode($1); }
    | BOOLEAN          { $$ = new BooleanNode($1); }
    ;
	 
idref:
    idref DOT idref {
        $$ = new DotOpNode((NodeAST) $1, (NodeAST) $3);
    }
    | IDENTIFIER { 
        if(!isIdentifierExist($1)) {
            String msg = String.format("identifier <%s> not declared\n", $1);
            System.err.println(msg);
        }
        $$ = new VariableNode($1); 
}

reference: 
    arrayref { $$ = $1; }
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
