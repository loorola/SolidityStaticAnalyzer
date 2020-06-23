package utils.Content.ContractNodeType.Statement;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Statement;

public class SingleStatement extends Statement {
    public SingleStatement(SolidityParser.StatementContext ctx) {
        super(ctx, 3);
    }
}
