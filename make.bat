call jflex ./src/edu/borzhch/language/borzhch.flex
cd ./src/edu/borzhch/language/
call yacc -v -J -Jpackage=edu.borzhch.language ./borzhch.y
PAUSE

