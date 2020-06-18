package utils.Content.ContractNodeType.ExpressionDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

public class FunctionIdentifier extends Expression {
    Expression front;
    String identifier;  //length or balance or identifier

    public FunctionIdentifier(String identifier, SolidityParser.ExpressionContext ctx, Expression e) {
        super(ctx, 5);
        this.identifier=identifier;
        this.front = e;
    }
}
