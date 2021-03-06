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
    0,   43,    1,    1,    2,    2,    2,   44,   45,   26,
   26,   19,   19,   18,   18,    7,    7,    7,    6,    6,
   22,   15,   32,   27,   33,   31,   31,   31,   31,   11,
    9,    9,    8,    8,   10,   10,   13,   13,   13,   13,
   13,   20,   21,   12,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   23,   23,   23,   23,   24,
   25,   39,   40,   40,   42,   41,   38,   35,   35,   35,
   16,   17,   17,   17,   14,   14,   14,   28,   28,   29,
   29,   30,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
   37,   34,   34,   34,   34,   34,   36,    4,    4,    4,
    5,    5,
};
final static short yylen[] = {                            2,
    2,    0,    0,    2,    1,    1,    1,    1,    1,    1,
    1,    2,    4,    6,    6,    0,    1,    2,    3,    2,
    3,    3,    3,    1,    3,    2,    1,    3,    2,    3,
    0,    3,    2,    4,    7,    3,    0,    3,    2,    2,
    2,    2,    3,    1,    1,    1,    2,    2,    1,    1,
    1,    1,    1,    1,    2,    3,    3,    3,    7,    4,
    3,    2,    1,    3,    1,    0,    2,    1,    1,    1,
    6,    0,    2,    2,    9,    5,    7,   11,    7,    0,
    2,    4,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    2,    2,    2,    2,    1,    1,    1,    1,    1,
    4,    1,    1,    1,    1,    1,    4,    0,    1,    2,
    3,    2,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    0,    1,    0,    6,    5,
    7,   10,   11,    0,    0,    0,   24,    0,    0,    4,
    0,   12,    0,    0,    8,   23,    0,   22,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   18,    0,   13,    0,    0,   29,   25,    0,   30,
   14,    0,    0,    0,   15,   28,   32,    0,    0,    0,
   50,   51,    0,    0,    0,    0,    0,    0,    0,   45,
    0,    0,    0,    0,   52,   46,    0,    0,    0,    0,
    0,    0,   19,   34,    0,    0,    0,   55,   47,    0,
  102,  103,    0,    0,  104,  105,    0,  106,    0,    0,
    0,    0,  100,   68,   99,   97,    0,   70,   96,    0,
    0,    0,    0,    0,    0,    0,    0,    9,   21,   40,
   39,    0,    0,   41,   62,    0,   65,   66,    0,    0,
    0,    0,    0,   94,    0,   95,   93,    0,    0,   92,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   38,    0,    0,    0,
   61,    0,   60,    0,  110,  107,   91,    0,   43,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   64,    0,  101,    0,    0,
   76,    0,    0,    0,    0,  111,    0,   71,    0,    0,
    0,    0,    0,    0,    0,   73,   74,    0,    0,    0,
   77,    0,    0,   81,   35,   59,    0,    0,    0,   75,
   82,    0,    0,   78,
};
final static short yydgoto[] = {                          1,
    7,    8,  102,  132,  165,   42,   30,   69,   39,   70,
   28,   71,   72,   73,    9,   74,  198,   36,   14,   75,
  103,   51,   76,  104,  105,   32,   18,   79,  202,  203,
   37,   11,   26,  106,  107,  108,  109,    0,   82,  128,
  161,  129,    2,   52,  119,
};
final static short yysindex[] = {                         0,
    0,  -44, -115, -248, -217, -215,    0,  -44,    0,    0,
    0,    0,    0, -258, -253, -225,    0, -180, -180,    0,
 -115,    0, -208, -115,    0,    0,  155,    0, -115, -170,
 -174, -130, -122, -131, -140,  155, -110, -128, -107, -180,
 -115,    0, -106,    0, -180,  155,    0,    0, -115,    0,
    0,  -66, -174, -105,    0,    0,    0, -222,  -93, -212,
    0,    0, -104,  -98,  -96, -180,  -94, -212, -103,    0,
  -85,  -80,  -66,  -66,    0,    0,  -70,  -57,  -66,  -56,
    0,  -26,    0,    0, -212, -212, -212,    0,    0, -200,
    0,    0, -212,  -19,    0,    0, -212,    0,  -17,  -31,
 -212,  268,    0,    0,    0,    0,  -56,    0,    0, -212,
 -115, -212,  -37, -212,  268, -201,  -66,    0,    0,    0,
    0, -212,  -73,    0,    0, -161,    0,    0,  -38,   67,
   99,  -30,  268,    0,   62,    0,    0, -212,   -9,    0,
 -212, -212, -212, -212, -212, -212, -212, -212,  246, -103,
  -32,  250,  -18,  263,  -69,  268,    0,  268,  -69,  268,
    0,  -26,    0, -212,    0,    0,    0,  304,    0,  -40,
 -218, -203, -242,  317, -166, -278,  -40, -180, -212, -180,
 -212,    7,    0,   17,   18,    0,   99,    0,   -1,  291,
    0,  287,   -3, -212, -212,    0, -188,    0,   24,    9,
   28,   37,   -3,   80,   84,    0,    0, -259,   32,  -57,
    0,   22,   35,    0,    0,    0, -180,  -66,   57,    0,
    0,  -66,   58,    0,
};
final static short yyrindex[] = {                         0,
    0,  323,    0,    0,    0,    0,    0,  323,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   68,    0,    0,   68,    0,    0,    0,    0,   63,    0,
   75,    0,    0,    0,    0,   78,    0,    0,    0,    0,
    0,    0,   41,    0,    0,   87,    0,    0,   63,    0,
    0,   97,   95,    0,    0,    0,    0, -249,    0, -128,
    0,    0,    0,    0,    0,    0,    0,    0,  106,    0,
    0,    0, -262, -262,    0,    0,  112,  113, -262,    0,
  -62,    0,    0,    0,    0,  126,    0,    0,    0,    8,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  115,    0,    0,    0,    0,   49,    0,    0,    0,
    0,    0,    0,    0,  122,    0, -262,    0,    0,    0,
    0,    0,    0,    0,    0,   -7,    0,    0,   34,    0,
  135,    0, -196,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  128,    0, -175,    0, -168,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  124,
  163,  178,  202,  236,  226,  212,  139,    0,    0,    0,
    0,    0,  -33,    0,    0,    0,  143,    0, -155,    0,
    0,    0,  161,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  161,    0,    0,    0,    0,  141,    0,    0,
    0,    0,  -20,    0,    0,    0,    0, -262,    0,    0,
    0,   97,    0,    0,
};
final static short yygindex[] = {                         0,
  424,    0,   52,    0,  253,  380,  417,   15,  393,  337,
    0,    0,  -67,    0,    0,  252,    0,  119,    0,    0,
    0,  -45,  255,  -49,  -41,    1,    0,    0,  248,    0,
   50,    0,    0,    0,  -51,  -39,    0,    0,    0,  290,
    0,    0,    0,  201,    0,
};
final static int YYTABLESIZE=624;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         55,
   80,   37,   77,   15,   22,  120,  121,   85,   11,   16,
   78,  124,   81,   23,   86,   21,  141,  142,  143,  144,
  113,   80,   80,   77,   77,   87,   37,   80,  148,   77,
  127,   78,   78,   81,   81,   31,   69,   78,   31,   81,
   17,   35,   19,   38,   85,   90,   91,   92,   24,  157,
   35,   86,  141,  142,  143,   53,   90,   91,   92,   33,
   35,   93,   87,   38,  148,   80,   85,   77,   94,   95,
   96,   88,   93,   86,   25,   78,  141,   81,   57,  155,
   95,   96,   25,   97,   63,   47,   98,   99,  148,  100,
   57,  141,  142,  134,   97,   56,  101,   98,   99,   58,
  100,   72,   72,  148,   40,   85,   56,  101,   72,   41,
  127,   58,   86,   72,   72,   72,   72,   72,   56,  115,
   10,   72,   72,   72,   72,  150,   10,   43,  141,  142,
  143,  144,  189,   72,  191,   44,  130,  131,  133,  147,
  148,   12,   13,   45,  135,   72,   46,   80,  137,   77,
  221,  207,  140,   48,  223,  184,   50,  210,   49,  185,
   54,  149,   84,  152,   89,  154,   80,  156,   77,  110,
   80,  220,   77,  158,  160,  111,   78,  112,   81,  114,
   78,  116,   81,  118,   90,   91,   92,   12,  183,  168,
   12,   58,  170,  171,  172,  173,  174,  175,  176,  177,
   93,  117,   59,   60,   61,   62,   63,  159,   95,   96,
   64,   65,   66,   67,  122,  187,    3,    4,   27,   29,
    5,    6,   97,   70,   53,   98,   99,  123,  100,  125,
  190,  126,  192,   11,   68,  101,   79,   79,  136,  138,
  153,   95,  139,   79,  166,  204,  205,  162,   79,   79,
   79,   79,   79,   95,  179,  181,   79,   79,   79,   79,
   69,   95,   95,   95,   95,  169,  148,   69,   79,  193,
   95,   95,   95,   95,  197,   69,   69,   69,   69,   69,
   79,  208,   69,  194,  195,  201,  212,   69,   69,   69,
   69,   69,   69,   69,   69,  211,   69,   69,   69,   69,
  213,   63,   69,   69,   69,   69,  217,   69,   63,  218,
  219,   69,   69,   69,   69,   33,   98,   63,   63,  222,
   63,  224,    3,   98,   33,   33,   31,   33,   63,   63,
   63,   63,   98,   63,  163,   98,  167,   63,   63,   63,
   63,   27,   16,   98,   98,   98,   98,  215,   98,   17,
   26,  216,   98,   98,   98,   98,  141,  142,  143,  144,
   37,  141,  142,  143,  144,  145,  146,  147,  148,   20,
  145,  146,  147,  148,  141,  142,  143,  144,  141,  142,
  143,  144,  164,  145,  146,  147,  148,  145,  146,  147,
  148,   84,   44,  141,  142,  143,  144,   68,   84,   54,
  108,   48,  145,  146,  147,  148,   85,   84,   42,  109,
   84,   12,   13,   85,   36,    3,    4,  112,   84,   84,
   84,   84,   85,   84,   80,   85,   69,   84,   84,   84,
   83,   20,   83,   85,   85,   85,   85,   83,   85,  196,
   34,   57,   85,   85,   85,   87,   83,  151,  206,   83,
  214,  186,   87,  209,    0,    0,    0,    0,   83,   83,
   83,   87,   83,    0,   87,    0,   83,   83,   83,   86,
    0,    0,    0,    0,   87,   87,   86,   87,    0,   90,
    0,   87,   87,   87,    0,   86,   90,    0,   86,    0,
    0,    0,    0,   88,    0,   90,    0,    0,   90,   86,
   88,   86,    0,   89,    0,   86,   86,   86,    0,   88,
   89,   90,   88,    0,    0,   90,   90,   90,    0,   89,
  178,    0,   89,    0,  180,   88,    0,    0,    0,   88,
   88,    0,    0,    0,    0,   89,    0,  182,    0,   89,
  141,  142,  143,  144,  141,  142,  143,  144,    0,  145,
  146,  147,  148,  145,  146,  147,  148,  141,  142,  143,
  144,  200,  141,  142,  143,  144,  145,  146,  147,  148,
    0,  145,  146,  147,  148,    0,    0,  199,    0,    0,
    0,  141,  142,  143,  144,  141,  142,  143,  144,    0,
  145,  146,  147,  148,  145,  146,  147,  148,  141,  142,
  143,  144,    0,  188,    0,    0,    0,  145,  146,  147,
  148,  141,  142,  143,  144,    0,    0,    0,    0,    0,
    0,  146,  147,  148,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         45,
   52,  264,   52,    3,  258,   73,   74,  267,  258,  258,
   52,   79,   52,  267,  274,  274,  295,  296,  297,  298,
   66,   73,   74,   73,   74,  285,  289,   79,  307,   79,
   82,   73,   74,   73,   74,   21,  286,   79,   24,   79,
  258,   27,  258,   29,  267,  258,  259,  260,  274,  117,
   36,  274,  295,  296,  297,   41,  258,  259,  260,  268,
   46,  274,  285,   49,  307,  117,  267,  117,  281,  282,
  283,  294,  274,  274,  263,  117,  295,  117,  275,  281,
  282,  283,  263,  296,  273,   36,  299,  300,  307,  302,
  287,  295,  296,  294,  296,   46,  309,  299,  300,  275,
  302,  257,  258,  307,  275,  267,  275,  309,  264,  284,
  162,  287,  274,  269,  270,  271,  272,  273,  287,   68,
    2,  277,  278,  279,  280,  111,    8,  258,  295,  296,
  297,  298,  178,  289,  180,  258,   85,   86,   87,  306,
  307,  257,  258,  275,   93,  301,  287,  199,   97,  199,
  218,  197,  101,  264,  222,  155,  264,  199,  287,  159,
  267,  110,  268,  112,  258,  114,  218,  116,  218,  274,
  222,  217,  222,  122,  123,  274,  218,  274,  218,  274,
  222,  285,  222,  264,  258,  259,  260,  257,  258,  138,
  257,  258,  141,  142,  143,  144,  145,  146,  147,  148,
  274,  287,  269,  270,  271,  272,  273,  281,  282,  283,
  277,  278,  279,  280,  285,  164,  261,  262,   18,   19,
  265,  266,  296,  286,  287,  299,  300,  285,  302,  286,
  179,  258,  181,  267,  301,  309,  257,  258,  258,  257,
  278,  275,  274,  264,  275,  194,  195,  286,  269,  270,
  271,  272,  273,  287,  287,  274,  277,  278,  279,  280,
  268,  295,  296,  297,  298,  275,  307,  275,  289,  263,
  304,  305,  306,  307,  276,  268,  284,  285,  286,  287,
  301,  258,  275,  267,  267,  289,  259,  295,  296,  297,
  298,  284,  300,  286,  287,  287,  304,  305,  306,  307,
  264,  268,  295,  296,  297,  298,  275,  300,  275,  288,
  276,  304,  305,  306,  307,  275,  268,  284,  285,  263,
  287,  264,    0,  275,  284,  285,  264,  287,  295,  296,
  297,  298,  284,  300,  268,  287,  275,  304,  305,  306,
  307,  264,  275,  295,  296,  297,  298,  268,  300,  275,
  264,  268,  304,  305,  306,  307,  295,  296,  297,  298,
  264,  295,  296,  297,  298,  304,  305,  306,  307,  275,
  304,  305,  306,  307,  295,  296,  297,  298,  295,  296,
  297,  298,  284,  304,  305,  306,  307,  304,  305,  306,
  307,  268,  287,  295,  296,  297,  298,  286,  275,  287,
  275,  287,  304,  305,  306,  307,  268,  284,  287,  275,
  287,  257,  258,  275,  287,  261,  262,  275,  295,  296,
  297,  298,  284,  300,  264,  287,  286,  304,  305,  306,
  268,    8,   53,  295,  296,  297,  298,  275,  300,  187,
   24,   49,  304,  305,  306,  268,  284,  111,  197,  287,
  203,  162,  275,  199,   -1,   -1,   -1,   -1,  296,  297,
  298,  284,  300,   -1,  287,   -1,  304,  305,  306,  268,
   -1,   -1,   -1,   -1,  297,  298,  275,  300,   -1,  268,
   -1,  304,  305,  306,   -1,  284,  275,   -1,  287,   -1,
   -1,   -1,   -1,  268,   -1,  284,   -1,   -1,  287,  298,
  275,  300,   -1,  268,   -1,  304,  305,  306,   -1,  284,
  275,  300,  287,   -1,   -1,  304,  305,  306,   -1,  284,
  275,   -1,  287,   -1,  275,  300,   -1,   -1,   -1,  304,
  305,   -1,   -1,   -1,   -1,  300,   -1,  275,   -1,  304,
  295,  296,  297,  298,  295,  296,  297,  298,   -1,  304,
  305,  306,  307,  304,  305,  306,  307,  295,  296,  297,
  298,  275,  295,  296,  297,  298,  304,  305,  306,  307,
   -1,  304,  305,  306,  307,   -1,   -1,  287,   -1,   -1,
   -1,  295,  296,  297,  298,  295,  296,  297,  298,   -1,
  304,  305,  306,  307,  304,  305,  306,  307,  295,  296,
  297,  298,   -1,  300,   -1,   -1,   -1,  304,  305,  306,
  307,  295,  296,  297,  298,   -1,   -1,   -1,   -1,   -1,
   -1,  305,  306,  307,
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
"func_header : type IDENTIFIER",
"func_header : type L_SQBRACE R_SQBRACE IDENTIFIER",
"function : DEFUN func_header L_BRACE param_list R_BRACE codeblock",
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
"class_block : openblock class_list R_CURBRACE",
"class_list : decl SEMICOLON",
"class_list : function",
"class_list : decl SEMICOLON class_list",
"class_list : function class_list",
"decl_block : openblock decl_list R_CURBRACE",
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

