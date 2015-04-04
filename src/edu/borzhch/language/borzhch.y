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
%token COMMA ASSIGN DOT SEMICOLON PRINT
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

%type <obj> global_list global exp_list exp_tail param_tail loop
%type <obj> function struct_decl decl_list if else tuple_value
%type <obj> codeblock stmt_list stmt decl decl_assign assign exp
%type <obj> reference arrayref param_list decl_block idref idref_tail
%type <obj> switch structref builtin
%%

start: 
     init global_list { 
        TreeAST.setRoot((NodeAST) $2); 
     }
     ;

init: /* empty */ {
        topTable = new SymTable(null);
        
        funcTable = new SymTable(null);
        funcTable.pushSymbol("print", "void");

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
    | struct_decl { $$ = $1; }
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
            yyerror(msg);
        }
        if(isIdentifierExist($3)) {
          String msg = String.format("identifier <%s> is already defined\n", $3);
          yyerror(msg);
        }

        func.setArguments((NodeList) $5);
        func.setStatements((StatementList) $7);

        funcTable.pushSymbol($3, $2);

        $$ = func;
    }
    | PROC IDENTIFIER L_BRACE param_list R_BRACE codeblock {
        if(isIdentifierExist($2)) {
          String msg = String.format("identifier <%s> is already defined\n", $2);
          yyerror(msg);
        }
        FunctionNode func = new FunctionNode($2, BOType.VOID);
        func.setStatements((StatementList) $6);

        funcTable.pushSymbol($2, "void");
    
        $$ = func;
    }
    ;

param_list: /* empty */ { $$ = null; }
          | decl { $$ = $1; }
          | decl param_tail { 
            StatementList node = new StatementList();
            node.add((NodeAST) $1);
            node.addAll((NodeList) $2);
            $$ = node; 
          }
          ;

param_tail: COMMA decl param_tail {
            StatementList node = new StatementList();
            node.add((NodeAST) $2);
            node.addAll((NodeList) $3);
            $$ = node; 
          }
          | COMMA decl {
            $$ = $2;
          }
          ;

codeblock: 
    openblock stmt_list endblock { $$ = $2; }
    ;

struct_decl: 
    STRUCT IDENTIFIER decl_block {
        if(isIdentifierExist($2)) {
            String msg = String.format("identifier <%s> is already defined\n", $2);
            yyerror(msg);
        }
        structTable.pushSymbol($2, "ref");

        StructDeclarationNode node = new StructDeclarationNode($2, (FieldList) $3);
        $$ = node;
    }
    ;

decl_block: 
    L_CURBRACE decl_list R_CURBRACE {
        $$ = $2;
    }
    ;

decl_list: /* empty*/ { $$ = null; }
    | decl SEMICOLON decl_list {
        FieldList node = new FieldList();
        DeclarationNode ldecl = (DeclarationNode) $1;
        ldecl.isField(true);
        node.add(ldecl);
        if ($3 != null) node.addAll((NodeList) $3);
        $$ = node;
    }
    ;

