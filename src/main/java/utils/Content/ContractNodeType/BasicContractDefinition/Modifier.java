package utils.Content.ContractNodeType.BasicContractDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.Statement.Block;

import java.util.ArrayList;
import java.util.List;

public class Modifier extends BaseFunction{
    List<Expression> expressionList = new ArrayList<>();
    Block block = null;

    public Modifier(String alias, Block b){
        this.alias=alias;
        this.block = b;
    }

}