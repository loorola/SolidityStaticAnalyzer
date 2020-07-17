package utils.Content.ContractNodeType.SolidityClassDefinition;

import parser.Base.SolidityParser;
import utils.Context.InheritanceType;

import java.util.ArrayList;
import java.util.List;

public class Contract extends Instance {
    public SolidityParser.ContractDefinitionContext ctx;
    public List<InheritanceType> inheritanceTypeList = new ArrayList<>();
    public List<SolidityParser.InheritanceSpecifierContext> inheritanceList = new ArrayList<>();

    public Contract(String filePath, String alias, SolidityParser.ContractDefinitionContext ctx){
        initCFGNode(filePath,"contract "+alias);
        this.alias = alias;
        this.ctx=ctx;
    }

    public void addInheritance(SolidityParser.InheritanceSpecifierContext identifier){
        inheritanceList.add(identifier);
    }
}