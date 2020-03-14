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
 * Hello\n\World -> 1\tHello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());
  private int _count;
  private boolean _newLine = false;

  public FileNumberingFilterWriter(Writer out) {
    super(out);
    _count = 1;
  }

  @Override
  public void write(String str, int off, int len) throws IOException
  {
    if(_count == 1) {
      super.out.write(_count++ + "\t");
    }

    String temp = str.substring(off, off+len);
    int index = Utils.getIndexNewLine(temp);

    while(index != -1)
    {
      super.out.write(temp.substring(0, index) + _count++ + "\t");
      temp = temp.substring(index);
      index = Utils.getIndexNewLine(temp);
    }

    super.out.write(temp);
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException
  {
    write(String.valueOf(cbuf), off, len);
  }

  @Override
  public void write(int c) throws IOException {

    //gère le \r\n afin de ne pas avoir 2 retours à la ligne
    if(c == 10) {
      _newLine = false;
    }

    //écrit le n° en début de ligne
    if(_count == 1 || _newLine) {
      super.out.write(_count++ + "\t");
      _newLine = false;
    }

    //écrit le char passé en paramètre
    super.out.write(String.valueOf((char)c));

    //prépare le retour à la ligne
    if(c == 13 || c == 10) {  // \r = 13  -  \n = 10
      _newLine = true;
    }

  }

}
