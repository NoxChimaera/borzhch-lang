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
   24,   18,   18,   18,    7,    7,    7,    6,    6,   20,
   15,   29,   31,   30,   28,   28,   28,   28,   11,    9,
    9,    8,    8,   10,   10,   13,   13,   13,   13,   13,
   19,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   21,   21,   21,   21,   22,   23,   23,   33,   33,
   33,   16,   17,   17,   17,   14,   14,   14,   25,   25,
   26,   26,   27,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,   32,   32,
   32,   32,   32,   32,   34,    4,    4,    4,    5,    5,
};
final static short yylen[] = {                            2,
    2,    0,    0,    2,    1,    1,    1,    1,    1,    1,
    1,    7,    9,    6,    0,    1,    2,    3,    2,    3,
    3,    3,    1,    3,    2,    1,    3,    2,    3,    0,
    3,    2,    4,    7,    3,    0,    3,    2,    2,    2,
    2,    1,    1,    1,    2,    2,    1,    1,    1,    1,
    1,    3,    3,    3,    7,    4,    3,    3,    1,    1,
    1,    6,    0,    2,    2,    9,    5,    7,   11,    7,
    0,    2,    4,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    2,    4,    2,    2,    1,    1,    1,    1,
    1,    1,    1,    1,    4,    0,    1,    2,    3,    2,
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
    0,    0,    0,   18,   33,    0,    0,    0,    0,   45,
    0,   89,   90,    0,    0,   91,   92,    0,   93,    0,
    0,   59,   94,   87,    0,   61,    0,    0,    0,    0,
    0,    0,    0,    0,   20,   39,   38,    0,    0,   40,
    0,   13,    0,    0,    0,    0,   85,    0,   86,    0,
   83,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   37,    0,    0,
    0,    0,   58,    0,   56,    0,   98,   95,   82,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   84,    0,    0,
   67,    0,    0,    0,    0,   99,    0,   62,    0,    0,
    0,    0,    0,    0,    0,   64,   65,    0,    0,   68,
    0,    0,   72,   34,   55,    0,    0,    0,   66,   73,
    0,    0,   69,
};
final static short yydgoto[] = {                          1,
    7,    8,  101,  125,  157,   42,   30,   71,   37,   72,
   26,   73,   74,   75,    9,   76,  188,   34,   77,   52,
   78,  102,  103,   32,   81,  192,  193,   35,   11,   24,
   17,  104,  105,  106,    2,   53,   47,
};
final static short yysindex[] = {                         0,
    0,  -23, -206, -243, -240, -209,    0,  -23,    0,    0,
    0,    0,    0, -177, -245,    0, -210, -210,    0, -205,
 -194, -206,    0,    0,  -25,    0, -206, -206, -167, -168,
 -175, -142, -161,  -25, -129, -148, -129, -130, -127, -210,
 -206,    0, -118,  -25,    0,    0,    0, -206,    0, -210,
 -206,    0,  271, -175, -116,    0,    0,    0, -125, -254,
 -105, -182,    0,    0, -117, -103, -101, -210,  -98, -182,
 -107,    0, -104, -129,  271,  271,    0,    0,  -96,  -89,
  271,  -99,    0,    0,    0, -210, -182, -182, -182,    0,
 -258,    0,    0, -182,  -76,    0,    0, -182,    0, -182,
  353,    0,    0,    0,  -99,    0, -182, -206, -182,  -79,
 -182,  353,  -97,  271,    0,    0,    0, -182,  -66,    0,
  -57,    0,  160,  318,  -73,  353,    0,  258,    0,  341,
    0, -182, -182, -182, -182, -182, -182, -182, -182,  270,
 -107,  -83,  283,  -69,  295, -191,  353,    0,  353, -191,
  353, -195,    0,  -99,    0, -182,    0,    0,    0, -182,
  -94, -193, -186, -200,  210,   81, -211,  -94, -210, -182,
 -210, -182,  -50,    0,  -55,  -45,  318,    0,  -62,  331,
    0,  308,  -67, -182, -182,    0, -237,    0,  -28,  -39,
  -19,  -14,  -67,  172,  185,    0,    0, -254,  -26,    0,
  -38,  -22,    0,    0,    0, -210,  271,  -11,    0,    0,
  271,    2,    0,
};
final static short yyrindex[] = {                         0,
    0,  273,    0,    0,    0,    0,    0,  273,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   -1,    0,    0,    0,    0,    6,   -1,    0,    0,
    1,    0,    0,   11,    0,    0,    0,    0,    0,    0,
    0,    0, -229,   15,    0,    0,    0,    6,    0,    0,
   -1,    0,   22,   16,    0,    0,    0,    0,    0, -244,
    0,   12,    0,    0,    0,    0,    0,    0,    0,    0,
   14,    0,    0,    0, -247, -247,    0,    0,    7,    0,
 -247,    0, -166,    0,    0,    0,    0,   23,    0,    0,
  -15,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   17,    0,    0,    0,   10,    0,    0,    0,    0,    0,
    0,   18,    0, -247,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   27,    0, -268,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   24,    0, -228,    0,
 -225,  -40,    0,  -78,    0,    0,    0,    0,    0,    0,
   35,   85,  106,  127, -227,   -7,  148,   60,    0,    0,
    0,    0,    0,  140,    0,    0,   41,    0,  222,    0,
    0,    0,   54,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   54,    0,    0,    0,    0,   34,    0,    0,
    0,  247,    0,    0,    0,    0, -247,    0,    0,    0,
   22,    0,    0,
};
final static short yygindex[] = {                         0,
  313,    0,   -5,    0,  146,  272,  -24,  147,  276,  217,
    0,    0,  -70,    0,    0,  142,    0,  115,    0,  -47,
  138,  -43,  -53,   -2,    0,  137,    0,   20,    0,    0,
    0,    0,  -51,  -41,    0,  125,  -29,
};
final static int YYTABLESIZE=658;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         80,
   14,   82,   58,   38,  116,  117,   53,   49,   87,   79,
  120,   83,   87,   11,   15,   88,   36,   16,   53,   88,
  110,   80,   80,   82,   82,   23,   59,   80,   22,   82,
   89,   79,   79,   83,   83,   65,  127,   79,  122,   83,
   80,   60,   36,  148,  115,   32,   54,   80,   18,   52,
   12,   13,   23,   45,   32,   32,   80,   32,   54,   80,
   80,   52,   82,   56,  112,   12,  174,  153,   28,  154,
   79,   87,   83,   29,   80,   91,   92,   93,   88,   80,
   20,  123,  124,  126,  132,  133,  134,  135,  128,   21,
   39,   94,  130,  139,  131,  132,  133,  134,   95,   96,
   97,  140,  132,  143,  139,  145,   40,  147,   41,  132,
  133,  139,  149,  151,   98,   43,   10,   99,  139,   61,
   51,  179,   10,  181,  100,   44,  161,  162,  163,  164,
  165,  166,  167,  168,   46,   80,  210,   82,   48,  197,
  212,   25,   27,  175,   50,   79,   51,  176,   55,   86,
  177,   85,   90,   80,  178,   82,  107,   80,  209,   82,
   91,   92,   93,   79,  180,   83,  182,   79,   31,   83,
  108,   33,  109,   36,   31,  111,   94,  113,  194,  195,
   33,  129,  114,  146,   96,   97,  121,   54,  118,   57,
   33,   91,   92,   93,   36,  119,   57,   31,  144,   98,
  152,  158,   99,  170,  172,   57,   57,   94,   57,  100,
  139,  184,  183,  187,  150,   96,   97,   57,   57,   57,
   57,  185,  191,   57,   57,   57,   57,   60,   57,  198,
   98,   12,   13,   99,   60,    3,    4,    3,    4,  201,
  100,    5,    6,   60,   60,   60,   60,  200,  206,  202,
  207,  211,   60,  208,  141,   60,   60,   60,   60,   60,
   79,   60,   60,   60,   60,  213,   60,   79,   60,   30,
   60,   60,    3,   15,   26,   16,   79,   88,   25,   79,
   60,   60,   60,   60,   88,   36,   60,   60,   60,   60,
   19,   60,   59,   88,   79,   79,   88,   96,   47,   79,
   42,   97,   75,   46,   41,   88,   88,   88,   88,   75,
   35,   88,   88,   88,   88,  100,   88,   71,   75,   60,
   19,   75,  186,   57,  142,   84,  199,   76,  196,  203,
   75,   75,   75,   75,   76,    0,   75,   75,   75,    0,
    0,   75,    0,   76,    0,    0,   76,    0,    0,    0,
    0,    0,   74,    0,    0,   76,   76,   76,   76,   74,
    0,   76,   76,   76,    0,    0,   76,    0,   74,    0,
    0,   74,    0,   78,    0,    0,  132,  133,  134,  135,
   78,   74,   74,   74,  138,  139,   74,   74,   74,   78,
    0,   74,   78,    0,   77,    0,    0,    0,    0,    0,
    0,   77,    0,   78,   78,    0,   11,   78,   78,   78,
   77,    0,   78,   77,   86,   81,    0,    0,    0,    0,
    0,    0,   81,    0,    0,   77,   86,  155,   77,   77,
   77,   81,    0,   77,   81,   86,   86,   86,   86,  204,
    0,   86,   86,   86,   86,    0,    0,    0,    0,   81,
   81,   81,  205,    0,   81,  132,  133,  134,  135,    0,
    0,  136,  137,  138,  139,    0,    0,  132,  133,  134,
  135,    0,    0,  136,  137,  138,  139,    0,   63,   63,
  132,  133,  134,  135,    0,   63,  136,  137,  138,  139,
   63,   63,   63,   63,   63,    0,    0,    0,   63,   63,
   63,   63,    0,   70,   70,  132,  133,  134,  135,   63,
   70,   63,  137,  138,  139,   70,   70,   70,   70,   70,
    0,    0,    0,   70,   70,   70,   70,   12,   60,    0,
    0,    0,  159,    0,   70,    0,   70,    0,    0,   61,
   62,   63,   64,   65,  169,    0,    0,   66,   67,   68,
   69,    0,    0,  132,  133,  134,  135,  171,   70,  136,
  137,  138,  139,    0,    0,  132,  133,  134,  135,  173,
    0,  136,  137,  138,  139,    0,    0,    0,  132,  133,
  134,  135,  190,    0,  136,  137,  138,  139,    0,    0,
  132,  133,  134,  135,    0,    0,  136,  137,  138,  139,
    0,  156,    0,  132,  133,  134,  135,    0,    0,  136,
  137,  138,  139,  132,  133,  134,  135,  189,    0,  136,
  137,  138,  139,    0,    0,    0,  132,  133,  134,  135,
    0,    0,  136,  137,  138,  139,  132,  133,  134,  135,
    0,    0,  136,  137,  138,  139,    0,  160,  132,  133,
  134,  135,    0,    0,  136,  137,  138,  139,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         53,
    3,   53,   50,   28,   75,   76,  275,   37,  267,   53,
   81,   53,  267,  258,  258,  274,  264,  258,  287,  274,
   68,   75,   76,   75,   76,  263,   51,   81,  274,   81,
  285,   75,   76,   75,   76,  273,  295,   81,   86,   81,
  268,  286,  290,  114,   74,  275,  275,  275,  258,  275,
  257,  258,  263,   34,  284,  285,  284,  287,  287,  287,
  114,  287,  114,   44,   70,  257,  258,  121,  274,  121,
  114,  267,  114,  268,  302,  258,  259,  260,  274,  307,
  258,   87,   88,   89,  296,  297,  298,  299,   94,  267,
  258,  274,   98,  305,  100,  296,  297,  298,  281,  282,
  283,  107,  296,  109,  305,  111,  275,  113,  284,  296,
  297,  305,  118,  119,  297,  258,    2,  300,  305,  286,
  287,  169,    8,  171,  307,  287,  132,  133,  134,  135,
  136,  137,  138,  139,  264,  189,  207,  189,  287,  187,
  211,   17,   18,  146,  275,  189,  274,  150,  267,  275,
  156,  268,  258,  207,  160,  207,  274,  211,  206,  211,
  258,  259,  260,  207,  170,  207,  172,  211,   22,  211,
  274,   25,  274,   27,   28,  274,  274,  285,  184,  185,
   34,  258,  287,  281,  282,  283,  286,   41,  285,  268,
   44,  258,  259,  260,   48,  285,  275,   51,  278,  297,
  258,  275,  300,  287,  274,  284,  285,  274,  287,  307,
  305,  267,  263,  276,  281,  282,  283,  296,  297,  298,
  299,  267,  290,  302,  303,  304,  305,  268,  307,  258,
  297,  257,  258,  300,  275,  261,  262,  261,  262,  259,
  307,  265,  266,  284,  285,  286,  287,  287,  275,  264,
  289,  263,  268,  276,  108,  296,  297,  298,  299,  275,
  268,  302,  303,  304,  305,  264,  307,  275,  284,  264,
  286,  287,    0,  275,  264,  275,  284,  268,  264,  287,
  296,  297,  298,  299,  275,  264,  302,  303,  304,  305,
  275,  307,  286,  284,  302,  303,  287,  275,  287,  307,
  287,  275,  268,  287,  287,  296,  297,  298,  299,  275,
  287,  302,  303,  304,  305,  275,  307,  264,  284,  286,
    8,  287,  177,   48,  108,   54,  189,  268,  187,  193,
  296,  297,  298,  299,  275,   -1,  302,  303,  304,   -1,
   -1,  307,   -1,  284,   -1,   -1,  287,   -1,   -1,   -1,
   -1,   -1,  268,   -1,   -1,  296,  297,  298,  299,  275,
   -1,  302,  303,  304,   -1,   -1,  307,   -1,  284,   -1,
   -1,  287,   -1,  268,   -1,   -1,  296,  297,  298,  299,
  275,  297,  298,  299,  304,  305,  302,  303,  304,  284,
   -1,  307,  287,   -1,  268,   -1,   -1,   -1,   -1,   -1,
   -1,  275,   -1,  298,  299,   -1,  267,  302,  303,  304,
  284,   -1,  307,  287,  275,  268,   -1,   -1,   -1,   -1,
   -1,   -1,  275,   -1,   -1,  299,  287,  268,  302,  303,
  304,  284,   -1,  307,  287,  296,  297,  298,  299,  268,
   -1,  302,  303,  304,  305,   -1,   -1,   -1,   -1,  302,
  303,  304,  268,   -1,  307,  296,  297,  298,  299,   -1,
   -1,  302,  303,  304,  305,   -1,   -1,  296,  297,  298,
  299,   -1,   -1,  302,  303,  304,  305,   -1,  257,  258,
  296,  297,  298,  299,   -1,  264,  302,  303,  304,  305,
  269,  270,  271,  272,  273,   -1,   -1,   -1,  277,  278,
  279,  280,   -1,  257,  258,  296,  297,  298,  299,  288,
  264,  290,  303,  304,  305,  269,  270,  271,  272,  273,
   -1,   -1,   -1,  277,  278,  279,  280,  257,  258,   -1,
   -1,   -1,  275,   -1,  288,   -1,  290,   -1,   -1,  269,
  270,  271,  272,  273,  275,   -1,   -1,  277,  278,  279,
  280,   -1,   -1,  296,  297,  298,  299,  275,  288,  302,
  303,  304,  305,   -1,   -1,  296,  297,  298,  299,  275,
   -1,  302,  303,  304,  305,   -1,   -1,   -1,  296,  297,
  298,  299,  275,   -1,  302,  303,  304,  305,   -1,   -1,
  296,  297,  298,  299,   -1,   -1,  302,  303,  304,  305,
   -1,  284,   -1,  296,  297,  298,  299,   -1,   -1,  302,
  303,  304,  305,  296,  297,  298,  299,  287,   -1,  302,
  303,  304,  305,   -1,   -1,   -1,  296,  297,  298,  299,
   -1,   -1,  302,  303,  304,  305,  296,  297,  298,  299,
   -1,   -1,  302,  303,  304,  305,   -1,  307,  296,  297,
  298,  299,   -1,   -1,  302,  303,  304,  305,
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

