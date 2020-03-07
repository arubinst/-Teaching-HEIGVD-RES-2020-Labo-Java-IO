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
  private int currentLine = 0;
  private int lastChar;

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    for (int i = off; i < off + len; i++) {
      write(str.charAt(i));
    }
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    write(String.copyValueOf(cbuf, off, len));
  }

  /**
   * Write a single character to the stream.
   * Note: In order to handle Windows newlines (\r\n), if a \r is encountered it is mandatory to wait until the
   * next char to see if it is a \n or not (in order to not break Windows newlines).
   * But this behavior has an impact on MacOS newlines (\r alone). Because we must wait for the next char, if the
   * last character output to the stream is a "\r" the next line number will not be output.
   * For example, "L1\rL2\r" will output "1\tL1\r2\tL2\r"
   *          but "L1\nL2\n" will output "1\tL1\n2\tL2\n3\t".
   *
   * @throws IOException If an I/O error occurs
   */
  @Override
  public void write(int c) throws IOException {
    // Handle the first time something is output to the stream
    if (currentLine == 0) {
      currentLine++;
      writeNewLine();
    }

    // Handle MacOS newlines
    if (lastChar == '\r' && c != '\n') {
      writeNewLine();
    }

    out.write(c);
    lastChar = c;

    // Handle UNIX and Windows newlines
    if (c == '\n') {
      writeNewLine();
    }
  }

  private void writeNewLine() throws IOException {
    out.write(Integer.toString(currentLine++));
    out.write('\t');
  }

}
