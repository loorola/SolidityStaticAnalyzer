package utils.Context;

import utils.Content.ContractNodeType.BasicContractDefinition.BaseFunction;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;

import java.util.ArrayList;
import java.util.List;

public class ContextDataType {
    String name;
    Instance instance;
    BaseFunction baseFunction;

    public ContextDataType(String name, Instance instance, BaseFunction bf){
        this.name = name;
        this.instance = instance;
        baseFunction=bf;
    }
}
