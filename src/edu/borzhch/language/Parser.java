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
public final static short IFX=300;
public final static short OR=301;
public final static short AND=302;
public final static short XOR=303;
public final static short POW=304;
public final static short UN_ARITHM=305;
public final static short NOT=306;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,   34,    1,    1,    2,    2,    2,   35,   36,   19,
   19,    7,    7,    7,    6,    6,   21,   15,   31,   33,
   32,   30,   30,   30,   30,   11,    9,    9,    8,    8,
    8,   10,   10,   13,   13,   13,   13,   13,   20,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   22,   22,
   22,   25,   26,   26,   16,   17,   17,   17,   14,   14,
   14,   27,   27,   28,   28,   29,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,   23,   24,
   18,    4,    4,    4,    5,    5,
};
final static short yylen[] = {                            2,
    2,    0,    0,    2,    1,    1,    1,    1,    1,    7,
    6,    0,    1,    2,    3,    2,    3,    3,    3,    1,
    3,    1,    1,    3,    2,    3,    0,    3,    2,    2,
    4,    7,    3,    0,    3,    2,    2,    2,    2,    1,
    1,    1,    2,    2,    1,    1,    1,    1,    3,    3,
    3,    3,    1,    3,    6,    0,    2,    2,    9,    5,
    7,   11,    7,    0,    2,    4,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    2,    4,    2,    2,    4,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    4,
    3,    0,    1,    2,    3,    2,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    0,    1,    0,    6,    5,
    7,    0,    0,   20,    0,    0,    4,    0,    0,    8,
   19,    0,    0,   18,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   29,    0,   30,    0,    0,
   14,    0,   25,    9,   21,    0,   26,    0,    0,   11,
    0,    0,   24,   28,   10,   31,    0,    0,    0,   46,
   47,    0,    0,    0,    0,    0,    0,    0,   41,    0,
    0,    0,    0,   48,   42,    0,    0,    0,   15,    0,
    0,    0,   43,    0,   84,   85,    0,    0,    0,   86,
   87,    0,    0,    0,   82,   81,   89,   83,    0,    0,
    0,    0,    0,    0,    0,    0,   17,   37,   36,    0,
    0,   38,    0,    0,    0,   52,    0,   78,    0,    0,
    0,   79,    0,   76,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   35,    0,    0,   90,    0,    0,    0,   94,   91,   75,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   54,   80,    0,   77,    0,
    0,   60,    0,    0,    0,   95,    0,   55,    0,    0,
    0,    0,    0,    0,   57,   58,    0,    0,   61,    0,
    0,   65,   32,    0,    0,    0,   59,   66,    0,    0,
   62,
};
final static short yydgoto[] = {                          1,
    7,    8,  119,  120,  148,   41,   28,   68,   34,   69,
   24,   70,   71,   72,    9,   73,  178,   95,   31,   74,
   50,   75,   96,   97,   98,  116,   78,  182,  183,   32,
   11,   21,   15,    2,   51,   45,
};
final static short yysindex[] = {                         0,
    0,  -32, -243, -217, -215, -192,    0,  -32,    0,    0,
    0, -179, -181,    0, -204, -166,    0, -175, -221,    0,
    0,   -3, -221,    0, -221, -251, -176, -180, -173, -182,
   -3, -152, -164, -140, -150,    0, -139,    0, -204, -221,
    0,   -3,    0,    0,    0, -221,    0, -204, -119,    0,
  156, -173,    0,    0,    0,    0, -252, -117, -123,    0,
    0, -132, -131, -127, -204, -120, -123, -130,    0, -142,
 -152,  156,  156,    0,    0, -114, -113,  156,    0, -123,
 -123, -102,    0, -242,    0,    0, -123, -123,  -93,    0,
    0, -123, -123,  231,    0,    0,    0,    0, -123, -221,
 -123,  -99, -123,  231,  -97,  156,    0,    0,    0, -123,
 -123,    0,   53,  231, -106,    0, -123,    0,  198,  -83,
  144,    0,  220,    0, -123, -123, -123, -123, -123, -123,
 -123, -123,  155, -130,  -94,  164,  -80,  174, -177,  231,
    0,  231,  231,    0, -102,  -79, -123,    0,    0,    0,
 -123, -107, -178, -183,   10,  240, -195, -129, -107, -204,
 -123, -204, -123,  -59,  -65,    0,    0,  198,    0,  -71,
  211,    0,  189,  -84, -123,    0, -200,    0,  -50,  -66,
  -40,  -42,  -84,   71,    0,    0, -237,  -48,    0,  -64,
  -52,    0,    0, -204,  156,  -19,    0,    0,  156,  -33,
    0,
};
final static short yyrindex[] = {                         0,
    0,  246,    0,    0,    0,    0,    0,  246,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -28,    0,
    0,    0,  -15,    0,  -28,    0,    0,    0,  -25,  -12,
   -8,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -15,    0,    0,    0,    0,
    4,   -6,    0,    0,    0,    0,    0,    0,  -16,    0,
    0,    0,    0,    0,    0,    0,    0,  -13,    0,    0,
    0, -255, -255,    0,    0,    0,    0, -255,    0,    0,
    0,    0,    0,  -61,    0,    0,    8,    0,    0,    0,
    0,    0,    0,  -10,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   -7,    0, -255,    0,    0,    0,    0,
    0,    0,    0, -274,  -86,    0,    6,    0, -203,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   -4,
    0, -233, -219,    0,    0,    0,    0,    0,    0,    0,
    0,  -36,   14, -111,   35, -168, -256,   60,  -11,    0,
    0,    0,    0,    0,    0,    0,    0, -199,    0,  107,
    0,    0,    0,   11,    0,    0,    0,    0,    0,    0,
    0,    0,   11,    0,    0,    0,    0,    0,    0,    0,
  132,    0,    0,    0, -255,    0,    0,    0,    4,    0,
    0,
};
final static short yygindex[] = {                         0,
  271,    0,  -41,  167,  125,  242,  272,   52,  250,  200,
    0,    0,  -68,    0,    0,  127,    0,    0,    9,    0,
  -45,  123,    0,  -51,  -49,  160,    0,  126,    0,   36,
    0,    0,    0,    0,  303,  252,
};
final static int YYTABLESIZE=544;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         76,
   50,   77,   55,  108,  109,   38,   36,   72,   34,  112,
   10,   72,   50,   12,   80,   37,   10,   94,   72,  102,
   76,   76,   77,   77,   80,  104,   76,   72,   77,   80,
   72,  117,   81,   82,   34,   26,   27,  141,  113,  114,
   13,   51,   14,   82,   72,   72,  121,   81,   82,   72,
  123,  124,  118,   51,   76,   49,   77,  133,   20,  136,
   93,  138,   20,  140,   96,   16,   43,   49,  142,  143,
   29,   93,   62,   30,   33,   96,   29,   53,   18,  165,
  122,   38,   30,  152,  153,  154,  155,  156,  157,  158,
  159,   52,   19,   30,   39,   73,   23,   33,   25,   73,
  125,  126,  127,  128,   42,  168,   73,  131,  132,  169,
   40,   44,  125,  126,  170,   73,  172,  125,   73,  171,
  132,  173,   46,   47,   48,  132,  198,   76,   49,   77,
  200,  186,   73,  184,   84,   85,   86,   73,   56,   87,
   83,   99,  100,   76,  106,   77,  101,   76,  197,   77,
   88,  134,   71,  103,  105,  115,   71,   89,   90,   91,
   84,   85,   86,   71,  122,   87,  125,  126,  127,  128,
  110,  111,   71,   92,  132,   71,   88,   53,  137,  145,
  149,   53,   93,  139,   90,   91,   71,   71,   53,   71,
   71,   71,  161,  163,   71,  167,  132,   53,   53,   92,
   53,  175,   88,  174,  177,  181,   88,  187,   93,   53,
   53,   53,   53,   88,   53,   53,   53,   53,  190,   53,
  189,  191,   88,  196,  195,   88,  194,   68,    3,    4,
  201,   68,    5,    6,   88,   88,   88,   88,   68,   88,
   88,   88,   88,  199,   88,    3,   12,   68,   27,   13,
   68,   22,   69,   26,   27,   23,   69,    3,    4,   68,
   68,   68,   68,   69,   68,   68,   68,   34,   16,   68,
   45,   92,   69,   40,   64,   69,   44,   67,   17,   39,
   92,   67,   33,  146,   69,   69,   69,   69,   67,   69,
   69,   69,  176,   79,   69,   54,   35,   67,   70,  135,
   67,  188,   70,  185,  166,  125,  126,  127,  192,   70,
   67,   67,   67,  132,   67,   67,   67,   22,   70,   67,
  144,   70,  107,   74,    0,    0,    0,   74,    0,    0,
    0,    0,    0,   70,   74,   70,   70,   70,  193,    0,
   70,    0,    0,   74,    0,    0,   74,    0,  125,  126,
  127,  128,    0,  129,  130,  131,  132,    0,    0,    0,
   74,   74,   74,   56,   56,   74,  125,  126,  127,  128,
   56,  129,  130,  131,  132,   56,   56,   56,   56,   56,
    0,    0,    0,   56,   56,   56,   56,    0,   63,   63,
    0,    0,    0,    0,   56,   63,   56,    0,    0,    0,
   63,   63,   63,   63,   63,    0,    0,    0,   63,   63,
   63,   63,   26,   57,    0,    0,    0,    0,  150,   63,
    0,   63,    0,    0,   58,   59,   60,   61,   62,  160,
    0,    0,   63,   64,   65,   66,    0,    0,  162,  125,
  126,  127,  128,   67,  129,  130,  131,  132,  164,    0,
  125,  126,  127,  128,    0,  129,  130,  131,  132,  125,
  126,  127,  128,  180,  129,  130,  131,  132,    0,  125,
  126,  127,  128,    0,  129,  130,  131,  132,    0,    0,
    0,  147,    0,    0,  125,  126,  127,  128,    0,  129,
  130,  131,  132,  125,  126,  127,  128,  179,  129,  130,
  131,  132,    0,    0,    0,    0,  125,  126,  127,  128,
    0,  129,  130,  131,  132,  125,  126,  127,  128,    0,
  129,  130,  131,  132,    0,  151,  125,  126,  127,  128,
    0,  129,  130,  131,  132,  125,  126,  127,  128,    0,
    0,  130,  131,  132,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         51,
  275,   51,   48,   72,   73,  258,  258,  264,  264,   78,
    2,  268,  287,  257,  267,  267,    8,   59,  275,   65,
   72,   73,   72,   73,  267,   67,   78,  284,   78,  267,
  287,  274,  285,  286,  290,  257,  258,  106,   80,   81,
  258,  275,  258,  286,  301,  302,   88,  285,  286,  306,
   92,   93,  295,  287,  106,  275,  106,   99,  263,  101,
  264,  103,  263,  105,  264,  258,   31,  287,  110,  111,
   19,  275,  273,   22,   23,  275,   25,   42,  258,  257,
  258,  258,   31,  125,  126,  127,  128,  129,  130,  131,
  132,   40,  274,   42,  275,  264,  263,   46,  274,  268,
  296,  297,  298,  299,  287,  147,  275,  303,  304,  151,
  284,  264,  296,  297,  160,  284,  162,  296,  287,  161,
  304,  163,  287,  264,  275,  304,  195,  179,  268,  179,
  199,  177,  301,  175,  258,  259,  260,  306,  258,  263,
  258,  274,  274,  195,  287,  195,  274,  199,  194,  199,
  274,  100,  264,  274,  285,  258,  268,  281,  282,  283,
  258,  259,  260,  275,  258,  263,  296,  297,  298,  299,
  285,  285,  284,  297,  304,  287,  274,  264,  278,  286,
  264,  268,  306,  281,  282,  283,  298,  299,  275,  301,
  302,  303,  287,  274,  306,  275,  304,  284,  285,  297,
  287,  267,  264,  263,  276,  290,  268,  258,  306,  296,
  297,  298,  299,  275,  301,  302,  303,  304,  259,  306,
  287,  264,  284,  276,  289,  287,  275,  264,  261,  262,
  264,  268,  265,  266,  296,  297,  298,  299,  275,  301,
  302,  303,  304,  263,  306,    0,  275,  284,  264,  275,
  287,  264,  264,  257,  258,  264,  268,  261,  262,  296,
  297,  298,  299,  275,  301,  302,  303,  264,  275,  306,
  287,  264,  284,  287,  264,  287,  287,  264,    8,  287,
  275,  268,  287,  117,  296,  297,  298,  299,  275,  301,
  302,  303,  168,   52,  306,   46,   25,  284,  264,  100,
  287,  179,  268,  177,  145,  296,  297,  298,  183,  275,
  297,  298,  299,  304,  301,  302,  303,   15,  284,  306,
  268,  287,   71,  264,   -1,   -1,   -1,  268,   -1,   -1,
   -1,   -1,   -1,  299,  275,  301,  302,  303,  268,   -1,
  306,   -1,   -1,  284,   -1,   -1,  287,   -1,  296,  297,
  298,  299,   -1,  301,  302,  303,  304,   -1,   -1,   -1,
  301,  302,  303,  257,  258,  306,  296,  297,  298,  299,
  264,  301,  302,  303,  304,  269,  270,  271,  272,  273,
   -1,   -1,   -1,  277,  278,  279,  280,   -1,  257,  258,
   -1,   -1,   -1,   -1,  288,  264,  290,   -1,   -1,   -1,
  269,  270,  271,  272,  273,   -1,   -1,   -1,  277,  278,
  279,  280,  257,  258,   -1,   -1,   -1,   -1,  275,  288,
   -1,  290,   -1,   -1,  269,  270,  271,  272,  273,  275,
   -1,   -1,  277,  278,  279,  280,   -1,   -1,  275,  296,
  297,  298,  299,  288,  301,  302,  303,  304,  275,   -1,
  296,  297,  298,  299,   -1,  301,  302,  303,  304,  296,
  297,  298,  299,  275,  301,  302,  303,  304,   -1,  296,
  297,  298,  299,   -1,  301,  302,  303,  304,   -1,   -1,
   -1,  284,   -1,   -1,  296,  297,  298,  299,   -1,  301,
  302,  303,  304,  296,  297,  298,  299,  287,  301,  302,
  303,  304,   -1,   -1,   -1,   -1,  296,  297,  298,  299,
   -1,  301,  302,  303,  304,  296,  297,  298,  299,   -1,
  301,  302,  303,  304,   -1,  306,  296,  297,  298,  299,
   -1,  301,  302,  303,  304,  296,  297,  298,  299,   -1,
   -1,  302,  303,  304,
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
"L_CURBRACE","R_CURBRACE","CLASS","STRUCT","L_SQBRACE","R_SQBRACE","GOTO",
"RETURN","BREAK","CONTINUE","IF","L_BRACE","R_BRACE","ELSE","FOR","WHILE","DO",
"SWITCH","NEW","STRING","BOOLEAN","COMMA","ASSIGN","DOT","SEMICOLON","PRINT",
"COLON","CASE","TUPLE","INCLUDE","UN_MINUS","UN_PLUS","INCR","MUL_ARITHM",
"ADD_ARITHM","MORELESS","EQ","IFX","OR","AND","XOR","POW","UN_ARITHM","NOT",
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
"function : DEFUN TYPE IDENTIFIER L_BRACE param_list R_BRACE codeblock",
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
"class_list : decl",
"class_list : function",
"class_list : decl SEMICOLON class_list",
"class_list : function class_list",
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