decl: 
    TYPE IDENTIFIER { 
        if(isIdentifierExist($2)) {
          String msg = String.format("identifier <%s> is already defined\n", $2);
          yyerror(msg);
        }
        if(!isTypeExist($1)) {
          String msg = String.format("unknown type <%s>\n", $1);
          yyerror(msg);
        }

        topTable.pushSymbol($2, $1);
        
        DeclarationNode node = new DeclarationNode($2, BOHelper.getType($1));
        node.type(BOHelper.getType($1));
        $$ = node;  
    }
    | IDENTIFIER IDENTIFIER {
        if(!isTypeExist($1)) {
          String msg = String.format("unknown type <%s>\n", $1);
          yyerror(msg);
        }
        if(isIdentifierExist($2)) {
          String msg = String.format("identifier <%s> is already defined\n", $2);
          yyerror(msg);
        }

        topTable.pushSymbol($2, $1);

        DeclarationNode decl = new DeclarationNode($2, $1);
        $$ = decl;
    }
    | TYPE L_SQBRACE exp R_SQBRACE IDENTIFIER { 
        if(!isTypeExist($1)) {
          String msg = String.format("unknown type <%s>\n", $1);
          yyerror(msg);
        }
        if(isIdentifierExist($5)) {
          String msg = String.format("identifier <%s> is already defined\n", $5);
          yyerror(msg);
        }
        
        topTable.pushSymbol($5, "ref", $1);

        DeclarationNode decl = new DeclarationNode($5, $1);
        NewArrayNode nan = new NewArrayNode($1, (NodeAST) $3);
        VariableNode var = new VariableNode($5, "ref");
        var.strType($1);

        AssignNode store = new AssignNode(var, nan);

        StatementList node = new StatementList();
        node.add(decl);
        node.add(store);
        $$ = node;
    }
    ;

decl_assign: 
    decl ASSIGN exp {
        DeclarationNode decl = (DeclarationNode) $1;
        String name = decl.getName();

        NodeAST exp = (NodeAST) $3;

        BOType infer = InferenceTypeTable.inferType(
            decl.type(),
            exp.type()
        );
        if (BOType.VOID == infer) {
            yyerror(ErrorHelper.incompatibleTypes(decl.type(), 
            (((NodeAST) $3).type())));
        }


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
        if ($2 != null) node.addAll((NodeList) $2);
        $$ = node; 
    }
    | loop stmt_list { 
        StatementList node = new StatementList();
        node.add((NodeAST) $1);
        if ($2 != null) node.addAll((NodeList) $2);
        $$ = node; 
    }
    | switch stmt_list { 
        StatementList node = new StatementList();
        node.add((NodeAST) $1);
        if ($2 != null) node.addAll((NodeList) $2);
        $$ = node; 
    }
    ;

builtin:
    PRINT exp {
        $$ = new PrintNode((NodeAST) $2);
    }
    ;

stmt: decl            { $$ = $1; }
    | decl_assign     { $$ = $1; }
    | assign          { $$ = $1; }
    | GOTO IDENTIFIER { 
      if(!isIdentifierExist($2)) {
        String msg = String.format("identifier <%s> not declared\n", $2);
        yyerror(msg);
      }
      $$ = null; 
    }
    | RETURN exp      { $$ = new ReturnNode((NodeAST) $2); }
    | BREAK           { $$ = new BreakNode(); }
    | CONTINUE        { $$ = new ContinueNode(); }
    | builtin         { $$ = $1; }
    ;

assign: 
    idref ASSIGN exp {
        DotOpNode dot = (DotOpNode) $1;
        NodeAST exp = (NodeAST) $3;
        SetFieldNode node = new SetFieldNode(dot, exp);
        $$ = node;
    }
    | IDENTIFIER ASSIGN exp {
        if(!isIdentifierExist($1)) {
          String msg = String.format("identifier <%s> not declared\n", $1);
          System.err.println(msg);
        }
        
        BOType infer = InferenceTypeTable.inferType(
            BOHelper.getType(topTable.getSymbolType($1)),
            ((NodeAST) $3).type()
        );
        if (BOType.VOID == infer) {
            yyerror(ErrorHelper.incompatibleTypes(BOHelper.getType(topTable.getSymbolType($1)), 
            (((NodeAST) $3).type())));
        }

        AssignNode an = new AssignNode(new VariableNode($1, topTable.getSymbolType($1)), 
            (NodeAST) $3);
        $$ = an;
    }
    | arrayref ASSIGN exp {
        /*arrayref := IDENTIFIER L_SQBRACE exp R_SQBRACE => ArrayElementNode*/
        
        ArrayElementNode index = (ArrayElementNode) $1;
        NodeAST value = (NodeAST) $3;
        SetArrayNode node = new SetArrayNode(index, value);
        $$ = node;
    }
    ;
	 
