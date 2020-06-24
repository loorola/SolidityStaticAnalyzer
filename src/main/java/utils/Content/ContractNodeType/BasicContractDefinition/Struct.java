package utils.Content.ContractNodeType.BasicContractDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BaseNode;

import java.util.ArrayList;
import java.util.List;

public class Struct extends BaseNode {
    public List<SolidityParser.VariableDeclarationContext> variableDeclarationContextList = new ArrayList<>();
    public SolidityParser.StructDefinitionContext ctx;
    public Struct(String alias, SolidityParser.StructDefinitionContext ctx){
        this.alias = alias;
        this.ctx = ctx;
    }

    public void addVariableDeclaration(SolidityParser.VariableDeclarationContext ctx){
        variableDeclarationContextList.add(ctx);
    }
}