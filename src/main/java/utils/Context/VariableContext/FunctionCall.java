package utils.Context.VariableContext;

import utils.Content.ContractNodeType.BasicContractDefinition.BaseFunction;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;

public class FunctionCall {
    Instance instance;
    BaseFunction baseFunction = null;

    public FunctionCall(Instance instance, BaseFunction baseFunction){
        this.instance=instance;
        this.baseFunction=baseFunction;
    }
}
