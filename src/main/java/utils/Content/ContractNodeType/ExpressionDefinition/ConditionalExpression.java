package utils.Content.ContractNodeType.ExpressionDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

public class ConditionalExpression extends Expression {

    public ConditionalExpression(SolidityParser.ExpressionContext ctx) {
        super(ctx, 14);
    }
}
