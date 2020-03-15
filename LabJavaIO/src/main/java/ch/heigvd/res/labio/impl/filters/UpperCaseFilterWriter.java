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
    String tmpToUpper = str.substring(off, (len + off)).toUpperCase();
    out.write(tmpToUpper);
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    for(int i = 0; i < len; ++i){
      out.write(String.valueOf(cbuf[off + i]).toUpperCase());
    }
  }

  @Override
  public void write(int c) throws IOException {
    out.write(String.valueOf((char)c).toUpperCase());
  }

}
