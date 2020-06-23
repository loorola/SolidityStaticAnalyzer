package utils.Content.ContractNodeType.Statement;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Statement;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement extends Statement {
    public List<Statement> statementList = new ArrayList<>();

    public BlockStatement(SolidityParser.StatementContext ctx){
        super(ctx, 7);
        this.ctx=ctx;
    }
}
