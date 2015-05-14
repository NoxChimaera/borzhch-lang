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
  import java.util.LinkedList;
  import java.util.ArrayList;
  import java.util.HashMap;
  import edu.borzhch.ast.*;
  import edu.borzhch.helpers.*;
  import edu.borzhch.SymTable;
  import edu.borzhch.FuncTable;
  import edu.borzhch.StructTable;
  import edu.borzhch.constants.*;
//#line 29 "Parser.java"




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
public final static short CLASS=265;
public final static short STRUCT=266;
public final static short L_SQBRACE=267;
public final static short R_SQBRACE=268;
public final static short GOTO=269;
public final static short RETURN=270;
public final static short BREAK=271;
public final static short CONTINUE=272;
public final static short IF=273;
public final static short L_BRACE=274;
public final static short R_BRACE=275;
public final static short ELSE=276;
public final static short FOR=277;
public final static short WHILE=278;
public final static short DO=279;
public final static short SWITCH=280;
public final static short NEW=281;
public final static short STRING=282;
public final static short BOOLEAN=283;
public final static short COMMA=284;
public final static short ASSIGN=285;
public final static short DOT=286;
public final static short SEMICOLON=287;
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
public final static short PRINT=301;
public final static short INPUT=302;
public final static short IFX=303;
public final static short OR=304;
public final static short AND=305;
public final static short XOR=306;
public final static short POW=307;
public final static short UN_ARITHM=308;
public final static short NOT=309;
public final static short tuple_value=310;
public final static short reference=311;
public final static short idref_tail=312;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,   42,    1,    1,    2,    2,    2,   43,   44,   25,
   25,   18,   18,   18,    7,    7,    7,    6,    6,   21,
   15,   31,   26,   32,   30,   30,   30,   30,   11,    9,
    9,    8,    8,   10,   10,   13,   13,   13,   13,   13,
   19,   20,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   22,   22,   22,   22,   23,   24,
   38,   39,   39,   39,   41,   40,   37,   34,   34,   34,
   16,   17,   17,   17,   14,   14,   14,   27,   27,   28,
   28,   29,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
   36,   33,   33,   33,   33,   33,   35,    4,    4,    4,
    5,    5,
};
final static short yylen[] = {                            2,
    2,    0,    0,    2,    1,    1,    1,    1,    1,    1,
    1,    7,    9,    6,    0,    1,    2,    3,    2,    3,
    3,    3,    1,    3,    2,    1,    3,    2,    3,    0,
    3,    2,    4,    7,    3,    0,    3,    2,    2,    2,
    2,    3,    1,    1,    1,    2,    2,    1,    1,    1,
    1,    1,    1,    2,    3,    3,    3,    7,    4,    3,
    2,    0,    1,    3,    1,    0,    2,    1,    1,    1,
    6,    0,    2,    2,    9,    5,    7,   11,    7,    0,
    2,    4,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    2,    2,    2,    2,    1,    1,    1,    1,    1,
    4,    1,    1,    1,    1,    1,    4,    0,    1,    2,
    3,    2,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    0,    1,    0,    6,    5,
    7,   10,   11,    0,    0,   23,    0,    0,    4,    0,
    0,    0,    8,   22,    0,   21,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   17,    0,    0,   28,    9,   24,    0,   29,    0,
    0,   14,    0,    0,    0,   27,   31,   12,    0,    0,
    0,    0,   49,   50,    0,    0,    0,    0,    0,    0,
    0,   44,    0,    0,    0,    0,   51,   45,    0,    0,
    0,    0,    0,    0,   18,   33,    0,    0,    0,    0,
   54,   46,    0,  102,  103,    0,    0,  104,  105,    0,
  106,    0,    0,    0,    0,  100,   68,   99,   97,    0,
   70,   96,    0,    0,    0,    0,    0,    0,    0,    0,
   20,   39,   38,    0,    0,   40,   61,    0,   65,   66,
    0,   13,    0,    0,    0,    0,   94,    0,   95,   93,
    0,    0,   92,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   37,
    0,    0,    0,   60,    0,   59,    0,  110,  107,   91,
    0,   42,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   64,    0,
  101,    0,    0,   76,    0,    0,    0,    0,  111,    0,
   71,    0,    0,    0,    0,    0,    0,    0,   73,   74,
    0,    0,    0,   77,    0,    0,   81,   34,   58,    0,
    0,    0,   75,   82,    0,    0,   78,
};
final static short yydgoto[] = {                          1,
    7,    8,  105,  135,  168,   42,   30,   71,   37,   72,
   26,   73,   74,   75,    9,   76,  201,   34,   77,  106,
   52,   78,  107,  108,   32,   17,   81,  205,  206,   35,
   11,   24,  109,  110,  111,  112,    0,   84,  130,  164,
  131,    2,   53,   47,
};
final static short yysindex[] = {                         0,
    0,   26, -237, -255, -222, -205,    0,   26,    0,    0,
    0,    0,    0, -157, -204,    0, -208, -208,    0, -194,
 -160, -237,    0,    0,   36,    0, -237, -237, -140, -170,
 -143, -112, -137,   36, -110, -125, -110, -109, -105, -208,
 -237,    0,  -94,   36,    0,    0,    0, -237,    0, -208,
 -237,    0,  -40, -143,  -85,    0,    0,    0,  -90, -127,
  -72, -196,    0,    0,  -83,  -81,  -73, -208,  -71, -196,
  -96,    0,  -79, -110,  -40,  -40,    0,    0,  -76,  -75,
  -40,  -84,    0,  -44,    0,    0, -208, -196, -196, -196,
    0,    0, -248,    0,    0, -196,  -30,    0,    0, -196,
    0,  -42,  -31, -196,  366,    0,    0,    0,    0,  -84,
    0,    0, -196, -237, -196,  -37, -196,  366, -185,  -40,
    0,    0,    0, -196,  -47,    0,    0, -218,    0,    0,
  -36,    0, -214,  340,  -24,  366,    0,  162,    0,    0,
 -196,  -21,    0, -196, -196, -196, -196, -196, -196, -196,
 -196,  303,  -96,  -27,  308,  -29,  321, -163,  366,    0,
  366, -163,  366,    0,  -44,    0, -196,    0,    0,    0,
  353,    0,  -63, -219, -236,  -49,  123,  370,  184,  -63,
 -208, -196, -208, -196,   -7,    0,    1,    2,    0,  340,
    0,  -11, -100,    0,  336,  -23, -196, -196,    0, -257,
    0,    9,  -16,   15,   16,  -23,  144,  148,    0,    0,
 -165,    7,  -75,    0,   12,    5,    0,    0,    0, -208,
  -40,   45,    0,    0,  -40,   49,    0,
};
final static short yyrindex[] = {                         0,
    0,  314,    0,    0,    0,    0,    0,  314,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   44,    0,    0,    0,    0,   56,   44,    0,    0,
   47,    0,    0,   60,    0,    0,    0,    0,    0,    0,
    0,    0, -270,   61,    0,    0,    0,   56,    0,    0,
   44,    0,   68,   59,    0,    0,    0,    0,    0, -249,
    0, -125,    0,    0,    0,    0,    0,    0,    0,    0,
   54,    0,    0,    0, -246, -246,    0,    0,   57,   58,
 -246,    0, -161,   55,    0,    0,    0,    0,   69,    0,
    0,    0,   79,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   62,    0,    0,    0,    0,  127,
    0,    0,    0,    0,    0,    0,    0,   70,    0, -246,
    0,    0,    0,    0,    0,    0,    0,   31,    0,    0,
  103,    0,    0,   71,    0, -154,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   77,    0,
 -153,    0, -132,    0,   55,    0,    0,    0,    0,    0,
    0,    0,  188,  227,  242,  266, -152,  290,  276,  203,
    0,    0,    0,    0,    0, -168,    0,    0,    0,   73,
    0,    6,    0,    0,    0,   92,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   92,    0,    0,    0,    0,
   72,    0,    0,    0,    0,   32,    0,    0,    0,    0,
 -246,    0,    0,    0,   68,    0,    0,
};
final static short yygindex[] = {                         0,
  359,    0,   75,    0,  178,  315,   -1,   17,  322,  258,
    0,    0,  -68,    0,    0,  173,    0,   64,    0,    0,
  -39,  179,  -51,  -43,   -2,    0,    0,  174,    0,   13,
    0,    0,    0,  -53,  -41,    0,    0,    0,  217,    0,
    0,    0,  159,  -33,
};
final static int YYTABLESIZE=677;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         82,
   14,   79,   15,   49,   32,   23,  122,  123,   11,   80,
   58,   83,  126,   32,   32,   65,   32,   36,   88,   12,
   13,   82,   82,   79,   79,   89,   38,   82,  116,   79,
  129,   80,   80,   83,   83,   16,   69,   80,   31,   83,
  121,   33,   36,   36,   31,  137,   45,  132,   88,   59,
   33,  160,   18,  166,   23,   89,   56,   54,  144,  145,
   33,   93,   94,   95,   36,   10,   82,   31,   79,   22,
  151,   10,   93,   94,   95,  144,   80,   96,   83,   28,
  144,  145,  146,  147,   97,   98,   99,  151,   96,  148,
  149,  150,  151,   12,  186,  158,   98,   99,   11,  100,
   20,   88,  101,  102,   40,  103,   95,   29,   89,   21,
  100,  129,  104,  101,  102,   89,  103,   39,   95,   90,
   56,   57,   89,  104,   70,   52,   95,   95,   95,   95,
  153,   89,   56,   57,   89,   95,   95,   95,   95,   88,
   41,  192,   55,  194,  118,   43,   89,   89,   82,   44,
   79,   89,  224,   46,   55,  187,  226,   90,  213,  188,
  210,   48,  133,  134,  136,   50,   91,   82,   51,   79,
  138,   82,   55,   79,  140,   25,   27,   80,  143,   83,
  223,   80,   86,   83,   87,   92,  202,  152,  119,  155,
  113,  157,  114,  159,  144,  145,  146,  147,  161,  163,
  115,  127,  117,  148,  149,  150,  151,  120,  124,  125,
   93,   94,   95,  128,  141,  171,   12,   60,  173,  174,
  175,  176,  177,  178,  179,  180,   96,  139,   61,   62,
   63,   64,   65,  162,   98,   99,   66,   67,   68,   69,
  156,  190,  142,  151,  184,  144,  145,  146,  100,  165,
  169,  101,  102,  172,  103,  196,  193,  151,  195,  182,
   70,  104,   72,   72,  200,  204,  211,  197,  198,   72,
  214,  207,  208,  215,   72,   72,   72,   72,   72,  216,
  222,  220,   72,   72,   72,   72,    3,    4,   79,   79,
    5,    6,   12,   13,   72,   79,    3,    4,   69,  221,
   79,   79,   79,   79,   79,   69,   72,  225,   79,   79,
   79,   79,  227,    3,   69,   69,   69,   69,   15,   30,
   79,   16,   62,   26,   25,   69,   69,   69,   69,   62,
   69,   36,   79,   19,   69,   69,   69,   69,   62,   62,
   43,   62,   68,  108,   53,  109,   69,  112,   47,   62,
   62,   62,   62,   69,   62,   80,   41,   69,   62,   62,
   62,   62,   69,   35,   69,   69,   19,  199,   85,   57,
   63,  154,  209,   69,   69,   69,   69,   63,   69,  217,
  212,  189,   69,   69,   69,   69,   63,   63,    0,   63,
    0,    0,    0,    0,   98,    0,    0,   63,   63,   63,
   63,   98,   63,    0,    0,    0,   63,   63,   63,   63,
   98,  218,    0,   98,    0,  219,    0,  144,  145,  146,
  147,   98,   98,   98,   98,    0,   98,  149,  150,  151,
   98,   98,   98,   98,    0,    0,  170,    0,  144,  145,
  146,  147,  144,  145,  146,  147,    0,  148,  149,  150,
  151,  148,  149,  150,  151,   84,  144,  145,  146,  147,
    0,    0,   84,    0,    0,  148,  149,  150,  151,    0,
   85,   84,    0,    0,   84,    0,    0,   85,  144,  145,
  146,  147,   84,   84,   84,   84,   85,   84,    0,   85,
  151,   84,   84,   84,   83,    0,    0,   85,   85,   85,
   85,   83,   85,    0,    0,    0,   85,   85,   85,   87,
   83,    0,    0,   83,    0,    0,   87,    0,    0,    0,
    0,    0,   83,   83,   83,   87,   83,    0,   87,    0,
   83,   83,   83,   86,    0,    0,    0,    0,   87,   87,
   86,   87,    0,   90,    0,   87,   87,   87,    0,   86,
   90,    0,   86,    0,    0,    0,    0,   88,    0,   90,
    0,    0,   90,   86,   88,   86,    0,    0,    0,   86,
   86,   86,    0,   88,    0,   90,   88,  181,    0,   90,
   90,   90,  183,    0,    0,    0,    0,    0,    0,   88,
    0,    0,    0,   88,   88,  185,    0,  144,  145,  146,
  147,    0,  144,  145,  146,  147,  148,  149,  150,  151,
  203,  148,  149,  150,  151,  144,  145,  146,  147,    0,
    0,    0,    0,  167,  148,  149,  150,  151,    0,    0,
  144,  145,  146,  147,  144,  145,  146,  147,    0,  148,
  149,  150,  151,  148,  149,  150,  151,  144,  145,  146,
  147,    0,  191,    0,    0,    0,  148,  149,  150,  151,
  144,  145,  146,  147,  144,  145,  146,  147,    0,  148,
  149,  150,  151,    0,    0,  150,  151,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         53,
    3,   53,  258,   37,  275,  263,   75,   76,  258,   53,
   50,   53,   81,  284,  285,  273,  287,  264,  267,  257,
  258,   75,   76,   75,   76,  274,   28,   81,   68,   81,
   84,   75,   76,   75,   76,  258,  286,   81,   22,   81,
   74,   25,  289,   27,   28,  294,   34,   87,  267,   51,
   34,  120,  258,  268,  263,  274,   44,   41,  295,  296,
   44,  258,  259,  260,   48,    2,  120,   51,  120,  274,
  307,    8,  258,  259,  260,  295,  120,  274,  120,  274,
  295,  296,  297,  298,  281,  282,  283,  307,  274,  304,
  305,  306,  307,  257,  258,  281,  282,  283,  267,  296,
  258,  267,  299,  300,  275,  302,  275,  268,  274,  267,
  296,  165,  309,  299,  300,  268,  302,  258,  287,  285,
  275,  275,  275,  309,  286,  287,  295,  296,  297,  298,
  114,  284,  287,  287,  287,  304,  305,  306,  307,  267,
  284,  181,  275,  183,   70,  258,  274,  300,  202,  287,
  202,  304,  221,  264,  287,  158,  225,  285,  202,  162,
  200,  287,   88,   89,   90,  275,  294,  221,  274,  221,
   96,  225,  267,  225,  100,   17,   18,  221,  104,  221,
  220,  225,  268,  225,  275,  258,  287,  113,  285,  115,
  274,  117,  274,  119,  295,  296,  297,  298,  124,  125,
  274,  286,  274,  304,  305,  306,  307,  287,  285,  285,
  258,  259,  260,  258,  257,  141,  257,  258,  144,  145,
  146,  147,  148,  149,  150,  151,  274,  258,  269,  270,
  271,  272,  273,  281,  282,  283,  277,  278,  279,  280,
  278,  167,  274,  307,  274,  295,  296,  297,  296,  286,
  275,  299,  300,  275,  302,  263,  182,  307,  184,  287,
  301,  309,  257,  258,  276,  289,  258,  267,  267,  264,
  287,  197,  198,  259,  269,  270,  271,  272,  273,  264,
  276,  275,  277,  278,  279,  280,  261,  262,  257,  258,
  265,  266,  257,  258,  289,  264,  261,  262,  268,  288,
  269,  270,  271,  272,  273,  275,  301,  263,  277,  278,
  279,  280,  264,    0,  284,  285,  286,  287,  275,  264,
  289,  275,  268,  264,  264,  295,  296,  297,  298,  275,
  300,  264,  301,  275,  304,  305,  306,  307,  284,  285,
  287,  287,  286,  275,  287,  275,  268,  275,  287,  295,
  296,  297,  298,  275,  300,  264,  287,  286,  304,  305,
  306,  307,  284,  287,  286,  287,    8,  190,   54,   48,
  268,  114,  200,  295,  296,  297,  298,  275,  300,  206,
  202,  165,  304,  305,  306,  307,  284,  285,   -1,  287,
   -1,   -1,   -1,   -1,  268,   -1,   -1,  295,  296,  297,
  298,  275,  300,   -1,   -1,   -1,  304,  305,  306,  307,
  284,  268,   -1,  287,   -1,  268,   -1,  295,  296,  297,
  298,  295,  296,  297,  298,   -1,  300,  305,  306,  307,
  304,  305,  306,  307,   -1,   -1,  275,   -1,  295,  296,
  297,  298,  295,  296,  297,  298,   -1,  304,  305,  306,
  307,  304,  305,  306,  307,  268,  295,  296,  297,  298,
   -1,   -1,  275,   -1,   -1,  304,  305,  306,  307,   -1,
  268,  284,   -1,   -1,  287,   -1,   -1,  275,  295,  296,
  297,  298,  295,  296,  297,  298,  284,  300,   -1,  287,
  307,  304,  305,  306,  268,   -1,   -1,  295,  296,  297,
  298,  275,  300,   -1,   -1,   -1,  304,  305,  306,  268,
  284,   -1,   -1,  287,   -1,   -1,  275,   -1,   -1,   -1,
   -1,   -1,  296,  297,  298,  284,  300,   -1,  287,   -1,
  304,  305,  306,  268,   -1,   -1,   -1,   -1,  297,  298,
  275,  300,   -1,  268,   -1,  304,  305,  306,   -1,  284,
  275,   -1,  287,   -1,   -1,   -1,   -1,  268,   -1,  284,
   -1,   -1,  287,  298,  275,  300,   -1,   -1,   -1,  304,
  305,  306,   -1,  284,   -1,  300,  287,  275,   -1,  304,
  305,  306,  275,   -1,   -1,   -1,   -1,   -1,   -1,  300,
   -1,   -1,   -1,  304,  305,  275,   -1,  295,  296,  297,
  298,   -1,  295,  296,  297,  298,  304,  305,  306,  307,
  275,  304,  305,  306,  307,  295,  296,  297,  298,   -1,
   -1,   -1,   -1,  284,  304,  305,  306,  307,   -1,   -1,
  295,  296,  297,  298,  295,  296,  297,  298,   -1,  304,
  305,  306,  307,  304,  305,  306,  307,  295,  296,  297,
  298,   -1,  300,   -1,   -1,   -1,  304,  305,  306,  307,
  295,  296,  297,  298,  295,  296,  297,  298,   -1,  304,
  305,  306,  307,   -1,   -1,  306,  307,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=312;
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
"L_CURBRACE","R_CURBRACE","CLASS","STRUCT","L_SQBRACE","R_SQBRACE","GOTO",
"RETURN","BREAK","CONTINUE","IF","L_BRACE","R_BRACE","ELSE","FOR","WHILE","DO",
"SWITCH","NEW","STRING","BOOLEAN","COMMA","ASSIGN","DOT","SEMICOLON","COLON",
"CASE","TUPLE","INCLUDE","UN_MINUS","UN_PLUS","INCR","MUL_ARITHM","ADD_ARITHM",
"MORELESS","EQ","NULL","PIPE","PRINT","INPUT","IFX","OR","AND","XOR","POW",
"UN_ARITHM","NOT","tuple_value","reference","idref_tail",
};
final static String yyrule[] = {
"$accept : start",
"start : init global_list",
"init :",
"global_list :",
"global_list : global global_list",
"global : function",
"global : struct_decl",
"global : class_decl",
"openblock : L_CURBRACE",
"endblock : R_CURBRACE",
"type : TYPE",
"type : IDENTIFIER",
"function : DEFUN type IDENTIFIER L_BRACE param_list R_BRACE codeblock",
"function : DEFUN type L_SQBRACE R_SQBRACE IDENTIFIER L_BRACE param_list R_BRACE codeblock",
"function : PROC IDENTIFIER L_BRACE param_list R_BRACE codeblock",
"param_list :",
"param_list : decl",
"param_list : decl param_tail",
"param_tail : COMMA decl param_tail",
"param_tail : COMMA decl",
"codeblock : openblock stmt_list endblock",
"struct_decl : STRUCT IDENTIFIER decl_block",
"class_decl : CLASS class_identifier class_block",
"class_identifier : IDENTIFIER",
"class_block : openblock class_list endblock",
"class_list : decl SEMICOLON",
"class_list : function",
"class_list : decl SEMICOLON class_list",
"class_list : function class_list",
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
"builtin_in : PRINT exp",
"builtin_out : INPUT L_BRACE R_BRACE",
"stmt : decl",
"stmt : decl_assign",
"stmt : assign",
"stmt : GOTO IDENTIFIER",
"stmt : RETURN exp",
"stmt : RETURN",
"stmt : BREAK",
"stmt : CONTINUE",
"stmt : builtin_in",
"stmt : funcall",
"stmt : idref",
"stmt : IDENTIFIER INCR",
"assign : idref ASSIGN exp",
"assign : IDENTIFIER ASSIGN exp",
"assign : arrayref ASSIGN exp",
"assign : idref ASSIGN NEW type L_SQBRACE exp R_SQBRACE",
"arrayref : IDENTIFIER L_SQBRACE exp R_SQBRACE",
"idref : idref_begin idref_mid idref_end",
"idref_begin : dynamic_value DOT",
"idref_mid :",
"idref_mid : dv_in_context",
"idref_mid : dv_in_context DOT idref_mid",
"dv_in_context : dynamic_value",
"idref_end :",
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
"exp : ADD_ARITHM exp",
"exp : IDENTIFIER INCR",
"exp : NEW IDENTIFIER",
"exp : cast",
"exp : constant",
"exp : dynamic_value",
"exp : idref",
"exp : builtin_out",
"cast : PIPE TYPE exp PIPE",
"constant : INTEGER",
"constant : FLOAT",
"constant : STRING",
"constant : BOOLEAN",
"constant : NULL",
"funcall : IDENTIFIER L_BRACE exp_list R_BRACE",
"exp_list :",
"exp_list : exp",
"exp_list : exp exp_tail",
"exp_tail : COMMA exp exp_tail",
"exp_tail : COMMA exp",
};

