package utils.Content.ContractNodeType.BasicContractDefinition;

import utils.Content.ContractNodeType.BaseNode;

public class UsingFor extends BaseNode {
    String dataTypeName;
    String alias;
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
