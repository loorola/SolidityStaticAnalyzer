package utils.Content.ContractNodeType.Statement;

import javafx.util.Pair;
import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.Content.ContractNodeType.BasicContractDefinition.Statement;

import java.util.ArrayList;
import java.util.List;

public class TryCatchStatement extends Statement {
    public List<String> returnParameterList = new ArrayList<>();
    public List<Pair<String,String>> catchParameter = new ArrayList<>();
    public Pair<String, String> exceptionText;

    public TryCatchStatement(Expression e, BlockStatement b, SolidityParser.StatementContext ctx){
        super(ctx, 1);
        this.expressionList.add(e);
    }
}