//#line 745 "./borzhch.y"

private SymTable topTable = null;
private static FuncTable funcTable = null;
private SymTable structTable = null;

private String mainClass = "Program";
private String currentClass = "Program";

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
//#line 554 "Parser.java"
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
//#line 56 "./borzhch.y"
{ 
        TreeAST.setRoot((NodeAST) val_peek(0).obj); 
     }
break;
case 2:
//#line 61 "./borzhch.y"
{
        topTable = new SymTable(null);
        
        funcTable = new FuncTable();

        structTable = new SymTable(null);

        structTable.pushSymbol("Program", "class");
    }
break;
case 3:
//#line 72 "./borzhch.y"
{ 
           yyval.obj = null; 
    }
break;
case 4:
//#line 75 "./borzhch.y"
{ 
        StatementList list = new StatementList(); 
        list.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) list.addAll((NodeList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 5:
//#line 83 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 6:
//#line 84 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 7:
//#line 85 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 8:
//#line 88 "./borzhch.y"
{
            topTable = new SymTable(topTable);
         }
break;
case 9:
//#line 93 "./borzhch.y"
{
            SymTable oldTable = topTable;
            topTable = oldTable.getPrevious();
            oldTable.setPrevious(null);
            oldTable.clear();
        }
break;
case 10:
//#line 101 "./borzhch.y"
{
        FunctionNode func = null;

        if (isTypeExist(val_peek(5).sval)) {
            func = new FunctionNode(val_peek(4).sval, val_peek(5).sval, currentClass);
        } else {
            String msg = ErrorHelper.unknownType(val_peek(5).sval);
            yyerror(msg);
        }
        if(isIdentifierExist(val_peek(4).sval)) {
          String msg = ErrorHelper.identifierExists(val_peek(4).sval);
          yyerror(msg);
        }

        func.setArguments((NodeList) val_peek(2).obj);
        func.setStatements((StatementList) val_peek(0).obj);

        funcTable.push(func);

        yyval.obj = func;
    }
break;
case 11:
//#line 122 "./borzhch.y"
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
case 12:
//#line 137 "./borzhch.y"
{ yyval.obj = null; }
break;
case 13:
//#line 138 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 14:
//#line 143 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
        }
break;
case 15:
//#line 151 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((NodeList) val_peek(0).obj);
            yyval.obj = node; 
          }
