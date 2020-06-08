package parser;

import javafx.util.Pair;
import parser.Base.SolidityBaseListener;
import parser.Base.SolidityParser;

import utils.File.FileNode;
import utils.File.FileTree;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SourceParser extends SolidityBaseListener {
    private FileNode self;
    public ArrayList<Pair> src=new ArrayList<Pair>();
    public ArrayList<Pair> srcModule = new ArrayList<Pair>();

    public ArrayList<Pair> notFoundInDirectory=new ArrayList<Pair>();

    public SourceParser(FileNode fn){
        fn.sourceParseTree =this;
        self=fn;
    }


    private Path getSourcePath(String path){
        path=path.replaceAll("^\"|\"$", "");
        Pair<FileNode,String>map;

        Path p = Paths.get(self.path);   //this file path
        Path q=Paths.get(path);   //the imported file path

        Path src_p=resolveSourcePath(p,q);  //resolve the src file path

        return src_p;
    }

    @Override public void enterImportFile(SolidityParser.ImportFileContext ctx) {   //same as import all
        Path sourcePath = getSourcePath(ctx.stringLiteral().getText());
        FileNode source_node=FileTree.findFileNode(sourcePath.toString());   //find if it is in the same directory
        Pair<FileNode,String>map1;
        Pair<Path,String>map2;
        String alias;
        if(ctx.identifier()==null){ //import all
            if(source_node==null){   //source not found from the directory which means it is from the internet
                map2=new Pair<Path,String>(sourcePath,null);
                notFoundInDirectory.add(map2);
                //...
            } else {
                map1=new Pair<FileNode,String>(source_node,null);    //add into src list
                src.add(map1);
            }

        }else {
            alias=ctx.identifier().getText().replaceAll("^\"|\"$", "");
            if(source_node==null){
                map2=new Pair<Path,String>(sourcePath,alias);
                notFoundInDirectory.add(map2);
            }else{
                map1=new Pair<FileNode,String>(source_node,alias);
                System.out.println(map1.getKey().path+" "+map1.getValue());
                src.add(map1);
            }

        }
    }

    @Override public void enterImportFileAsSymbol(SolidityParser.ImportFileAsSymbolContext ctx) {
        String parent=ctx.stringLiteral().getText();
        System.out.println(parent);

        if(ctx.identifier()!=null){

        }else{

        }
    }

    @Override public void enterImportFileFrom(SolidityParser.ImportFileFromContext ctx) {
        String parent=ctx.stringLiteral().getText();
        System.out.println(parent);
        if(ctx.importDeclaration().size()==1){
            if(ctx.importDeclaration(0).identifier().size()==1){

            }else{

            }
        }else{
            for(SolidityParser.ImportDeclarationContext x: ctx.importDeclaration()){
                if(x.identifier().size()==1){

                }else{
                    for(SolidityParser.IdentifierContext y: x.identifier()){

                    }
                }

            }
        }
    }

    private Path resolveSourcePath(Path p, Path q){
        if(q.startsWith(".")){
            p=p.resolve("../").resolve(q);  //"./"means the file in same directory https://solidity.readthedocs.io/en/latest/layout-of-source-files.html
        }else{
            p=p.resolve(q);
        }
        return p.normalize();
    }




}
