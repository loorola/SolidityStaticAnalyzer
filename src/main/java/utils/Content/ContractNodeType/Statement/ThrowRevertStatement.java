package utils.Content.ContractNodeType.Statement;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Statement;

public class ThrowRevertStatement extends Statement {
    public ThrowRevertStatement(SolidityParser.StatementContext ctx) {
        super(ctx, 4);
    }
}
