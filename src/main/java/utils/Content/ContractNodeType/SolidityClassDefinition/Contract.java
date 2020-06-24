package utils.Content.ContractNodeType.SolidityClassDefinition;

import parser.Base.SolidityParser;

import java.util.ArrayList;
import java.util.List;

public class Contract extends Instance {
    SolidityParser.ContractDefinitionContext ctx;
    public List<SolidityParser.InheritanceSpecifierContext> inheritanceList = new ArrayList<>();
    public Contract(String alias, SolidityParser.ContractDefinitionContext ctx){
        this.alias = alias;
        this.ctx=ctx;
    }

    public void addInheritance(SolidityParser.InheritanceSpecifierContext identifier){
        inheritanceList.add(identifier);
    }
}