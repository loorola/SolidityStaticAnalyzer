package utils.Content.ContractNodeType.StateVariableDeclaration;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

public class PrimaryVariable  extends StateVariableDeclaration{
    public SolidityParser.TypeNameContext dataType;
    public PrimaryVariable(String alias, SolidityParser.TypeNameContext dataType, String visibleType, boolean isConstant, Expression e){
        super(alias, visibleType, isConstant,e);
        this.dataType=dataType;
        this.isConstant = isConstant;
        this.expression=e;
    }
}
