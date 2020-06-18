package utils.Content.ContractNodeType.ExpressionDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

public class TupleExpression extends Expression {

    public TupleExpression(SolidityParser.ExpressionContext ctx, int type) {
        super(ctx, 6);
    }
}
