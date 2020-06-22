package utils.Content.ContractNodeType.ExpressionDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

public class TypeExpression extends Expression {

    public TypeExpression(Expression e, SolidityParser.ExpressionContext ctx) {
        super(ctx, 7);
        expressionList.add(e);
    }
}
