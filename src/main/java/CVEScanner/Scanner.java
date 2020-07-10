package CVEScanner;

import config.FileDirectory;
import org.antlr.v4.runtime.ANTLRInputStream;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;
import utils.File.FileNode;
import utils.File.FileTree;

import java.io.File;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Stack;

public class Scanner {
    static FileTree ft;

    public static void scanCode(){

    }

    private static void resetInstanceFunctionModifier(Instance in){
        for(int i=0;i<in.functionList.size();i++){
            in.functionList.get(i).visited=false;
        }

        for(int i=0;i<in.modifierList.size();i++){
            in.modifierList.get(i).visited=false;
        }
    }

    public static void reset(){
        Stack<FileNode> s=new Stack<FileNode>();
        s.push(ft.root);
        FileNode fn;
        File tf;
        while(!s.isEmpty()){
            fn=s.pop();
            if(fn.path!=null){
                tf=new File(fn.path);
                if(!tf.isDirectory()){
                    for(int i=0;i<fn.fileContent.contractList.size();i++){
                        resetInstanceFunctionModifier(fn.fileContent.contractList.get(i));
                    }
                    for(int i=0;i<fn.fileContent.libraryList.size();i++){
                        resetInstanceFunctionModifier(fn.fileContent.libraryList.get(i));
                    }
                }
            }
            Iterator<FileNode> f = fn.children.iterator();
            while(f.hasNext()){
                s.push(f.next());
            }
        }

    }
}
