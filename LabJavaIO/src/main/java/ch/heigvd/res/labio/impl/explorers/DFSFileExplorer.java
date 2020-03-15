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
    // If rootDirectory is a file, when end the discovery
    vistor.visit(rootDirectory);
    if(rootDirectory.isFile()){
      return;
    }
    // list current directory file and sort
    File[] currentFile = rootDirectory.listFiles();
    if(currentFile == null){
      return;
    }
    Arrays.sort(currentFile);

    // For each file in the directory, we call visitor and DFS in sub-folder.
    for(File f : currentFile){
      if(f.isDirectory()){
        explore(f, vistor);
      }
    }
  }

}
