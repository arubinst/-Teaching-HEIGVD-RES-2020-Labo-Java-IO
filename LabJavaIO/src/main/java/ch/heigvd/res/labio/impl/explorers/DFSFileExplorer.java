package ch.heigvd.res.labio.impl.explorers;

import ch.heigvd.res.labio.interfaces.IFileExplorer;
import ch.heigvd.res.labio.interfaces.IFileVisitor;
import java.io.File;
import java.util.Arrays;

/**
 * This implementation of the IFileExplorer interface performs a depth-first
 * exploration of the file system and invokes the visitor for every encountered
 * node (file and directory). When the explorer reaches a directory, it visits all
 * files in the directory and then moves into the subdirectories.
 *
 * @author Olivier Liechti
 */
public class DFSFileExplorer implements IFileExplorer {

  @Override
  public void explore(File rootDirectory, IFileVisitor vistor) {
    if (rootDirectory == null){
      System.out.println("Le fichier est NULL : Probleme");
      return;
    }

    //We need to visit beforehand the rootDirectory to make sure that the dir is not empty
    vistor.visit(rootDirectory);

    //As the rootDirectory is a File made of Files :
    File[] files = rootDirectory.listFiles();


    if (files != null && files.length > 0) {
      Arrays.sort(files);

      for (File f : files) {
        //We want to make a recusrive call to this method on the file explored
        //As we get to check if there is anything in this file, if not we check the next file.
        explore(f, vistor);
      }
    }
  }

}
