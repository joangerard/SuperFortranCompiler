//import java_cup.runtime.*; uncommet if you use CUP
import utils.errorhandling.*;

%%// Options of the scanner

%class          LexicalAnalyzer//Name
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

%function execute

////////
//CODE//
////////
%init{//code to execute before scanning
    System.out.println("");
%init}

%{//adding Java code (methods, inner classes, ...)
MapOrderInterface process = new MapOrder();
IdentifierListInterface identifierList = new IdentifierList(process);
ErrorHandlerInterface errorHandler = new ErrorHandler();
ErrorPrinter errorPrinter = new ErrorPrinter(errorHandler);
TokenizerInterface tokenizer = new Tokenizer();
PrinterInterface tokenPrinter = new TokenPrinter(tokenizer);

%}

%eof{//code to execute after scanning
   tokenPrinter.print();
   System.out.println(""); 
   System.out.println("Identifiers");
   System.out.println(identifierList.toString());

    errorPrinter.print();
%eof}

////////////////////////////////
//Extended Regular Expressions//
////////////////////////////////

//-00005.05E-05
WhiteSpace     = [ \t\f]

EndLine = "\r"?"\n"

//DecimalNumber = [-+]?[0-9]*(\.[0-9]+E[+-][0-9]+)?
Number = [0-9]|([1-9][0-9]*)
AnyChar = [0-9a-zA-Z]*
StartWithZero = 0+[0-9]+
NotNumber = {StartWithZero}

//ProgName
BeginProgram = "BEGINPROG"
ProgramName = [A-Z]{AnyChar}[a-z]+{AnyChar}
NotProgramName = [a-z]([A-Za-z0-9]+) | {MixedSpecialChar}
EndProg = "ENDPROG"

//Variables
Variables = "VARIABLES"
VarName = [a-z][a-z0-9]*
SpecialChars = [_\!@#\$%\^&\.\?\|\/\{\}\[\]\`\~\"\'\;]
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
Assignment = ":="
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
    {BeginProgram}              {tokenizer.addToken(LexicalUnit.BEGINPROG, yyline, yycolumn, yytext());
                                    yybegin(BEGINPROGRAMSTATE);}
    {EndProg}                   {tokenizer.addToken(LexicalUnit.ENDPROG, yyline, yycolumn, yytext());}

    //Variables
    {Variables}                 {tokenizer.addToken(LexicalUnit.VARIABLES, yyline, yycolumn, yytext());}
    {VarName}                   {tokenizer.addToken(LexicalUnit.VARNAME, yyline, yycolumn, yytext());
                                identifierList.add(yytext(), yyline + 1);}

    //Operators
    {GreaterThan}               {tokenizer.addToken(LexicalUnit.GT, yyline, yycolumn, yytext());}
    {GreaterEqualThan}          {tokenizer.addToken(LexicalUnit.GEQ, yyline, yycolumn, yytext());}
    {LessThan}                  {tokenizer.addToken(LexicalUnit.LT, yyline, yycolumn, yytext());}
    {LessEqualThan}             {tokenizer.addToken(LexicalUnit.LEQ, yyline, yycolumn, yytext());}
    {Equal}                     {tokenizer.addToken(LexicalUnit.EQ, yyline, yycolumn, yytext());}
    {Assignment}                {tokenizer.addToken(LexicalUnit.ASSIGN, yyline, yycolumn, yytext());}
    {Different}                 {tokenizer.addToken(LexicalUnit.NEQ, yyline, yycolumn, yytext());}
    {Comma}                     {tokenizer.addToken(LexicalUnit.COMMA, yyline, yycolumn, yytext());}

    //Comment
    {LongCommentInit}           {yybegin(LONGCOMMENTSTATE);}
    {ShortComment}              {yybegin(SHORTCOMMENTSTATE);}

    //EndLine
    {EndLine}                   {tokenizer.addToken(LexicalUnit.ENDLINE, yyline, yycolumn,"\\n");}

    //Instructions
    {If}                        {tokenizer.addToken(LexicalUnit.IF, yyline, yycolumn, yytext());}
    {While}                     {tokenizer.addToken(LexicalUnit.WHILE, yyline, yycolumn, yytext());}
    {Do}                        {tokenizer.addToken(LexicalUnit.DO, yyline, yycolumn, yytext());}
    {EndWhile}                  {tokenizer.addToken(LexicalUnit.ENDWHILE, yyline, yycolumn, yytext());}
    {For}                       {tokenizer.addToken(LexicalUnit.FOR, yyline, yycolumn, yytext());}
    {To}                        {tokenizer.addToken(LexicalUnit.TO, yyline, yycolumn, yytext());}
    {EndFor}                    {tokenizer.addToken(LexicalUnit.ENDFOR, yyline, yycolumn, yytext());}
    {Print}                     {tokenizer.addToken(LexicalUnit.PRINT, yyline, yycolumn, yytext());}
    {Read}                      {tokenizer.addToken(LexicalUnit.READ, yyline, yycolumn, yytext());}
    {Then}                      {tokenizer.addToken(LexicalUnit.THEN, yyline, yycolumn, yytext());}
    {EndIf}                     {tokenizer.addToken(LexicalUnit.ENDIF, yyline, yycolumn, yytext());}
    {Else}                      {tokenizer.addToken(LexicalUnit.ELSE, yyline, yycolumn, yytext());}
    {Not}                       {tokenizer.addToken(LexicalUnit.NOT, yyline, yycolumn, yytext());}
    
    //Operations
    {Plus}                      {tokenizer.addToken(LexicalUnit.PLUS, yyline, yycolumn, yytext());}
    {Minus}                     {tokenizer.addToken(LexicalUnit.MINUS, yyline, yycolumn, yytext());}
    {Times}                     {tokenizer.addToken(LexicalUnit.TIMES, yyline, yycolumn, yytext());}
    {Divide}                    {tokenizer.addToken(LexicalUnit.DIVIDE, yyline, yycolumn, yytext());}

    //Binary Operations
    {And}                       {tokenizer.addToken(LexicalUnit.AND, yyline, yycolumn, yytext());}
    {Or}                        {tokenizer.addToken(LexicalUnit.OR, yyline, yycolumn, yytext());}

    //Parenthesis
    {OpenParenthesis}           {tokenizer.addToken(LexicalUnit.LPAREN, yyline, yycolumn, yytext());}
    {CloseParenthesis}          {tokenizer.addToken(LexicalUnit.RPAREN, yyline, yycolumn, yytext());}

    {NotNumber}                 {errorHandler.addError(ErrorType.SYNTAX_ERROR_NUMBER, yyline, yycolumn, yytext());}
    {Number}                    {tokenizer.addToken(LexicalUnit.NUMBER, yyline, yycolumn, yytext());}

    {NotVarName}                {errorHandler.addError(ErrorType.SYNTAX_ERROR_VARNAME, yyline, yycolumn, yytext());}

    /* whitespace */
    {WhiteSpace}                { /* ignore */ }
    .                           {}
}

<BEGINPROGRAMSTATE> {
    {NotProgramName}            {errorHandler.addError(ErrorType.SYNTAX_ERROR_PROGNAME, yyline, yycolumn, yytext());
                                yybegin(YYINITIAL);}
    {ProgramName}$              {tokenizer.addToken(LexicalUnit.PROGNAME, yyline, yycolumn, yytext());
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