//#line 862 "./borzhch.y"

private SymTable topTable = null;
private static FuncTable funcTable = null;
private SymTable structTable = null;

private SymTable backup;

private HashMap<String, SymTable> context = new HashMap<>();

private void fullRestoreContext() {
    while (null != topTable.getPrevious()) {
        topTable = topTable.getPrevious();
    }
}

private void restoreContext() {
    topTable = topTable.getPrevious();
}
private String mainClass = "Program";
private String currentClass = "Program";

private static boolean parseError = false;

public static boolean wasParseError() {
    return parseError;
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

private String getSymbolType(String identifier) {
    String result = topTable.getSymbolType(identifier);
    
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
    parseError = true;
    String msg = String.format("Error on line %d, column %d: %s", lexer.Yyline(), lexer.Yycolumn(), error);
    throw new Error(msg);
}

public Parser(Reader r, boolean debug) {
  lexer = new Lexer(r, this);
  yydebug = debug;
}
//#line 646 "Parser.java"
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
//#line 65 "./borzhch.y"
{ 
        TreeAST.setRoot((NodeAST) val_peek(0).obj); 
     }
break;
case 2:
//#line 70 "./borzhch.y"
{
        topTable = new SymTable(null);
        
        funcTable = new FuncTable();

        structTable = new SymTable(null);

        structTable.pushSymbol("Program", "class");
    }
break;
case 3:
//#line 81 "./borzhch.y"
{ 
           yyval.obj = null; 
    }
break;
case 4:
//#line 84 "./borzhch.y"
{ 
        StatementList list = new StatementList(); 
        list.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) list.addAll((NodeList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 5:
//#line 92 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 6:
//#line 93 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 7:
//#line 94 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 8:
//#line 98 "./borzhch.y"
{
        topTable = new SymTable(topTable);
    }
break;
case 9:
//#line 103 "./borzhch.y"
{
            /*SymTable oldTable = topTable;
            topTable = oldTable.getPrevious();
            oldTable.setPrevious(null);
            oldTable.clear();*/
        }
break;
case 10:
//#line 112 "./borzhch.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 11:
//#line 113 "./borzhch.y"
{ 
        if (!isTypeExist(val_peek(0).sval)) {
            yyerror(String.format("can not resolve symbol <%s>\n", val_peek(0).sval));
        }
        yyval.sval = val_peek(0).sval; 
    }
break;
case 12:
//#line 122 "./borzhch.y"
{
        if (isIdentifierExist(val_peek(4).sval)) {
            yyerror(String.format("identifier <%s> is already defined", val_peek(4).sval));
        }
        FunctionNode node = new FunctionNode(val_peek(4).sval, val_peek(5).sval, currentClass);
        node.setArguments((NodeList) val_peek(2).obj);
        node.setStatements((StatementList) val_peek(0).obj);

        context.put(val_peek(4).sval, topTable);
        restoreContext();
        funcTable.push(node);
        yyval.obj = node;
    }
break;
case 13:
//#line 135 "./borzhch.y"
{
        if (isIdentifierExist(val_peek(4).sval)) {
            yyerror(String.format("identifier <%s> is already defined", val_peek(4).sval));
        }
        FunctionNode node = new FunctionNode(val_peek(4).sval, "$array", currentClass);
        node.setArguments((NodeList) val_peek(2).obj);
        node.setStatements((StatementList) val_peek(0).obj);

        context.put(val_peek(4).sval, topTable);
        restoreContext();
        funcTable.push(node);
        yyval.obj = node;
    }
break;
case 14:
//#line 148 "./borzhch.y"
{
        if(isIdentifierExist(val_peek(4).sval)) {
          String msg = ErrorHelper.identifierExists(val_peek(4).sval);
          yyerror(msg);
        }
        FunctionNode func = new FunctionNode(val_peek(4).sval, BOType.VOID, currentClass);
        func.setArguments((StatementList) val_peek(2).obj);
        func.setStatements((StatementList) val_peek(0).obj);

        context.put(val_peek(4).sval, topTable);
        restoreContext();
        funcTable.push(func);
    
        yyval.obj = func;
    }
break;
case 15:
//#line 165 "./borzhch.y"
{ yyval.obj = null; }
break;
case 16:
//#line 166 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 17:
//#line 171 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
        }
break;
case 18:
//#line 179 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((NodeList) val_peek(0).obj);
            yyval.obj = node; 
          }
break;
case 19:
//#line 185 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
          }
