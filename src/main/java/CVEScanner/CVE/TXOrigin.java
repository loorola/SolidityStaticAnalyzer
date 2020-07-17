package CVEScanner.CVE;

import CVEScanner.BaseCVE;
import CVEScanner.FileScanner;
import CVEScanner.FunctionGetter;
import javafx.util.Pair;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.xpath.XPath;
import parser.Base.SolidityLexer;
import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.BaseFunction;
import utils.Content.ContractNodeType.BasicContractDefinition.Modifier;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;
import utils.File.FileNode;

import java.io.File;
import java.util.*;

public class TXOrigin extends BaseCVE {
    FileNode fn;

    public TXOrigin(FileNode fn){
        this.fn=fn;
        name="tx.origin Authorization";
        description="If your wallet had checked msg.sender for authorization, it would get the address of the attack wallet, instead of the owner address. But by checking tx.origin, it gets the original address that kicked off the transaction, which is still the owner address. The attack wallet instantly drains all your funds.";
        xpath="//requireStatement/expression";
    }

    public List<String> matchChild(FileNode n, ParseTree parseTree) throws Exception {
        List<String> error_msg = new ArrayList<>();
        if(parseTree.getText().contains("tx.origin")){
            int lineNumber = FileScanner.FileMatchLine(parseTree.getText(),n);
            error_msg.add("In File <"+n.path+"> "+"line: "+lineNumber+" <"+parseTree.getSourceInterval().a+", "+parseTree.getSourceInterval().b+"> "+ parseTree.getText() +"\n"+name+" is found.\n"+description);
        }
        return error_msg;
    }

    @Override
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
                        List<Pair<Instance, BaseFunction>> functionList = FunctionGetter.getFunctionListWithFunctionCall(fn.fileContent.contractList.get(i), true);
                        List<Modifier> modifierList = FunctionGetter.getModifierFromFunction(functionList);
                        for(int j=0;j<modifierList.size();j++){
                            if(modifierList.get(j).block!=null){
                                int start = modifierList.get(j).block.blockContext.start.getStartIndex();
                                int end = modifierList.get(j).block.blockContext.stop.getStopIndex();

                                Interval interval = new Interval(start, end);
                                String input = modifierList.get(j).block.blockContext.start.getInputStream().getText(interval);

                                SolidityLexer lexer = new SolidityLexer(CharStreams.fromString(input));
                                CommonTokenStream tokens = new CommonTokenStream(lexer);
                                SolidityParser parser = new SolidityParser(tokens);
                                ParseTree tree = parser.block();
                                try {

                                    Collection<ParseTree> matches = XPath.findAll(tree, xpath, parser);
                                    for (ParseTree t : matches) {
                                        error_msg.addAll(matchChild(fn,t));
                                    }

                                } catch (Exception e) {
                                    System.out.println(e.toString());
                                }
                            }
                        }

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
                                        //System.out.println(t.getText());
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
