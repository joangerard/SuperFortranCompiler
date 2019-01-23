/* The following code was generated by JFlex 1.7.0 */

//import java_cup.runtime.*; uncommet if you use CUP
import utils.errorhandling.*;
import java.util.List;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.7.0
 * from the specification file <tt>LexicalAnalyzer.flex</tt>
 */
class LexicalAnalyzer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int BEGINPROGRAMSTATE = 2;
  public static final int LONGCOMMENTSTATE = 4;
  public static final int SHORTCOMMENTSTATE = 6;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1,  1,  2,  2,  3, 3
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\1\1\3\1\45\1\46\1\2\22\0\1\1\7\25\1\43"+
    "\1\44\1\34\1\41\1\32\1\42\1\25\1\33\1\4\11\5\1\31"+
    "\1\25\1\30\1\27\1\26\2\25\1\22\1\7\1\17\1\20\1\10"+
    "\1\35\1\11\1\37\1\12\2\17\1\23\1\17\1\13\1\16\1\14"+
    "\1\17\1\15\1\24\1\40\1\17\1\21\1\36\3\17\1\25\1\0"+
    "\4\25\32\6\4\25\6\0\1\45\u1fa2\0\1\45\1\45\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\udfe6\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\4\0\1\1\1\2\1\3\1\4\2\5\1\6\14\7"+
    "\1\10\1\11\1\12\1\1\1\13\1\14\1\15\3\7"+
    "\1\16\1\17\1\20\1\21\1\3\1\22\1\23\1\22"+
    "\2\23\2\24\1\3\1\25\3\7\1\26\3\7\1\27"+
    "\1\30\3\7\1\31\1\32\1\33\1\34\1\35\1\36"+
    "\2\7\1\37\1\7\1\0\1\23\1\0\1\23\3\7"+
    "\1\40\3\7\1\41\1\42\2\7\2\43\5\7\1\44"+
    "\1\7\1\45\2\7\1\46\1\47\1\7\1\50\3\7"+
    "\1\51\1\7\1\52\2\7\1\53\3\7\1\54\3\7"+
    "\1\55\1\7\1\56\1\57";

  private static int [] zzUnpackAction() {
    int [] result = new int[120];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\47\0\116\0\165\0\234\0\234\0\303\0\234"+
    "\0\352\0\u0111\0\u0138\0\u015f\0\u0186\0\u01ad\0\u01d4\0\u01fb"+
    "\0\u0222\0\u0249\0\u0270\0\u0297\0\u02be\0\u02e5\0\u030c\0\u0333"+
    "\0\234\0\u035a\0\u0381\0\234\0\u03a8\0\234\0\u03cf\0\u03f6"+
    "\0\u041d\0\234\0\234\0\234\0\234\0\234\0\234\0\u0444"+
    "\0\u046b\0\u0492\0\u04b9\0\234\0\u04e0\0\u0507\0\352\0\u052e"+
    "\0\u0555\0\u057c\0\u01ad\0\u05a3\0\u05ca\0\u05f1\0\u01ad\0\u01ad"+
    "\0\u0618\0\u063f\0\u0666\0\234\0\234\0\234\0\234\0\234"+
    "\0\234\0\u068d\0\u06b4\0\u01ad\0\u06db\0\u0702\0\u046b\0\u0729"+
    "\0\u0750\0\u0777\0\u079e\0\u07c5\0\u01ad\0\u07ec\0\u0813\0\u083a"+
    "\0\u01ad\0\u01ad\0\u0861\0\u0888\0\u08af\0\234\0\u08d6\0\u08fd"+
    "\0\u0924\0\u094b\0\u0972\0\u01ad\0\u0999\0\u01ad\0\u09c0\0\u09e7"+
    "\0\u01ad\0\234\0\u0a0e\0\u01ad\0\u0a35\0\u0a5c\0\u0a83\0\u01ad"+
    "\0\u0aaa\0\u01ad\0\u0ad1\0\u0af8\0\u01ad\0\u0b1f\0\u0b46\0\u0b6d"+
    "\0\u01ad\0\u0b94\0\u0bbb\0\u0be2\0\u01ad\0\u0c09\0\u01ad\0\u01ad";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[120];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
    "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\16"+
    "\1\24\1\25\1\26\2\16\1\27\1\30\1\31\1\32"+
    "\1\33\1\34\1\35\1\36\1\37\1\40\1\16\1\41"+
    "\1\42\1\43\1\44\1\45\1\46\1\6\1\47\1\6"+
    "\2\46\2\50\1\51\16\52\1\53\7\47\4\52\4\47"+
    "\1\46\1\6\2\54\1\7\1\10\30\54\1\55\10\54"+
    "\2\46\2\10\1\56\1\47\41\10\2\46\52\0\1\10"+
    "\47\0\2\57\17\16\1\27\7\0\4\16\12\0\2\12"+
    "\17\16\1\27\7\0\4\16\12\0\3\13\16\16\1\27"+
    "\7\0\4\16\12\0\4\16\1\60\14\16\1\27\7\0"+
    "\4\16\12\0\7\16\1\61\7\16\1\62\1\16\1\27"+
    "\7\0\4\16\12\0\21\16\1\27\7\0\4\16\12\0"+
    "\21\16\1\27\7\0\1\63\3\16\12\0\12\16\1\64"+
    "\6\16\1\27\7\0\4\16\12\0\11\16\1\65\7\16"+
    "\1\27\7\0\4\16\12\0\4\16\1\66\14\16\1\27"+
    "\7\0\4\16\12\0\11\16\1\67\7\16\1\27\7\0"+
    "\4\16\12\0\12\16\1\70\6\16\1\27\7\0\4\16"+
    "\12\0\16\16\1\71\2\16\1\27\7\0\4\16\12\0"+
    "\7\16\1\72\11\16\1\27\7\0\4\16\12\0\21\73"+
    "\1\27\7\0\4\73\35\0\1\74\45\0\1\75\1\76"+
    "\46\0\1\77\52\0\1\100\1\101\16\0\12\16\1\102"+
    "\6\16\1\27\7\0\4\16\12\0\21\16\1\27\7\0"+
    "\2\16\1\103\1\16\12\0\12\16\1\104\6\16\1\27"+
    "\7\0\2\16\1\105\1\16\12\0\2\50\1\106\16\50"+
    "\1\53\7\0\4\50\12\0\21\107\1\53\7\0\4\107"+
    "\12\0\2\52\1\110\16\52\1\53\7\0\4\52\12\0"+
    "\21\111\1\53\7\0\4\111\41\0\1\47\16\0\1\47"+
    "\47\0\5\16\1\112\13\16\1\27\7\0\4\16\12\0"+
    "\14\16\1\113\4\16\1\27\7\0\4\16\12\0\20\16"+
    "\1\114\1\27\7\0\4\16\12\0\21\16\1\27\7\0"+
    "\3\16\1\115\12\0\6\16\1\116\12\16\1\27\7\0"+
    "\4\16\12\0\16\16\1\117\2\16\1\27\7\0\4\16"+
    "\12\0\11\16\1\120\7\16\1\27\7\0\4\16\12\0"+
    "\14\16\1\121\4\16\1\27\7\0\4\16\12\0\21\73"+
    "\10\0\4\73\12\0\11\16\1\122\7\16\1\27\7\0"+
    "\4\16\12\0\6\16\1\123\12\16\1\27\7\0\4\16"+
    "\12\0\4\16\1\124\14\16\1\27\7\0\4\16\12\0"+
    "\21\106\1\53\7\0\4\106\10\0\1\125\1\126\21\110"+
    "\1\53\7\0\4\110\4\0\2\126\4\0\21\111\10\0"+
    "\4\111\12\0\6\16\1\127\12\16\1\27\7\0\4\16"+
    "\12\0\6\16\1\130\1\16\1\131\10\16\1\27\7\0"+
    "\1\132\1\133\2\16\12\0\4\16\1\134\14\16\1\27"+
    "\7\0\4\16\12\0\7\16\1\135\11\16\1\27\7\0"+
    "\4\16\12\0\14\16\1\136\4\16\1\27\7\0\4\16"+
    "\12\0\6\16\1\137\12\16\1\27\7\0\4\16\12\0"+
    "\17\16\1\140\1\16\1\27\7\0\4\16\12\0\7\16"+
    "\1\141\11\16\1\27\7\0\4\16\11\0\1\142\47\0"+
    "\7\16\1\143\11\16\1\27\7\0\4\16\12\0\21\16"+
    "\1\27\7\0\1\144\3\16\12\0\11\16\1\145\7\16"+
    "\1\27\7\0\4\16\12\0\12\16\1\146\6\16\1\27"+
    "\7\0\4\16\12\0\21\16\1\27\7\0\2\16\1\147"+
    "\1\16\12\0\21\16\1\27\7\0\3\16\1\150\12\0"+
    "\16\16\1\151\2\16\1\27\7\0\4\16\12\0\4\16"+
    "\1\152\14\16\1\27\7\0\4\16\12\0\10\16\1\153"+
    "\10\16\1\27\7\0\4\16\12\0\12\16\1\154\6\16"+
    "\1\27\7\0\4\16\12\0\11\16\1\155\7\16\1\27"+
    "\7\0\4\16\12\0\6\16\1\156\12\16\1\27\7\0"+
    "\4\16\12\0\3\16\1\157\15\16\1\27\7\0\4\16"+
    "\12\0\11\16\1\160\7\16\1\27\7\0\4\16\12\0"+
    "\5\16\1\161\13\16\1\27\7\0\4\16\12\0\17\16"+
    "\1\162\1\16\1\27\7\0\4\16\12\0\17\16\1\163"+
    "\1\16\1\27\7\0\4\16\12\0\12\16\1\164\6\16"+
    "\1\27\7\0\4\16\12\0\4\16\1\165\14\16\1\27"+
    "\7\0\4\16\12\0\4\16\1\166\14\16\1\27\7\0"+
    "\4\16\12\0\5\16\1\167\13\16\1\27\7\0\4\16"+
    "\12\0\20\16\1\170\1\27\7\0\4\16\6\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[3120];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\4\0\2\11\1\1\1\11\20\1\1\11\2\1\1\11"+
    "\1\1\1\11\3\1\6\11\4\1\1\11\17\1\6\11"+
    "\4\1\1\0\1\1\1\0\15\1\1\11\13\1\1\11"+
    "\26\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[120];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true iff the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true iff the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;
  
  /** 
   * The number of occupied positions in zzBuffer beyond zzEndRead.
   * When a lead/high surrogate has been read from the input stream
   * into the final zzBuffer position, this will have a value of 1;
   * otherwise, it will have a value of 0.
   */
  private int zzFinalHighSurrogate = 0;

  /* user code: */
MapOrderInterface process = new MapOrder();
IdentifierListInterface identifierList = new IdentifierList(process);
ErrorHandlerInterface errorHandler = new ErrorHandler();
ErrorPrinter errorPrinter = new ErrorPrinter(errorHandler);
TokenizerInterface tokenizer = new Tokenizer();
PrinterInterface tokenPrinter = new TokenPrinter(tokenizer);
public List<Symbol> getTokens(){
    return tokenizer.getTokens();
}
public Boolean gotAnyError() {
    return errorHandler.getErrors().size() > 0;
}

public void printIdentifiers() {
   System.out.println("");
   System.out.println("Identifiers");

    System.out.println(identifierList.toString());
}

public void printTokens() {
    System.out.println("");
   System.out.println("Tokens");

    tokenPrinter.print();
}



  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  LexicalAnalyzer(java.io.Reader in) {
      System.out.println("");
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x110000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 150) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length - zzFinalHighSurrogate) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzBuffer.length*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
    }

    /* fill the buffer with new input */
    int requested = zzBuffer.length - zzEndRead;
    int numRead = zzReader.read(zzBuffer, zzEndRead, requested);

    /* not supposed to occur according to specification of java.io.Reader */
    if (numRead == 0) {
      throw new java.io.IOException("Reader returned 0 characters. See JFlex examples for workaround.");
    }
    if (numRead > 0) {
      zzEndRead += numRead;
      /* If numRead == requested, we might have requested to few chars to
         encode a full Unicode character. We assume that a Reader would
         otherwise never return half characters. */
      if (numRead == requested) {
        if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        }
      }
      /* potentially more input available */
      return false;
    }

    /* numRead < 0 ==> end of stream */
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * Internal scan buffer is resized down to its initial length, if it has grown.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    zzFinalHighSurrogate = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
    if (zzBuffer.length > ZZ_BUFFERSIZE)
      zzBuffer = new char[ZZ_BUFFERSIZE];
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() {
    if (!zzEOFDone) {
      zzEOFDone = true;
       tokenizer.addToken(LexicalUnit.EOS, yyline, yycolumn, "EOS");
    errorPrinter.print();

    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public int execute() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      int zzCh;
      int zzCharCount;
      for (zzCurrentPosL = zzStartRead  ;
           zzCurrentPosL < zzMarkedPosL ;
           zzCurrentPosL += zzCharCount ) {
        zzCh = Character.codePointAt(zzBufferL, zzCurrentPosL, zzMarkedPosL);
        zzCharCount = Character.charCount(zzCh);
        switch (zzCh) {
        case '\u000B':  // fall through
        case '\u000C':  // fall through
        case '\u0085':  // fall through
        case '\u2028':  // fall through
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn += zzCharCount;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
            zzDoEOF();
        return YYEOF;
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { errorHandler.addError(ErrorType.SYNTAX_ERROR_NOT_RECOGNIZED_CHARACTER, yyline, yycolumn, yytext());
            } 
            // fall through
          case 48: break;
          case 2: 
            { /* ignore */
            } 
            // fall through
          case 49: break;
          case 3: 
            { System.out.print(yytext());
            } 
            // fall through
          case 50: break;
          case 4: 
            { tokenizer.addToken(LexicalUnit.ENDLINE, yyline, yycolumn,"\\n");
            } 
            // fall through
          case 51: break;
          case 5: 
            { tokenizer.addToken(LexicalUnit.NUMBER, yyline, yycolumn, yytext());
            } 
            // fall through
          case 52: break;
          case 6: 
            { tokenizer.addToken(LexicalUnit.VARNAME, yyline, yycolumn, yytext());
                                identifierList.add(yytext(), yyline + 1);
            } 
            // fall through
          case 53: break;
          case 7: 
            { errorHandler.addError(ErrorType.SYNTAX_ERROR_VARNAME, yyline, yycolumn, yytext());
            } 
            // fall through
          case 54: break;
          case 8: 
            { tokenizer.addToken(LexicalUnit.GT, yyline, yycolumn, yytext());
            } 
            // fall through
          case 55: break;
          case 9: 
            { tokenizer.addToken(LexicalUnit.EQ, yyline, yycolumn, yytext());
            } 
            // fall through
          case 56: break;
          case 10: 
            { tokenizer.addToken(LexicalUnit.LT, yyline, yycolumn, yytext());
            } 
            // fall through
          case 57: break;
          case 11: 
            { tokenizer.addToken(LexicalUnit.COMMA, yyline, yycolumn, yytext());
            } 
            // fall through
          case 58: break;
          case 12: 
            { tokenizer.addToken(LexicalUnit.DIVIDE, yyline, yycolumn, yytext());
            } 
            // fall through
          case 59: break;
          case 13: 
            { tokenizer.addToken(LexicalUnit.TIMES, yyline, yycolumn, yytext());
            } 
            // fall through
          case 60: break;
          case 14: 
            { tokenizer.addToken(LexicalUnit.PLUS, yyline, yycolumn, yytext());
            } 
            // fall through
          case 61: break;
          case 15: 
            { tokenizer.addToken(LexicalUnit.MINUS, yyline, yycolumn, yytext());
            } 
            // fall through
          case 62: break;
          case 16: 
            { tokenizer.addToken(LexicalUnit.LPAREN, yyline, yycolumn, yytext());
            } 
            // fall through
          case 63: break;
          case 17: 
            { tokenizer.addToken(LexicalUnit.RPAREN, yyline, yycolumn, yytext());
            } 
            // fall through
          case 64: break;
          case 18: 
            { yybegin(YYINITIAL);
            } 
            // fall through
          case 65: break;
          case 19: 
            { errorHandler.addError(ErrorType.SYNTAX_ERROR_PROGNAME, yyline, yycolumn, yytext());
                                yybegin(YYINITIAL);
            } 
            // fall through
          case 66: break;
          case 20: 
            { 
            } 
            // fall through
          case 67: break;
          case 21: 
            { errorHandler.addError(ErrorType.SYNTAX_ERROR_NUMBER, yyline, yycolumn, yytext());
            } 
            // fall through
          case 68: break;
          case 22: 
            { tokenizer.addToken(LexicalUnit.IF, yyline, yycolumn, yytext());
            } 
            // fall through
          case 69: break;
          case 23: 
            { tokenizer.addToken(LexicalUnit.OR, yyline, yycolumn, yytext());
            } 
            // fall through
          case 70: break;
          case 24: 
            { tokenizer.addToken(LexicalUnit.DO, yyline, yycolumn, yytext());
            } 
            // fall through
          case 71: break;
          case 25: 
            { tokenizer.addToken(LexicalUnit.GEQ, yyline, yycolumn, yytext());
            } 
            // fall through
          case 72: break;
          case 26: 
            { tokenizer.addToken(LexicalUnit.NEQ, yyline, yycolumn, yytext());
            } 
            // fall through
          case 73: break;
          case 27: 
            { tokenizer.addToken(LexicalUnit.LEQ, yyline, yycolumn, yytext());
            } 
            // fall through
          case 74: break;
          case 28: 
            { tokenizer.addToken(LexicalUnit.ASSIGN, yyline, yycolumn, yytext());
            } 
            // fall through
          case 75: break;
          case 29: 
            { yybegin(SHORTCOMMENTSTATE);
            } 
            // fall through
          case 76: break;
          case 30: 
            { yybegin(LONGCOMMENTSTATE);
            } 
            // fall through
          case 77: break;
          case 31: 
            { tokenizer.addToken(LexicalUnit.TO, yyline, yycolumn, yytext());
            } 
            // fall through
          case 78: break;
          case 32: 
            { tokenizer.addToken(LexicalUnit.NOT, yyline, yycolumn, yytext());
            } 
            // fall through
          case 79: break;
          case 33: 
            { tokenizer.addToken(LexicalUnit.AND, yyline, yycolumn, yytext());
            } 
            // fall through
          case 80: break;
          case 34: 
            { tokenizer.addToken(LexicalUnit.FOR, yyline, yycolumn, yytext());
            } 
            // fall through
          case 81: break;
          case 35: 
            // lookahead expression with fixed lookahead length
            zzMarkedPos = Character.offsetByCodePoints
                (zzBufferL, zzStartRead, zzEndRead - zzStartRead, zzMarkedPos, -1);
            { tokenizer.addToken(LexicalUnit.PROGNAME, yyline, yycolumn, yytext());
                                yybegin(YYINITIAL);
            } 
            // fall through
          case 82: break;
          case 36: 
            { tokenizer.addToken(LexicalUnit.ELSE, yyline, yycolumn, yytext());
            } 
            // fall through
          case 83: break;
          case 37: 
            { tokenizer.addToken(LexicalUnit.READ, yyline, yycolumn, yytext());
            } 
            // fall through
          case 84: break;
          case 38: 
            { tokenizer.addToken(LexicalUnit.THEN, yyline, yycolumn, yytext());
            } 
            // fall through
          case 85: break;
          case 39: 
            // lookahead expression with fixed lookahead length
            zzMarkedPos = Character.offsetByCodePoints
                (zzBufferL, zzStartRead, zzEndRead - zzStartRead, zzMarkedPos, -2);
            { tokenizer.addToken(LexicalUnit.PROGNAME, yyline, yycolumn, yytext());
                                yybegin(YYINITIAL);
            } 
            // fall through
          case 86: break;
          case 40: 
            { tokenizer.addToken(LexicalUnit.ENDIF, yyline, yycolumn, yytext());
            } 
            // fall through
          case 87: break;
          case 41: 
            { tokenizer.addToken(LexicalUnit.PRINT, yyline, yycolumn, yytext());
            } 
            // fall through
          case 88: break;
          case 42: 
            { tokenizer.addToken(LexicalUnit.WHILE, yyline, yycolumn, yytext());
            } 
            // fall through
          case 89: break;
          case 43: 
            { tokenizer.addToken(LexicalUnit.ENDFOR, yyline, yycolumn, yytext());
            } 
            // fall through
          case 90: break;
          case 44: 
            { tokenizer.addToken(LexicalUnit.ENDPROG, yyline, yycolumn, yytext());
            } 
            // fall through
          case 91: break;
          case 45: 
            { tokenizer.addToken(LexicalUnit.ENDWHILE, yyline, yycolumn, yytext());
            } 
            // fall through
          case 92: break;
          case 46: 
            { tokenizer.addToken(LexicalUnit.BEGINPROG, yyline, yycolumn, yytext());
                                    yybegin(BEGINPROGRAMSTATE);
            } 
            // fall through
          case 93: break;
          case 47: 
            { tokenizer.addToken(LexicalUnit.VARIABLES, yyline, yycolumn, yytext());
            } 
            // fall through
          case 94: break;
          default:
            zzScanError(ZZ_NO_MATCH);
        }
      }
    }
  }

  /**
   * Runs the scanner on input files.
   *
   * This is a standalone scanner, it will print any unmatched
   * text to System.out unchanged.
   *
   * @param argv   the command line, contains the filenames to run
   *               the scanner on.
   */
  public static void main(String argv[]) {
    if (argv.length == 0) {
      System.out.println("Usage : java LexicalAnalyzer [ --encoding <name> ] <inputfile(s)>");
    }
    else {
      int firstFilePos = 0;
      String encodingName = "UTF-8";
      if (argv[0].equals("--encoding")) {
        firstFilePos = 2;
        encodingName = argv[1];
        try {
          java.nio.charset.Charset.forName(encodingName); // Side-effect: is encodingName valid? 
        } catch (Exception e) {
          System.out.println("Invalid encoding '" + encodingName + "'");
          return;
        }
      }
      for (int i = firstFilePos; i < argv.length; i++) {
        LexicalAnalyzer scanner = null;
        try {
          java.io.FileInputStream stream = new java.io.FileInputStream(argv[i]);
          java.io.Reader reader = new java.io.InputStreamReader(stream, encodingName);
          scanner = new LexicalAnalyzer(reader);
          while ( !scanner.zzAtEOF ) scanner.execute();
        }
        catch (java.io.FileNotFoundException e) {
          System.out.println("File not found : \""+argv[i]+"\"");
        }
        catch (java.io.IOException e) {
          System.out.println("IO error scanning file \""+argv[i]+"\"");
          System.out.println(e);
        }
        catch (Exception e) {
          System.out.println("Unexpected exception:");
          e.printStackTrace();
        }
      }
    }
  }


}