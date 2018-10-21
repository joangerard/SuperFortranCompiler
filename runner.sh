#!/bin/bash

if [ $# -gt 0 ]
then 
    
    jflex src/LexicalAnalyzer.flex
    javac -cp src src/LexicalAnalyzer.java
    cd src
    jar cvmf META-INFO/MANIFEST.MF part1.jar *.class
    mv part1.jar ../dist/part1.jar
    cd ..
    java -jar dist/part1.jar test/$1

else
    echo "

    File name param missing... Please add your .sf file to compile 
    in the test/* directory and compile it like: 

    ./runner.sh counter.sf
    
    "
fi
