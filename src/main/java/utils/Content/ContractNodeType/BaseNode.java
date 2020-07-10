package utils.Content.ContractNodeType;

import utils.Content.ContractNodeType.ExpressionDefinition.VariableDeclaration;
import utils.Content.ContractNodeType.StateVariableDeclaration.StateVariableDeclaration;
import utils.Context.InheritanceType;
import utils.Context.VariableType;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseNode{
    public String alias = null;
    public String nodeName;
    public String label;
    public List<InheritanceType>inheritanceTypeList = new ArrayList<>();
    public List<VariableType> variableTypeList = new ArrayList<>();

}