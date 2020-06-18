package utils.Content.ContractNodeType.BasicContractDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BaseNode;

import java.util.ArrayList;
import java.util.List;

public class Struct extends BaseNode {
    List<SolidityParser.VariableDeclarationContext> variableDeclarationContextList = new ArrayList<>();

    public Struct(String alias){
        this.alias = alias;
    }

    public void addVariableDeclaration(SolidityParser.VariableDeclarationContext ctx){
        variableDeclarationContextList.add(ctx);
    }
}