# Super Fortran Compiler

A simple compiler for Super Fortran language (A very simple version of Fortran language). This is an 
example for creating your own compiler containing all the necessary steps to generate your 
own compiler. The grammar and 
all language aspects are documented and well defined in .pdf files inside Documents-Manuals/Statements folder.

The complete documentation about the development and the command line options that this project offers 
can be found in folder Documents-Manuals/Documents.

In order to make it run just generate a .jar file and use the command line implicitly used in the documentation.
You can use Gradle as well.

This project is separated in three main parts: Syntax Analyzer, Parser and Semantic Analyzer.

We use JFlex and LLVM.

### Part 1 : Lexical Analyzer ###
Recognize the different lexical units of "Super Fortran" language using Jflex

### Part 2 : Parser ###
Simple decent recursive parser

### Part 3 : Code Generation LLVM ###
Generate LLVM code in order to complete the compiler process.

## Installing / Getting started

### Gradle
Gradle is an open-source build automation system that builds upon the concepts of Apache Ant and Apache Maven and introduces a Groovy-based domain-specific language (DSL) instead of the XML form used by Apache Maven for declaring the project configuration. 

The Java plugin emulates many of the expected Maven lifecycles as tasks in the directed acyclic graph of dependencies for the inputs and outputs of each task. 
#### OSX

##### Using Homebrew

Install homebrew running the following command from the terminal:

```bash
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

Then run brew update to make sure Homebrew is up to date.

```bash
brew update
```

As a safe measure you should run brew doctor to make sure your system is ready to brew. Run the command below and follow any recommendations from brew doctor.

```bash
brew doctor
```

Next, add Homebrew's location to your $PATH in your .bash_profile or .zshrc file.

```bash
export PATH="/usr/local/bin:$PATH"
```

Next, install gradle:

```bash
brew install gradle
```
To test out that gradle was installed run:

```bash
gradle -v

------------------------------------------------------------
Gradle 4.10.2
------------------------------------------------------------

```

### Build

To build the project and generate a jar file, run the following command

```bash
gradle build
```

## Run

To run the project run the following command:

```shell
gradle run -PtestFile=testFile.sf
```

"testFile" argument is the program to be executed by the lexer, this file must be found in "test" directory.


### Deploying / Publishing



## Features


## Configuration

Arguments

#### testFile
Type: `String`  


program to be executed by the lexer, this file must be found in "test" directory

Example:
```bash
gradle run -PtestFile=all_grammar.sf
```

## Contributing



## Links

- Jflex Manual: http://jflex.de/manual.html
- LLVM: https://llvm.org/docs/GettingStarted.html


## Licensing

"The code in this project is licensed under MIT license."

## Authors

* [Joan Gerard](https://github.com/joangerard)
* [Niklaus Geisser](https://github.com/nik1168)
