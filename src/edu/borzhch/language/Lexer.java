/* The following code was generated by JFlex 1.6.0 */

package edu.borzhch.language;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.6.0
 * from the specification file <tt>C:/Java/borzhch/src/edu/borzhch/language/lexer.flex</tt>
 */
public class Lexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;
  private static final String ZZ_NL = System.getProperty("line.separator");

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int STRING_DQUOTED = 2;
  public static final int STRING_SQUOTED = 4;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1,  1,  2, 2
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\3\1\2\1\0\1\3\1\1\22\0\1\3\1\40\1\45"+
    "\3\0\1\41\1\46\1\43\1\43\1\5\1\43\1\43\1\43\1\14"+
    "\1\4\1\12\11\13\1\10\1\43\1\40\1\37\1\40\2\0\32\7"+
    "\1\43\1\47\1\43\1\43\1\6\1\0\1\22\1\31\1\33\1\11"+
    "\1\20\1\21\1\30\1\35\1\25\1\7\1\36\1\23\1\7\1\26"+
    "\1\27\1\32\1\7\1\16\1\24\1\15\1\17\1\7\1\34\1\44"+
    "\2\7\1\43\1\42\1\43\54\0\1\7\12\0\1\7\4\0\1\7"+
    "\5\0\27\7\1\0\37\7\1\0\u01ca\7\4\0\14\7\16\0\5\7"+
    "\7\0\1\7\1\0\1\7\201\0\5\7\1\0\2\7\2\0\4\7"+
    "\1\0\1\7\6\0\1\7\1\0\3\7\1\0\1\7\1\0\24\7"+
    "\1\0\123\7\1\0\213\7\10\0\246\7\1\0\46\7\2\0\1\7"+
    "\7\0\47\7\110\0\33\7\5\0\3\7\55\0\53\7\43\0\2\7"+
    "\1\0\143\7\1\0\1\7\17\0\2\7\7\0\2\7\12\0\3\7"+
    "\2\0\1\7\20\0\1\7\1\0\36\7\35\0\131\7\13\0\1\7"+
    "\30\0\41\7\11\0\2\7\4\0\1\7\5\0\26\7\4\0\1\7"+
    "\11\0\1\7\3\0\1\7\27\0\31\7\107\0\23\7\121\0\66\7"+
    "\3\0\1\7\22\0\1\7\7\0\12\7\17\0\20\7\4\0\10\7"+
    "\2\0\2\7\2\0\26\7\1\0\7\7\1\0\1\7\3\0\4\7"+
    "\3\0\1\7\20\0\1\7\15\0\2\7\1\0\3\7\16\0\2\7"+
    "\23\0\6\7\4\0\2\7\2\0\26\7\1\0\7\7\1\0\2\7"+
    "\1\0\2\7\1\0\2\7\37\0\4\7\1\0\1\7\23\0\3\7"+
    "\20\0\11\7\1\0\3\7\1\0\26\7\1\0\7\7\1\0\2\7"+
    "\1\0\5\7\3\0\1\7\22\0\1\7\17\0\2\7\43\0\10\7"+
    "\2\0\2\7\2\0\26\7\1\0\7\7\1\0\2\7\1\0\5\7"+
    "\3\0\1\7\36\0\2\7\1\0\3\7\17\0\1\7\21\0\1\7"+
    "\1\0\6\7\3\0\3\7\1\0\4\7\3\0\2\7\1\0\1\7"+
    "\1\0\2\7\3\0\2\7\3\0\3\7\3\0\14\7\26\0\1\7"+
    "\64\0\10\7\1\0\3\7\1\0\27\7\1\0\20\7\3\0\1\7"+
    "\32\0\2\7\6\0\2\7\43\0\10\7\1\0\3\7\1\0\27\7"+
    "\1\0\12\7\1\0\5\7\3\0\1\7\40\0\1\7\1\0\2\7"+
    "\17\0\2\7\22\0\10\7\1\0\3\7\1\0\51\7\2\0\1\7"+
    "\20\0\1\7\21\0\2\7\30\0\6\7\5\0\22\7\3\0\30\7"+
    "\1\0\11\7\1\0\1\7\2\0\7\7\72\0\60\7\1\0\2\7"+
    "\14\0\7\7\72\0\2\7\1\0\1\7\2\0\2\7\1\0\1\7"+
    "\2\0\1\7\6\0\4\7\1\0\7\7\1\0\3\7\1\0\1\7"+
    "\1\0\1\7\2\0\2\7\1\0\4\7\1\0\2\7\11\0\1\7"+
    "\2\0\5\7\1\0\1\7\25\0\4\7\40\0\1\7\77\0\10\7"+
    "\1\0\44\7\33\0\5\7\163\0\53\7\24\0\1\7\20\0\6\7"+
    "\4\0\4\7\3\0\1\7\3\0\2\7\7\0\3\7\4\0\15\7"+
    "\14\0\1\7\21\0\46\7\1\0\1\7\5\0\1\7\2\0\53\7"+
    "\1\0\u014d\7\1\0\4\7\2\0\7\7\1\0\1\7\1\0\4\7"+
    "\2\0\51\7\1\0\4\7\2\0\41\7\1\0\4\7\2\0\7\7"+
    "\1\0\1\7\1\0\4\7\2\0\17\7\1\0\71\7\1\0\4\7"+
    "\2\0\103\7\45\0\20\7\20\0\125\7\14\0\u026c\7\2\0\21\7"+
    "\1\0\32\7\5\0\113\7\6\0\10\7\7\0\15\7\1\0\4\7"+
    "\16\0\22\7\16\0\22\7\16\0\15\7\1\0\3\7\17\0\64\7"+
    "\43\0\1\7\4\0\1\7\103\0\130\7\10\0\51\7\1\0\1\7"+
    "\5\0\106\7\12\0\37\7\61\0\36\7\2\0\5\7\13\0\54\7"+
    "\25\0\7\7\70\0\27\7\11\0\65\7\122\0\1\7\135\0\57\7"+
    "\21\0\7\7\67\0\36\7\15\0\2\7\12\0\54\7\32\0\44\7"+
    "\51\0\3\7\12\0\44\7\153\0\4\7\1\0\4\7\3\0\2\7"+
    "\11\0\300\7\100\0\u0116\7\2\0\6\7\2\0\46\7\2\0\6\7"+
    "\2\0\10\7\1\0\1\7\1\0\1\7\1\0\1\7\1\0\37\7"+
    "\2\0\65\7\1\0\7\7\1\0\1\7\3\0\3\7\1\0\7\7"+
    "\3\0\4\7\2\0\6\7\4\0\15\7\5\0\3\7\1\0\7\7"+
    "\164\0\1\7\15\0\1\7\20\0\15\7\145\0\1\7\4\0\1\7"+
    "\2\0\12\7\1\0\1\7\3\0\5\7\6\0\1\7\1\0\1\7"+
    "\1\0\1\7\1\0\4\7\1\0\13\7\2\0\4\7\5\0\5\7"+
    "\4\0\1\7\64\0\2\7\u0a7b\0\57\7\1\0\57\7\1\0\205\7"+
    "\6\0\4\7\3\0\2\7\14\0\46\7\1\0\1\7\5\0\1\7"+
    "\2\0\70\7\7\0\1\7\20\0\27\7\11\0\7\7\1\0\7\7"+
    "\1\0\7\7\1\0\7\7\1\0\7\7\1\0\7\7\1\0\7\7"+
    "\1\0\7\7\120\0\1\7\u01d5\0\2\7\52\0\5\7\5\0\2\7"+
    "\4\0\126\7\6\0\3\7\1\0\132\7\1\0\4\7\5\0\51\7"+
    "\3\0\136\7\21\0\33\7\65\0\20\7\u0200\0\u19b6\7\112\0\u51cd\7"+
    "\63\0\u048d\7\103\0\56\7\2\0\u010d\7\3\0\20\7\12\0\2\7"+
    "\24\0\57\7\20\0\37\7\2\0\106\7\61\0\11\7\2\0\147\7"+
    "\2\0\4\7\1\0\36\7\2\0\2\7\105\0\13\7\1\0\3\7"+
    "\1\0\4\7\1\0\27\7\35\0\64\7\16\0\62\7\76\0\6\7"+
    "\3\0\1\7\16\0\34\7\12\0\27\7\31\0\35\7\7\0\57\7"+
    "\34\0\1\7\20\0\5\7\1\0\12\7\12\0\5\7\1\0\51\7"+
    "\27\0\3\7\1\0\10\7\24\0\27\7\3\0\1\7\3\0\62\7"+
    "\1\0\1\7\3\0\2\7\2\0\5\7\2\0\1\7\1\0\1\7"+
    "\30\0\3\7\2\0\13\7\7\0\3\7\14\0\6\7\2\0\6\7"+
    "\2\0\6\7\11\0\7\7\1\0\7\7\1\0\53\7\1\0\4\7"+
    "\4\0\2\7\132\0\43\7\35\0\u2ba4\7\14\0\27\7\4\0\61\7"+
    "\u2104\0\u016e\7\2\0\152\7\46\0\7\7\14\0\5\7\5\0\1\7"+
    "\1\0\12\7\1\0\15\7\1\0\5\7\1\0\1\7\1\0\2\7"+
    "\1\0\2\7\1\0\154\7\41\0\u016b\7\22\0\100\7\2\0\66\7"+
    "\50\0\14\7\164\0\5\7\1\0\207\7\44\0\32\7\6\0\32\7"+
    "\13\0\131\7\3\0\6\7\2\0\6\7\2\0\6\7\2\0\3\7"+
    "\43\0\14\7\1\0\32\7\1\0\23\7\1\0\2\7\1\0\17\7"+
    "\2\0\16\7\42\0\173\7\u0185\0\35\7\3\0\61\7\57\0\40\7"+
    "\20\0\21\7\1\0\10\7\6\0\46\7\12\0\36\7\2\0\44\7"+
    "\4\0\10\7\60\0\236\7\142\0\50\7\10\0\64\7\234\0\u0137\7"+
    "\11\0\26\7\12\0\10\7\230\0\6\7\2\0\1\7\1\0\54\7"+
    "\1\0\2\7\3\0\1\7\2\0\27\7\12\0\27\7\11\0\37\7"+
    "\141\0\26\7\12\0\32\7\106\0\70\7\6\0\2\7\100\0\1\7"+
    "\17\0\4\7\1\0\3\7\1\0\33\7\54\0\35\7\3\0\35\7"+
    "\43\0\10\7\1\0\34\7\33\0\66\7\12\0\26\7\12\0\23\7"+
    "\15\0\22\7\156\0\111\7\u03ba\0\65\7\113\0\55\7\40\0\31\7"+
    "\32\0\44\7\51\0\43\7\3\0\1\7\14\0\60\7\16\0\4\7"+
    "\25\0\1\7\45\0\22\7\1\0\31\7\204\0\57\7\46\0\10\7"+
    "\2\0\2\7\2\0\26\7\1\0\7\7\1\0\2\7\1\0\5\7"+
    "\3\0\1\7\37\0\5\7\u011e\0\60\7\24\0\2\7\1\0\1\7"+
    "\270\0\57\7\121\0\60\7\24\0\1\7\73\0\53\7\u01f5\0\100\7"+
    "\37\0\1\7\u01c0\0\71\7\u0507\0\u0399\7\u0c67\0\u042f\7\u33d1\0\u0239\7"+
    "\7\0\37\7\161\0\36\7\22\0\60\7\20\0\4\7\37\0\25\7"+
    "\5\0\23\7\u0370\0\105\7\13\0\1\7\102\0\15\7\u4060\0\2\7"+
    "\u0bfe\0\153\7\5\0\15\7\3\0\11\7\7\0\12\7\u1766\0\125\7"+
    "\1\0\107\7\1\0\2\7\2\0\1\7\2\0\2\7\2\0\4\7"+
    "\1\0\14\7\1\0\1\7\1\0\7\7\1\0\101\7\1\0\4\7"+
    "\2\0\10\7\1\0\7\7\1\0\34\7\1\0\4\7\1\0\5\7"+
    "\1\0\1\7\3\0\7\7\1\0\u0154\7\2\0\31\7\1\0\31\7"+
    "\1\0\37\7\1\0\31\7\1\0\37\7\1\0\31\7\1\0\37\7"+
    "\1\0\31\7\1\0\37\7\1\0\31\7\1\0\10\7\u1034\0\305\7"+
    "\u053b\0\4\7\1\0\33\7\1\0\2\7\1\0\1\7\2\0\1\7"+
    "\1\0\12\7\1\0\4\7\1\0\1\7\1\0\1\7\6\0\1\7"+
    "\4\0\1\7\1\0\1\7\1\0\1\7\1\0\3\7\1\0\2\7"+
    "\1\0\1\7\2\0\1\7\1\0\1\7\1\0\1\7\1\0\1\7"+
    "\1\0\1\7\1\0\2\7\1\0\1\7\2\0\4\7\1\0\7\7"+
    "\1\0\4\7\1\0\4\7\1\0\1\7\1\0\12\7\1\0\21\7"+
    "\5\0\3\7\1\0\5\7\1\0\21\7\u1144\0\ua6d7\7\51\0\u1035\7"+
    "\13\0\336\7\u3fe2\0\u021e\7\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\u05f0\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\3\0\1\1\2\2\2\3\1\1\22\3\2\1\1\3"+
    "\1\4\1\5\1\3\1\6\3\3\1\2\1\0\1\3"+
    "\1\0\2\3\1\0\2\3\1\0\1\3\2\0\1\3"+
    "\4\0\1\3\11\0\1\3\1\0\1\3\3\0\1\3"+
    "\6\0\1\3\11\0";

  private static int [] zzUnpackAction() {
    int [] result = new int[88];
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
    "\0\0\0\50\0\120\0\170\0\240\0\170\0\310\0\360"+
    "\0\u0118\0\u0140\0\u0168\0\u0190\0\u01b8\0\170\0\u01e0\0\u0208"+
    "\0\u0230\0\u0258\0\u0280\0\u02a8\0\u02d0\0\u02f8\0\u0320\0\u0348"+
    "\0\u0370\0\u0398\0\u03c0\0\u03e8\0\u0410\0\u0438\0\170\0\170"+
    "\0\u0460\0\170\0\u0488\0\u04b0\0\u04d8\0\u0500\0\u0528\0\u0550"+
    "\0\u0190\0\u0578\0\u05a0\0\u05c8\0\u05f0\0\u0618\0\u0640\0\u0668"+
    "\0\u0690\0\u06b8\0\u06e0\0\u0708\0\u0730\0\u0758\0\u0780\0\u07a8"+
    "\0\u07d0\0\u07f8\0\u0820\0\u0848\0\u0870\0\u0898\0\u08c0\0\u08e8"+
    "\0\u0910\0\u0938\0\u0960\0\u0988\0\u09b0\0\u09d8\0\u0a00\0\u0a28"+
    "\0\u0a50\0\u0a78\0\u0aa0\0\u0ac8\0\u0af0\0\u0b18\0\u0b40\0\u0b68"+
    "\0\u0b90\0\u0bb8\0\u0be0\0\u0c08\0\u0c30\0\u0c58\0\u0c80\0\u0ca8";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[88];
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
    "\1\4\1\5\2\6\1\7\1\10\1\11\1\12\1\4"+
    "\1\13\1\14\1\15\1\16\1\17\1\20\1\12\1\21"+
    "\1\22\1\23\1\12\1\24\1\25\1\26\1\12\1\27"+
    "\1\30\1\12\1\31\1\32\2\12\2\33\1\34\1\35"+
    "\1\16\1\36\1\37\1\40\1\4\1\41\2\4\42\41"+
    "\1\42\1\41\1\43\1\44\2\4\43\44\1\42\1\45"+
    "\52\0\1\6\51\0\1\46\1\47\47\0\1\16\51\0"+
    "\1\12\1\0\1\12\3\0\22\12\5\0\1\12\13\0"+
    "\2\12\3\0\2\12\1\0\1\12\2\0\1\12\1\0"+
    "\1\12\2\0\1\12\27\0\2\12\3\0\2\12\1\0"+
    "\1\50\2\0\1\12\1\0\1\12\1\0\1\16\1\12"+
    "\31\0\2\51\1\52\45\0\2\15\1\52\43\0\2\12"+
    "\3\0\1\12\1\53\1\54\1\12\2\0\1\12\1\0"+
    "\1\12\2\0\1\12\27\0\2\12\3\0\2\12\1\0"+
    "\1\55\2\0\1\12\1\0\1\12\2\0\1\12\27\0"+
    "\2\12\3\0\2\12\1\0\1\12\2\0\1\56\1\0"+
    "\1\12\2\0\1\12\27\0\2\12\3\0\2\12\1\0"+
    "\1\12\1\0\1\57\1\60\1\0\1\12\1\0\1\61"+
    "\1\12\27\0\2\12\3\0\2\12\1\0\1\12\2\0"+
    "\1\12\1\0\1\12\1\62\1\0\1\12\27\0\2\12"+
    "\3\0\1\63\1\12\1\0\1\12\2\0\1\12\1\0"+
    "\1\12\2\0\1\12\3\0\1\64\23\0\2\12\3\0"+
    "\2\12\1\0\1\12\1\16\1\0\1\12\1\0\1\12"+
    "\1\65\1\0\1\12\27\0\2\12\3\0\2\12\1\0"+
    "\1\12\2\0\1\12\1\0\1\12\1\0\1\66\1\12"+
    "\27\0\2\12\3\0\2\12\1\0\1\12\2\0\1\12"+
    "\1\0\1\12\1\0\1\67\1\12\27\0\2\12\3\0"+
    "\1\12\1\70\1\0\1\12\2\0\1\12\1\0\1\12"+
    "\1\0\1\71\1\12\27\0\2\12\3\0\2\12\1\0"+
    "\1\12\1\0\1\72\1\12\1\0\1\12\1\0\1\73"+
    "\1\12\27\0\2\12\3\0\2\12\1\0\1\12\2\0"+
    "\1\12\1\0\1\12\2\0\1\12\4\0\1\74\51\0"+
    "\1\16\51\0\1\16\50\0\1\16\15\0\2\12\3\0"+
    "\2\12\1\0\1\12\2\0\1\12\1\0\1\12\1\0"+
    "\1\61\1\12\17\0\1\41\2\0\42\41\1\0\1\41"+
    "\16\0\2\16\7\0\1\16\16\0\1\16\2\0\1\44"+
    "\2\0\43\44\17\0\2\16\7\0\1\16\17\0\1\16"+
    "\1\0\1\46\1\5\1\6\45\46\5\75\1\76\42\75"+
    "\10\0\2\12\3\0\2\12\1\0\1\12\1\77\1\0"+
    "\1\12\1\0\1\12\2\0\1\12\31\0\2\52\44\0"+
    "\2\12\3\0\2\12\1\100\1\12\2\0\1\12\1\0"+
    "\1\12\2\0\1\12\51\0\1\101\25\0\2\12\3\0"+
    "\1\102\1\12\1\0\1\12\2\0\1\12\1\0\1\12"+
    "\2\0\1\12\27\0\2\12\3\0\2\12\1\0\1\12"+
    "\2\0\1\12\1\100\1\12\2\0\1\12\42\0\1\72"+
    "\34\0\2\12\3\0\2\12\1\0\1\12\2\0\1\12"+
    "\1\0\1\12\1\0\1\103\1\12\35\0\1\16\42\0"+
    "\1\16\46\0\2\12\3\0\1\12\1\104\1\0\1\12"+
    "\2\0\1\12\1\0\1\12\2\0\1\12\44\0\1\105"+
    "\37\0\1\16\15\0\1\106\31\0\1\16\47\0\1\107"+
    "\42\0\2\12\3\0\2\12\1\0\1\110\2\0\1\12"+
    "\1\0\1\12\2\0\1\12\46\0\1\111\44\0\1\100"+
    "\51\0\1\112\46\0\1\101\22\0\5\75\1\113\42\75"+
    "\4\0\1\6\1\76\61\0\1\114\50\0\1\16\52\0"+
    "\1\100\34\0\2\12\3\0\2\12\1\115\1\12\2\0"+
    "\1\12\1\0\1\12\2\0\1\12\41\0\1\66\35\0"+
    "\2\12\3\0\2\12\1\116\1\12\2\0\1\12\1\0"+
    "\1\117\2\0\1\12\34\0\1\120\55\0\1\121\53\0"+
    "\1\16\30\0\2\12\3\0\2\12\1\0\1\12\1\0"+
    "\1\122\1\12\1\0\1\12\2\0\1\12\42\0\1\16"+
    "\41\0\1\123\32\0\4\75\1\6\1\113\42\75\26\0"+
    "\1\16\37\0\1\114\64\0\1\66\24\0\2\12\3\0"+
    "\2\12\1\0\1\12\2\0\1\12\1\0\1\12\1\124"+
    "\1\0\1\12\52\0\1\125\33\0\1\126\66\0\1\16"+
    "\36\0\1\127\52\0\1\16\54\0\1\16\23\0\1\100"+
    "\64\0\1\130\40\0\1\100\30\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[3280];
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
    "\3\0\1\11\1\1\1\11\7\1\1\11\20\1\2\11"+
    "\1\1\1\11\4\1\1\0\1\1\1\0\2\1\1\0"+
    "\2\1\1\0\1\1\2\0\1\1\4\0\1\1\11\0"+
    "\1\1\1\0\1\1\3\0\1\1\6\0\1\1\11\0";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[88];
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
    while (i < 2298) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }

  private static String zzToPrintable(String str) {
    StringBuilder builder = new StringBuilder();
    for (int n = 0 ; n < str.length() ; ) {
      int ch = str.codePointAt(n);
      int charCount = Character.charCount(ch);
      n += charCount;
      if (ch > 31 && ch < 127) {
        builder.append((char)ch);
      } else if (charCount == 1) {
        builder.append(String.format("\\u%04X", ch));
      } else {
        builder.append(String.format("\\U%06X", ch));
      }
    }
    return builder.toString();
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
          System.out.println("line: "+(yyline+1)+" "+"col: "+(yycolumn+1)+" "+"match: --"+zzToPrintable(yytext())+"--");
          System.out.println("action [130] { throw new Error(\"Illegal character <\"+"+ZZ_NL+"                                       yytext()+\">\"); }");
          { throw new Error("Illegal character <"+
                                       yytext()+">");
          }
        case 7: break;
        case 2: 
          System.out.println("line: "+(yyline+1)+" "+"col: "+(yycolumn+1)+" "+"match: --"+zzToPrintable(yytext())+"--");
          System.out.println("action [32] { /* ignore */ }");
          { /* ignore */
          }
        case 8: break;
        case 3: 
          System.out.println("line: "+(yyline+1)+" "+"col: "+(yycolumn+1)+" "+"match: --"+zzToPrintable(yytext())+"--");
          System.out.println("action [75] {  }");
          { 
          }
        case 9: break;
        case 4: 
          System.out.println("line: "+(yyline+1)+" "+"col: "+(yycolumn+1)+" "+"match: --"+zzToPrintable(yytext())+"--");
          System.out.println("action [103] { yybegin(STRING_DQUOTED); }");
          { yybegin(STRING_DQUOTED);
          }
        case 10: break;
        case 5: 
          System.out.println("line: "+(yyline+1)+" "+"col: "+(yycolumn+1)+" "+"match: --"+zzToPrintable(yytext())+"--");
          System.out.println("action [104] { yybegin(STRING_SQUOTED); }");
          { yybegin(STRING_SQUOTED);
          }
        case 11: break;
        case 6: 
          System.out.println("line: "+(yyline+1)+" "+"col: "+(yycolumn+1)+" "+"match: --"+zzToPrintable(yytext())+"--");
          System.out.println("action [108] { yybegin(YYINITIAL); }");
          { yybegin(YYINITIAL);
          }
        case 12: break;
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

  /**
   * Runs the scanner on input files.
   *
   * This main method is the debugging routine for the scanner.
   * It prints debugging information about each returned token to
   * System.out until the end of file is reached, or an error occured.
   *
   * @param argv   the command line, contains the filenames to run
   *               the scanner on.
   */
  public static void main(String argv[]) {
    if (argv.length == 0) {
      System.out.println("Usage : java Lexer [ --encoding <name> ] <inputfile(s)>");
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
        Lexer scanner = null;
        try {
          java.io.FileInputStream stream = new java.io.FileInputStream(argv[i]);
          java.io.Reader reader = new java.io.InputStreamReader(stream, encodingName);
          scanner = new Lexer(reader);
          do {
            System.out.println(scanner.yylex());
          } while (!scanner.zzAtEOF);

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