idref:
    IDENTIFIER DOT idref_tail {
        VariableNode var = new VariableNode($1, topTable.getSymbolType($1));
        $$ = new DotOpNode(var, (NodeAST) $3);
    }
    ;

idref_tail:
    IDENTIFIER {
        FieldNode node = new FieldNode($1);
        $$ = node;
    }
    | IDENTIFIER DOT idref_tail {
        FieldNode field = new FieldNode($1);
        DotOpNode node = new DotOpNode(field, (NodeAST) $3);
        $$ = node;
    }
    ;


if: 
    IF L_BRACE exp R_BRACE codeblock %prec IFX else {
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

loop: FOR L_BRACE decl_assign SEMICOLON exp SEMICOLON assign R_BRACE codeblock {
        NodeAST decl = (NodeAST) $3;
        NodeAST counter = (NodeAST) $5;
        NodeAST step = (NodeAST) $7;
        NodeAST statements = (NodeAST) $9; 
        ForNode node = new ForNode(decl, counter, step, statements);
        $$ = node;
    }
    | WHILE L_BRACE exp R_BRACE codeblock {
        WhileNode node = new WhileNode((NodeAST) $3, (StatementList) $5);
        $$ = node;
    }
    | DO codeblock WHILE L_BRACE exp R_BRACE SEMICOLON{
        DoWhileNode node = new DoWhileNode((NodeAST) $5, (StatementList) $2);
        $$ = node;
    }
    ;

switch: SWITCH L_BRACE exp R_BRACE codeblock ELSE codeblock {
        SwitchNode node = new SwitchNode((NodeAST) $3, (StatementList) $5, (StatementList) $7);
        $$ = node;
      }
      | SWITCH L_BRACE exp R_BRACE codeblock {
        SwitchNode node = new SwitchNode((NodeAST) $3, (StatementList) $5, null);
        $$ = node;
      }
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
   | IDENTIFIER INCR { 
      if(!isIdentifierExist($1)) {
        String msg = String.format("identifier <%s> is not declared\n", $1);
        yyerror(msg);
      }
      $$ = new PostOpNode(new VariableNode($1), $2); 
        ((NodeAST) $$).type(BOType.INT);
        ((PostOpNode) $$).setPush(true);
   }
    | NEW IDENTIFIER { 
        if(!isTypeExist($2)) {
            String msg = String.format("unknown type <%s>\n", $2);
            yyerror(msg);
        }
        $$ = new NewObjectNode($2); 
    }
    | reference        { $$ = $1; }
    | tuple_value      { $$ = $1; }
    | idref            { $$ = $1; }
    | INTEGER          { $$ = new IntegerNode($1); }
    | FLOAT            { $$ = new FloatNode((float)$1); }
    | STRING           { $$ = new StringNode($1); }
    | BOOLEAN          { $$ = new BooleanNode($1); }
    | IDENTIFIER { 
	if(!isIdentifierExist($1)) {
            String msg = String.format("identifier <%s> is not declared\n", $1);
            yyerror(msg);
        }
        $$ = new VariableNode($1, topTable.getSymbolType($1)); 
    }
    ;

reference: 
    arrayref { $$ = $1; }
    ;

arrayref: 
    IDENTIFIER L_SQBRACE exp R_SQBRACE {
        if(!isIdentifierExist($1)) {
          String msg = String.format("identifier <%s> not declared\n", $1);
          yyerror(msg);
        }

        VariableNode var = new VariableNode($1, topTable.getBaseType($1));
        var.type(BOType.REF);
        ArrayElementNode node = new ArrayElementNode(var, (NodeAST) $3);
        $$ = node;
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
  System.err.println(String.format("Error on line %d, column %d: %s", lexer.Yyline(), lexer.Yycolumn(), error));
}

public Parser(Reader r, boolean debug) {
  lexer = new Lexer(r, this);
  yydebug = debug;
}
