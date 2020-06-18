package utils.Content.ContractNodeType.BasicContractDefinition;

import parser.Base.SolidityParser;

import java.util.ArrayList;
import java.util.List;

public class Modifier extends BaseFunction{
    List<Expression> expressionList = new ArrayList<>();
    SolidityParser.BlockContext block;

    public Modifier(String alias, SolidityParser.BlockContext ctx){
        this.alias=alias;
        this.block=ctx;
    }

}