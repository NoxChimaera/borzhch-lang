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
  import java.util.Queue;
  import java.util.Stack;
  import java.util.LinkedList;
  import java.util.ArrayList;
  import edu.borzhch.ast.*;
  import edu.borzhch.helpers.*;
  import edu.borzhch.SymTable;
  import edu.borzhch.FuncTable;
  import edu.borzhch.StructTable;
  import edu.borzhch.constants.*;
//#line 30 "Parser.java"




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
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,   35,    1,    1,    2,    2,    2,   36,   37,   24,
   24,   18,   18,    7,    7,    7,    6,    6,   20,   15,
   29,   31,   30,   28,   28,   28,   28,   11,    9,    9,
    8,    8,   10,   10,   13,   13,   13,   13,   13,   19,
   12,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   21,   21,   21,   21,   22,   23,   23,   33,   33,   33,
   16,   17,   17,   17,   14,   14,   14,   25,   25,   26,
   26,   27,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,   32,   32,   32,
   32,   32,   32,   34,    4,    4,    4,    5,    5,
};
final static short yylen[] = {                            2,
    2,    0,    0,    2,    1,    1,    1,    1,    1,    1,
    1,    7,    6,    0,    1,    2,    3,    2,    3,    3,
    3,    1,    3,    2,    1,    3,    2,    3,    0,    3,
    2,    4,    7,    3,    0,    3,    2,    2,    2,    2,
    1,    1,    1,    2,    2,    1,    1,    1,    1,    1,
    3,    3,    3,    7,    4,    3,    3,    1,    1,    1,
    6,    0,    2,    2,    9,    5,    7,   11,    7,    0,
    2,    4,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    2,    4,    2,    2,    1,    1,    1,    1,    1,
    1,    1,    1,    4,    0,    1,    2,    3,    2,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    0,    1,    0,    6,    5,
    7,   10,   11,    0,    0,   22,    0,    0,    4,    0,
    0,    8,   21,    0,   20,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   16,    0,
    0,   27,    9,   23,    0,   28,    0,   13,    0,    0,
    0,   26,   30,   12,    0,    0,    0,   47,   48,    0,
    0,    0,    0,    0,    0,    0,   42,    0,    0,    0,
    0,   49,   43,    0,    0,    0,    0,    0,   17,   32,
    0,    0,    0,   44,    0,   88,   89,    0,    0,   90,
   91,    0,   92,    0,    0,   58,   93,   86,    0,   60,
    0,    0,    0,    0,    0,    0,    0,    0,   19,   38,
   37,    0,    0,   39,    0,    0,    0,    0,    0,   84,
    0,   85,    0,   82,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   36,    0,    0,    0,    0,   57,    0,   55,    0,   97,
   94,   81,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   83,    0,    0,   66,    0,    0,    0,    0,   98,    0,
   61,    0,    0,    0,    0,    0,    0,    0,   63,   64,
    0,    0,   67,    0,    0,   71,   33,   54,    0,    0,
    0,   65,   72,    0,    0,   68,
};
final static short yydgoto[] = {                          1,
    7,    8,   95,  118,  150,   39,   28,   66,   35,   67,
   25,   68,   69,   70,    9,   71,  181,   32,   72,   48,
   73,   96,   97,   30,   76,  185,  186,   33,   11,   23,
   17,   98,   99,  100,    2,   49,   44,
};
final static short yysindex[] = {                         0,
    0,   -1, -171, -251, -241, -233,    0,   -1,    0,    0,
    0,    0,    0, -230, -231,    0, -206, -206,    0, -209,
 -171,    0,    0,   41,    0, -171, -171, -200, -196, -159,
 -186,   41, -149, -167, -149, -141, -206, -171,    0, -131,
   41,    0,    0,    0, -171,    0, -206,    0,  253, -196,
 -128,    0,    0,    0, -222, -114,  -80,    0,    0, -125,
 -122, -120, -206, -110,  -80, -117,    0, -111, -149,  253,
  253,    0,    0, -115, -112,  253, -105,    0,    0,    0,
  -80,  -80,  -80,    0, -223,    0,    0,  -80,  -76,    0,
    0,  -80,    0,  -80,  335,    0,    0,    0, -105,    0,
  -80, -171,  -80,  -94,  -80,  335,  -67,  253,    0,    0,
    0,  -80,  -24,    0,  -73,  142,  300,  -88,  335,    0,
  240,    0,  323,    0,  -80,  -80,  -80,  -80,  -80,  -80,
  -80,  -80,  252, -117,  -92,  265,  -70,  277, -140,  335,
    0,  335, -140,  335, -207,    0, -105,    0,  -80,    0,
    0,    0,  -80,  -96, -215,  -59, -166,  192,  -86,  -99,
  -96, -206,  -80, -206,  -80,  -75,    0,  -46,  -45,  300,
    0,  -50,  313,    0,  290,  -61,  -80,  -80,    0, -262,
    0,  -27,  -44,  -18,  -22,  -61,  154,  167,    0,    0,
 -222,  -31,    0,  -40,  -23,    0,    0,    0, -206,  253,
  -11,    0,    0,  253,  -19,    0,
};
final static short yyrindex[] = {                         0,
    0,  254,    0,    0,    0,    0,    0,  254,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -20,    0,    0,    0,    0,   -2,  -20,    0,   -7,    0,
    0,    5,    0,    0,    0,    0,    0,    0,    0, -211,
    6,    0,    0,    0,   -2,    0,    0,    0,   14,    4,
    0,    0,    0,    0, -245,    0,  -10,    0,    0,    0,
    0,    0,    0,    0,    0, -186,    0,    0,    0, -248,
 -248,    0,    0,   -6,    0, -248,    0, -139,    0,    0,
    0,    7,    0,    0,  -12,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -167,    0,    0,    0,   13,    0,
    0,    0,    0,    0,    0,   17,    0, -248,    0,    0,
    0,    0,    0,    0,    0,    0,   19,    0, -272,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   20,
    0, -257,    0, -205, -191,    0, -249,    0,    0,    0,
    0,    0,    0,   38,   88,  109,  130,   21,  -36,  -79,
   63,    0,    0,    0,    0,    0,  122,    0,    0,   26,
    0,  204,    0,    0,    0,   50,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   50,    0,    0,    0,    0,
   33,    0,    0,    0,  229,    0,    0,    0,    0, -248,
    0,    0,    0,   14,    0,    0,
};
final static short yygindex[] = {                         0,
  316,    0,   -3,    0,  151,  276,  302,  145,  282,  228,
    0,    0,  -62,    0,    0,  152,    0,   95,    0,  -43,
  157,  -39,  -49,    2,    0,  147,    0,   51,    0,    0,
    0,    0,  -47,  -37,    0,  141,  -29,
};
final static int YYTABLESIZE=640;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         75,
   22,   77,   52,   54,   14,   46,   15,  110,  111,   74,
   60,   78,   11,  114,   52,   35,   16,   53,   56,  104,
   75,   75,   77,   77,   18,   56,   75,   20,   77,   53,
   74,   74,   78,   78,   56,   56,   74,   56,   78,  109,
   59,   35,   21,   81,   81,  141,   56,   56,   56,   56,
   82,   82,   56,   56,   56,   56,   22,   56,   75,   81,
   77,  106,   83,   31,   27,  146,   82,  147,   74,   51,
   78,  120,   31,   31,   37,   31,   59,  116,  117,  119,
  125,   51,   42,   59,  121,   12,   13,   38,  123,  132,
  124,   52,   59,   59,   59,   59,   10,  133,   40,  136,
   41,  138,   10,  140,   59,   59,   59,   59,  142,  144,
   59,   59,   59,   59,   43,   59,   12,  167,  172,   45,
  174,  154,  155,  156,  157,  158,  159,  160,  161,  125,
  126,  127,   75,   47,   77,   51,  190,  203,  132,   80,
  168,  205,   74,   84,  169,  170,   60,   50,  101,  171,
   75,  102,   77,  103,   75,  202,   77,   24,   26,  173,
   74,  175,   78,  105,   74,   29,   78,  107,   31,  112,
   34,   29,  113,  187,  188,  108,   31,   85,   86,   87,
  115,  122,   50,  137,  145,   31,  151,  176,   80,   34,
   85,   86,   87,   88,  163,   80,  125,  126,  127,  128,
   89,   90,   91,  165,   80,  132,   88,   80,  132,  125,
  126,  127,  128,  139,   90,   91,   92,  131,  132,   93,
  177,  178,   80,   80,   80,  180,   94,   80,  184,   92,
  191,   78,   93,   85,   86,   87,  125,  126,   78,   94,
  194,  195,  193,  199,  206,  132,  134,   78,  200,   88,
   78,  204,  201,    3,   14,   59,  143,   90,   91,    3,
    4,   29,   59,    5,    6,   78,   78,   15,   25,   24,
   78,   59,   92,   59,   59,   93,   46,   35,   18,   58,
   87,   95,   94,   59,   59,   59,   59,   87,   79,   59,
   59,   59,   59,   96,   59,   79,   87,   12,   13,   87,
   99,    3,    4,   40,   79,   74,   34,   79,   87,   87,
   87,   87,   74,   70,   87,   87,   87,   87,   59,   87,
  179,   74,   79,   19,   74,   79,   53,   79,   36,  135,
   75,  189,  196,   74,   74,   74,   74,   75,  192,   74,
   74,   74,    0,    0,   74,    0,   75,    0,    0,   75,
    0,    0,    0,    0,    0,   73,    0,    0,   75,   75,
   75,   75,   73,    0,   75,   75,   75,    0,    0,   75,
    0,   73,    0,    0,   73,    0,   77,    0,    0,    0,
    0,    0,    0,   77,   73,   73,   73,    0,   11,   73,
   73,   73,   77,    0,   73,   77,   85,   76,    0,    0,
    0,    0,    0,    0,   76,    0,   77,   77,   85,  148,
   77,   77,   77,   76,    0,   77,   76,   85,   85,   85,
   85,  197,    0,   85,   85,   85,   85,    0,   76,    0,
    0,   76,   76,   76,  198,    0,   76,  125,  126,  127,
  128,    0,    0,  129,  130,  131,  132,    0,    0,  125,
  126,  127,  128,    0,    0,  129,  130,  131,  132,    0,
   62,   62,  125,  126,  127,  128,    0,   62,  129,  130,
  131,  132,   62,   62,   62,   62,   62,    0,    0,    0,
   62,   62,   62,   62,    0,   69,   69,  125,  126,  127,
  128,   62,   69,   62,  130,  131,  132,   69,   69,   69,
   69,   69,    0,    0,    0,   69,   69,   69,   69,   12,
   55,    0,    0,    0,  152,    0,   69,    0,   69,    0,
    0,   56,   57,   58,   59,   60,  162,    0,    0,   61,
   62,   63,   64,    0,    0,  125,  126,  127,  128,  164,
   65,  129,  130,  131,  132,    0,    0,  125,  126,  127,
  128,  166,    0,  129,  130,  131,  132,    0,    0,    0,
  125,  126,  127,  128,  183,    0,  129,  130,  131,  132,
    0,    0,  125,  126,  127,  128,    0,    0,  129,  130,
  131,  132,    0,  149,    0,  125,  126,  127,  128,    0,
    0,  129,  130,  131,  132,  125,  126,  127,  128,  182,
    0,  129,  130,  131,  132,    0,    0,    0,  125,  126,
  127,  128,    0,    0,  129,  130,  131,  132,  125,  126,
  127,  128,    0,    0,  129,  130,  131,  132,    0,  153,
  125,  126,  127,  128,    0,    0,  129,  130,  131,  132,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         49,
  263,   49,  275,   47,    3,   35,  258,   70,   71,   49,
  273,   49,  258,   76,  287,  264,  258,  275,  268,   63,
   70,   71,   70,   71,  258,  275,   76,  258,   76,  287,
   70,   71,   70,   71,  284,  285,   76,  287,   76,   69,
  286,  290,  274,  267,  267,  108,  296,  297,  298,  299,
  274,  274,  302,  303,  304,  305,  263,  307,  108,  267,
  108,   65,  285,  275,  274,  115,  274,  115,  108,  275,
  108,  295,  284,  285,  275,  287,  268,   81,   82,   83,
  296,  287,   32,  275,   88,  257,  258,  284,   92,  305,
   94,   41,  284,  285,  286,  287,    2,  101,  258,  103,
  287,  105,    8,  107,  296,  297,  298,  299,  112,  113,
  302,  303,  304,  305,  264,  307,  257,  258,  162,  287,
  164,  125,  126,  127,  128,  129,  130,  131,  132,  296,
  297,  298,  182,  275,  182,  267,  180,  200,  305,  268,
  139,  204,  182,  258,  143,  149,  286,  287,  274,  153,
  200,  274,  200,  274,  204,  199,  204,   17,   18,  163,
  200,  165,  200,  274,  204,   21,  204,  285,   24,  285,
   26,   27,  285,  177,  178,  287,   32,  258,  259,  260,
  286,  258,   38,  278,  258,   41,  275,  263,  268,   45,
  258,  259,  260,  274,  287,  275,  296,  297,  298,  299,
  281,  282,  283,  274,  284,  305,  274,  287,  305,  296,
  297,  298,  299,  281,  282,  283,  297,  304,  305,  300,
  267,  267,  302,  303,  304,  276,  307,  307,  290,  297,
  258,  268,  300,  258,  259,  260,  296,  297,  275,  307,
  259,  264,  287,  275,  264,  305,  102,  284,  289,  274,
  287,  263,  276,    0,  275,  268,  281,  282,  283,  261,
  262,  264,  275,  265,  266,  302,  303,  275,  264,  264,
  307,  284,  297,  286,  287,  300,  287,  264,  275,  286,
  268,  275,  307,  296,  297,  298,  299,  275,  268,  302,
  303,  304,  305,  275,  307,  275,  284,  257,  258,  287,
  275,  261,  262,  287,  284,  268,  287,  287,  296,  297,
  298,  299,  275,  264,  302,  303,  304,  305,  286,  307,
  170,  284,  302,    8,  287,   50,   45,  307,   27,  102,
  268,  180,  186,  296,  297,  298,  299,  275,  182,  302,
  303,  304,   -1,   -1,  307,   -1,  284,   -1,   -1,  287,
   -1,   -1,   -1,   -1,   -1,  268,   -1,   -1,  296,  297,
  298,  299,  275,   -1,  302,  303,  304,   -1,   -1,  307,
   -1,  284,   -1,   -1,  287,   -1,  268,   -1,   -1,   -1,
   -1,   -1,   -1,  275,  297,  298,  299,   -1,  267,  302,
  303,  304,  284,   -1,  307,  287,  275,  268,   -1,   -1,
   -1,   -1,   -1,   -1,  275,   -1,  298,  299,  287,  268,
  302,  303,  304,  284,   -1,  307,  287,  296,  297,  298,
  299,  268,   -1,  302,  303,  304,  305,   -1,  299,   -1,
   -1,  302,  303,  304,  268,   -1,  307,  296,  297,  298,
  299,   -1,   -1,  302,  303,  304,  305,   -1,   -1,  296,
  297,  298,  299,   -1,   -1,  302,  303,  304,  305,   -1,
  257,  258,  296,  297,  298,  299,   -1,  264,  302,  303,
  304,  305,  269,  270,  271,  272,  273,   -1,   -1,   -1,
  277,  278,  279,  280,   -1,  257,  258,  296,  297,  298,
  299,  288,  264,  290,  303,  304,  305,  269,  270,  271,
  272,  273,   -1,   -1,   -1,  277,  278,  279,  280,  257,
  258,   -1,   -1,   -1,  275,   -1,  288,   -1,  290,   -1,
   -1,  269,  270,  271,  272,  273,  275,   -1,   -1,  277,
  278,  279,  280,   -1,   -1,  296,  297,  298,  299,  275,
  288,  302,  303,  304,  305,   -1,   -1,  296,  297,  298,
  299,  275,   -1,  302,  303,  304,  305,   -1,   -1,   -1,
  296,  297,  298,  299,  275,   -1,  302,  303,  304,  305,
   -1,   -1,  296,  297,  298,  299,   -1,   -1,  302,  303,
  304,  305,   -1,  284,   -1,  296,  297,  298,  299,   -1,
   -1,  302,  303,  304,  305,  296,  297,  298,  299,  287,
   -1,  302,  303,  304,  305,   -1,   -1,   -1,  296,  297,
  298,  299,   -1,   -1,  302,  303,  304,  305,  296,  297,
  298,  299,   -1,   -1,  302,  303,  304,  305,   -1,  307,
  296,  297,  298,  299,   -1,   -1,  302,  303,  304,  305,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=310;
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
"ADD_ARITHM","MORELESS","EQ","NULL","IFX","OR","AND","XOR","POW","UN_ARITHM",
"NOT","tuple_value","reference","idref_tail",
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
"idref : dynamic_value DOT dynamic_value",
"idref : dynamic_value DOT idref",
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
"exp : constant",
"exp : dynamic_value",
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

