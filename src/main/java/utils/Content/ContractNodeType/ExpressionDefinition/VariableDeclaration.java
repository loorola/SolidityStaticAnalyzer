package utils.Content.ContractNodeType.ExpressionDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.Context.VariableType;

import java.util.ArrayList;
import java.util.List;

public class VariableDeclaration extends Expression {
    public SolidityParser.TypeNameContext dataType;
    public VariableType variableType;
    public String storageLocation;
    public String alias;


    public VariableDeclaration(SolidityParser.TypeNameContext dataType, String storageLocation, String alias, SolidityParser.ExpressionContext ctx){
        super(ctx,11);
        this.dataType=dataType;
        this.storageLocation=storageLocation;
        this.alias=alias;
    }
}
