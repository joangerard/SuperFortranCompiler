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
    System.out.println("");
    System.out.println("      LEXICAL ANALIZER   ");
    System.out.println("___________________________");
%init}

%{//adding Java code (methods, inner classes, ...)
ProcessInterface process = new MapOrder();
IdentifierListInterface identifierList = new IdentifierList(process);
PrinterInterface symbolPrinter = new SymbolPrinter();

%}

%eof{//code to execute after scanning
   System.out.println(""); 
   System.out.println("Identifiers");
   System.out.println(identifierList.toString());
%eof}

////////////////////////////////
//Extended Regular Expressions//
////////////////////////////////

//-00005.05E-05
WhiteSpace     = [ \t\f]

EndLine = "\r"?"\n"

//DecimalNumber = [-+]?[0-9]*(\.[0-9]+E[+-][0-9]+)?
Number = [0-9]|([1-9][0-9]*)
NumberLetter = [a-zA-Z][0-9]|[0-9][a-zA-Z]|[a-zA-Z][a-zA-Z]
AnyChar = [0-9a-zA-Z]*
NumberLetterCombination = {AnyChar}{NumberLetter}+{AnyChar}
StartWithZero = 0+[0-9]+
NotNumber = {StartWithZero} | {NumberLetterCombination} | [a-zA-Z]

//ProgName
BeginProgram = "BEGINPROG"
ProgramName = [A-Z]([a-z]+[A-Z0-9]*|[A-Z0-9]*[a-z]+)+([A-Za-z0-9])*
NotProgramName = [a-z]([A-Za-z0-9]+) | {MixedSpecialChar}
EndProg = "ENDPROG"

//Variables
Variables = "VARIABLES"
VarName = [a-z][a-z0-9]*
SpecialChars = [_]
AtLeastOneUppercase = {AnyChar}[A-Z]+{AnyChar}
MixedSpecialChar = {AnyChar}{SpecialChars}+{AnyChar}
DigitsAtThebegining = [0-9]+{AnyChar}
NotVarName = {MixedSpecialChar} | {AtLeastOneUppercase} | {DigitsAtThebegining}

//Operators
GreaterThan = ">"
GreaterEqualThan = ">="
LessThan = "<"
LessEqualThan = "<="
Equal = "="
Different = "<>"
Comma = ","

//Comments
LongCommentInit = "/*"
LongCommentEnd = "*/"
ShortComment = "//"

//Instruction
If = "IF"
While = "WHILE"
Do = "DO"
EndWhile = "ENDWHILE"
For = "FOR"
To = "TO"
EndFor = "ENDFOR"
Print = "PRINT"
Read = "READ"
Then = "THEN"
EndIf = "ENDIF"
Else = "ELSE"
Not = "NOT"

//Operations
Plus = "+"
Minus = "-"
Times = "*"
Divide = "/"

//Binary Operations
And = "AND"
Or = "OR"

//Parenthesis
OpenParenthesis = "("
CloseParenthesis = ")"

//////////
//States//
//////////

%xstate YYINITIAL, BEGINPROGRAMSTATE, LONGCOMMENTSTATE, SHORTCOMMENTSTATE

%%//Identification of tokens and actions

