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
  import edu.borzhch.ast.*;
  import edu.borzhch.helpers.*;
  import edu.borzhch.SymTable;
  
//#line 24 "Parser.java"




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
public final static short TYPE=257;
public final static short IDENTIFIER=258;
public final static short INTEGER=259;
public final static short DEFUN=260;
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
    0,   12,    1,    1,    2,    2,   13,   14,    3,    3,
   15,   15,   15,   16,   16,    5,    4,   17,   18,   18,
    8,    8,    8,    8,    9,    6,    6,    6,    6,    6,
    7,    7,    7,    7,    7,    7,    7,   10,   10,   10,
   19,   24,   24,   24,   20,   20,   20,   21,   21,   11,
   11,   11,   11,   11,   11,   11,   11,   11,   11,   11,
   11,   11,   11,   11,   11,   11,   11,   11,   11,   11,
   11,   26,   26,   22,   22,   23,   27,   25,   25,   25,
   28,   28,
};
final static short yylen[] = {                            2,
    2,    0,    2,    0,    1,    1,    1,    1,    7,    6,
    0,    1,    2,    3,    2,    3,    3,    3,    0,    3,
    2,    2,    4,    5,    3,    0,    3,    2,    2,    2,
    1,    1,    1,    2,    2,    1,    1,    3,    3,    3,
    6,    0,    2,    2,    9,    5,    7,    7,    5,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    4,    2,
    2,    2,    2,    4,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    3,    3,    4,    3,    0,    1,    2,
    3,    2,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    1,    0,    5,    6,    0,
    0,    0,    3,    0,    0,    0,   17,    0,    0,    0,
    0,    0,    0,    0,    0,   21,    0,   22,    0,   13,
    0,    0,   18,    0,    0,   68,    0,    0,    0,   69,
    0,   70,   71,    0,    0,    0,   72,   73,   65,   66,
    0,    7,   10,    0,   20,    9,    0,    0,    0,   62,
    0,    0,   23,    0,    0,   63,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   14,    0,    0,
    0,   36,   37,    0,    0,    0,    0,    0,    0,    0,
    0,   32,   33,    0,    0,    0,    0,    0,    0,    0,
    0,   75,    0,   80,   77,    0,   58,   24,    0,    0,
    0,    0,    0,    0,   54,   53,    0,   34,    0,    0,
    0,    0,    0,    0,    8,   16,    0,    0,   28,   29,
   30,    0,    0,   76,   64,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   27,    0,    0,    0,   81,    0,
    0,    0,    0,    0,    0,    0,   46,    0,    0,    0,
   41,    0,    0,    0,   44,   43,    0,   47,   48,    0,
   45,
};
final static short yydgoto[] = {                          1,
    6,    7,    8,    9,   53,   89,   90,   91,   92,   93,
   61,    2,   54,  126,   22,   30,   17,   24,   94,   95,
   96,   47,   48,  161,   62,   49,   50,  104,
};
final static short yysindex[] = {                         0,
    0, -179, -246, -231, -220,    0, -179,    0,    0, -206,
 -205, -193,    0, -197, -185, -185,    0, -185, -252, -175,
 -263, -194, -196, -170, -177,    0,  -17,    0, -185,    0,
 -154, -185,    0, -154, -255,    0,  -12, -148,  -71,    0,
 -146,    0,    0,  -12,  -12,  -68,    0,    0,    0,    0,
 -263,    0,    0,  147,    0,    0,  -12,  -12, -144,    0,
   53, -140,    0, -158,   16,    0,  141,  141, -127,  -12,
  -12,  -12,  -12,  -12,  -12,  -12,  -12,    0, -234, -125,
  -12,    0,    0, -153, -135, -131, -154, -122,  -99, -107,
 -103,    0,    0,  147,  147,  147, -100,  -95,  -60,  -74,
 -147,    0,  -12,    0,    0,  -12,    0,    0,  141,  -82,
 -129, -242, -213, -213,    0,    0,  -12,    0,  141,  -12,
 -185,  -12,  -72,  -12,    0,    0,  147,  -12,    0,    0,
    0,  -12,  -12,    0,    0,   53,  141,  141,   25, -103,
  -84,   33,  -67,   44,    0,  141,  141,  141,    0, -154,
  -12, -154,  -12, -154,  -62,  133,    0,   61,  -53, -257,
    0,  -12,  -79, -154,    0,    0,   72,    0,    0, -154,
    0,
};
final static short yyrindex[] = {                         0,
    0,  222,    0,    0,    0,    0,  222,    0,    0,    0,
    0,    0,    0,    0,  -50,  -39,    0,  -50,    0,    0,
  -30,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -39,    0,    0, -139,    0,  -19,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -25,    0,    0,  -10,    0,    0,    0,  -22,    0,    0,
 -238,    0,    0,    0,    0,    0, -189, -138,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -32,    0,    0,  -10,  -10,  -10,    0,    0,    0,    0,
 -195,    0,    0,    0,    0,    0,    0,    0, -134,   14,
    9, -254, -120, -101,    0,    0,    0,    0,  -29,    0,
    0,    0,    0,    0,    0,    0,  -10,    0,    0,    0,
    0,    0,    0,    0,    0, -237,  -83,  -28,    0,    0,
    0,    0,    0,    0,    0,  -27,  -20,  -15,    0,    0,
    0,    0,    0,    0,  116,    0,    0,    0,  132,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
  245,    0,    0,    0,  -34,  -73,    0,  -14,  135,    0,
  -11,    0,    0,    0,  243,  225,    0,  249,  123,    0,
    0,  -51,  -47,    0,  226,    0,    0,  149,
};
final static int YYTABLESIZE=441;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         56,
   21,   23,   97,   21,   52,   26,   98,  102,   57,   57,
   10,   57,   27,   84,   51,   46,   58,   23,   57,   29,
  129,  130,  131,   28,   79,   82,   11,   65,   57,   59,
   57,   57,   67,   68,   79,   82,   60,   12,   57,   57,
   57,   57,   97,   97,   97,   99,   98,   98,   98,  117,
   59,   14,  123,  145,   74,   75,   76,   77,  109,  110,
  111,  112,  113,  114,  115,  116,   15,   74,   16,  119,
   74,   19,   20,   61,   18,   97,   61,   74,   31,   98,
    3,    4,   28,   61,    5,   76,   77,   74,   74,   32,
   74,  136,   33,   61,  137,   34,   61,   74,   74,   74,
   74,   74,   74,   74,   74,  138,  140,   52,  139,   63,
  142,   66,  144,  101,  106,  155,  146,  157,  120,  159,
  147,  148,  105,   67,   60,  165,   67,   60,   52,  169,
  108,   52,  118,   67,   60,  171,  121,   59,   52,  156,
  122,  158,   55,   67,   60,   55,   67,   60,   52,  124,
  167,   52,   55,   67,   67,   67,   67,   67,   67,   67,
   67,   56,   55,  125,   56,   55,   73,   74,   75,   76,
   77,   56,   55,   55,   55,   55,   55,   55,  127,   59,
  128,   56,   59,  132,   56,   64,   35,   36,  133,   59,
   37,   56,   56,   56,   56,   56,   56,   69,  135,   59,
   39,  151,   59,  143,  153,  134,  168,   40,   41,   42,
   43,  160,   72,   73,   74,   75,   76,   77,   44,   45,
  164,    4,   11,   19,   70,   71,   72,   73,   74,   75,
   76,   77,   70,   71,   72,   73,   74,   75,   76,   77,
   35,   36,   12,   78,   37,   35,   36,   15,   38,   37,
   78,   13,   26,   31,   39,  141,   35,   39,   25,   39,
   25,   40,   41,   42,   43,   38,   40,   41,   42,   43,
   40,   50,   44,   45,   50,   78,   51,   44,   45,   51,
   55,   50,  166,  100,  149,    0,   51,    0,  107,    0,
    0,   50,    0,    0,   50,    0,   51,  150,    0,   51,
    0,   50,   50,   50,    0,  152,   51,   51,   70,   71,
   72,   73,   74,   75,   76,   77,  154,   70,   71,   72,
   73,   74,   75,   76,   77,   70,   71,   72,   73,   74,
   75,   76,   77,  163,    0,  103,   70,   71,   72,   73,
   74,   75,   76,   77,  170,   70,   71,   72,   73,   74,
   75,   76,   77,   70,   71,   72,   73,   74,   75,   76,
   77,    0,    0,    0,   70,   71,   72,   73,   74,   75,
   76,   77,   42,   42,    0,    0,    0,    0,   42,    0,
    0,    0,   42,   42,   42,   42,   42,    0,   49,   49,
   42,   42,   42,   42,   49,    0,    0,    0,   49,   49,
   49,   49,   49,   19,   79,    0,   49,   49,   49,   49,
    0,    0,    0,   80,   81,   82,   83,   84,  162,    0,
    0,   85,   86,   87,   88,   70,   71,   72,   73,   74,
   75,   76,   77,   70,   71,   72,   73,   74,   75,   76,
   77,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         34,
   15,   16,   54,   18,  262,  258,   54,   59,  263,  265,
  257,  266,  265,  271,   29,   27,  272,   32,  273,  283,
   94,   95,   96,  258,  263,  263,  258,   39,  283,  285,
  265,  286,   44,   45,  273,  273,  292,  258,  293,  294,
  295,  296,   94,   95,   96,   57,   94,   95,   96,  284,
  285,  258,   87,  127,  297,  298,  299,  300,   70,   71,
   72,   73,   74,   75,   76,   77,  272,  263,  262,   81,
  266,  257,  258,  263,  272,  127,  266,  273,  273,  127,
  260,  261,  258,  273,  264,  299,  300,  283,  284,  286,
  286,  103,  263,  283,  106,  273,  286,  293,  294,  295,
  296,  297,  298,  299,  300,  117,  121,  262,  120,  258,
  122,  258,  124,  258,  273,  150,  128,  152,  272,  154,
  132,  133,  263,  263,  263,  160,  266,  266,  263,  164,
  258,  266,  258,  273,  273,  170,  272,  285,  273,  151,
  272,  153,  263,  283,  283,  266,  286,  286,  283,  272,
  162,  286,  273,  293,  294,  295,  296,  297,  298,  299,
  300,  263,  283,  263,  266,  286,  296,  297,  298,  299,
  300,  273,  293,  294,  295,  296,  297,  298,  286,  263,
  284,  283,  266,  284,  286,  257,  258,  259,  284,  273,
  262,  293,  294,  295,  296,  297,  298,  266,  273,  283,
  272,  286,  286,  276,  272,  266,  286,  279,  280,  281,
  282,  274,  295,  296,  297,  298,  299,  300,  290,  291,
  274,    0,  273,  263,  293,  294,  295,  296,  297,  298,
  299,  300,  293,  294,  295,  296,  297,  298,  299,  300,
  258,  259,  273,  263,  262,  258,  259,  273,  266,  262,
  273,    7,  263,  286,  272,  121,  286,  286,  286,  272,
   18,  279,  280,  281,  282,  286,  279,  280,  281,  282,
  286,  263,  290,  291,  266,   51,  263,  290,  291,  266,
   32,  273,  160,   58,  136,   -1,  273,   -1,  273,   -1,
   -1,  283,   -1,   -1,  286,   -1,  283,  273,   -1,  286,
   -1,  293,  294,  295,   -1,  273,  293,  294,  293,  294,
  295,  296,  297,  298,  299,  300,  273,  293,  294,  295,
  296,  297,  298,  299,  300,  293,  294,  295,  296,  297,
  298,  299,  300,  273,   -1,  283,  293,  294,  295,  296,
  297,  298,  299,  300,  273,  293,  294,  295,  296,  297,
  298,  299,  300,  293,  294,  295,  296,  297,  298,  299,
  300,   -1,   -1,   -1,  293,  294,  295,  296,  297,  298,
  299,  300,  257,  258,   -1,   -1,   -1,   -1,  263,   -1,
   -1,   -1,  267,  268,  269,  270,  271,   -1,  257,  258,
  275,  276,  277,  278,  263,   -1,   -1,   -1,  267,  268,
  269,  270,  271,  257,  258,   -1,  275,  276,  277,  278,
   -1,   -1,   -1,  267,  268,  269,  270,  271,  286,   -1,
   -1,  275,  276,  277,  278,  293,  294,  295,  296,  297,
  298,  299,  300,  293,  294,  295,  296,  297,  298,  299,
  300,
};
}
final static short YYFINAL=1;
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
null,null,null,"TYPE","IDENTIFIER","INTEGER","DEFUN","PROC","L_CURBRACE",
"R_CURBRACE","STRUCT","L_SQBRACE","R_SQBRACE","GOTO","RETURN","BREAK",
"CONTINUE","IF","L_BRACE","R_BRACE","ELSE","FOR","WHILE","DO","SWITCH","FLOAT",
"NEW","STRING","BOOLEAN","COMMA","ASSIGN","DOT","SEMICOLON","CASE","TUPLE",
"INCLUDE","UNAR_ARITHM","NOT","INCR","POW","MUL_ARITHM","ADD_ARITHM","XOR",
"AND","OR","MORELESS","EQ","IFX",
};
final static String yyrule[] = {
"$accept : start",
"start : init global_list",
"init :",
"global_list : global global_list",
"global_list :",
"global : function",
"global : struct_decl",
"openblock : L_CURBRACE",
"endblock : R_CURBRACE",
"function : DEFUN TYPE IDENTIFIER L_BRACE param_list R_BRACE codeblock",
"function : PROC IDENTIFIER L_BRACE param_list R_BRACE codeblock",
"param_list :",
"param_list : decl",
"param_list : decl param_tail",
"param_tail : COMMA decl param_tail",
"param_tail : COMMA decl",
"codeblock : openblock stmt_list endblock",
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
"stmt_list : if stmt_list",
"stmt_list : loop stmt_list",
"stmt_list : switch stmt_list",
"stmt : decl",
"stmt : decl_assign",
"stmt : assign",
"stmt : GOTO IDENTIFIER",
"stmt : RETURN exp",
"stmt : BREAK",
"stmt : CONTINUE",
"assign : structref ASSIGN exp",
"assign : IDENTIFIER ASSIGN exp",
"assign : arrayref ASSIGN exp",
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
"exp : L_BRACE TYPE R_BRACE exp",
"exp : NOT exp",
"exp : UNAR_ARITHM exp",
"exp : IDENTIFIER INCR",
"exp : NEW IDENTIFIER",
"exp : IDENTIFIER L_BRACE exp_list R_BRACE",
"exp : reference",
"exp : tuple_value",
"exp : IDENTIFIER",
"exp : INTEGER",
"exp : FLOAT",
"exp : STRING",
"exp : BOOLEAN",
"reference : structref",
"reference : arrayref",
"structref : IDENTIFIER DOT IDENTIFIER",
"structref : IDENTIFIER DOT structref",
"arrayref : IDENTIFIER L_SQBRACE exp R_SQBRACE",
"tuple_value : L_CURBRACE exp_list R_CURBRACE",
"exp_list :",
"exp_list : exp",
"exp_list : exp exp_tail",
"exp_tail : COMMA exp exp_tail",
"exp_tail : COMMA exp",
};

