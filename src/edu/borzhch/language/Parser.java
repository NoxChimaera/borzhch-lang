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
public final static short structref=309;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,   31,    2,    2,    3,    3,   32,   33,    1,    1,
    8,    8,   21,   21,   21,    6,    6,   13,    9,   22,
   10,   10,   16,   16,   17,   17,   14,   14,   14,   14,
   14,   28,   15,   15,   15,   15,   15,   15,   15,   15,
   15,   18,   18,   18,   20,   23,   24,   24,   11,   12,
   12,   12,    7,    7,    7,   25,   25,   27,   27,   26,
   19,   19,   19,   19,   19,   19,   19,   19,   19,   19,
   19,   19,   19,   19,   19,   29,   29,   29,   29,   30,
   30,   30,   30,    4,    4,    4,    5,    5,
};
final static short yylen[] = {                            2,
    2,    0,    0,    2,    1,    1,    1,    1,    1,    1,
    7,    6,    0,    1,    2,    3,    2,    3,    3,    3,
    0,    3,    2,    4,    7,    3,    0,    3,    2,    2,
    2,    2,    1,    1,    1,    2,    2,    1,    1,    1,
    1,    3,    3,    3,    4,    3,    1,    3,    6,    0,
    2,    2,    9,    5,    7,   11,    7,    0,    2,    4,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    2,
    4,    2,    2,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    4,    0,    1,    2,    3,    2,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    1,    0,    5,    6,    9,
   10,    0,    0,    0,    4,    0,    0,    0,   19,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   15,    0,
   20,    0,    0,    0,    0,    7,   12,    0,   22,   11,
   24,   16,    0,    0,    0,   39,   40,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   34,   35,
    0,    0,    0,   41,    0,    0,    0,   36,    0,   76,
   77,    0,    0,   78,   79,    0,    0,    0,   80,   81,
   74,   75,    0,    0,    0,    0,    0,    0,   30,   29,
    8,   18,    0,    0,    0,    0,   31,    0,    0,    0,
   46,    0,   72,    0,   73,    0,   70,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   28,    0,    0,    0,    0,   45,    0,    0,    0,
   69,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   48,   83,    0,
   86,   71,    0,    0,   54,    0,    0,    0,    0,    0,
   49,    0,    0,    0,    0,    0,    0,   87,   51,   52,
    0,    0,   55,    0,   59,    0,   25,    0,    0,    0,
   53,   60,    0,    0,   56,
};
final static short yydgoto[] = {                          1,
   21,    6,    7,  129,  151,   29,   54,    8,    9,   24,
   55,  161,   37,   56,   57,   58,   59,   60,   78,   79,
   23,   19,   80,  101,   63,  165,  166,   64,   81,   82,
    2,   38,   92,
};
final static short yysindex[] = {                         0,
    0, -188, -178, -251, -249,    0, -188,    0,    0,    0,
    0, -247, -245, -224,    0, -219, -178, -178,    0, -178,
 -243, -202, -177, -159, -200, -168, -156, -178,    0, -149,
    0, -178, -149, -147, -202,    0,    0,  147,    0,    0,
    0,    0, -221, -136, -198,    0,    0, -150, -148, -146,
 -149, -139, -198,  147,  147, -129, -137, -145,    0,    0,
 -144, -134,  147,    0, -198, -198, -122,    0, -236,    0,
    0, -198, -120,    0,    0, -198, -198,  229,    0,    0,
    0,    0, -198, -178, -198, -125, -198,  229,    0,    0,
    0,    0,  147, -164, -198, -198,    0,   46,  229, -128,
    0, -198,    0,  134,    0,  217,    0, -198, -198, -198,
 -198, -198, -198, -198, -198,  146, -145, -131,  159, -115,
  171,    0, -154,  229,  229,  229,    0, -122, -114,  194,
    0, -198, -143, -263, -248, -204,  239,  -33,  -13, -143,
 -149, -198, -149, -198, -104,    0, -103,    0,    0, -198,
    0,    0, -111,  207,    0,  184, -110, -198,  194, -187,
    0,  -83, -106,  -77, -110,  -81,   58,    0,    0,    0,
 -221,  -90,    0, -100,    0,  -75,    0, -149,  147,  -78,
    0,    0,  147,  -69,    0,
};
final static short yyrindex[] = {                         0,
    0,  202,    0,    0,    0,    0,  202,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -67,  -56,    0,  -67,
    0,  -64,    0,    0,    0,    0, -196,    0,    0,    0,
    0,  -56,    0,    0,  -63,    0,    0,  -51,    0,    0,
    0,    0,  -44,    0,  -66,    0,    0,    0,    0,    0,
    0,    0,    0, -258, -258,    0,    0,  -62,    0,    0,
    0,    0, -258,    0,    0,    0,    0,    0, -105,    0,
    0,    0,    0,    0,    0,    0,    0,  -61,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -200,    0,    0,
    0,    0, -258,    0,    0,    0,    0,    0, -266, -130,
    0,  -47,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -57, -262, -260,    0,    0,    0,  -42,
    0,    0,  -80,  -29,   -8,   13, -155,  -97,   34,  -55,
    0,    0,    0,    0,    0,   26,    0,    0,    0,    0,
    0,    0,  100,    0,    0,    0,  -34,    0,  -41,    0,
    0,    0,    0,    0,  -34,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  124,    0,    0, -258,    0,
    0,    0,  -51,    0,    0,
};
final static short yygindex[] = {                         0,
   -2,  227,    0,    0,   76,  201,    0,    0,    0,  205,
   79,    0,  -30,  -50,    0,   18,  160,   87,  -43,  -38,
  230,    0,  -36,  125,    0,    0,   90,    0,    0,    0,
    0,    0,    0,
};
final static int YYTABLESIZE=543;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         61,
   12,   62,   40,   89,   90,   27,   13,   43,   14,   88,
   16,   44,   97,   42,   27,   61,   61,   62,   62,   43,
   86,   98,   99,   44,   61,   42,   62,   17,  104,   65,
   27,  108,  106,  107,   22,   25,  102,   22,   18,  116,
  115,  119,  122,  121,   65,   35,  108,  109,   67,   25,
  124,  125,  126,   20,   61,  115,   62,  103,  130,   69,
   70,   71,   66,   67,  133,  134,  135,  136,  137,  138,
  139,  140,    3,    4,   72,   36,    5,   23,   10,   11,
   28,   73,   74,   75,   48,   32,   23,   23,  152,   23,
  108,  109,  110,   69,   70,   71,   30,   76,  154,  115,
  156,  117,   10,  146,   31,   33,  159,   77,   72,   34,
  153,   67,  155,   36,  167,  123,   74,   75,   67,   41,
  147,   68,   83,   61,   84,   62,   85,   67,  182,  170,
   67,   76,  184,   87,   91,  100,   47,  105,   94,   95,
   61,   77,   62,   47,   61,   67,   62,  181,   93,   96,
   67,  120,   47,   47,  142,   47,  128,  144,  157,  149,
  115,   82,  158,  160,   47,   47,   47,   47,   82,   66,
   47,   47,   47,   47,  171,   47,   66,   82,  164,  173,
   82,  174,  176,  178,  183,   66,   62,  179,   66,   82,
   82,   82,   82,   62,  185,   82,   82,   82,   82,  180,
   82,    3,   62,   66,   66,   62,   13,   21,   66,   14,
   17,   63,   27,   10,   62,   62,   62,   62,   63,   38,
   62,   62,   62,   33,   37,   62,   84,   63,   26,   58,
   63,   85,   88,   15,  168,   42,   39,   61,  169,   63,
   63,   63,   63,  118,   61,   63,   63,   63,  172,   26,
   63,    0,  148,   61,  175,    0,   61,    0,   65,    0,
    0,  108,  109,  110,  111,   65,   61,   61,   61,  114,
  115,   61,   61,   61,   65,    0,   61,   65,    0,   64,
    0,  108,  109,  110,  111,    0,   64,    0,   65,   65,
  115,   10,   65,   65,   65,   64,    0,   65,   64,    0,
   68,    0,    0,    0,    0,    0,    0,   68,    0,    0,
   64,   73,  127,   64,   64,   64,   68,    0,   64,   68,
   73,   73,   73,   73,  177,    0,   73,   73,   73,   73,
    0,    0,    0,    0,   68,   68,   68,    0,    0,   68,
  108,  109,  110,  111,    0,    0,  112,  113,  114,  115,
    0,    0,  108,  109,  110,  111,   50,   50,  112,  113,
  114,  115,    0,   50,    0,    0,    0,   50,   50,   50,
   50,   50,    0,    0,    0,   50,   50,   50,   50,    0,
   57,   57,    0,    0,    0,    0,   50,   57,   50,    0,
    0,   57,   57,   57,   57,   57,    0,    0,    0,   57,
   57,   57,   57,   10,   43,    0,    0,  131,    0,    0,
   57,    0,   57,    0,   44,   45,   46,   47,   48,  141,
    0,    0,   49,   50,   51,   52,    0,    0,  108,  109,
  110,  111,  143,   53,  112,  113,  114,  115,    0,    0,
  108,  109,  110,  111,  145,    0,  112,  113,  114,  115,
    0,    0,    0,  108,  109,  110,  111,  163,    0,  112,
  113,  114,  115,    0,    0,  108,  109,  110,  111,    0,
    0,  112,  113,  114,  115,    0,  150,    0,  108,  109,
  110,  111,    0,    0,  112,  113,  114,  115,  108,  109,
  110,  111,  162,    0,  112,  113,  114,  115,    0,    0,
    0,  108,  109,  110,  111,    0,    0,  112,  113,  114,
  115,  108,  109,  110,  111,    0,    0,  112,  113,  114,
  115,    0,  132,  108,  109,  110,  111,    0,    0,  112,
  113,  114,  115,  108,  109,  110,  111,    0,    0,    0,
  113,  114,  115,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         38,
    3,   38,   33,   54,   55,  264,  258,  274,  258,   53,
  258,  274,   63,  274,  258,   54,   55,   54,   55,  286,
   51,   65,   66,  286,   63,  286,   63,  273,   72,  266,
  289,  295,   76,   77,   17,   18,  273,   20,  263,   83,
  304,   85,   93,   87,  266,   28,  295,  296,  285,   32,
   94,   95,   96,  273,   93,  304,   93,  294,  102,  258,
  259,  260,  284,  285,  108,  109,  110,  111,  112,  113,
  114,  115,  261,  262,  273,  263,  265,  274,  257,  258,
  283,  280,  281,  282,  272,  286,  283,  284,  132,  286,
  295,  296,  297,  258,  259,  260,  274,  296,  142,  304,
  144,   84,  257,  258,  264,  274,  150,  306,  273,  266,
  141,  267,  143,  263,  158,  280,  281,  282,  274,  267,
  123,  258,  273,  162,  273,  162,  273,  283,  179,  160,
  286,  296,  183,  273,  264,  258,  267,  258,  284,  284,
  179,  306,  179,  274,  183,  301,  183,  178,  286,  284,
  306,  277,  283,  284,  286,  286,  285,  273,  263,  274,
  304,  267,  266,  275,  295,  296,  297,  298,  274,  267,
  301,  302,  303,  304,  258,  306,  274,  283,  289,  286,
  286,  259,  264,  274,  263,  283,  267,  288,  286,  295,
  296,  297,  298,  274,  264,  301,  302,  303,  304,  275,
  306,    0,  283,  301,  302,  286,  274,  264,  306,  274,
  274,  267,  264,  258,  295,  296,  297,  298,  274,  286,
  301,  302,  303,  286,  286,  306,  274,  283,  286,  264,
  286,  274,  274,    7,  159,   35,   32,  267,  160,  295,
  296,  297,  298,   84,  274,  301,  302,  303,  162,   20,
  306,   -1,  128,  283,  165,   -1,  286,   -1,  267,   -1,
   -1,  295,  296,  297,  298,  274,  296,  297,  298,  303,
  304,  301,  302,  303,  283,   -1,  306,  286,   -1,  267,
   -1,  295,  296,  297,  298,   -1,  274,   -1,  297,  298,
  304,  266,  301,  302,  303,  283,   -1,  306,  286,   -1,
  267,   -1,   -1,   -1,   -1,   -1,   -1,  274,   -1,   -1,
  298,  286,  267,  301,  302,  303,  283,   -1,  306,  286,
  295,  296,  297,  298,  267,   -1,  301,  302,  303,  304,
   -1,   -1,   -1,   -1,  301,  302,  303,   -1,   -1,  306,
  295,  296,  297,  298,   -1,   -1,  301,  302,  303,  304,
   -1,   -1,  295,  296,  297,  298,  257,  258,  301,  302,
  303,  304,   -1,  264,   -1,   -1,   -1,  268,  269,  270,
  271,  272,   -1,   -1,   -1,  276,  277,  278,  279,   -1,
  257,  258,   -1,   -1,   -1,   -1,  287,  264,  289,   -1,
   -1,  268,  269,  270,  271,  272,   -1,   -1,   -1,  276,
  277,  278,  279,  257,  258,   -1,   -1,  274,   -1,   -1,
  287,   -1,  289,   -1,  268,  269,  270,  271,  272,  274,
   -1,   -1,  276,  277,  278,  279,   -1,   -1,  295,  296,
  297,  298,  274,  287,  301,  302,  303,  304,   -1,   -1,
  295,  296,  297,  298,  274,   -1,  301,  302,  303,  304,
   -1,   -1,   -1,  295,  296,  297,  298,  274,   -1,  301,
  302,  303,  304,   -1,   -1,  295,  296,  297,  298,   -1,
   -1,  301,  302,  303,  304,   -1,  283,   -1,  295,  296,
  297,  298,   -1,   -1,  301,  302,  303,  304,  295,  296,
  297,  298,  286,   -1,  301,  302,  303,  304,   -1,   -1,
   -1,  295,  296,  297,  298,   -1,   -1,  301,  302,  303,
  304,  295,  296,  297,  298,   -1,   -1,  301,  302,  303,
  304,   -1,  306,  295,  296,  297,  298,   -1,   -1,  301,
  302,  303,  304,  295,  296,  297,  298,   -1,   -1,   -1,
  302,  303,  304,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=309;
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
"tuple_value","reference","structref",
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
"exp : constant",
"exp : dynamic_value",
"constant : INTEGER",
"constant : FLOAT",
"constant : STRING",
"constant : BOOLEAN",
"dynamic_value : arrayref",
"dynamic_value : idref",
"dynamic_value : IDENTIFIER",
"dynamic_value : IDENTIFIER L_BRACE exp_list R_BRACE",
"exp_list :",
"exp_list : exp",
"exp_list : exp exp_tail",
"exp_tail : COMMA exp exp_tail",
"exp_tail : COMMA exp",
};

