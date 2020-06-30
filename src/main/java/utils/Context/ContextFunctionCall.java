package utils.Context;

import utils.Content.ContractNodeType.BasicContractDefinition.BaseFunction;
import utils.Content.ContractNodeType.SolidityClassDefinition.Contract;
import utils.Content.ContractNodeType.StateVariableDeclaration.StateVariableDeclaration;
import utils.File.FileNode;

import java.util.List;

public class ContextFunctionCall {
    FileNode fileNode;
    Contract contract;
    BaseFunction baseFunction;
    StateVariableDeclaration stateVariableDeclaration;
    List<String> alias;

    public ContextFunctionCall(FileNode fileNode, Contract contract, BaseFunction baseFunction, StateVariableDeclaration stateVariableDeclaration){

    }
}
