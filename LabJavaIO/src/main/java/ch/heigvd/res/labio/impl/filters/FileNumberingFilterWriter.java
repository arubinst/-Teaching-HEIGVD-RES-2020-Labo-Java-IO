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
  private int linesWritten;
  private String previousChar;
  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    str = str.substring(off, off + len);
    boolean endOfLine = false;

    //We may have more than one line passed at once
    do {
      String[] parsedStr = Utils.getNextLine(str);
      String toWrite = "";

      // For the first time we write in the stream
      if (linesWritten == 0) {
        toWrite = ++linesWritten + "\t";
      }

      if (parsedStr[0].equals("")) { // No return character in the line
        toWrite += parsedStr[1];
        endOfLine = true;
      } else { // There is a return character in the line
        toWrite += parsedStr[0] + ++linesWritten + "\t";
      }

      this.out.write(toWrite);

      str = parsedStr[1];
    } while(!endOfLine);
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    char[] result = Arrays.copyOfRange(cbuf, off, off + len);

    // We go over each char and write them as an int
    for(int i = 0; i < result.length; ++i)
      this.write((int) result[i]);
  }

  @Override
  public void write(int c) throws IOException {
    String toWrite = Character.toString((char) c);

    // For the first time we write
    if(linesWritten == 0) {
      toWrite = ++linesWritten + "\t" + toWrite;
    } else if(toWrite.equals("\n")) {
      // We just have to check if we are on a windows return
      if(previousChar.equals("\r")) {
        toWrite = "\r\n";
      }

      toWrite += ++linesWritten + "\t";
    } else if(toWrite.equals("\r")) {

      toWrite = "";

    } else if(previousChar.equals("\r")) {
      toWrite = "\r" + ++linesWritten + "\t" + toWrite;
    }

    previousChar = Character.toString((char) c);

    this.out.write(toWrite);
  }

}
