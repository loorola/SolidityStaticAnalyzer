package CVEScanner.CVE;

import CVEScanner.BaseCVE;
import CVEScanner.FunctionGetter;
import CVEScanner.FileScanner;
import javafx.util.Pair;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.xpath.XPath;
import parser.Base.SolidityLexer;
import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.BaseFunction;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;
import utils.File.FileNode;

import java.io.File;
import java.util.*;

public class Reentrancy extends BaseCVE {
    FileNode fn;
    public Reentrancy(FileNode fn){
        name = "Re-entrancy";
        description = "This exploit was missed in review so many times by so many different people: reviewers tend to review functions one at a time, and assume that calls to secure subroutines will operate securely and as intended.";
        xpath = "//functionCall";
        this.fn = fn;

    }

    public List<String> matchChild(FileNode n, ParseTree parseTree) throws Exception {
        List<String> error_msg = new ArrayList<>();
        if(parseTree.getChild(0).getText().equals("call")&&parseTree.getChild(1).getText().contains(".value")){
            int lineNumber = FileScanner.FileMatchLine(parseTree.getText(),n);
            error_msg.add("In File <"+n.path+"> "+"line: "+lineNumber+" <"+parseTree.getSourceInterval().a+", "+parseTree.getSourceInterval().b+"> "+ parseTree.getText() +"\n"+name+" is found.\n"+description);
        }
        return error_msg;
    }

    public List<String> scan() {
        List<String> error_msg = new ArrayList<>();

        Stack<FileNode> s=new Stack<FileNode>();
        s.push(fn);
        FileNode f;
        File tf;
        while(!s.isEmpty()){
            fn=s.pop();
            if(fn.path!=null){
                tf=new File(fn.path);
                if(!tf.isDirectory()){
                    for(int i=0;i<fn.fileContent.contractList.size();i++){
                        List<Pair <Instance,BaseFunction>> functionList = FunctionGetter.getFunctionListWithFunctionCall(fn.fileContent.contractList.get(i), true);
                        for (int j = 0; j < functionList.size(); j++) {
                            if (functionList.get(j).getValue().function != null && functionList.get(j).getValue().function.block != null) {
                                int start = functionList.get(j).getValue().function.block.blockContext.start.getStartIndex();
                                int end = functionList.get(j).getValue().function.block.blockContext.stop.getStopIndex();

                                Interval interval = new Interval(start, end);
                                String input = functionList.get(j).getValue().function.block.blockContext.start.getInputStream().getText(interval);

                                SolidityLexer lexer = new SolidityLexer(CharStreams.fromString(input));
                                CommonTokenStream tokens = new CommonTokenStream(lexer);
                                SolidityParser parser = new SolidityParser(tokens);
                                ParseTree tree = parser.block();
                                try {

                                    Collection<ParseTree> matches = XPath.findAll(tree, xpath, parser);
                                    for (ParseTree t : matches) {
                                        error_msg.addAll(matchChild(functionList.get(j).getKey().fn,t));
                                    }

                                } catch (Exception e) {
                                    System.out.println(e.toString());
                                }
                            }
                        }
                    }
                }
            }
            Iterator<FileNode> itr = fn.children.iterator();
            while(itr.hasNext()){
                s.push(itr.next());
            }
        }
        return error_msg;
    }

}
