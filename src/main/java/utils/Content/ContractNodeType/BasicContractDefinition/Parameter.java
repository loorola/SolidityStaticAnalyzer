package utils.Content.ContractNodeType.BasicContractDefinition;

import utils.Content.ContractNodeType.BaseNode;

public class Parameter extends BaseNode {
    String dataType;
    String storageLocation;
    public Parameter(String alias,String dataType, String storageLocation){
        this.dataType=dataType;
        this.alias=alias;
        this.storageLocation=storageLocation;
    }


}