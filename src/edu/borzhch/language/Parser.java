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
    0,   34,    1,    1,    2,    2,    2,   35,   36,   24,
   24,   18,   18,    7,    7,    7,    6,    6,   20,   15,
   29,   31,   30,   28,   28,   28,   28,   11,    9,    9,
    8,    8,   10,   10,   13,   13,   13,   13,   13,   19,
   12,   12,   12,   12,   12,   12,   12,   12,   12,   21,
   21,   21,   22,   23,   16,   17,   17,   17,   14,   14,
   14,   25,   25,   26,   26,   27,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,   32,   32,   32,   32,   32,   32,   33,   33,   33,
    4,    4,    4,    5,    5,
};
final static short yylen[] = {                            2,
    2,    0,    0,    2,    1,    1,    1,    1,    1,    1,
    1,    7,    6,    0,    1,    2,    3,    2,    3,    3,
    3,    1,    3,    1,    1,    3,    2,    3,    0,    3,
    2,    4,    7,    3,    0,    3,    2,    2,    2,    2,
    1,    1,    1,    2,    2,    1,    1,    1,    1,    3,
    3,    3,    4,    3,    6,    0,    2,    2,    9,    5,
    7,   11,    7,    0,    2,    4,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    2,    4,    2,    2,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    4,
    0,    1,    2,    3,    2,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    0,    1,    0,    6,    5,
    7,   10,   11,    0,    0,   22,    0,    0,    4,    0,
    0,    8,   21,    0,    0,   20,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   16,    0,
    0,   27,    9,   23,    0,   28,    0,   13,    0,    0,
    0,   26,   30,   12,    0,    0,    0,   47,   48,    0,
    0,    0,    0,    0,    0,    0,   42,    0,    0,    0,
    0,   49,   43,    0,    0,    0,    0,   17,   32,    0,
    0,    0,   44,    0,   82,   83,    0,    0,   84,   85,
    0,   86,    0,    0,   88,   87,   80,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   19,   38,   37,    0,
    0,   39,    0,    0,    0,    0,    0,   78,    0,   79,
    0,   76,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   36,    0,
    0,    0,   54,   53,    0,   93,   90,   75,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   77,    0,    0,   60,    0,
    0,    0,   94,    0,   55,    0,    0,    0,    0,    0,
    0,   57,   58,    0,    0,   61,    0,    0,   65,   33,
    0,    0,    0,   59,   66,    0,    0,   62,
};
final static short yydgoto[] = {                          1,
    7,    8,   94,  116,  146,   39,   28,   66,   35,   67,
   26,   68,   69,   70,    9,   71,  175,   32,   72,   48,
   73,   95,   96,   30,   76,  179,  180,   33,   11,   23,
   17,   97,   98,    2,   49,   44,
};
final static short yysindex[] = {                         0,
    0, -143, -157, -255, -250, -242,    0, -143,    0,    0,
    0,    0,    0, -239, -226,    0, -190, -186,    0, -193,
 -157,    0,    0, -137, -157,    0, -157, -192, -199, -171,
 -184, -137, -152, -173, -148, -138, -190, -157,    0, -128,
 -137,    0,    0,    0, -157,    0, -190,    0,  204, -199,
 -130,    0,    0,    0, -257, -117, -101,    0,    0, -123,
 -120, -119, -190, -114, -101, -141,    0, -122, -152,  204,
  204,    0,    0, -139, -135,  204, -115,    0,    0, -101,
 -101, -101,    0, -256,    0,    0, -101,  -94,    0,    0,
 -101,    0, -101,  286,    0,    0,    0, -115, -101, -157,
 -101, -112, -101,  286,  -97,  204,    0,    0,    0, -101,
 -101,    0,  -86,  106,  251,  -96,  286,    0,  191,    0,
  274,    0, -101, -101, -101, -101, -101, -101, -101, -101,
  203, -141, -109,  216,  -91,  228, -124,  286,    0,  286,
  286, -218,    0,    0, -101,    0,    0,    0, -101, -118,
 -203, -207, -227, -129, -107,   47, -118, -190, -101, -190,
 -101,  -75,    0,  -74,  251,    0,  -82,  264,    0,  241,
  -89, -101,    0, -198,    0,  -54,  -80,  -51,  -59,  -89,
  118,    0,    0, -257,  -66,    0,  -77,  -61,    0,    0,
 -190,  204,  -46,    0,    0,  204,  -45,    0,
};
final static short yyrindex[] = {                         0,
    0,  218,    0,    0,    0,    0,    0,  218,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -53,    0,    0,    0,  -36,    0,  -53,    0,  -42,    0,
  -29,  -26,    0,    0,    0,    0,    0,    0,    0, -232,
    0,    0,    0,    0,  -36,    0,    0,    0,  -24,  -34,
    0,    0,    0,    0, -253,    0,  -71,    0,    0,    0,
    0,    0,    0,    0,    0, -184,    0,    0,    0, -249,
 -249,    0,    0,  -65,    0, -249,    0,    0,    0,    0,
  -33,    0,    0,  -73,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -173,    0,    0,    0,  -23,    0,    0,
    0,    0,    0,  -44,    0, -249,    0,    0,    0,    0,
    0,    0,    0,    0,  -31,    0, -262,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -41,    0, -261,
 -240,  -48,    0,    0,    0,    0,    0,    0,    0,    2,
   52,   73,   94,   10,  -15, -208,   27,    0,    0,    0,
    0,    0,   86,    0,  -28,    0,  155,    0,    0,    0,
   -6,    0,    0,    0,    0,    0,    0,    0,    0,   -6,
    0,    0,    0,  -21,    0,    0,    0,  180,    0,    0,
    0, -249,    0,    0,    0,  -24,    0,    0,
};
final static short yygindex[] = {                         0,
  254,    0,  -19,    0,   98,  217,  239,   13,  223,  171,
    0,    0,  -64,    0,    0,  109,    0,   42,    0,  -43,
  114,  -47,  -40,   -2,    0,  111,    0,   56,    0,    0,
    0,    0,  -49,    0,  276,  227,
};
final static int YYTABLESIZE=591;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         77,
   14,   74,   15,   54,   11,  108,  109,   16,   75,   80,
   80,  112,   51,   52,   35,   18,   81,   81,   20,  102,
   77,   77,   74,   74,   51,   52,   77,   82,   74,   75,
   75,   31,   89,   29,   50,   75,   31,   34,  118,   29,
   35,  139,   31,   10,   31,  104,   50,   21,   80,   10,
   50,   31,   31,   31,   31,   81,   77,   34,   74,   74,
  114,  115,  117,  143,   22,   75,   74,  119,  123,  124,
  125,  121,   22,  122,   60,   74,   25,  130,   74,  131,
   27,  134,   37,  136,   38,  138,   40,   42,  123,  124,
  140,  141,  123,   74,   74,   74,   52,  130,   74,   12,
   13,  130,   41,  150,  151,  152,  153,  154,  155,  156,
  157,   43,  132,   45,  167,   46,  169,    3,    4,   12,
   13,    5,    6,    3,    4,  165,   77,  195,   74,  166,
  183,  197,   12,  163,  164,   75,   47,   79,   51,  168,
   83,  170,   77,  105,   74,  110,   77,  194,   74,  111,
   99,   75,  181,  100,  101,   75,   84,   85,   86,  103,
   84,   85,   86,  120,  106,  135,  123,  124,  125,  126,
  113,  142,   87,  128,  129,  130,   87,  159,  147,   88,
   89,   90,  161,  137,   89,   90,  130,  171,  123,  124,
  125,  126,  172,  174,   89,   91,  129,  130,   92,   91,
  178,   89,   92,  184,  188,   93,  186,  187,  191,   93,
   89,  192,   89,   89,  193,   46,  196,    3,  198,   89,
   88,   14,   89,   89,   89,   89,   89,   29,   89,   89,
   89,   89,   15,   89,   24,   89,   89,   25,   89,   35,
   18,   91,   40,   92,   81,   34,   95,   89,   89,   89,
   89,   81,   72,   89,   89,   89,   89,   64,   89,   72,
   81,   19,  173,   81,   89,   36,   78,   53,   72,   68,
  133,   72,   81,   81,   81,   81,   68,   73,   81,   81,
   81,   81,  182,   81,   73,   68,   72,   72,   68,  185,
  189,   72,   24,   73,   69,  107,   73,   68,   68,   68,
   68,   69,    0,   68,   68,   68,    0,    0,   68,    0,
   69,   73,    0,   69,    0,    0,   73,    0,    0,   67,
    0,    0,   69,   69,   69,   69,   67,    0,   69,   69,
   69,    0,    0,   69,    0,   67,    0,    0,   67,    0,
   71,    0,  123,  124,  125,  126,    0,   71,   67,   67,
   67,  130,   11,   67,   67,   67,   71,    0,   67,   71,
    0,   70,    0,    0,    0,    0,    0,    0,   70,    0,
   71,   71,   79,  144,   71,   71,   71,   70,    0,   71,
   70,   79,   79,   79,   79,  190,    0,   79,   79,   79,
   79,    0,   70,    0,    0,   70,   70,   70,    0,    0,
   70,  123,  124,  125,  126,    0,    0,  127,  128,  129,
  130,   56,   56,  123,  124,  125,  126,    0,   56,  127,
  128,  129,  130,   56,   56,   56,   56,   56,    0,    0,
    0,   56,   56,   56,   56,    0,   63,   63,    0,    0,
    0,    0,   56,   63,   56,    0,    0,    0,   63,   63,
   63,   63,   63,    0,    0,    0,   63,   63,   63,   63,
   12,   55,    0,    0,    0,  148,    0,   63,    0,   63,
    0,    0,   56,   57,   58,   59,   60,  158,    0,    0,
   61,   62,   63,   64,    0,    0,  123,  124,  125,  126,
  160,   65,  127,  128,  129,  130,    0,    0,  123,  124,
  125,  126,  162,    0,  127,  128,  129,  130,    0,    0,
    0,  123,  124,  125,  126,  177,    0,  127,  128,  129,
  130,    0,    0,  123,  124,  125,  126,    0,    0,  127,
  128,  129,  130,    0,  145,    0,  123,  124,  125,  126,
    0,    0,  127,  128,  129,  130,  123,  124,  125,  126,
  176,    0,  127,  128,  129,  130,    0,    0,    0,  123,
  124,  125,  126,    0,    0,  127,  128,  129,  130,  123,
  124,  125,  126,    0,    0,  127,  128,  129,  130,    0,
  149,  123,  124,  125,  126,    0,    0,  127,  128,  129,
  130,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         49,
    3,   49,  258,   47,  258,   70,   71,  258,   49,  267,
  267,   76,  275,  275,  264,  258,  274,  274,  258,   63,
   70,   71,   70,   71,  287,  287,   76,  285,   76,   70,
   71,  264,  286,   21,  275,   76,   24,   25,  295,   27,
  290,  106,  275,    2,   32,   65,  287,  274,  267,    8,
   38,  284,  285,   41,  287,  274,  106,   45,  106,  268,
   80,   81,   82,  113,  263,  106,  275,   87,  296,  297,
  298,   91,  263,   93,  273,  284,  263,  305,  287,   99,
  274,  101,  275,  103,  284,  105,  258,   32,  296,  297,
  110,  111,  296,  302,  303,  304,   41,  305,  307,  257,
  258,  305,  287,  123,  124,  125,  126,  127,  128,  129,
  130,  264,  100,  287,  158,  264,  160,  261,  262,  257,
  258,  265,  266,  261,  262,  145,  176,  192,  176,  149,
  174,  196,  257,  258,  137,  176,  275,  268,  267,  159,
  258,  161,  192,  285,  192,  285,  196,  191,  196,  285,
  274,  192,  172,  274,  274,  196,  258,  259,  260,  274,
  258,  259,  260,  258,  287,  278,  296,  297,  298,  299,
  286,  258,  274,  303,  304,  305,  274,  287,  275,  281,
  282,  283,  274,  281,  282,  283,  305,  263,  296,  297,
  298,  299,  267,  276,  268,  297,  304,  305,  300,  297,
  290,  275,  300,  258,  264,  307,  287,  259,  275,  307,
  284,  289,  286,  287,  276,  287,  263,    0,  264,  268,
  286,  275,  296,  297,  298,  299,  275,  264,  302,  303,
  304,  305,  275,  307,  264,  284,  285,  264,  287,  264,
  275,  275,  287,  275,  268,  287,  275,  296,  297,  298,
  299,  275,  268,  302,  303,  304,  305,  264,  307,  275,
  284,    8,  165,  287,  286,   27,   50,   45,  284,  268,
  100,  287,  296,  297,  298,  299,  275,  268,  302,  303,
  304,  305,  174,  307,  275,  284,  302,  303,  287,  176,
  180,  307,   17,  284,  268,   69,  287,  296,  297,  298,
  299,  275,   -1,  302,  303,  304,   -1,   -1,  307,   -1,
  284,  302,   -1,  287,   -1,   -1,  307,   -1,   -1,  268,
   -1,   -1,  296,  297,  298,  299,  275,   -1,  302,  303,
  304,   -1,   -1,  307,   -1,  284,   -1,   -1,  287,   -1,
  268,   -1,  296,  297,  298,  299,   -1,  275,  297,  298,
  299,  305,  267,  302,  303,  304,  284,   -1,  307,  287,
   -1,  268,   -1,   -1,   -1,   -1,   -1,   -1,  275,   -1,
  298,  299,  287,  268,  302,  303,  304,  284,   -1,  307,
  287,  296,  297,  298,  299,  268,   -1,  302,  303,  304,
  305,   -1,  299,   -1,   -1,  302,  303,  304,   -1,   -1,
  307,  296,  297,  298,  299,   -1,   -1,  302,  303,  304,
  305,  257,  258,  296,  297,  298,  299,   -1,  264,  302,
  303,  304,  305,  269,  270,  271,  272,  273,   -1,   -1,
   -1,  277,  278,  279,  280,   -1,  257,  258,   -1,   -1,
   -1,   -1,  288,  264,  290,   -1,   -1,   -1,  269,  270,
  271,  272,  273,   -1,   -1,   -1,  277,  278,  279,  280,
  257,  258,   -1,   -1,   -1,  275,   -1,  288,   -1,  290,
   -1,   -1,  269,  270,  271,  272,  273,  275,   -1,   -1,
  277,  278,  279,  280,   -1,   -1,  296,  297,  298,  299,
  275,  288,  302,  303,  304,  305,   -1,   -1,  296,  297,
  298,  299,  275,   -1,  302,  303,  304,  305,   -1,   -1,
   -1,  296,  297,  298,  299,  275,   -1,  302,  303,  304,
  305,   -1,   -1,  296,  297,  298,  299,   -1,   -1,  302,
  303,  304,  305,   -1,  284,   -1,  296,  297,  298,  299,
   -1,   -1,  302,  303,  304,  305,  296,  297,  298,  299,
  287,   -1,  302,  303,  304,  305,   -1,   -1,   -1,  296,
  297,  298,  299,   -1,   -1,  302,  303,  304,  305,  296,
  297,  298,  299,   -1,   -1,  302,  303,  304,  305,   -1,
  307,  296,  297,  298,  299,   -1,   -1,  302,  303,  304,
  305,
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
"class_list : decl",
"class_list : function",
"class_list : decl SEMICOLON class_list",
"class_list : function class_list",
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

