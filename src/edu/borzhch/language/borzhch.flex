package edu.borzhch.language;

%%

%public
%class Lexer
%byaccj
%unicode
%line
%column

%{
  StringBuilder sb = new StringBuilder();

  private Parser yyparser;
%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
Whitespace     = {LineTerminator} | [ \t\f]

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment}
TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?

/* identifiers */
Identifier = _?[:letter:][[:letter:][:digit:]]*

/* literals */
IntegerLiteral = 0 | [1-9][0-9]*
FloatLiteral = [0-9]+\.[0-9]*
BooleanLiteral = "true" | "false"

%state STRING_DQUOTED, STRING_SQUOTED

%%

<YYINITIAL> {Whitespace} {/* ignore */}

/* keywords */
<YYINITIAL> {
  "int"     { return Parser.TYPE; }
  "float"   { return Parser.TYPE; }
  "string"  { return Parser.TYPE; }
  "bool"    { return Parser.TYPE; }
  "tuple"   { return Parser.TYPE; }
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
  "defun"     { return Parser.DEFUN; }
  "include"   { return Parser.INCLUDE; }
  "new"       { return Parser.NEW; }
  "struct"    { return Parser.STRUCT; }
}  

/* operators */
<YYINITIAL> {
  "**"          { return Parser.POW; }
  "==" | "!="   { return Parser.EQ; }
  "<=" | ">="   { return Parser.MORELESS; }
  "++" | "--"   { return Parser.INCR; }
  "&&" | "and"  { return Parser.AND; }
  "||" | "or"   { return Parser.OR; }
}

<YYINITIAL> {
  "+" | "-"   { return Parser.ADD_ARITHM; }
  "*" | "/"   { return Parser.MUL_ARITHM; }
  "<" | ">"   { return Parser.MORELESS; }
  "!" | "not" { return Parser.NOT; }
  "^" | "xor" { return Parser.XOR; }
  "="         { return Parser.ASSIGN; }
}

/*delimiters*/
<YYINITIAL> {
  ","   { return Parser.COMMA; }
  "."   { return Parser.DOT; }
  ";"   { return Parser.SEMICOLON; }
  "{"   { return Parser.L_CURBRACE; }
  "}"   { return Parser.R_CURBRACE; }
  "("   { return Parser.L_BRACE; }
  ")"   { return Parser.R_BRACE; }
  "["   { return Parser.L_SQBRACE; }
  "]"   { return Parser.R_SQBRACE; }
}

<YYINITIAL> {BooleanLiteral}    { return Parser.BOOLEAN; }

/* identifiers */
<YYINITIAL> {Identifier}        { return Parser.IDENTIFIER; }

/* literals */
<YYINITIAL> {IntegerLiteral}    { return Parser.INTEGER; }
<YYINITIAL> {FloatLiteral}      { return Parser.FLOAT; }
<YYINITIAL> \"                  { sb.setLength(0); yybegin(STRING_DQUOTED); }
<YYINITIAL> \'                  { sb.setLength(0); yybegin(STRING_SQUOTED); }
<YYINITIAL> {Comment}           {/* ignore */}

<STRING_DQUOTED> {
  \"            { 
                  yybegin(YYINITIAL); 
                  yyparser.yylval = new ParserVal(sb.toString()); 
                  return Parser.STRING; 
                }
  [^\n\r\"\\]+  { sb.append(yytext()); }
  \\t           { sb.append('\t'); }
  \\n           { sb.append('\n'); }

  \\r           { sb.append('\r'); }
  \\\"          { sb.append('\"'); }
  \\            { sb.append('\\'); }
}

<STRING_SQUOTED> {
  \'            { 
                  yybegin(YYINITIAL); 
                  yyparser.yylval = new ParserVal(sb.toString()); 
                  return Parser.STRING; 
                }
  [^\n\r\'\\]+  { sb.append(yytext()); }
  \\t           { sb.append('\t'); }
  \\n           { sb.append('\n'); }

  \\r           { sb.append('\r'); }
  \\\'          { sb.append('\''); }
  \\            { sb.append('\\'); }
}

/* error fallback */
[^]             { throw new Error("Illegal character <"+
                                   yytext()+">"); }
