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
//#line 22 "Parser.java"




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
public final static short FLOAT=260;
public final static short DEFUN=261;
public final static short PROC=262;
public final static short L_CURBRACE=263;
public final static short R_CURBRACE=264;
public final static short STRUCT=265;
public final static short L_SQBRACE=266;
public final static short R_SQBRACE=267;
public final static short GOTO=268;
public final static short RETURN=269;
public final static short BREAK=270;
public final static short CONTINUE=271;
public final static short IF=272;
public final static short L_BRACE=273;
public final static short R_BRACE=274;
public final static short ELSE=275;
public final static short FOR=276;
public final static short WHILE=277;
public final static short DO=278;
public final static short SWITCH=279;
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
public final static short UN_MINUS=290;
public final static short UN_PLUS=291;
public final static short INCR=292;
public final static short MUL_ARITHM=293;
public final static short ADD_ARITHM=294;
public final static short MORELESS=295;
public final static short EQ=296;
public final static short IFX=297;
public final static short AND=298;
public final static short OR=299;
public final static short XOR=300;
public final static short POW=301;
public final static short UN_ARITHM=302;
public final static short NOT=303;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    3,    3,   15,   15,   15,
   16,   16,    5,    4,   17,   18,   18,    8,    8,    8,
    8,    9,    6,    6,    6,    6,    6,    7,    7,    7,
    7,    7,    7,    7,   10,   10,   10,   19,   22,   22,
   22,   20,   20,   20,   21,   21,   11,   11,   11,   11,
   11,   11,   11,   11,   11,   11,   11,   11,   11,   11,
   11,   11,   11,   11,   11,   11,   12,   12,   13,   13,
   14,   23,   24,   24,   24,   25,   25,
};
final static short yylen[] = {                            2,
    1,    2,    0,    1,    1,    7,    6,    0,    1,    2,
    3,    2,    3,    3,    3,    0,    3,    2,    2,    4,
    5,    3,    0,    3,    2,    2,    2,    1,    1,    1,
    2,    2,    1,    1,    3,    3,    3,    6,    0,    2,
    2,    9,    5,    7,    7,    5,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    2,    2,    2,    2,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    3,
    4,    3,    0,    1,    2,    3,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    1,    0,    4,    5,    0,    0,
    0,    2,    0,    0,    0,   14,    0,    0,    0,    0,
    0,    0,    0,    0,   18,    0,   19,    0,   10,    0,
    0,   15,    0,    0,   63,   64,    0,    0,    0,    0,
   65,   66,    0,    0,    0,   60,   67,   68,   61,    0,
    0,    7,   17,    6,    0,    0,   58,    0,    0,   20,
    0,   59,   57,   56,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   11,    0,    0,    0,   33,   34,    0,
    0,    0,    0,    0,    0,    0,    0,   29,   30,    0,
    0,    0,    0,    0,    0,    0,   70,    0,   75,   72,
   55,   21,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   31,    0,    0,    0,    0,    0,    0,   13,    0,
    0,    0,    0,   25,   26,   27,   71,    0,    0,    0,
    0,    0,    0,    0,    0,   24,    0,    0,    0,   76,
    0,    0,    0,    0,    0,    0,    0,   43,    0,    0,
    0,   38,    0,    0,    0,   41,   40,    0,   44,   45,
    0,   42,
};
final static short yydgoto[] = {                          4,
    5,    6,    7,    8,   52,   85,   86,   87,   88,   89,
   45,   46,   47,   48,   21,   29,   16,   23,   92,   93,
   94,  152,   49,   59,   99,
};
final static short yysindex[] = {                      -166,
 -226, -222, -197,    0,    0, -166,    0,    0, -193, -201,
 -195,    0, -199, -251, -251,    0, -251, -207, -192, -208,
 -198, -202, -181, -188,    0, -203,    0, -251,    0, -161,
 -251,    0, -161, -204,    0,    0, -133, -153, -133, -152,
    0,    0, -133, -133, -266,    0,    0,    0,    0, -208,
  177,    0,    0,    0, -133, -151,    0,  167, -167,    0,
   45,    0,    0,    0, -149, -133, -133, -133, -133, -133,
 -133, -133, -133,    0, -168, -144, -133,    0,    0, -154,
 -139, -135, -161, -134, -123, -165, -155,    0,    0, -142,
 -140,  177,  177,  177,    1, -131,    0, -133,    0,    0,
    0,    0, -156, -230, -143, -143, -276, -276, -281, -156,
 -133,    0,  187, -133, -251, -133, -125, -133,    0,  177,
 -133, -133, -133,    0,    0,    0,    0,  167,  187,   54,
 -155, -127,   68, -113,   77,    0,  187,  187,  187,    0,
 -161, -133, -161, -133, -161, -111,  178,    0,   86, -108,
 -205,    0, -133, -118, -161,    0,    0,  100,    0,    0,
 -161,    0,
};
final static short yyrindex[] = {                       171,
    0,    0,    0,    0,    0,  171,    0,    0,    0,    0,
    0,    0,    0,  -93,  -82,    0,  -93,    0,    0,  -91,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -82,    0,    0,  -98,    0,    0,  -80,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -88,
  -77,    0,    0,    0,    0,    0,    0,  -74,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -95,    0,    0,    0,
    0,  -77,  -77,  -77,    0, -121,    0,    0,    0,    0,
    0,    0,  -75, -163,   39,   41,  -16,  -11,  -34,  -57,
    0,    0,  -92,    0,    0,    0,    0,    0,    0,  -77,
    0,    0,    0,    0,    0,    0,    0,  -60,  -81,    0,
    0,    0,    0,    0,    0,    0,  -73,  -72,  -70,    0,
    0,    0,    0,    0,    0,  145,    0,    0,    0,  161,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  200,    0,    0,    0,  -33,  -71,    0,  -12,   78,    0,
  -29,    0,  -47,  -40,  192,  162,    0,  184,   71,    0,
    0,    0,    0,    0,   99,
};
final static int YYTABLESIZE=488;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         54,
   65,   20,   22,   90,   20,   18,   19,   58,   97,   61,
   91,   66,   67,   63,   64,   50,   66,   67,   22,   73,
  124,  125,  126,   72,   73,   95,   66,   67,   68,   69,
    9,   70,   71,   72,   73,   10,  103,  104,  105,  106,
  107,  108,  109,  110,   90,   90,   90,  113,  136,  117,
   25,   91,   91,   91,   34,   35,   36,   51,   26,   37,
   11,   55,   66,   38,   13,   27,   80,   15,  128,   39,
   73,   14,   90,   17,   28,   30,   40,   41,   42,   91,
   56,  129,   32,   31,  130,   33,  133,   57,  135,   27,
   43,  137,  138,  139,    1,    2,  100,   55,    3,   44,
   47,   51,  131,   47,   60,   62,   96,  146,  102,  148,
   47,  150,  147,  112,  149,  111,   56,  156,  114,   47,
  120,  160,   47,  158,   34,   35,   36,  162,  121,   37,
   47,   47,   47,  115,   47,   47,   47,  116,  118,   39,
  119,  122,   69,  123,   73,   69,   40,   41,   42,   66,
   67,  134,   69,   56,   70,   71,   72,   73,  142,  144,
   43,   69,   69,  151,   69,   62,  155,  159,   62,   44,
    3,   69,   69,   69,   69,   62,   69,   69,   69,   69,
    8,   16,    9,   73,   62,   12,   23,   62,   48,   74,
   28,   48,  132,   32,   62,   62,   62,   62,   48,   62,
   62,   62,   62,   77,   36,   12,   49,   48,   24,   49,
   48,   74,   22,   35,   53,   37,   49,   48,   48,   48,
   48,  157,   48,   48,   48,   49,  140,    0,   49,   54,
    0,    0,   54,    0,    0,   49,   49,   49,   49,   54,
   49,   49,   49,    0,    0,    0,    0,   52,   54,    0,
   52,   54,   53,    0,    0,   53,    0,   52,    0,    0,
   54,   54,   53,   54,   54,   54,   52,  127,    0,   52,
    0,   53,    0,    0,   53,    0,    0,    0,   52,   52,
    0,   52,   52,   53,   53,    0,   53,   53,    0,    0,
    0,    0,    0,   66,   67,   68,   69,    0,   70,   71,
   72,   73,   51,    0,   50,   51,    0,   50,    0,    0,
    0,    0,   51,    0,   50,    0,    0,    0,  101,    0,
    0,   51,    0,   50,   51,    0,   50,  141,    0,    0,
    0,    0,    0,   51,   51,   50,   50,   66,   67,   68,
   69,  143,   70,   71,   72,   73,   66,   67,   68,   69,
  145,   70,   71,   72,   73,    0,    0,    0,    0,  154,
   66,   67,   68,   69,    0,   70,   71,   72,   73,   66,
   67,   68,   69,  161,   70,   71,   72,   73,   66,   67,
   68,   69,    0,   70,   71,   72,   73,    0,    0,    0,
    0,    0,   66,   67,   68,   69,    0,   70,   71,   72,
   73,   39,   39,    0,    0,    0,    0,    0,   39,    0,
    0,    0,   39,   39,   39,   39,   39,   46,   46,    0,
   39,   39,   39,   39,   46,    0,    0,    0,   46,   46,
   46,   46,   46,   18,   75,    0,   46,   46,   46,   46,
    0,    0,    0,    0,   76,   77,   78,   79,   80,   98,
    0,    0,   81,   82,   83,   84,    0,    0,    0,   66,
   67,   68,   69,  153,   70,   71,   72,   73,    0,    0,
   66,   67,   68,   69,    0,   70,   71,   72,   73,   66,
   67,   68,   69,    0,   70,   71,   72,   73,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
  267,   14,   15,   51,   17,  257,  258,   37,   56,   39,
   51,  293,  294,   43,   44,   28,  293,  294,   31,  301,
   92,   93,   94,  300,  301,   55,  293,  294,  295,  296,
  257,  298,  299,  300,  301,  258,   66,   67,   68,   69,
   70,   71,   72,   73,   92,   93,   94,   77,  120,   83,
  258,   92,   93,   94,  258,  259,  260,  263,  266,  263,
  258,  266,  293,  267,  258,  258,  272,  263,   98,  273,
  301,  273,  120,  273,  283,  274,  280,  281,  282,  120,
  285,  111,  264,  286,  114,  274,  116,  292,  118,  258,
  294,  121,  122,  123,  261,  262,  264,  266,  265,  303,
  264,  263,  115,  267,  258,  258,  258,  141,  258,  143,
  274,  145,  142,  258,  144,  284,  285,  151,  273,  283,
  286,  155,  286,  153,  258,  259,  260,  161,  284,  263,
  294,  295,  296,  273,  298,  299,  300,  273,  273,  273,
  264,  284,  264,  284,  301,  267,  280,  281,  282,  293,
  294,  277,  274,  285,  298,  299,  300,  301,  286,  273,
  294,  283,  284,  275,  286,  264,  275,  286,  267,  303,
    0,  293,  294,  295,  296,  274,  298,  299,  300,  301,
  274,  264,  274,  264,  283,  274,  264,  286,  264,  264,
  286,  267,  115,  286,  293,  294,  295,  296,  274,  298,
  299,  300,  301,  264,  286,    6,  264,  283,   17,  267,
  286,   50,  286,  286,   31,  286,  274,  293,  294,  295,
  296,  151,  298,  299,  300,  283,  128,   -1,  286,  264,
   -1,   -1,  267,   -1,   -1,  293,  294,  295,  296,  274,
  298,  299,  300,   -1,   -1,   -1,   -1,  264,  283,   -1,
  267,  286,  264,   -1,   -1,  267,   -1,  274,   -1,   -1,
  295,  296,  274,  298,  299,  300,  283,  267,   -1,  286,
   -1,  283,   -1,   -1,  286,   -1,   -1,   -1,  295,  296,
   -1,  298,  299,  295,  296,   -1,  298,  299,   -1,   -1,
   -1,   -1,   -1,  293,  294,  295,  296,   -1,  298,  299,
  300,  301,  264,   -1,  264,  267,   -1,  267,   -1,   -1,
   -1,   -1,  274,   -1,  274,   -1,   -1,   -1,  274,   -1,
   -1,  283,   -1,  283,  286,   -1,  286,  274,   -1,   -1,
   -1,   -1,   -1,  295,  296,  295,  296,  293,  294,  295,
  296,  274,  298,  299,  300,  301,  293,  294,  295,  296,
  274,  298,  299,  300,  301,   -1,   -1,   -1,   -1,  274,
  293,  294,  295,  296,   -1,  298,  299,  300,  301,  293,
  294,  295,  296,  274,  298,  299,  300,  301,  293,  294,
  295,  296,   -1,  298,  299,  300,  301,   -1,   -1,   -1,
   -1,   -1,  293,  294,  295,  296,   -1,  298,  299,  300,
  301,  257,  258,   -1,   -1,   -1,   -1,   -1,  264,   -1,
   -1,   -1,  268,  269,  270,  271,  272,  257,  258,   -1,
  276,  277,  278,  279,  264,   -1,   -1,   -1,  268,  269,
  270,  271,  272,  257,  258,   -1,  276,  277,  278,  279,
   -1,   -1,   -1,   -1,  268,  269,  270,  271,  272,  283,
   -1,   -1,  276,  277,  278,  279,   -1,   -1,   -1,  293,
  294,  295,  296,  286,  298,  299,  300,  301,   -1,   -1,
  293,  294,  295,  296,   -1,  298,  299,  300,  301,  293,
  294,  295,  296,   -1,  298,  299,  300,  301,
};
}
final static short YYFINAL=4;
final static short YYMAXTOKEN=303;
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
null,null,null,"TYPE","IDENTIFIER","INTEGER","FLOAT","DEFUN","PROC",
"L_CURBRACE","R_CURBRACE","STRUCT","L_SQBRACE","R_SQBRACE","GOTO","RETURN",
"BREAK","CONTINUE","IF","L_BRACE","R_BRACE","ELSE","FOR","WHILE","DO","SWITCH",
"NEW","STRING","BOOLEAN","COMMA","ASSIGN","DOT","SEMICOLON","CASE","TUPLE",
"INCLUDE","UN_MINUS","UN_PLUS","INCR","MUL_ARITHM","ADD_ARITHM","MORELESS","EQ",
"IFX","AND","OR","XOR","POW","UN_ARITHM","NOT",
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
"param_list : decl",
"param_list : decl param_tail",
"param_tail : COMMA decl param_tail",
"param_tail : COMMA decl",
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
"exp : NOT exp",
"exp : ADD_ARITHM exp",
"exp : IDENTIFIER INCR",
"exp : NEW IDENTIFIER",
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

