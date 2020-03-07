package ch.heigvd.res.labio.impl.explorers;

import ch.heigvd.res.labio.interfaces.IFileExplorer;
import ch.heigvd.res.labio.interfaces.IFileVisitor;
import java.io.File;

/**
 * This implementation of the IFileExplorer interface performs a depth-first
 * exploration of the file system and invokes the visitor for every encountered
 * node (file and directory). When the explorer reaches a directory, it visits all
 * files in the directory and then moves into the subdirectories.
 *
 * Note: this specification is wrong! If the DFS visit "all files in the directory and
 * the moves into the subdirectories", then this test won't pass:
 * ApplicationTest::theApplicationShouldBeAbleToGenerateTheListOfFileNames()
 * 
 * @author Olivier Liechti
 */
public class DFSFileExplorer implements IFileExplorer {

  @Override
  public void explore(File rootDirectory, IFileVisitor visitor) {
    File[] childNodes = rootDirectory.listFiles();
    visitor.visit(rootDirectory);

    if (childNodes == null) {
      return;
    }

    for (File child : childNodes) {
        explore(child, visitor);
    }
  }

}
