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
  import edu.borzhch.FuncTable;
  import edu.borzhch.constants.*;
//#line 25 "Parser.java"




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
public final static short PRINT=287;
public final static short COLON=288;
public final static short CASE=289;
public final static short TUPLE=290;
public final static short INCLUDE=291;
public final static short UN_MINUS=292;
public final static short UN_PLUS=293;
public final static short INCR=294;
public final static short MUL_ARITHM=295;
public final static short ADD_ARITHM=296;
public final static short MORELESS=297;
public final static short EQ=298;
public final static short IFX=299;
public final static short OR=300;
public final static short AND=301;
public final static short XOR=302;
public final static short POW=303;
public final static short UN_ARITHM=304;
public final static short NOT=305;
public final static short structref=306;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,   30,    1,    1,    2,    2,   31,   32,    7,    7,
   22,   22,   22,    5,    5,   13,    8,   23,    9,    9,
   16,   16,   16,   17,   17,   14,   14,   14,   14,   14,
   29,   15,   15,   15,   15,   15,   15,   15,   15,   15,
   18,   18,   18,   24,   25,   25,   10,   11,   11,   11,
    6,    6,    6,   26,   26,   28,   28,   27,   19,   19,
   19,   19,   19,   19,   19,   19,   19,   19,   19,   19,
   19,   19,   19,   19,   19,   19,   19,   19,   19,   19,
   20,   21,   12,    3,    3,    3,    4,    4,
};
final static short yylen[] = {                            2,
    2,    0,    0,    2,    1,    1,    1,    1,    7,    6,
    0,    1,    2,    3,    2,    3,    3,    3,    0,    3,
    2,    2,    4,    7,    3,    0,    3,    2,    2,    2,
    2,    1,    1,    1,    2,    2,    1,    1,    1,    1,
    3,    3,    3,    3,    1,    3,    6,    0,    2,    2,
    9,    5,    7,   11,    7,    0,    2,    4,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    2,    4,    2,
    2,    4,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    4,    3,    0,    1,    2,    3,    2,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    1,    0,    5,    6,    0,
    0,    0,    4,    0,    0,    0,   17,    0,    0,    0,
    0,    0,    0,    0,    0,   21,    0,   22,    0,   13,
    0,   18,    0,    0,    0,    0,    7,   10,    0,   20,
    9,   23,   14,    0,    0,    0,   38,   39,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   33,
   34,    0,    0,    0,   40,    0,    0,    0,   35,    0,
   76,   77,    0,    0,    0,   78,   79,    0,    0,   74,
    0,   73,   81,   75,    0,    0,    0,    0,    0,    0,
   29,   28,    8,   16,    0,    0,    0,    0,   30,    0,
    0,    0,   44,    0,   70,    0,    0,    0,   71,    0,
   68,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   27,    0,    0,    0,    0,
   82,    0,    0,   83,    0,   86,   67,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   46,   72,    0,   69,    0,    0,   52,    0,
    0,    0,   87,    0,   47,    0,    0,    0,    0,    0,
    0,   49,   50,    0,    0,   53,    0,   57,    0,   24,
    0,    0,    0,   51,   58,    0,    0,   54,
};
final static short yydgoto[] = {                          1,
    6,    7,  106,  136,   30,   55,    8,    9,   23,   56,
  165,   80,   38,   57,   58,   59,   60,   61,  107,   82,
   83,   22,   17,   84,  103,   64,  169,  170,   65,    2,
   39,   94,
};
final static short yysindex[] = {                         0,
    0,  -73, -256, -248, -228,    0,  -73,    0,    0, -198,
 -211, -189,    0, -208, -212, -212,    0, -212, -147, -174,
 -185, -164, -150, -169, -144,    0, -135,    0, -212,    0,
 -129,    0, -212, -129, -118, -185,    0,    0,  153,    0,
    0,    0,    0, -251, -117, -121,    0,    0, -127, -125,
 -122, -129, -120, -121,  153,  153, -107, -142, -111,    0,
    0, -110, -105,  153,    0, -121, -121, -100,    0, -230,
    0,    0, -121, -121,  -92,    0,    0, -121, -121,    0,
  228,    0,    0,    0, -121, -212, -121,  -96, -121,  228,
    0,    0,    0,    0,  153,  -95, -121, -121,    0, -220,
  228, -103,    0, -121,    0,  -97,  195,  141,    0,  217,
    0, -121, -121, -121, -121, -121, -121, -121, -121,  152,
 -111,  -93,  161,  -82,  171,    0, -132,  228,  228,  228,
    0, -100,  -80,    0, -121,    0,    0, -121, -108, -180,
 -175, -141,   40, -126, -190, -108, -129, -121, -129, -121,
  -67,  -69,    0,    0,  195,    0,  -77,  208,    0,  186,
  -86, -121,    0, -249,    0,  -54,  -79,  -53,  -86,  -55,
 -201,    0,    0, -181,  -52,    0,  -68,    0,  -50,    0,
 -129,  153,  -40,    0,    0,  153,  -38,    0,
};
final static short yyrindex[] = {                         0,
    0,  229,    0,    0,    0,    0,  229,    0,    0,    0,
    0,    0,    0,    0,  -46,  -33,    0,  -46,    0,    0,
  -42,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -33,    0,    0,  -29,    0,    0,  -30,    0,
    0,    0,    0,    0,    0,  -35,    0,    0,    0,    0,
    0,    0,    0,    0, -258, -258,    0,    0,  -32,    0,
    0,    0,    0, -258,    0,    0,    0,    0,    0,  -59,
    0,    0,  -17,    0,    0,    0,    0,    0,    0,    0,
  -28,    0,    0,    0,    0,    0,    0,    0,    0,  -27,
    0,    0,    0,    0, -258,    0,    0,    0,    0,    0,
 -262,  -84,    0,  -19,    0,    0, -235,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -21, -237, -236,
    0,    0,    0,    0,    0,    0,    0,    0,  -34,    6,
   26,   46,   86, -232,   66,  -14,    0,    0,    0,    0,
    0,    0,    0,    0, -207,    0,  106,    0,    0,    0,
  -16,    0,    0,    0,    0,    0,    0,    0,  -16,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  130,    0,
    0, -258,    0,    0,    0,  -30,    0,    0,
};
final static short yygindex[] = {                         0,
  249,    0,  131,  102,  238,    0,    0,    0,  242,  112,
    0,    0,  -31,  -51,    0,   -7,  191,  113,  -26,    0,
  -39,  260,    0,  -37,  162,    0,    0,  116,    0,    0,
    0,    0,
};
final static int YYTABLESIZE=531;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         62,
   10,   63,   41,   91,   92,   26,   28,   21,   24,   11,
   21,   42,   99,   37,   66,   62,   62,   63,   63,   81,
   88,   36,   49,   42,   62,   24,   63,   90,   85,   12,
   26,   64,   67,   68,   64,   66,   43,   41,   85,  100,
  101,   64,  104,  126,   19,   20,  131,  108,   43,   41,
   64,  110,  111,   64,   68,   62,   88,   63,  120,   14,
  123,   15,  125,  105,   18,  180,   88,   64,   64,  128,
  129,  130,   64,   16,  112,  113,  114,  115,  121,  116,
  117,  118,  119,   28,   66,  139,  140,  141,  142,  143,
  144,  145,  146,  112,  113,  114,  115,   29,  116,  117,
  118,  119,   67,   68,  112,  113,  114,  115,  155,   31,
   26,  156,  119,   32,  112,  157,   33,  159,   27,  112,
  113,  158,  119,  160,  152,  109,   62,  119,   63,   34,
  185,   35,  173,   37,  187,  171,   70,   71,   72,   42,
   69,   73,   62,   95,   63,   85,   62,   86,   63,  184,
   87,   74,   89,  112,  113,  114,   93,  102,   75,   76,
   77,  119,   70,   71,   72,  109,  134,   73,  112,  113,
  114,  115,   96,   97,   78,  118,  119,   74,   98,   45,
  124,  132,   45,   79,  127,   76,   77,    3,    4,   45,
  150,    5,  148,  154,  119,  161,  162,  164,   45,   45,
   78,   45,  168,  174,   80,  177,  176,   80,  179,   79,
   45,   45,   45,   45,   80,   45,   45,   45,   45,  182,
   45,  181,  186,   80,  183,  188,   80,   11,    3,   60,
   19,   12,   60,   26,  133,   80,   80,   80,   80,   60,
   80,   80,   80,   80,   15,   80,   84,   56,   60,   61,
   37,   60,   61,   32,   84,   13,  163,   36,   31,   61,
   60,   60,   60,   60,   25,   60,   60,   60,   61,   59,
   60,   61,   59,   43,   40,  172,  122,   25,  175,   59,
   61,   61,   61,   61,  178,   61,   61,   61,   59,   63,
   61,   59,   63,  153,    0,    0,    0,    0,    0,   63,
    0,   59,   59,   59,    0,   59,   59,   59,   63,   62,
   59,   63,   62,    0,    0,    0,    0,    0,    0,   62,
    0,    0,   63,   63,    0,   63,   63,   63,   62,   66,
   63,   62,   66,    0,  112,  113,  114,  115,    0,   66,
  117,  118,  119,   62,    0,   62,   62,   62,   66,   65,
   62,   66,   65,    0,    0,    0,    0,    0,    0,   65,
    0,    0,   48,   48,    0,   66,   66,   66,   65,   48,
   66,   65,    0,   48,   48,   48,   48,   48,    0,    0,
    0,   48,   48,   48,   48,   65,   55,   55,    0,    0,
   65,    0,   48,   55,   48,    0,    0,   55,   55,   55,
   55,   55,    0,    0,    0,   55,   55,   55,   55,   19,
   44,    0,    0,    0,  137,    0,   55,    0,   55,    0,
   45,   46,   47,   48,   49,  147,    0,    0,   50,   51,
   52,   53,    0,    0,  149,  112,  113,  114,  115,   54,
  116,  117,  118,  119,  151,    0,  112,  113,  114,  115,
    0,  116,  117,  118,  119,  112,  113,  114,  115,  167,
  116,  117,  118,  119,    0,  112,  113,  114,  115,    0,
  116,  117,  118,  119,    0,    0,    0,  135,    0,    0,
  112,  113,  114,  115,    0,  116,  117,  118,  119,  112,
  113,  114,  115,  166,  116,  117,  118,  119,    0,    0,
    0,    0,  112,  113,  114,  115,    0,  116,  117,  118,
  119,  112,  113,  114,  115,    0,  116,  117,  118,  119,
    0,  138,  112,  113,  114,  115,    0,  116,  117,  118,
  119,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         39,
  257,   39,   34,   55,   56,  264,  258,   15,   16,  258,
   18,  274,   64,  263,  266,   55,   56,   55,   56,   46,
   52,   29,  272,  286,   64,   33,   64,   54,  264,  258,
  289,  264,  284,  285,  267,  266,  274,  274,  274,   66,
   67,  274,  273,   95,  257,  258,  267,   74,  286,  286,
  283,   78,   79,  286,  285,   95,  264,   95,   85,  258,
   87,  273,   89,  294,  273,  267,  274,  300,  301,   96,
   97,   98,  305,  263,  295,  296,  297,  298,   86,  300,
  301,  302,  303,  258,  266,  112,  113,  114,  115,  116,
  117,  118,  119,  295,  296,  297,  298,  283,  300,  301,
  302,  303,  284,  285,  295,  296,  297,  298,  135,  274,
  258,  138,  303,  264,  295,  147,  286,  149,  266,  295,
  296,  148,  303,  150,  257,  258,  166,  303,  166,  274,
  182,  267,  164,  263,  186,  162,  258,  259,  260,  258,
  258,  263,  182,  286,  182,  273,  186,  273,  186,  181,
  273,  273,  273,  295,  296,  297,  264,  258,  280,  281,
  282,  303,  258,  259,  260,  258,  264,  263,  295,  296,
  297,  298,  284,  284,  296,  302,  303,  273,  284,  264,
  277,  285,  267,  305,  280,  281,  282,  261,  262,  274,
  273,  265,  286,  274,  303,  263,  266,  275,  283,  284,
  296,  286,  289,  258,  264,  259,  286,  267,  264,  305,
  295,  296,  297,  298,  274,  300,  301,  302,  303,  288,
  305,  274,  263,  283,  275,  264,  286,  274,    0,  264,
  264,  274,  267,  264,  104,  295,  296,  297,  298,  274,
  300,  301,  302,  303,  274,  305,  264,  264,  283,  264,
  286,  286,  267,  286,  274,    7,  155,  286,  286,  274,
  295,  296,  297,  298,  286,  300,  301,  302,  283,  264,
  305,  286,  267,   36,   33,  164,   86,   18,  166,  274,
  295,  296,  297,  298,  169,  300,  301,  302,  283,  264,
  305,  286,  267,  132,   -1,   -1,   -1,   -1,   -1,  274,
   -1,  296,  297,  298,   -1,  300,  301,  302,  283,  264,
  305,  286,  267,   -1,   -1,   -1,   -1,   -1,   -1,  274,
   -1,   -1,  297,  298,   -1,  300,  301,  302,  283,  264,
  305,  286,  267,   -1,  295,  296,  297,  298,   -1,  274,
  301,  302,  303,  298,   -1,  300,  301,  302,  283,  264,
  305,  286,  267,   -1,   -1,   -1,   -1,   -1,   -1,  274,
   -1,   -1,  257,  258,   -1,  300,  301,  302,  283,  264,
  305,  286,   -1,  268,  269,  270,  271,  272,   -1,   -1,
   -1,  276,  277,  278,  279,  300,  257,  258,   -1,   -1,
  305,   -1,  287,  264,  289,   -1,   -1,  268,  269,  270,
  271,  272,   -1,   -1,   -1,  276,  277,  278,  279,  257,
  258,   -1,   -1,   -1,  274,   -1,  287,   -1,  289,   -1,
  268,  269,  270,  271,  272,  274,   -1,   -1,  276,  277,
  278,  279,   -1,   -1,  274,  295,  296,  297,  298,  287,
  300,  301,  302,  303,  274,   -1,  295,  296,  297,  298,
   -1,  300,  301,  302,  303,  295,  296,  297,  298,  274,
  300,  301,  302,  303,   -1,  295,  296,  297,  298,   -1,
  300,  301,  302,  303,   -1,   -1,   -1,  283,   -1,   -1,
  295,  296,  297,  298,   -1,  300,  301,  302,  303,  295,
  296,  297,  298,  286,  300,  301,  302,  303,   -1,   -1,
   -1,   -1,  295,  296,  297,  298,   -1,  300,  301,  302,
  303,  295,  296,  297,  298,   -1,  300,  301,  302,  303,
   -1,  305,  295,  296,  297,  298,   -1,  300,  301,  302,
  303,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=306;
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
"NEW","STRING","BOOLEAN","COMMA","ASSIGN","DOT","SEMICOLON","PRINT","COLON",
"CASE","TUPLE","INCLUDE","UN_MINUS","UN_PLUS","INCR","MUL_ARITHM","ADD_ARITHM",
"MORELESS","EQ","IFX","OR","AND","XOR","POW","UN_ARITHM","NOT","structref",
};
final static String yyrule[] = {
"$accept : start",
"start : init global_list",
"init :",
"global_list :",
"global_list : global global_list",
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
"decl_assign : decl ASSIGN NEW TYPE L_SQBRACE exp R_SQBRACE",
"decl_assign : decl ASSIGN exp",
"stmt_list :",
"stmt_list : stmt SEMICOLON stmt_list",
"stmt_list : if stmt_list",
"stmt_list : loop stmt_list",
"stmt_list : switch stmt_list",
"builtin : PRINT exp",
"stmt : decl",
"stmt : decl_assign",
"stmt : assign",
"stmt : GOTO IDENTIFIER",
"stmt : RETURN exp",
"stmt : RETURN",
"stmt : BREAK",
"stmt : CONTINUE",
"stmt : builtin",
"assign : idref ASSIGN exp",
"assign : IDENTIFIER ASSIGN exp",
"assign : arrayref ASSIGN exp",
"idref : IDENTIFIER DOT idref_tail",
"idref_tail : IDENTIFIER",
"idref_tail : IDENTIFIER DOT idref_tail",
"if : IF L_BRACE exp R_BRACE codeblock else",
"else :",
"else : ELSE if",
"else : ELSE codeblock",
"loop : FOR L_BRACE decl_assign SEMICOLON exp SEMICOLON assign R_BRACE codeblock",
"loop : WHILE L_BRACE exp R_BRACE codeblock",
"loop : DO codeblock WHILE L_BRACE exp R_BRACE SEMICOLON",
"switch : SWITCH L_BRACE exp R_BRACE L_CURBRACE switchblock R_CURBRACE ELSE L_CURBRACE stmt_list R_CURBRACE",
"switch : SWITCH L_BRACE exp R_BRACE L_CURBRACE switchblock R_CURBRACE",
"switchblock :",
"switchblock : case switchblock",
"case : CASE INTEGER COLON stmt_list",
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
"exp : ADD_ARITHM exp NOT exp",
"exp : IDENTIFIER INCR",
"exp : NEW IDENTIFIER",
"exp : IDENTIFIER L_BRACE exp_list R_BRACE",
"exp : reference",
"exp : tuple_value",
"exp : idref",
"exp : INTEGER",
"exp : FLOAT",
"exp : STRING",
"exp : BOOLEAN",
"exp : IDENTIFIER",
"reference : arrayref",
"arrayref : IDENTIFIER L_SQBRACE exp R_SQBRACE",
"tuple_value : L_CURBRACE exp_list R_CURBRACE",
"exp_list :",
"exp_list : exp",
"exp_list : exp exp_tail",
"exp_tail : COMMA exp exp_tail",
"exp_tail : COMMA exp",
};

