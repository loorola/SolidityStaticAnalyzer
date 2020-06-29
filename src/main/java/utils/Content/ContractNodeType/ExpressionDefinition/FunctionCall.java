package utils.Content.ContractNodeType.ExpressionDefinition;

import javafx.util.Pair;
import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.BaseFunction;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;
import utils.Content.ContractNodeType.StateVariableDeclaration.FunctionVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FunctionCall extends Expression{
    public Expression front;
    public Pair<Instance, BaseFunction> functionCallContext;
    public FunctionCall(String functionName, Expression front, SolidityParser.ExpressionContext ctx) {
        super(ctx, 4);
        this.front = front;
        this.functionName=functionName;
    }

    public List<Pair<String, Expression>> nameValueList = new ArrayList<>();
    public String functionName;

}
