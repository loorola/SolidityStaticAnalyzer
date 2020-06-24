package utils.Content.ContractNodeType.SolidityClassDefinition;

import utils.Content.ContractNodeType.BaseNode;
import utils.Content.ContractNodeType.BasicContractDefinition.*;
import utils.Content.ContractNodeType.BasicContractDefinition.Enum;
import utils.Content.ContractNodeType.FunctionDefinition.FallbackFunction;
import utils.Content.ContractNodeType.FunctionDefinition.Function;
import utils.Content.ContractNodeType.StateVariableDeclaration.StateVariableDeclaration;

import java.util.ArrayList;
import java.util.List;

public class Instance extends BaseNode {
    public List<String> eventList = new ArrayList<>();
    public List<UsingFor>usingForList=new ArrayList<>();
    public List<Struct>structList = new ArrayList<>();
    public List<Modifier>modifierList = new ArrayList<>();
    public List<StateVariableDeclaration> stateVariableDeclarationList = new ArrayList<>();
    public List<FallbackFunction>fallbackFunctionList = new ArrayList<>();
    public List<Function>functionList = new ArrayList<>();
    public List<Enum>enumList = new ArrayList<>();

    public void addEvent(String event){
        eventList.add(event);
    }

    public void addStruct(Struct struct){
        structList.add(struct);
    }

    public void addModifier(Modifier modifier){
        modifierList.add(modifier);
    }

    public void addStateVariable(StateVariableDeclaration variable){
        stateVariableDeclarationList.add(variable);
    }

    public void addFallbackFunction(FallbackFunction fallbackFunction){
        fallbackFunctionList.add(fallbackFunction);
    }

    public void addFunction(Function function){
        functionList.add(function);
    }

    public void addEnum(Enum e){
        enumList.add(e);
    }

    public void addUsingFor(UsingFor u){
        usingForList.add(u);
    }
}
