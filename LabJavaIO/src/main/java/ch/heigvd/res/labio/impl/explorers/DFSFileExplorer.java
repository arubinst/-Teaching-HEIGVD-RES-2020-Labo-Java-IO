package ch.heigvd.res.labio.impl.explorers;

import ch.heigvd.res.labio.interfaces.IFileExplorer;
import ch.heigvd.res.labio.interfaces.IFileVisitor;
import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * This implementation of the IFileExplorer interface performs a depth-first
 * exploration of the file system and invokes the visitor for every encountered
 * node (file and directory). When the explorer reaches a directory, it visits all
 * files in the directory and then moves into the subdirectories.
 *
 * Remark: Pour faire passer tous les tests, il a été nécessaire de faire un sort des fichiers
 * par ordre alphabétique. Sinon, le résultat était faux. Peut-être une spécialité sur les systèmes
 * UNIX ?
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

    // Solution trouvée pour faire passer le test
    // theApplicationShoulgBeAbleToGenerateTheListOfFileNames()
    // Sans cela le résultat était toujours à l'envers.
    Arrays.sort(files);

    for (File f : files) {
      if (f.isDirectory())
        explore(f, vistor);
      else
        vistor.visit(f);
    }
  }

}
