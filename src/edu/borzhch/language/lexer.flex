package edu.borzhch.language;

%%

%public
%class Lexer
%byaccj
%unicode
%line
%column
%debug

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
Whitespace     = {LineTerminator} | [ \t\f]

/* comments */
TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?

Comment = {TraditionalComment} | {EndOfLineComment}

Identifier = _?[:letter:][:letter::digit:]*

DecIntegerLiteral = 0 | [1-9][0-9]*
FloatLiteral = [0-9]+\.[0-9]*
BooleanLiteral = "true" | "false"

%state STRING_DQUOTED, STRING_SQUOTED

%%

<YYINITIAL> {Whitespace} {/* ignore */}

/* keywords */
<YYINITIAL> {
  "int"     {}
  "float"   {}
  "string"  {}
  "bool"    {}
  "tuple"   {}
  "struct"  {}
}

<YYINITIAL> {
  "for"       {}
  "while"     {}
  "do"        {}
  "return"    {}
  "goto"      {}
  "break"     {}
  "continue"  {}
  "if"        {}
  "else"      {}
  "switch"    {}
  "case"      {}
  "defun"     {}
  "include"   {}
}  

/* operators */
<YYINITIAL> {
  "**"          {}
  "=="          {}
  "!="          {}
  "<="          {}
  ">="          {}
  "&&" | "and"  {}
  "||" | "or"   {}
}

<YYINITIAL> {
  "+"         {}
  "-"         {}
  "*"         {}
  "/"         {}
  "<"         {}
  ">"         {}
  "!" | "not" {}
  "^" | "xor" {}
  "="         {}
}

/*delimiters*/
<YYINITIAL> {
  ","   {}
  "."   {}
  ";"   {}
  "{"   {}
  "}"   {}
  "("   {}
  ")"   {}
  "["   {}
  "]"   {}
}

/* identifiers */
<YYINITIAL> {Identifier}        {}

/* literals */
<YYINITIAL> {DecIntegerLiteral} {}
<YYINITIAL> {FloatLiteral}      {}
<YYINITIAL> {BooleanLiteral}    {}
<YYINITIAL> \"                { yybegin(STRING_DQUOTED); }
<YYINITIAL> \'                { yybegin(STRING_SQUOTED); }
<YYINITIAL> {Comment}           {/* ignore */}

<STRING_DQUOTED> {
  \"            { yybegin(YYINITIAL); }
  [^\n\r\"\\]+  {}
  \\t           {}
  \\n           {}

  \\r           {}
  \\\"          {}
  \\            {}
}

<STRING_SQUOTED> {
  \'            { yybegin(YYINITIAL); }
  [^\n\r\'\\]+  {}
  \\t           {}
  \\n           {}

  \\r           {}
  \\\'          {}
  \\            {}
}

/* error fallback */
[^]                 { throw new Error("Illegal character <"+
                                       yytext()+">"); }
