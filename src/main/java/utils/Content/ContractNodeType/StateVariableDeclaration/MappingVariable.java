package utils.Content.ContractNodeType.StateVariableDeclaration;

import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

public class MappingVariable extends StateVariableDeclaration{
    String dataType1, dataType2;
    public MappingVariable(String alias, String dataType1, String dateType2, String visibleType, boolean isConstant, Expression e){
        super(alias, visibleType,isConstant, e);
        this.dataType1=dataType1;
        this.dataType2=dateType2;
    }
}
