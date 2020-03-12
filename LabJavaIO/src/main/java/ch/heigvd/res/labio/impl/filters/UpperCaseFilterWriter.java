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
    String beginStr = str.substring(0, off);
    String upperStr = str.substring(off);
    out.write(beginStr + upperStr.toUpperCase(), off, len);
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    char[] tmp = new char[cbuf.length];
    for (int i = 0; i < len; ++i) {
      if(i >= off) {
        tmp[i] = Character.toUpperCase(cbuf[i]);
      } else {
        tmp[i] = cbuf[i];
      }
    }
    out.write(tmp, off, len);
  }

  @Override
  public void write(int c) throws IOException {
    out.write(Character.toUpperCase(c));
  }

}
