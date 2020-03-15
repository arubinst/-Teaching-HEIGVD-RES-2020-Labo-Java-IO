package ch.heigvd.res.labio.impl;

import java.util.logging.Logger;

/**
 *
 * @author Olivier Liechti
 */
public class Utils {

  private static final Logger LOG = Logger.getLogger(Utils.class.getName());

  /**
   * This method looks for the next new line separators (\r, \n, \r\n) to extract
   * the next line in the string passed in arguments. 
   * 
   * @param lines a string that may contain 0, 1 or more lines
   * @return an array with 2 elements; the first element is the next line with
   * the line separator, the second element is the remaining text. If the argument does not
   * contain any line separator, then the first element is an empty string.
   */
  public static String[] getNextLine(String lines) {
    String[] strings = new String[2];
    int indx = lines.indexOf('\n');
    if (indx == -1) {
      indx = lines.indexOf('\r');
    }

    if (indx == -1) {
      strings[0] = "";
      strings[1] = lines;
    } else {
      strings[0] = lines.substring(0, indx + 1);
      if (lines.length() < indx + 1) {
        strings[1] = "";
      } else {
        strings[1] = lines.substring(indx + 1);
      }
    }
    return strings;
  }

}
