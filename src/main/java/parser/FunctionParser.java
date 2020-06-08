package parser;

import parser.Base.SolidityBaseListener;
import parser.Base.SolidityParser;
import utils.Call.CallTree;

public class FunctionPreparser extends SolidityBaseListener {
    public CallTree ct;

    public FunctionPreparser(){
        ct=new CallTree();
    }

    @Override
    public void enterModifierDefinition(SolidityParser.ModifierDefinitionContext ctx) {

    }

}
