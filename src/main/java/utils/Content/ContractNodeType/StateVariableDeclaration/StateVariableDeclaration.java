package utils.Content.ContractNodeType.StateVariableDeclaration;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BaseNode;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.Content.ContractNodeType.BasicContractDefinition.Parameter;
import utils.Context.VariableDataType.InstanceVariableDataType;

import java.util.ArrayList;
import java.util.List;

public class StateVariableDeclaration extends BaseNode {
    public Expression expression;
    public String visibleType;
    boolean isConstant;
    InstanceVariableDataType instanceVariableDataType;

    public StateVariableDeclaration(String alias, String visibleType, boolean isConstant, Expression e){
        this.alias=alias;
        this.visibleType = visibleType;
        this.isConstant = isConstant;
        this.expression=e;
    }
}