break;
case 20:
//#line 193 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 21:
//#line 197 "./borzhch.y"
{
        if(isIdentifierExist(val_peek(1).sval)) {
            String msg = ErrorHelper.identifierExists(val_peek(1).sval);
            yyerror(msg);
        }
        
        context.put(val_peek(1).sval, topTable);
        restoreContext();
        structTable.pushSymbol(val_peek(1).sval, "ref");

        StructDeclarationNode node = new StructDeclarationNode(val_peek(1).sval, (FieldList) val_peek(0).obj, false);
        yyval.obj = node;
    }
break;
case 22:
//#line 213 "./borzhch.y"
{
        String identifier = (String) val_peek(1).sval;
        if(isIdentifierExist(identifier)) {
            String msg = ErrorHelper.identifierExists(identifier);
            yyerror(msg);
        }
        context.put(val_peek(1).sval, topTable);
        restoreContext();
        structTable.pushSymbol(identifier, "ref");

        currentClass = mainClass;
        StructDeclarationNode node = new StructDeclarationNode(identifier, (FieldList) val_peek(0).obj, true);
        yyval.obj = node;
    }
break;
case 23:
//#line 230 "./borzhch.y"
{
        funcTable = new FuncTable(funcTable);
        currentClass = val_peek(0).sval; 
        yyval.sval = val_peek(0).sval;
    }