//#line 298 "./borzhch.y"

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
//#line 494 "Parser.java"
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
case 1:
//#line 38 "./borzhch.y"
{ 
        TreeAST.setRoot((NodeAST) val_peek(0).obj); 
     }
break;
case 2:
//#line 43 "./borzhch.y"
{
        topTable = new SymTable(null);
        funcTable = new SymTable(null);
        structTable = new SymTable(null);
    }
break;
case 3:
//#line 51 "./borzhch.y"
{ 
        StatementList list = new StatementList(); 
        list.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) list.addAll((NodeList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 4:
//#line 57 "./borzhch.y"
{ yyval.obj = null; }
break;
case 5:
//#line 60 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 6:
//#line 61 "./borzhch.y"
{ yyval.obj = null; }
break;
case 7:
//#line 64 "./borzhch.y"
{
            topTable = new SymTable(topTable);
         }
break;
case 8:
//#line 69 "./borzhch.y"
{
            SymTable oldTable = topTable;
            topTable = oldTable.getPrevious();
            oldTable.setPrevious(null);
            oldTable.clear();
        }
break;
case 9:
//#line 77 "./borzhch.y"
{
        FunctionNode func = null;

        if (isTypeExist(val_peek(5).sval)) {
            func = new FunctionNode(val_peek(4).sval, val_peek(5).sval);
        } else {
            String msg = String.format("unknown type <%s>\n", val_peek(5).sval);
            System.err.println(msg);
        }

        func.setStatements((StatementList) val_peek(0).obj);

        funcTable.pushSymbol(val_peek(4).sval, val_peek(5).sval);        

        yyval.obj = func;
    }
break;
case 10:
//#line 93 "./borzhch.y"
{
        FunctionNode func = new FunctionNode(val_peek(4).sval, BOHelper.getType("void"));
        func.setStatements((StatementList) val_peek(0).obj);

        funcTable.pushSymbol(val_peek(4).sval, "void");
    
        yyval.obj = func;
    }
break;
case 16:
//#line 113 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 17:
//#line 116 "./borzhch.y"
{
              if(isIdentifierExist(val_peek(1).sval)) {
                String msg = String.format("identifier <%s> already in use\n", val_peek(1).sval);
                System.err.println(msg);
              }
              structTable.pushSymbol(val_peek(1).sval, "ref");
           }
break;
case 21:
//#line 132 "./borzhch.y"
{ 
        if(isIdentifierExist(val_peek(0).sval)) {
          String msg = String.format("identifier <%s> already in use\n", val_peek(0).sval);
          System.err.println(msg);
        }
        if(!isTypeExist(val_peek(1).sval)) {
          String msg = String.format("unknown type <%s>\n", val_peek(1).sval);
          System.err.println(msg);
        }
        DeclarationNode decl = new DeclarationNode(val_peek(0).sval, BOHelper.getType(val_peek(1).sval));
        topTable.pushSymbol(val_peek(0).sval, val_peek(1).sval);
        yyval.obj = decl;  
    }
break;
case 22:
//#line 145 "./borzhch.y"
{
        if(isIdentifierExist(val_peek(0).sval)) {
          String msg = String.format("identifier <%s> already in use\n", val_peek(0).sval);
          System.err.println(msg);
        }
        if(!isTypeExist(val_peek(1).sval)) {
          String msg = String.format("unknown type <%s>\n", val_peek(1).sval);
          System.err.println(msg);
        }

        DeclarationNode decl = new DeclarationNode(val_peek(0).sval, val_peek(1).sval);

        topTable.pushSymbol(val_peek(0).sval, val_peek(1).sval);

        yyval.obj = decl;
    }
break;
case 23:
//#line 161 "./borzhch.y"
{ 
        if(isIdentifierExist(val_peek(0).sval)) {
          String msg = String.format("identifier <%s> already in use\n", val_peek(0).sval);
          System.err.println(msg);
        }
        if(!isTypeExist(val_peek(3).sval)) {
          String msg = String.format("unknown type <%s>\n", val_peek(3).sval);
          System.err.println(msg);
        }
        
        topTable.pushSymbol(val_peek(0).sval, "ref");

        yyval.obj = null;
    }
break;
case 24:
//#line 175 "./borzhch.y"
{ 
        if(isIdentifierExist(val_peek(0).sval)) {
          String msg = String.format("identifier <%s> already in use\n", val_peek(0).sval);
          System.err.println(msg);
        }
        if(!isTypeExist(val_peek(4).sval)) {
          String msg = String.format("unknown type <%s>\n", val_peek(4).sval);
          System.err.println(msg);
        }
        
        topTable.pushSymbol(val_peek(0).sval, "ref");
        
        yyval.obj = null;
    }
break;
case 25:
//#line 191 "./borzhch.y"
{
        DeclarationNode decl = (DeclarationNode) val_peek(2).obj;
        AssignNode an = new AssignNode(decl.getName(), (NodeAST) val_peek(0).obj);

        StatementList list = new StatementList();
        list.add(decl);
        list.add(an);
        yyval.obj = list;
    }
break;
case 26:
//#line 202 "./borzhch.y"
{ yyval.obj = null; }
break;
case 27:
//#line 203 "./borzhch.y"
{ 
             StatementList list = new StatementList();
             list.add((NodeAST) val_peek(2).obj);
             if (val_peek(0).obj != null) list.addAll((StatementList) val_peek(0).obj);
         }
break;
case 28:
//#line 208 "./borzhch.y"
{ yyval.obj = null; }
break;
case 29:
//#line 209 "./borzhch.y"
{ yyval.obj = null; }
break;
case 30:
//#line 210 "./borzhch.y"
{ yyval.obj = null; }
break;
case 31:
//#line 213 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 32:
//#line 214 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 33:
//#line 215 "./borzhch.y"
{ yyval.obj = null; }
break;
case 34:
//#line 216 "./borzhch.y"
{ yyval.obj = null; }
break;
case 35:
//#line 217 "./borzhch.y"
{ yyval.obj = null; }
break;
case 36:
//#line 218 "./borzhch.y"
{ yyval.obj = null; }
break;
case 37:
//#line 219 "./borzhch.y"
{ yyval.obj = null; }
break;
case 50:
//#line 244 "./borzhch.y"
{ yyval.obj = null; }
break;
case 51:
//#line 245 "./borzhch.y"
{ yyval.obj = null; }
break;
case 52:
//#line 246 "./borzhch.y"
{ yyval.obj = null; }
break;
case 53:
//#line 247 "./borzhch.y"
{ yyval.obj = null; }
break;
case 54:
//#line 248 "./borzhch.y"
{ yyval.obj = null; }
break;
case 55:
//#line 249 "./borzhch.y"
{ yyval.obj = null; }
break;
case 56:
//#line 250 "./borzhch.y"
{ yyval.obj = null; }
break;
case 57:
//#line 251 "./borzhch.y"
{ yyval.obj = null; }
break;
case 58:
//#line 257 "./borzhch.y"
{ yyval.obj = null; }
break;
case 59:
//#line 258 "./borzhch.y"
{ yyval.obj = null; }
break;
case 60:
//#line 260 "./borzhch.y"
{ yyval.obj = null; }
break;
case 61:
//#line 261 "./borzhch.y"
{ yyval.obj = null; }
break;
case 62:
//#line 262 "./borzhch.y"
{ yyval.obj = null; }
break;
case 63:
//#line 263 "./borzhch.y"
{ yyval.obj = null; }
break;
case 64:
//#line 264 "./borzhch.y"
{ yyval.obj = null; }
break;
case 65:
//#line 265 "./borzhch.y"
{ yyval.obj = null; }
break;
case 66:
//#line 266 "./borzhch.y"
{ yyval.obj = null; }
break;
case 67:
//#line 267 "./borzhch.y"
{ yyval.obj = null; }
break;
case 68:
//#line 268 "./borzhch.y"
{ yyval.obj = new IntegerNode(val_peek(0).ival); }
break;
case 69:
//#line 269 "./borzhch.y"
{ yyval.obj = null; }
break;
case 70:
//#line 270 "./borzhch.y"
{ yyval.obj = null; }
break;
case 71:
//#line 271 "./borzhch.y"
{ yyval.obj = null; }
break;
//#line 958 "Parser.java"
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
