package utils.File;

import java.util.ArrayList;
import java.util.List;

import parser.Base.SolidityParser;
import utils.Content.FileContent;
import utils.Source.GlobalSource;
import utils.Source.LocalSource;

public class FileNode {
    public String path;
    public List<FileNode> children;
    public FileNode parent;
    public List<LocalSource> localSourceList = new ArrayList<>();
    public List<GlobalSource> globalSourceList = new ArrayList<>();
    public FileContent fileContent = new FileContent();
    public SolidityParser.SourceUnitContext srcCTX;
    public FileNode(){
        path=null;
        parent=null;
        this.children=new ArrayList<FileNode>();
    }

    public FileNode(String path){
        this.path=path;
        this.children=new ArrayList<FileNode>();
        parent=null;
    }

    public FileNode addChild(String path){
        FileNode t = new FileNode(path);
        t.parent = this;
        this.children.add(t);
        return t;
    }
}
