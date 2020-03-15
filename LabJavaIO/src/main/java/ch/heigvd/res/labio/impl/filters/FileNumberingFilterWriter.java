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

  private int lineNb = 1;
  private boolean returnC = false;
  private String res = Integer.toString(lineNb++) + '\t';

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    write(str.toCharArray(), off, len);
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    for(int i = off; i < len+off; i++) {
      char d = cbuf[i];

      if(returnC || d ==  '\n') {
        if (returnC && d == '\n') {
          res += "\r\n";
        } else if (returnC) {
          res += '\r';
        } else if (d == '\n') {
          res += '\n';
        }
        res += Integer.toString(lineNb++) + '\t';
      }
      if (d != '\n' && d != '\r'){
        res += d;
      }
      returnC = d == '\r';
    }
    super.write(res, 0, res.length());
    res = "";
  }

  @Override
  public void write(int c) throws IOException {
    char[] d = {(char)c};
    write(d, 0, d.length);  }

}
