package utils.FunctionCall.ContractNodeType.ExpressionDefinition;

import parser.Base.SolidityParser;
import utils.FunctionCall.ContractNodeType.Expression;

public class Equality extends Expression {
    Expression left;
    Expression right;

    public Equality(SolidityParser.ExpressionContext ctx, Expression left, Expression right) {
        super(ctx, 0);
        this.left=left;
        this.right = right;
    }


}
