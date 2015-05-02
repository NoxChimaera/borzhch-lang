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
public final static short PRINT=288;
public final static short COLON=289;
public final static short CASE=290;
public final static short TUPLE=291;
public final static short INCLUDE=292;
public final static short UN_MINUS=293;
public final static short UN_PLUS=294;
public final static short INCR=295;
public final static short MUL_ARITHM=296;
public final static short ADD_ARITHM=297;
public final static short MORELESS=298;
public final static short EQ=299;
public final static short NULL=300;
public final static short PIPE=301;
public final static short IFX=302;
public final static short OR=303;
public final static short AND=304;
public final static short XOR=305;
public final static short POW=306;
public final static short UN_ARITHM=307;
public final static short NOT=308;
public final static short tuple_value=309;
public final static short reference=310;
public final static short idref_tail=311;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,   41,    1,    1,    2,    2,    2,   42,   43,   24,
   24,   18,   18,   18,    7,    7,    7,    6,    6,   20,
   15,   30,   25,   31,   29,   29,   29,   29,   11,    9,
    9,    8,    8,   10,   10,   13,   13,   13,   13,   13,
   19,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   21,   21,   21,   21,   22,   23,   37,   38,   38,
   38,   40,   39,   36,   33,   33,   33,   16,   17,   17,
   17,   14,   14,   14,   26,   26,   27,   27,   28,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,   35,   32,   32,   32,
   32,   32,   34,    4,    4,    4,    5,    5,
};
final static short yylen[] = {                            2,
    2,    0,    0,    2,    1,    1,    1,    1,    1,    1,
    1,    7,    9,    6,    0,    1,    2,    3,    2,    3,
    3,    3,    1,    3,    2,    1,    3,    2,    3,    0,
    3,    2,    4,    7,    3,    0,    3,    2,    2,    2,
    2,    1,    1,    1,    2,    2,    1,    1,    1,    1,
    1,    3,    3,    3,    7,    4,    3,    2,    0,    1,
    3,    1,    0,    2,    1,    1,    1,    6,    0,    2,
    2,    9,    5,    7,   11,    7,    0,    2,    4,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    2,    4,
    2,    2,    1,    1,    1,    1,    4,    1,    1,    1,
    1,    1,    4,    0,    1,    2,    3,    2,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    0,    1,    0,    6,    5,
    7,   10,   11,    0,    0,   23,    0,    0,    4,    0,
    0,    0,    8,   22,    0,   21,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   17,    0,    0,   28,    9,   24,    0,   29,    0,
    0,   14,    0,    0,    0,   27,   31,   12,    0,    0,
    0,    0,   48,   49,    0,    0,    0,    0,    0,    0,
    0,   43,    0,    0,    0,    0,   50,   44,    0,    0,
    0,    0,    0,    0,   18,   33,    0,    0,    0,    0,
   45,    0,   98,   99,    0,    0,  100,  101,    0,  102,
    0,    0,    0,   65,   96,   94,    0,   67,   93,    0,
    0,    0,    0,    0,    0,    0,    0,   20,   39,   38,
    0,    0,   40,   58,    0,   62,   63,    0,   13,    0,
    0,    0,    0,   91,    0,   92,    0,    0,   89,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   37,    0,    0,    0,   57,
    0,   56,    0,  106,  103,   88,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   61,    0,   90,   97,    0,    0,
   73,    0,    0,    0,    0,  107,    0,   68,    0,    0,
    0,    0,    0,    0,    0,   70,   71,    0,    0,   74,
    0,    0,   78,   34,   55,    0,    0,    0,   72,   79,
    0,    0,   75,
};
final static short yydgoto[] = {                          1,
    7,    8,  103,  132,  164,   42,   30,   71,   37,   72,
   26,   73,   74,   75,    9,   76,  198,   34,   77,   52,
   78,  104,  105,   32,   17,   81,  202,  203,   35,   11,
   24,  106,  107,  108,  109,    0,   84,  127,  160,  128,
    2,   53,   47,
};
final static short yysindex[] = {                         0,
    0,  -76, -238, -237, -222, -217,    0,  -76,    0,    0,
    0,    0,    0, -172, -207,    0, -211, -211,    0, -202,
 -174, -238,    0,    0,  -64,    0, -238, -238, -138, -144,
 -134, -106, -133,  -64, -102, -122, -102, -108, -105, -211,
 -238,    0,  -94,  -64,    0,    0,    0, -238,    0, -211,
 -238,    0,  380, -134,  -93,    0,    0,    0,  -96, -214,
  -74, -204,    0,    0,  -87,  -83,  -78, -211,  -75, -204,
  -85,    0,  -86, -102,  380,  380,    0,    0,  -81,  -79,
  380,  -84,    0,  -50,    0,    0, -211, -204, -204, -204,
    0, -258,    0,    0, -204,  -47,    0,    0, -204,    0,
  -45, -204,  475,    0,    0,    0,  -84,    0,    0, -204,
 -238, -204,  -65, -204,  475, -176,  380,    0,    0,    0,
 -204, -171,    0,    0, -123,    0,    0,  -70,    0,  265,
  427,  -57,  475,    0,  366,    0,  451, -204,    0, -204,
 -204, -204, -204, -204, -204, -204, -204,  379,  -85,  -67,
  391,  -55,  404, -166,  475,    0,  475, -166,  475,    0,
  -50,    0, -204,    0,    0,    0, -204,  464,  -82, -233,
 -216, -179, -163, -183, -197,  -82, -211, -204, -211, -204,
  -42,    0,  -44,  -40,    0,  427,    0,    0,  -54,  440,
    0,  416,  -62, -204, -204,    0, -188,    0,  -33,  -46,
  -29,  -32,  -62,  277,  288,    0,    0, -214,  -28,    0,
  -38,  -34,    0,    0,    0, -211,  380,  -19,    0,    0,
  380,  -16,    0,
};
final static short yyrindex[] = {                         0,
    0,  249,    0,    0,    0,    0,    0,  249,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -23,    0,    0,    0,    0,  -10,  -23,    0,    0,
  -20,    0,    0,   -7,    0,    0,    0,    0,    0,    0,
    0,    0, -270,   -6,    0,    0,    0,  -10,    0,    0,
  -23,    0,   -5,  -14,    0,    0,    0,    0,    0, -240,
    0,  -21,    0,    0,    0,    0,    0,    0,    0,    0,
  -18,    0,    0,    0, -263, -263,    0,    0,    3,    0,
 -263,    0, -126,   10,    0,    0,    0,    0,   -8,    0,
    0,   35,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -17,    0,    0,    0,   85,    0,    0,    0,
    0,    0,    0,    0,  -15,    0, -263,    0,    0,    0,
    0,    0,    0,    0,  -22,    0,    0,   60,    0,    0,
    5,    0, -228,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    4,    0, -225,    0, -218,    0,
   10,    0,    0,    0,    0,    0,    0,    0,  110,  160,
  185,  198,  -58,  236,  223,  135,    0,    0,    0,    0,
    0,  254,    0,    0,    0,   15,    0,    0,  331,    0,
    0,    0,   28,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   28,    0,    0,    0,    0,    7,    0,    0,
    0,  356,    0,    0,    0,    0, -263,    0,    0,    0,
   -5,    0,    0,
};
final static short yygindex[] = {                         0,
  260,    0,   93,    0,  112,  242,  -25,   17,  251,  189,
    0,    0,  -68,    0,    0,  104,    0,  137,    0,  -39,
  103,  -51,  -43,    1,    0,    0,  101,    0,  113,    0,
    0,    0,  -53,  -41,    0,    0,    0,  144,    0,    0,
    0,  154,  -31,
};
final static int YYTABLESIZE=781;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         82,
   36,   79,   38,   14,   32,   49,  119,  120,   88,   80,
   58,   83,  123,   32,   32,   89,   32,   11,   12,   13,
   15,   82,   82,   79,   79,   59,   36,   82,  113,   79,
  126,   80,   80,   83,   83,   16,  134,   80,   31,   83,
   18,   33,  118,   36,   31,   66,   53,  129,  156,   54,
   33,   23,   88,   92,   93,   94,   52,   54,   53,   89,
   33,   54,  140,   82,   36,   79,   22,   31,   52,   95,
   90,   28,  147,   80,   23,   83,   96,   97,   98,  140,
  141,   92,   93,   94,   65,   20,   92,   93,   94,  147,
   12,  182,   99,   29,   21,  100,  101,   95,  140,  141,
  142,  143,   95,  102,  154,   97,   98,  126,  147,  158,
   97,   98,  140,  141,  142,  143,  140,  141,  142,   39,
   99,  146,  147,  100,  101,   99,  147,  149,  100,  101,
   40,  102,  140,  141,  142,  143,  102,  189,   10,  191,
  145,  146,  147,   88,   10,   82,   45,   79,  220,   41,
   89,   43,  222,   44,  183,   80,   56,  207,  184,   67,
   51,   46,  115,   82,   48,   79,   50,   82,   51,   79,
   25,   27,   55,   80,   86,   83,  219,   80,   87,   83,
  130,  131,  133,   91,    3,    4,  110,  135,    5,    6,
  111,  137,   12,   13,  139,  112,    3,    4,  114,  116,
  117,  124,  148,  121,  151,  122,  153,  125,  155,   86,
  136,  138,  152,  157,  159,  161,   86,  165,  180,  178,
  193,  197,  194,  147,  208,   86,  195,  201,   86,  211,
  168,  212,  169,  170,  171,  172,  173,  174,  175,  176,
  210,  218,   86,  221,   86,   66,  216,  223,    3,   86,
  217,   15,   66,   30,   16,  186,   26,   25,   36,  187,
   19,   66,   66,   66,   66,   47,  104,   19,   42,   46,
  190,   41,  192,   66,   66,   66,   66,   59,   66,  105,
   66,   66,   66,   66,   59,   66,  204,  205,   65,  108,
   35,   77,   66,   59,   59,   85,   59,  196,   57,  150,
  206,  209,   66,  213,  185,   59,   59,   59,   59,   66,
   59,    0,   59,   59,   59,   59,    0,   59,   66,    0,
   66,   66,    0,    0,    0,    0,    0,   60,    0,    0,
   66,   66,   66,   66,   60,   66,    0,   66,   66,   66,
   66,    0,   66,   60,   60,    0,   60,    0,    0,    0,
    0,    0,   95,    0,    0,   60,   60,   60,   60,   95,
   60,    0,   60,   60,   60,   60,    0,   60,   95,    0,
    0,   95,    0,    0,    0,    0,    0,   81,    0,    0,
   95,   95,   95,   95,   81,   95,    0,   95,   95,   95,
   95,    0,   95,   81,    0,    0,   81,    0,    0,    0,
    0,    0,   82,    0,    0,   81,   81,   81,   81,   82,
   81,    0,   81,   81,   81,    0,    0,   81,   82,    0,
    0,   82,    0,    0,    0,    0,    0,   80,    0,    0,
   82,   82,   82,   82,   80,   82,    0,   82,   82,   82,
    0,    0,   82,   80,    0,    0,   80,    0,    0,    0,
    0,    0,   84,    0,    0,    0,   80,   80,   80,   84,
   80,    0,   80,   80,   80,   83,    0,   80,   84,    0,
    0,   84,   83,    0,    0,    0,    0,    0,    0,    0,
    0,   83,   84,   84,   83,   84,    0,   84,   84,   84,
   87,    0,   84,    0,    0,    0,   83,   87,   83,    0,
   83,   83,   83,   85,    0,   83,   87,    0,    0,   87,
   85,    0,    0,    0,    0,    0,    0,    0,    0,   85,
   11,    0,   85,   87,    0,   87,   87,   87,   92,    0,
   87,    0,  162,    0,    0,    0,   85,    0,   85,   85,
   92,    0,    0,   85,  214,    0,    0,    0,    0,   92,
   92,   92,   92,    0,    0,  215,   92,   92,   92,   92,
  140,  141,  142,  143,    0,    0,    0,  144,  145,  146,
  147,    0,  140,  141,  142,  143,    0,    0,    0,  144,
  145,  146,  147,  140,  141,  142,  143,   69,   69,    0,
  144,  145,  146,  147,   69,    0,    0,    0,    0,   69,
   69,   69,   69,   69,    0,    0,    0,   69,   69,   69,
   69,    0,   76,   76,    0,    0,    0,    0,   69,   76,
   69,    0,    0,    0,   76,   76,   76,   76,   76,    0,
    0,    0,   76,   76,   76,   76,   12,   60,    0,    0,
  166,    0,    0,   76,    0,   76,    0,    0,   61,   62,
   63,   64,   65,  177,    0,    0,   66,   67,   68,   69,
    0,  140,  141,  142,  143,  179,    0,   70,  144,  145,
  146,  147,    0,    0,  140,  141,  142,  143,  181,    0,
    0,  144,  145,  146,  147,    0,  140,  141,  142,  143,
  200,    0,    0,  144,  145,  146,  147,    0,    0,  140,
  141,  142,  143,    0,    0,    0,  144,  145,  146,  147,
  163,  140,  141,  142,  143,    0,    0,    0,  144,  145,
  146,  147,  140,  141,  142,  143,  199,    0,    0,  144,
  145,  146,  147,    0,    0,  140,  141,  142,  143,    0,
    0,    0,  144,  145,  146,  147,  140,  141,  142,  143,
    0,    0,    0,  144,  145,  146,  147,    0,  167,  140,
  141,  142,  143,    0,  188,    0,  144,  145,  146,  147,
  140,  141,  142,  143,    0,    0,    0,  144,  145,  146,
  147,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         53,
  264,   53,   28,    3,  275,   37,   75,   76,  267,   53,
   50,   53,   81,  284,  285,  274,  287,  258,  257,  258,
  258,   75,   76,   75,   76,   51,  290,   81,   68,   81,
   84,   75,   76,   75,   76,  258,  295,   81,   22,   81,
  258,   25,   74,   27,   28,  286,  275,   87,  117,  275,
   34,  263,  267,  258,  259,  260,  275,   41,  287,  274,
   44,  287,  296,  117,   48,  117,  274,   51,  287,  274,
  285,  274,  306,  117,  263,  117,  281,  282,  283,  296,
  297,  258,  259,  260,  273,  258,  258,  259,  260,  306,
  257,  258,  297,  268,  267,  300,  301,  274,  296,  297,
  298,  299,  274,  308,  281,  282,  283,  161,  306,  281,
  282,  283,  296,  297,  298,  299,  296,  297,  298,  258,
  297,  305,  306,  300,  301,  297,  306,  111,  300,  301,
  275,  308,  296,  297,  298,  299,  308,  177,    2,  179,
  304,  305,  306,  267,    8,  199,   34,  199,  217,  284,
  274,  258,  221,  287,  154,  199,   44,  197,  158,  286,
  287,  264,   70,  217,  287,  217,  275,  221,  274,  221,
   17,   18,  267,  217,  268,  217,  216,  221,  275,  221,
   88,   89,   90,  258,  261,  262,  274,   95,  265,  266,
  274,   99,  257,  258,  102,  274,  261,  262,  274,  285,
  287,  286,  110,  285,  112,  285,  114,  258,  116,  268,
  258,  257,  278,  121,  122,  286,  275,  275,  274,  287,
  263,  276,  267,  306,  258,  284,  267,  290,  287,  259,
  138,  264,  140,  141,  142,  143,  144,  145,  146,  147,
  287,  276,  301,  263,  303,  268,  275,  264,    0,  308,
  289,  275,  275,  264,  275,  163,  264,  264,  264,  167,
  275,  284,  285,  286,  287,  287,  275,    8,  287,  287,
  178,  287,  180,  296,  297,  298,  299,  268,  301,  275,
  303,  304,  305,  306,  275,  308,  194,  195,  286,  275,
  287,  264,  286,  284,  285,   54,  287,  186,   48,  111,
  197,  199,  268,  203,  161,  296,  297,  298,  299,  275,
  301,   -1,  303,  304,  305,  306,   -1,  308,  284,   -1,
  286,  287,   -1,   -1,   -1,   -1,   -1,  268,   -1,   -1,
  296,  297,  298,  299,  275,  301,   -1,  303,  304,  305,
  306,   -1,  308,  284,  285,   -1,  287,   -1,   -1,   -1,
   -1,   -1,  268,   -1,   -1,  296,  297,  298,  299,  275,
  301,   -1,  303,  304,  305,  306,   -1,  308,  284,   -1,
   -1,  287,   -1,   -1,   -1,   -1,   -1,  268,   -1,   -1,
  296,  297,  298,  299,  275,  301,   -1,  303,  304,  305,
  306,   -1,  308,  284,   -1,   -1,  287,   -1,   -1,   -1,
   -1,   -1,  268,   -1,   -1,  296,  297,  298,  299,  275,
  301,   -1,  303,  304,  305,   -1,   -1,  308,  284,   -1,
   -1,  287,   -1,   -1,   -1,   -1,   -1,  268,   -1,   -1,
  296,  297,  298,  299,  275,  301,   -1,  303,  304,  305,
   -1,   -1,  308,  284,   -1,   -1,  287,   -1,   -1,   -1,
   -1,   -1,  268,   -1,   -1,   -1,  297,  298,  299,  275,
  301,   -1,  303,  304,  305,  268,   -1,  308,  284,   -1,
   -1,  287,  275,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  284,  298,  299,  287,  301,   -1,  303,  304,  305,
  268,   -1,  308,   -1,   -1,   -1,  299,  275,  301,   -1,
  303,  304,  305,  268,   -1,  308,  284,   -1,   -1,  287,
  275,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  284,
  267,   -1,  287,  301,   -1,  303,  304,  305,  275,   -1,
  308,   -1,  268,   -1,   -1,   -1,  301,   -1,  303,  304,
  287,   -1,   -1,  308,  268,   -1,   -1,   -1,   -1,  296,
  297,  298,  299,   -1,   -1,  268,  303,  304,  305,  306,
  296,  297,  298,  299,   -1,   -1,   -1,  303,  304,  305,
  306,   -1,  296,  297,  298,  299,   -1,   -1,   -1,  303,
  304,  305,  306,  296,  297,  298,  299,  257,  258,   -1,
  303,  304,  305,  306,  264,   -1,   -1,   -1,   -1,  269,
  270,  271,  272,  273,   -1,   -1,   -1,  277,  278,  279,
  280,   -1,  257,  258,   -1,   -1,   -1,   -1,  288,  264,
  290,   -1,   -1,   -1,  269,  270,  271,  272,  273,   -1,
   -1,   -1,  277,  278,  279,  280,  257,  258,   -1,   -1,
  275,   -1,   -1,  288,   -1,  290,   -1,   -1,  269,  270,
  271,  272,  273,  275,   -1,   -1,  277,  278,  279,  280,
   -1,  296,  297,  298,  299,  275,   -1,  288,  303,  304,
  305,  306,   -1,   -1,  296,  297,  298,  299,  275,   -1,
   -1,  303,  304,  305,  306,   -1,  296,  297,  298,  299,
  275,   -1,   -1,  303,  304,  305,  306,   -1,   -1,  296,
  297,  298,  299,   -1,   -1,   -1,  303,  304,  305,  306,
  284,  296,  297,  298,  299,   -1,   -1,   -1,  303,  304,
  305,  306,  296,  297,  298,  299,  287,   -1,   -1,  303,
  304,  305,  306,   -1,   -1,  296,  297,  298,  299,   -1,
   -1,   -1,  303,  304,  305,  306,  296,  297,  298,  299,
   -1,   -1,   -1,  303,  304,  305,  306,   -1,  308,  296,
  297,  298,  299,   -1,  301,   -1,  303,  304,  305,  306,
  296,  297,  298,  299,   -1,   -1,   -1,  303,  304,  305,
  306,
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
"L_CURBRACE","R_CURBRACE","CLASS","STRUCT","L_SQBRACE","R_SQBRACE","GOTO",
"RETURN","BREAK","CONTINUE","IF","L_BRACE","R_BRACE","ELSE","FOR","WHILE","DO",
"SWITCH","NEW","STRING","BOOLEAN","COMMA","ASSIGN","DOT","SEMICOLON","PRINT",
"COLON","CASE","TUPLE","INCLUDE","UN_MINUS","UN_PLUS","INCR","MUL_ARITHM",
"ADD_ARITHM","MORELESS","EQ","NULL","PIPE","IFX","OR","AND","XOR","POW",
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
"exp : ADD_ARITHM exp NOT exp",
"exp : IDENTIFIER INCR",
"exp : NEW IDENTIFIER",
"exp : cast",
"exp : constant",
"exp : dynamic_value",
"exp : idref",
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