//#line 871 "./borzhch.y"

private SymTable topTable = null;
private static FuncTable funcTable = null;
private SymTable structTable = null;

private SymTable backup;

private HashMap<String, SymTable> context = new HashMap<>();

private String mainClass = "Program";
private String currentClass = "Program";
private String filename = "";

private static boolean parseError = false;

private Lexer lexer;

private void fullRestoreContext() {
    while (null != topTable.getPrevious()) {
        topTable = topTable.getPrevious();
    }
}

private void restoreContext() {
    topTable = topTable.getPrevious();
}

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
  if(!result) result = funcTable.find(identifier, currentClass);
  if(!result) result = structTable.findSymbol(identifier);

  return result;
}

private String getSymbolType(String identifier) {
    String result = topTable.getSymbolType(identifier);
    
    return result;
}

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
    String msg = String.format("Error in %s on line %d, column %d: %s", this.filename, lexer.Yyline(), lexer.Yycolumn(), error);
    throw new Error(msg);
}

public Parser(Reader r, boolean debug) {
  lexer = new Lexer(r, this);
  yydebug = debug;
}

public void newLexer(Reader r, String filename) {
    lexer = new Lexer(r, this);
    this.filename = filename;
}

public String getFilename() {
    return this.filename;
}
//#line 647 "Parser.java"
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
        if (null == topTable) {
            topTable = new SymTable(null);
            topTable.contextIdentifier = "TOP";
        }
        if (null == funcTable)
            funcTable = new FuncTable();
        if (null == structTable) {
            structTable = new SymTable(null);
            structTable.pushSymbol("Program", "class");
        }
    }
