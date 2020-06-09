package parser;

import org.antlr.v4.runtime.ParserRuleContext;
import parser.Base.SolidityBaseListener;
import parser.Base.SolidityParser;

import utils.File.FileNode;
import utils.File.FileTree;
import utils.Path.PathResolver;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
this class is looking for all imported resources from other file with its imported contract or library
 */


public class SourceParser extends SolidityBaseListener {
    public class Source{    //imported local file
        public FileNode fn;
        public String alias;
        List<SourceModule> srcModule = new ArrayList<>();   //contract or library that is included
        public Source(FileNode fn, String alias){
            this.fn=fn;
            this.alias=alias;
        }

        public void addModule(String moduleName,String alias){
            SourceModule srm=new SourceModule(moduleName,alias);
            srcModule.add(srm);
        }
    }
    public class GlobalSource{  //imported global file
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

    public class SourceModule{  //imported contract or library
        String moduleName;
        ParserRuleContext ctx;

        public SourceModule(String moduleName, String alias){
            this.moduleName=moduleName;
        }
    }


    private FileNode self;
    List<Source>sourceList = new ArrayList<>();
    List<GlobalSource>globalSourceList = new ArrayList<>();

    public SourceParser(FileNode fn){
        fn.sourceParser =this;
        self=fn;
    }

    @Override
    public void enterImportFile(SolidityParser.ImportFileContext ctx) {   //same as import all
        Path sourcePath = PathResolver.resolveImportedPath(Paths.get(self.path),ctx.stringLiteral().getText());
        FileNode source_node=FileTree.findFileNode(sourcePath.toString());   //find if it is in the same directory
        GlobalSource gs;
        Source s;
        String alias;
        if(ctx.identifier()==null){
            if(source_node==null){   //source not found from the directory which means it is from the internet
                gs = new GlobalSource(sourcePath,null);
                globalSourceList.add(gs);
            } else {
                s = new Source(source_node,null);
                sourceList.add(s);
            }

        }else { //with identifier
            alias=ctx.identifier().getText().replaceAll("^\"|\"$", "");
            if(source_node==null){
                gs = new GlobalSource(sourcePath,alias);
                globalSourceList.add(gs);
            }else{
                s = new Source(source_node,alias);
                sourceList.add(s);
            }

        }
    }

    @Override
    public void enterImportFileAsSymbol(SolidityParser.ImportFileAsSymbolContext ctx) {
        Path sourcePath = PathResolver.resolveImportedPath(Paths.get(self.path),ctx.stringLiteral().getText());
        FileNode source_node=FileTree.findFileNode(sourcePath.toString());   //find if it is in the same directory
        GlobalSource gs;
        Source s;

        if(ctx.identifier()==null){ //import file by default identifier
            if(source_node==null&&ctx.importAllOrNot().identifier()==null){
                //source not found from the directory which means it is from the internet
                //import all
                gs = new GlobalSource(sourcePath,null);
                globalSourceList.add(gs);
            } else if(source_node==null){
                //source not found from the directory which means it is from the internet
                //import one contract or library only
                gs = new GlobalSource(sourcePath,null);
                gs.addModule(ctx.importAllOrNot().identifier().getText(),null);
                globalSourceList.add(gs);
            } else if(ctx.importAllOrNot().identifier()==null){
                //source can be found from the directory
                //import all
                s = new Source(source_node,null);
                sourceList.add(s);
            }else{
                //source can be found from the directory
                //import one contract or library only
                s = new Source(source_node,null);
                s.addModule(ctx.importAllOrNot().identifier().getText(),null);
                sourceList.add(s);
            }
        }else{  //import file by using identifier
            if(source_node==null&&ctx.importAllOrNot().identifier()==null){
                //source not found from the directory which means it is from the internet
                //import all
                gs = new GlobalSource(sourcePath,ctx.identifier().getText());
                globalSourceList.add(gs);
            } else if(source_node==null){
                //source not found from the directory which means it is from the internet
                //import one contract or library only
                gs = new GlobalSource(sourcePath,ctx.identifier().getText());
                gs.addModule(ctx.importAllOrNot().identifier().getText(),null);
                globalSourceList.add(gs);
            } else if(ctx.importAllOrNot().identifier()==null){
                //source can be found from the directory
                //import all
                s = new Source(source_node,ctx.identifier().getText());
                sourceList.add(s);
            }else{
                //source can be found from the directory
                //import one contract or library only
                s = new Source(source_node,ctx.identifier().getText());
                s.addModule(ctx.importAllOrNot().identifier().getText(),null);
                sourceList.add(s);
            }
        }
    }

    @Override
    public void enterImportFileFrom(SolidityParser.ImportFileFromContext ctx) {
        Path sourcePath = PathResolver.resolveImportedPath(Paths.get(self.path),ctx.stringLiteral().getText());
        FileNode source_node=FileTree.findFileNode(sourcePath.toString());   //find if it is in the same directory
        GlobalSource gs;
        Source s;

        if(source_node==null){
            gs = new GlobalSource(sourcePath,null);
            ctx.importDeclaration().forEach(x->{
                if(x.identifier(1)==null){  //contract or library without identifier
                    gs.addModule(x.identifier(0).getText(),null);
                }else{
                    gs.addModule(x.identifier(0).getText(),x.identifier(1).getText());
                }
            });
            globalSourceList.add(gs);
        }else{
            s=new Source(source_node,null);
            ctx.importDeclaration().forEach(x->{
                if(x.identifier(1)==null){  //contract or library without identifier
                    s.addModule(x.identifier(0).getText(),null);
                }else{
                    s.addModule(x.identifier(0).getText(),x.identifier(1).getText());
                }
            });
            sourceList.add(s);
        }
    }

}
