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
public final static short NULL=299;
public final static short IFX=300;
public final static short OR=301;
public final static short AND=302;
public final static short XOR=303;
public final static short POW=304;
public final static short UN_ARITHM=305;
public final static short NOT=306;
public final static short tuple_value=307;
public final static short reference=308;
public final static short idref_tail=309;
public final static short structref=310;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,   30,    2,    2,    3,    3,   31,   32,    1,    1,
    8,    8,   21,   21,   21,    6,    6,   13,    9,   22,
   10,   10,   16,   16,   17,   17,   14,   14,   14,   14,
   14,   27,   15,   15,   15,   15,   15,   15,   15,   15,
   15,   18,   18,   18,   20,   23,   11,   12,   12,   12,
    7,    7,    7,   24,   24,   26,   26,   25,   19,   19,
   19,   19,   19,   19,   19,   19,   19,   19,   19,   19,
   19,   19,   19,   28,   28,   28,   28,   28,   28,   29,
   29,   29,    4,    4,    4,    5,    5,
};
final static short yylen[] = {                            2,
    2,    0,    0,    2,    1,    1,    1,    1,    1,    1,
    7,    6,    0,    1,    2,    3,    2,    3,    3,    3,
    0,    3,    2,    4,    7,    3,    0,    3,    2,    2,
    2,    2,    1,    1,    1,    2,    2,    1,    1,    1,
    1,    3,    3,    3,    4,    3,    6,    0,    2,    2,
    9,    5,    7,   11,    7,    0,    2,    4,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    2,    4,    2,
    2,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    4,    0,    1,    2,    3,    2,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    1,    0,    5,    6,    9,
   10,    0,    0,    0,    4,    0,    0,    0,   19,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   15,    0,
   20,    0,    0,    0,    0,    7,   12,    0,   22,   11,
   24,   16,    0,    0,    0,   39,   40,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   34,   35,
    0,    0,    0,   41,    0,    0,    0,    0,   36,    0,
   74,   75,    0,    0,   76,   77,    0,   78,    0,    0,
   80,   79,   72,    0,    0,    0,    0,    0,    0,    0,
   30,   29,    8,   18,    0,    0,    0,    0,   31,    0,
    0,    0,    0,    0,   70,    0,   71,    0,   68,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   28,    0,    0,    0,    0,    0,   46,
   45,   82,    0,   85,   67,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   69,    0,    0,   52,    0,    0,    0,   86,
    0,   47,    0,    0,    0,    0,    0,    0,   49,   50,
    0,    0,   53,    0,   57,    0,   25,    0,    0,    0,
   51,   58,    0,    0,   54,
};
final static short yydgoto[] = {                          1,
   21,    6,    7,  102,  134,   29,   54,    8,    9,   24,
   55,  162,   37,   56,   57,   58,   59,   60,   80,   81,
   23,   19,   82,   63,  166,  167,   64,   83,   84,    2,
   38,   94,
};
final static short yysindex[] = {                         0,
    0, -127, -107, -249, -222,    0, -127,    0,    0,    0,
    0, -220, -267, -223,    0, -215, -107, -107,    0, -107,
 -198, -217, -203, -182, -197, -171, -170, -107,    0, -154,
    0, -107, -154, -153, -217,    0,    0,  198,    0,    0,
    0,    0, -251, -142, -212,    0,    0, -152, -149, -145,
 -154, -134, -212,  198,  198, -120, -126, -130,    0,    0,
 -123, -121,  198,    0, -118, -212, -212, -212,    0, -252,
    0,    0, -212,  -88,    0,    0, -212,    0, -212,  280,
    0,    0,    0, -118, -212, -107, -212, -106, -212,  280,
    0,    0,    0,    0,  198, -208, -212, -212,    0,  -86,
   96, -101,  245,  280,    0,  185,    0,  268,    0, -212,
 -212, -212, -212, -212, -212, -212, -212,  197, -130, -105,
  210,  -94,  222,    0,  -99,  280,  280,  280, -232,    0,
    0,    0, -212,    0,    0, -212, -124, -214, -284, -178,
  290, -218, -196, -124, -154, -212, -154, -212,  -80,    0,
  -78,  245,    0,  -85,  258,    0,  235, -100, -212,    0,
 -132,    0,  -66,  -91,  -62, -100,  -49,  109,    0,    0,
 -251,  -76,    0,  -70,    0,  -56,    0, -154,  198,  -43,
    0,    0,  198,  -41,    0,
};
final static short yyrindex[] = {                         0,
    0,  221,    0,    0,    0,    0,  221,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -47,  -40,    0,  -47,
    0,  -44,    0,    0,    0,    0, -230,    0,    0,    0,
    0,  -40,    0,    0,  -42,    0,    0,  -33,    0,    0,
    0,    0, -255,    0,  -53,    0,    0,    0,    0,    0,
    0,    0,    0, -254, -254,    0,    0,  -52,    0,    0,
  -34,    0, -254,    0,    0,    0,  -32,    0,    0,  -57,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -30,
    0,    0,    0,  -24,    0,    0,    0,    0,    0, -197,
    0,    0,    0,    0, -254,    0,    0,    0,    0,    0,
    0,    0,  -22, -237,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -29, -219, -211, -191,    0,
    0,    0,    0,    0,    0,    0,    1,   51,  -90,   72,
  -92, -137,   59,   26,    0,    0,    0,    0,    0,   85,
    0,  -21,    0,  151,    0,    0,    0,  -28,    0,    0,
    0,    0,    0,    0,    0,  -28,    0,    0,    0,    0,
  -27,    0,    0,    0,    0,  175,    0,    0, -254,    0,
    0,    0,  -33,    0,    0,
};
final static short yygindex[] = {                         0,
   -2,  247,    0,    0,  103,  225,    0,    0,    0,  229,
  102,    0,  -25,  -50,    0,   11,  178,  104,   89,  -36,
  246,    0,  -31,    0,    0,   99,    0,    0,  -38,    0,
    0,    0,
};
final static int YYTABLESIZE=594;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         65,
   12,   61,   10,   91,   92,   17,   62,   40,   13,   27,
  110,  111,   99,   66,   66,   65,   65,   61,   61,  117,
   67,   67,   62,   62,   65,   88,   61,   22,   25,   81,
   22,   62,   68,   66,   27,   14,   43,   16,   35,   18,
   67,  105,   25,   23,  124,   70,   71,   72,   43,   70,
   71,   72,   23,   23,   44,   23,   65,   20,   61,   27,
   73,  130,   42,   62,   73,   28,   44,   74,   75,   76,
   30,  125,   75,   76,   42,   81,  110,  111,  112,  113,
  110,   31,   81,   77,  116,  117,   78,   77,   32,  117,
   78,   81,   81,   79,   81,   34,  119,   79,  110,  111,
  112,  113,   33,   81,   81,   81,   81,  117,   36,   81,
   81,   81,   81,   41,   81,   69,  110,  111,  112,  154,
   85,  156,  151,   86,   65,  117,   61,   87,  182,   64,
   36,   62,  184,    3,    4,  170,   64,    5,   89,   48,
   65,   90,   61,   93,   65,   64,   61,   62,   64,   10,
   11,   62,  181,   96,  101,  103,  104,   10,  150,   95,
   97,  106,   98,   64,   64,  108,  100,  109,   64,  107,
  122,  129,  132,  118,   65,  121,   63,  123,  148,  117,
  146,   65,  158,   63,  126,  127,  128,  159,  165,  161,
   65,  171,   63,   65,  173,   63,  174,  178,  137,  138,
  139,  140,  141,  142,  143,  144,   63,   63,   65,   81,
   63,   63,   63,   65,  176,   63,   81,  179,  180,  183,
    3,  152,  185,   21,  153,   81,   13,   81,   81,   14,
   27,   17,   38,   33,  155,   56,  157,   81,   81,   81,
   81,   83,   73,   81,   81,   81,   81,  168,   81,   73,
   80,   84,   87,   15,  160,   37,   26,   81,   73,   42,
   39,   73,  169,  120,  175,   26,  172,   60,    0,    0,
   73,   73,   73,   73,   60,    0,   73,   73,   73,   73,
    0,   73,    0,   60,    0,    0,   60,    0,    0,    0,
    0,    0,   61,    0,    0,   60,   60,   60,   60,   61,
    0,   60,   60,   60,    0,    0,   60,    0,   61,    0,
    0,   61,    0,    0,    0,    0,    0,   59,    0,    0,
   61,   61,   61,   61,   59,   66,   61,   61,   61,    0,
    0,   61,   66,   59,    0,    0,   59,    0,   62,    0,
    0,   66,    0,    0,   66,   62,   59,   59,   59,    0,
   10,   59,   59,   59,   62,    0,   59,   62,    0,   66,
   66,   66,  131,    0,   66,    0,    0,    0,    0,   62,
   71,    0,   62,   62,   62,  177,    0,   62,    0,   71,
   71,   71,   71,    0,    0,   71,   71,   71,   71,    0,
  110,  111,  112,  113,    0,    0,  114,  115,  116,  117,
    0,    0,    0,  110,  111,  112,  113,   48,   48,  114,
  115,  116,  117,    0,   48,    0,    0,    0,   48,   48,
   48,   48,   48,    0,    0,    0,   48,   48,   48,   48,
    0,   55,   55,    0,    0,    0,    0,   48,   55,   48,
    0,    0,   55,   55,   55,   55,   55,    0,    0,    0,
   55,   55,   55,   55,   10,   43,    0,    0,  135,    0,
    0,   55,    0,   55,    0,   44,   45,   46,   47,   48,
  145,    0,    0,   49,   50,   51,   52,    0,    0,  110,
  111,  112,  113,  147,   53,  114,  115,  116,  117,    0,
    0,  110,  111,  112,  113,  149,    0,  114,  115,  116,
  117,    0,    0,    0,  110,  111,  112,  113,  164,    0,
  114,  115,  116,  117,    0,    0,  110,  111,  112,  113,
    0,    0,  114,  115,  116,  117,    0,  133,    0,  110,
  111,  112,  113,    0,    0,  114,  115,  116,  117,  110,
  111,  112,  113,  163,    0,  114,  115,  116,  117,    0,
    0,    0,  110,  111,  112,  113,    0,    0,  114,  115,
  116,  117,  110,  111,  112,  113,    0,    0,  114,  115,
  116,  117,    0,  136,  110,  111,  112,  113,    0,    0,
  114,  115,  116,  117,  110,  111,  112,  113,    0,    0,
    0,  115,  116,  117,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         38,
    3,   38,  258,   54,   55,  273,   38,   33,  258,  264,
  295,  296,   63,  266,  266,   54,   55,   54,   55,  304,
  273,  273,   54,   55,   63,   51,   63,   17,   18,  285,
   20,   63,  284,  266,  289,  258,  274,  258,   28,  263,
  273,  294,   32,  274,   95,  258,  259,  260,  286,  258,
  259,  260,  283,  284,  274,  286,   95,  273,   95,  258,
  273,  100,  274,   95,  273,  283,  286,  280,  281,  282,
  274,  280,  281,  282,  286,  267,  295,  296,  297,  298,
  295,  264,  274,  296,  303,  304,  299,  296,  286,  304,
  299,  283,  284,  306,  286,  266,   86,  306,  295,  296,
  297,  298,  274,  295,  296,  297,  298,  304,  263,  301,
  302,  303,  304,  267,  306,  258,  295,  296,  297,  145,
  273,  147,  125,  273,  163,  304,  163,  273,  179,  267,
  263,  163,  183,  261,  262,  161,  274,  265,  273,  272,
  179,   53,  179,  264,  183,  283,  183,  179,  286,  257,
  258,  183,  178,  284,   66,   67,   68,  257,  258,  286,
  284,   73,  284,  301,  302,   77,  285,   79,  306,  258,
  277,  258,  274,   85,  267,   87,  267,   89,  273,  304,
  286,  274,  263,  274,   96,   97,   98,  266,  289,  275,
  283,  258,  283,  286,  286,  286,  259,  274,  110,  111,
  112,  113,  114,  115,  116,  117,  297,  298,  301,  267,
  301,  302,  303,  306,  264,  306,  274,  288,  275,  263,
    0,  133,  264,  264,  136,  283,  274,  285,  286,  274,
  264,  274,  286,  286,  146,  264,  148,  295,  296,  297,
  298,  274,  267,  301,  302,  303,  304,  159,  306,  274,
  285,  274,  274,    7,  152,  286,  286,  285,  283,   35,
   32,  286,  161,   86,  166,   20,  163,  267,   -1,   -1,
  295,  296,  297,  298,  274,   -1,  301,  302,  303,  304,
   -1,  306,   -1,  283,   -1,   -1,  286,   -1,   -1,   -1,
   -1,   -1,  267,   -1,   -1,  295,  296,  297,  298,  274,
   -1,  301,  302,  303,   -1,   -1,  306,   -1,  283,   -1,
   -1,  286,   -1,   -1,   -1,   -1,   -1,  267,   -1,   -1,
  295,  296,  297,  298,  274,  267,  301,  302,  303,   -1,
   -1,  306,  274,  283,   -1,   -1,  286,   -1,  267,   -1,
   -1,  283,   -1,   -1,  286,  274,  296,  297,  298,   -1,
  266,  301,  302,  303,  283,   -1,  306,  286,   -1,  301,
  302,  303,  267,   -1,  306,   -1,   -1,   -1,   -1,  298,
  286,   -1,  301,  302,  303,  267,   -1,  306,   -1,  295,
  296,  297,  298,   -1,   -1,  301,  302,  303,  304,   -1,
  295,  296,  297,  298,   -1,   -1,  301,  302,  303,  304,
   -1,   -1,   -1,  295,  296,  297,  298,  257,  258,  301,
  302,  303,  304,   -1,  264,   -1,   -1,   -1,  268,  269,
  270,  271,  272,   -1,   -1,   -1,  276,  277,  278,  279,
   -1,  257,  258,   -1,   -1,   -1,   -1,  287,  264,  289,
   -1,   -1,  268,  269,  270,  271,  272,   -1,   -1,   -1,
  276,  277,  278,  279,  257,  258,   -1,   -1,  274,   -1,
   -1,  287,   -1,  289,   -1,  268,  269,  270,  271,  272,
  274,   -1,   -1,  276,  277,  278,  279,   -1,   -1,  295,
  296,  297,  298,  274,  287,  301,  302,  303,  304,   -1,
   -1,  295,  296,  297,  298,  274,   -1,  301,  302,  303,
  304,   -1,   -1,   -1,  295,  296,  297,  298,  274,   -1,
  301,  302,  303,  304,   -1,   -1,  295,  296,  297,  298,
   -1,   -1,  301,  302,  303,  304,   -1,  283,   -1,  295,
  296,  297,  298,   -1,   -1,  301,  302,  303,  304,  295,
  296,  297,  298,  286,   -1,  301,  302,  303,  304,   -1,
   -1,   -1,  295,  296,  297,  298,   -1,   -1,  301,  302,
  303,  304,  295,  296,  297,  298,   -1,   -1,  301,  302,
  303,  304,   -1,  306,  295,  296,  297,  298,   -1,   -1,
  301,  302,  303,  304,  295,  296,  297,  298,   -1,   -1,
   -1,  302,  303,  304,
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
"L_CURBRACE","R_CURBRACE","STRUCT","L_SQBRACE","R_SQBRACE","GOTO","RETURN",
"BREAK","CONTINUE","IF","L_BRACE","R_BRACE","ELSE","FOR","WHILE","DO","SWITCH",
"NEW","STRING","BOOLEAN","COMMA","ASSIGN","DOT","SEMICOLON","PRINT","COLON",
"CASE","TUPLE","INCLUDE","UN_MINUS","UN_PLUS","INCR","MUL_ARITHM","ADD_ARITHM",
"MORELESS","EQ","NULL","IFX","OR","AND","XOR","POW","UN_ARITHM","NOT",
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
"decl_block : L_CURBRACE decl_list R_CURBRACE",
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
"assign : idref ASSIGN exp",
"assign : IDENTIFIER ASSIGN exp",
"assign : arrayref ASSIGN exp",
"arrayref : IDENTIFIER L_SQBRACE exp R_SQBRACE",
"idref : dynamic_value DOT dynamic_value",
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
"dynamic_value : arrayref",
"dynamic_value : IDENTIFIER",
"dynamic_value : IDENTIFIER L_BRACE exp_list R_BRACE",
"exp_list :",
"exp_list : exp",
"exp_list : exp exp_tail",
"exp_tail : COMMA exp exp_tail",
"exp_tail : COMMA exp",
};