//#line 230 "./borzhch.y"

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
//#line 468 "Parser.java"
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
//#line 42 "./borzhch.y"
{ TreeAST.setRoot((NodeAST) val_peek(0).obj); }
break;
case 2:
//#line 47 "./borzhch.y"
{ 
        StatementList list = new StatementList(); 
        list.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) list.addAll((NodeList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 3:
//#line 53 "./borzhch.y"
{ yyval.obj = null; }
break;
case 4:
//#line 56 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 5:
//#line 57 "./borzhch.y"
{ yyval.obj = null; }
break;
case 6:
//#line 61 "./borzhch.y"
{
        FunctionNode func;
        if (BOHelper.isType(val_peek(5).sval)) {
            func = new FunctionNode(val_peek(4).sval, BOHelper.getType(val_peek(5).sval));
        } else {
            /* TODO: Find in sometable*/
            func = new FunctionNode(val_peek(4).sval, val_peek(5).sval);
        }
        func.setStatements((StatementList) val_peek(0).obj);
        yyval.obj = func;
    }
break;
case 13:
//#line 85 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 18:
//#line 98 "./borzhch.y"
{ 
        DeclarationNode decl = new DeclarationNode(val_peek(0).sval, BOHelper.getType(val_peek(1).sval));
        yyval.obj = decl;  
    }
break;
case 19:
//#line 102 "./borzhch.y"
{
        DeclarationNode decl = new DeclarationNode(val_peek(0).sval, val_peek(1).sval);
        yyval.obj = decl;
    }
break;
case 20:
//#line 106 "./borzhch.y"
{ yyval.obj = null; }
break;
case 21:
//#line 107 "./borzhch.y"
{ yyval.obj = null; }
break;
case 22:
//#line 110 "./borzhch.y"
{
        DeclarationNode decl = (DeclarationNode) val_peek(2).obj;
        AssignNode an = new AssignNode(decl.getName(), (NodeAST) val_peek(0).obj);

        StatementList list = new StatementList();
        list.add(decl);
        list.add(an);
        yyval.obj = list;
    }
break;
case 23:
//#line 122 "./borzhch.y"
{ yyval.obj = null; }
break;
case 24:
//#line 123 "./borzhch.y"
{ 
        StatementList list = new StatementList();
        list.add((NodeAST) val_peek(2).obj);
        if (val_peek(0).obj != null) list.addAll((StatementList) val_peek(0).obj);
    }
break;
case 25:
//#line 128 "./borzhch.y"
{ yyval.obj = null; }
break;
case 26:
//#line 129 "./borzhch.y"
{ yyval.obj = null; }
break;
case 27:
//#line 130 "./borzhch.y"
{ yyval.obj = null; }
break;
case 28:
//#line 133 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 29:
//#line 134 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 30:
//#line 135 "./borzhch.y"
{ yyval.obj = null; }
break;
case 31:
//#line 136 "./borzhch.y"
{ yyval.obj = null; }
break;
case 32:
//#line 137 "./borzhch.y"
{ yyval.obj = null; }
break;
case 33:
//#line 138 "./borzhch.y"
{ yyval.obj = null; }
break;
case 34:
//#line 139 "./borzhch.y"
{ yyval.obj = null; }
break;
case 47:
//#line 165 "./borzhch.y"
{ yyval.obj = new ArOpNode((NodeAST) val_peek(2).obj, (NodeAST) val_peek(0).obj, val_peek(1).sval); }
break;
case 48:
//#line 166 "./borzhch.y"
{ yyval.obj = new ArOpNode((NodeAST) val_peek(2).obj, (NodeAST) val_peek(0).obj, val_peek(1).sval); }
break;
case 49:
//#line 167 "./borzhch.y"
{ yyval.obj = new ArOpNode((NodeAST) val_peek(2).obj, (NodeAST) val_peek(0).obj, "**"); }
break;
case 50:
//#line 168 "./borzhch.y"
{ yyval.obj = new CmpOpNode((NodeAST) val_peek(2).obj, (NodeAST) val_peek(0).obj, val_peek(1).sval); }
break;
case 51:
//#line 169 "./borzhch.y"
{ yyval.obj = new CmpOpNode((NodeAST) val_peek(2).obj, (NodeAST) val_peek(0).obj, val_peek(1).sval); }
break;
case 52:
//#line 170 "./borzhch.y"
{ yyval.obj = new LogOpNode((NodeAST) val_peek(2).obj, (NodeAST) val_peek(0).obj, "a"); }
break;
case 53:
//#line 171 "./borzhch.y"
{ yyval.obj = new LogOpNode((NodeAST) val_peek(2).obj, (NodeAST) val_peek(0).obj, "o"); }
break;
case 54:
//#line 172 "./borzhch.y"
{ yyval.obj = new LogOpNode((NodeAST) val_peek(2).obj, (NodeAST) val_peek(0).obj, "x"); }
break;
case 55:
//#line 173 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 56:
//#line 174 "./borzhch.y"
{ yyval.obj = new UnOpNode((NodeAST) val_peek(0).obj, "n"); }
break;
case 57:
//#line 176 "./borzhch.y"
{ yyval.obj = new UnOpNode((NodeAST) val_peek(0).obj, val_peek(1).sval); }
break;
case 58:
//#line 181 "./borzhch.y"
{ yyval.obj = new PostOpNode(new VariableNode(val_peek(1).sval), val_peek(0).sval); }
break;
case 59:
//#line 182 "./borzhch.y"
{ yyval.obj = new NewObjectNode(val_peek(0).sval); }
break;
case 60:
//#line 183 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 61:
//#line 184 "./borzhch.y"
{ yyval.obj = null; }
break;
case 62:
//#line 185 "./borzhch.y"
{ yyval.obj = new VariableNode(val_peek(0).sval); }
break;
case 63:
//#line 186 "./borzhch.y"
{ yyval.obj = new IntegerNode(val_peek(0).ival); }
break;
case 64:
//#line 187 "./borzhch.y"
{ yyval.obj = new FloatNode((float)val_peek(0).dval); }
break;
case 65:
//#line 188 "./borzhch.y"
{ yyval.obj = new StringNode(val_peek(0).sval); }
break;
case 66:
//#line 189 "./borzhch.y"
{ yyval.obj = new BooleanNode(val_peek(0).ival); }
break;
case 67:
//#line 193 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 68:
//#line 194 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 69:
//#line 198 "./borzhch.y"
{
        DotOpNode dot = new DotOpNode((VariableNode) new VariableNode(val_peek(2).sval), (NodeAST) new VariableNode(val_peek(0).sval));
        yyval.obj = dot;
    }
break;
case 70:
//#line 202 "./borzhch.y"
{
        DotOpNode dot = new DotOpNode((VariableNode) new VariableNode(val_peek(2).sval), (NodeAST) val_peek(0).obj);
        yyval.obj = dot;
    }
break;
case 71:
//#line 209 "./borzhch.y"
{
        yyval.obj = new ArrayElementNode(
            new VariableNode(val_peek(3).sval),
            (NodeAST) val_peek(1).obj
        );
    }
break;
//#line 857 "Parser.java"
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
