package ch.heigvd.res.labio.impl.filters;

import ch.heigvd.res.labio.impl.Utils;
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

  /**
   * Writes a portion of a string.
   * Note: When using this method, each call will start an independent write. For example, when writing "\r" and
   * then "\n", both will output a newline. This is different from the behavior of write(int c) that would write
   * a single Windows-style newline.
   *
   * @param  str  String to be written
   * @param  off  Offset from which to start reading characters
   * @param  len  Number of characters to be written
   *
   * @throws  IndexOutOfBoundsException
   *          If the values of the {@code off} and {@code len} parameters
   *          cause the corresponding method of the underlying {@code Writer}
   *          to throw an {@code IndexOutOfBoundsException}
   *
   * @throws  IOException  If an I/O error occurs
   */
  @Override
  public void write(String str, int off, int len) throws IOException {
    ensureStreamStarted();

    String[] parts = new String[] {
            "",
            str.substring(off, off + len)
    };

    while (!parts[1].isEmpty()) {
      parts = Utils.getNextLine(parts[1]);

      // If first element is empty, there is no newline and the result is stored in the second element
      if (parts[0].isEmpty()) {
        out.write(parts[1]);
        break;
      }

      // If the first element is non-empty, it contains a single line and the second element contains the remaining text
      out.write(parts[0]);
      writeNewLine();
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
   * For example, "#\r" will output "1\t#\r"
   *          but "#\n" will output "1\t#\n2\t".
   *
   * @throws IOException If an I/O error occurs
   */
  @Override
  public void write(int c) throws IOException {
    ensureStreamStarted();

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

  /**
   * Handle the first time something is output to the stream.
   *
   * @throws IOException If an I/O error occurs
   */
  private void ensureStreamStarted() throws IOException {
    if (currentLine == 0) {
      currentLine++;
      writeNewLine();
    }
  }

  /**
   * Increment the line counter and write the number of the new line.
   *
   * @throws IOException If an I/O error occurs
   */
  private void writeNewLine() throws IOException {
    out.write(Integer.toString(currentLine++));
    out.write('\t');
  }

}
