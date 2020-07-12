package CVEScanner.CVE;

import CVEScanner.BaseCVE;
import CVEScanner.Scanner;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.pattern.ParseTreeMatch;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.antlr.v4.runtime.tree.xpath.XPath;
import parser.Base.SolidityLexer;
import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;
import utils.File.FileNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Reentrancy extends BaseCVE {
    FileNode fn;
    public Reentrancy(FileNode fn){
        name = "Reentrancy";
        description = "This exploit was missed in review so many times by so many different people: reviewers tend to review functions one at a time, and assume that calls to secure subroutines will operate securely and as intended.";
        xpath = "//functionCall";
        this.fn = fn;
    }

    public void matchChild(ParseTree parseTree) throws Exception {
        if(parseTree.getChild(0).getText().equals("call")&&parseTree.getChild(1).getText().contains(".value")){
            int lineNumber = Scanner.FileMatchLine(parseTree.getText(),fn);
            System.out.println("line: "+lineNumber+" <"+parseTree.getSourceInterval().a+", "+parseTree.getSourceInterval().b+"> "+ parseTree.getText() +"\n"+name+" is found.\n"+description);
        }
    }

    public void scan(){
        for(int i=0;i<fn.fileContent.contractList.size();i++){
            for(int j=0;j<fn.fileContent.contractList.get(i).functionList.size();j++){
                if(fn.fileContent.contractList.get(i).functionList.get(j).block!=null){
                    int start = fn.fileContent.contractList.get(i).functionList.get(j).block.blockContext.start.getStartIndex();
                    int end = fn.fileContent.contractList.get(i).functionList.get(j).block.blockContext.stop.getStopIndex();

                    Interval interval = new Interval(start,end);
                    String input = fn.fileContent.contractList.get(i).functionList.get(j).block.blockContext.start.getInputStream().getText(interval);

                    SolidityLexer lexer = new SolidityLexer(CharStreams.fromString(input));
                    CommonTokenStream tokens = new CommonTokenStream(lexer);
                    SolidityParser parser = new SolidityParser(tokens);
                    ParseTree tree = parser.block();

                    try{

                        //ParseTreePattern p = parser.compileParseTreePattern(pattern,SolidityParser.RULE_statement);
                        Collection<ParseTree> matches = XPath.findAll(tree, xpath,parser);
                        //List<ParseTreeMatch> result =p.findAll(tree,xpath);
                        //System.out.println(result);

                        for (ParseTree t : matches) {

                            System.out.println(t.getText());
                            matchChild(t);
                        }

                    }catch(Exception e){
                        System.out.println(e.toString());
                    }

                    }
                }
            }

        }

}