//#line 823 "./borzhch.y"

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
//#line 661 "Parser.java"
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
//#line 64 "./borzhch.y"
{ 
        TreeAST.setRoot((NodeAST) val_peek(0).obj); 
     }
break;
case 2:
//#line 69 "./borzhch.y"
{
        topTable = new SymTable(null);
        
        funcTable = new FuncTable();

        structTable = new SymTable(null);

        structTable.pushSymbol("Program", "class");
    }
break;
case 3:
//#line 80 "./borzhch.y"
{ 
           yyval.obj = null; 
    }
break;
case 4:
//#line 83 "./borzhch.y"
{ 
        StatementList list = new StatementList(); 
        list.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) list.addAll((NodeList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 5:
//#line 91 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 6:
//#line 92 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 7:
//#line 93 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 8:
//#line 97 "./borzhch.y"
{
        topTable = new SymTable(topTable);
    }
break;
case 9:
//#line 102 "./borzhch.y"
{
            /*SymTable oldTable = topTable;
            topTable = oldTable.getPrevious();
            oldTable.setPrevious(null);
            oldTable.clear();*/
        }
break;
case 10:
//#line 111 "./borzhch.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 11:
//#line 112 "./borzhch.y"
{ 
        if (!isTypeExist(val_peek(0).sval)) {
            yyerror(String.format("can not resolve symbol <%s>\n", val_peek(0).sval));
        }
        yyval.sval = val_peek(0).sval; 
    }
break;
case 12:
//#line 121 "./borzhch.y"
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
//#line 134 "./borzhch.y"
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
//#line 147 "./borzhch.y"
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
//#line 164 "./borzhch.y"
{ yyval.obj = null; }
break;
case 16:
//#line 165 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 17:
//#line 170 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
        }
break;
case 18:
//#line 178 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((NodeList) val_peek(0).obj);
            yyval.obj = node; 
          }
