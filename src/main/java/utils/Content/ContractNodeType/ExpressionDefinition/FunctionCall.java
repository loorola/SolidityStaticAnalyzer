package utils.Content.ContractNodeType.ExpressionDefinition;

import javafx.util.Pair;
import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

import java.util.HashMap;

public class FunctionCall extends Expression{
    Expression front;
    public FunctionCall(String functionName, Expression front, SolidityParser.ExpressionContext ctx) {
        super(ctx, 4);
        this.front = front;
        this.functionName=functionName;
    }

    Pair<String, Expression> callArguments;
    String functionName;

}
