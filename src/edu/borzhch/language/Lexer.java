/* The following code was generated by JFlex 1.6.0 */

package edu.borzhch.language;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.6.0
 * from the specification file <tt>./src/edu/borzhch/language/borzhch.flex</tt>
 */
public class Lexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int STRING_DQUOTED = 2;
  public static final int STRING_SQUOTED = 4;
  public static final int COMMENT = 6;

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
    "\11\0\1\3\1\2\1\0\1\3\1\1\22\0\1\3\1\40\1\61"+
    "\1\42\2\0\1\43\1\62\1\55\1\56\1\36\1\45\1\50\1\45"+
    "\1\12\1\4\1\10\11\11\1\51\1\52\1\41\1\37\1\41\1\0"+
    "\1\42\32\6\1\57\1\63\1\60\1\46\1\5\1\0\1\20\1\27"+
    "\1\35\1\33\1\16\1\17\1\26\1\32\1\23\1\6\1\34\1\21"+
    "\1\6\1\24\1\25\1\30\1\6\1\14\1\22\1\13\1\15\1\6"+
    "\1\31\1\47\2\6\1\53\1\44\1\54\54\0\1\6\12\0\1\6"+
    "\4\0\1\6\5\0\27\6\1\0\37\6\1\0\u01ca\6\4\0\14\6"+
    "\16\0\5\6\7\0\1\6\1\0\1\6\201\0\5\6\1\0\2\6"+
    "\2\0\4\6\1\0\1\6\6\0\1\6\1\0\3\6\1\0\1\6"+
    "\1\0\24\6\1\0\123\6\1\0\213\6\10\0\246\6\1\0\46\6"+
    "\2\0\1\6\7\0\47\6\110\0\33\6\5\0\3\6\55\0\53\6"+
    "\25\0\12\7\4\0\2\6\1\0\143\6\1\0\1\6\17\0\2\6"+
    "\7\0\2\6\12\7\3\6\2\0\1\6\20\0\1\6\1\0\36\6"+
    "\35\0\131\6\13\0\1\6\16\0\12\7\41\6\11\0\2\6\4\0"+
    "\1\6\5\0\26\6\4\0\1\6\11\0\1\6\3\0\1\6\27\0"+
    "\31\6\107\0\23\6\121\0\66\6\3\0\1\6\22\0\1\6\7\0"+
    "\12\6\4\0\12\7\1\0\20\6\4\0\10\6\2\0\2\6\2\0"+
    "\26\6\1\0\7\6\1\0\1\6\3\0\4\6\3\0\1\6\20\0"+
    "\1\6\15\0\2\6\1\0\3\6\4\0\12\7\2\6\23\0\6\6"+
    "\4\0\2\6\2\0\26\6\1\0\7\6\1\0\2\6\1\0\2\6"+
    "\1\0\2\6\37\0\4\6\1\0\1\6\7\0\12\7\2\0\3\6"+
    "\20\0\11\6\1\0\3\6\1\0\26\6\1\0\7\6\1\0\2\6"+
    "\1\0\5\6\3\0\1\6\22\0\1\6\17\0\2\6\4\0\12\7"+
    "\25\0\10\6\2\0\2\6\2\0\26\6\1\0\7\6\1\0\2\6"+
    "\1\0\5\6\3\0\1\6\36\0\2\6\1\0\3\6\4\0\12\7"+
    "\1\0\1\6\21\0\1\6\1\0\6\6\3\0\3\6\1\0\4\6"+
    "\3\0\2\6\1\0\1\6\1\0\2\6\3\0\2\6\3\0\3\6"+
    "\3\0\14\6\26\0\1\6\25\0\12\7\25\0\10\6\1\0\3\6"+
    "\1\0\27\6\1\0\20\6\3\0\1\6\32\0\2\6\6\0\2\6"+
    "\4\0\12\7\25\0\10\6\1\0\3\6\1\0\27\6\1\0\12\6"+
    "\1\0\5\6\3\0\1\6\40\0\1\6\1\0\2\6\4\0\12\7"+
    "\1\0\2\6\22\0\10\6\1\0\3\6\1\0\51\6\2\0\1\6"+
    "\20\0\1\6\21\0\2\6\4\0\12\7\12\0\6\6\5\0\22\6"+
    "\3\0\30\6\1\0\11\6\1\0\1\6\2\0\7\6\37\0\12\7"+
    "\21\0\60\6\1\0\2\6\14\0\7\6\11\0\12\7\47\0\2\6"+
    "\1\0\1\6\2\0\2\6\1\0\1\6\2\0\1\6\6\0\4\6"+
    "\1\0\7\6\1\0\3\6\1\0\1\6\1\0\1\6\2\0\2\6"+
    "\1\0\4\6\1\0\2\6\11\0\1\6\2\0\5\6\1\0\1\6"+
    "\11\0\12\7\2\0\4\6\40\0\1\6\37\0\12\7\26\0\10\6"+
    "\1\0\44\6\33\0\5\6\163\0\53\6\24\0\1\6\12\7\6\0"+
    "\6\6\4\0\4\6\3\0\1\6\3\0\2\6\7\0\3\6\4\0"+
    "\15\6\14\0\1\6\1\0\12\7\6\0\46\6\1\0\1\6\5\0"+
    "\1\6\2\0\53\6\1\0\u014d\6\1\0\4\6\2\0\7\6\1\0"+
    "\1\6\1\0\4\6\2\0\51\6\1\0\4\6\2\0\41\6\1\0"+
    "\4\6\2\0\7\6\1\0\1\6\1\0\4\6\2\0\17\6\1\0"+
    "\71\6\1\0\4\6\2\0\103\6\45\0\20\6\20\0\125\6\14\0"+
    "\u026c\6\2\0\21\6\1\0\32\6\5\0\113\6\6\0\10\6\7\0"+
    "\15\6\1\0\4\6\16\0\22\6\16\0\22\6\16\0\15\6\1\0"+
    "\3\6\17\0\64\6\43\0\1\6\4\0\1\6\3\0\12\7\46\0"+
    "\12\7\6\0\130\6\10\0\51\6\1\0\1\6\5\0\106\6\12\0"+
    "\37\6\47\0\12\7\36\6\2\0\5\6\13\0\54\6\25\0\7\6"+
    "\10\0\12\7\46\0\27\6\11\0\65\6\53\0\12\7\6\0\12\7"+
    "\15\0\1\6\135\0\57\6\21\0\7\6\4\0\12\7\51\0\36\6"+
    "\15\0\2\6\12\7\54\6\32\0\44\6\34\0\12\7\3\0\3\6"+
    "\12\7\44\6\153\0\4\6\1\0\4\6\3\0\2\6\11\0\300\6"+
    "\100\0\u0116\6\2\0\6\6\2\0\46\6\2\0\6\6\2\0\10\6"+
    "\1\0\1\6\1\0\1\6\1\0\1\6\1\0\37\6\2\0\65\6"+
    "\1\0\7\6\1\0\1\6\3\0\3\6\1\0\7\6\3\0\4\6"+
    "\2\0\6\6\4\0\15\6\5\0\3\6\1\0\7\6\164\0\1\6"+
    "\15\0\1\6\20\0\15\6\145\0\1\6\4\0\1\6\2\0\12\6"+
    "\1\0\1\6\3\0\5\6\6\0\1\6\1\0\1\6\1\0\1\6"+
    "\1\0\4\6\1\0\13\6\2\0\4\6\5\0\5\6\4\0\1\6"+
    "\64\0\2\6\u0a7b\0\57\6\1\0\57\6\1\0\205\6\6\0\4\6"+
    "\3\0\2\6\14\0\46\6\1\0\1\6\5\0\1\6\2\0\70\6"+
    "\7\0\1\6\20\0\27\6\11\0\7\6\1\0\7\6\1\0\7\6"+
    "\1\0\7\6\1\0\7\6\1\0\7\6\1\0\7\6\1\0\7\6"+
    "\120\0\1\6\u01d5\0\2\6\52\0\5\6\5\0\2\6\4\0\126\6"+
    "\6\0\3\6\1\0\132\6\1\0\4\6\5\0\51\6\3\0\136\6"+
    "\21\0\33\6\65\0\20\6\u0200\0\u19b6\6\112\0\u51cd\6\63\0\u048d\6"+
    "\103\0\56\6\2\0\u010d\6\3\0\20\6\12\7\2\6\24\0\57\6"+
    "\20\0\37\6\2\0\106\6\61\0\11\6\2\0\147\6\2\0\4\6"+
    "\1\0\36\6\2\0\2\6\105\0\13\6\1\0\3\6\1\0\4\6"+
    "\1\0\27\6\35\0\64\6\16\0\62\6\34\0\12\7\30\0\6\6"+
    "\3\0\1\6\4\0\12\7\34\6\12\0\27\6\31\0\35\6\7\0"+
    "\57\6\34\0\1\6\12\7\6\0\5\6\1\0\12\6\12\7\5\6"+
    "\1\0\51\6\27\0\3\6\1\0\10\6\4\0\12\7\6\0\27\6"+
    "\3\0\1\6\3\0\62\6\1\0\1\6\3\0\2\6\2\0\5\6"+
    "\2\0\1\6\1\0\1\6\30\0\3\6\2\0\13\6\7\0\3\6"+
    "\14\0\6\6\2\0\6\6\2\0\6\6\11\0\7\6\1\0\7\6"+
    "\1\0\53\6\1\0\4\6\4\0\2\6\132\0\43\6\15\0\12\7"+
    "\6\0\u2ba4\6\14\0\27\6\4\0\61\6\u2104\0\u016e\6\2\0\152\6"+
    "\46\0\7\6\14\0\5\6\5\0\1\6\1\0\12\6\1\0\15\6"+
    "\1\0\5\6\1\0\1\6\1\0\2\6\1\0\2\6\1\0\154\6"+
    "\41\0\u016b\6\22\0\100\6\2\0\66\6\50\0\14\6\164\0\5\6"+
    "\1\0\207\6\23\0\12\7\7\0\32\6\6\0\32\6\13\0\131\6"+
    "\3\0\6\6\2\0\6\6\2\0\6\6\2\0\3\6\43\0\14\6"+
    "\1\0\32\6\1\0\23\6\1\0\2\6\1\0\17\6\2\0\16\6"+
    "\42\0\173\6\u0185\0\35\6\3\0\61\6\57\0\40\6\20\0\21\6"+
    "\1\0\10\6\6\0\46\6\12\0\36\6\2\0\44\6\4\0\10\6"+
    "\60\0\236\6\2\0\12\7\126\0\50\6\10\0\64\6\234\0\u0137\6"+
    "\11\0\26\6\12\0\10\6\230\0\6\6\2\0\1\6\1\0\54\6"+
    "\1\0\2\6\3\0\1\6\2\0\27\6\12\0\27\6\11\0\37\6"+
    "\141\0\26\6\12\0\32\6\106\0\70\6\6\0\2\6\100\0\1\6"+
    "\17\0\4\6\1\0\3\6\1\0\33\6\54\0\35\6\3\0\35\6"+
    "\43\0\10\6\1\0\34\6\33\0\66\6\12\0\26\6\12\0\23\6"+
    "\15\0\22\6\156\0\111\6\u03ba\0\65\6\56\0\12\7\23\0\55\6"+
    "\40\0\31\6\7\0\12\7\11\0\44\6\17\0\12\7\20\0\43\6"+
    "\3\0\1\6\14\0\60\6\16\0\4\6\13\0\12\7\1\6\45\0"+
    "\22\6\1\0\31\6\204\0\57\6\21\0\12\7\13\0\10\6\2\0"+
    "\2\6\2\0\26\6\1\0\7\6\1\0\2\6\1\0\5\6\3\0"+
    "\1\6\37\0\5\6\u011e\0\60\6\24\0\2\6\1\0\1\6\10\0"+
    "\12\7\246\0\57\6\121\0\60\6\24\0\1\6\13\0\12\7\46\0"+
    "\53\6\25\0\12\7\u01d6\0\100\6\12\7\25\0\1\6\u01c0\0\71\6"+
    "\u0507\0\u0399\6\u0c67\0\u042f\6\u33d1\0\u0239\6\7\0\37\6\1\0\12\7"+
    "\146\0\36\6\22\0\60\6\20\0\4\6\14\0\12\7\11\0\25\6"+
    "\5\0\23\6\u0370\0\105\6\13\0\1\6\102\0\15\6\u4060\0\2\6"+
    "\u0bfe\0\153\6\5\0\15\6\3\0\11\6\7\0\12\6\u1766\0\125\6"+
    "\1\0\107\6\1\0\2\6\2\0\1\6\2\0\2\6\2\0\4\6"+
    "\1\0\14\6\1\0\1\6\1\0\7\6\1\0\101\6\1\0\4\6"+
    "\2\0\10\6\1\0\7\6\1\0\34\6\1\0\4\6\1\0\5\6"+
    "\1\0\1\6\3\0\7\6\1\0\u0154\6\2\0\31\6\1\0\31\6"+
    "\1\0\37\6\1\0\31\6\1\0\37\6\1\0\31\6\1\0\37\6"+
    "\1\0\31\6\1\0\37\6\1\0\31\6\1\0\10\6\2\0\62\7"+
    "\u1000\0\305\6\u053b\0\4\6\1\0\33\6\1\0\2\6\1\0\1\6"+
    "\2\0\1\6\1\0\12\6\1\0\4\6\1\0\1\6\1\0\1\6"+
    "\6\0\1\6\4\0\1\6\1\0\1\6\1\0\1\6\1\0\3\6"+
    "\1\0\2\6\1\0\1\6\2\0\1\6\1\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\2\6\1\0\1\6\2\0\4\6"+
    "\1\0\7\6\1\0\4\6\1\0\4\6\1\0\1\6\1\0\12\6"+
    "\1\0\21\6\5\0\3\6\1\0\5\6\1\0\21\6\u1144\0\ua6d7\6"+
    "\51\0\u1035\6\13\0\336\6\u3fe2\0\u021e\6\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\u05f0\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\4\0\1\1\2\2\1\3\1\1\1\4\2\5\1\6"+
    "\17\4\1\3\1\7\1\10\1\11\1\12\2\1\1\13"+
    "\1\14\1\4\1\15\1\16\1\17\1\20\1\21\1\22"+
    "\1\23\1\24\1\25\1\26\1\27\1\30\1\31\1\32"+
    "\1\30\1\32\2\33\1\0\1\34\1\0\1\35\13\4"+
    "\1\36\3\4\1\37\5\4\1\40\2\4\1\41\1\42"+
    "\1\11\1\43\1\37\1\4\1\44\1\45\1\46\1\47"+
    "\1\50\1\51\7\4\1\52\1\43\2\4\1\53\1\4"+
    "\1\54\1\10\10\4\1\14\1\55\2\4\1\56\6\4"+
    "\1\57\4\4\1\60\7\4\1\61\1\62\1\4\1\63"+
    "\1\4\1\64\1\4\1\65\1\66\4\4\1\67\2\4"+
    "\1\70\1\4\1\71\1\72";

  private static int [] zzUnpackAction() {
    int [] result = new int[163];
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
    "\0\0\0\64\0\150\0\234\0\320\0\u0104\0\320\0\u0138"+
    "\0\u016c\0\u01a0\0\u01d4\0\u0208\0\320\0\u023c\0\u0270\0\u02a4"+
    "\0\u02d8\0\u030c\0\u0340\0\u0374\0\u03a8\0\u03dc\0\u0410\0\u0444"+
    "\0\u0478\0\u04ac\0\u04e0\0\u0514\0\u0548\0\u057c\0\u057c\0\u05b0"+
    "\0\320\0\u05e4\0\u0618\0\320\0\320\0\u064c\0\320\0\320"+
    "\0\320\0\320\0\320\0\320\0\320\0\320\0\320\0\320"+
    "\0\320\0\u0680\0\320\0\u06b4\0\u06e8\0\u071c\0\320\0\u0750"+
    "\0\u0784\0\320\0\u01d4\0\u07b8\0\u07ec\0\u0820\0\u0854\0\u0888"+
    "\0\u08bc\0\u08f0\0\u0924\0\u0958\0\u098c\0\u09c0\0\u09f4\0\u01a0"+
    "\0\u0a28\0\u0a5c\0\u0a90\0\u01a0\0\u0ac4\0\u0af8\0\u0b2c\0\u0b60"+
    "\0\u0b94\0\u01a0\0\u0bc8\0\u0bfc\0\320\0\320\0\320\0\320"+
    "\0\320\0\u0c30\0\320\0\320\0\320\0\320\0\320\0\320"+
    "\0\u0c64\0\u0c98\0\u0ccc\0\u0d00\0\u0d34\0\u0d68\0\u0d9c\0\u01a0"+
    "\0\u01a0\0\u0dd0\0\u0e04\0\u01a0\0\u0e38\0\u01a0\0\u01a0\0\u0e6c"+
    "\0\u0ea0\0\u0ed4\0\u0f08\0\u0f3c\0\u0f70\0\u0fa4\0\u0fd8\0\u01a0"+
    "\0\u01a0\0\u100c\0\u1040\0\u01a0\0\u1074\0\u10a8\0\u10dc\0\u1110"+
    "\0\u1144\0\u1178\0\u01a0\0\u11ac\0\u11e0\0\u1214\0\u1248\0\u01a0"+
    "\0\u127c\0\u12b0\0\u12e4\0\u1318\0\u134c\0\u1380\0\u13b4\0\u01a0"+
    "\0\u01a0\0\u13e8\0\u01a0\0\u141c\0\u01a0\0\u1450\0\u01a0\0\u01a0"+
    "\0\u1484\0\u14b8\0\u14ec\0\u1520\0\u01a0\0\u1554\0\u1588\0\u01a0"+
    "\0\u15bc\0\u01a0\0\u01a0";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[163];
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
    "\1\5\1\6\2\7\1\10\1\11\1\12\1\5\1\13"+
    "\1\14\1\15\1\16\1\17\1\12\1\20\1\21\1\22"+
    "\1\12\1\23\1\24\1\25\1\26\1\27\1\30\1\31"+
    "\1\32\1\12\1\33\1\12\1\34\1\35\1\36\1\37"+
    "\1\40\1\41\1\42\1\43\1\44\1\45\1\46\1\47"+
    "\1\50\1\51\1\52\1\53\1\54\1\55\1\56\1\57"+
    "\1\60\1\61\1\5\1\62\2\5\56\62\1\63\1\62"+
    "\1\64\1\65\2\5\57\65\1\63\1\66\36\67\1\70"+
    "\25\67\66\0\1\7\65\0\1\71\31\0\1\72\33\0"+
    "\1\12\4\0\23\12\11\0\1\12\22\0\4\12\1\0"+
    "\23\12\11\0\1\12\24\0\2\73\1\74\61\0\2\14"+
    "\1\74\57\0\4\12\1\0\1\12\1\75\1\76\20\12"+
    "\11\0\1\12\22\0\4\12\1\0\3\12\1\77\17\12"+
    "\11\0\1\12\22\0\4\12\1\0\6\12\1\100\14\12"+
    "\11\0\1\12\22\0\4\12\1\0\2\12\1\101\2\12"+
    "\1\102\1\103\3\12\1\104\10\12\11\0\1\12\22\0"+
    "\4\12\1\0\11\12\1\105\11\12\11\0\1\12\22\0"+
    "\4\12\1\0\1\106\15\12\1\107\4\12\11\0\1\12"+
    "\22\0\4\12\1\0\4\12\1\110\4\12\1\111\11\12"+
    "\11\0\1\12\22\0\4\12\1\0\3\12\1\112\6\12"+
    "\1\113\10\12\11\0\1\12\22\0\4\12\1\0\1\12"+
    "\1\114\21\12\11\0\1\12\22\0\4\12\1\0\12\12"+
    "\1\115\10\12\11\0\1\12\22\0\4\12\1\0\1\12"+
    "\1\116\10\12\1\117\10\12\11\0\1\12\22\0\4\12"+
    "\1\0\1\12\1\120\21\12\11\0\1\12\22\0\4\12"+
    "\1\0\17\12\1\121\3\12\11\0\1\12\22\0\4\12"+
    "\1\0\12\12\1\122\10\12\11\0\1\12\22\0\4\12"+
    "\1\0\5\12\1\123\4\12\1\124\10\12\11\0\1\12"+
    "\52\0\1\125\64\0\1\126\63\0\1\127\67\0\1\130"+
    "\64\0\1\131\25\0\4\12\1\0\12\12\1\132\10\12"+
    "\11\0\1\12\14\0\1\62\2\0\56\62\1\0\1\62"+
    "\14\0\1\133\1\134\7\0\1\135\34\0\1\136\2\0"+
    "\1\65\2\0\57\65\15\0\1\133\1\134\7\0\1\135"+
    "\35\0\1\137\5\0\1\140\57\0\1\71\1\6\1\7"+
    "\61\71\10\0\2\74\60\0\4\12\1\0\2\12\1\141"+
    "\20\12\11\0\1\12\22\0\4\12\1\0\15\12\1\142"+
    "\5\12\11\0\1\12\22\0\4\12\1\0\1\143\22\12"+
    "\11\0\1\12\22\0\4\12\1\0\7\12\1\144\13\12"+
    "\11\0\1\12\22\0\4\12\1\0\11\12\1\145\11\12"+
    "\11\0\1\12\22\0\4\12\1\0\6\12\1\146\14\12"+
    "\11\0\1\12\22\0\4\12\1\0\12\12\1\147\10\12"+
    "\11\0\1\12\22\0\4\12\1\0\1\12\1\150\21\12"+
    "\11\0\1\12\22\0\4\12\1\0\20\12\1\151\2\12"+
    "\11\0\1\12\22\0\4\12\1\0\1\12\1\152\21\12"+
    "\11\0\1\12\22\0\4\12\1\0\10\12\1\153\12\12"+
    "\11\0\1\12\22\0\4\12\1\0\1\154\21\12\1\155"+
    "\11\0\1\12\22\0\4\12\1\0\16\12\1\156\4\12"+
    "\11\0\1\12\22\0\4\12\1\0\1\157\22\12\11\0"+
    "\1\12\22\0\4\12\1\0\1\160\22\12\11\0\1\12"+
    "\22\0\4\12\1\0\3\12\1\161\17\12\11\0\1\12"+
    "\22\0\4\12\1\0\12\12\1\162\10\12\11\0\1\12"+
    "\22\0\4\12\1\0\10\12\1\163\1\12\1\164\10\12"+
    "\11\0\1\12\22\0\4\12\1\0\10\12\1\165\12\12"+
    "\11\0\1\12\22\0\4\12\1\0\7\12\1\166\13\12"+
    "\11\0\1\12\22\0\4\12\1\0\11\12\1\167\11\12"+
    "\11\0\1\12\22\0\4\12\1\0\1\12\1\170\21\12"+
    "\11\0\1\12\22\0\4\12\1\0\3\12\1\171\17\12"+
    "\11\0\1\12\22\0\4\12\1\0\6\12\1\172\14\12"+
    "\11\0\1\12\22\0\4\12\1\0\2\12\1\173\20\12"+
    "\11\0\1\12\22\0\4\12\1\0\3\12\1\174\17\12"+
    "\11\0\1\12\22\0\4\12\1\0\22\12\1\175\11\0"+
    "\1\12\22\0\4\12\1\0\7\12\1\141\13\12\11\0"+
    "\1\12\22\0\4\12\1\0\5\12\1\176\15\12\11\0"+
    "\1\12\22\0\4\12\1\0\2\12\1\177\5\12\1\200"+
    "\12\12\11\0\1\12\22\0\4\12\1\0\1\201\22\12"+
    "\11\0\1\12\22\0\4\12\1\0\6\12\1\202\14\12"+
    "\11\0\1\12\22\0\4\12\1\0\12\12\1\203\10\12"+
    "\11\0\1\12\22\0\4\12\1\0\5\12\1\204\15\12"+
    "\11\0\1\12\22\0\4\12\1\0\6\12\1\154\14\12"+
    "\11\0\1\12\22\0\4\12\1\0\11\12\1\205\11\12"+
    "\11\0\1\12\22\0\4\12\1\0\22\12\1\206\11\0"+
    "\1\12\22\0\4\12\1\0\6\12\1\207\14\12\11\0"+
    "\1\12\22\0\4\12\1\0\3\12\1\210\17\12\11\0"+
    "\1\12\22\0\4\12\1\0\1\211\22\12\11\0\1\12"+
    "\22\0\4\12\1\0\3\12\1\154\17\12\11\0\1\12"+
    "\22\0\4\12\1\0\1\12\1\212\21\12\11\0\1\12"+
    "\22\0\4\12\1\0\1\213\22\12\11\0\1\12\22\0"+
    "\4\12\1\0\1\154\22\12\11\0\1\12\22\0\4\12"+
    "\1\0\22\12\1\214\11\0\1\12\22\0\4\12\1\0"+
    "\11\12\1\215\11\12\11\0\1\12\22\0\4\12\1\0"+
    "\22\12\1\216\11\0\1\12\22\0\4\12\1\0\2\12"+
    "\1\217\20\12\11\0\1\12\22\0\4\12\1\0\21\12"+
    "\1\220\1\12\11\0\1\12\22\0\4\12\1\0\1\221"+
    "\22\12\11\0\1\12\22\0\4\12\1\0\3\12\1\222"+
    "\17\12\11\0\1\12\22\0\4\12\1\0\3\12\1\223"+
    "\17\12\11\0\1\12\22\0\4\12\1\0\10\12\1\224"+
    "\12\12\11\0\1\12\22\0\4\12\1\0\11\12\1\225"+
    "\11\12\11\0\1\12\22\0\4\12\1\0\10\12\1\226"+
    "\12\12\11\0\1\12\22\0\4\12\1\0\1\227\22\12"+
    "\11\0\1\12\22\0\4\12\1\0\13\12\1\154\7\12"+
    "\11\0\1\12\22\0\4\12\1\0\17\12\1\230\3\12"+
    "\11\0\1\12\22\0\4\12\1\0\20\12\1\231\2\12"+
    "\11\0\1\12\22\0\4\12\1\0\20\12\1\232\2\12"+
    "\11\0\1\12\22\0\4\12\1\0\11\12\1\233\11\12"+
    "\11\0\1\12\22\0\4\12\1\0\12\12\1\234\10\12"+
    "\11\0\1\12\22\0\4\12\1\0\3\12\1\235\17\12"+
    "\11\0\1\12\22\0\4\12\1\0\2\12\1\236\20\12"+
    "\11\0\1\12\22\0\4\12\1\0\2\12\1\237\20\12"+
    "\11\0\1\12\22\0\4\12\1\0\11\12\1\240\11\12"+
    "\11\0\1\12\22\0\4\12\1\0\1\12\1\241\21\12"+
    "\11\0\1\12\22\0\4\12\1\0\3\12\1\242\17\12"+
    "\11\0\1\12\22\0\4\12\1\0\3\12\1\243\17\12"+
    "\11\0\1\12\14\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[5616];
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
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\4\0\1\11\1\1\1\11\5\1\1\11\23\1\1\11"+
    "\2\1\2\11\1\1\13\11\1\1\1\11\3\1\1\11"+
    "\1\1\1\0\1\11\1\0\31\1\5\11\1\1\6\11"+
    "\103\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[163];
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
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
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
  private static final int BUFSIZE = 8192;
  StringBuilder sb = new StringBuilder(BUFSIZE);
  
  public int Yyline() {
    return yyline + 1;
  }

  public int Yycolumn() {
    return yycolumn + 1;
  }
  private Parser yyparser = new Parser();
  public Lexer(java.io.Reader r, Parser yyparser) {
    this(r);
    this.yyparser = yyparser;
  }


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public Lexer(java.io.Reader in) {
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
    while (i < 2462) {
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
    int totalRead = 0;
    while (totalRead < requested) {
      int numRead = zzReader.read(zzBuffer, zzEndRead + totalRead, requested - totalRead);
      if (numRead == -1) {
        break;
      }
      totalRead += numRead;
    }

    if (totalRead > 0) {
      zzEndRead += totalRead;
      if (totalRead == requested) { /* possibly more input available */
        if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        }
      }
      return false;
    }

    // totalRead = 0: End of stream
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
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public int yylex() throws java.io.IOException {
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
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
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

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 1: 
          { throw new Error("Illegal character <"+
                                   yytext()+">");
          }
        case 59: break;
        case 2: 
          { /* ignore */
          }
        case 60: break;
        case 3: 
          { yyparser.yylval = new ParserVal(yytext()); return Parser.MUL_ARITHM;
          }
        case 61: break;
        case 4: 
          { yyparser.yylval = new ParserVal(yytext());
    return Parser.IDENTIFIER;
          }
        case 62: break;
        case 5: 
          { yyparser.yylval = new ParserVal(Integer.parseInt(yytext()));
    return Parser.INTEGER;
          }
        case 63: break;
        case 6: 
          { return Parser.DOT;
          }
        case 64: break;
        case 7: 
          { return Parser.ASSIGN;
          }
        case 65: break;
        case 8: 
          { yyparser.yylval = new ParserVal(yytext()); return Parser.NOT;
          }
        case 66: break;
        case 9: 
          { yyparser.yylval = new ParserVal(yytext()); return Parser.MORELESS;
          }
        case 67: break;
        case 10: 
          { yyparser.yylval = new ParserVal(yytext()); return Parser.INCR;
          }
        case 68: break;
        case 11: 
          { yyparser.yylval = new ParserVal(yytext()); return Parser.ADD_ARITHM;
          }
        case 69: break;
        case 12: 
          { yyparser.yylval = new ParserVal(yytext()); return Parser.XOR;
          }
        case 70: break;
        case 13: 
          { return Parser.COMMA;
          }
        case 71: break;
        case 14: 
          { return Parser.COLON;
          }
        case 72: break;
        case 15: 
          { return Parser.SEMICOLON;
          }
        case 73: break;
        case 16: 
          { return Parser.L_CURBRACE;
          }
        case 74: break;
        case 17: 
          { return Parser.R_CURBRACE;
          }
        case 75: break;
        case 18: 
          { return Parser.L_BRACE;
          }
        case 76: break;
        case 19: 
          { return Parser.R_BRACE;
          }
        case 77: break;
        case 20: 
          { return Parser.L_SQBRACE;
          }
        case 78: break;
        case 21: 
          { return Parser.R_SQBRACE;
          }
        case 79: break;
        case 22: 
          { sb.setLength(0); yybegin(STRING_DQUOTED);
          }
        case 80: break;
        case 23: 
          { sb.setLength(0); yybegin(STRING_SQUOTED);
          }
        case 81: break;
        case 24: 
          { sb.append(yytext());
          }
        case 82: break;
        case 25: 
          { yybegin(YYINITIAL); 
                  yyparser.yylval = new ParserVal(sb.toString()); 
                  return Parser.STRING;
          }
        case 83: break;
        case 26: 
          { sb.append('\\');
          }
        case 84: break;
        case 27: 
          { /*ignore*/
          }
        case 85: break;
        case 28: 
          { yybegin(COMMENT);
          }
        case 86: break;
        case 29: 
          { yyparser.yylval = new ParserVal(Float.parseFloat(yytext()));
    return Parser.FLOAT;
          }
        case 87: break;
        case 30: 
          { return Parser.IF;
          }
        case 88: break;
        case 31: 
          { yyparser.yylval = new ParserVal(yytext()); return Parser.OR;
          }
        case 89: break;
        case 32: 
          { return Parser.DO;
          }
        case 90: break;
        case 33: 
          { return Parser.POW;
          }
        case 91: break;
        case 34: 
          { yyparser.yylval = new ParserVal(yytext()); return Parser.EQ;
          }
        case 92: break;
        case 35: 
          { yyparser.yylval = new ParserVal(yytext()); return Parser.AND;
          }
        case 93: break;
        case 36: 
          { sb.append('\t');
          }
        case 94: break;
        case 37: 
          { sb.append('\r');
          }
        case 95: break;
        case 38: 
          { sb.append('\n');
          }
        case 96: break;
        case 39: 
          { sb.append('\"');
          }
        case 97: break;
        case 40: 
          { sb.append('\'');
          }
        case 98: break;
        case 41: 
          { yybegin(YYINITIAL);
          }
        case 99: break;
        case 42: 
          { return Parser.FOR;
          }
        case 100: break;
        case 43: 
          { yyparser.yylval = new ParserVal(yytext()); return Parser.TYPE;
          }
        case 101: break;
        case 44: 
          { return Parser.NEW;
          }
        case 102: break;
        case 45: 
          { if (yytext().equals("true")) { yyparser.yylval = new ParserVal(1); }
    else { yyparser.yylval = new ParserVal(0); } 
    return Parser.BOOLEAN;
          }
        case 103: break;
        case 46: 
          { return Parser.ELSE;
          }
        case 104: break;
        case 47: 
          { return Parser.GOTO;
          }
        case 105: break;
        case 48: 
          { return Parser.CASE;
          }
        case 106: break;
        case 49: 
          { return Parser.BREAK;
          }
        case 107: break;
        case 50: 
          { return Parser.PRINT;
          }
        case 108: break;
        case 51: 
          { return Parser.WHILE;
          }
        case 109: break;
        case 52: 
          { return Parser.RETURN;
          }
        case 110: break;
        case 53: 
          { return Parser.STRUCT;
          }
        case 111: break;
        case 54: 
          { return Parser.SWITCH;
          }
        case 112: break;
        case 55: 
          { return Parser.INCLUDE;
          }
        case 113: break;
        case 56: 
          { return Parser.DEFUN;
          }
        case 114: break;
        case 57: 
          { return Parser.CONTINUE;
          }
        case 115: break;
        case 58: 
          { return Parser.PROC;
          }
        case 116: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
              { return 0; }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
