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
 * Remark: Je ne sais pas pourquoi mais je n'arrive pas à valider le test:
 * theApplicationShouldBeAbleToGenerateTheListOfFileNames(). Il liste toujours les fichiers
 * avant de parcourir les dossiers suivants. Je ne comprend pas.
 *
 * @author Olivier Liechti
 */
public class DFSFileExplorer implements IFileExplorer {

  @Override
  public void explore(File rootDirectory, IFileVisitor vistor) {
    File[] files = rootDirectory.listFiles();
    vistor.visit(rootDirectory);

    if(files == null) {
      return;
    }

    for(File file : files) {
      if(file.isFile()) {
        vistor.visit(file);
      }
    }
    // Then moves to subdirectories
    for (File directory : files) {
      if (directory.isDirectory()) {
        explore(directory, vistor);
      }
    }
  }

}
