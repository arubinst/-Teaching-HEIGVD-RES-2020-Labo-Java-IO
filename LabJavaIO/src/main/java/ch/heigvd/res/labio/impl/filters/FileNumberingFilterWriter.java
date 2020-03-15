package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 *
 * Hello\n\World -> 1\Hello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  int curLineNbr = 0;

  @Override
  public void write(String str, int off, int len) throws IOException {

    try {
      curLineNbr = Integer.parseInt(str);
    }
      catch(NumberFormatException e){
    }

    int tabPos = str.indexOf('\t');

    if(curLineNbr == 0){
      out.write(Integer.toString(++curLineNbr)+'\t');
    }

    if(tabPos < 0 && tabPos > off){
      out.write(Integer.toString(++curLineNbr)+'\t');
    }
    int lineNbr = 2;
    String outStr = "";

    for(int i = off; i<(len + off); i++){
      out.write(str.charAt(i));
      if( (str.charAt(i) == '\r' && i+1 <str.length() &&  str.charAt(i+1) != '\n'  )|| str.charAt(i) == '\n'){
        out.write(strLine(++curLineNbr));
      }
    }
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    this.write(String.valueOf(cbuf), off, len);
    //throw new UnsupportedOperationException("The student has not implemented this method yet.");
  }

  int lastChar;

  @Override
  public void write(int c) throws IOException {

    if(curLineNbr == 0){
      out.write( strLine(++curLineNbr));
    }

    if(lastChar == '\r' && c != '\n'){
      out.write( strLine(++curLineNbr));
    }

    out.write(c);
    lastChar = c;

    if (c == '\n') {
      out.write( strLine(++curLineNbr));
    }
  }

  private String strLine(int lineNbr){
    return String.valueOf(lineNbr)+'\t';
  }

}
