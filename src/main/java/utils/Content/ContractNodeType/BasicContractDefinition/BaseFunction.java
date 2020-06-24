package utils.Content.ContractNodeType.BasicContractDefinition;

import javafx.util.Pair;
import utils.Content.ContractNodeType.BaseNode;
import utils.Content.ContractNodeType.FunctionDefinition.FunctionInheritance;
import utils.Content.ContractNodeType.Statement.Block;

import java.util.ArrayList;
import java.util.List;

public class BaseFunction extends BaseNode {
    public List<Parameter> parameterList = new ArrayList<>();
    public List<FunctionInheritance> functionInheritanceList = new ArrayList<>();
}
