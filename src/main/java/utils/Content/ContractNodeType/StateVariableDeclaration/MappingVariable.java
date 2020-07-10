package utils.Content.ContractNodeType.StateVariableDeclaration;

import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

public class MappingVariable extends StateVariableDeclaration{
    String dataType1, dataType2;
    public MappingVariable(String previousNodeName, String alias, String dataType1, String dateType2, String visibleType, boolean isConstant, Expression e){
        super(previousNodeName, alias, visibleType,isConstant, e);
        nodeName = "mapping "+dataType1+" => "+dataType2+")";
        this.dataType1=dataType1;
        this.dataType2=dateType2;
    }
}
