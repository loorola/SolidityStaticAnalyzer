package utils.Content.ContractNodeType.ExpressionDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.Context.VariableDataType.FunctionVariableDataType;
import utils.Context.VariableDataType.InstanceVariableDataType;

public class VariableDeclaration extends Expression {
    InstanceVariableDataType instanceVariableDataType;
    String dataType;
    String storageLocation;
    String alias;

    public VariableDeclaration(String dataType, String storageLocation, String alias, SolidityParser.ExpressionContext ctx){
        super(ctx,11);
        this.dataType=dataType;
        this.storageLocation=storageLocation;
        this.alias=alias;
    }
}
