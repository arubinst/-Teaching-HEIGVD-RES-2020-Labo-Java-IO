package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

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
  private static boolean firstCall = false;
  private static int nbLine = 1;

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    String tmp;
    int start = off;
    int nbChar = 0;
    for(int idx = off; idx < off + len; ++idx){
      char c = str.charAt(idx);
      ++nbChar;
      if(c == '\n' || c == '\r'){
        tmp = str.substring(start, start + nbChar);
        out.write(nbLine + "\t" + tmp);
        start = idx;
        nbChar = 0;
        ++nbLine;
        if(idx == off + len - 1){
          out.write(nbLine + "\t");
        }else{
          --idx;
        }
      }
    }
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    throw new UnsupportedOperationException("The student has not implemented this method yet.");
  }

  @Override
  public void write(int c) throws IOException {
    throw new UnsupportedOperationException("The student has not implemented this method yet.");
    // tester cette approche.....
  }

}
