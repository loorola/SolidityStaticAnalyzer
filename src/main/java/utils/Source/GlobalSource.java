package utils.Source;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GlobalSource {
    public Path path;
    public String alias;
    List<SourceModule> srcModule = new ArrayList<>();
    public GlobalSource(Path path, String alias){
        this.path=path;
        this.alias=alias;
    }

    public void addModule(String moduleName,String alias){
        SourceModule srm=new SourceModule(moduleName,alias);
        srcModule.add(srm);
    }
}
