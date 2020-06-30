package utils.Content.ContractNodeType;

import utils.Content.ContractNodeType.StateVariableDeclaration.StateVariableDeclaration;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseNode{
    public String alias = null;
    public List<StateVariableDeclaration> stateVariableDeclarationList = new ArrayList<StateVariableDeclaration>();
}