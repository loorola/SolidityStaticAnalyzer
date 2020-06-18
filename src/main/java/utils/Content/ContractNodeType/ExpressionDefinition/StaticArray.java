package utils.Content.ContractNodeType.ExpressionDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

public class StaticArray extends Expression {
    boolean hasColon;
    Expression left;
    Expression right;

    Expression front;
    public StaticArray(boolean hasColon, Expression e1, Expression e2, Expression e3, SolidityParser.ExpressionContext ctx) {
        super(ctx, 1);
        this.hasColon=hasColon;
        this.left=e1;
        this.right=e2;
        this.front = e3;
    }
}
