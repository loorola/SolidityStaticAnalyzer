package utils.Content.ContractNodeType.Statement;

import javafx.util.Pair;
import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.Content.ContractNodeType.BasicContractDefinition.Parameter;
import utils.Content.ContractNodeType.BasicContractDefinition.Statement;

import java.util.ArrayList;
import java.util.List;

public class TryCatchStatement extends Statement {
    public List<Parameter> returnParameterList = new ArrayList<>();
    public String catchAlias=null;
    public List<Pair<String,List<Parameter>>> catchParameterList = new ArrayList<>();
    public Pair<String, String> exceptionText;
    public Block block1;
    public Block block2;
    public TryCatchStatement(Expression e, Block b1, Block b2, SolidityParser.StatementContext ctx){
        super(ctx, 1);
        this.expressionList.add(e);
        block1=b1;
        block2 = b2;
    }
}
