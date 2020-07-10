package utils.Content.ContractNodeType.StateVariableDeclaration;

import utils.Content.ContractNodeType.BasicContractDefinition.BaseFunction;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.Content.ContractNodeType.BasicContractDefinition.Parameter;

import java.util.ArrayList;
import java.util.List;

public class FunctionVariable extends StateVariableDeclaration {
    public List<String> returnParameterList = new ArrayList<>();
    public List<Parameter> parameterList = new ArrayList<>();

    public String functionVisibleType;
    public String stateMutability;

    public FunctionVariable(String previousNodeName,String alias, String visibleType, String stateMutability, String functionVisibleType, boolean isConstant, Expression e){
        super(previousNodeName, alias, visibleType, isConstant, e);
        nodeName = "function "+alias+stateMutability+" "+visibleType+isConstant;
        this.stateMutability=stateMutability;
        this.functionVisibleType = functionVisibleType;
    }
}
