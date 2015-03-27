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






//#line 6 "borzhch.y"
  import Lexer;
//#line 19 "Parser.java"




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
public final static short STRING=280;
public final static short BOOLEAN=281;
public final static short COMMA=282;
public final static short ASSIGN=283;
public final static short STRUCT_IDENTIFIER=284;
public final static short DOT=285;
public final static short SEMICOLON=286;
public final static short UNAR_ARITHM=287;
public final static short NOT=288;
public final static short INCR=289;
public final static short POW=290;
public final static short MUL_ARITHM=291;
public final static short ADD_ARITHM=292;
public final static short XOR=293;
public final static short AND=294;
public final static short OR=295;
public final static short MORELESS=296;
public final static short EQ=297;
public final static short IFX=298;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    1,    2,    2,    3,    3,    5,    8,    8,
    6,    4,   10,   11,   11,    7,    7,    7,   13,    9,
   14,   14,   14,   14,   14,   14,   14,   14,   14,   14,
   15,   18,   18,   18,   16,   16,   16,   17,   17,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   19,   19,   21,
   21,   22,   20,
};
final static short yylen[] = {                            2,
    0,    1,    2,    1,    1,    7,    6,    2,    2,    1,
    3,    3,    3,    0,    3,    2,    4,    5,    3,    2,
    2,    1,    1,    1,    2,    2,    3,    3,    2,    2,
    6,    0,    2,    2,    9,    5,    7,    7,    5,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    2,    2,
    2,    1,    1,    1,    1,    1,    1,    1,    1,    3,
    3,    4,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    2,    0,    4,    5,    0,    0,
    0,    3,    0,    0,    0,   12,    0,    0,    0,    0,
    0,    0,    0,   16,    0,    0,    0,   10,    8,    0,
   13,    0,   54,    0,    0,    0,    0,   55,   56,   57,
    0,    0,    0,   52,   53,   58,   59,    0,    7,    9,
   15,    6,    0,    0,   51,    0,   17,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   22,   23,   24,    0,    0,   61,   63,
   48,   18,    0,    0,    0,    0,    0,    0,   44,   43,
    0,    0,   29,   30,    0,    0,    0,    0,    0,    0,
   25,   11,   21,   26,   20,   62,   27,   28,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   36,    0,    0,    0,   31,    0,    0,    0,
   34,   33,    0,   37,   38,    0,   35,
};
final static short yydgoto[] = {                          4,
    5,    6,    7,    8,   19,   49,   20,   29,   80,   16,
   22,   81,   82,   83,   84,   85,   86,  137,   44,   45,
   46,   47,
};
final static short yysindex[] = {                      -143,
 -254, -257, -272,    0,    0, -143,    0,    0, -242, -252,
 -233,    0, -223, -195, -195,    0, -195, -251, -207, -238,
 -219, -187, -205,    0,  -15, -185, -238,    0,    0, -195,
    0, -185,    0, -261,   -4, -167,   -4,    0,    0,    0,
   -4,   -4,   -5,    0,    0,    0,    0, -197,    0,    0,
    0,    0,   -4, -159,    0, -255,    0,   49,  154,  154,
 -153,   -4,   -4,   -4,   -4,   -4,   -4,   -4,   -4, -150,
   -4, -177, -175, -156, -152, -149, -185, -148, -264, -141,
  122, -145, -197,    0,    0,    0,    3, -158,    0,    0,
    0,    0,  154, -140, -208, -151, -249, -249,    0,    0,
 -136,  134,    0,    0,   -4, -195,   -4, -138,   -4,   -4,
    0,    0,    0,    0,    0,    0,    0,    0,   60, -122,
 -127,   74, -109,   85,  154, -185,   -4, -185,   -4, -185,
 -104,  146,    0,   99, -102, -260,    0,   -4, -113, -185,
    0,    0,  110,    0,    0, -185,    0,
};
final static short yyrindex[] = {                       174,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -81,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -81,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -256, -160,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -161,    0,    0,
    0,    0, -124, -240,   46,   38, -126, -115,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -101,    0,    0,    0,    0,    0,
  -76,    0,    0,    0,  -44,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  181,    0,    0,    0,  171,  -32,  -14,  162,  107,    0,
  167,  -10,   92,    0,   70,    0,    0,    0,    0,    0,
  153,    0,
};
final static int YYTABLESIZE=451;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         52,
   21,   48,   10,   53,    9,   28,   50,   90,   24,   50,
   74,   11,   28,   25,   43,   21,   50,   13,  110,   14,
   18,  111,   41,   54,   56,   41,   58,   55,   15,   50,
   59,   60,   41,   79,   62,   63,   64,   65,   66,   67,
   68,   69,   87,   27,  108,   41,   68,   69,   17,   41,
   41,   93,   94,   95,   96,   97,   98,   99,  100,   33,
  102,   18,   34,   18,   35,   26,   30,   32,   79,   70,
   71,   72,   73,   74,   37,   31,   48,   75,   76,   77,
   78,   38,   39,   40,   65,   66,   67,   68,   69,   41,
   42,  120,   57,  131,  119,  133,  122,  135,  124,  125,
   88,   60,   49,  141,   60,   49,   92,  145,  103,  101,
  104,   60,   49,  147,    1,  105,  132,    2,  134,  106,
    3,  112,  107,  109,   60,   49,   54,  143,   60,   60,
   60,   60,   60,   60,   60,   60,   45,  123,   42,   45,
  114,   42,   66,   67,   68,   69,   45,   46,   42,  117,
   46,   64,   65,   66,   67,   68,   69,   46,  127,   45,
  110,   42,  129,   45,   45,   45,   45,   45,   45,  136,
   46,  140,  144,    1,   46,   46,   46,   46,   46,   46,
   32,   14,   32,   32,   19,   32,   12,   23,   50,  115,
   32,   32,   32,   32,   32,   32,   51,  121,   32,   32,
   32,   32,   32,   32,   32,  142,   89,    0,    0,    0,
   32,   32,   39,    0,   39,   39,    0,   39,    0,    0,
    0,    0,   39,   39,   39,   39,   39,   39,    0,    0,
   39,   39,   39,   39,   39,   39,   39,    0,    0,    0,
    0,   33,   39,   39,   34,    0,   35,    0,    0,    0,
   36,    0,   33,    0,    0,   34,   37,   35,    0,    0,
   61,    0,    0,   38,   39,   40,    0,   37,  116,    0,
    0,   41,   42,    0,   38,   39,   40,    0,    0,    0,
    0,    0,   41,   42,   62,   63,   64,   65,   66,   67,
   68,   69,   62,   63,   64,   65,   66,   67,   68,   69,
   47,    0,    0,   47,    0,    0,    0,    0,   40,    0,
   47,   40,    0,    0,    0,    0,    0,    0,   40,    0,
    0,   91,    0,   47,    0,    0,    0,   47,   47,   47,
   47,   40,  126,    0,    0,   40,   40,   40,   62,   63,
   64,   65,   66,   67,   68,   69,  128,    0,    0,   62,
   63,   64,   65,   66,   67,   68,   69,  130,    0,    0,
    0,    0,    0,   62,   63,   64,   65,   66,   67,   68,
   69,  139,    0,    0,   62,   63,   64,   65,   66,   67,
   68,   69,  146,    0,    0,    0,    0,    0,   62,   63,
   64,   65,   66,   67,   68,   69,    0,    0,    0,   62,
   63,   64,   65,   66,   67,   68,   69,  113,    0,    0,
    0,   62,   63,   64,   65,   66,   67,   68,   69,  118,
    0,    0,    0,   62,   63,   64,   65,   66,   67,   68,
   69,  138,    0,    0,    0,   62,   63,   64,   65,   66,
   67,   68,   69,   62,   63,   64,   65,   66,   67,   68,
   69,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         32,
   15,  262,  260,  265,  259,   20,  263,  263,  260,  266,
  271,  284,   27,  265,   25,   30,  273,  260,  283,  272,
  259,  286,  263,  285,   35,  266,   37,  289,  262,  286,
   41,   42,  273,   48,  290,  291,  292,  293,  294,  295,
  296,  297,   53,  282,   77,  286,  296,  297,  272,  290,
  291,   62,   63,   64,   65,   66,   67,   68,   69,  257,
   71,  259,  260,  259,  262,  273,  286,  273,   83,  267,
  268,  269,  270,  271,  272,  263,  262,  275,  276,  277,
  278,  279,  280,  281,  293,  294,  295,  296,  297,  287,
  288,  106,  260,  126,  105,  128,  107,  130,  109,  110,
  260,  263,  263,  136,  266,  266,  260,  140,  286,  260,
  286,  273,  273,  146,  258,  272,  127,  261,  129,  272,
  264,  263,  272,  272,  286,  286,  285,  138,  290,  291,
  292,  293,  294,  295,  296,  297,  263,  276,  263,  266,
  286,  266,  294,  295,  296,  297,  273,  263,  273,  286,
  266,  292,  293,  294,  295,  296,  297,  273,  286,  286,
  283,  286,  272,  290,  291,  292,  293,  294,  295,  274,
  286,  274,  286,    0,  290,  291,  292,  293,  294,  295,
  257,  263,  259,  260,  286,  262,    6,   17,   27,   83,
  267,  268,  269,  270,  271,  272,   30,  106,  275,  276,
  277,  278,  279,  280,  281,  136,   54,   -1,   -1,   -1,
  287,  288,  257,   -1,  259,  260,   -1,  262,   -1,   -1,
   -1,   -1,  267,  268,  269,  270,  271,  272,   -1,   -1,
  275,  276,  277,  278,  279,  280,  281,   -1,   -1,   -1,
   -1,  257,  287,  288,  260,   -1,  262,   -1,   -1,   -1,
  266,   -1,  257,   -1,   -1,  260,  272,  262,   -1,   -1,
  266,   -1,   -1,  279,  280,  281,   -1,  272,  266,   -1,
   -1,  287,  288,   -1,  279,  280,  281,   -1,   -1,   -1,
   -1,   -1,  287,  288,  290,  291,  292,  293,  294,  295,
  296,  297,  290,  291,  292,  293,  294,  295,  296,  297,
  263,   -1,   -1,  266,   -1,   -1,   -1,   -1,  263,   -1,
  273,  266,   -1,   -1,   -1,   -1,   -1,   -1,  273,   -1,
   -1,  273,   -1,  286,   -1,   -1,   -1,  290,  291,  292,
  293,  286,  273,   -1,   -1,  290,  291,  292,  290,  291,
  292,  293,  294,  295,  296,  297,  273,   -1,   -1,  290,
  291,  292,  293,  294,  295,  296,  297,  273,   -1,   -1,
   -1,   -1,   -1,  290,  291,  292,  293,  294,  295,  296,
  297,  273,   -1,   -1,  290,  291,  292,  293,  294,  295,
  296,  297,  273,   -1,   -1,   -1,   -1,   -1,  290,  291,
  292,  293,  294,  295,  296,  297,   -1,   -1,   -1,  290,
  291,  292,  293,  294,  295,  296,  297,  286,   -1,   -1,
   -1,  290,  291,  292,  293,  294,  295,  296,  297,  286,
   -1,   -1,   -1,  290,  291,  292,  293,  294,  295,  296,
  297,  286,   -1,   -1,   -1,  290,  291,  292,  293,  294,
  295,  296,  297,  290,  291,  292,  293,  294,  295,  296,
  297,
};
}
final static short YYFINAL=4;
final static short YYMAXTOKEN=298;
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
"STRING","BOOLEAN","COMMA","ASSIGN","STRUCT_IDENTIFIER","DOT","SEMICOLON",
"UNAR_ARITHM","NOT","INCR","POW","MUL_ARITHM","ADD_ARITHM","XOR","AND","OR",
"MORELESS","EQ","IFX",
};
final static String yyrule[] = {
"$accept : start",
"start :",
"start : global_list",
"global_list : global global_list",
"global : function",
"global : struct_decl",
"function : DEFUN TYPE IDENTIFIER L_BRACE param_list R_BRACE codeblock",
"function : PROC IDENTIFIER L_BRACE param_list R_BRACE codeblock",
"param_list : decl param_tail",
"param_tail : COMMA param_tail",
"param_tail : decl",
"codeblock : L_CURBRACE stmt_list R_CURBRACE",
"struct_decl : STRUCT STRUCT_IDENTIFIER decl_block",
"decl_block : L_CURBRACE decl_list R_CURBRACE",
"decl_list :",
"decl_list : decl SEMICOLON decl_list",
"decl : TYPE IDENTIFIER",
"decl : TYPE L_SQBRACE R_SQBRACE IDENTIFIER",
"decl : TYPE L_SQBRACE exp R_SQBRACE IDENTIFIER",
"decl_assign : decl ASSIGN exp",
"stmt_list : stmt stmt_list",
"stmt : exp SEMICOLON",
"stmt : if",
"stmt : loop",
"stmt : switch",
"stmt : decl SEMICOLON",
"stmt : decl_assign SEMICOLON",
"stmt : GOTO IDENTIFIER SEMICOLON",
"stmt : RETURN exp SEMICOLON",
"stmt : BREAK SEMICOLON",
"stmt : CONTINUE SEMICOLON",
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
