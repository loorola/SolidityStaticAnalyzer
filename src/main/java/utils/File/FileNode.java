package utils.File;

import java.util.ArrayList;
import java.util.List;

import parser.SourceParser;
import utils.FunctionCall.ContentNode;

public class FileNode {
    public String path;
    public List<FileNode> children;
    public FileNode parent;
    public List<ContentNode> contentNodeList = new ArrayList<>();
    public SourceParser sourceParser=null;

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
