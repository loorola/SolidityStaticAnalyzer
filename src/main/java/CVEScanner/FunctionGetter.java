package CVEScanner;

import utils.Content.ContractNodeType.FunctionDefinition.Function;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;

import java.util.ArrayList;
import java.util.List;

public class FunctionGetter {
    //01
    public static List<Function> getFunctionListWithFunctionCall(Instance in, boolean isIncludeFallback){
        List<Function> functionList = new ArrayList<>();
        for(int i=0;i<in.functionList.size();i++){
            if(in.functionList.get(i).visited!=true){

            }
        }
        return functionList;
    }

    //02
    public static List<Function> getFunctionListExcludePrivate(Instance in, boolean isIncludeFallback){
        List<Function> functionList = new ArrayList<>();
        for(int i=0;i<in.functionList.size();i++){
            if(in.functionList.get(i).visited!=true){

            }
        }
        return functionList;
    }

    //03
    public static List<Function> getAllFunctionList(Instance in, boolean isIncludeFallback){
        List<Function> functionList = new ArrayList<>();
        for(int i=0;i<in.functionList.size();i++){
            if(in.functionList.get(i).visited!=true){

            }
        }
        return functionList;
    }

    //04 -> 01 - external; 02 - public; 03 - internal; 04 - private;
    public static List<Function> getTargetFunctionList(Instance in, int VisibleType, boolean isIncludeFallback){
        List<Function> functionList = new ArrayList<>();
        for(int i=0;i<in.functionList.size();i++){
            if(in.functionList.get(i).visited!=true){

            }
        }
        return functionList;
    }
}
