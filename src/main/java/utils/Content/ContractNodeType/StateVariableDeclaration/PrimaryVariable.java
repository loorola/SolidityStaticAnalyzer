package utils.Content.ContractNodeType.StateVariableDeclaration;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

public class PrimaryVariable  extends StateVariableDeclaration{
    public SolidityParser.TypeNameContext dataType;
    public PrimaryVariable(String previousNodeName, String alias, SolidityParser.TypeNameContext dataType, String visibleType, boolean isConstant, Expression e){
        super(previousNodeName, alias, visibleType, isConstant,e);
        nodeName = dataType+" "+visibleType+" "+alias;
        this.dataType=dataType;
        this.isConstant = isConstant;
        this.expression=e;
    }
}
