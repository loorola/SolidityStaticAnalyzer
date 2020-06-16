package utils.FunctionCall.ContractNodeType.SolidityClassDefinition;

import parser.Base.SolidityParser;

import java.util.ArrayList;
import java.util.List;

public class Contract extends Instance {
    SolidityParser.ContractDefinitionContext ctx;
    public List<String> inheritanceList = new ArrayList<>();
    public Contract(String alias, SolidityParser.ContractDefinitionContext ctx){
        this.alias = alias;
        this.ctx=ctx;
    }

    public void addInheritance(String identifier){
        inheritanceList.add(identifier);
    }
}