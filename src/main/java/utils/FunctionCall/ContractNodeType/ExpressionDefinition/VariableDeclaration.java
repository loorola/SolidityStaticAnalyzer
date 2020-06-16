package utils.FunctionCall.ContractNodeType.ExpressionDefinition;

import parser.Base.SolidityParser;
import utils.FunctionCall.ContractNodeType.Expression;

import java.util.List;

public class VariableDeclaration extends Expression {
    String dataType;
    String storageLocation;
    String alias;

    VariableDeclaration(String dataType, String storageLocation, String alias, SolidityParser.ExpressionContext ctx, int type){
        super(ctx,type);
        this.dataType=dataType;
        this.storageLocation=storageLocation;
        this.alias=alias;
    }
}
