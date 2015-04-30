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
  import java.util.HashMap;
//#line 26 "Parser.java"




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
public final static short NULL=299;
public final static short PIPE=300;
public final static short IFX=301;
public final static short OR=302;
public final static short AND=303;
public final static short XOR=304;
public final static short POW=305;
public final static short UN_ARITHM=306;
public final static short NOT=307;
public final static short tuple_value=308;
public final static short reference=309;
public final static short idref_tail=310;
public final static short structref=311;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,   33,    2,    2,    3,    3,   34,   35,    1,    1,
    8,    8,   21,   21,   21,    6,    6,   13,    9,   22,
   10,   10,   16,   16,   17,   17,   14,   14,   14,   14,
   14,   27,   15,   15,   15,   15,   15,   15,   15,   15,
   15,   15,   18,   18,   18,   18,   20,   23,   23,   32,
   29,   29,   29,   11,   12,   12,   12,    7,    7,    7,
   24,   24,   26,   26,   25,   19,   19,   19,   19,   19,
   19,   19,   19,   19,   19,   19,   19,   19,   19,   19,
   19,   31,   28,   28,   28,   28,   28,   28,   30,    4,
    4,    4,    5,    5,
};
final static short yylen[] = {                            2,
    2,    0,    0,    2,    1,    1,    1,    1,    1,    1,
    7,    6,    0,    1,    2,    3,    2,    3,    3,    3,
    0,    3,    2,    4,    7,    3,    0,    3,    2,    2,
    2,    2,    1,    1,    1,    2,    2,    1,    1,    1,
    1,    1,    3,    3,    3,    7,    4,    2,    2,    2,
    1,    1,    1,    6,    0,    2,    2,    9,    5,    7,
   11,    7,    0,    2,    4,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    2,    4,    2,    2,    1,    1,
    1,    4,    1,    1,    1,    1,    1,    1,    4,    0,
    1,    2,    3,    2,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    1,    0,    5,    6,    9,
   10,    0,    0,    0,    4,    0,    0,    7,   19,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   15,
    0,    8,   20,    0,    0,    0,    0,   12,    0,   22,
   11,   24,   16,    0,    0,    0,   39,   40,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   34,
   35,    0,    0,    0,   41,    0,    0,    0,    0,    0,
    0,   36,    0,   83,   84,    0,    0,   85,   86,    0,
   87,    0,    0,    0,   51,   88,   80,    0,   53,   79,
    0,    0,    0,    0,    0,    0,   30,   29,   18,    0,
    0,    0,    0,   31,   50,    0,   49,    0,    0,    0,
    0,    0,   77,    0,   78,    0,    0,   75,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   28,    0,    0,    0,    0,    0,   47,   89,
    0,   92,   74,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   76,   82,    0,    0,   59,    0,    0,    0,
    0,   93,    0,   54,    0,    0,    0,    0,    0,    0,
    0,   56,   57,    0,    0,   60,    0,   64,    0,   25,
   46,    0,    0,    0,   58,   65,    0,    0,   61,
};
final static short yydgoto[] = {                          1,
   22,    6,    7,  110,  142,   30,   55,    8,    9,   25,
   56,  174,   38,   57,   58,   59,   60,   61,   84,   85,
   24,   19,   86,   64,  178,  179,   65,   87,   88,   89,
   90,   68,    2,   39,   33,
};
final static short yysindex[] = {                         0,
    0, -221, -171, -248, -243,    0, -221,    0,    0,    0,
    0, -220, -227, -203,    0, -195, -171,    0,    0, -171,
 -171, -216, -178, -167, -146, -163, -145, -135, -171,    0,
 -203,    0,    0, -171, -203, -133, -178,    0,  358,    0,
    0,    0,    0, -157, -121, -211,    0,    0, -134, -129,
 -127, -203, -123, -211,  358,  358, -146, -138, -144,    0,
    0, -122, -120,  358,    0, -130,    0, -107, -211, -211,
 -211,    0, -260,    0,    0, -211, -101,    0,    0, -211,
    0,  -82, -211,  392,    0,    0,    0, -130,    0,    0,
 -211, -171, -211, -100, -211,  392,    0,    0,    0,  358,
 -206, -211, -201,    0,    0, -169,    0, -130,  172,  -98,
  355,  392,    0,  265,    0,  379, -211,    0, -211, -211,
 -211, -211, -211, -211, -211, -211,  276, -144,  -96,  291,
  -79,  302,    0, -147,  392,  392, -147,  392,    0,    0,
 -211,    0,    0, -211,  344, -110, -291, -213, -109,  403,
 -125, -113, -110, -203, -211, -203, -211,  -65,    0,  -64,
  -63,  355,    0,    0,  -69,  368,    0,  316,  -84, -211,
 -211,    0, -199,    0,  -51,  -78,  -50,  -84,  -54,  198,
  211,    0,    0, -157,  -62,    0,  -77,    0,  -46,    0,
    0, -203,  358,  -27,    0,    0,  358,  -33,    0,
};
final static short yyrindex[] = {                         0,
    0,  238,    0,    0,    0,    0,  238,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -30,    0,    0,  -23,
  -30,    0,  -29,    0,    0,    0,    0, -141,    0,    0,
    0,    0,    0,  -23,    0,    0,  -28,    0,  -17,    0,
    0,    0,    0, -250,    0,  -38,    0,    0,    0,    0,
    0,    0,    0,    0, -261, -261,    0,    0,  -35,    0,
    0,  -45,    0, -261,    0,    0, -160,    0,    0,  -25,
    0,    0, -183,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -22,    0,    0,    0,    3,    0,    0,
    0,    0,    0,    0,    0,  -21,    0,    0,    0, -261,
    0,    0,    0,    0,    0,  -24,    0,  -70,    0,    0,
  -20, -235,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -19, -231,    0, -209,    0,    0,
    0,    0,    0,    0,    0,   28,   78,  103,  116,  164,
  154,  141,   53,    0,    0,    0,    0,    0,  186,    0,
    0,  -18,    0,    0,  253,    0,    0,    0,  -12,    0,
    0,    0,    0,    0,    0,    0,    0,  -12,    0,    0,
    0,    0,    0,  -10,    0,    0,    0,    0,  277,    0,
    0,    0, -261,    0,    0,    0,  -17,    0,    0,
};
final static short yygindex[] = {                         0,
   -2,  250,    0,    0,   96,  226,    0,    0,    0,  232,
  109,    0,  -26,  -44,    0,   16,  192,  110,   98,  -34,
  266,    0,  -39,    0,    0,  112,    0,    0,  -37,  -32,
    0,    0,    0,  274,  234,
};
final static int YYTABLESIZE=708;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         63,
   12,   66,   27,  119,   62,   69,   67,   10,   41,   13,
   97,   98,   70,  126,   14,   63,   63,   66,   66,  104,
   62,   62,   67,   67,   63,   94,   66,   27,  107,   62,
  108,   67,   23,  113,   52,   26,   23,   16,   44,    3,
    4,   28,   45,    5,   37,   17,   73,   74,   75,   26,
   44,   73,   74,   75,   45,  133,   73,   74,   75,   18,
   63,   76,   66,   18,   43,   62,   76,   67,   77,   78,
   79,   76,   49,  134,   78,   79,   43,   21,  137,   78,
   79,  119,  120,   52,   80,   10,   11,   81,   82,   80,
   52,  126,   81,   82,   80,   83,   69,   81,   82,   52,
   83,   52,   52,   70,   29,   83,   31,  128,   69,   10,
  159,   52,   52,   52,   52,   70,   52,   32,   52,   52,
   52,   52,   34,   52,   53,   42,   71,  165,   35,  167,
   36,  160,   23,   42,  161,   63,   72,   66,   91,  101,
   62,   23,   23,   92,   23,   93,  183,  100,  196,   95,
  106,   96,  198,   63,  105,   66,  115,   63,   62,   66,
   67,  102,   62,  103,   67,  195,  109,  111,  112,  119,
  120,  121,  122,  114,  117,  140,  131,  116,  125,  126,
  118,  119,  120,  121,  122,  119,  120,  121,  127,  155,
  130,  126,  132,  157,  126,  126,   48,  169,  135,  136,
  138,  170,  171,   48,  177,  173,  184,  186,  187,  189,
  193,  192,   48,   48,  145,   48,  146,  147,  148,  149,
  150,  151,  152,  153,   48,   48,   48,   48,  194,   48,
  199,   48,   48,   48,   48,  197,   48,    3,  162,   51,
   21,  163,   52,   13,   14,   17,   27,   38,   90,   52,
   33,   63,  166,   91,  168,   94,   15,  172,   52,   52,
   52,   52,   43,   37,   32,   40,   26,  180,  181,   81,
   52,   52,   52,   52,   52,   52,   81,   52,   52,   52,
   52,  182,   52,  129,  185,   81,   27,   20,   81,  188,
   99,    0,    0,    0,   67,    0,    0,   81,   81,   81,
   81,   67,   81,    0,   81,   81,   81,   81,    0,   81,
   67,    0,    0,   67,    0,    0,    0,    0,    0,   68,
    0,    0,   67,   67,   67,   67,   68,   67,    0,   67,
   67,   67,    0,    0,   67,   68,    0,    0,   68,    0,
    0,    0,    0,    0,   66,    0,    0,   68,   68,   68,
   68,   66,   68,    0,   68,   68,   68,    0,    0,   68,
   66,    0,    0,   66,    0,    0,    0,    0,    0,   70,
    0,    0,    0,   66,   66,   66,   70,   66,    0,   66,
   66,   66,   69,    0,   66,   70,    0,    0,   70,   69,
    0,    0,    0,    0,    0,    0,    0,    0,   69,   70,
   70,   69,   70,    0,   70,   70,   70,   73,    0,   70,
    0,    0,    0,   69,   73,   69,    0,   69,   69,   69,
   71,    0,   69,   73,    0,    0,   73,   71,    0,    0,
   72,    0,    0,    0,    0,    0,   71,   72,  139,   71,
   73,    0,   73,   73,   73,    0,   72,   73,    0,   72,
    0,   10,    0,   71,    0,   71,   71,    0,    0,   78,
   71,    0,    0,   72,  190,   72,  119,  120,  121,  122,
   72,   78,    0,  123,  124,  125,  126,  191,    0,    0,
   78,   78,   78,   78,    0,    0,    0,   78,   78,   78,
   78,    0,  119,  120,  121,  122,    0,    0,    0,  123,
  124,  125,  126,    0,    0,  119,  120,  121,  122,   55,
   55,    0,  123,  124,  125,  126,   55,    0,    0,    0,
   55,   55,   55,   55,   55,    0,    0,    0,   55,   55,
   55,   55,    0,   62,   62,    0,    0,    0,  143,   55,
   62,   55,    0,    0,   62,   62,   62,   62,   62,  154,
    0,    0,   62,   62,   62,   62,    0,    0,    0,  119,
  120,  121,  122,   62,  156,   62,  123,  124,  125,  126,
  119,  120,  121,  122,    0,  158,    0,  123,  124,  125,
  126,    0,    0,    0,    0,  119,  120,  121,  122,  176,
    0,    0,  123,  124,  125,  126,  119,  120,  121,  122,
    0,    0,    0,  123,  124,  125,  126,    0,    0,    0,
  119,  120,  121,  122,   10,   44,    0,  123,  124,  125,
  126,    0,    0,    0,    0,   45,   46,   47,   48,   49,
    0,    0,    0,   50,   51,   52,   53,  141,  119,  120,
  121,  122,    0,  164,   54,  123,  124,  125,  126,  119,
  120,  121,  122,  175,    0,    0,  123,  124,  125,  126,
    0,    0,  119,  120,  121,  122,    0,    0,    0,  123,
  124,  125,  126,  119,  120,  121,  122,    0,    0,    0,
  123,  124,  125,  126,    0,  144,  119,  120,  121,  122,
    0,    0,    0,  123,  124,  125,  126,  119,  120,  121,
  122,    0,    0,    0,    0,  124,  125,  126,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         39,
    3,   39,  264,  295,   39,  266,   39,  258,   35,  258,
   55,   56,  273,  305,  258,   55,   56,   55,   56,   64,
   55,   56,   55,   56,   64,   52,   64,  289,   68,   64,
   68,   64,   17,  294,  285,   20,   21,  258,  274,  261,
  262,  258,  274,  265,   29,  273,  258,  259,  260,   34,
  286,  258,  259,  260,  286,  100,  258,  259,  260,  263,
  100,  273,  100,  263,  274,  100,  273,  100,  280,  281,
  282,  273,  272,  280,  281,  282,  286,  273,  280,  281,
  282,  295,  296,  267,  296,  257,  258,  299,  300,  296,
  274,  305,  299,  300,  296,  307,  266,  299,  300,  283,
  307,  285,  286,  273,  283,  307,  274,   92,  266,  257,
  258,  295,  296,  297,  298,  273,  300,  264,  302,  303,
  304,  305,  286,  307,  285,  286,  284,  154,  274,  156,
  266,  134,  274,  267,  137,  175,  258,  175,  273,  284,
  175,  283,  284,  273,  286,  273,  173,  286,  193,  273,
  258,   54,  197,  193,  285,  193,  258,  197,  193,  197,
  193,  284,  197,  284,  197,  192,   69,   70,   71,  295,
  296,  297,  298,   76,  257,  274,  277,   80,  304,  305,
   83,  295,  296,  297,  298,  295,  296,  297,   91,  286,
   93,  305,   95,  273,  305,  305,  267,  263,  101,  102,
  103,  266,  266,  274,  289,  275,  258,  286,  259,  264,
  288,  274,  283,  284,  117,  286,  119,  120,  121,  122,
  123,  124,  125,  126,  295,  296,  297,  298,  275,  300,
  264,  302,  303,  304,  305,  263,  307,    0,  141,  285,
  264,  144,  267,  274,  274,  274,  264,  286,  274,  274,
  286,  264,  155,  274,  157,  274,    7,  162,  283,  284,
  285,  286,   37,  286,  286,   34,  286,  170,  171,  267,
  295,  296,  297,  298,  285,  300,  274,  302,  303,  304,
  305,  173,  307,   92,  175,  283,   21,   14,  286,  178,
   57,   -1,   -1,   -1,  267,   -1,   -1,  295,  296,  297,
  298,  274,  300,   -1,  302,  303,  304,  305,   -1,  307,
  283,   -1,   -1,  286,   -1,   -1,   -1,   -1,   -1,  267,
   -1,   -1,  295,  296,  297,  298,  274,  300,   -1,  302,
  303,  304,   -1,   -1,  307,  283,   -1,   -1,  286,   -1,
   -1,   -1,   -1,   -1,  267,   -1,   -1,  295,  296,  297,
  298,  274,  300,   -1,  302,  303,  304,   -1,   -1,  307,
  283,   -1,   -1,  286,   -1,   -1,   -1,   -1,   -1,  267,
   -1,   -1,   -1,  296,  297,  298,  274,  300,   -1,  302,
  303,  304,  267,   -1,  307,  283,   -1,   -1,  286,  274,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  283,  297,
  298,  286,  300,   -1,  302,  303,  304,  267,   -1,  307,
   -1,   -1,   -1,  298,  274,  300,   -1,  302,  303,  304,
  267,   -1,  307,  283,   -1,   -1,  286,  274,   -1,   -1,
  267,   -1,   -1,   -1,   -1,   -1,  283,  274,  267,  286,
  300,   -1,  302,  303,  304,   -1,  283,  307,   -1,  286,
   -1,  266,   -1,  300,   -1,  302,  303,   -1,   -1,  274,
  307,   -1,   -1,  300,  267,  302,  295,  296,  297,  298,
  307,  286,   -1,  302,  303,  304,  305,  267,   -1,   -1,
  295,  296,  297,  298,   -1,   -1,   -1,  302,  303,  304,
  305,   -1,  295,  296,  297,  298,   -1,   -1,   -1,  302,
  303,  304,  305,   -1,   -1,  295,  296,  297,  298,  257,
  258,   -1,  302,  303,  304,  305,  264,   -1,   -1,   -1,
  268,  269,  270,  271,  272,   -1,   -1,   -1,  276,  277,
  278,  279,   -1,  257,  258,   -1,   -1,   -1,  274,  287,
  264,  289,   -1,   -1,  268,  269,  270,  271,  272,  274,
   -1,   -1,  276,  277,  278,  279,   -1,   -1,   -1,  295,
  296,  297,  298,  287,  274,  289,  302,  303,  304,  305,
  295,  296,  297,  298,   -1,  274,   -1,  302,  303,  304,
  305,   -1,   -1,   -1,   -1,  295,  296,  297,  298,  274,
   -1,   -1,  302,  303,  304,  305,  295,  296,  297,  298,
   -1,   -1,   -1,  302,  303,  304,  305,   -1,   -1,   -1,
  295,  296,  297,  298,  257,  258,   -1,  302,  303,  304,
  305,   -1,   -1,   -1,   -1,  268,  269,  270,  271,  272,
   -1,   -1,   -1,  276,  277,  278,  279,  283,  295,  296,
  297,  298,   -1,  300,  287,  302,  303,  304,  305,  295,
  296,  297,  298,  286,   -1,   -1,  302,  303,  304,  305,
   -1,   -1,  295,  296,  297,  298,   -1,   -1,   -1,  302,
  303,  304,  305,  295,  296,  297,  298,   -1,   -1,   -1,
  302,  303,  304,  305,   -1,  307,  295,  296,  297,  298,
   -1,   -1,   -1,  302,  303,  304,  305,  295,  296,  297,
  298,   -1,   -1,   -1,   -1,  303,  304,  305,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=311;
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
"MORELESS","EQ","NULL","PIPE","IFX","OR","AND","XOR","POW","UN_ARITHM","NOT",
"tuple_value","reference","idref_tail","structref",
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
"type : TYPE",
"type : IDENTIFIER",
"function : DEFUN type IDENTIFIER L_BRACE param_list R_BRACE codeblock",
"function : PROC IDENTIFIER L_BRACE param_list R_BRACE codeblock",
"param_list :",
"param_list : decl",
"param_list : decl param_tail",
"param_tail : COMMA decl param_tail",
"param_tail : COMMA decl",
"codeblock : openblock stmt_list endblock",
"struct_decl : STRUCT IDENTIFIER decl_block",
"decl_block : openblock decl_list endblock",
"decl_list :",
"decl_list : decl SEMICOLON decl_list",
"decl : type IDENTIFIER",
"decl : type IDENTIFIER L_SQBRACE R_SQBRACE",
"decl_assign : decl ASSIGN NEW type L_SQBRACE exp R_SQBRACE",
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
"stmt : funcall",
"assign : idref ASSIGN exp",
"assign : IDENTIFIER ASSIGN exp",
"assign : arrayref ASSIGN exp",
"assign : idref ASSIGN NEW type L_SQBRACE exp R_SQBRACE",
"arrayref : IDENTIFIER L_SQBRACE exp R_SQBRACE",
"idref : dot dynamic_value",
"idref : dot idref",
"dot : dynamic_value DOT",
"dynamic_value : arrayref",
"dynamic_value : IDENTIFIER",
"dynamic_value : funcall",
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
"exp : cast",
"exp : constant",
"exp : dynamic_value",
"cast : PIPE TYPE exp PIPE",
"constant : INTEGER",
"constant : FLOAT",
"constant : STRING",
"constant : BOOLEAN",
"constant : NULL",
"constant : idref",
"funcall : IDENTIFIER L_BRACE exp_list R_BRACE",
"exp_list :",
"exp_list : exp",
"exp_list : exp exp_tail",
"exp_tail : COMMA exp exp_tail",
"exp_tail : COMMA exp",
};

