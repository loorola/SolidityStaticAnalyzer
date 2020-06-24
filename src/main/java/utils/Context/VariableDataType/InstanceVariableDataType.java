package utils.Context.VariableDataType;

import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;

public class InstanceVariableDataType {
    public Instance instance;
    public String dataType;
    public String alias;
    public InstanceVariableDataType(Instance instance, String dataType,String  alias){
        this.instance = instance;
        this.alias = alias;
        this.dataType = dataType;
    }
}
