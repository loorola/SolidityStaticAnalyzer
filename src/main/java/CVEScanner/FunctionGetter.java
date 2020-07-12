package CVEScanner;

import utils.Content.ContractNodeType.BasicContractDefinition.BaseFunction;
import utils.Content.ContractNodeType.FunctionDefinition.FallbackFunction;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;

import java.util.ArrayList;
import java.util.List;

public class FunctionGetter {

    private static List<BaseFunction> getFunctionListFromFunctionCall(BaseFunction baseFunction){
        List<BaseFunction> functionList = new ArrayList<>();

        for(int i=0;i<baseFunction.functionCallList.size();i++){
            if(baseFunction.functionCallList.get(i).baseFunction.visited!=true){
                baseFunction.functionCallList.get(i).baseFunction.visited=true;
                functionList.add(baseFunction.functionCallList.get(i).baseFunction.function);
                functionList.addAll(getFunctionListFromFunctionCall(baseFunction.functionCallList.get(i).baseFunction));
            }
        }
        return functionList;
    }


    public static List<BaseFunction> getFallbackFunction(Instance instance, boolean isFunctionCall){
        List<BaseFunction> functionList = new ArrayList<>();

        for(int i=0;i< instance.fallbackFunctionList.size();i++){
            FallbackFunction f = instance.fallbackFunctionList.get(i);
            if(f.visited!=true) {
                functionList.add(f);
                f.visited=true;
                if(isFunctionCall) functionList.addAll(getFunctionListFromFunctionCall(f));

            }

        }
        return functionList;
    }

    public static List<BaseFunction> getFunctionListWithFunctionCall(Instance in, boolean isFallbackIncluded){
        List<BaseFunction> functionList = new ArrayList<>();
        for(int i=0;i<in.functionList.size();i++){
            if(in.functionList.get(i).visited!=true&&in.functionList.get(i).visibleType!=null&&(in.functionList.get(i).visibleType.equals("public")||in.functionList.get(i).visibleType.equals("external"))){
                in.functionList.get(i).visited=true;
                functionList.add(in.functionList.get(i));
                functionList.addAll(getFunctionListFromFunctionCall(in.functionList.get(i)));
            }
        }

        if(isFallbackIncluded) functionList.addAll(getFallbackFunction(in,true));
        return functionList;
    }

    public static List<BaseFunction> getFunctionListExcludePrivateInternal(Instance in, boolean isFallbackIncluded){
        List<BaseFunction> functionList = new ArrayList<>();
        for(int i=0;i<in.functionList.size();i++){
            if(in.functionList.get(i).visited!=true&&in.functionList.get(i).visibleType!=null&&(in.functionList.get(i).visibleType.equals("public")||in.functionList.get(i).visibleType.equals("external"))){
                functionList.add(in.functionList.get(i));
            }
        }
        if(isFallbackIncluded) functionList.addAll(getFallbackFunction(in,false));

        return functionList;
    }


    public static List<BaseFunction> getAllFunctionList(Instance instance){
        List<BaseFunction> functionList = new ArrayList<>();
        for(int i=0;i<instance.functionList.size();i++){
            if(!instance.functionList.get(i).visited){
                instance.functionList.get(i).visited=true;
                functionList.add(instance.functionList.get(i));
            }
        }

        for(int i=0;i<instance.fallbackFunctionList.size();i++){
            if(!instance.fallbackFunctionList.get(i).visited){
                instance.fallbackFunctionList.get(i).visited=true;
                functionList.add(instance.fallbackFunctionList.get(i));
            }
        }
        return functionList;
    }

    //0 - external; 1 - public; 2 - internal; 3 - private;
    public static List<BaseFunction> getTargetFunctionList(Instance in, int VisibleType, boolean isFallbackIncluded, boolean functionCallisNeeded){
        List<BaseFunction> functionList = new ArrayList<>();
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
                functionList.add(in.functionList.get(i));
                if(functionCallisNeeded) {
                    functionList.addAll(getFunctionListFromFunctionCall(in.functionList.get(i)));
                }
            }
        }

        if(isFallbackIncluded) functionList.addAll(getFallbackFunction(in, functionCallisNeeded));
        return functionList;
    }
}
