package CVEScanner.CVE;

import CVEScanner.BaseCVE;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.xpath.XPath;
import parser.Base.SolidityLexer;
import parser.Base.SolidityParser;
import utils.File.FileNode;

import java.util.Collection;

public class ArithmeticIssues extends BaseCVE {
    public ArithmeticIssues(){
        name = "Arithmetic Issues";
        description = "An overflow condition gives incorrect results and, particularly if the possibility has not been anticipated, can compromise a program’s reliability and security.";
    }

    public void matchChild(ParseTree parseTree){
        if(parseTree.getChild(0).getText().equals("call")&&parseTree.getChild(1).getText().contains("value")){
            System.out.println("bad");
        }
    }

    public void scan(FileNode fn){
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
                    ParseTree tree = parser.statement();

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