//#line 638 "./borzhch.y"

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
//#line 549 "Parser.java"
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
//#line 51 "./borzhch.y"
{ 
        TreeAST.setRoot((NodeAST) val_peek(0).obj); 
     }
break;
case 2:
//#line 56 "./borzhch.y"
{
        topTable = new SymTable(null);
        
        funcTable = new FuncTable();

        structTable = new SymTable(null);
    }
break;
case 3:
//#line 65 "./borzhch.y"
{ 
           yyval.obj = null; 
    }
break;
case 4:
//#line 68 "./borzhch.y"
{ 
        StatementList list = new StatementList(); 
        list.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) list.addAll((NodeList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 5:
//#line 76 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 6:
//#line 77 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 7:
//#line 80 "./borzhch.y"
{
            topTable = new SymTable(topTable);
         }
break;
case 8:
//#line 85 "./borzhch.y"
{
            SymTable oldTable = topTable;
            topTable = oldTable.getPrevious();
            oldTable.setPrevious(null);
            oldTable.clear();
        }
break;
case 9:
//#line 94 "./borzhch.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 10:
//#line 95 "./borzhch.y"
{ 
        if (!isTypeExist(val_peek(0).sval)) {
            yyerror(String.format("can not resolve symbol <%s>\n", val_peek(0).sval));
        }
        yyval.sval = val_peek(0).sval; 
    }
break;
case 11:
//#line 104 "./borzhch.y"
{
        if (isIdentifierExist(val_peek(4).sval)) {
            yyerror(String.format("identifier <%s> is already defined", val_peek(4).sval));
        }
        FunctionNode node = new FunctionNode(val_peek(4).sval, val_peek(5).sval);
        node.setArguments((NodeList) val_peek(2).obj);
        node.setStatements((StatementList) val_peek(0).obj);

        funcTable.push(node);
        yyval.obj = node;
    }
break;
case 12:
//#line 115 "./borzhch.y"
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
case 13:
//#line 130 "./borzhch.y"
{ yyval.obj = null; }
break;
case 14:
//#line 131 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 15:
//#line 136 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
        }
break;
case 16:
//#line 144 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((NodeList) val_peek(0).obj);
            yyval.obj = node; 
          }
