//import java_cup.runtime.*; uncommet if you use CUP

%%// Options of the scanner

%class          LexicalAnalizer//Name
%unicode			//Use unicode
%line				//Use line counter (yyline variable)
%column			//Use character counter by line (yycolumn variable)

//you can use either %cup or %standalone
//   %standalone is for a Scanner which works alone and scan a file
//   %cup is to interact with a CUP parser. In this case, you have to return
//        a Symbol object (defined in the CUP library) for each action.
//        Two constructors:
//                          1. Symbol(int id,int line, int column)
//                          2. Symbol(int id,int line, int column,Object value)
%standalone

////////
//CODE//
////////
%init{//code to execute before scanning
	

%init}

%{//adding Java code (methods, inner classes, ...)
public Symbol match(Symbol symbol) {
    System.out.println(symbol.toString());
    return symbol;
}
%}

%eof{//code to execute after scanning
   System.out.println("Done");
%eof}

////////////////////////////////
//Extended Regular Expressions//
////////////////////////////////

//-00005.05E-05
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

//DecimalNumber = [-+]?[0-9]*(\.[0-9]+E[+-][0-9]+)?
Number = ([1-9][0-9]*)|0

//ProgName
BeginProgram = "BEGINPROG"
ProgramName = [A-Z]([a-z]+[A-Z]*|[A-Z]*[a-z]+)*

Identifier = [A-Za-z][A-Za-z0-9_]*

//Operators
GreaterThan = ">"
GreaterEqualThan = ">="
LessThan = "<"
LessEqualThan = "<="
Equal = "=="
Different = "!="
Negation = "!"

//////////
//States//
//////////

%xstate YYINITIAL, BEGINPROGRAMSTATE

%%//Identification of tokens and actions

<YYINITIAL> {
    {BeginProgram}              {match(new Symbol(LexicalUnit.BEGINPROG,yyline,yycolumn,yytext()));
                                    yybegin(BEGINPROGRAMSTATE);}

    ^{Number}$                  {match(new Symbol(LexicalUnit.NUMBER,yyline,yycolumn,yytext()));}
    //{Identifier}                {match(new Symbol(LexicalUnit.VARNAME,yyline,yycolumn,yytext()));}

    //Operators
    {GreaterThan}               {match(new Symbol(LexicalUnit.GT,yyline,yycolumn,yytext()));}
    {GreaterEqualThan}          {match(new Symbol(LexicalUnit.GEQ,yyline,yycolumn,yytext()));}
    {LessThan}                  {match(new Symbol(LexicalUnit.LT,yyline,yycolumn,yytext()));}
    {LessEqualThan}             {match(new Symbol(LexicalUnit.LEQ,yyline,yycolumn,yytext()));}
    {Equal}                     {match(new Symbol(LexicalUnit.EQ,yyline,yycolumn,yytext()));}
    {Different}                 {match(new Symbol(LexicalUnit.NEQ,yyline,yycolumn,yytext()));}
    {Negation}                  {match(new Symbol(LexicalUnit.NOT,yyline,yycolumn,yytext()));}

    /* whitespace */
    {WhiteSpace}                { /* ignore */ }
    .                           {}
}

<BEGINPROGRAMSTATE> {
    ^{ProgramName}$             {match(new Symbol(LexicalUnit.PROGNAME,yyline,yycolumn,yytext()));
                                yybegin(YYINITIAL);}
}
