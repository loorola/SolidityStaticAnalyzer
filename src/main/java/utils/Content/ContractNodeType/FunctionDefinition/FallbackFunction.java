package utils.Content.ContractNodeType.FunctionDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BaseNode;
import utils.Content.ContractNodeType.Statement.Block;

import java.util.ArrayList;
import java.util.List;

public class FallbackFunction extends BaseNode {
    public String alias = null;
    public Block block;
    public String stateMutability = null;
    public SolidityParser.InheritanceContext inheritanceContext = null;
    public List<String> returnParameterList = new ArrayList<>();

    public FallbackFunction(Block block){
        this.block = block;
    }
}