//#line 723 "./borzhch.y"

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
//#line 670 "Parser.java"
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
        if (isIdentifierExist(val_peek(4).sval)) {
            yyerror(String.format("identifier <%s> is already defined", val_peek(4).sval));
        }
        FunctionNode node = new FunctionNode(val_peek(4).sval, "$array", currentClass);
        node.setArguments((NodeList) val_peek(2).obj);
        node.setStatements((StatementList) val_peek(0).obj);

        funcTable.push(node);
        yyval.obj = node;
    }
break;
case 14:
//#line 145 "./borzhch.y"
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
case 15:
//#line 160 "./borzhch.y"
{ yyval.obj = null; }
break;
case 16:
//#line 161 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 17:
//#line 166 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
        }
break;
case 18:
//#line 174 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((NodeList) val_peek(0).obj);
            yyval.obj = node; 
          }
break;
case 19:
//#line 180 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
          }
break;
case 20:
//#line 188 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 21:
//#line 192 "./borzhch.y"
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
case 22:
//#line 205 "./borzhch.y"
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
case 23:
//#line 220 "./borzhch.y"
{
        funcTable = new FuncTable(funcTable);
        currentClass = val_peek(0).sval; 
        yyval.obj = val_peek(0).sval;
    }
break;
case 24:
//#line 228 "./borzhch.y"
{
        FieldList node = (FieldList) val_peek(1).obj;
        yyval.obj = node; 
    }
