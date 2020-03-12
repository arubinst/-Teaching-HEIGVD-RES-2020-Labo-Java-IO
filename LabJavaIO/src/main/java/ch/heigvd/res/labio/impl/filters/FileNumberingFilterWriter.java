package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

import static ch.heigvd.res.labio.impl.Utils.getNextLine;

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
  private int indexLine = 0;
  private boolean newLine = false;
  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    String substring = str.substring(off, off + len);
    StringBuilder result = new StringBuilder();
    String[] lines = getNextLine(substring);
    int i = 1;
    for (String line : lines)
    {
      result.append(i);
      result.append("\t");
      result.append(line);
      i++;
    }
    out.write(result.toString());
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    write(cbuf.toString(), off, len);
  }

  @Override
  public void write(int c) throws IOException {
    if (indexLine == 0 || (newLine && c != '\n'))
    {
      out.write(Integer.toString(++indexLine));
      out.write('\t');
      newLine = false;
    }

    if (c == '\n') {
      newLine = true;
    }
    else if (c == '\r') {
      newLine = true;
    }
    else {
      newLine = false;
    }

    out.write(c);

  }

}
