package utils.Content.ContractNodeType.Statement;

import javafx.util.Pair;
import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.Content.ContractNodeType.BasicContractDefinition.Statement;

import java.util.ArrayList;
import java.util.List;

public class emitEventStatement extends Statement {
    public List<Pair<String, Expression>> nameValueList = new ArrayList<>();

    public emitEventStatement(SolidityParser.StatementContext ctx, int type) {
        super(ctx, 5);
    }
}