//#line 712 "./borzhch.y"

private SymTable topTable = null;
private static FuncTable funcTable = null;
private SymTable structTable = null;

private String mainClass = "Program";
private String currentClass = "Program";

private Queue<String> refQueue = null;
private Stack<String> refTypesStack = null;

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

/**
 * Checks stack to see if it's possible to call resource.
 * @param stack Identifiers stack.
 * @param prevVal Previous value. First is always null.
 * @param prevType Previous value type. First is always null.
 * @return String Type of last field.
 */
private String resolveRefQueueType(Queue<String> queue, String prev, String prevType) {
    String result = null; //By default it's false.
    
    boolean empty = (queue.peek() == null);
    if (prev == null) {
        if (empty) {
            return null;
        } else {
            String prevNext = queue.poll();
            String prevNextType = getSymbolType(prevNext);
            refTypesStack.push(prevNextType);
            result = resolveRefQueueType(queue, prevNext, prevNextType);
        }
    } else {
        if (empty) {
            return prevType;
        } else {
            result = StructTable.getFieldType(prevType, queue.peek());
            if (result != null) {
                refTypesStack.push(result);
                result = resolveRefQueueType(queue, queue.poll(), result);
            } else {
                return null;
            }
        }
    }

    return result;
}

private GetFieldNode resolveFieldsTypes(GetFieldNode getNode, Queue<String> ref, Stack<String> types) {
    GetFieldNode result = getNode;

    String type = null;
    if ((type = resolveRefQueueType(refQueue, null, null)) == null) {
        String msg = String.format("Unable to get resource.");
        yyerror(msg);
    }

    BOType boType = null;
    
    ArrayList<NodeAST> fields = getNode.getFields();
    int end = fields.size();
    for (int i = 0; i < end; i++) {
        type = types.pop();
        boType = BOHelper.getType(type);
        fields.get(i).type(boType);
        //ArrayElementNode, VariableNode, FuncallNode
        if (fields.get(i) instanceof INodeWithVarTypeName) ((INodeWithVarTypeName) fields.get(i)).setVarTypeName(type);
    }

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
    parseError = true;
}

