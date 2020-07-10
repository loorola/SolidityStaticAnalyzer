package utils.Content.ContractNodeType.Statement;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Statement;
import utils.Content.ContractNodeType.ExpressionDefinition.VariableDeclaration;
import utils.Content.ContractNodeType.StateVariableDeclaration.StateVariableDeclaration;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement extends Statement {

    public BlockStatement(SolidityParser.StatementContext ctx){
        super(ctx, 7);
        this.ctx=ctx;
        blockStatement=this;
    }
}
