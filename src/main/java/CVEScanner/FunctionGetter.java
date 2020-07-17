package CVEScanner;

import javafx.util.Pair;
import utils.Content.ContractNodeType.BasicContractDefinition.BaseFunction;
import utils.Content.ContractNodeType.BasicContractDefinition.Modifier;
import utils.Content.ContractNodeType.FunctionDefinition.FallbackFunction;
import utils.Content.ContractNodeType.FunctionDefinition.Function;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;

import java.util.ArrayList;
import java.util.List;



public class FunctionGetter {

    public static List<Modifier> getAllModifier(Instance instance){
        List<Modifier> modifierList = new ArrayList<>();

        for(int i=0;i<instance.modifierList.size();i++){
            if(!instance.modifierList.get(i).visited){
                instance.modifierList.get(i).visited=true;
                modifierList.add(instance.modifierList.get(i));
            }
        }
        return modifierList;
    }

    public static List<Modifier> getModifierFromFunction(List<Pair<Instance,BaseFunction>> pair){
        List<Modifier> modifierList = new ArrayList<>();
        for(int i=0;i<pair.size();i++){
            if(pair.get(i).getValue().function!=null){
                Function f = pair.get(i).getValue().function;
                for(int j = 0;j<f.modifiers.size();j++){
                    if(!f.modifiers.get(j).visited){
                        f.modifiers.get(j).visited=true;
                        modifierList.add(f.modifiers.get(j));
                    }
                }
            }
        }
        return modifierList;
    }

    private static List<Pair<Instance,BaseFunction>> getFunctionListFromFunctionCall(Pair<Instance,BaseFunction> pair){
        List<Pair<Instance,BaseFunction>> functionList = new ArrayList<>();

        for(int i=0;i<pair.getValue().functionCallList.size();i++){
            if(pair.getValue().functionCallList.get(i).baseFunction.visited!=true){
                BaseFunction bf = pair.getValue().functionCallList.get(i).baseFunction;
                Instance in = pair.getValue().functionCallList.get(i).instance;
                bf.visited=true;
                functionList.add(new Pair(in,bf));
                functionList.addAll(getFunctionListFromFunctionCall(new Pair(in,bf)));
            }
        }
        return functionList;
    }


    public static List<Pair<Instance,BaseFunction>> getFallbackFunction(Instance instance, boolean isFunctionCall){
        List<Pair<Instance,BaseFunction>> functionList = new ArrayList<>();

        for(int i=0;i< instance.fallbackFunctionList.size();i++){
            FallbackFunction f = instance.fallbackFunctionList.get(i);
            if(f.visited!=true) {
                functionList.add(new Pair(instance,f));
                f.visited=true;
                if(isFunctionCall) functionList.addAll(getFunctionListFromFunctionCall(new Pair(instance, f)));

            }

        }
        return functionList;
    }

    public static List<Pair<Instance,BaseFunction>> getFunctionListWithFunctionCall(Instance in, boolean isFallbackIncluded){
        List<Pair<Instance,BaseFunction>> functionList = new ArrayList<>();
        for(int i=0;i<in.functionList.size();i++){
            if(in.functionList.get(i).visited!=true&&(in.functionList.get(i).visibleType==null||(in.functionList.get(i).visibleType!=null&&!in.functionList.get(i).visibleType.equals("private")))){
                in.functionList.get(i).visited=true;
                functionList.add(new Pair(in,in.functionList.get(i)));
                functionList.addAll(getFunctionListFromFunctionCall(new Pair(in, in.functionList.get(i))));
            }
        }

        if(isFallbackIncluded) functionList.addAll(getFallbackFunction(in,true));
        return functionList;
    }


    public static List<Pair<Instance,BaseFunction>> getAllFunctionList(Instance instance){
        List<Pair<Instance,BaseFunction>> functionList = new ArrayList<>();
        for(int i=0;i<instance.functionList.size();i++){
            if(!instance.functionList.get(i).visited){
                instance.functionList.get(i).visited=true;
                functionList.add(new Pair(instance, instance.functionList.get(i)));
            }
        }

        for(int i=0;i<instance.fallbackFunctionList.size();i++){
            if(!instance.fallbackFunctionList.get(i).visited){
                instance.fallbackFunctionList.get(i).visited=true;
                functionList.add(new Pair(instance, instance.fallbackFunctionList.get(i)));
            }
        }
        return functionList;
    }

    //0 - external; 1 - public; 2 - internal; 3 - private;
    public static List<Pair<Instance,BaseFunction>> getTargetFunctionList(Instance in, int VisibleType, boolean isFallbackIncluded, boolean functionCallisNeeded){
        List<Pair<Instance,BaseFunction>> functionList = new ArrayList<>();
        String visibleType = null;
        switch(VisibleType){
            case 0: visibleType = "external"; break;
            case 1: visibleType = "public"; break;
            case 2: visibleType = "internal"; break;
            case 3: visibleType = "private"; break;
        }
        for(int i=0;i<in.functionList.size();i++){
            if(!in.functionList.get(i).visited&&((in.functionList.get(i).visibleType!=null&&in.functionList.get(i).visibleType.equals(visibleType))||(in.functionList.get(i).visibleType==null&&visibleType.equals("internal")))){
                in.functionList.get(i).visited=true;
                functionList.add(new Pair(in,in.functionList.get(i)));
                if(functionCallisNeeded) {
                    functionList.addAll(getFunctionListFromFunctionCall(new Pair(in,in.functionList.get(i))));
                }
            }
        }

        if(isFallbackIncluded) functionList.addAll(getFallbackFunction(in, functionCallisNeeded));
        return functionList;
    }
}
