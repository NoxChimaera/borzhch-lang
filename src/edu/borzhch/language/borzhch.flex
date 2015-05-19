package edu.borzhch.language;

%%

%public
%class Lexer
%byaccj
%unicode
%line
%column

%{
  private static final int BUFSIZE = 8192;
  StringBuilder sb = new StringBuilder(BUFSIZE);
  
  public int Yyline() {
    return yyline + 1;
  }

  public int Yycolumn() {
    return yycolumn + 1;
  }
  private Parser yyparser = new Parser();
  public Lexer(java.io.Reader r, Parser yyparser) {
    this(r);
    this.yyparser = yyparser;
  }
%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
Whitespace     = {LineTerminator} | [ \t\f]

/* comments */
Comment = "//" {InputCharacter}* {LineTerminator}

/* identifiers */
Identifier = _?(([:letter:][[:letter:][:digit:]]*)|([:letter:][[:letter:][:digit:]]*_*[[:letter:][:digit:]]+))

/* literals */
IntegerLiteral = 0 | [1-9][0-9]*
FloatLiteral = [0-9]+\.[0-9]*
BooleanLiteral = "true" | "false"

%state STRING_DQUOTED, STRING_SQUOTED, COMMENT

%%

<YYINITIAL> {Whitespace} {/* ignore */}

/* keywords */
<YYINITIAL> {
  "int"|"float"|"string"|"bool"|"tuple"   { yyparser.yylval = new ParserVal(yytext()); return Parser.TYPE; }
}

<YYINITIAL> {
  "for"       { return Parser.FOR; }
  "while"     { return Parser.WHILE; }
  "do"        { return Parser.DO; }
  "return"    { return Parser.RETURN; }
  "goto"      { return Parser.GOTO; }
  "break"     { return Parser.BREAK; }
  "continue"  { return Parser.CONTINUE; }
  "if"        { return Parser.IF; }
  "else"      { return Parser.ELSE; }
  "switch"    { return Parser.SWITCH;}
  "case"      { return Parser.CASE; }
  "function"  { return Parser.DEFUN; }
  "procedure" { return Parser.PROC; }
  "include"   { return Parser.INCLUDE; }
  "new"       { return Parser.NEW; }
  "struct"    { return Parser.STRUCT; }
  "class"     { return Parser.CLASS; }
} 
 
<YYINITIAL> {
  "print"     { return Parser.PRINT; }
  "input"     { return Parser.INPUT; }
}
/* operators */
<YYINITIAL> {
  "**"          { return Parser.POW; }
  "==" | "!="   { yyparser.yylval = new ParserVal(yytext()); return Parser.EQ; }
  "<=" | ">="   { yyparser.yylval = new ParserVal(yytext()); return Parser.MORELESS; }
  "#" | "@"     { yyparser.yylval = new ParserVal(yytext()); return Parser.INCR; }
  "&&" | "and"  { yyparser.yylval = new ParserVal(yytext()); return Parser.AND; }
  "||" | "or"   { yyparser.yylval = new ParserVal(yytext()); return Parser.OR; }
  "|"           { return Parser.PIPE; }
}

<YYINITIAL> {
  "+" | "-"   { yyparser.yylval = new ParserVal(yytext()); return Parser.ADD_ARITHM;  }
  "*" | "/"   { yyparser.yylval = new ParserVal(yytext()); return Parser.MUL_ARITHM; }
  "<" | ">"   { yyparser.yylval = new ParserVal(yytext()); return Parser.MORELESS; }
  "!" | "not" { yyparser.yylval = new ParserVal(yytext()); return Parser.NOT; }
  "^" | "xor" { yyparser.yylval = new ParserVal(yytext()); return Parser.XOR; }
  "="         { return Parser.ASSIGN; }
}

/*delimiters*/
<YYINITIAL> {
  ","   { return Parser.COMMA; }
  "."   { return Parser.DOT; }
  ":"   { return Parser.COLON; }
  ";"   { return Parser.SEMICOLON; }
  "{"   { return Parser.L_CURBRACE; }
  "}"   { return Parser.R_CURBRACE; }
  "("   { return Parser.L_BRACE; }
  ")"   { return Parser.R_BRACE; }
  "["   { return Parser.L_SQBRACE; }
  "]"   { return Parser.R_SQBRACE; }
}

<YYINITIAL> { 
    "null" {
        return Parser.NULL;
    }
}

<YYINITIAL> {BooleanLiteral}    { 
    if (yytext().equals("true")) { yyparser.yylval = new ParserVal(1); }
    else { yyparser.yylval = new ParserVal(0); } 
    return Parser.BOOLEAN; 
}

/* identifiers */
<YYINITIAL> {Identifier}        { 
    yyparser.yylval = new ParserVal(yytext());
    return Parser.IDENTIFIER; 
}

/* literals */
<YYINITIAL> {IntegerLiteral}    { 
    yyparser.yylval = new ParserVal(Integer.parseInt(yytext()));
    return Parser.INTEGER; 
}
<YYINITIAL> {FloatLiteral}      { 
    yyparser.yylval = new ParserVal(Float.parseFloat(yytext()));
    return Parser.FLOAT; 
}
<YYINITIAL> \"                  { sb.setLength(0); yybegin(STRING_DQUOTED); }
<YYINITIAL> \'                  { sb.setLength(0); yybegin(STRING_SQUOTED); }
<YYINITIAL> {Comment}           {/* ignore */}
<YYINITIAL> \/\*                { yybegin(COMMENT); }

<STRING_DQUOTED> {
  \" {
    yybegin(YYINITIAL);
    yyparser.yylval = new ParserVal(sb.toString());
    return Parser.STRING;
  }
  \\\" { sb.append('\"'); }
  \\t  { sb.append('\t'); }
  \\r  { sb.append('\r'); }
  \\n  { sb.append('\n'); }
  \\   { sb.append('\\'); }
  [^\"\\]+ { sb.append(yytext()); }
}

<STRING_SQUOTED> {
  \'            { 
                  yybegin(YYINITIAL); 
                  yyparser.yylval = new ParserVal(sb.toString()); 
                  return Parser.STRING; 
                }
  \\\' { sb.append('\''); }
  \\t { sb.append('\t'); }
  \\r { sb.append('\r'); }
  \\n  { sb.append('\n'); }
  \\\\ { sb.append('\\'); }
  [^\'\\]+ { sb.append(yytext()); }
}

<COMMENT> {
  \*\/ { yybegin(YYINITIAL); }
  [^] { /*ignore*/ }
}

/* error fallback */
[^] { 
  String msg = String.format("Illegal character <%s> at line %d, column %d in file %s",
    yytext(), Yyline(), Yycolumn(), yyparser.getFilename());
  throw new Error(msg);
}
