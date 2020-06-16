package utils.FunctionCall.ContractNodeType.SolidityClassDefinition;

import parser.Base.SolidityParser;

public class Library extends Instance{
    SolidityParser.LibraryDefinitionContext ctx;
    public Library(String alias, SolidityParser.LibraryDefinitionContext ctx){
        this.alias = alias;
        this.ctx = ctx;
    }
}