//#line 632 "./borzhch.y"

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
//#line 539 "Parser.java"
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
        /*DotOpNode dot = (DotOpNode) $1;
        dot.reduce();*/
        GetFieldNode get = (GetFieldNode) val_peek(2).obj;

        NodeAST exp = (NodeAST) val_peek(0).obj;
        SetFieldNode node = new SetFieldNode(get, exp);
        /*SetFieldNode node = new SetFieldNode(dot, exp);*/
        yyval.obj = node;
    }
break;
case 43:
//#line 312 "./borzhch.y"
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
//#line 331 "./borzhch.y"
{
        /*arrayref := IDENTIFIER L_SQBRACE exp R_SQBRACE => ArrayElementNode*/
        
        ArrayElementNode index = (ArrayElementNode) val_peek(2).obj;
        NodeAST value = (NodeAST) val_peek(0).obj;
        SetArrayNode node = new SetArrayNode(index, value);
        yyval.obj = node;
    }
break;
case 45:
//#line 342 "./borzhch.y"
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
//#line 356 "./borzhch.y"
{
        VariableNode var = new VariableNode(val_peek(2).sval, topTable.getSymbolType(val_peek(2).sval));
        DotOpNode dot = new DotOpNode(var, (NodeAST) val_peek(0).obj);
        /*((IDotNode) node).setStructName(var.strType());*/
        /*node.type(node.getLastNode().type());*/
        GetFieldNode node = new GetFieldNode(var, dot.reduce());
        yyval.obj = node;
    }
