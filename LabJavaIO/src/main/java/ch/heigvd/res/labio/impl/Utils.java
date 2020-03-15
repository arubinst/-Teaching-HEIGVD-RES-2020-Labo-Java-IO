package ch.heigvd.res.labio.impl;

import java.util.Vector;
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
        String[] separators = new String[] {"\r\n", "\r", "\n"};
        int foundSep = -1;
        int sep = 0;

        for(; sep < separators.length; ++sep){
            foundSep = lines.indexOf(separators[sep]);
            if(foundSep != -1){
                break;
            }
        }

        /*  */
        if(foundSep == -1){
            foundSep = 0;
        }else{
            foundSep = foundSep + separators[sep].length();
        }

        String first = lines.substring(0, foundSep);
        String second = lines.substring(foundSep);
        return new String[] { first, second };
    }

}
