package utils.Source;

import utils.File.FileNode;

import java.util.ArrayList;
import java.util.List;

public class LocalSource {  //imported local file
    public FileNode fn; //link to the local file that is used in this contract
    public String alias;    //identifier of the source
    List<SourceModule> srcModule = new ArrayList<>();   //contract or library that is included

    public LocalSource(FileNode fn, String alias){
        this.fn=fn;
        this.alias=alias;
    }

    public void addModule(String moduleName,String alias){
        SourceModule srm=new SourceModule(moduleName,alias);
        srcModule.add(srm);
    }
}
