package parser;

import javafx.util.Pair;
import parser.Base.SolidityBaseListener;
import parser.Base.SolidityParser;

import utils.File.FileNode;
import utils.File.FileTree;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SourceListener extends SolidityBaseListener {
    public FileNode self;
    public ArrayList<Pair> src=new ArrayList<Pair>();
    public ArrayList<Path> notFoundInDirectory=new ArrayList<Path>();

    public SourceListener(FileNode fn){
        fn.sourceListener =this;
        self=fn;
    }


    @Override public void enterImportFile(SolidityParser.ImportFileContext ctx) {
        String source=ctx.stringLiteral().getText().replaceAll("^\"|\"$", "");
        Pair<FileNode,String>map;   //add to the src

        Path p = Paths.get(self.path.toString());   //this file path
        Path q=Paths.get(source);   //the imported file path

        Path src_p=resolveSourcePath(p,q);  //resolve the src file path

        FileNode source_node=FileTree.findFileNode(src_p.toString());   //find if it is in the same directory


        if(ctx.identifier()==null){
            if(source_node==null){   //source not found from the directory which means it is from the internet
               notFoundInDirectory.add(src_p);  //do it later
            } else {
                map=new Pair<FileNode,String>(source_node,null);    //add into src list
                src.add(map);
            }

        }else {
            String alias=ctx.identifier().getText().replaceAll("^\"|\"$", "");
            map=new Pair<FileNode,String>(source_node,alias);
            System.out.println(map.getKey().path+" "+map.getValue());
            src.add(map);
        }
    }

    @Override public void enterImportFileAsSymbol(SolidityParser.ImportFileAsSymbolContext ctx) {
        String parent=ctx.stringLiteral().getText();
        System.out.println(parent);
        if(ctx.identifier().size()==1){

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
