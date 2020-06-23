package utils.Content.ContractNodeType.Statement;

import javafx.util.Pair;
import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.Content.ContractNodeType.BasicContractDefinition.Statement;

import java.util.ArrayList;
import java.util.List;

public class EmitEventStatement extends Statement {
    public Pair<String,String> alias = null;
    public List<Pair<String, Expression>> nameValueList = new ArrayList<>();

    public EmitEventStatement(SolidityParser.StatementContext ctx) {
        super(ctx, 5);
    }
}
