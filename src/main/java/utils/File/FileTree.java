package utils.File;

import java.util.LinkedList;
import java.util.Stack;

public class FileTree {
    public static FileNode root;

    public FileTree(){
        root=new FileNode();
    }

    public static FileNode addFileNode(FileNode<String> parent, String path){
        return parent.addChild(path);
    }

    public static FileNode findFileNode(String path){
        Stack<FileNode> q=new Stack<FileNode>();
        FileNode tmp=null;
        boolean found=false;
        q.push(root);
        while(!q.isEmpty()){
            tmp=q.pop();
            if(tmp.path!=null&&tmp.path.equals(path)){
                found=true;
                break;
            }
            tmp.children.forEach(x->{
                q.push((FileNode) x);
            });
        }
        if(found) return tmp;
        return null;
    }
}
