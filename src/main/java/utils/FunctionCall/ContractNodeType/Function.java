package utils.FunctionCall.ContractNodeType;

import parser.Base.SolidityParser;

import java.util.ArrayList;
import java.util.List;

public class Function extends BaseFunction{
    SolidityParser.BlockContext block;
    public String stateMutability = null;
    public String visibleType;
    public SolidityParser.InheritanceContext inheritanceContext=null;
    public List<String> modifierList = new ArrayList<>();
    public List<SolidityParser.ExpressionContext> modifierWithParameterList = new ArrayList<>();
    public List<String> returnParameterList = new ArrayList<>();

    public Function(String alias, SolidityParser.BlockContext block, String stateMutability, String visibleType){
        this.alias = alias;
        this.stateMutability = stateMutability;
        this.visibleType = visibleType;
    }

    public void addModifier(String alias){
        modifierList.add(alias);
    }

    public void addReturnParameter(String dataType){
        returnParameterList.add(dataType);
    }

    public void addModifierWithParameterList(SolidityParser.ExpressionContext ctx){
        modifierWithParameterList.add(ctx);
    }
}