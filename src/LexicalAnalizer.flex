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
public Symbol createSymbol(LexicalUnit lexicalUnit) {
    Symbol symbol = new Symbol(lexicalUnit,yyline,yycolumn,yytext());
    System.out.println(symbol.toString());
    return symbol;
}

public Symbol createSymbol(LexicalUnit lexicalUnit, Object value) {
    Symbol symbol = new Symbol(lexicalUnit,yyline,yycolumn,value);
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
WhiteSpace     = [ \t\f]

EndLine = "\r"?"\n"

//DecimalNumber = [-+]?[0-9]*(\.[0-9]+E[+-][0-9]+)?
Number = [0-9]|[1-9][0-9]*

//ProgName
BeginProgram = "BEGINPROG"
ProgramName = [A-Z]([a-z]+[A-Z]*|[A-Z]*[a-z]+)+([A-Za-z])*
EndProg = "ENDPROG"

//Variables
Variables = "VARIABLES"
VarName = [a-z][a-z0-9_]*

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
    {BeginProgram}              {createSymbol(LexicalUnit.BEGINPROG);
                                    yybegin(BEGINPROGRAMSTATE);}
    {EndProg}                   {createSymbol(LexicalUnit.ENDPROG);}

    //Variables
    {Variables}                 {createSymbol(LexicalUnit.VARIABLES);}
    {VarName}                   {createSymbol(LexicalUnit.VARNAME);}

    //Operators
    {GreaterThan}               {createSymbol(LexicalUnit.GT);}
    {GreaterEqualThan}          {createSymbol(LexicalUnit.GEQ);}
    {LessThan}                  {createSymbol(LexicalUnit.LT);}
    {LessEqualThan}             {createSymbol(LexicalUnit.LEQ);}
    {Equal}                     {createSymbol(LexicalUnit.EQ);}
    {Different}                 {createSymbol(LexicalUnit.NEQ);}
    {Comma}                     {createSymbol(LexicalUnit.COMMA);}

    //Comment
    {LongCommentInit}           {yybegin(LONGCOMMENTSTATE);}
    {ShortComment}              {yybegin(SHORTCOMMENTSTATE);}

    //EndLine
    {EndLine}                   {createSymbol(LexicalUnit.ENDLINE,"\\n");}

    //Instructions
    {If}                        {createSymbol(LexicalUnit.IF);}
    {While}                     {createSymbol(LexicalUnit.WHILE);}
    {Do}                        {createSymbol(LexicalUnit.DO);}
    {EndWhile}                  {createSymbol(LexicalUnit.ENDWHILE);}
    {For}                       {createSymbol(LexicalUnit.FOR);}
    {To}                        {createSymbol(LexicalUnit.TO);}
    {EndFor}                    {createSymbol(LexicalUnit.ENDFOR);}
    {Print}                     {createSymbol(LexicalUnit.PRINT);}
    {Read}                      {createSymbol(LexicalUnit.READ);}
    {Then}                      {createSymbol(LexicalUnit.THEN);}
    {EndIf}                     {createSymbol(LexicalUnit.ENDIF);}
    {Else}                      {createSymbol(LexicalUnit.ELSE);}
    {Not}                       {createSymbol(LexicalUnit.NOT);}
    
    //Operations
    {Plus}                      {createSymbol(LexicalUnit.PLUS);}
    {Minus}                     {createSymbol(LexicalUnit.MINUS);}
    {Times}                     {createSymbol(LexicalUnit.TIMES);}
    {Divide}                    {createSymbol(LexicalUnit.DIVIDE);}

    //Parenthesis
    {OpenParenthesis}           {createSymbol(LexicalUnit.LPAREN);}
    {CloseParenthesis}          {createSymbol(LexicalUnit.RPAREN);}

    {Number}                    {createSymbol(LexicalUnit.NUMBER);}

    /* whitespace */
    {WhiteSpace}                { /* ignore */ }
    .                           {}
}

<BEGINPROGRAMSTATE> {
    {ProgramName}$             {createSymbol(LexicalUnit.PROGNAME);
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