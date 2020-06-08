package parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import parser.Base.SolidityLexer;
import parser.Base.SolidityParser;
import utils.File.FileNode;
import utils.File.FileTree;
import config.FileDirectory;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class ProjectParser {
    public static List<SourceListener>cg_list = new ArrayList<>();

    public static void init(FileTree ft) throws Exception{ //parse all .sol files
        Stack<FileNode> s=new Stack<FileNode>();
        Path p;
        ANTLRInputStream input;
        s.push(ft.root);
        FileNode<String> fn;
        File tf;
        while(!s.isEmpty()){
            fn=s.pop();
            if(fn.path!=null){
                tf=new File(fn.path);
                if(!tf.isDirectory()){
                    //SourceListener cg = new SourceListener(fn);
                    FunctionPreparser fl = new FunctionPreparser();
                    p = FileDirectory.tmp_root.toPath().resolve(fn.path);

                    input = new ANTLRFileStream(p.toString());

                    SolidityLexer lexer = new SolidityLexer(input);
                    CommonTokenStream tokens = new CommonTokenStream(lexer);
                    SolidityParser parser = new SolidityParser(tokens);

                    ParseTree tree = parser.sourceUnit();
                    ParseTreeWalker walker = new ParseTreeWalker();

                    //walker.walk(cg,tree);
                    walker.walk(fl,tree);

                    //System.out.println(tree.toStringTree(parser));
                }

            }


            Iterator<FileNode>f = fn.children.iterator();
            while(f.hasNext()){
                s.push(f.next());
            }
        }
    }
}
