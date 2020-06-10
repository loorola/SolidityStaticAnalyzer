package utils.FunctionCall.ContractNodeType;

import parser.Base.SolidityParser;
import utils.FunctionCall.ContractNodeType.Instance;

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