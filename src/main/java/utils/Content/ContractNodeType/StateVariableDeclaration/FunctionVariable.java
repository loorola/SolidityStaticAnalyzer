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
    public String stateMutability = null;

    public FunctionVariable(String alias, String visibleType, String stateMutability, String functionVisibleType, boolean isConstant, Expression e){
       super(alias, visibleType, isConstant, e);
       this.stateMutability=stateMutability;
       this.functionVisibleType = functionVisibleType;
    }
}
