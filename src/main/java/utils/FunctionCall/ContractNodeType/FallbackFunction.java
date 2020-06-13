package utils.FunctionCall.ContractNodeType;

import parser.Base.SolidityParser;

import java.util.ArrayList;
import java.util.List;

public class FallbackFunction extends BaseNode{
    public String alias = null;
    public SolidityParser.BlockContext block;
    public String stateMutability = null;
    public SolidityParser.InheritanceContext inheritanceContext = null;
    public List<String> returnParameterList = new ArrayList<>();

    public FallbackFunction(SolidityParser.BlockContext ctx){
        this.block = ctx;
    }

    public void addReturnParameter(String dataType){
        returnParameterList.add(dataType);
    }
}