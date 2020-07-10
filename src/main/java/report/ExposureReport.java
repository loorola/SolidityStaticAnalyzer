package report;

import config.FileDirectory;
import org.antlr.v4.runtime.ANTLRInputStream;
import parser.ContextSensitiveParser;
import utils.File.FileNode;
import utils.File.FileTree;

import java.io.File;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Stack;

public class ExposureReport {

    public static void generateCFG(FileTree ft){
        Stack<FileNode> s=new Stack<FileNode>();
        Path p;
        ANTLRInputStream input;
        s.push(ft.root);
        FileNode fn;
        File tf;
        while(!s.isEmpty()){
            fn=s.pop();
            if(fn.path!=null){
                tf=new File(fn.path);
                if(!tf.isDirectory()){
                    p = FileDirectory.tmp_root.toPath().resolve(fn.path);

                }

            }

            Iterator<FileNode> f = fn.children.iterator();
            while(f.hasNext()){
                s.push(f.next());
            }
        }
    }
}
