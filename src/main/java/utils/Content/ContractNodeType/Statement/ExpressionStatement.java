package utils.Content.ContractNodeType.Statement;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.Content.ContractNodeType.BasicContractDefinition.Statement;

public class ExpressionStatement extends Statement {
    Expression e;
    public ExpressionStatement(Expression e, SolidityParser.StatementContext ctx){
        super(ctx, 8);
        this.e=e;
    }
}