public Parser(Reader r, boolean debug) {
  lexer = new Lexer(r, this);
  yydebug = debug;
}
//#line 662 "Parser.java"
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

        refQueue = new LinkedList<String>();
        refTypesStack = new Stack<String>();
    }
break;
case 3:
//#line 83 "./borzhch.y"
{ 
           yyval.obj = null; 
    }
break;
case 4:
//#line 86 "./borzhch.y"
{ 
        StatementList list = new StatementList(); 
        list.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) list.addAll((NodeList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 5:
//#line 94 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 6:
//#line 95 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 7:
//#line 96 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 8:
//#line 99 "./borzhch.y"
{
            topTable = new SymTable(topTable);
         }
break;
case 9:
//#line 104 "./borzhch.y"
{
            SymTable oldTable = topTable;
            topTable = oldTable.getPrevious();
            oldTable.setPrevious(null);
            oldTable.clear();
        }
break;
case 10:
//#line 113 "./borzhch.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 11:
//#line 114 "./borzhch.y"
{ 
        if (!isTypeExist(val_peek(0).sval)) {
            yyerror(String.format("can not resolve symbol <%s>\n", val_peek(0).sval));
        }
        yyval.sval = val_peek(0).sval; 
    }
break;
case 12:
//#line 123 "./borzhch.y"
{
        if (isIdentifierExist(val_peek(4).sval)) {
            yyerror(String.format("identifier <%s> is already defined", val_peek(4).sval));
        }
        FunctionNode node = new FunctionNode(val_peek(4).sval, val_peek(5).sval, currentClass);
        node.setArguments((NodeList) val_peek(2).obj);
        node.setStatements((StatementList) val_peek(0).obj);

        funcTable.push(node);
        yyval.obj = node;
    }
break;
case 13:
//#line 134 "./borzhch.y"
{
        if(isIdentifierExist(val_peek(4).sval)) {
          String msg = ErrorHelper.identifierExists(val_peek(4).sval);
          yyerror(msg);
        }
        FunctionNode func = new FunctionNode(val_peek(4).sval, BOType.VOID, currentClass);
        func.setArguments((StatementList) val_peek(2).obj);
        func.setStatements((StatementList) val_peek(0).obj);

        funcTable.push(func);
    
        yyval.obj = func;
    }
break;
case 14:
//#line 149 "./borzhch.y"
{ yyval.obj = null; }
break;
case 15:
//#line 150 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 16:
//#line 155 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
        }
break;
case 17:
//#line 163 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((NodeList) val_peek(0).obj);
            yyval.obj = node; 
          }
