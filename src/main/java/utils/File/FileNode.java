package utils.File;

import java.util.ArrayList;
import java.util.List;

import parser.Base.SolidityParser;
import parser.SourceListener;

public class FileNode<String> {
    public String path;

    public List<FileNode> children;
    public FileNode<String> parent;
    public SolidityParser parseTree;
    public SourceListener sourceListener;

    public FileNode(){
        path=null;
        parent=null;
        this.children=new ArrayList<FileNode>();
        parseTree=null;
    }

    public FileNode(String path){
        this.path=path;
        this.children=new ArrayList<FileNode>();
        parent=null;
    }

    public FileNode addChild(String path){
        FileNode<String> t = new FileNode<String>(path);
        t.parent = this;
        this.children.add(t);
        return t;
    }
}
