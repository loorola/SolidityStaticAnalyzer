package utils.Source;

import org.antlr.v4.runtime.ParserRuleContext;

public class SourceModule { //the contract or class that is used by the targeted file
    String moduleName;  //the name of contract or library
    ParserRuleContext ctx;  //will link to it later

    public SourceModule(String moduleName, String alias){
        this.moduleName=moduleName;
    }
}
