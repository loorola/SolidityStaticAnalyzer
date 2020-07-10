package utils.Content.ContractNodeType.SolidityClassDefinition;

import parser.Base.SolidityParser;

public class Library extends Instance{
    SolidityParser.LibraryDefinitionContext ctx;
    public Library(String filePath, String alias, SolidityParser.LibraryDefinitionContext ctx){
        initCFGNode(filePath,alias);

        this.alias = alias;
        this.ctx = ctx;
    }
}