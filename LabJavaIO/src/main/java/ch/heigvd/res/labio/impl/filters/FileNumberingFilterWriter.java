package ch.heigvd.res.labio.impl.filters;

import ch.heigvd.res.labio.impl.Utils;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
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
  private Integer counter = 0;
  private int lastChar = 0;

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    String[] line = new String[] {"", str.substring(off, len)};
    while (!(line = Utils.getNextLine(line[1]))[0].isEmpty()) {
      stringBuilder.append(getNextNumber());
      stringBuilder.append(line[0]);
    }
    out.write(stringBuilder.toString(), off, len);
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    String[] line = new String[] {"", Arrays.toString(cbuf).substring(off, len)};
    while (!(line = Utils.getNextLine(line[1]))[0].isEmpty()) {
      stringBuilder.append(getNextNumber());
      stringBuilder.append(line[0]);
    }
    out.write(stringBuilder.toString().toCharArray(), off, len);
  }

  @Override
  public void write(int c) throws IOException {
    if(counter == 0) {
      out.write(getNextNumber());
    }

    if(lastChar == '\r') {
      if(c != '\n'){
        out.write(getNextNumber());
      }
    }

    lastChar = c;
    out.write(c);

    if(c == '\n') {
      out.write(getNextNumber());
    }
  }

  private String getNextNumber() {
    return (++counter).toString() + "\t";
  }
}