break;
case 17:
//#line 150 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
          }
break;
case 18:
//#line 158 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 19:
//#line 162 "./borzhch.y"
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
case 20:
//#line 175 "./borzhch.y"
{
        yyval.obj = val_peek(1).obj;
    }
break;
case 21:
//#line 180 "./borzhch.y"
{ yyval.obj = null; }
break;
case 22:
//#line 181 "./borzhch.y"
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
//#line 192 "./borzhch.y"
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
//#line 200 "./borzhch.y"
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
//#line 212 "./borzhch.y"
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
//#line 225 "./borzhch.y"
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
//#line 251 "./borzhch.y"
{ yyval.obj = null; }
break;
case 28:
//#line 252 "./borzhch.y"
{ 
        StatementList list = new StatementList();
        list.add((NodeAST) val_peek(2).obj);
        if (val_peek(0).obj != null) list.addAll((StatementList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 29:
//#line 258 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 30:
//#line 264 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 31:
//#line 270 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 32:
//#line 279 "./borzhch.y"
{
        yyval.obj = new PrintNode((NodeAST) val_peek(0).obj);
    }
break;
case 33:
//#line 284 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 34:
//#line 285 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 35:
//#line 286 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 36:
//#line 287 "./borzhch.y"
{ 
      if(!isIdentifierExist(val_peek(0).sval)) {
        String msg = String.format("identifier <%s> is not declared\n", val_peek(0).sval);
        yyerror(msg);
      }
      yyval.obj = null; 
    }
break;
case 37:
//#line 294 "./borzhch.y"
{ yyval.obj = new ReturnNode((NodeAST) val_peek(0).obj); }
break;
case 38:
//#line 295 "./borzhch.y"
{ yyval.obj = new ReturnNode(null); }
break;
case 39:
//#line 296 "./borzhch.y"
{ yyval.obj = new BreakNode(); }
break;
case 40:
//#line 297 "./borzhch.y"
{ yyval.obj = new ContinueNode(); }
break;
case 41:
//#line 298 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 42:
//#line 302 "./borzhch.y"
{
        GetFieldNode get = (GetFieldNode) val_peek(2).obj;
        NodeAST expr = (NodeAST) val_peek(0).obj;
        SetFieldNode node = new SetFieldNode(get, expr);
        yyval.obj = node;
    }
break;
case 43:
//#line 308 "./borzhch.y"
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
case 44:
//#line 327 "./borzhch.y"
{
        /*arrayref := IDENTIFIER L_SQBRACE exp R_SQBRACE => ArrayElementNode*/
        
        ArrayElementNode index = (ArrayElementNode) val_peek(2).obj;
        NodeAST value = (NodeAST) val_peek(0).obj;
        SetArrayNode node = new SetArrayNode(index, value);
        yyval.obj = node;
    }
break;
case 45:
//#line 338 "./borzhch.y"
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
case 46:
//#line 352 "./borzhch.y"
{
        DotOpNode dot = new DotOpNode((NodeAST) val_peek(2).obj, (NodeAST) val_peek(0).obj);
        GetFieldNode node = new GetFieldNode(dot.reduce());
        yyval.obj = node;
    }
break;
case 47:
//#line 384 "./borzhch.y"
{
        IfNode node = new IfNode((NodeAST) val_peek(3).obj, (StatementList) val_peek(1).obj, (IfNode) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 48:
//#line 390 "./borzhch.y"
{
      yyval.obj = null;
    }
break;
case 49:
//#line 393 "./borzhch.y"
{
      yyval.obj = (IfNode) val_peek(0).obj;
    }
break;
case 50:
//#line 396 "./borzhch.y"
{
      IfNode node = new IfNode(null, (StatementList) val_peek(0).obj, null);
      yyval.obj = node;
    }
break;
case 51:
//#line 402 "./borzhch.y"
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
//#line 410 "./borzhch.y"
{
        WhileNode node = new WhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 53:
//#line 414 "./borzhch.y"
{
        DoWhileNode node = new DoWhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(5).obj);
        yyval.obj = node;
    }
break;
case 54:
//#line 420 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(8).obj, (StatementList) val_peek(5).obj, (StatementList) val_peek(1).obj);
        yyval.obj = node;
      }
break;
case 55:
//#line 425 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(4).obj, (StatementList) val_peek(1).obj, null);
        yyval.obj = node;
      }
break;
case 56:
//#line 432 "./borzhch.y"
{ yyval.obj = null; }
break;
case 57:
//#line 433 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((StatementList) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 58:
//#line 440 "./borzhch.y"
{
        CaseNode node = new CaseNode(val_peek(2).ival, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 59:
//#line 447 "./borzhch.y"
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
//#line 458 "./borzhch.y"
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
//#line 469 "./borzhch.y"
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
//#line 483 "./borzhch.y"
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
//#line 494 "./borzhch.y"
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
//#line 508 "./borzhch.y"
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
//#line 519 "./borzhch.y"
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
//#line 530 "./borzhch.y"
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
//#line 541 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 68:
//#line 542 "./borzhch.y"
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
//#line 551 "./borzhch.y"
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
//#line 560 "./borzhch.y"
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
//#line 569 "./borzhch.y"
{ 
        if(!isTypeExist(val_peek(0).sval)) {
            String msg = String.format("unknown type <%s>\n", val_peek(0).sval);
            yyerror(msg);
        }
        yyval.obj = new NewObjectNode(val_peek(0).sval); 
    }
break;
case 72:
//#line 576 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 73:
//#line 577 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 74:
//#line 581 "./borzhch.y"
{ yyval.obj = new IntegerNode(val_peek(0).ival); }
break;
case 75:
//#line 582 "./borzhch.y"
{ yyval.obj = new FloatNode((float)val_peek(0).dval); }
break;
case 76:
//#line 583 "./borzhch.y"
{ yyval.obj = new StringNode(val_peek(0).sval); }
break;
case 77:
//#line 584 "./borzhch.y"
{ yyval.obj = new BooleanNode(val_peek(0).ival); }
break;
case 78:
//#line 585 "./borzhch.y"
{ yyval.obj = new NullNode(); }
break;
case 79:
//#line 586 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 80:
//#line 590 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 81:
//#line 591 "./borzhch.y"
{ 
	    if(!isIdentifierExist(val_peek(0).sval)) {
            String msg = String.format("identifier <%s> is not declared\n", val_peek(0).sval);
            yyerror(msg);
        }
        yyval.obj = new VariableNode(val_peek(0).sval, topTable.getSymbolType(val_peek(0).sval)); 
    }
break;
case 82:
//#line 598 "./borzhch.y"
{
        if(!isIdentifierExist(val_peek(3).sval)) {
            String msg = String.format("identifier <%s> is not declared\n", val_peek(3).sval);
            yyerror(msg);
        }
        FunctionCallNode node = new FunctionCallNode(val_peek(3).sval, (StatementList) val_peek(1).obj);
        yyval.obj = node;
    }
break;
case 83:
//#line 608 "./borzhch.y"
{
          yyval.obj = null;
        }
break;
case 84:
//#line 611 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 85:
//#line 616 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 86:
//#line 624 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 87:
//#line 630 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
//#line 1441 "Parser.java"
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
