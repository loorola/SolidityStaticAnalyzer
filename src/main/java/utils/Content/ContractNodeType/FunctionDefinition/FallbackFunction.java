package utils.Content.ContractNodeType.FunctionDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BaseNode;
import utils.Content.ContractNodeType.BasicContractDefinition.BaseFunction;
import utils.Content.ContractNodeType.ExpressionDefinition.VariableDeclaration;
import utils.Content.ContractNodeType.Statement.Block;

import java.util.ArrayList;
import java.util.List;

public class FallbackFunction extends BaseFunction {
    public String alias = null;
    public Block block;
    public String stateMutability = null;
    public SolidityParser.InheritanceContext inheritanceContext = null;
    public List<String> returnParameterList = new ArrayList<>();
    public FallbackFunction(String previousNodeName, Block block, SolidityParser.FunctionFallBackDefinitionContext ctx){
        nodeName = ctx.fallbackIdentifier().getText();
        label=previousNodeName+nodeName;
        this.block = block;
        fallbackFunction=this;
    }
}