package utils.Content.ContractNodeType.Statement;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.Content.ContractNodeType.BasicContractDefinition.Statement;

public class ConditionalStatement extends Statement {
    public Expression condition;
    public ConditionalStatement(Expression condition, SolidityParser.StatementContext ctx) {
        super(ctx, 0);
        this.condition = condition;
        conditionalStatement=this;
    }
}
