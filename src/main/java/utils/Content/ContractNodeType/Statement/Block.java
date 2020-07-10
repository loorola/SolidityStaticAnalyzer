package utils.Content.ContractNodeType.Statement;

import utils.Content.ContractNodeType.BasicContractDefinition.Statement;
import utils.Content.ContractNodeType.ExpressionDefinition.VariableDeclaration;

import java.util.ArrayList;
import java.util.List;

public class Block {
    public List<VariableDeclaration>variableDeclarationList = new ArrayList<>();
    public List<Statement>statementList = new ArrayList<>();
    public Block(){}

}