//#line 676 "./borzhch.y"

private SymTable topTable = null;
private static FuncTable funcTable = null;
private SymTable structTable = null;

private HashMap<String, SymTable> context = new HashMap<>();

private void fullRestoreContext() {
    while (null != topTable.getPrevious()) {
        topTable = topTable.getPrevious();
    }
}

private void restoreContext() {
    SymTable oldTable = topTable;
    topTable = oldTable.getPrevious();
    oldTable.setPrevious(null);
    //oldTable.clear();
}

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
//#line 600 "Parser.java"
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
//#line 52 "./borzhch.y"
{ 
        TreeAST.setRoot((NodeAST) val_peek(0).obj); 
     }
break;
case 2:
//#line 57 "./borzhch.y"
{
        topTable = new SymTable(null);
        
        funcTable = new FuncTable();

        structTable = new SymTable(null);
    }
break;
case 3:
//#line 66 "./borzhch.y"
{ 
           yyval.obj = null; 
    }
break;
case 4:
//#line 69 "./borzhch.y"
{ 
        StatementList list = new StatementList(); 
        list.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) list.addAll((NodeList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 5:
//#line 77 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 6:
//#line 78 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 7:
//#line 82 "./borzhch.y"
{
        topTable = new SymTable(topTable);
    }
break;
case 8:
//#line 87 "./borzhch.y"
{
            /*SymTable oldTable = topTable;
            topTable = oldTable.getPrevious();
            oldTable.setPrevious(null);
            oldTable.clear();*/
        }
break;
case 9:
//#line 96 "./borzhch.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 10:
//#line 97 "./borzhch.y"
{ 
        if (!isTypeExist(val_peek(0).sval)) {
            yyerror(String.format("can not resolve symbol <%s>\n", val_peek(0).sval));
        }
        yyval.sval = val_peek(0).sval; 
    }
break;
case 11:
//#line 106 "./borzhch.y"
{
        if (isIdentifierExist(val_peek(4).sval)) {
            yyerror(String.format("identifier <%s> is already defined", val_peek(4).sval));
        }
        FunctionNode node = new FunctionNode(val_peek(4).sval, val_peek(5).sval);
        node.setArguments((NodeList) val_peek(2).obj);
        node.setStatements((StatementList) val_peek(0).obj);

        context.put(val_peek(4).sval, topTable);
        restoreContext();
        funcTable.push(node);
        yyval.obj = node;
    }
break;
case 12:
//#line 119 "./borzhch.y"
{
        if(isIdentifierExist(val_peek(4).sval)) {
          String msg = String.format("identifier <%s> is already defined\n", val_peek(4).sval);
          yyerror(msg);
        }
        FunctionNode func = new FunctionNode(val_peek(4).sval, BOType.VOID);
        func.setArguments((StatementList) val_peek(2).obj);
        func.setStatements((StatementList) val_peek(0).obj);

        context.put(val_peek(4).sval, topTable);
        restoreContext();
        funcTable.push(func);
    
        yyval.obj = func;
    }
break;
case 13:
//#line 136 "./borzhch.y"
{ yyval.obj = null; }
break;
case 14:
//#line 137 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 15:
//#line 142 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
        }
break;
case 16:
//#line 150 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((NodeList) val_peek(0).obj);
            yyval.obj = node; 
          }
