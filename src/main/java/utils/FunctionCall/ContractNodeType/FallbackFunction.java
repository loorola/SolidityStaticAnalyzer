package utils.FunctionCall.ContractNodeType;

import parser.Base.SolidityParser;

import java.util.ArrayList;
import java.util.List;

public class FallbackFunction{
    SolidityParser.BlockContext block;
    public SolidityParser.StateMutabilityContext stateMutability = null;
    public SolidityParser.InheritanceContext inheritanceContext = null;
    public List<Parameter> returnParameterList = new ArrayList<>();

    public void addReturnParameter(String alias, String dataType){
        Parameter p =new Parameter(alias, dataType);
        returnParameterList.add(p);
    }

    public FallbackFunction(SolidityParser.StateMutabilityContext stateMutability, SolidityParser.BlockContext ctx){
        this.stateMutability=stateMutability;
        this.block = ctx;
    }
}