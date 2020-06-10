package utils.FunctionCall.ContractNodeType;

import utils.FunctionCall.ContractNodeType.BaseNode;

import java.util.ArrayList;
import java.util.List;

public class Enum extends BaseNode {
    public List<String> value = new ArrayList<>();
    public Enum(String alias){
        this.alias = alias;
    }

    public void addValue(String value){
        this.value.add(value);
    }
}