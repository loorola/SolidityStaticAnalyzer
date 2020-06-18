package utils.Content.ContractNodeType.ExpressionDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

public class Equality extends Expression {
    Expression left;
    Expression right;
    public Equality(SolidityParser.ExpressionContext ctx, Expression left, Expression right) {
        super(ctx, 0);
        this.left=left;
        this.right = right;
    }


}