//#line 682 "./borzhch.y"

private SymTable topTable = null;
private static FuncTable funcTable = null;
private SymTable structTable = null;

public static FuncTable getFuncTable() {
    return funcTable;
}

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
  if(!result) result = funcTable.find(identifier);
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
//#line 533 "Parser.java"
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
//#line 48 "./borzhch.y"
{ 
        TreeAST.setRoot((NodeAST) val_peek(0).obj); 
     }
break;
case 2:
//#line 53 "./borzhch.y"
{
        topTable = new SymTable(null);
        
        funcTable = new FuncTable();

        structTable = new SymTable(null);
    }
break;
case 3:
//#line 62 "./borzhch.y"
{ 
           yyval.obj = null; 
    }
break;
case 4:
//#line 65 "./borzhch.y"
{ 
        StatementList list = new StatementList(); 
        list.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) list.addAll((NodeList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 5:
//#line 73 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 6:
//#line 74 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 7:
//#line 77 "./borzhch.y"
{
            topTable = new SymTable(topTable);
         }
break;
case 8:
//#line 82 "./borzhch.y"
{
            SymTable oldTable = topTable;
            topTable = oldTable.getPrevious();
            oldTable.setPrevious(null);
            oldTable.clear();
        }
break;
case 9:
//#line 90 "./borzhch.y"
{
        FunctionNode func = null;

        if (isTypeExist(val_peek(5).sval)) {
            func = new FunctionNode(val_peek(4).sval, val_peek(5).sval);
        } else {
            String msg = String.format("unknown type <%s>\n", val_peek(5).sval);
            yyerror(msg);
        }
        if(isIdentifierExist(val_peek(4).sval)) {
          String msg = String.format("identifier <%s> is already defined\n", val_peek(4).sval);
          yyerror(msg);
        }

        func.setArguments((NodeList) val_peek(2).obj);
        func.setStatements((StatementList) val_peek(0).obj);

        funcTable.push(func);

        yyval.obj = func;
    }
break;
case 10:
//#line 111 "./borzhch.y"
{
        if(isIdentifierExist(val_peek(4).sval)) {
          String msg = String.format("identifier <%s> is already defined\n", val_peek(4).sval);
          yyerror(msg);
        }
        FunctionNode func = new FunctionNode(val_peek(4).sval, BOType.VOID);
        func.setArguments((StatementList) val_peek(2).obj);
        func.setStatements((StatementList) val_peek(0).obj);

        funcTable.push(func);
    
        yyval.obj = func;
    }
break;
case 11:
//#line 126 "./borzhch.y"
{ yyval.obj = null; }
break;
case 12:
//#line 127 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 13:
//#line 132 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
        }
break;
case 14:
//#line 140 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((NodeList) val_peek(0).obj);
            yyval.obj = node; 
          }