break;
case 47:
//#line 367 "./borzhch.y"
{
        FieldNode node = new FieldNode(val_peek(0).sval);
        yyval.obj = node;
    }
break;
case 48:
//#line 371 "./borzhch.y"
{
        FieldNode field = new FieldNode(val_peek(2).sval);
        DotOpNode node = new DotOpNode(field, (NodeAST) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 49:
//#line 379 "./borzhch.y"
{
        IfNode node = new IfNode((NodeAST) val_peek(3).obj, (StatementList) val_peek(1).obj, (IfNode) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 50:
//#line 385 "./borzhch.y"
{
      yyval.obj = null;
    }
break;
case 51:
//#line 388 "./borzhch.y"
{
      yyval.obj = (IfNode) val_peek(0).obj;
    }
break;
case 52:
//#line 391 "./borzhch.y"
{
      IfNode node = new IfNode(null, (StatementList) val_peek(0).obj, null);
      yyval.obj = node;
    }
break;
case 53:
//#line 397 "./borzhch.y"
{
        NodeAST decl = (NodeAST) val_peek(6).obj;
        NodeAST counter = (NodeAST) val_peek(4).obj;
        NodeAST step = (NodeAST) val_peek(2).obj;
        NodeAST statements = (NodeAST) val_peek(0).obj; 
        ForNode node = new ForNode(decl, counter, step, statements);
        yyval.obj = node;
    }
break;
case 54:
//#line 405 "./borzhch.y"
{
        WhileNode node = new WhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 55:
//#line 409 "./borzhch.y"
{
        DoWhileNode node = new DoWhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(5).obj);
        yyval.obj = node;
    }
break;
case 56:
//#line 415 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(8).obj, (StatementList) val_peek(5).obj, (StatementList) val_peek(1).obj);
        yyval.obj = node;
      }
break;
case 57:
//#line 420 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(4).obj, (StatementList) val_peek(1).obj, null);
        yyval.obj = node;
      }
break;
case 58:
//#line 427 "./borzhch.y"
{ yyval.obj = null; }
break;
case 59:
//#line 428 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((StatementList) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 60:
//#line 435 "./borzhch.y"
{
        CaseNode node = new CaseNode(val_peek(2).ival, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 61:
//#line 442 "./borzhch.y"
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
case 62:
//#line 453 "./borzhch.y"
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
case 63:
//#line 464 "./borzhch.y"
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
case 64:
//#line 478 "./borzhch.y"
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
case 65:
//#line 489 "./borzhch.y"
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
case 66:
//#line 503 "./borzhch.y"
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
case 67:
//#line 514 "./borzhch.y"
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
case 68:
//#line 525 "./borzhch.y"
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
case 69:
//#line 536 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 70:
//#line 537 "./borzhch.y"
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
case 71:
//#line 546 "./borzhch.y"
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
case 72:
//#line 555 "./borzhch.y"
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
case 73:
//#line 564 "./borzhch.y"
{ 
        if(!isTypeExist(val_peek(0).sval)) {
            String msg = String.format("unknown type <%s>\n", val_peek(0).sval);
            yyerror(msg);
        }
        yyval.obj = new NewObjectNode(val_peek(0).sval); 
    }
break;
case 74:
//#line 571 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 75:
//#line 572 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 76:
//#line 576 "./borzhch.y"
{ yyval.obj = new IntegerNode(val_peek(0).ival); }
break;
case 77:
//#line 577 "./borzhch.y"
{ yyval.obj = new FloatNode((float)val_peek(0).dval); }
break;
case 78:
//#line 578 "./borzhch.y"
{ yyval.obj = new StringNode(val_peek(0).sval); }
break;
case 79:
//#line 579 "./borzhch.y"
{ yyval.obj = new BooleanNode(val_peek(0).ival); }
break;
case 80:
//#line 583 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 81:
//#line 584 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 82:
//#line 585 "./borzhch.y"
{ 
	    if(!isIdentifierExist(val_peek(0).sval)) {
            String msg = String.format("identifier <%s> is not declared\n", val_peek(0).sval);
            yyerror(msg);
        }
        yyval.obj = new VariableNode(val_peek(0).sval, topTable.getSymbolType(val_peek(0).sval)); 
    }
break;
case 83:
//#line 592 "./borzhch.y"
{
        if(!isIdentifierExist(val_peek(3).sval)) {
            String msg = String.format("identifier <%s> is not declared\n", val_peek(3).sval);
            yyerror(msg);
        }
        FunctionCallNode node = new FunctionCallNode(val_peek(3).sval, (StatementList) val_peek(1).obj);
        yyval.obj = node;
    }
break;
case 84:
//#line 602 "./borzhch.y"
{
          yyval.obj = null;
        }
break;
case 85:
//#line 605 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 86:
//#line 610 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 87:
//#line 618 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 88:
//#line 624 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
//#line 1449 "Parser.java"
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