break;
case 3:
//#line 84 "./borzhch.y"
{ 
           yyval.obj = null; 
    }
break;
case 4:
//#line 87 "./borzhch.y"
{ 
        StatementList list = new StatementList(); 
        list.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) list.addAll((NodeList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 5:
//#line 95 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 6:
//#line 96 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 7:
//#line 97 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 8:
//#line 101 "./borzhch.y"
{
        topTable = new SymTable(topTable);
    }
break;
case 9:
//#line 106 "./borzhch.y"
{
            /*SymTable oldTable = topTable;
            topTable = oldTable.getPrevious();
            oldTable.setPrevious(null);
            oldTable.clear();*/
            restoreContext();
        }
break;
case 10:
//#line 116 "./borzhch.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 11:
//#line 117 "./borzhch.y"
{ 
        if (!isTypeExist(val_peek(0).sval)) {
            yyerror(String.format("can not resolve symbol <%s>\n", val_peek(0).sval));
        }
        yyval.sval = val_peek(0).sval; 
    }
break;
case 12:
//#line 126 "./borzhch.y"
{
        if (isIdentifierExist(val_peek(0).sval)) {
            yyerror(String.format("identifier <%s> is already defined", val_peek(0).sval));
        }
        FunctionNode node = new FunctionNode(val_peek(0).sval, val_peek(1).sval, currentClass);
        funcTable.push(node);
        topTable = new SymTable(topTable);
        yyval.obj = node;
    }
break;
case 13:
//#line 135 "./borzhch.y"
{
        if (isIdentifierExist(val_peek(0).sval)) {
            yyerror(String.format("identifier <%s> is already defined", val_peek(0).sval));
        }
        FunctionNode node = new FunctionNode(val_peek(0).sval, "$" + val_peek(3).sval, currentClass);
        funcTable.push(node);
        topTable = new SymTable(topTable);
        yyval.obj = node;
    }
break;
case 14:
//#line 147 "./borzhch.y"
{
        FunctionNode node = (FunctionNode) val_peek(4).obj;
        node.setArguments((NodeList) val_peek(2).obj);
        node.setStatements((StatementList) val_peek(0).obj);

        context.put(node.getFuncName(), topTable);
        /*restoreContext();*/
        restoreContext();
        yyval.obj = node;
    }
break;
case 15:
//#line 157 "./borzhch.y"
{
        if(isIdentifierExist(val_peek(4).sval)) {
          String msg = ErrorHelper.identifierExists(val_peek(4).sval);
          yyerror(msg);
        }
        FunctionNode func = new FunctionNode(val_peek(4).sval, BOType.VOID, currentClass);
        func.setArguments((StatementList) val_peek(2).obj);
        func.setStatements((StatementList) val_peek(0).obj);

        context.put(val_peek(4).sval, topTable);
        /*restoreContext();*/
        /*restoreContext();*/
        funcTable.push(func);
    
        yyval.obj = func;
    }
break;
case 16:
//#line 175 "./borzhch.y"
{ yyval.obj = new StatementList(); }
break;
case 17:
//#line 176 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 18:
//#line 181 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
        }
break;
case 19:
//#line 189 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((NodeList) val_peek(0).obj);
            yyval.obj = node; 
          }
