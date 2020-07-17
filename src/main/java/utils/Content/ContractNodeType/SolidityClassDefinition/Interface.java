package utils.Content.ContractNodeType.SolidityClassDefinition;

import parser.Base.SolidityParser;
import utils.Context.InheritanceType;

import java.util.ArrayList;
import java.util.List;

public class Interface extends Instance{
    public SolidityParser.InterfaceDefinitionContext ctx;
    public List<SolidityParser.InheritanceSpecifierContext> inheritanceList = new ArrayList<>();
    public List<InheritanceType> inheritanceTypeList = new ArrayList<>();

    public Interface(String filePath, String alias, SolidityParser.InterfaceDefinitionContext ctx){
        initCFGNode(filePath,"interface "+alias);

        this.alias = alias;
        this.ctx=ctx;
    }

    public void addInheritance(SolidityParser.InheritanceSpecifierContext identifier){
        inheritanceList.add(identifier);
    }
}