break;
case 25:
//#line 234 "./borzhch.y"
{ 
        FieldList node = new FieldList();
        DeclarationNode decl = (DeclarationNode) val_peek(1).obj;
        decl.isField(true);
        node.add((NodeAST) decl);
        yyval.obj = node; 
    }
break;
case 26:
//#line 241 "./borzhch.y"
{
        FieldList node = new FieldList();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 27:
//#line 246 "./borzhch.y"
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
//#line 254 "./borzhch.y"
{
        FieldList node = new FieldList();
        node.add((NodeAST) val_peek(1).obj);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 29:
//#line 263 "./borzhch.y"
{
        yyval.obj = val_peek(1).obj;
    }
break;
case 30:
//#line 268 "./borzhch.y"
{ yyval.obj = null; }
break;
case 31:
//#line 269 "./borzhch.y"
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
//#line 280 "./borzhch.y"
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
//#line 288 "./borzhch.y"
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
//#line 300 "./borzhch.y"
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
//#line 313 "./borzhch.y"
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
//#line 338 "./borzhch.y"
{ yyval.obj = null; }
break;
case 37:
//#line 339 "./borzhch.y"
{ 
        StatementList list = new StatementList();
        list.add((NodeAST) val_peek(2).obj);
        if (val_peek(0).obj != null) list.addAll((StatementList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 38:
//#line 345 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 39:
//#line 351 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 40:
//#line 357 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 41:
//#line 366 "./borzhch.y"
{
        yyval.obj = new PrintNode((NodeAST) val_peek(0).obj);
    }
break;
case 42:
//#line 371 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 43:
//#line 372 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 44:
//#line 373 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 45:
//#line 374 "./borzhch.y"
{ 
      if(!isIdentifierExist(val_peek(0).sval)) {
        String msg = ErrorHelper.notDeclared(val_peek(0).sval);
        yyerror(msg);
      }
      yyval.obj = null; 
    }
break;
case 46:
//#line 381 "./borzhch.y"
{ yyval.obj = new ReturnNode((NodeAST) val_peek(0).obj); }
break;
case 47:
//#line 382 "./borzhch.y"
{ yyval.obj = new ReturnNode(null); }
break;
case 48:
//#line 383 "./borzhch.y"
{ yyval.obj = new BreakNode(); }
break;
case 49:
//#line 384 "./borzhch.y"
{ yyval.obj = new ContinueNode(); }
break;
case 50:
//#line 385 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 51:
//#line 386 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 52:
//#line 390 "./borzhch.y"
{
        GetFieldNode get = (GetFieldNode) val_peek(2).obj;
        get = resolveFieldsTypes(get, refQueue, refTypesStack);
        
        NodeAST expr = (NodeAST) val_peek(0).obj;
        SetFieldNode node = new SetFieldNode(get, expr);
        yyval.obj = node;
    }
break;
case 53:
//#line 398 "./borzhch.y"
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
//#line 417 "./borzhch.y"
{
        /*arrayref := IDENTIFIER L_SQBRACE exp R_SQBRACE => ArrayElementNode*/
        
        ArrayElementNode index = (ArrayElementNode) val_peek(2).obj;
        NodeAST value = (NodeAST) val_peek(0).obj;
        SetArrayNode node = new SetArrayNode(index, value);
        yyval.obj = node;
    }
break;
case 55:
//#line 425 "./borzhch.y"
{
        GetFieldNode get = (GetFieldNode) val_peek(6).obj;
        get = resolveFieldsTypes(get, refQueue, refTypesStack);
        NewArrayNode nan = new NewArrayNode(val_peek(3).sval, (NodeAST) val_peek(1).obj);
        SetFieldNode node = new SetFieldNode(get, nan);
        yyval.obj = node;
    }
break;
case 56:
//#line 435 "./borzhch.y"
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
case 57:
//#line 450 "./borzhch.y"
{
        DotOpNode dot = new DotOpNode((NodeAST) val_peek(2).obj, (NodeAST) val_peek(0).obj);
        GetFieldNode node = new GetFieldNode(dot.reduce());
        yyval.obj = node;
    }
break;
case 58:
//#line 455 "./borzhch.y"
{
        DotOpNode dot = new DotOpNode((NodeAST) val_peek(2).obj, (NodeAST) val_peek(0).obj);
        GetFieldNode node = new GetFieldNode(dot.reduce());
        yyval.obj = node;
    }
break;
case 59:
//#line 463 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 60:
//#line 464 "./borzhch.y"
{ 
        refQueue.add(val_peek(0).sval);
        yyval.obj = new VariableNode(val_peek(0).sval, getSymbolType(val_peek(0).sval)); 
    }
break;
case 61:
//#line 468 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 62:
//#line 472 "./borzhch.y"
{
        IfNode node = new IfNode((NodeAST) val_peek(3).obj, (StatementList) val_peek(1).obj, (IfNode) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 63:
//#line 478 "./borzhch.y"
{
      yyval.obj = null;
    }
break;
case 64:
//#line 481 "./borzhch.y"
{
      yyval.obj = (IfNode) val_peek(0).obj;
    }
break;
case 65:
//#line 484 "./borzhch.y"
{
      IfNode node = new IfNode(null, (StatementList) val_peek(0).obj, null);
      yyval.obj = node;
    }
break;
case 66:
//#line 490 "./borzhch.y"
{
        NodeAST decl = (NodeAST) val_peek(6).obj;
        NodeAST counter = (NodeAST) val_peek(4).obj;
        NodeAST step = (NodeAST) val_peek(2).obj;
        NodeAST statements = (NodeAST) val_peek(0).obj; 
        ForNode node = new ForNode(decl, counter, step, statements);
        yyval.obj = node;
    }
break;
case 67:
//#line 498 "./borzhch.y"
{
        WhileNode node = new WhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 68:
//#line 502 "./borzhch.y"
{
        DoWhileNode node = new DoWhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(5).obj);
        yyval.obj = node;
    }
break;
case 69:
//#line 508 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(8).obj, (StatementList) val_peek(5).obj, (StatementList) val_peek(1).obj);
        yyval.obj = node;
      }
break;
case 70:
//#line 513 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(4).obj, (StatementList) val_peek(1).obj, null);
        yyval.obj = node;
      }
break;
case 71:
//#line 520 "./borzhch.y"
{ yyval.obj = null; }
break;
case 72:
//#line 521 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((StatementList) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 73:
//#line 528 "./borzhch.y"
{
        CaseNode node = new CaseNode(val_peek(2).ival, (StatementList) val_peek(0).obj);
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
        BOType infer = InferenceTypeTable.inferType(l.type(), r.type());
        if (BOType.VOID == infer) {
            yyerror(ErrorHelper.incompatibleTypes(r.type(), l.type()));
        }
        ArOpNode node = new ArOpNode(l, r, val_peek(1).sval);
        node.type(infer);
        yyval.obj = node;
    }
break;
case 76:
//#line 557 "./borzhch.y"
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
case 77:
//#line 571 "./borzhch.y"
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
case 78:
//#line 582 "./borzhch.y"
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
case 79:
//#line 596 "./borzhch.y"
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
case 80:
//#line 607 "./borzhch.y"
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
case 81:
//#line 618 "./borzhch.y"
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
case 82:
//#line 629 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 83:
//#line 630 "./borzhch.y"
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
case 84:
//#line 639 "./borzhch.y"
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
case 85:
//#line 648 "./borzhch.y"
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
case 86:
//#line 657 "./borzhch.y"
{ 
        if(!isTypeExist(val_peek(0).sval)) {
            String msg = ErrorHelper.unknownType(val_peek(0).sval);
            yyerror(msg);
        }
        yyval.obj = new NewObjectNode(val_peek(0).sval); 
    }
break;
case 87:
//#line 664 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 88:
//#line 665 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 89:
//#line 669 "./borzhch.y"
{ yyval.obj = new IntegerNode(val_peek(0).ival); }
break;
case 90:
//#line 670 "./borzhch.y"
{ yyval.obj = new FloatNode((float)val_peek(0).dval); }
break;
case 91:
//#line 671 "./borzhch.y"
{ yyval.obj = new StringNode(val_peek(0).sval); }
break;
case 92:
//#line 672 "./borzhch.y"
{ yyval.obj = new BooleanNode(val_peek(0).ival); }
break;
case 93:
//#line 673 "./borzhch.y"
{ yyval.obj = new NullNode(); }
break;
case 94:
//#line 674 "./borzhch.y"
{ 
        GetFieldNode node = (GetFieldNode) val_peek(0).obj;
        node = resolveFieldsTypes(node, refQueue, refTypesStack);
        yyval.obj = node;
    }
break;
case 95:
//#line 682 "./borzhch.y"
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
case 96:
//#line 693 "./borzhch.y"
{
          yyval.obj = null;
        }
break;
case 97:
//#line 696 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 98:
//#line 701 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 99:
//#line 709 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 100:
//#line 715 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
//#line 1683 "Parser.java"
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
