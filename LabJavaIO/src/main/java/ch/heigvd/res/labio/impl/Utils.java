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

    String[] result = { "", lines };

    //récupère l'index
    int index = lines.contains("\r\n")            //teste pour \r\n
                  ? lines.indexOf("\r\n")+2
                  : lines.contains("\r")          //teste pour \r
                      ? lines.indexOf("\r")+1
                      : lines.contains("\n")      //teste pour \n
                          ? lines.indexOf("\n")+1
                          : -1;                   //valeur par défaut, si rien n'est trouvé

    //casse la ligne si un symbole est trouvé
    if(index != -1)
    {
      result[0] = lines.substring(0, index);
      result[1] = lines.substring(index);
    }

    return result;

  }

}
