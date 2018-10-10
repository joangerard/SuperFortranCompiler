jflex src/LexicalAnalizer.flex
javac -cp src src/LexicalAnalizer.java
cd src
jar cvmf META-INFO/MANIFEST.MF part1.jar *.class
mv part1.jar ../dist/part1.jar
cd ..
java -jar dist/part1.jar test/$1
