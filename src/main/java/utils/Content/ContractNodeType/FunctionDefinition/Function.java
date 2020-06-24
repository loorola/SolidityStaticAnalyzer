package utils.Content.ContractNodeType.FunctionDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.BaseFunction;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.Content.ContractNodeType.Statement.Block;

import java.util.ArrayList;
import java.util.List;

public class Function extends BaseFunction {
    public String stateMutability = null;
    public String visibleType;
    public List<String> modifierList = new ArrayList<>();
    public List<SolidityParser.ExpressionContext> modifierWithParameterList = new ArrayList<>();
    public List<String> returnParameterList = new ArrayList<>();
    public Block block;

    public Function(String alias, Block block, String stateMutability, String visibleType){
        this.alias = alias;
        this.stateMutability = stateMutability;
        this.visibleType = visibleType;
        this.block = block;
    }

}