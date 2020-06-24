package utils.Content.ContractNodeType.BasicContractDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BaseNode;

import java.util.ArrayList;
import java.util.List;

public class Enum extends BaseNode {
    public List<String> value = new ArrayList<>();
    public SolidityParser.EnumDefinitionContext ctx;
    public Enum(String alias, SolidityParser.EnumDefinitionContext ctx){
        this.alias = alias;
        this.ctx = ctx;
    }

    public void addValue(String value){
        this.value.add(value);
    }
}