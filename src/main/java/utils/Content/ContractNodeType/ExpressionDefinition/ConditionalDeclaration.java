package utils.Content.ContractNodeType.ExpressionDefinition;
import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

public class ConditionalDeclaration extends Expression {

    public ConditionalDeclaration(Expression a, Expression b, Expression c, SolidityParser.ExpressionContext ctx) {
        super(ctx, 13);
        this.expressionList.add(a);
        this.expressionList.add(b);
        this.expressionList.add(c);
    }
}
