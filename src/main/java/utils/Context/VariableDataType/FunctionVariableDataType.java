package utils.Context.VariableDataType;

public class FunctionVariableDataType {
    public InstanceVariableDataType instanceVariableDataType;
    public String alias;
    public FunctionVariableDataType(InstanceVariableDataType instanceVariableDataType, String alias){
        this.instanceVariableDataType = instanceVariableDataType;
        this.alias = alias;
    }
}
