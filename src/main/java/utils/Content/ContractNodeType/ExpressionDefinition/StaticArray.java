package utils.Content.ContractNodeType.ExpressionDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

public class StaticArray extends Expression {
    public StaticArray(SolidityParser.ExpressionContext ctx, Expression e) {
        super(ctx, 1);
        expressionList.add(e);
    }
}
