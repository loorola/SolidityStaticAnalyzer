package parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import parser.Base.SolidityLexer;
import parser.Base.SolidityParser;
import utils.File.FileNode;
import utils.File.FileTree;
import config.FileDirectory;
import utils.functionCall.FunctionCallNode;
import utils.functionCall.FunctionCallTree;

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

                    SourceParser cg = new SourceParser(fn);
                    FunctionParser fl = new FunctionParser();
                    p = FileDirectory.tmp_root.toPath().resolve(fn.path);

                    input = new ANTLRFileStream(p.toString());

                    SolidityLexer lexer = new SolidityLexer(input);
                    CommonTokenStream tokens = new CommonTokenStream(lexer);
                    SolidityParser parser = new SolidityParser(tokens);

                    ParseTree tree = parser.sourceUnit();
                    ParseTreeWalker walker = new ParseTreeWalker();

                    walker.walk(cg,tree);
                    //walker.walk(fl,tree);
                    //fn.functionCallTree = fl.ct;
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


    private static class Testing{
        private static void printFunctionCallTree(FunctionCallTree ft){
            FunctionCallNode n = ft.root;
            Queue<FunctionCallNode> q=new LinkedList<FunctionCallNode>();
            q.add(n);
            while(!q.isEmpty()){
                n=q.remove();
                try{
                    if(n.ctx!=null) {
                        String txt=n.toString();
                        System.out.println(txt);
                    }
                    Iterator<FunctionCallNode>f = n.functionCallNodeList.iterator();
                    while(f.hasNext()){
                        q.add(f.next());
                    }
                }catch(Exception e){
                    System.out.println(e);
                }

            }
        }

        private static void printSource(){

        }
    }



}