break;
case 17:
//#line 156 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
          }
break;
case 18:
//#line 164 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 19:
//#line 168 "./borzhch.y"
{
        if(isIdentifierExist(val_peek(1).sval)) {
            String msg = String.format("identifier <%s> is already defined\n", val_peek(1).sval);
            yyerror(msg);
        }
        
        context.put(val_peek(1).sval, topTable);
        restoreContext();
        structTable.pushSymbol(val_peek(1).sval, "ref");

        StructDeclarationNode node = new StructDeclarationNode(val_peek(1).sval, (FieldList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 20:
//#line 184 "./borzhch.y"
{
        yyval.obj = val_peek(1).obj;
    }
break;
case 21:
//#line 189 "./borzhch.y"
{ yyval.obj = null; }
break;
case 22:
//#line 190 "./borzhch.y"
{
        FieldList node = new FieldList();
        DeclarationNode ldecl = (DeclarationNode) val_peek(2).obj;
        ldecl.isField(true);
        node.add(ldecl);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 23:
//#line 201 "./borzhch.y"
{
        if (isIdentifierExist(val_peek(0).sval)) {
            yyerror(String.format("identifier <%s> is already defined\n", val_peek(0).sval));
        }
        topTable.pushSymbol(val_peek(0).sval, val_peek(1).sval);
        DeclarationNode node = new DeclarationNode(val_peek(0).sval, val_peek(1).sval);
        yyval.obj = node;
    }
break;
case 24:
//#line 209 "./borzhch.y"
{
        if (isIdentifierExist(val_peek(2).sval)) {
            yyerror(String.format("identifier <%s> is already defined\n", val_peek(2).sval));
        }
        topTable.pushSymbol(val_peek(2).sval, "$array", val_peek(3).sval);
        DeclarationNode node = new DeclarationNode(val_peek(2).sval, val_peek(3).sval);
        node.type(BOType.ARRAY);
        yyval.obj = node;
    }
break;
case 25:
//#line 221 "./borzhch.y"
{
        DeclarationNode decl = (DeclarationNode) val_peek(6).obj;

        decl.type(BOType.ARRAY);
        NewArrayNode nan = new NewArrayNode(val_peek(3).sval, (NodeAST) val_peek(1).obj);
        VariableNode var = new VariableNode(decl.getName(), "$array");
        var.strType(val_peek(3).sval);
        AssignNode store = new AssignNode(var, nan);
        StatementList node = new StatementList();
        node.add(decl);
        node.add(store);
        yyval.obj = node;
    }
break;
case 26:
//#line 234 "./borzhch.y"
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
case 27:
//#line 260 "./borzhch.y"
{ yyval.obj = null; }
break;
case 28:
//#line 261 "./borzhch.y"
{ 
        StatementList list = new StatementList();
        list.add((NodeAST) val_peek(2).obj);
        if (val_peek(0).obj != null) list.addAll((StatementList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 29:
//#line 267 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 30:
//#line 273 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 31:
//#line 279 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 32:
//#line 288 "./borzhch.y"
{
        yyval.obj = new PrintNode((NodeAST) val_peek(0).obj);
    }
break;
case 33:
//#line 293 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 34:
//#line 294 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 35:
//#line 295 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 36:
//#line 296 "./borzhch.y"
{ 
      if(!isIdentifierExist(val_peek(0).sval)) {
        String msg = String.format("identifier <%s> is not declared\n", val_peek(0).sval);
        yyerror(msg);
      }
      yyval.obj = null; 
    }
break;
case 37:
//#line 303 "./borzhch.y"
{ yyval.obj = new ReturnNode((NodeAST) val_peek(0).obj); }
break;
case 38:
//#line 304 "./borzhch.y"
{ yyval.obj = new ReturnNode(null); }
break;
case 39:
//#line 305 "./borzhch.y"
{ yyval.obj = new BreakNode(); }
break;
case 40:
//#line 306 "./borzhch.y"
{ yyval.obj = new ContinueNode(); }
break;
case 41:
//#line 307 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 42:
//#line 308 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 43:
//#line 312 "./borzhch.y"
{
        GetFieldNode get = (GetFieldNode) val_peek(2).obj;
        NodeAST expr = (NodeAST) val_peek(0).obj;
        SetFieldNode node = new SetFieldNode(get, expr);
        yyval.obj = node;

        /*restoreContext();*/
    }
break;
case 44:
//#line 320 "./borzhch.y"
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
case 45:
//#line 339 "./borzhch.y"
{
        /*arrayref := IDENTIFIER L_SQBRACE exp R_SQBRACE => ArrayElementNode*/
        
        ArrayElementNode index = (ArrayElementNode) val_peek(2).obj;
        NodeAST value = (NodeAST) val_peek(0).obj;
        SetArrayNode node = new SetArrayNode(index, value);
        yyval.obj = node;
    }
break;
case 46:
//#line 347 "./borzhch.y"
{
        GetFieldNode get = (GetFieldNode) val_peek(6).obj;
        NewArrayNode nan = new NewArrayNode(val_peek(3).sval, (NodeAST) val_peek(1).obj);
        SetFieldNode node = new SetFieldNode(get, nan);
        yyval.obj = node;

        /*restoreContext();*/
    }
break;
case 47:
//#line 358 "./borzhch.y"
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
case 48:
//#line 372 "./borzhch.y"
{
        DotOpNode dot = new DotOpNode((NodeAST) val_peek(1).obj, (NodeAST) val_peek(0).obj);
        GetFieldNode node = new GetFieldNode(dot.reduce());
        yyval.obj = node;
        /*restoreContext();*/
    }
break;
case 49:
//#line 378 "./borzhch.y"
{
        DotOpNode dot = new DotOpNode((NodeAST) val_peek(1).obj, (NodeAST) val_peek(0).obj);
        GetFieldNode node = new GetFieldNode(dot.reduce());
        yyval.obj = node;

        restoreContext();
    }
break;
case 50:
//#line 388 "./borzhch.y"
{
        INodeWithVarTypeName tmp = (INodeWithVarTypeName) val_peek(1).obj;
        String schema = tmp.getVarTypeName();

        /*SymTable tmpt = topTable;*/
        /*topTable = context.get(schema);*/
        /*topTable.setPrevious(tmpt);*/

        /*restoreContext();*/
        if (!"void".equals(schema)) {
            SymTable tmpt = context.get(schema);

            if (tmpt != topTable)
                tmpt.setPrevious(topTable);
            topTable = tmpt;
        }
        yyval.obj = val_peek(1).obj;
    }
break;
case 51:
//#line 408 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 52:
//#line 409 "./borzhch.y"
{ 
        if (null == topTable.getSymbolType(val_peek(0).sval)) {
            yyval.obj = new VariableNode(val_peek(0).sval, BOType.VOID);
        } else {
            yyval.obj = new VariableNode(val_peek(0).sval, topTable.getSymbolType(val_peek(0).sval)); 
        }
    }
break;
case 53:
//#line 416 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 54:
//#line 420 "./borzhch.y"
{
        IfNode node = new IfNode((NodeAST) val_peek(3).obj, (StatementList) val_peek(1).obj, (IfNode) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 55:
//#line 426 "./borzhch.y"
{
      yyval.obj = null;
    }
break;
case 56:
//#line 429 "./borzhch.y"
{
      yyval.obj = (IfNode) val_peek(0).obj;
    }
break;
case 57:
//#line 432 "./borzhch.y"
{
      IfNode node = new IfNode(null, (StatementList) val_peek(0).obj, null);
      yyval.obj = node;
    }
break;
case 58:
//#line 438 "./borzhch.y"
{
        NodeAST decl = (NodeAST) val_peek(6).obj;
        NodeAST counter = (NodeAST) val_peek(4).obj;
        NodeAST step = (NodeAST) val_peek(2).obj;
        NodeAST statements = (NodeAST) val_peek(0).obj; 
        ForNode node = new ForNode(decl, counter, step, statements);
        yyval.obj = node;
    }
break;
case 59:
//#line 446 "./borzhch.y"
{
        WhileNode node = new WhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 60:
//#line 450 "./borzhch.y"
{
        DoWhileNode node = new DoWhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(5).obj);
        yyval.obj = node;
    }
break;
case 61:
//#line 456 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(8).obj, (StatementList) val_peek(5).obj, (StatementList) val_peek(1).obj);
        yyval.obj = node;
      }
break;
case 62:
//#line 461 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(4).obj, (StatementList) val_peek(1).obj, null);
        yyval.obj = node;
      }
break;
case 63:
//#line 468 "./borzhch.y"
{ yyval.obj = null; }
break;
case 64:
//#line 469 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((StatementList) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 65:
//#line 476 "./borzhch.y"
{
        CaseNode node = new CaseNode(val_peek(2).ival, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 66:
//#line 483 "./borzhch.y"
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
case 67:
//#line 494 "./borzhch.y"
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
case 68:
//#line 505 "./borzhch.y"
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
case 69:
//#line 519 "./borzhch.y"
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
case 70:
//#line 530 "./borzhch.y"
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
case 71:
//#line 544 "./borzhch.y"
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
case 72:
//#line 555 "./borzhch.y"
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
case 73:
//#line 566 "./borzhch.y"
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
case 74:
//#line 577 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 75:
//#line 578 "./borzhch.y"
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
case 76:
//#line 587 "./borzhch.y"
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
case 77:
//#line 596 "./borzhch.y"
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
case 78:
//#line 605 "./borzhch.y"
{ 
        if(!isTypeExist(val_peek(0).sval)) {
            String msg = String.format("unknown type <%s>\n", val_peek(0).sval);
            yyerror(msg);
        }
        yyval.obj = new NewObjectNode(val_peek(0).sval); 
    }
break;
case 79:
//#line 612 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 80:
//#line 613 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 81:
//#line 614 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 82:
//#line 618 "./borzhch.y"
{
        BOType type = BOHelper.getType(val_peek(2).sval);
        NodeAST exp = (NodeAST) val_peek(1).obj;
        CastNode node = new CastNode(type, exp);
        yyval.obj = node;
    }
break;
case 83:
//#line 627 "./borzhch.y"
{ yyval.obj = new IntegerNode(val_peek(0).ival); }
break;
case 84:
//#line 628 "./borzhch.y"
{ yyval.obj = new FloatNode((float)val_peek(0).dval); }
break;
case 85:
//#line 629 "./borzhch.y"
{ yyval.obj = new StringNode(val_peek(0).sval); }
break;
case 86:
//#line 630 "./borzhch.y"
{ yyval.obj = new BooleanNode(val_peek(0).ival); }
break;
case 87:
//#line 631 "./borzhch.y"
{ yyval.obj = new NullNode(); }
break;
case 88:
//#line 632 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 89:
//#line 636 "./borzhch.y"
{
        if(!isIdentifierExist(val_peek(3).sval)) {
            String msg = String.format("identifier <%s> is not declared\n", val_peek(3).sval);
            yyerror(msg);
        }
        FunctionCallNode node = new FunctionCallNode(val_peek(3).sval, (StatementList) val_peek(1).obj);
        yyval.obj = node;
    }
break;
case 90:
//#line 646 "./borzhch.y"
{
          yyval.obj = null;
        }
break;
case 91:
//#line 649 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 92:
//#line 654 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 93:
//#line 662 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 94:
//#line 668 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
//#line 1565 "Parser.java"
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
