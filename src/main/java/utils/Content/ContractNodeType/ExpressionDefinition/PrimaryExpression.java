package utils.Content.ContractNodeType.ExpressionDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

public class PrimaryExpression extends Expression {
    public PrimaryExpression(SolidityParser.ExpressionContext ctx) {
        super(ctx, 8);
    }
}
