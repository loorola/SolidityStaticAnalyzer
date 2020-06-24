package utils.Content.ContractNodeType.BasicContractDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BaseNode;

import java.util.ArrayList;
import java.util.List;

public class Struct extends BaseNode {
    public class VariableDeclaration{
        String dataType;
        String storageLocation;
        String alias;
        SolidityParser.VariableDeclarationContext ctx;
        public VariableDeclaration(String dataType, String storageLocation, String alias, SolidityParser.VariableDeclarationContext ctx){
            this.dataType = dataType;
            this.storageLocation = storageLocation;
            this.alias = alias;
            this.ctx = ctx;
        }
    }

    public List<VariableDeclaration> variableDeclarationList = new ArrayList<>();
    public SolidityParser.StructDefinitionContext ctx;
    public Struct(String alias, SolidityParser.StructDefinitionContext ctx){
        this.alias = alias;
        this.ctx = ctx;
    }

    public void addVariableDeclaration(String dataType, String storageLocation, String alias, SolidityParser.VariableDeclarationContext ctx){
        VariableDeclaration v = new VariableDeclaration(dataType, storageLocation, alias, ctx);
        variableDeclarationList.add(v);
    }

    @Override
    public String toString() {
        return "Struct{" +
                "variableDeclarationContextList=" + variableDeclarationList +
                ", ctx=" + ctx +
                '}';
    }
}