//#line 706 "./borzhch.y"

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
//#line 565 "Parser.java"
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
//#line 59 "./borzhch.y"
{ 
        TreeAST.setRoot((NodeAST) val_peek(0).obj); 
     }
break;
case 2:
//#line 64 "./borzhch.y"
{
        topTable = new SymTable(null);
        
        funcTable = new FuncTable();

        structTable = new SymTable(null);

        structTable.pushSymbol("Program", "class");
    }
break;
case 3:
//#line 75 "./borzhch.y"
{ 
           yyval.obj = null; 
    }
break;
case 4:
//#line 78 "./borzhch.y"
{ 
        StatementList list = new StatementList(); 
        list.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) list.addAll((NodeList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 5:
//#line 86 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 6:
//#line 87 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 7:
//#line 88 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 8:
//#line 91 "./borzhch.y"
{
            topTable = new SymTable(topTable);
         }
break;
case 9:
//#line 96 "./borzhch.y"
{
            SymTable oldTable = topTable;
            topTable = oldTable.getPrevious();
            oldTable.setPrevious(null);
            oldTable.clear();
        }
break;
case 10:
//#line 105 "./borzhch.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 11:
//#line 106 "./borzhch.y"
{ 
        if (!isTypeExist(val_peek(0).sval)) {
            yyerror(String.format("can not resolve symbol <%s>\n", val_peek(0).sval));
        }
        yyval.sval = val_peek(0).sval; 
    }
break;
case 12:
//#line 115 "./borzhch.y"
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
//#line 126 "./borzhch.y"
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
//#line 141 "./borzhch.y"
{ yyval.obj = null; }
break;
case 15:
//#line 142 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 16:
//#line 147 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
        }
break;
case 17:
//#line 155 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((NodeList) val_peek(0).obj);
            yyval.obj = node; 
          }