break;
case 15:
//#line 146 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
          }
break;
case 16:
//#line 154 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 17:
//#line 158 "./borzhch.y"
{
        if(isIdentifierExist(val_peek(1).sval)) {
            String msg = String.format("identifier <%s> is already defined\n", val_peek(1).sval);
            yyerror(msg);
        }
        structTable.pushSymbol(val_peek(1).sval, "ref");;

        StructDeclarationNode node = new StructDeclarationNode(val_peek(1).sval, (FieldList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 18:
//#line 171 "./borzhch.y"
{
        yyval.obj = val_peek(1).obj;
    }
break;
case 19:
//#line 176 "./borzhch.y"
{ yyval.obj = null; }
break;
case 20:
//#line 177 "./borzhch.y"
{
        FieldList node = new FieldList();
        DeclarationNode ldecl = (DeclarationNode) val_peek(2).obj;
        ldecl.isField(true);
        node.add(ldecl);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 21:
//#line 188 "./borzhch.y"
{ 
        if(isIdentifierExist(val_peek(0).sval)) {
          String msg = String.format("identifier <%s> is already defined\n", val_peek(0).sval);
          yyerror(msg);
        }
        if(!isTypeExist(val_peek(1).sval)) {
          String msg = String.format("unknown type <%s>\n", val_peek(1).sval);
          yyerror(msg);
        }

        topTable.pushSymbol(val_peek(0).sval, val_peek(1).sval);
        
        DeclarationNode node = new DeclarationNode(val_peek(0).sval, BOHelper.getType(val_peek(1).sval));
        node.type(BOHelper.getType(val_peek(1).sval));
        yyval.obj = node;  
    }
break;
case 22:
//#line 204 "./borzhch.y"
{
        if(!isTypeExist(val_peek(1).sval)) {
          String msg = String.format("unknown type <%s>\n", val_peek(1).sval);
          yyerror(msg);
        }
        if(isIdentifierExist(val_peek(0).sval)) {
          String msg = String.format("identifier <%s> is already defined\n", val_peek(0).sval);
          yyerror(msg);
        }

        topTable.pushSymbol(val_peek(0).sval, val_peek(1).sval);

        DeclarationNode decl = new DeclarationNode(val_peek(0).sval, val_peek(1).sval);
        yyval.obj = decl;
    }
break;
case 23:
//#line 219 "./borzhch.y"
{
        if(!isTypeExist(val_peek(3).sval)) {
          String msg = String.format("unknown type <%s>\n", val_peek(3).sval);
          yyerror(msg);
        }
        if(isIdentifierExist(val_peek(0).sval)) {
          String msg = String.format("identifier <%s> is already defined\n", val_peek(0).sval);
          yyerror(msg);
        }
        
        topTable.pushSymbol(val_peek(0).sval, "ref", val_peek(3).sval);
                
        DeclarationNode node = new DeclarationNode(val_peek(0).sval, val_peek(3).sval);
        yyval.obj = node;
    }
break;
case 24:
//#line 261 "./borzhch.y"
{
        DeclarationNode decl = (DeclarationNode) val_peek(6).obj;
        NewArrayNode nan = new NewArrayNode(val_peek(3).sval, (NodeAST) val_peek(1).obj);
        VariableNode var = new VariableNode(decl.getName(), "ref");
        var.strType(val_peek(3).sval);
        AssignNode store = new AssignNode(var, nan);
        StatementList node = new StatementList();
        node.add(decl);
        node.add(store);
        yyval.obj = node;
    }
break;
case 25:
//#line 272 "./borzhch.y"
{
        DeclarationNode decl = (DeclarationNode) val_peek(2).obj;
        String name = decl.getName();

        NodeAST exp = (NodeAST) val_peek(0).obj;

        BOType infer = InferenceTypeTable.inferType(
            decl.type(),
            exp.type()
        );
        if (BOType.VOID == infer) {
            yyerror(ErrorHelper.incompatibleTypes(decl.type(), 
            (((NodeAST) val_peek(0).obj).type())));
        }


        AssignNode an = new AssignNode(new VariableNode(name, topTable.getSymbolType(name)), 
                (NodeAST) val_peek(0).obj);

        StatementList list = new StatementList();
        list.add(decl);
        list.add(an);
        yyval.obj = list;
    }
break;
case 26:
//#line 298 "./borzhch.y"
{ yyval.obj = null; }
break;
case 27:
//#line 299 "./borzhch.y"
{ 
        StatementList list = new StatementList();
        list.add((NodeAST) val_peek(2).obj);
        if (val_peek(0).obj != null) list.addAll((StatementList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 28:
//#line 305 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 29:
//#line 311 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 30:
//#line 317 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 31:
//#line 326 "./borzhch.y"
{
        yyval.obj = new PrintNode((NodeAST) val_peek(0).obj);
    }
break;
case 32:
//#line 331 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 33:
//#line 332 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 34:
//#line 333 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 35:
//#line 334 "./borzhch.y"
{ 
      if(!isIdentifierExist(val_peek(0).sval)) {
        String msg = String.format("identifier <%s> is not declared\n", val_peek(0).sval);
        yyerror(msg);
      }
      yyval.obj = null; 
    }
break;
case 36:
//#line 341 "./borzhch.y"
{ yyval.obj = new ReturnNode((NodeAST) val_peek(0).obj); }
break;
case 37:
//#line 342 "./borzhch.y"
{ yyval.obj = new ReturnNode(null); }
break;
case 38:
//#line 343 "./borzhch.y"
{ yyval.obj = new BreakNode(); }
break;
case 39:
//#line 344 "./borzhch.y"
{ yyval.obj = new ContinueNode(); }
break;
case 40:
//#line 345 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 41:
//#line 349 "./borzhch.y"
{
        /*DotOpNode dot = (DotOpNode) $1;
        dot.reduce();*/
        GetFieldNode get = (GetFieldNode) val_peek(2).obj;

        NodeAST exp = (NodeAST) val_peek(0).obj;
        SetFieldNode node = new SetFieldNode(get, exp);
        /*SetFieldNode node = new SetFieldNode(dot, exp);*/
        yyval.obj = node;
    }
break;
case 42:
//#line 359 "./borzhch.y"
{
        if(!isIdentifierExist(val_peek(2).sval)) {
          String msg = String.format("identifier <%s> is not declared\n", val_peek(2).sval);
          System.err.println(msg);
        }
        
        BOType infer = InferenceTypeTable.inferType(
            BOHelper.getType(topTable.getSymbolType(val_peek(2).sval)),
            ((NodeAST) val_peek(0).obj).type()
        );
        if (BOType.VOID == infer) {
            yyerror(ErrorHelper.incompatibleTypes(BOHelper.getType(topTable.getSymbolType(val_peek(2).sval)), 
            (((NodeAST) val_peek(0).obj).type())));
        }

        AssignNode an = new AssignNode(new VariableNode(val_peek(2).sval, topTable.getSymbolType(val_peek(2).sval)), 
            (NodeAST) val_peek(0).obj);
        yyval.obj = an;
    }
break;
case 43:
//#line 378 "./borzhch.y"
{
        /*arrayref := IDENTIFIER L_SQBRACE exp R_SQBRACE => ArrayElementNode*/
        
        ArrayElementNode index = (ArrayElementNode) val_peek(2).obj;
        NodeAST value = (NodeAST) val_peek(0).obj;
        SetArrayNode node = new SetArrayNode(index, value);
        yyval.obj = node;
    }
break;
case 44:
//#line 389 "./borzhch.y"
{
        VariableNode var = new VariableNode(val_peek(2).sval, topTable.getSymbolType(val_peek(2).sval));
        DotOpNode dot = new DotOpNode(var, (NodeAST) val_peek(0).obj);
        /*((IDotNode) node).setStructName(var.strType());*/
        /*node.type(node.getLastNode().type());*/
        GetFieldNode node = new GetFieldNode(var, dot.reduce());
        yyval.obj = node;
    }
break;
case 45:
//#line 400 "./borzhch.y"
{
        FieldNode node = new FieldNode(val_peek(0).sval);
        yyval.obj = node;
    }
break;
case 46:
//#line 404 "./borzhch.y"
{
        FieldNode field = new FieldNode(val_peek(2).sval);
        DotOpNode node = new DotOpNode(field, (NodeAST) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 47:
//#line 412 "./borzhch.y"
{
        IfNode node = new IfNode((NodeAST) val_peek(3).obj, (StatementList) val_peek(1).obj, (IfNode) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 48:
//#line 418 "./borzhch.y"
{
      yyval.obj = null;
    }
break;
case 49:
//#line 421 "./borzhch.y"
{
      yyval.obj = (IfNode) val_peek(0).obj;
    }
break;
case 50:
//#line 424 "./borzhch.y"
{
      IfNode node = new IfNode(null, (StatementList) val_peek(0).obj, null);
      yyval.obj = node;
    }
break;
case 51:
//#line 430 "./borzhch.y"
{
        NodeAST decl = (NodeAST) val_peek(6).obj;
        NodeAST counter = (NodeAST) val_peek(4).obj;
        NodeAST step = (NodeAST) val_peek(2).obj;
        NodeAST statements = (NodeAST) val_peek(0).obj; 
        ForNode node = new ForNode(decl, counter, step, statements);
        yyval.obj = node;
    }
break;
case 52:
//#line 438 "./borzhch.y"
{
        WhileNode node = new WhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 53:
//#line 442 "./borzhch.y"
{
        DoWhileNode node = new DoWhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(5).obj);
        yyval.obj = node;
    }
break;
case 54:
//#line 448 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(8).obj, (StatementList) val_peek(5).obj, (StatementList) val_peek(1).obj);
        yyval.obj = node;
      }
break;
case 55:
//#line 453 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(4).obj, (StatementList) val_peek(1).obj, null);
        yyval.obj = node;
      }
break;
case 56:
//#line 460 "./borzhch.y"
{ yyval.obj = null; }
break;
case 57:
//#line 461 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((StatementList) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 58:
//#line 468 "./borzhch.y"
{
        CaseNode node = new CaseNode(val_peek(2).ival, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 59:
//#line 475 "./borzhch.y"
{ 
        NodeAST l = (NodeAST) val_peek(2).obj;
        NodeAST r = (NodeAST) val_peek(0).obj;
        BOType infer = InferenceTypeTable.inferType(l.type(), r.type());
        if (BOType.VOID == infer) {
            yyerror(ErrorHelper.incompatibleTypes(r.type(), l.type()));
        }
        ArOpNode node = new ArOpNode(l, r, val_peek(1).sval);
        node.type(infer);
        yyval.obj = node;
    }
break;
case 60:
//#line 486 "./borzhch.y"
{ 
        NodeAST l = (NodeAST) val_peek(2).obj;
        NodeAST r = (NodeAST) val_peek(0).obj;
        BOType infer = InferenceTypeTable.inferType(l.type(), r.type());
        if (BOType.VOID == infer) {
            yyerror(ErrorHelper.incompatibleTypes(r.type(), l.type()));
        }
        ArOpNode node = new ArOpNode(l, r, val_peek(1).sval);
        node.type(infer);
        yyval.obj = node;
    }
break;
case 61:
//#line 497 "./borzhch.y"
{ 
        NodeAST l = (NodeAST) val_peek(2).obj;
        NodeAST r = (NodeAST) val_peek(0).obj;
        if (!BOHelper.isNumber(l.type())) {
            yyerror(ErrorHelper.incompatibleTypes(l.type(), BOType.FLOAT));
        } else
        if (!BOHelper.isNumber(r.type())) {
            yyerror(ErrorHelper.incompatibleTypes(l.type(), BOType.FLOAT));
        }

        ArOpNode node = new ArOpNode(l, r, "**");
       node.type(BOType.FLOAT);
        yyval.obj = node;
    }
break;
case 62:
//#line 511 "./borzhch.y"
{          
        NodeAST l = (NodeAST) val_peek(2).obj;
        NodeAST r = (NodeAST) val_peek(0).obj;     
        BOType infer = InferenceTypeTable.inferType(l.type(), r.type());
        if (BOType.VOID == infer) {
            yyerror(ErrorHelper.incompatibleTypes(r.type(), l.type()));
        }
        CmpOpNode node = new CmpOpNode(l, r, val_peek(1).sval);
        node.type(BOType.BOOL);
        yyval.obj = node;
    }
break;
case 63:
//#line 522 "./borzhch.y"
{         
        NodeAST l = (NodeAST) val_peek(2).obj;
        NodeAST r = (NodeAST) val_peek(0).obj;     
        BOType infer = InferenceTypeTable.inferType(l.type(), r.type());
        if (BOType.VOID == infer) {
            yyerror(ErrorHelper.incompatibleTypes(r.type(), l.type()));
        } else
        if (!BOHelper.isNumber(infer)) {
            yyerror(ErrorHelper.incompatibleTypes(r.type(), "number"));
        }
        CmpOpNode node = new CmpOpNode(l, r, val_peek(1).sval);
        node.type(BOType.BOOL);
        yyval.obj = node;
    }
break;
case 64:
//#line 536 "./borzhch.y"
{
        NodeAST l = (NodeAST) val_peek(2).obj;
        NodeAST r = (NodeAST) val_peek(0).obj; 
        BOType infer = InferenceTypeTable.inferType(l.type(), r.type());
        if (BOType.BOOL != infer) {
            yyerror(ErrorHelper.incompatibleTypes(infer, BOType.BOOL));
        }
        LogOpNode node = new LogOpNode(l, r, "and");
        node.type(BOType.BOOL);
        yyval.obj = node;
    }
break;
case 65:
//#line 547 "./borzhch.y"
{
        NodeAST l = (NodeAST) val_peek(2).obj;
        NodeAST r = (NodeAST) val_peek(0).obj; 
        BOType infer = InferenceTypeTable.inferType(l.type(), r.type());
        if (BOType.BOOL != infer) {
            yyerror(ErrorHelper.incompatibleTypes(infer, BOType.BOOL));
        }
        LogOpNode node = new LogOpNode(l, r, "or");
        node.type(BOType.BOOL);
        yyval.obj = node;
    }
break;
case 66:
//#line 558 "./borzhch.y"
{
        NodeAST l = (NodeAST) val_peek(2).obj;
        NodeAST r = (NodeAST) val_peek(0).obj; 
        BOType infer = InferenceTypeTable.inferType(l.type(), r.type());
        if (BOType.BOOL != infer) {
            yyerror(ErrorHelper.incompatibleTypes(infer, BOType.BOOL));
        }
        LogOpNode node = new LogOpNode(l, r, "xor");
        node.type(BOType.BOOL);
        yyval.obj = node;
    }
break;
case 67:
//#line 569 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 68:
//#line 570 "./borzhch.y"
{
        NodeAST r = (NodeAST) val_peek(0).obj; 
        if (BOType.BOOL != r.type()) {
            yyerror(ErrorHelper.incompatibleTypes(r.type(), BOType.BOOL));
        }
        UnOpNode node = new UnOpNode(r, "not");
        node.type(BOType.BOOL);
        yyval.obj = node;
    }
break;
case 69:
//#line 579 "./borzhch.y"
{
        NodeAST e = (NodeAST) val_peek(2).obj; 
        if (!BOHelper.isNumber(e.type())) {
            yyerror(ErrorHelper.incompatibleTypes(e.type(), "number"));
        }
        UnOpNode node = new UnOpNode(e, val_peek(3).sval);
        node.type(e.type());
        yyval.obj = node;
    }
break;
case 70:
//#line 588 "./borzhch.y"
{ 
      if(!isIdentifierExist(val_peek(1).sval)) {
        String msg = String.format("identifier <%s> is not declared\n", val_peek(1).sval);
        yyerror(msg);
      }
      yyval.obj = new PostOpNode(new VariableNode(val_peek(1).sval), val_peek(0).sval); 
        ((NodeAST) yyval.obj).type(BOType.INT);
        ((PostOpNode) yyval.obj).setPush(true);
   }
break;
case 71:
//#line 597 "./borzhch.y"
{ 
        if(!isTypeExist(val_peek(0).sval)) {
            String msg = String.format("unknown type <%s>\n", val_peek(0).sval);
            yyerror(msg);
        }
        yyval.obj = new NewObjectNode(val_peek(0).sval); 
    }
break;
case 72:
//#line 604 "./borzhch.y"
{
        if(!isIdentifierExist(val_peek(3).sval)) {
            String msg = String.format("identifier <%s> is not declared\n", val_peek(3).sval);
            yyerror(msg);
        }
        FunctionCallNode node = new FunctionCallNode(val_peek(3).sval, (StatementList) val_peek(1).obj);
        yyval.obj = node;
    }
break;
case 73:
//#line 612 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 74:
//#line 613 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 75:
//#line 614 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 76:
//#line 615 "./borzhch.y"
{ yyval.obj = new IntegerNode(val_peek(0).ival); }
break;
case 77:
//#line 616 "./borzhch.y"
{ yyval.obj = new FloatNode((float)val_peek(0).dval); }
break;
case 78:
//#line 617 "./borzhch.y"
{ yyval.obj = new StringNode(val_peek(0).sval); }
break;
case 79:
//#line 618 "./borzhch.y"
{ yyval.obj = new BooleanNode(val_peek(0).ival); }
break;
case 80:
//#line 619 "./borzhch.y"
{ 
	    if(!isIdentifierExist(val_peek(0).sval)) {
            String msg = String.format("identifier <%s> is not declared\n", val_peek(0).sval);
            yyerror(msg);
        }
        yyval.obj = new VariableNode(val_peek(0).sval, topTable.getSymbolType(val_peek(0).sval)); 
    }
break;
case 81:
//#line 629 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 82:
//#line 633 "./borzhch.y"
{
        if(!isIdentifierExist(val_peek(3).sval)) {
          String msg = String.format("identifier <%s> is not declared\n", val_peek(3).sval);
          yyerror(msg);
        }

        VariableNode var = new VariableNode(val_peek(3).sval, topTable.getBaseType(val_peek(3).sval));
        var.type(BOType.REF);
        ArrayElementNode node = new ArrayElementNode(var, (NodeAST) val_peek(1).obj);
        yyval.obj = node;
    }
break;
case 83:
//#line 646 "./borzhch.y"
{
            TupleNode node = new TupleNode((StatementList) val_peek(1).obj);
            yyval.obj = node;
           }
break;
case 84:
//#line 652 "./borzhch.y"
{
          yyval.obj = null;
        }
break;
case 85:
//#line 655 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 86:
//#line 660 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 87:
//#line 668 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 88:
//#line 674 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
//#line 1477 "Parser.java"
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
