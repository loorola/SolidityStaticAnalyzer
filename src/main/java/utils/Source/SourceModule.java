package utils.Source;

import org.antlr.v4.runtime.ParserRuleContext;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;

public class SourceModule { //the contract or class that is used by the targeted file
    String moduleName;  //the name of contract or library
    public String alias;
    public SourceModule(String moduleName, String alias){
        this.moduleName=moduleName;
        this.alias = alias;
    }
}
