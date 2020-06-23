package utils.Content.ContractNodeType.Statement;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Statement;

public class InlineAssemblyStatement extends Statement {

    public InlineAssemblyStatement(SolidityParser.StatementContext ctx, int type) {
        super(ctx, 2);
    }
}
