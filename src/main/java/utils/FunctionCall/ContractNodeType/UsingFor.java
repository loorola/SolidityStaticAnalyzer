package utils.FunctionCall.ContractNodeType;

import java.util.ArrayList;
import java.util.List;

public class UsingFor extends BaseNode{
    String dataTypeName;
    String alias;
    public UsingFor(String alias, String dataTypeName){
        this.alias=alias;
        this.dataTypeName=dataTypeName;
    }

}
