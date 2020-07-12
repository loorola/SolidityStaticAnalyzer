package utils.Content.ContractNodeType.BasicContractDefinition;

import javafx.util.Pair;
import utils.Content.ContractNodeType.BaseNode;
import utils.Content.ContractNodeType.ExpressionDefinition.VariableDeclaration;
import utils.Content.ContractNodeType.FunctionDefinition.FallbackFunction;
import utils.Content.ContractNodeType.FunctionDefinition.Function;
import utils.Content.ContractNodeType.FunctionDefinition.FunctionInheritance;
import utils.Content.ContractNodeType.Statement.Block;
import utils.Context.VariableContext.FunctionCall;

import java.util.ArrayList;
import java.util.List;

public class BaseFunction extends BaseNode {
    public List<Parameter> parameterList = new ArrayList<>();
    public List<FunctionInheritance> functionInheritanceList = new ArrayList<>();
    public Function function=null;
    public FallbackFunction fallbackFunction = null;
    public Modifier modifier = null;
    public List<FunctionCall> functionCallList = new ArrayList<>();
    public List<VariableDeclaration> variableDeclarationList = new ArrayList<>();
    public boolean visited = false;

}
