package utils.Content.ContractNodeType.BasicContractDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BaseNode;

public class Parameter extends BaseNode {
    public SolidityParser.TypeNameContext dataType;
    public String storageLocation;
    public Parameter(String alias, SolidityParser.TypeNameContext dataType, String storageLocation){
        this.dataType=dataType;
        this.alias=alias;
        this.storageLocation=storageLocation;
    }


}