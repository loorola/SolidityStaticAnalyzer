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
import java.util.*;

public class ProjectParser {

    public static void init(FileTree ft) throws Exception{ //parse all .sol files
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
                    System.out.println("Contract: "+fn.path);

                    ContentParser contentParser = new ContentParser(fn);
                    //ContextSensitiveParser ps = new ContextSensitiveParser(fn);
                    p = FileDirectory.tmp_root.toPath().resolve(fn.path);

                    input = new ANTLRFileStream(p.toString());

                    SolidityLexer lexer = new SolidityLexer(input);
                    CommonTokenStream tokens = new CommonTokenStream(lexer);
                    SolidityParser parser = new SolidityParser(tokens);

                    ParseTree tree = parser.sourceUnit();
                    ParseTreeWalker walker = new ParseTreeWalker();

                    walker.walk(contentParser,tree);
                    //walker.walk(contentParser,tree);
                    //fn.sourceParser = contentParser;
                    //fn.contentParser = contentParser;

                    //Testing.printSource(cg);
                    //walker.walk(contentParser,tree);
                    //fn.functionCallTree = contentParser.ct;
                    //Testing.printFunctionCallTree(fn.functionCallTree);
                    //System.out.println(tree.toStringTree(parser));
                }

            }

            Iterator<FileNode>f = fn.children.iterator();
            while(f.hasNext()){
                s.push(f.next());
            }
        }
    }



        private static void printSource(SourceParser sp){
            System.out.println("Local Source List: ");
            sp.sourceList.forEach(x->{
                System.out.println(x.fn.path+"->"+x.alias);
                if(!x.srcModule.isEmpty()){
                    System.out.println("Used Contract: ");
                    x.srcModule.forEach(y->{
                        System.out.println(y.moduleName);
                    });
                }else{
                    System.out.println("all");
                }
            });
            System.out.println();
            System.out.println("Global Source List: ");
            sp.globalSourceList.forEach(x->{
                System.out.println(x.path+"->"+x.alias);
                if(!x.srcModule.isEmpty()){
                    System.out.println("Used Contract: ");
                    x.srcModule.forEach(y->{
                        System.out.println(y.moduleName);
                    });
                }else{
                    System.out.println("all");
                }
            });
        }
}




