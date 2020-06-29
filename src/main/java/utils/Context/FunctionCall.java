package utils.Context;

import utils.Content.ContractNodeType.FunctionDefinition.Function;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;

public class FunctionCall {
    Instance instance = null;
    Function function = null;

    public FunctionCall(Instance instance, Function function){
        this.instance = instance;
        this.function = function;
    }
}
