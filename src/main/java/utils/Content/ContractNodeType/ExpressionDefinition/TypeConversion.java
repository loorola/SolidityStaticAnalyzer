package utils.Content.ContractNodeType.ExpressionDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

public class TypeConversion extends Expression {
    String dataType;


    public TypeConversion(String dataType, SolidityParser.ExpressionContext ctx) {
        super(ctx, 9);
        this.dataType = dataType;
    }
}
