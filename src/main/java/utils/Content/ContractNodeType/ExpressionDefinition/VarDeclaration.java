package utils.Content.ContractNodeType.ExpressionDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

public class VarDeclaration extends Expression {
    public VarDeclaration(Expression e, SolidityParser.ExpressionContext ctx) {
        super(ctx, 12);
    }
}
