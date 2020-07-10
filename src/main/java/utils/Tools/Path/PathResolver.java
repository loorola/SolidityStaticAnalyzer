package utils.Tools.Path;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class PathResolver {
    public static Path resolveImportedPath(Path thisFilePath, String importedPath){
        importedPath=importedPath.replaceAll("^\"|\"$", "");
        Path p=Paths.get(importedPath);   //the imported file path
        Iterator<Path> e = p.iterator();
        String tmp;
        int count = 0;
        while(e.hasNext()){
            tmp = e.next().toString();
            if(tmp.equals(".")){
                thisFilePath=thisFilePath.resolve("../");
            }else if(tmp.equals("..")){
                thisFilePath=thisFilePath.resolve("../../");
            }else if(count!=0){
                thisFilePath=thisFilePath.resolve(tmp);
            }else{
                thisFilePath=thisFilePath.resolve("../"+tmp);

            }
            count++;

        }
        return thisFilePath.normalize();
    }
}
