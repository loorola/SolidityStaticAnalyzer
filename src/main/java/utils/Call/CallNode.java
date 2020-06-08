package utils.Call;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

public class CallNode {
    enum Type{ CONTRACT, FUNCTION, EXPRESSION, MODIFIER, LIBRARY }
    public List<CallNode> calling;
    public CallNode parent;
    public Type type;
    public String alias;
    public ParserRuleContext ctx;

    public CallNode(){
        ctx=null;
        parent=null;
        this.calling=new ArrayList<CallNode>();
        type=null;
        alias = null;
    }

    public CallNode addChild(ParserRuleContext ctx, int type){
        //type: 0->CONTRACT; type:1->FUNCTION; type:2->EXPRESSION; type:3-> MODIFIER; type: 4->LIBRARY
        CallNode c = new CallNode();
        c.ctx = ctx;
        switch(type){
            case 0: this.type= Type.CONTRACT; break;
            case 1: this.type=Type.FUNCTION; break;
            case 2: this.type=Type.EXPRESSION; break;
            case 3: this.type=Type.MODIFIER; break;
            case 4: this.type=Type.LIBRARY; break;
        }
        this.calling.add(c);
        return c;
    }

    public int getType(){
        if(this.type==Type.CONTRACT) return 0;
        else if(this.type==Type.FUNCTION) return 1;
        else if(this.type== Type.EXPRESSION) return 2;
        else if(this.type==Type.MODIFIER) return 3;
        return 4;
    }
}
