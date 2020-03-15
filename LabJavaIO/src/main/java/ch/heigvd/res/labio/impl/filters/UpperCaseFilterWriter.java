package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Olivier Liechti
 */
public class UpperCaseFilterWriter extends FilterWriter {
  
  public UpperCaseFilterWriter(Writer wrappedWriter) {
    super(wrappedWriter);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    String toUpperString = str.substring(off, off+len).toUpperCase();
    out.write(toUpperString);
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    String fromBuff = new String(cbuf);
    out.write(fromBuff.toUpperCase(), off, len);
  }

  @Override
  public void write(int c) throws IOException {
    char toUpperChar = Character.toUpperCase((char)c);
    out.write(toUpperChar);
  }

}
