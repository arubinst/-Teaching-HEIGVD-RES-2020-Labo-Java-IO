package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;
import  ch.heigvd.res.labio.impl.Utils;

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
  private char TABULATION = '\t';
  private int previousChar;
  private int counter = 1;

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    String sub = str.substring(off, off + len);
    String[] tab = Utils.getNextLine(sub);
    StringBuilder sb = new StringBuilder();

    if(counter==1){
      sb.append(counter + "" + TABULATION);
      counter++;
    }

    while(!tab[0].isEmpty()){
      sb.append(tab[0] + "" + counter + "" + TABULATION);
      counter++;
      tab = Utils.getNextLine(tab[1]);
    }

    //si il reste une partie on l'insere
    if(!tab[1].isEmpty()){
      sb.append(tab[1]);
    }

    out.write(sb.toString());
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    String fromBuffer = new String(cbuf);
    out.write(fromBuffer, off, len);
  }

  @Override
  public void write(int c) throws IOException {
    if(counter == 1){
      out.write(counter+""+TABULATION);
      counter++;
    }

    if(c != '\n' && previousChar == '\r'){
      out.write(counter+""+TABULATION);
      counter++;
    }

    out.write(c);

    if(c == '\n'){
      out.write(counter+""+TABULATION);
      counter++;
    }

    previousChar = c;
  }

}
