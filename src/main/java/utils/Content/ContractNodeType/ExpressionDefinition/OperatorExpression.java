package utils.Content.ContractNodeType.ExpressionDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

public class OperatorExpression extends Expression {
    String operatorType;

    public OperatorExpression(String operatorType,Expression left, Expression right, SolidityParser.ExpressionContext ctx) {
        super(ctx, 10);
        this.operatorType=operatorType;
        this.expressionList.add(left);
        this.expressionList.add(right);
    }
}