<YYINITIAL> {
    //ProgName
    {BeginProgram}              {symbolPrinter.print(LexicalUnit.BEGINPROG, yyline, yycolumn, yytext());
                                    yybegin(BEGINPROGRAMSTATE);}
    {EndProg}                   {symbolPrinter.print(LexicalUnit.ENDPROG, yyline, yycolumn, yytext());}

    //Variables
    {NotVarName}                {}
    {Variables}                 {symbolPrinter.print(LexicalUnit.VARIABLES, yyline, yycolumn, yytext());}
    {VarName}                   {symbolPrinter.print(LexicalUnit.VARNAME, yyline, yycolumn, yytext());
                                identifierList.add(yytext(), yyline + 1);}

    //Operators
    {GreaterThan}               {symbolPrinter.print(LexicalUnit.GT, yyline, yycolumn, yytext());}
    {GreaterEqualThan}          {symbolPrinter.print(LexicalUnit.GEQ, yyline, yycolumn, yytext());}
    {LessThan}                  {symbolPrinter.print(LexicalUnit.LT, yyline, yycolumn, yytext());}
    {LessEqualThan}             {symbolPrinter.print(LexicalUnit.LEQ, yyline, yycolumn, yytext());}
    {Equal}                     {symbolPrinter.print(LexicalUnit.EQ, yyline, yycolumn, yytext());}
    {Different}                 {symbolPrinter.print(LexicalUnit.NEQ, yyline, yycolumn, yytext());}
    {Comma}                     {symbolPrinter.print(LexicalUnit.COMMA, yyline, yycolumn, yytext());}

    //Comment
    {LongCommentInit}           {yybegin(LONGCOMMENTSTATE);}
    {ShortComment}              {yybegin(SHORTCOMMENTSTATE);}

    //EndLine
    {EndLine}                   {symbolPrinter.print(LexicalUnit.ENDLINE, yyline, yycolumn,"\\n");}

    //Instructions
    {If}                        {symbolPrinter.print(LexicalUnit.IF, yyline, yycolumn, yytext());}
    {While}                     {symbolPrinter.print(LexicalUnit.WHILE, yyline, yycolumn, yytext());}
    {Do}                        {symbolPrinter.print(LexicalUnit.DO, yyline, yycolumn, yytext());}
    {EndWhile}                  {symbolPrinter.print(LexicalUnit.ENDWHILE, yyline, yycolumn, yytext());}
    {For}                       {symbolPrinter.print(LexicalUnit.FOR, yyline, yycolumn, yytext());}
    {To}                        {symbolPrinter.print(LexicalUnit.TO, yyline, yycolumn, yytext());}
    {EndFor}                    {symbolPrinter.print(LexicalUnit.ENDFOR, yyline, yycolumn, yytext());}
    {Print}                     {symbolPrinter.print(LexicalUnit.PRINT, yyline, yycolumn, yytext());}
    {Read}                      {symbolPrinter.print(LexicalUnit.READ, yyline, yycolumn, yytext());}
    {Then}                      {symbolPrinter.print(LexicalUnit.THEN, yyline, yycolumn, yytext());}
    {EndIf}                     {symbolPrinter.print(LexicalUnit.ENDIF, yyline, yycolumn, yytext());}
    {Else}                      {symbolPrinter.print(LexicalUnit.ELSE, yyline, yycolumn, yytext());}
    {Not}                       {symbolPrinter.print(LexicalUnit.NOT, yyline, yycolumn, yytext());}
    
    //Operations
    {Plus}                      {symbolPrinter.print(LexicalUnit.PLUS, yyline, yycolumn, yytext());}
    {Minus}                     {symbolPrinter.print(LexicalUnit.MINUS, yyline, yycolumn, yytext());}
    {Times}                     {symbolPrinter.print(LexicalUnit.TIMES, yyline, yycolumn, yytext());}
    {Divide}                    {symbolPrinter.print(LexicalUnit.DIVIDE, yyline, yycolumn, yytext());}

    //Binary Operations
    {And}                       {symbolPrinter.print(LexicalUnit.AND, yyline, yycolumn, yytext());}
    {Or}                        {symbolPrinter.print(LexicalUnit.OR, yyline, yycolumn, yytext());}

    //Parenthesis
    {OpenParenthesis}           {symbolPrinter.print(LexicalUnit.LPAREN, yyline, yycolumn, yytext());}
    {CloseParenthesis}          {symbolPrinter.print(LexicalUnit.RPAREN, yyline, yycolumn, yytext());}

    {NotNumber}                 {}
    {Number}                    {symbolPrinter.print(LexicalUnit.NUMBER, yyline, yycolumn, yytext());}

    /* whitespace */
    {WhiteSpace}                { /* ignore */ }
    .                           {}
}

<BEGINPROGRAMSTATE> {
    {NotProgramName}            {yybegin(YYINITIAL);}
    {ProgramName}$             {symbolPrinter.print(LexicalUnit.PROGNAME, yyline, yycolumn, yytext());
                                yybegin(YYINITIAL);}
    /* whitespace */
    {WhiteSpace}                { /* ignore */ }
    .                           {yybegin(YYINITIAL);}
}

<LONGCOMMENTSTATE> {
    {LongCommentEnd}            {yybegin(YYINITIAL);}
    .                           {}
    {EndLine}                   {}
}

<SHORTCOMMENTSTATE> {
    {EndLine}                   {yybegin(YYINITIAL);}
    .                           {}
}