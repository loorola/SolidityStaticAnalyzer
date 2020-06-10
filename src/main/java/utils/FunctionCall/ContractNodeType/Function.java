package utils.FunctionCall.ContractNodeType;

import parser.Base.SolidityParser;

import java.util.ArrayList;
import java.util.List;

public class Function extends BaseFunction{
    SolidityParser.BlockContext block;
    public SolidityParser.StateMutabilityContext stateMutability = null;
    public String visibleType;
    public SolidityParser.InheritanceContext inheritanceContext=null;
    public List<String> modifierList = new ArrayList<>();

    public Function(String alias, SolidityParser.BlockContext block, SolidityParser.StateMutabilityContext stateMutability, String visibleType){
        this.alias = alias;
        this.stateMutability = stateMutability;
        this.visibleType = visibleType;
    }

    public void addModifier(String alias){
        modifierList.add(alias);
    }

}