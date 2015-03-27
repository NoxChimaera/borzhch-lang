package edu.borzhch.language;

%%

%public
%class Lexer
%byaccj
%unicode
%line
%column
%debug

%{
  StringBuilder sb = new StringBuilder();
%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
Whitespace     = {LineTerminator} | [ \t\f]

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment}
TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?

/* identifiers */
StructIdentifier = _?[:uppercase:][:letter::digit:]*
Identifier = _?[:lowercase:][:letter::digit:]*

/* literals */
IntegerLiteral = 0 | [1-9][0-9]*
FloatLiteral = [0-9]+\.[0-9]*
BooleanLiteral = "true" | "false"

%state STRING_DQUOTED, STRING_SQUOTED

%%

<YYINITIAL> {Whitespace} {/* ignore */}

/* keywords */
<YYINITIAL> {
  "int"     { return Parser.INTEGER; }
  "float"   { return Parser.FLOAT; }
  "string"  { return Parser.STRING; }
  "bool"    { return Parser.BOOLEAN; }
  "tuple"   { return Parser.TUPLE; }
  "struct"  { return Parser.STRUCT; }
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

/* identifiers */
<YYINITIAL> {StructIdentifier}  { return Parser.STRUCT_IDENTIFIER; }
<YYINITIAL> {Identifier}        { return Parser.IDENTIFIER; }

/* literals */
<YYINITIAL> {IntegerLiteral}    { return Parser.INTEGER; }
<YYINITIAL> {FloatLiteral}      { return Parser.FLOAT; }
<YYINITIAL> {BooleanLiteral}    { return Parser.BOOLEAN; }
<YYINITIAL> \"                  { sb.setLength(0); yybegin(STRING_DQUOTED); }
<YYINITIAL> \'                  { sb.setLength(0); yybegin(STRING_SQUOTED); }
<YYINITIAL> {Comment}           {/* ignore */}

<STRING_DQUOTED> {
  \"            { 
                  yybegin(YYINITIAL); 
                  Parser.yylval = new ParserVal(sb.toString()); 
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
                  Parser.yylval = new ParserVal(sb.toString()); 
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
