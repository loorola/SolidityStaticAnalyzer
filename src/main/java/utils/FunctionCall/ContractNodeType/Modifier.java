package utils.FunctionCall.ContractNodeType;

import parser.Base.SolidityParser;

public class Modifier extends BaseFunction{
    SolidityParser.ModifierBlockContext block;

    public Modifier(String alias, SolidityParser.ModifierBlockContext ctx){
        this.alias=alias;
        this.block=ctx;
    }

}