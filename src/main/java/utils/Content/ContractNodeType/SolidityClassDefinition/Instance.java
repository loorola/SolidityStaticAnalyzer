package utils.Content.ContractNodeType.SolidityClassDefinition;

import javafx.util.Pair;
import utils.Content.ContractNodeType.BaseNode;
import utils.Content.ContractNodeType.BasicContractDefinition.*;
import utils.Content.ContractNodeType.BasicContractDefinition.Enum;
import utils.Content.ContractNodeType.FunctionDefinition.FallbackFunction;
import utils.Content.ContractNodeType.FunctionDefinition.Function;
import utils.Content.ContractNodeType.StateVariableDeclaration.StateVariableDeclaration;
import utils.Context.VariableContext.FunctionCall;

import java.util.ArrayList;
import java.util.List;

public class Instance extends BaseNode {
    public List<FunctionCall> functionCallList = new ArrayList<>();
    public List<String> eventList = new ArrayList<>();
    public List<UsingFor>usingForList=new ArrayList<>();
    public List<Struct>structList = new ArrayList<>();
    public List<Modifier>modifierList = new ArrayList<>();
    public List<StateVariableDeclaration> stateVariableDeclarationList = new ArrayList<>();
    public List<FallbackFunction>fallbackFunctionList = new ArrayList<>();
    public List<Function>functionList = new ArrayList<>();
    public List<Enum>enumList = new ArrayList<>();
    public Contract contract = null;
    public Interface anInterface = null;
    public Library library = null;


    public void initCFGNode(String filePath, String alias){
        nodeName=alias;
        label = filePath;
    }

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

    public void initContract(Contract c){
        contract=c;
    }

    public void initInterface(Interface i){
        anInterface=i;
    }

    public void initLibrary(Library l){
        library = l;
    }
}
