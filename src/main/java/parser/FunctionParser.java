package parser;

import parser.Base.SolidityBaseListener;
import parser.Base.SolidityParser;
import utils.Call.CallTree;

public class FunctionParser extends SolidityBaseListener {
    public CallTree ct;

    public FunctionParser(){
        ct=new CallTree();
    }



}