break;
case 20:
//#line 195 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
          }
break;
case 21:
//#line 203 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 22:
//#line 207 "./borzhch.y"
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
case 23:
//#line 223 "./borzhch.y"
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
case 24:
//#line 240 "./borzhch.y"
{
        funcTable = new FuncTable(funcTable);
        currentClass = val_peek(0).sval; 
        yyval.sval = val_peek(0).sval;
    }
break;
case 25:
//#line 248 "./borzhch.y"
{
        FieldList node = (FieldList) val_peek(1).obj;
        currentClass = mainClass;
        yyval.obj = node; 
    }
break;
case 26:
//#line 255 "./borzhch.y"
{ 
        FieldList node = new FieldList();
        DeclarationNode decl = (DeclarationNode) val_peek(1).obj;
        decl.isField(true);
        node.add((NodeAST) decl);
        yyval.obj = node; 
    }
break;
case 27:
//#line 262 "./borzhch.y"
{
        FieldList node = new FieldList();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 28:
//#line 267 "./borzhch.y"
{
        FieldList node = new FieldList();
        DeclarationNode decl = (DeclarationNode) val_peek(2).obj;
        decl.isField(true);
        node.add((NodeAST) decl);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 29:
//#line 275 "./borzhch.y"
{
        FieldList node = new FieldList();
        node.add((NodeAST) val_peek(1).obj);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 30:
//#line 284 "./borzhch.y"
{
        yyval.obj = val_peek(1).obj;
    }
break;
case 31:
//#line 289 "./borzhch.y"
{ yyval.obj = null; }
break;
case 32:
//#line 290 "./borzhch.y"
{
        FieldList node = new FieldList();
        DeclarationNode ldecl = (DeclarationNode) val_peek(2).obj;
        ldecl.isField(true);
        node.add(ldecl);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 33:
//#line 301 "./borzhch.y"
{
        if (isIdentifierExist(val_peek(0).sval)) {
            yyerror(String.format("identifier <%s> is already defined\n", val_peek(0).sval));
        }
        topTable.pushSymbol(val_peek(0).sval, val_peek(1).sval);
        DeclarationNode node = new DeclarationNode(val_peek(0).sval, val_peek(1).sval);
        yyval.obj = node;
    }
break;
case 34:
//#line 309 "./borzhch.y"
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
case 35:
//#line 321 "./borzhch.y"
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
case 36:
//#line 334 "./borzhch.y"
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
case 37:
//#line 359 "./borzhch.y"
{ yyval.obj = null; }
break;
case 38:
//#line 360 "./borzhch.y"
{ 
        StatementList list = new StatementList();
        list.add((NodeAST) val_peek(2).obj);
        if (val_peek(0).obj != null) list.addAll((StatementList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 39:
//#line 366 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 40:
//#line 372 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 41:
//#line 378 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 42:
//#line 387 "./borzhch.y"
{
        yyval.obj = new PrintNode((NodeAST) val_peek(0).obj);
    }
break;
case 43:
//#line 393 "./borzhch.y"
{
        yyval.obj = new InputNode();
    }
break;
case 44:
//#line 398 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 45:
//#line 399 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 46:
//#line 400 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 47:
//#line 401 "./borzhch.y"
{ 
      if(!isIdentifierExist(val_peek(0).sval)) {
        String msg = ErrorHelper.notDeclared(val_peek(0).sval);
        yyerror(msg);
      }
      yyval.obj = null; 
    }
break;
case 48:
//#line 408 "./borzhch.y"
{ yyval.obj = new ReturnNode((NodeAST) val_peek(0).obj); }
break;
case 49:
//#line 409 "./borzhch.y"
{ yyval.obj = new ReturnNode(null); }
break;
case 50:
//#line 410 "./borzhch.y"
{ yyval.obj = new BreakNode(); }
break;
case 51:
//#line 411 "./borzhch.y"
{ yyval.obj = new ContinueNode(); }
break;
case 52:
//#line 412 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 53:
//#line 413 "./borzhch.y"
{ 
        FunctionCallNode node = (FunctionCallNode) val_peek(0).obj;
        if (!node.isProcedure()) node.popLast(true);
        yyval.obj = node;
    }
break;
case 54:
//#line 418 "./borzhch.y"
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
case 55:
//#line 430 "./borzhch.y"
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
case 56:
//#line 445 "./borzhch.y"
{
        GetFieldNode get = (GetFieldNode) val_peek(2).obj;
        
        NodeAST expr = (NodeAST) val_peek(0).obj;
        SetFieldNode node = new SetFieldNode(get, expr);
        yyval.obj = node;

        /*restoreContext();*/
    }
break;
case 57:
//#line 454 "./borzhch.y"
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
case 58:
//#line 473 "./borzhch.y"
{
        /*arrayref := IDENTIFIER L_SQBRACE exp R_SQBRACE => ArrayElementNode*/
        
        ArrayElementNode index = (ArrayElementNode) val_peek(2).obj;
        NodeAST value = (NodeAST) val_peek(0).obj;
        SetArrayNode node = new SetArrayNode(index, value);
        yyval.obj = node;
    }
break;
case 59:
//#line 481 "./borzhch.y"
{
        GetFieldNode get = (GetFieldNode) val_peek(6).obj;
        NewArrayNode nan = new NewArrayNode(val_peek(3).sval, (NodeAST) val_peek(1).obj);
        SetFieldNode node = new SetFieldNode(get, nan);
        yyval.obj = node;

        /*restoreContext();*/
    }
break;
case 60:
//#line 492 "./borzhch.y"
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
case 61:
//#line 506 "./borzhch.y"
{
        ArrayList<NodeAST> nodes = new ArrayList<>();
        nodes.add((NodeAST) val_peek(2).obj);
        nodes.addAll((ArrayList<NodeAST>) val_peek(1).obj);
        GetFieldNode node = new GetFieldNode(nodes);
        node.generateLast(true);
        yyval.obj = node;
    }
break;
case 62:
//#line 518 "./borzhch.y"
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
//#line 537 "./borzhch.y"
{ 
        ArrayList<NodeAST> node = new ArrayList<>();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 64:
//#line 542 "./borzhch.y"
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
//#line 554 "./borzhch.y"
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
//#line 570 "./borzhch.y"
{
        topTable = backup;
        currentClass = mainClass;
    }
break;
case 67:
//#line 578 "./borzhch.y"
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
//#line 599 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 69:
//#line 600 "./borzhch.y"
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
//#line 608 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 71:
//#line 612 "./borzhch.y"
{
        IfNode node = new IfNode((NodeAST) val_peek(3).obj, (StatementList) val_peek(1).obj, (NodeAST) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 72:
//#line 618 "./borzhch.y"
{
      yyval.obj = null;
    }
break;
case 73:
//#line 621 "./borzhch.y"
{
      yyval.obj = (NodeAST) val_peek(0).obj;
    }
break;
case 74:
//#line 624 "./borzhch.y"
{
      yyval.obj = val_peek(0).obj;
    }
break;
case 75:
//#line 629 "./borzhch.y"
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
//#line 637 "./borzhch.y"
{
        WhileNode node = new WhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 77:
//#line 641 "./borzhch.y"
{
        DoWhileNode node = new DoWhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(5).obj);
        yyval.obj = node;
    }
break;
case 78:
//#line 647 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(8).obj, (StatementList) val_peek(5).obj, (StatementList) val_peek(1).obj);
        yyval.obj = node;
      }
break;
case 79:
//#line 652 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(4).obj, (StatementList) val_peek(1).obj, null);
        yyval.obj = node;
      }
break;
case 80:
//#line 659 "./borzhch.y"
{ yyval.obj = null; }
break;
case 81:
//#line 660 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((StatementList) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 82:
//#line 667 "./borzhch.y"
{
        CaseNode node = new CaseNode(val_peek(2).ival, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 83:
//#line 674 "./borzhch.y"
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
//#line 685 "./borzhch.y"
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
//#line 696 "./borzhch.y"
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
//#line 710 "./borzhch.y"
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
//#line 721 "./borzhch.y"
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
//#line 735 "./borzhch.y"
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
//#line 746 "./borzhch.y"
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
//#line 757 "./borzhch.y"
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
//#line 768 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 92:
//#line 769 "./borzhch.y"
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
//#line 778 "./borzhch.y"
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
//#line 787 "./borzhch.y"
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
//#line 799 "./borzhch.y"
{ 
        if(!isTypeExist(val_peek(0).sval)) {
            String msg = ErrorHelper.unknownType(val_peek(0).sval);
            yyerror(msg);
        }
        yyval.obj = new NewObjectNode(val_peek(0).sval); 
    }
break;
case 96:
//#line 806 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 97:
//#line 807 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 98:
//#line 808 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 99:
//#line 809 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 100:
//#line 810 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 101:
//#line 814 "./borzhch.y"
{
        BOType type = BOHelper.getType(val_peek(2).sval);
        NodeAST exp = (NodeAST) val_peek(1).obj;
        CastNode node = new CastNode(type, exp);
        yyval.obj = node;
    }
break;
case 102:
//#line 823 "./borzhch.y"
{ yyval.obj = new IntegerNode(val_peek(0).ival); }
break;
case 103:
//#line 824 "./borzhch.y"
{ yyval.obj = new FloatNode((float)val_peek(0).dval); }
break;
case 104:
//#line 825 "./borzhch.y"
{ yyval.obj = new StringNode(val_peek(0).sval); }
break;
case 105:
//#line 826 "./borzhch.y"
{ yyval.obj = new BooleanNode(val_peek(0).ival); }
break;
case 106:
//#line 827 "./borzhch.y"
{ yyval.obj = new NullNode(); }
break;
case 107:
//#line 831 "./borzhch.y"
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
//#line 841 "./borzhch.y"
{
          yyval.obj = null;
        }
break;
case 109:
//#line 844 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 110:
//#line 849 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 111:
//#line 857 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 112:
//#line 863 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
//#line 1818 "Parser.java"
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
