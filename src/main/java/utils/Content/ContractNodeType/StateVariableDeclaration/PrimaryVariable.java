package utils.Content.ContractNodeType.StateVariableDeclaration;

import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

public class PrimaryVariable  extends StateVariableDeclaration{
    String dataType;
    public PrimaryVariable(String alias, String dataType, String visibleType, boolean isConstant, Expression e){
        super(alias, visibleType, isConstant,e);
        this.dataType=dataType;
        this.isConstant = isConstant;
        this.expression=e;
    }
}
