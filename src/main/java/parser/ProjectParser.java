package parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.tree.pattern.ParseTreeMatch;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;

import parser.Base.SolidityLexer;
import parser.Base.SolidityParser;
import report.CFG;
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
                    p = FileDirectory.tmp_root.toPath().resolve(fn.path);

                    input = new ANTLRFileStream(p.toString());

                    SolidityLexer lexer = new SolidityLexer(input);
                    CommonTokenStream tokens = new CommonTokenStream(lexer);
                    SolidityParser parser = new SolidityParser(tokens);

                    ParseTree tree = parser.sourceUnit();

                    /*
                    ParseTreePattern pp = parser.compileParseTreePattern("<expression>.<functionCall>",SolidityParser.RULE_expression );
                    ParseTree tt= fn.fileContent.contractList.get(0).functionList.get(0).block.statementList.get(0).expressionList.get(0).expressionContext;
                    List<ParseTreeMatch> matches = pp.findAll(tree, "//expression");
*/

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

    public static void initContextSensitiveParser(FileTree ft)throws Exception{
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
                    ContextSensitiveParser contextSensitiveParser = new ContextSensitiveParser(fn);
                    contextSensitiveParser.initContextSensitive();
                }

            }

            Iterator<FileNode>f = fn.children.iterator();
            while(f.hasNext()){
                s.push(f.next());
            }
        }
        //CFG cfg = new CFG(ft.root);

    }

    public static void initCFG(FileTree ft)throws Exception{
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
                    ContextSensitiveParser contextSensitiveParser = new ContextSensitiveParser(fn);
                    contextSensitiveParser.initCFG();
                }

            }

            Iterator<FileNode>f = fn.children.iterator();
            while(f.hasNext()){
                s.push(f.next());
            }
        }
        //CFG cfg = new CFG(ft.root);

    }

}




