package utils.Content.ContractNodeType.StateVariableDeclaration;

import utils.Content.ContractNodeType.BaseNode;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.Content.ContractNodeType.ExpressionDefinition.PrimaryExpression;
import utils.Context.ContextDataType;

import java.util.ArrayList;
import java.util.List;

public class StateVariableDeclaration extends BaseNode {
    public List<ContextDataType> contextDataType = new ArrayList<>();
    public Expression expression;
    public String visibleType;
    boolean isConstant;
    public FunctionVariable functionVariable = null;
    public MappingVariable mappingVariable = null;
    public PrimaryVariable primaryVariable = null;
    public List<StateVariableDeclaration> stateVariableDeclarationList = new ArrayList<>();

    public StateVariableDeclaration(String alias, String visibleType, boolean isConstant, Expression e){
        this.alias=alias;
        this.visibleType = visibleType;
        this.isConstant = isConstant;
        this.expression=e;
    }

    public void initFunctionVariable(FunctionVariable f){
        functionVariable = f;
    }

    public void initMappingVariable(MappingVariable m){
        mappingVariable = m;
    }

    public void initPrimaryVariable(PrimaryVariable p){
        primaryVariable=p;
    }

}