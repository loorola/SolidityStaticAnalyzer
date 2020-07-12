package CVEScanner;

import CVEScanner.CVE.Reentrancy;
import config.FileDirectory;
import org.antlr.v4.runtime.ANTLRInputStream;
import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.BaseFunction;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.Content.ContractNodeType.BasicContractDefinition.Statement;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;
import utils.Content.ContractNodeType.Statement.Block;
import utils.File.FileNode;
import utils.File.FileTree;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

public class Scanner {
    public static FileTree ft;

    public Scanner (FileTree ft){
        this.ft = ft;
    }


    public static int FileMatchLine(String txt,FileNode fn) throws Exception{
        List<String> list=new ArrayList<>();
        Path p= FileDirectory.tmp_root.toPath().resolve(fn.path);
        int count = 1;
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(p.toString()));
        String line=null;
        while((line=lineNumberReader.readLine())!=null){
            if(line.contains(txt)) {
                break;
            }
            count++;
        }
        if (lineNumberReader != null){
            lineNumberReader.close();
        }
        return count;
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
