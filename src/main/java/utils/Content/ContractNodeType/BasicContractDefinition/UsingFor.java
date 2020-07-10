package utils.Content.ContractNodeType.BasicContractDefinition;

import utils.Content.ContractNodeType.BaseNode;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;

public class UsingFor extends BaseNode {
    public Instance instance=null;
    public String dataTypeName;
    public String alias;
    public UsingFor(String alias, String dataTypeName){
        this.alias=alias;
        this.dataTypeName=dataTypeName;
    }

    @Override
    public String toString() {
        return "UsingFor{" +
                "dataTypeName='" + dataTypeName + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}
