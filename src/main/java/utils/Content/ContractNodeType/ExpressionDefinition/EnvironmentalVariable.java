package utils.Content.ContractNodeType.ExpressionDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

public class EnvironmentalVariable extends Expression {
    public EnvironmentalVariable( SolidityParser.ExpressionContext ctx, Expression e) {
        super(ctx, 3);
        if(e!=null) this.expressionList.add(e);
    }
}
