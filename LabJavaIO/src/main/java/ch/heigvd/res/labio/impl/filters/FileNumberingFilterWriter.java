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
  private int lineNumber = 0;
  private int previousChar = 0;

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    checkIfFirstLine();

    // Initialisation: Sera remplis par la méthode Utils.getNextLine()
    String[] partsToWrite = new String[] {
            "",
            str.substring(off,off + len)
    };

    while(!partsToWrite[1].isEmpty()) {
      partsToWrite = Utils.getNextLine(partsToWrite[1]);

      // Si le premier élément est vide, le reste est stocké dans le second élément
      if (partsToWrite[0].isEmpty()) {
        out.write(partsToWrite[1]);
        break;
      }

      // Si le premier élément n'est pas vide, il contient une ligne et le second
      // element contient le reste. => Boucle sur le reste
      out.write(partsToWrite[0]);
      writeNewLine();
    }
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    write(String.copyValueOf(cbuf,off,len));
  }

  @Override
  public void write(int c) throws IOException {
    checkIfFirstLine();

    // Si c'est un \r mais pas un \r\n
    if(previousChar == '\r' && c != '\n') {
      writeNewLine();
    }
    out.write(c);
    previousChar = c; // Pour gérer le retour ligne macOs (\r)
    if(c == '\n') {
      writeNewLine();
    }
  }

  /**
   * Ecrit une nouvelle ligne en incrémentant
   * le compteur
   *
   * @throws IOException
   */
  private void writeNewLine() throws IOException{
    out.write(Integer.toString(lineNumber));
    lineNumber++;
    out.write('\t');  // Ecrit la tabulation
  }

  /**
   * Check si on se trouve sur la première ligne écrite
   *
   * @throws IOException
   */
  private void checkIfFirstLine() throws IOException{
    if(lineNumber == 0) {
      lineNumber = 1;
      writeNewLine();
    }
  }

}