break;
case 18:
//#line 161 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
          }
break;
case 19:
//#line 169 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 20:
//#line 173 "./borzhch.y"
{
        if(isIdentifierExist(val_peek(1).sval)) {
            String msg = ErrorHelper.identifierExists(val_peek(1).sval);
            yyerror(msg);
        }
        structTable.pushSymbol(val_peek(1).sval, "ref");;

        StructDeclarationNode node = new StructDeclarationNode(val_peek(1).sval, (FieldList) val_peek(0).obj, false);
        yyval.obj = node;
    }
break;
case 21:
//#line 186 "./borzhch.y"
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
//#line 201 "./borzhch.y"
{
        funcTable = new FuncTable(funcTable);
        currentClass = val_peek(0).sval; 
        yyval.obj = val_peek(0).sval;
    }
break;
case 23:
//#line 209 "./borzhch.y"
{
        FieldList node = (FieldList) val_peek(1).obj;
        yyval.obj = node; 
    }
break;
case 24:
//#line 215 "./borzhch.y"
{ 
        FieldList node = new FieldList();
        DeclarationNode decl = (DeclarationNode) val_peek(0).obj;
        decl.isField(true);
        node.add((NodeAST) decl);
        yyval.obj = node; 
    }
break;
case 25:
//#line 222 "./borzhch.y"
{
        FieldList node = new FieldList();
        node.add((NodeAST) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 26:
//#line 227 "./borzhch.y"
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
//#line 235 "./borzhch.y"
{
        FieldList node = new FieldList();
        node.add((NodeAST) val_peek(1).obj);
        node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 28:
//#line 244 "./borzhch.y"
{
        yyval.obj = val_peek(1).obj;
    }
break;
case 29:
//#line 249 "./borzhch.y"
{ yyval.obj = null; }
break;
case 30:
//#line 250 "./borzhch.y"
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
//#line 261 "./borzhch.y"
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
//#line 269 "./borzhch.y"
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
//#line 281 "./borzhch.y"
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
case 34:
//#line 294 "./borzhch.y"
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
//#line 319 "./borzhch.y"
{ yyval.obj = null; }
break;
case 36:
//#line 320 "./borzhch.y"
{ 
        StatementList list = new StatementList();
        list.add((NodeAST) val_peek(2).obj);
        if (val_peek(0).obj != null) list.addAll((StatementList) val_peek(0).obj);
        yyval.obj = list;
    }
break;
case 37:
//#line 326 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 38:
//#line 332 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 39:
//#line 338 "./borzhch.y"
{ 
        StatementList node = new StatementList();
        node.add((NodeAST) val_peek(1).obj);
        if (val_peek(0).obj != null) node.addAll((NodeList) val_peek(0).obj);
        yyval.obj = node; 
    }
break;
case 40:
//#line 347 "./borzhch.y"
{
        yyval.obj = new PrintNode((NodeAST) val_peek(0).obj);
    }
break;
case 41:
//#line 352 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 42:
//#line 353 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 43:
//#line 354 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 44:
//#line 355 "./borzhch.y"
{ 
      if(!isIdentifierExist(val_peek(0).sval)) {
        String msg = ErrorHelper.notDeclared(val_peek(0).sval);
        yyerror(msg);
      }
      yyval.obj = null; 
    }
break;
case 45:
//#line 362 "./borzhch.y"
{ yyval.obj = new ReturnNode((NodeAST) val_peek(0).obj); }
break;
case 46:
//#line 363 "./borzhch.y"
{ yyval.obj = new ReturnNode(null); }
break;
case 47:
//#line 364 "./borzhch.y"
{ yyval.obj = new BreakNode(); }
break;
case 48:
//#line 365 "./borzhch.y"
{ yyval.obj = new ContinueNode(); }
break;
case 49:
//#line 366 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 50:
//#line 370 "./borzhch.y"
{
        GetFieldNode get = (GetFieldNode) val_peek(2).obj;
        NodeAST expr = (NodeAST) val_peek(0).obj;
        SetFieldNode node = new SetFieldNode(get, expr);
        yyval.obj = node;
    }
break;
case 51:
//#line 376 "./borzhch.y"
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
case 52:
//#line 395 "./borzhch.y"
{
        /*arrayref := IDENTIFIER L_SQBRACE exp R_SQBRACE => ArrayElementNode*/
        
        ArrayElementNode index = (ArrayElementNode) val_peek(2).obj;
        NodeAST value = (NodeAST) val_peek(0).obj;
        SetArrayNode node = new SetArrayNode(index, value);
        yyval.obj = node;
    }
break;
case 53:
//#line 406 "./borzhch.y"
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
case 54:
//#line 420 "./borzhch.y"
{
        DotOpNode dot = new DotOpNode((NodeAST) val_peek(2).obj, (NodeAST) val_peek(0).obj);
        GetFieldNode node = new GetFieldNode(dot.reduce());
        yyval.obj = node;
    }
break;
case 55:
//#line 452 "./borzhch.y"
{
        IfNode node = new IfNode((NodeAST) val_peek(3).obj, (StatementList) val_peek(1).obj, (IfNode) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 56:
//#line 458 "./borzhch.y"
{
      yyval.obj = null;
    }
break;
case 57:
//#line 461 "./borzhch.y"
{
      yyval.obj = (IfNode) val_peek(0).obj;
    }
break;
case 58:
//#line 464 "./borzhch.y"
{
      IfNode node = new IfNode(null, (StatementList) val_peek(0).obj, null);
      yyval.obj = node;
    }
break;
case 59:
//#line 470 "./borzhch.y"
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
//#line 478 "./borzhch.y"
{
        WhileNode node = new WhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 61:
//#line 482 "./borzhch.y"
{
        DoWhileNode node = new DoWhileNode((NodeAST) val_peek(2).obj, (StatementList) val_peek(5).obj);
        yyval.obj = node;
    }
break;
case 62:
//#line 488 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(8).obj, (StatementList) val_peek(5).obj, (StatementList) val_peek(1).obj);
        yyval.obj = node;
      }
break;
case 63:
//#line 493 "./borzhch.y"
{
        /*TODO: exp should be of INTEGER type*/
        SwitchNode node = new SwitchNode((NodeAST) val_peek(4).obj, (StatementList) val_peek(1).obj, null);
        yyval.obj = node;
      }
break;
case 64:
//#line 500 "./borzhch.y"
{ yyval.obj = null; }
break;
case 65:
//#line 501 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(1).obj);
            node.addAll((StatementList) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 66:
//#line 508 "./borzhch.y"
{
        CaseNode node = new CaseNode(val_peek(2).ival, (StatementList) val_peek(0).obj);
        yyval.obj = node;
    }
break;
case 67:
//#line 515 "./borzhch.y"
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
//#line 526 "./borzhch.y"
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
//#line 537 "./borzhch.y"
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
//#line 551 "./borzhch.y"
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
//#line 562 "./borzhch.y"
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
//#line 576 "./borzhch.y"
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
//#line 587 "./borzhch.y"
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
//#line 598 "./borzhch.y"
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
//#line 609 "./borzhch.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 76:
//#line 610 "./borzhch.y"
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
//#line 619 "./borzhch.y"
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
//#line 628 "./borzhch.y"
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
//#line 637 "./borzhch.y"
{ 
        if(!isTypeExist(val_peek(0).sval)) {
            String msg = ErrorHelper.unknownType(val_peek(0).sval);
            yyerror(msg);
        }
        yyval.obj = new NewObjectNode(val_peek(0).sval); 
    }
break;
case 80:
//#line 644 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 81:
//#line 645 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 82:
//#line 649 "./borzhch.y"
{ yyval.obj = new IntegerNode(val_peek(0).ival); }
break;
case 83:
//#line 650 "./borzhch.y"
{ yyval.obj = new FloatNode((float)val_peek(0).dval); }
break;
case 84:
//#line 651 "./borzhch.y"
{ yyval.obj = new StringNode(val_peek(0).sval); }
break;
case 85:
//#line 652 "./borzhch.y"
{ yyval.obj = new BooleanNode(val_peek(0).ival); }
break;
case 86:
//#line 653 "./borzhch.y"
{ yyval.obj = new NullNode(); }
break;
case 87:
//#line 654 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 88:
//#line 658 "./borzhch.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 89:
//#line 659 "./borzhch.y"
{ 
	    if(!isIdentifierExist(val_peek(0).sval)) {
            String msg = ErrorHelper.notDeclared(val_peek(0).sval);
            yyerror(msg);
        }
        yyval.obj = new VariableNode(val_peek(0).sval, topTable.getSymbolType(val_peek(0).sval)); 
    }
break;
case 90:
//#line 666 "./borzhch.y"
{
        if(!isIdentifierExist(val_peek(3).sval)) {
          String msg = ErrorHelper.notDeclared(val_peek(3).sval);
          yyerror(msg);
        }
        FunctionCallNode node = new FunctionCallNode(val_peek(3).sval, (StatementList) val_peek(1).obj);
        yyval.obj = node;
    }
break;
case 91:
//#line 676 "./borzhch.y"
{
          yyval.obj = null;
        }
break;
case 92:
//#line 679 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
case 93:
//#line 684 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 94:
//#line 692 "./borzhch.y"
{
          StatementList node = new StatementList();
          node.add((NodeAST) val_peek(1).obj);
          node.addAll((NodeList) val_peek(0).obj);
          yyval.obj = node;
        }
break;
case 95:
//#line 698 "./borzhch.y"
{
            StatementList node = new StatementList();
            node.add((NodeAST) val_peek(0).obj);
            yyval.obj = node;
        }
break;
//#line 1530 "Parser.java"
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
