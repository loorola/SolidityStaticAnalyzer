package utils.Content.ContractNodeType.FunctionDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.BaseFunction;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.Content.ContractNodeType.BasicContractDefinition.Modifier;
import utils.Content.ContractNodeType.ExpressionDefinition.VariableDeclaration;
import utils.Content.ContractNodeType.Statement.Block;
import utils.Context.VariableContext.FunctionCall;

import java.util.ArrayList;
import java.util.List;

public class Function extends BaseFunction {
    public String stateMutability = null;
    public String visibleType;
    public List<String> modifierList = new ArrayList<>();
    public List<Modifier> modifiers = new ArrayList<>();
    public List<SolidityParser.ExpressionContext> derivedconstructorList = new ArrayList<>();
    public List<String> returnParameterList = new ArrayList<>();
    public Block block;

    public Function(String previousNodeName, String alias, Block block, String stateMutability, String visibleType){
        this.alias = alias;
        this.stateMutability = stateMutability;
        this.visibleType = visibleType;
        this.block = block;

        nodeName = alias;
        label=previousNodeName+nodeName;
        function = this;

    }

}