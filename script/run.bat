javac -encoding UTF-8 -d ..\out -cp .;..\lib\algs4.jar ..\src\proj\*.java ..\src\proj\bhtree\*.java ..\src\proj\kdtree\*.java ..\src\proj\math\*.java

SET file="..\sample\sample1.txt"
IF NOT "%1"=="" (set file="%1")
java -cp ..\out;..\lib\algs4.jar proj.Main --gui benchmark < %file%
pause
