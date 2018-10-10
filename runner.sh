jflex src/LexicalAnalizer.flex
javac -cp src src/LexicalAnalizer.java
java -cp src LexicalAnalizer more/inputs/test_texte.txt