break;
case 19:
//#line 184 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
          }
break;
case 20:
//#line 192 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 21:
//#line 196 "./borzhch.y"
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
//#line 212 "./borzhch.y"
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
//#line 229 "./borzhch.y"
{
        funcTable = new FuncTable(funcTable);
        currentClass = val_peek(0).sval; 
        yyval.sval = val_peek(0).sval;
    }
break;
case 24:
//#line 237 "./borzhch.y"
{
        FieldList node = (FieldList) val_peek(1).obj;
        currentClass = mainClass;
        yyval.obj = node; 
    }
break;
case 25:
//#line 244 "./borzhch.y"
{ 
        FieldList node = new FieldList();
        DeclarationNode decl = (DeclarationNode) val_peek(1).obj;
        decl.isField(true);
        node.add((NodeAST) decl);
        yyval.obj = node; 
    }
break;
case 26:
//#line 251 "./borzhch.y"
{
        FieldList node = new FieldList();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 27:
//#line 256 "./borzhch.y"
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
//#line 264 "./borzhch.y"
{
        FieldList node = new FieldList();
        node.add((NodeAST) val_peek(1).obj);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 29:
//#line 273 "./borzhch.y"
{
        yyval.obj = val_peek(1).obj;
    }
break;
case 30:
//#line 278 "./borzhch.y"
{ yyval.obj = null; }
break;
case 31:
//#line 279 "./borzhch.y"
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
//#line 290 "./borzhch.y"
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
//#line 298 "./borzhch.y"
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
//#line 310 "./borzhch.y"
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
//#line 323 "./borzhch.y"
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
//#line 348 "./borzhch.y"
{ yyval.obj = null; }
break;
case 37:
//#line 349 "./borzhch.y"
{ 
        StatementList list = new StatementList();
        list.add((NodeAST) val_peek(2).obj);
        if (val_peek(0).obj != null) list.addAll((StatementList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 38:
//#line 355 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 39:
//#line 361 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 40:
//#line 367 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 41:
//#line 376 "./borzhch.y"
{
        yyval.obj = new PrintNode((NodeAST) val_peek(0).obj);
    }
break;
case 42:
//#line 381 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 43:
//#line 382 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 44:
//#line 383 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 45:
//#line 384 "./borzhch.y"
{ 
      if(!isIdentifierExist(val_peek(0).sval)) {
        String msg = ErrorHelper.notDeclared(val_peek(0).sval);
        yyerror(msg);
      }
      yyval.obj = null; 
    }
break;
case 46:
//#line 391 "./borzhch.y"
{ yyval.obj = new ReturnNode((NodeAST) val_peek(0).obj); }
break;
case 47:
//#line 392 "./borzhch.y"
{ yyval.obj = new ReturnNode(null); }
break;
case 48:
//#line 393 "./borzhch.y"
{ yyval.obj = new BreakNode(); }
break;
case 49:
//#line 394 "./borzhch.y"
{ yyval.obj = new ContinueNode(); }
break;
case 50:
//#line 395 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 51:
//#line 396 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 52:
//#line 400 "./borzhch.y"
{
        GetFieldNode get = (GetFieldNode) val_peek(2).obj;
        
        NodeAST expr = (NodeAST) val_peek(0).obj;
        SetFieldNode node = new SetFieldNode(get, expr);
        yyval.obj = node;

        /*restoreContext();*/
    }
break;
case 53:
//#line 409 "./borzhch.y"
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
case 54:
//#line 428 "./borzhch.y"
{
        /*arrayref := IDENTIFIER L_SQBRACE exp R_SQBRACE => ArrayElementNode*/
        
        ArrayElementNode index = (ArrayElementNode) val_peek(2).obj;
        NodeAST value = (NodeAST) val_peek(0).obj;
        SetArrayNode node = new SetArrayNode(index, value);
        yyval.obj = node;
    }
break;
case 55:
//#line 436 "./borzhch.y"
{
        GetFieldNode get = (GetFieldNode) val_peek(6).obj;
        NewArrayNode nan = new NewArrayNode(val_peek(3).sval, (NodeAST) val_peek(1).obj);
        SetFieldNode node = new SetFieldNode(get, nan);
        yyval.obj = node;

        /*restoreContext();*/
    }
break;
case 56:
//#line 447 "./borzhch.y"
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
case 57:
//#line 461 "./borzhch.y"
{
        ArrayList<NodeAST> nodes = new ArrayList<>();
        nodes.add((NodeAST) val_peek(2).obj);
        nodes.addAll((ArrayList<NodeAST>) val_peek(1).obj);
        GetFieldNode node = new GetFieldNode(nodes);
        node.generateLast(true);
        yyval.obj = node;
    }
break;
case 58:
//#line 473 "./borzhch.y"
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
case 60:
//#line 492 "./borzhch.y"
{ 
        ArrayList<NodeAST> node = new ArrayList<>();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 61:
//#line 497 "./borzhch.y"
{ 
        ArrayList<NodeAST> node = new ArrayList<>(); 
        node.add((NodeAST) val_peek(2).obj);
        if (null != val_peek(0).obj) {
            node.addAll((ArrayList<NodeAST>) val_peek(0).obj);
        }

        yyval.obj = node;
    }
break;
case 62:
//#line 509 "./borzhch.y"
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
case 63:
//#line 525 "./borzhch.y"
{
        topTable = backup;
        currentClass = mainClass;
    }
break;
case 64:
//#line 533 "./borzhch.y"
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
case 65:
//#line 554 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 66:
//#line 555 "./borzhch.y"
{ 
        if (null == topTable.getSymbolType(val_peek(0).sval)) {
            yyval.obj = new VariableNode(val_peek(0).sval, BOType.VOID);
            yyerror(String.format("identifier <%s> is not declared", val_peek(0).sval));
        } else {
            yyval.obj = new VariableNode(val_peek(0).sval, topTable.getSymbolType(val_peek(0).sval)); 
        }
    }
break;
case 67:
//#line 563 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 68:
//#line 567 "./borzhch.y"
{
        IfNode node = new IfNode((NodeAST) val_peek(3).obj, (StatementList) val_peek(1).obj, (IfNode) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 69:
//#line 573 "./borzhch.y"
{
      yyval.obj = null;
    }
break;
case 70:
//#line 576 "./borzhch.y"
{
      yyval.obj = (IfNode) val_peek(0).obj;
    }
break;
case 71:
//#line 579 "./borzhch.y"
{
      IfNode node = new IfNode(null, (StatementList) val_peek(0).obj, null);
      yyval.obj = node;
    }
break;
case 72:
//#line 585 "./borzhch.y"
{
        NodeAST decl = (NodeAST) val_peek(6).obj;
        NodeAST counter = (NodeAST) val_peek(4).obj;
        NodeAST step = (NodeAST) val_peek(2).obj;
        NodeAST statements = (NodeAST) val_peek(0).obj; 
        ForNode node = new ForNode(decl, counter, step, statements);
        yyval.obj = node;
    }
break;
case 73:
//#line 593 "./borzhch.y"
{
        WhileNode node = new WhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 74:
//#line 597 "./borzhch.y"
{
        DoWhileNode node = new DoWhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(5).obj);
        yyval.obj = node;
    }
break;
case 75:
//#line 603 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(8).obj, (StatementList) val_peek(5).obj, (StatementList) val_peek(1).obj);
        yyval.obj = node;
      }
break;
case 76:
//#line 608 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(4).obj, (StatementList) val_peek(1).obj, null);
        yyval.obj = node;
      }
break;
case 77:
//#line 615 "./borzhch.y"
{ yyval.obj = null; }
break;
case 78:
//#line 616 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((StatementList) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 79:
//#line 623 "./borzhch.y"
{
        CaseNode node = new CaseNode(val_peek(2).ival, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 80:
//#line 630 "./borzhch.y"
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
case 81:
//#line 641 "./borzhch.y"
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
case 82:
//#line 652 "./borzhch.y"
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
case 83:
//#line 666 "./borzhch.y"
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
case 84:
//#line 677 "./borzhch.y"
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
case 85:
//#line 691 "./borzhch.y"
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
case 86:
//#line 702 "./borzhch.y"
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
case 87:
//#line 713 "./borzhch.y"
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
case 88:
//#line 724 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 89:
//#line 725 "./borzhch.y"
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
case 90:
//#line 734 "./borzhch.y"
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
case 91:
//#line 743 "./borzhch.y"
{ 
      if(!isIdentifierExist(val_peek(1).sval)) {
        String msg = ErrorHelper.notDeclared(val_peek(1).sval);
        yyerror(msg);
      }
      yyval.obj = new PostOpNode(new VariableNode(val_peek(1).sval), val_peek(0).sval); 
        ((NodeAST) yyval.obj).type(BOType.INT);
        ((PostOpNode) yyval.obj).setPush(true);
    }
break;
case 92:
//#line 752 "./borzhch.y"
{ 
        if(!isTypeExist(val_peek(0).sval)) {
            String msg = ErrorHelper.unknownType(val_peek(0).sval);
            yyerror(msg);
        }
        yyval.obj = new NewObjectNode(val_peek(0).sval); 
    }
break;
case 93:
//#line 759 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 94:
//#line 760 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 95:
//#line 761 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 96:
//#line 762 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 97:
//#line 766 "./borzhch.y"
{
        BOType type = BOHelper.getType(val_peek(2).sval);
        NodeAST exp = (NodeAST) val_peek(1).obj;
        CastNode node = new CastNode(type, exp);
        yyval.obj = node;
    }
break;
case 98:
//#line 775 "./borzhch.y"
{ yyval.obj = new IntegerNode(val_peek(0).ival); }
break;
case 99:
//#line 776 "./borzhch.y"
{ yyval.obj = new FloatNode((float)val_peek(0).dval); }
break;
case 100:
//#line 777 "./borzhch.y"
{ yyval.obj = new StringNode(val_peek(0).sval); }
break;
case 101:
//#line 778 "./borzhch.y"
{ yyval.obj = new BooleanNode(val_peek(0).ival); }
break;
case 102:
//#line 779 "./borzhch.y"
{ yyval.obj = new NullNode(); }
break;
case 103:
//#line 783 "./borzhch.y"
{
        if(!isIdentifierExist(val_peek(3).sval)) {
          String msg = ErrorHelper.notDeclared(val_peek(3).sval);
          yyerror(msg);
        }
        FunctionCallNode node = new FunctionCallNode(val_peek(3).sval, (StatementList) val_peek(1).obj, currentClass);
        yyval.obj = node;
    }
break;
case 104:
//#line 793 "./borzhch.y"
{
          yyval.obj = null;
        }
break;
case 105:
//#line 796 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 106:
//#line 801 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 107:
//#line 809 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 108:
//#line 815 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
//#line 1776 "Parser.java"
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