break;
case 18:
//#line 169 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
          }
break;
case 19:
//#line 177 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 20:
//#line 181 "./borzhch.y"
{
        if(isIdentifierExist(val_peek(1).sval)) {
            String msg = ErrorHelper.identifierExists(val_peek(1).sval);
            yyerror(msg);
        }
        structTable.pushSymbol(val_peek(1).sval, "ref");

        StructDeclarationNode node = new StructDeclarationNode(val_peek(1).sval, (FieldList) val_peek(0).obj, false);
        yyval.obj = node;
    }
break;
case 21:
//#line 194 "./borzhch.y"
{
        String identifier = (String) val_peek(1).obj;
        if(isIdentifierExist(identifier)) {
            String msg = ErrorHelper.identifierExists(identifier);
            yyerror(msg);
        }
        structTable.pushSymbol(identifier, "ref");

        currentClass = mainClass;
        StructDeclarationNode node = new StructDeclarationNode(identifier, (FieldList) val_peek(0).obj, true);
        yyval.obj = node;
    }
break;
case 22:
//#line 209 "./borzhch.y"
{
        funcTable = new FuncTable(funcTable);
        currentClass = val_peek(0).sval; 
        yyval.obj = val_peek(0).sval;
    }
break;
case 23:
//#line 217 "./borzhch.y"
{
        FieldList node = (FieldList) val_peek(1).obj;
        yyval.obj = node; 
    }
