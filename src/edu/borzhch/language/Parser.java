//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";



package edu.borzhch.language;



//#line 6 "./borzhch.y"
  import java.io.IOException;
  import java.io.Reader;
//#line 20 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short INTEGER=257;
public final static short DEFUN=258;
public final static short TYPE=259;
public final static short IDENTIFIER=260;
public final static short PROC=261;
public final static short L_CURBRACE=262;
public final static short R_CURBRACE=263;
public final static short STRUCT=264;
public final static short L_SQBRACE=265;
public final static short R_SQBRACE=266;
public final static short GOTO=267;
public final static short RETURN=268;
public final static short BREAK=269;
public final static short CONTINUE=270;
public final static short IF=271;
public final static short L_BRACE=272;
public final static short R_BRACE=273;
public final static short ELSE=274;
public final static short FOR=275;
public final static short WHILE=276;
public final static short DO=277;
public final static short SWITCH=278;
public final static short FLOAT=279;
public final static short NEW=280;
public final static short STRING=281;
public final static short BOOLEAN=282;
public final static short COMMA=283;
public final static short ASSIGN=284;
public final static short DOT=285;
public final static short SEMICOLON=286;
public final static short CASE=287;
public final static short TUPLE=288;
public final static short INCLUDE=289;
public final static short UNAR_ARITHM=290;
public final static short NOT=291;
public final static short INCR=292;
public final static short POW=293;
public final static short MUL_ARITHM=294;
public final static short ADD_ARITHM=295;
public final static short XOR=296;
public final static short AND=297;
public final static short OR=298;
public final static short MORELESS=299;
public final static short EQ=300;
public final static short IFX=301;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    3,    3,    5,    5,    8,
    8,    6,    4,   10,   11,   11,    7,    7,    7,    7,
   13,    9,    9,   14,   14,   14,   14,   14,   14,   14,
   14,   14,   14,   15,   18,   18,   18,   16,   16,   16,
   17,   17,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   19,   19,   21,   21,   22,   20,
};
final static short yylen[] = {                            2,
    1,    2,    0,    1,    1,    7,    6,    0,    2,    2,
    1,    3,    3,    3,    0,    3,    2,    2,    4,    5,
    3,    0,    3,    1,    1,    1,    1,    1,    1,    2,
    2,    1,    1,    6,    0,    2,    2,    9,    5,    7,
    7,    5,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    2,    2,    2,    2,    1,    1,    1,    1,    1,
    1,    1,    1,    3,    3,    4,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    1,    0,    4,    5,    0,    0,
    0,    2,    0,    0,    0,   13,    0,    0,    0,    0,
    0,    0,    0,    0,   17,    0,   18,    0,    0,   11,
    9,    0,   14,    0,   58,    0,    0,    0,    0,   59,
    0,   60,   61,    0,    0,    0,   56,   57,   62,   63,
    0,    7,   10,   16,    6,    0,    0,   54,    0,   19,
    0,   55,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   32,   33,    0,    0,
    0,    0,    0,    0,    0,    0,   29,    0,   25,   26,
   27,    0,    0,   65,   67,   51,   20,    0,    0,    0,
    0,    0,    0,   47,   46,   30,    0,    0,    0,    0,
    0,    0,    0,   12,    0,   66,    0,    0,    0,    0,
    0,    0,    0,   23,    0,    0,    0,    0,    0,    0,
    0,   39,    0,    0,    0,   34,    0,    0,    0,   37,
   36,    0,   40,   41,    0,   38,
};
final static short yydgoto[] = {                          4,
    5,    6,    7,    8,   20,   52,   21,   31,   85,   16,
   23,   86,   87,   88,   89,   90,   91,  136,   47,   48,
   49,   50,
};
final static short yysindex[] = {                      -244,
 -257, -248, -235,    0,    0, -244,    0,    0, -224, -238,
 -214,    0, -223, -250, -250,    0, -250, -180, -205, -217,
 -206, -228, -188, -197,    0,  -86,    0, -183, -206,    0,
    0, -250,    0, -183,    0, -261,  -73, -177,  -73,    0,
 -165,    0,    0,  -73,  -73,  -74,    0,    0,    0,    0,
 -112,    0,    0,    0,    0,  -73, -163,    0, -226,    0,
  -10,    0,   60,   60, -161,  -73,  -73,  -73,  -73,  -73,
  -73,  -73,  -73, -259, -155,  -73,    0,    0, -152, -145,
 -144, -183, -143, -168, -132,   60,    0, -140,    0,    0,
    0,  -66, -167,    0,    0,    0,    0,   60,  -85, -207,
 -175, -277, -277,    0,    0,    0,   60,  -73, -250,  -73,
 -127,  -73,  -73,    0, -112,    0,   -1, -168, -135,    9,
 -120,   18,   60,    0, -183,  -73, -183,  -73, -183, -121,
   52,    0,   27, -113, -255,    0,  -73, -124, -183,    0,
    0,   37,    0,    0, -183,    0,
};
final static short yyrindex[] = {                       154,
    0,    0,    0,    0,    0,  154,    0,    0,    0,    0,
    0,    0,    0, -101,  -90,    0, -101,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -90,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -88,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -258, -185,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -109,    0, -105,    0,    0,    0,    0,
    0,    0, -234,    0,    0,    0,    0, -156,  -13,  -24,
  -28, -160, -154,    0,    0,    0, -104,    0,    0,    0,
    0,    0,    0,    0,  -88,    0,    0,    0,    0,    0,
    0,    0, -103,    0,    0,    0,    0,    0,    0,  -98,
    0,    0,    0,  -96,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  179,    0,    0,    0,  174,  -31,    6,  168,   83,    0,
  169,  -26,   93,    0,   68,    0,    0,    0,    0,    0,
  159,    0,
};
final static int YYTABLESIZE=360;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         46,
   27,    9,   55,   56,   53,   56,   51,   53,   18,   19,
   59,   10,   61,    1,   53,   79,    2,   63,   64,    3,
   22,   72,   73,   57,   11,   57,   30,   53,   64,   92,
   58,   64,   58,   14,   30,   13,   95,   22,   64,   98,
   99,  100,  101,  102,  103,  104,  105,   15,   17,  107,
  111,   64,   18,   19,   27,   28,   84,   32,   64,   64,
   64,   64,   64,   64,   64,   64,   66,   67,   68,   69,
   70,   71,   72,   73,   33,   34,   29,   52,   51,   25,
   52,  117,   60,  120,   26,  122,  123,   52,   69,   70,
   71,   72,   73,  130,   62,  132,   93,  134,   97,  131,
   52,  133,   48,  140,  106,   48,   45,  144,   49,   45,
  142,   49,   48,  146,  118,  113,   45,   57,   49,  108,
   84,   70,   71,   72,   73,   48,  109,  110,  112,   45,
  114,   49,   48,   48,   48,   48,   48,   48,   49,   49,
   49,   49,   49,   49,   35,  115,   18,   74,  121,   37,
  126,  128,  135,    3,   75,   76,   77,   78,   79,   39,
  139,  143,   80,   81,   82,   83,   40,   41,   42,   43,
   35,    8,   15,   36,   22,   37,   28,   44,   45,   38,
   24,   31,   21,   35,   12,   39,   36,   35,   37,   42,
   24,   65,   40,   41,   42,   43,   53,  124,   39,  116,
   54,  119,  141,   44,   45,   40,   41,   42,   43,   68,
   69,   70,   71,   72,   73,   94,   44,   45,   66,   67,
   68,   69,   70,   71,   72,   73,   66,   67,   68,   69,
   70,   71,   72,   73,   50,    0,    0,   50,   43,    0,
    0,   43,    0,    0,   50,    0,    0,    0,   43,   44,
    0,    0,   44,    0,    0,    0,    0,   50,    0,   44,
    0,   43,   96,    0,   50,   50,   50,   50,   43,   43,
   43,  125,   44,    0,    0,    0,    0,    0,    0,   44,
   44,  127,   66,   67,   68,   69,   70,   71,   72,   73,
  129,   66,   67,   68,   69,   70,   71,   72,   73,  138,
    0,   66,   67,   68,   69,   70,   71,   72,   73,  145,
   66,   67,   68,   69,   70,   71,   72,   73,    0,   66,
   67,   68,   69,   70,   71,   72,   73,    0,    0,   66,
   67,   68,   69,   70,   71,   72,   73,  137,    0,    0,
    0,    0,    0,    0,   66,   67,   68,   69,   70,   71,
   72,   73,   66,   67,   68,   69,   70,   71,   72,   73,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         26,
  260,  259,   34,  265,  263,  265,  262,  266,  259,  260,
   37,  260,   39,  258,  273,  271,  261,   44,   45,  264,
   15,  299,  300,  285,  260,  285,   21,  286,  263,   56,
  292,  266,  292,  272,   29,  260,  263,   32,  273,   66,
   67,   68,   69,   70,   71,   72,   73,  262,  272,   76,
   82,  286,  259,  260,  260,  273,   51,  286,  293,  294,
  295,  296,  297,  298,  299,  300,  293,  294,  295,  296,
  297,  298,  299,  300,  263,  273,  283,  263,  262,  260,
  266,  108,  260,  110,  265,  112,  113,  273,  296,  297,
  298,  299,  300,  125,  260,  127,  260,  129,  260,  126,
  286,  128,  263,  135,  260,  266,  263,  139,  263,  266,
  137,  266,  273,  145,  109,  284,  273,  285,  273,  272,
  115,  297,  298,  299,  300,  286,  272,  272,  272,  286,
  263,  286,  293,  294,  295,  296,  297,  298,  293,  294,
  295,  296,  297,  298,  257,  286,  259,  260,  276,  262,
  286,  272,  274,    0,  267,  268,  269,  270,  271,  272,
  274,  286,  275,  276,  277,  278,  279,  280,  281,  282,
  257,  273,  263,  260,  263,  262,  286,  290,  291,  266,
  286,  286,  286,  257,    6,  272,  260,  286,  262,  286,
   17,  266,  279,  280,  281,  282,   29,  115,  272,  266,
   32,  109,  135,  290,  291,  279,  280,  281,  282,  295,
  296,  297,  298,  299,  300,   57,  290,  291,  293,  294,
  295,  296,  297,  298,  299,  300,  293,  294,  295,  296,
  297,  298,  299,  300,  263,   -1,   -1,  266,  263,   -1,
   -1,  266,   -1,   -1,  273,   -1,   -1,   -1,  273,  263,
   -1,   -1,  266,   -1,   -1,   -1,   -1,  286,   -1,  273,
   -1,  286,  273,   -1,  293,  294,  295,  296,  293,  294,
  295,  273,  286,   -1,   -1,   -1,   -1,   -1,   -1,  293,
  294,  273,  293,  294,  295,  296,  297,  298,  299,  300,
  273,  293,  294,  295,  296,  297,  298,  299,  300,  273,
   -1,  293,  294,  295,  296,  297,  298,  299,  300,  273,
  293,  294,  295,  296,  297,  298,  299,  300,   -1,  293,
  294,  295,  296,  297,  298,  299,  300,   -1,   -1,  293,
  294,  295,  296,  297,  298,  299,  300,  286,   -1,   -1,
   -1,   -1,   -1,   -1,  293,  294,  295,  296,  297,  298,
  299,  300,  293,  294,  295,  296,  297,  298,  299,  300,
};
}
final static short YYFINAL=4;
final static short YYMAXTOKEN=301;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"INTEGER","DEFUN","TYPE","IDENTIFIER","PROC","L_CURBRACE",
"R_CURBRACE","STRUCT","L_SQBRACE","R_SQBRACE","GOTO","RETURN","BREAK",
"CONTINUE","IF","L_BRACE","R_BRACE","ELSE","FOR","WHILE","DO","SWITCH","FLOAT",
"NEW","STRING","BOOLEAN","COMMA","ASSIGN","DOT","SEMICOLON","CASE","TUPLE",
"INCLUDE","UNAR_ARITHM","NOT","INCR","POW","MUL_ARITHM","ADD_ARITHM","XOR",
"AND","OR","MORELESS","EQ","IFX",
};
final static String yyrule[] = {
"$accept : start",
"start : global_list",
"global_list : global global_list",
"global_list :",
"global : function",
"global : struct_decl",
"function : DEFUN TYPE IDENTIFIER L_BRACE param_list R_BRACE codeblock",
"function : PROC IDENTIFIER L_BRACE param_list R_BRACE codeblock",
"param_list :",
"param_list : decl param_tail",
"param_tail : COMMA param_tail",
"param_tail : decl",
"codeblock : L_CURBRACE stmt_list R_CURBRACE",
"struct_decl : STRUCT IDENTIFIER decl_block",
"decl_block : L_CURBRACE decl_list R_CURBRACE",
"decl_list :",
"decl_list : decl SEMICOLON decl_list",
"decl : TYPE IDENTIFIER",
"decl : IDENTIFIER IDENTIFIER",
"decl : TYPE L_SQBRACE R_SQBRACE IDENTIFIER",
"decl : TYPE L_SQBRACE exp R_SQBRACE IDENTIFIER",
"decl_assign : decl ASSIGN exp",
"stmt_list :",
"stmt_list : stmt SEMICOLON stmt_list",
"stmt : exp",
"stmt : if",
"stmt : loop",
"stmt : switch",
"stmt : decl",
"stmt : decl_assign",
"stmt : GOTO IDENTIFIER",
"stmt : RETURN exp",
"stmt : BREAK",
"stmt : CONTINUE",
"if : IF L_BRACE exp R_BRACE codeblock else",
"else :",
"else : ELSE if",
"else : ELSE codeblock",
"loop : FOR L_BRACE decl_assign SEMICOLON exp SEMICOLON exp R_BRACE codeblock",
"loop : WHILE L_BRACE exp R_BRACE codeblock",
"loop : DO codeblock WHILE L_BRACE exp R_BRACE SEMICOLON",
"switch : SWITCH L_BRACE exp R_BRACE codeblock ELSE codeblock",
"switch : SWITCH L_BRACE exp R_BRACE codeblock",
"exp : exp ADD_ARITHM exp",
"exp : exp MUL_ARITHM exp",
"exp : exp POW exp",
"exp : exp EQ exp",
"exp : exp MORELESS exp",
"exp : exp AND exp",
"exp : exp OR exp",
"exp : exp XOR exp",
"exp : L_BRACE exp R_BRACE",
"exp : NOT exp",
"exp : UNAR_ARITHM exp",
"exp : IDENTIFIER INCR",
"exp : NEW IDENTIFIER",
"exp : reference",
"exp : tuple_value",
"exp : INTEGER",
"exp : FLOAT",
"exp : STRING",
"exp : BOOLEAN",
"reference : structref",
"reference : arrayref",
"structref : IDENTIFIER DOT IDENTIFIER",
"structref : IDENTIFIER DOT structref",
"arrayref : IDENTIFIER L_SQBRACE exp R_SQBRACE",
"tuple_value : L_CURBRACE exp R_CURBRACE",
};

//#line 141 "./borzhch.y"

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
//#line 419 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
