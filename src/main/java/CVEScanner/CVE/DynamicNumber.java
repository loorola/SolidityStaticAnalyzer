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
import org.bouncycastle.math.raw.Mod;
import parser.Base.SolidityLexer;
import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.BaseFunction;
import utils.Content.ContractNodeType.BasicContractDefinition.Modifier;
import utils.Content.ContractNodeType.BasicContractDefinition.Parameter;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;
import utils.File.FileNode;

import java.io.File;
import java.util.*;

public class DynamicNumber extends BaseCVE {
    FileNode fn;
    public DynamicNumber(FileNode fn){
        this.fn = fn;
        xpath="//condition//expression//identifier";
        description = "Loops that do not have a fixed number of iterations, for example, loops that depend on storage values, have to be used carefully: Due to the block gas limit, transactions can only consume a certain amount of gas. Either explicitly or just due to normal operation, the number of iterations in a loop can grow beyond the block gas limit which can cause the complete contract to be stalled at a certain point. This may not apply to view functions that are only executed to read data from the blockchain. Still, such functions may be called by other contracts as part of on-chain operations and stall those. Please be explicit about such cases in the documentation of your contracts.";
        name = "Dynamic Number for Looping";
    }

    public List<String> matchChild(FileNode fn, ParseTree parseTree, List<String>alias, BaseFunction f){
        List<String> error_msg = new ArrayList<>();
        for(int i=0;i<alias.size();i++){
            if(parseTree.getText().equals(alias.get(i))){
                String functionIdentifier;
                if(f.function!=null)functionIdentifier="function";
                else functionIdentifier="modifier";
                error_msg.add("In File <"+fn.path+"> "+functionIdentifier+" "+ f.alias+" parameter <"+parseTree.getText()+"> should not be the condition statement in loop");
                return error_msg;
            }
        }
        return error_msg;
    }

    private static List<String> parameterList2AliasList(List<Parameter>parameterList){
       List<String> aliasList = new ArrayList<>();
       for(int i=0;i<parameterList.size();i++){
           if(parameterList.get(i).alias!=null)aliasList.add(parameterList.get(i).alias);
       }
       return aliasList;
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
                        for (int j = 0; j < functionList.size(); j++) {
                            if (functionList.get(j).getValue().function != null && functionList.get(j).getValue().function.block != null) {
                                int start = functionList.get(j).getValue().function.block.blockContext.start.getStartIndex();
                                int end = functionList.get(j).getValue().function.block.blockContext.stop.getStopIndex();

                                Interval interval = new Interval(start, end);
                                String input = functionList.get(j).getValue().function.block.blockContext.start.getInputStream().getText(interval);

                                SolidityLexer lexer = new SolidityLexer(CharStreams.fromString(input));
                                CommonTokenStream tokens = new CommonTokenStream(lexer);
                                SolidityParser parser = new SolidityParser(tokens);
                                List<String>alias =parameterList2AliasList(functionList.get(j).getValue().parameterList);
                                ParseTree tree = parser.block();
                                try {

                                    Collection<ParseTree> matches = XPath.findAll(tree, xpath, parser);
                                    for (ParseTree t : matches) {
                                        error_msg.addAll(matchChild(functionList.get(j).getKey().fn,t,alias,functionList.get(j).getValue()));
                                    }

                                } catch (Exception e) {
                                    System.out.println(e.toString());
                                }

                                for(int k=0;k<functionList.get(j).getValue().function.modifierList.size();k++){
                                    Modifier m = functionList.get(j).getValue().function.modifiers.get(k);
                                    List<String> al = parameterList2AliasList(m.parameterList);
                                    if(m.block!=null){
                                        int a = m.block.blockContext.start.getStartIndex();
                                        int b=m.block.blockContext.stop.getStopIndex();
                                        Interval interval1 = new Interval(a,b);
                                        String input1 = m.block.blockContext.start.getInputStream().getText(interval1);
                                        SolidityLexer lexer1 = new SolidityLexer(CharStreams.fromString(input1));
                                        CommonTokenStream tokens1 = new CommonTokenStream(lexer1);
                                        SolidityParser parser1 = new SolidityParser(tokens1);
                                        try {

                                            Collection<ParseTree> matches = XPath.findAll(tree, xpath, parser1);
                                            for (ParseTree t : matches) {
                                                error_msg.addAll(matchChild(fn,t,al,m));
                                            }

                                        } catch (Exception e) {
                                            System.out.println(e.toString());
                                        }
                                    }
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
