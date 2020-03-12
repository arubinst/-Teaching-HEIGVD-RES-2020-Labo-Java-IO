package ch.heigvd.res.labio.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    int position = lines.indexOf("\r\n");

    if (position == -1) position = lines.indexOf("\r");
    else position++;
    if (position == -1) position = lines.indexOf("\n");

    String[] result = {"", ""};
    position++;
    if (position >= 0)
        result[0] = lines.substring(0, position);
    result[1] = lines.substring(position);
    return result;
  }

}