break;
case 24:
//#line 238 "./borzhch.y"
{
        FieldList node = (FieldList) val_peek(1).obj;
        currentClass = mainClass;
        yyval.obj = node; 
    }
break;
case 25:
//#line 245 "./borzhch.y"
{ 
        FieldList node = new FieldList();
        DeclarationNode decl = (DeclarationNode) val_peek(1).obj;
        decl.isField(true);
        node.add((NodeAST) decl);
        yyval.obj = node; 
    }
break;
case 26:
//#line 252 "./borzhch.y"
{
        FieldList node = new FieldList();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 27:
//#line 257 "./borzhch.y"
{
        FieldList node = new FieldList();
        DeclarationNode decl = (DeclarationNode) val_peek(2).obj;
        decl.isField(true);
        node.add((NodeAST) decl);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 28:
//#line 265 "./borzhch.y"
{
        FieldList node = new FieldList();
        node.add((NodeAST) val_peek(1).obj);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 29:
//#line 274 "./borzhch.y"
{
        yyval.obj = val_peek(1).obj;
    }
break;
case 30:
//#line 279 "./borzhch.y"
{ yyval.obj = null; }
break;
case 31:
//#line 280 "./borzhch.y"
{
        FieldList node = new FieldList();
        DeclarationNode ldecl = (DeclarationNode) val_peek(2).obj;
        ldecl.isField(true);
        node.add(ldecl);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 32:
//#line 291 "./borzhch.y"
{
        if (isIdentifierExist(val_peek(0).sval)) {
            yyerror(String.format("identifier <%s> is already defined\n", val_peek(0).sval));
        }
        topTable.pushSymbol(val_peek(0).sval, val_peek(1).sval);
        DeclarationNode node = new DeclarationNode(val_peek(0).sval, val_peek(1).sval);
        yyval.obj = node;
    }
break;
case 33:
//#line 299 "./borzhch.y"
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
case 34:
//#line 311 "./borzhch.y"
{
        DeclarationNode decl = (DeclarationNode) val_peek(6).obj;

        decl.type(BOType.ARRAY);
        NewArrayNode nan = new NewArrayNode(val_peek(3).sval, (NodeAST) val_peek(1).obj);
        VariableNode var = new VariableNode(decl.getName(), "$array");
        var.setVarTypeName(val_peek(3).sval);
        AssignNode store = new AssignNode(var, nan);
        StatementList node = new StatementList();
        node.add(decl);
        node.add(store);
        yyval.obj = node;
    }
break;
case 35:
//#line 324 "./borzhch.y"
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
case 36:
//#line 349 "./borzhch.y"
{ yyval.obj = null; }
break;
case 37:
//#line 350 "./borzhch.y"
{ 
        StatementList list = new StatementList();
        list.add((NodeAST) val_peek(2).obj);
        if (val_peek(0).obj != null) list.addAll((StatementList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 38:
//#line 356 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 39:
//#line 362 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 40:
//#line 368 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 41:
//#line 377 "./borzhch.y"
{
        yyval.obj = new PrintNode((NodeAST) val_peek(0).obj);
    }
break;
case 42:
//#line 383 "./borzhch.y"
{
        yyval.obj = new InputNode();
    }
break;
case 43:
//#line 388 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 44:
//#line 389 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 45:
//#line 390 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 46:
//#line 391 "./borzhch.y"
{ 
      if(!isIdentifierExist(val_peek(0).sval)) {
        String msg = ErrorHelper.notDeclared(val_peek(0).sval);
        yyerror(msg);
      }
      yyval.obj = null; 
    }
break;
case 47:
//#line 398 "./borzhch.y"
{ yyval.obj = new ReturnNode((NodeAST) val_peek(0).obj); }
break;
case 48:
//#line 399 "./borzhch.y"
{ yyval.obj = new ReturnNode(null); }
break;
case 49:
//#line 400 "./borzhch.y"
{ yyval.obj = new BreakNode(); }
break;
case 50:
//#line 401 "./borzhch.y"
{ yyval.obj = new ContinueNode(); }
break;
case 51:
//#line 402 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 52:
//#line 403 "./borzhch.y"
{ 
        FunctionCallNode node = (FunctionCallNode) val_peek(0).obj;
        if (!node.isProcedure()) node.popLast(true);
        yyval.obj = node;
    }
break;
case 53:
//#line 408 "./borzhch.y"
{
        GetFieldNode node = (GetFieldNode) val_peek(0).obj;
        NodeAST last = node.getLast();
        if (last instanceof FunctionCallNode) {
            FunctionCallNode funCall = (FunctionCallNode) last;
            if(!funCall.isProcedure()) { 
                funCall.popLast(true);
                node.setLast((NodeAST) funCall);
            }
        }
        yyval.obj = node; 
    }
break;
case 54:
//#line 420 "./borzhch.y"
{ 
        VariableNode var;
        if (null == topTable.getSymbolType(val_peek(1).sval)) {
            var = new VariableNode(val_peek(1).sval, BOType.VOID);
            yyerror(String.format("identifier <%s> is not declared", val_peek(1).sval));
        } else {
            var = new VariableNode(val_peek(1).sval, topTable.getSymbolType(val_peek(1).sval)); 
        }
        yyval.obj = new PostOpNode(var, val_peek(0).sval); 
        ((NodeAST) yyval.obj).type(BOType.INT);
        ((PostOpNode) yyval.obj).setPush(true);
    }
break;
case 55:
//#line 435 "./borzhch.y"
{
        GetFieldNode get = (GetFieldNode) val_peek(2).obj;
        
        NodeAST expr = (NodeAST) val_peek(0).obj;
        SetFieldNode node = new SetFieldNode(get, expr);
        yyval.obj = node;

        /*restoreContext();*/
    }
break;
case 56:
//#line 444 "./borzhch.y"
{
        if(!isIdentifierExist(val_peek(2).sval)) {
          String msg = ErrorHelper.notDeclared(val_peek(2).sval);
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
case 57:
//#line 463 "./borzhch.y"
{
        /*arrayref := IDENTIFIER L_SQBRACE exp R_SQBRACE => ArrayElementNode*/
        
        ArrayElementNode index = (ArrayElementNode) val_peek(2).obj;
        NodeAST value = (NodeAST) val_peek(0).obj;
        SetArrayNode node = new SetArrayNode(index, value);
        yyval.obj = node;
    }
break;
case 58:
//#line 471 "./borzhch.y"
{
        GetFieldNode get = (GetFieldNode) val_peek(6).obj;
        NewArrayNode nan = new NewArrayNode(val_peek(3).sval, (NodeAST) val_peek(1).obj);
        SetFieldNode node = new SetFieldNode(get, nan);
        yyval.obj = node;

        /*restoreContext();*/
    }
break;
case 59:
//#line 482 "./borzhch.y"
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
case 60:
//#line 496 "./borzhch.y"
{
        ArrayList<NodeAST> nodes = new ArrayList<>();
        nodes.add((NodeAST) val_peek(2).obj);
        nodes.addAll((ArrayList<NodeAST>) val_peek(1).obj);
        GetFieldNode node = new GetFieldNode(nodes);
        node.generateLast(true);
        yyval.obj = node;
    }
break;
case 61:
//#line 508 "./borzhch.y"
{ 
        backup = topTable;

        INodeWithVarTypeName tmp = (INodeWithVarTypeName) val_peek(1).obj;
        String schema = tmp.getVarTypeName();

        if (!BOHelper.isType(schema) && !"$array".equals(schema)) {
            currentClass = schema;
            SymTable tmpt = context.get(schema);

            if (tmpt != topTable)
                tmpt.setPrevious(topTable);
            topTable = tmpt;
        }
        yyval.obj = val_peek(1).obj;
    }
break;
case 63:
//#line 527 "./borzhch.y"
{ 
        ArrayList<NodeAST> node = new ArrayList<>();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 64:
//#line 532 "./borzhch.y"
{ 
        ArrayList<NodeAST> node = new ArrayList<>(); 
        node.add((NodeAST) val_peek(2).obj);
        if (null != val_peek(0).obj) {
            node.addAll((ArrayList<NodeAST>) val_peek(0).obj);
        }

        yyval.obj = node;
    }
break;
case 65:
//#line 544 "./borzhch.y"
{
        INodeWithVarTypeName tmp = (INodeWithVarTypeName) val_peek(0).obj;
        String schema = tmp.getVarTypeName();

        if (!BOHelper.isType(schema) && !"$array".equals(schema)) {
            currentClass = schema;
            SymTable tmpt = context.get(schema);

            if (tmpt != topTable)
                tmpt.setPrevious(topTable);
            topTable = tmpt;
        }
        yyval.obj = val_peek(0).obj;
    }
break;
case 66:
//#line 560 "./borzhch.y"
{
        topTable = backup;
        currentClass = mainClass;
    }
break;
case 67:
//#line 568 "./borzhch.y"
{
        INodeWithVarTypeName tmp = (INodeWithVarTypeName) val_peek(1).obj;
        String schema = tmp.getVarTypeName();

        /*SymTable tmpt = topTable;*/
        /*topTable = context.get(schema);*/
        /*topTable.setPrevious(tmpt);*/

        /*??? restoreContext();*/
        if (!"void".equals(schema)) {
            currentClass = schema;
            SymTable tmpt = context.get(schema);

            if (tmpt != topTable)
                tmpt.setPrevious(topTable);
            topTable = tmpt;
        }
        yyval.obj = val_peek(1).obj;
    }
break;
case 68:
//#line 589 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 69:
//#line 590 "./borzhch.y"
{ 
        if (null == topTable.getSymbolType(val_peek(0).sval)) {
            yyval.obj = new VariableNode(val_peek(0).sval, BOType.VOID);
            yyerror(String.format("identifier <%s> is not declared", val_peek(0).sval));
        } else {
            yyval.obj = new VariableNode(val_peek(0).sval, topTable.getSymbolType(val_peek(0).sval)); 
        }
    }
break;
case 70:
//#line 598 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 71:
//#line 602 "./borzhch.y"
{
        IfNode node = new IfNode((NodeAST) val_peek(3).obj, (StatementList) val_peek(1).obj, (IfNode) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 72:
//#line 608 "./borzhch.y"
{
      yyval.obj = null;
    }
break;
case 73:
//#line 611 "./borzhch.y"
{
      yyval.obj = (IfNode) val_peek(0).obj;
    }
break;
case 74:
//#line 614 "./borzhch.y"
{
      IfNode node = new IfNode(null, (StatementList) val_peek(0).obj, null);
      yyval.obj = node;
    }
break;
case 75:
//#line 620 "./borzhch.y"
{
        NodeAST decl = (NodeAST) val_peek(6).obj;
        NodeAST counter = (NodeAST) val_peek(4).obj;
        NodeAST step = (NodeAST) val_peek(2).obj;
        NodeAST statements = (NodeAST) val_peek(0).obj; 
        ForNode node = new ForNode(decl, counter, step, statements);
        yyval.obj = node;
    }
break;
case 76:
//#line 628 "./borzhch.y"
{
        WhileNode node = new WhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 77:
//#line 632 "./borzhch.y"
{
        DoWhileNode node = new DoWhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(5).obj);
        yyval.obj = node;
    }
break;
case 78:
//#line 638 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(8).obj, (StatementList) val_peek(5).obj, (StatementList) val_peek(1).obj);
        yyval.obj = node;
      }
break;
case 79:
//#line 643 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(4).obj, (StatementList) val_peek(1).obj, null);
        yyval.obj = node;
      }
break;
case 80:
//#line 650 "./borzhch.y"
{ yyval.obj = null; }
break;
case 81:
//#line 651 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((StatementList) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 82:
//#line 658 "./borzhch.y"
{
        CaseNode node = new CaseNode(val_peek(2).ival, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 83:
//#line 665 "./borzhch.y"
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
case 84:
//#line 676 "./borzhch.y"
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
case 85:
//#line 687 "./borzhch.y"
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
case 86:
//#line 701 "./borzhch.y"
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
case 87:
//#line 712 "./borzhch.y"
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
case 88:
//#line 726 "./borzhch.y"
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
case 89:
//#line 737 "./borzhch.y"
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
case 90:
//#line 748 "./borzhch.y"
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
case 91:
//#line 759 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 92:
//#line 760 "./borzhch.y"
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
case 93:
//#line 769 "./borzhch.y"
{
        NodeAST e = (NodeAST) val_peek(0).obj; 
        if (!BOHelper.isNumber(e.type())) {
            yyerror(ErrorHelper.incompatibleTypes(e.type(), "number"));
        }
        UnOpNode node = new UnOpNode(e, val_peek(1).sval);
        node.type(e.type());
        yyval.obj = node;
    }
break;
case 94:
//#line 778 "./borzhch.y"
{ 
        VariableNode var;
        if (null == topTable.getSymbolType(val_peek(1).sval)) {
            var = new VariableNode(val_peek(1).sval, BOType.VOID);
            yyerror(String.format("identifier <%s> is not declared", val_peek(1).sval));
        } else {
            var = new VariableNode(val_peek(1).sval, topTable.getSymbolType(val_peek(1).sval)); 
        }
        yyval.obj = new PostOpNode(var, val_peek(0).sval); 
        ((NodeAST) yyval.obj).type(BOType.INT);
        ((PostOpNode) yyval.obj).setPush(true);
    }
break;
case 95:
//#line 790 "./borzhch.y"
{ 
        if(!isTypeExist(val_peek(0).sval)) {
            String msg = ErrorHelper.unknownType(val_peek(0).sval);
            yyerror(msg);
        }
        yyval.obj = new NewObjectNode(val_peek(0).sval); 
    }
break;
case 96:
//#line 797 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 97:
//#line 798 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 98:
//#line 799 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 99:
//#line 800 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 100:
//#line 801 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 101:
//#line 805 "./borzhch.y"
{
        BOType type = BOHelper.getType(val_peek(2).sval);
        NodeAST exp = (NodeAST) val_peek(1).obj;
        CastNode node = new CastNode(type, exp);
        yyval.obj = node;
    }
break;
case 102:
//#line 814 "./borzhch.y"
{ yyval.obj = new IntegerNode(val_peek(0).ival); }
break;
case 103:
//#line 815 "./borzhch.y"
{ yyval.obj = new FloatNode((float)val_peek(0).dval); }
break;
case 104:
//#line 816 "./borzhch.y"
{ yyval.obj = new StringNode(val_peek(0).sval); }
break;
case 105:
//#line 817 "./borzhch.y"
{ yyval.obj = new BooleanNode(val_peek(0).ival); }
break;
case 106:
//#line 818 "./borzhch.y"
{ yyval.obj = new NullNode(); }
break;
case 107:
//#line 822 "./borzhch.y"
{
        if(!isIdentifierExist(val_peek(3).sval)) {
          String msg = ErrorHelper.notDeclared(val_peek(3).sval);
          yyerror(msg);
        }
        FunctionCallNode node = new FunctionCallNode(val_peek(3).sval, (StatementList) val_peek(1).obj, currentClass);
        yyval.obj = node;
    }
break;
case 108:
//#line 832 "./borzhch.y"
{
          yyval.obj = null;
        }
break;
case 109:
//#line 835 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 110:
//#line 840 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 111:
//#line 848 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 112:
//#line 854 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
//#line 1808 "Parser.java"
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
