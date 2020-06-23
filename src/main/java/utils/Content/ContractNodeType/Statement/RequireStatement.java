package utils.Content.ContractNodeType.Statement;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.Content.ContractNodeType.BasicContractDefinition.Statement;

public class RequireStatement extends Statement {

    public RequireStatement(Expression e, SolidityParser.StatementContext ctx) {
        super(ctx, 6);
        this.expressionList.add(e);
    }
}
