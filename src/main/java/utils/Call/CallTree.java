package utils.Call;

import org.antlr.v4.runtime.ParserRuleContext;

public class CallTree {
    public CallNode root;

    public CallTree(){
        root= new CallNode();
    }

    public static CallNode addCallNode(CallNode parent, ParserRuleContext ctx, int type){
        return parent.addChild(ctx, type);
    }


}