break;
case 24:
//#line 223 "./borzhch.y"
{ 
        FieldList node = new FieldList();
        DeclarationNode decl = (DeclarationNode) val_peek(1).obj;
        decl.isField(true);
        node.add((NodeAST) decl);
        yyval.obj = node; 
    }
break;
case 25:
//#line 230 "./borzhch.y"
{
        FieldList node = new FieldList();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 26:
//#line 235 "./borzhch.y"
{
        FieldList node = new FieldList();
        DeclarationNode decl = (DeclarationNode) val_peek(2).obj;
        decl.isField(true);
        node.add((NodeAST) decl);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 27:
//#line 243 "./borzhch.y"
{
        FieldList node = new FieldList();
        node.add((NodeAST) val_peek(1).obj);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 28:
//#line 252 "./borzhch.y"
{
        yyval.obj = val_peek(1).obj;
    }
break;
case 29:
//#line 257 "./borzhch.y"
{ yyval.obj = null; }
break;
case 30:
//#line 258 "./borzhch.y"
{
        FieldList node = new FieldList();
        DeclarationNode ldecl = (DeclarationNode) val_peek(2).obj;
        ldecl.isField(true);
        node.add(ldecl);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 31:
//#line 269 "./borzhch.y"
{
        if (isIdentifierExist(val_peek(0).sval)) {
            yyerror(String.format("identifier <%s> is already defined\n", val_peek(0).sval));
        }
        topTable.pushSymbol(val_peek(0).sval, val_peek(1).sval);
        DeclarationNode node = new DeclarationNode(val_peek(0).sval, val_peek(1).sval);
        yyval.obj = node;
    }
break;
case 32:
//#line 277 "./borzhch.y"
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
case 33:
//#line 289 "./borzhch.y"
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
case 34:
//#line 302 "./borzhch.y"
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
case 35:
//#line 327 "./borzhch.y"
{ yyval.obj = null; }
break;
case 36:
//#line 328 "./borzhch.y"
{ 
        StatementList list = new StatementList();
        list.add((NodeAST) val_peek(2).obj);
        if (val_peek(0).obj != null) list.addAll((StatementList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 37:
//#line 334 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 38:
//#line 340 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 39:
//#line 346 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 40:
//#line 355 "./borzhch.y"
{
        yyval.obj = new PrintNode((NodeAST) val_peek(0).obj);
    }
break;
case 41:
//#line 360 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 42:
//#line 361 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 43:
//#line 362 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 44:
//#line 363 "./borzhch.y"
{ 
      if(!isIdentifierExist(val_peek(0).sval)) {
        String msg = ErrorHelper.notDeclared(val_peek(0).sval);
        yyerror(msg);
      }
      yyval.obj = null; 
    }
break;
case 45:
//#line 370 "./borzhch.y"
{ yyval.obj = new ReturnNode((NodeAST) val_peek(0).obj); }
break;
case 46:
//#line 371 "./borzhch.y"
{ yyval.obj = new ReturnNode(null); }
break;
case 47:
//#line 372 "./borzhch.y"
{ yyval.obj = new BreakNode(); }
break;
case 48:
//#line 373 "./borzhch.y"
{ yyval.obj = new ContinueNode(); }
break;
case 49:
//#line 374 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 50:
//#line 375 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 51:
//#line 379 "./borzhch.y"
{
        GetFieldNode get = (GetFieldNode) val_peek(2).obj;
        get = resolveFieldsTypes(get, refQueue, refTypesStack);
        
        NodeAST expr = (NodeAST) val_peek(0).obj;
        SetFieldNode node = new SetFieldNode(get, expr);
        yyval.obj = node;
    }
break;
case 52:
//#line 387 "./borzhch.y"
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
case 53:
//#line 406 "./borzhch.y"
{
        /*arrayref := IDENTIFIER L_SQBRACE exp R_SQBRACE => ArrayElementNode*/
        
        ArrayElementNode index = (ArrayElementNode) val_peek(2).obj;
        NodeAST value = (NodeAST) val_peek(0).obj;
        SetArrayNode node = new SetArrayNode(index, value);
        yyval.obj = node;
    }
break;
case 54:
//#line 414 "./borzhch.y"
{
        GetFieldNode get = (GetFieldNode) val_peek(6).obj;
        get = resolveFieldsTypes(get, refQueue, refTypesStack);
        NewArrayNode nan = new NewArrayNode(val_peek(3).sval, (NodeAST) val_peek(1).obj);
        SetFieldNode node = new SetFieldNode(get, nan);
        yyval.obj = node;
    }
break;
case 55:
//#line 424 "./borzhch.y"
{
        if(!isIdentifierExist(val_peek(3).sval)) {
          String msg = String.format("identifier <%s> is not declared\n", val_peek(3).sval);
          yyerror(msg);
        }

        refQueue.add(val_peek(3).sval);
        VariableNode var = new VariableNode(val_peek(3).sval, topTable.getBaseType(val_peek(3).sval));
        var.type(BOType.REF);
        ArrayElementNode node = new ArrayElementNode(var, (NodeAST) val_peek(1).obj);
        yyval.obj = node;
    }
break;
case 56:
//#line 439 "./borzhch.y"
{
        DotOpNode dot = new DotOpNode((NodeAST) val_peek(2).obj, (NodeAST) val_peek(0).obj);
        GetFieldNode node = new GetFieldNode(dot.reduce());
        yyval.obj = node;
    }
break;
case 57:
//#line 444 "./borzhch.y"
{
        DotOpNode dot = new DotOpNode((NodeAST) val_peek(2).obj, (NodeAST) val_peek(0).obj);
        GetFieldNode node = new GetFieldNode(dot.reduce());
        yyval.obj = node;
    }
break;
case 58:
//#line 452 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 59:
//#line 453 "./borzhch.y"
{ 
        refQueue.add(val_peek(0).sval);
        yyval.obj = new VariableNode(val_peek(0).sval, getSymbolType(val_peek(0).sval)); 
    }
break;
case 60:
//#line 457 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 61:
//#line 461 "./borzhch.y"
{
        IfNode node = new IfNode((NodeAST) val_peek(3).obj, (StatementList) val_peek(1).obj, (IfNode) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 62:
//#line 467 "./borzhch.y"
{
      yyval.obj = null;
    }
break;
case 63:
//#line 470 "./borzhch.y"
{
      yyval.obj = (IfNode) val_peek(0).obj;
    }
break;
case 64:
//#line 473 "./borzhch.y"
{
      IfNode node = new IfNode(null, (StatementList) val_peek(0).obj, null);
      yyval.obj = node;
    }
break;
case 65:
//#line 479 "./borzhch.y"
{
        NodeAST decl = (NodeAST) val_peek(6).obj;
        NodeAST counter = (NodeAST) val_peek(4).obj;
        NodeAST step = (NodeAST) val_peek(2).obj;
        NodeAST statements = (NodeAST) val_peek(0).obj; 
        ForNode node = new ForNode(decl, counter, step, statements);
        yyval.obj = node;
    }
break;
case 66:
//#line 487 "./borzhch.y"
{
        WhileNode node = new WhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 67:
//#line 491 "./borzhch.y"
{
        DoWhileNode node = new DoWhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(5).obj);
        yyval.obj = node;
    }
break;
case 68:
//#line 497 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(8).obj, (StatementList) val_peek(5).obj, (StatementList) val_peek(1).obj);
        yyval.obj = node;
      }
break;
case 69:
//#line 502 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(4).obj, (StatementList) val_peek(1).obj, null);
        yyval.obj = node;
      }
break;
case 70:
//#line 509 "./borzhch.y"
{ yyval.obj = null; }
break;
case 71:
//#line 510 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((StatementList) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 72:
//#line 517 "./borzhch.y"
{
        CaseNode node = new CaseNode(val_peek(2).ival, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 73:
//#line 524 "./borzhch.y"
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
case 74:
//#line 535 "./borzhch.y"
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
case 75:
//#line 546 "./borzhch.y"
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
case 76:
//#line 560 "./borzhch.y"
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
case 77:
//#line 571 "./borzhch.y"
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
case 78:
//#line 585 "./borzhch.y"
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
case 79:
//#line 596 "./borzhch.y"
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
case 80:
//#line 607 "./borzhch.y"
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
case 81:
//#line 618 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 82:
//#line 619 "./borzhch.y"
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
case 83:
//#line 628 "./borzhch.y"
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
case 84:
//#line 637 "./borzhch.y"
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
case 85:
//#line 646 "./borzhch.y"
{ 
        if(!isTypeExist(val_peek(0).sval)) {
            String msg = ErrorHelper.unknownType(val_peek(0).sval);
            yyerror(msg);
        }
        yyval.obj = new NewObjectNode(val_peek(0).sval); 
    }
break;
case 86:
//#line 653 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 87:
//#line 654 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 88:
//#line 658 "./borzhch.y"
{ yyval.obj = new IntegerNode(val_peek(0).ival); }
break;
case 89:
//#line 659 "./borzhch.y"
{ yyval.obj = new FloatNode((float)val_peek(0).dval); }
break;
case 90:
//#line 660 "./borzhch.y"
{ yyval.obj = new StringNode(val_peek(0).sval); }
break;
case 91:
//#line 661 "./borzhch.y"
{ yyval.obj = new BooleanNode(val_peek(0).ival); }
break;
case 92:
//#line 662 "./borzhch.y"
{ yyval.obj = new NullNode(); }
break;
case 93:
//#line 663 "./borzhch.y"
{ 
        GetFieldNode node = (GetFieldNode) val_peek(0).obj;
        node = resolveFieldsTypes(node, refQueue, refTypesStack);
        yyval.obj = node;
    }
break;
case 94:
//#line 671 "./borzhch.y"
{
        if(!isIdentifierExist(val_peek(3).sval)) {
          String msg = ErrorHelper.notDeclared(val_peek(3).sval);
          yyerror(msg);
        }
        refQueue.add(val_peek(3).sval);
        FunctionCallNode node = new FunctionCallNode(val_peek(3).sval, (StatementList) val_peek(1).obj);
        yyval.obj = node;
    }
break;
case 95:
//#line 682 "./borzhch.y"
{
          yyval.obj = null;
        }
break;
case 96:
//#line 685 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 97:
//#line 690 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 98:
//#line 698 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 99:
//#line 704 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
//#line 1661 "Parser.java"
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
