package utils.Content.ContractNodeType.BasicContractDefinition;

import utils.Content.ContractNodeType.BaseNode;
import utils.Content.ContractNodeType.Statement.Block;

import java.util.ArrayList;
import java.util.List;

public class BaseFunction extends BaseNode {
    Block functionBlock;
    public List<Parameter> parameterList = new ArrayList<>();
    public void addParameter(String alias, String dataType, String storageLocation){
        Parameter p = new Parameter(alias,dataType,storageLocation  );
        parameterList.add(p);
    }
}
