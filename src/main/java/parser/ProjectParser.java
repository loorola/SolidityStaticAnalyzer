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
    public static List<SourceParser>cg_list = new ArrayList<>();

    private static String ctxToString(FunctionCallNode.Type type,ParserRuleContext ctx){
        String txt="nil";
        switch(type){
            case CONTRACT:
            case LIBRARY:
                txt=ctx.getChild(SolidityParser.IdentifierContext.class,0).getText();
                break;
            case FUNCTION: {
                if(ctx.getChild(SolidityParser.ConstructorIdentifierContext.class,0)!=null){
                    txt="constructor";
                }else{
                    txt=ctx.getChild(SolidityParser.ConstructorIdentifierContext.class,0).getText();
                }
            }
            break;
            case USINGFOR: {
                txt=ctx.getText();
            }
            break;
            case FALLBACK:
                txt=ctx.getChild(SolidityParser.FallbackIdentifierContext.class,0).getText();
                break;
            case EXPRESSION:
                txt=ctx.getText();
                break;
            case MODIFIER:
                txt=ctx.getChild(SolidityParser.IdentifierContext.class,0).getText();
                break;
            default: break;
        }
        return txt;
    }

    private static void printFunctionCallTree(FunctionCallTree ft){
        FunctionCallNode n = ft.root;
        Queue<FunctionCallNode> q=new LinkedList<FunctionCallNode>();
        q.add(n);
        while(!q.isEmpty()){
            n=q.remove();
            try{
                if(n.ctx!=null) {
                    String txt=ctxToString(n.type,n.ctx);
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
        /*
        s.push(ft.root);
        while(!s.isEmpty()){
            n=s.pop();
            if(n.ctx!=null){
                System.out.println(n.ctx.getText());
            }
            Iterator<FunctionCallNode>f = n.functionCallNodeList.iterator();
            while(f.hasNext()){
                s.push(f.next());
            }
        }

         */
    }

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
                    System.out.println(fn.path);

                    //SourceListener cg = new SourceListener(fn);
                    FunctionParser fl = new FunctionParser();
                    p = FileDirectory.tmp_root.toPath().resolve(fn.path);

                    input = new ANTLRFileStream(p.toString());

                    SolidityLexer lexer = new SolidityLexer(input);
                    CommonTokenStream tokens = new CommonTokenStream(lexer);
                    SolidityParser parser = new SolidityParser(tokens);

                    ParseTree tree = parser.sourceUnit();
                    ParseTreeWalker walker = new ParseTreeWalker();

                    //walker.walk(cg,tree);
                    walker.walk(fl,tree);
                    fn.functionCallTree = fl.ct;

                    printFunctionCallTree(fn.functionCallTree);
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