break;
case 16:
//#line 157 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
          }
break;
case 17:
//#line 165 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 18:
//#line 169 "./borzhch.y"
{
        if(isIdentifierExist(val_peek(1).sval)) {
            String msg = ErrorHelper.identifierExists(val_peek(1).sval);
            yyerror(msg);
        }
        structTable.pushSymbol(val_peek(1).sval, "ref");;

        StructDeclarationNode node = new StructDeclarationNode(val_peek(1).sval, (FieldList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 19:
//#line 182 "./borzhch.y"
{
        String identifier = (String) val_peek(1).obj;
        if(isIdentifierExist(identifier)) {
            String msg = ErrorHelper.identifierExists(identifier);
            yyerror(msg);
        }
        structTable.pushSymbol(identifier, "class");

        currentClass = mainClass;
        ClassNode node = new ClassNode(identifier, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 20:
//#line 197 "./borzhch.y"
{
        currentClass = val_peek(0).sval; 
        yyval.obj = val_peek(0).sval;
    }
break;
case 21:
//#line 204 "./borzhch.y"
{
        StatementList node = (StatementList) val_peek(1).obj;
        yyval.obj = node; 
    }
break;
case 22:
//#line 210 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 23:
//#line 215 "./borzhch.y"
{
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 24:
//#line 220 "./borzhch.y"
{
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(2).obj);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 25:
//#line 226 "./borzhch.y"
{
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 26:
//#line 235 "./borzhch.y"
{
        yyval.obj = val_peek(1).obj;
    }
break;
case 27:
//#line 240 "./borzhch.y"
{ yyval.obj = null; }
break;
case 28:
//#line 241 "./borzhch.y"
{
        FieldList node = new FieldList();
        DeclarationNode ldecl = (DeclarationNode) val_peek(2).obj;
        ldecl.isField(true);
        node.add(ldecl);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 29:
//#line 252 "./borzhch.y"
{ 
        if(isIdentifierExist(val_peek(0).sval)) {
          String msg = ErrorHelper.identifierExists(val_peek(0).sval);
          yyerror(msg);
        }
        if(!isTypeExist(val_peek(1).sval)) {
          String msg = ErrorHelper.unknownType(val_peek(1).sval);
          yyerror(msg);
        }

        topTable.pushSymbol(val_peek(0).sval, val_peek(1).sval);
        
        DeclarationNode node = new DeclarationNode(val_peek(0).sval, BOHelper.getType(val_peek(1).sval));
        node.type(BOHelper.getType(val_peek(1).sval));
        yyval.obj = node;  
    }
break;
case 30:
//#line 268 "./borzhch.y"
{
        if(!isTypeExist(val_peek(1).sval)) {
          String msg = ErrorHelper.unknownType(val_peek(1).sval);
          yyerror(msg);
        }
        if(isIdentifierExist(val_peek(0).sval)) {
          String msg = ErrorHelper.identifierExists(val_peek(0).sval);
          yyerror(msg);
        }

        topTable.pushSymbol(val_peek(0).sval, val_peek(1).sval);

        DeclarationNode decl = new DeclarationNode(val_peek(0).sval, val_peek(1).sval);
        yyval.obj = decl;
    }
break;
case 31:
//#line 283 "./borzhch.y"
{
        if(!isTypeExist(val_peek(3).sval)) {
          String msg = ErrorHelper.unknownType(val_peek(3).sval);
          yyerror(msg);
        }
        if(isIdentifierExist(val_peek(0).sval)) {
          String msg = ErrorHelper.identifierExists(val_peek(0).sval);
          yyerror(msg);
        }
        
        topTable.pushSymbol(val_peek(0).sval, "ref", val_peek(3).sval);
                
        DeclarationNode node = new DeclarationNode(val_peek(0).sval, val_peek(3).sval);
        yyval.obj = node;
    }
break;
case 32:
//#line 325 "./borzhch.y"
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
case 33:
//#line 336 "./borzhch.y"
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
case 34:
//#line 361 "./borzhch.y"
{ yyval.obj = null; }
break;
case 35:
//#line 362 "./borzhch.y"
{ 
        StatementList list = new StatementList();
        list.add((NodeAST) val_peek(2).obj);
        if (val_peek(0).obj != null) list.addAll((StatementList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 36:
//#line 368 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 37:
//#line 374 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 38:
//#line 380 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 39:
//#line 389 "./borzhch.y"
{
        yyval.obj = new PrintNode((NodeAST) val_peek(0).obj);
    }
break;
case 40:
//#line 394 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 41:
//#line 395 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 42:
//#line 396 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 43:
//#line 397 "./borzhch.y"
{ 
      if(!isIdentifierExist(val_peek(0).sval)) {
        String msg = ErrorHelper.notDeclared(val_peek(0).sval);
        yyerror(msg);
      }
      yyval.obj = null; 
    }
break;
case 44:
//#line 404 "./borzhch.y"
{ yyval.obj = new ReturnNode((NodeAST) val_peek(0).obj); }
break;
case 45:
//#line 405 "./borzhch.y"
{ yyval.obj = new ReturnNode(null); }
break;
case 46:
//#line 406 "./borzhch.y"
{ yyval.obj = new BreakNode(); }
break;
case 47:
//#line 407 "./borzhch.y"
{ yyval.obj = new ContinueNode(); }
break;
case 48:
//#line 408 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 49:
//#line 412 "./borzhch.y"
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
case 50:
//#line 422 "./borzhch.y"
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
case 51:
//#line 441 "./borzhch.y"
{
        /*arrayref := IDENTIFIER L_SQBRACE exp R_SQBRACE => ArrayElementNode*/
        
        ArrayElementNode index = (ArrayElementNode) val_peek(2).obj;
        NodeAST value = (NodeAST) val_peek(0).obj;
        SetArrayNode node = new SetArrayNode(index, value);
        yyval.obj = node;
    }
break;
case 52:
//#line 452 "./borzhch.y"
{
        VariableNode var = new VariableNode(val_peek(2).sval, topTable.getSymbolType(val_peek(2).sval));
        DotOpNode dot = new DotOpNode(var, (NodeAST) val_peek(0).obj);
        /*((IDotNode) node).setStructName(var.strType());*/
        /*node.type(node.getLastNode().type());*/
        GetFieldNode node = new GetFieldNode(var, dot.reduce());
        yyval.obj = node;
    }
break;
case 53:
//#line 463 "./borzhch.y"
{
        FieldNode node = new FieldNode(val_peek(0).sval);
        yyval.obj = node;
    }
break;
case 54:
//#line 467 "./borzhch.y"
{
        FieldNode field = new FieldNode(val_peek(2).sval);
        DotOpNode node = new DotOpNode(field, (NodeAST) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 55:
//#line 475 "./borzhch.y"
{
        IfNode node = new IfNode((NodeAST) val_peek(3).obj, (StatementList) val_peek(1).obj, (IfNode) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 56:
//#line 481 "./borzhch.y"
{
      yyval.obj = null;
    }
break;
case 57:
//#line 484 "./borzhch.y"
{
      yyval.obj = (IfNode) val_peek(0).obj;
    }
break;
case 58:
//#line 487 "./borzhch.y"
{
      IfNode node = new IfNode(null, (StatementList) val_peek(0).obj, null);
      yyval.obj = node;
    }
break;
case 59:
//#line 493 "./borzhch.y"
{
        NodeAST decl = (NodeAST) val_peek(6).obj;
        NodeAST counter = (NodeAST) val_peek(4).obj;
        NodeAST step = (NodeAST) val_peek(2).obj;
        NodeAST statements = (NodeAST) val_peek(0).obj; 
        ForNode node = new ForNode(decl, counter, step, statements);
        yyval.obj = node;
    }
break;
case 60:
//#line 501 "./borzhch.y"
{
        WhileNode node = new WhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 61:
//#line 505 "./borzhch.y"
{
        DoWhileNode node = new DoWhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(5).obj);
        yyval.obj = node;
    }
break;
case 62:
//#line 511 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(8).obj, (StatementList) val_peek(5).obj, (StatementList) val_peek(1).obj);
        yyval.obj = node;
      }
break;
case 63:
//#line 516 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(4).obj, (StatementList) val_peek(1).obj, null);
        yyval.obj = node;
      }
break;
case 64:
//#line 523 "./borzhch.y"
{ yyval.obj = null; }
break;
case 65:
//#line 524 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((StatementList) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 66:
//#line 531 "./borzhch.y"
{
        CaseNode node = new CaseNode(val_peek(2).ival, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 67:
//#line 538 "./borzhch.y"
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
//#line 549 "./borzhch.y"
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
case 69:
//#line 560 "./borzhch.y"
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
case 70:
//#line 574 "./borzhch.y"
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
case 71:
//#line 585 "./borzhch.y"
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
case 72:
//#line 599 "./borzhch.y"
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
case 73:
//#line 610 "./borzhch.y"
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
case 74:
//#line 621 "./borzhch.y"
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
case 75:
//#line 632 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 76:
//#line 633 "./borzhch.y"
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
case 77:
//#line 642 "./borzhch.y"
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
case 78:
//#line 651 "./borzhch.y"
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
case 79:
//#line 660 "./borzhch.y"
{ 
        if(!isTypeExist(val_peek(0).sval)) {
            String msg = ErrorHelper.unknownType(val_peek(0).sval);
            yyerror(msg);
        }
        yyval.obj = new NewObjectNode(val_peek(0).sval); 
    }
break;
case 80:
//#line 667 "./borzhch.y"
{
        if(!isIdentifierExist(val_peek(3).sval)) {
            String msg = ErrorHelper.notDeclared(val_peek(3).sval);
            yyerror(msg);
        }
        FunctionCallNode node = new FunctionCallNode(val_peek(3).sval, (StatementList) val_peek(1).obj);
        yyval.obj = node;
    }
break;
case 81:
//#line 675 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 82:
//#line 676 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 83:
//#line 677 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 84:
//#line 678 "./borzhch.y"
{ yyval.obj = new IntegerNode(val_peek(0).ival); }
break;
case 85:
//#line 679 "./borzhch.y"
{ yyval.obj = new FloatNode((float)val_peek(0).dval); }
break;
case 86:
//#line 680 "./borzhch.y"
{ yyval.obj = new StringNode(val_peek(0).sval); }
break;
case 87:
//#line 681 "./borzhch.y"
{ yyval.obj = new BooleanNode(val_peek(0).ival); }
break;
case 88:
//#line 682 "./borzhch.y"
{ 
	    if(!isIdentifierExist(val_peek(0).sval)) {
            String msg = ErrorHelper.notDeclared(val_peek(0).sval);
            yyerror(msg);
        }
        yyval.obj = new VariableNode(val_peek(0).sval, topTable.getSymbolType(val_peek(0).sval)); 
    }
break;
case 89:
//#line 692 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 90:
//#line 696 "./borzhch.y"
{
        if(!isIdentifierExist(val_peek(3).sval)) {
          String msg = ErrorHelper.notDeclared(val_peek(3).sval);
          yyerror(msg);
        }

        VariableNode var = new VariableNode(val_peek(3).sval, topTable.getBaseType(val_peek(3).sval));
        var.type(BOType.REF);
        ArrayElementNode node = new ArrayElementNode(var, (NodeAST) val_peek(1).obj);
        yyval.obj = node;
    }
break;
case 91:
//#line 709 "./borzhch.y"
{
            TupleNode node = new TupleNode((StatementList) val_peek(1).obj);
            yyval.obj = node;
           }
break;
case 92:
//#line 715 "./borzhch.y"
{
          yyval.obj = null;
        }
break;
case 93:
//#line 718 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 94:
//#line 723 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 95:
//#line 731 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 96:
//#line 737 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
//#line 1566 "Parser.java"
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
