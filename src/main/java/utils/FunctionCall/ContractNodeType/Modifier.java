package utils.FunctionCall.ContractNodeType;

import parser.Base.SolidityParser;

public class Modifier extends BaseFunction{
    SolidityParser.BlockContext block;

    public Modifier(String alias, SolidityParser.BlockContext ctx){
        this.alias=alias;
        this.block=ctx;
    }

}