package utils.Content.ContractNodeType.SolidityClassDefinition;

import parser.Base.SolidityParser;

import java.util.ArrayList;
import java.util.List;

public class Interface extends Instance{
    SolidityParser.InterfaceDefinitionContext ctx;
    public List<String> inheritanceList = new ArrayList<>();
    public Interface(String alias, SolidityParser.InterfaceDefinitionContext ctx){
        this.alias = alias;
        this.ctx=ctx;
    }

    public void addInheritance(String identifier){
        inheritanceList.add(identifier);
    }
}