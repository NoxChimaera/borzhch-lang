JFLEX = jflex
BYACCJ = yacc -J
JAVA = java

Lexer.java: borzhch.flex
	$(FLEX) borzhch.flex

Parser.java: borzhch.y
	$(BYACCJ) borzhch.y
