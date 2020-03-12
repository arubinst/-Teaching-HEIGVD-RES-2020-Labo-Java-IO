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
    int breakLine = 0;

    for (int i = 0 ; i < lines.length(); ++i) {
      if(lines.charAt(i) == '\r') {
        if(i + 1 < lines.length() && lines.charAt(i + 1) == '\n') {
          breakLine = i + 2;
          break;
        } else {
          breakLine = i + 1;
          break;
        }
      } else if (lines.charAt(i) == '\n') {
        breakLine = i + 1;
        break;
      }
    }

    return new String[] {
            lines.substring(0, breakLine),
            lines.substring(breakLine)
    };
  }

}
