package utils.functionCall;

import org.antlr.v4.runtime.ParserRuleContext;
import parser.Base.SolidityParser;

import java.util.*;

public class FunctionCallNode {
    public static final Map<Integer,Type> typeMap=new HashMap<Integer, Type>();
    public enum Type{ CONTRACT, LIBRARY, FUNCTION, USINGFOR, FALLBACK, EXPRESSION, MODIFIER, ROOT }

    public List<FunctionCallNode> functionCallNodeList;
    public Type type;
    public String alias;
    public ParserRuleContext ctx;

    public FunctionCallNode(){
        ctx=null;
        typeMap.put(0, Type.CONTRACT);
        typeMap.put(1,Type.LIBRARY);
        typeMap.put(2,Type.FUNCTION);
        typeMap.put(3, Type.USINGFOR);
        typeMap.put(4,Type.FALLBACK);
        typeMap.put(5,Type.EXPRESSION);
        typeMap.put(6,Type.MODIFIER);
        typeMap.put(7,Type.ROOT);
        this.functionCallNodeList =new ArrayList<FunctionCallNode>();
        type= Type.ROOT;
        alias = null;
    }

    public FunctionCallNode addChild(ParserRuleContext ctx, int type){
        FunctionCallNode c = new FunctionCallNode();
        c.ctx = ctx;
        c.type=typeMap.get(type);
        this.functionCallNodeList.add(c);
        return c;
    }

    public int getType(){
        for(Map.Entry<Integer,Type> entry:typeMap.entrySet()){
            if(entry.getValue().equals(type))return entry.getKey();
        }
        return -1;
    }

    @Override
    public String toString(){
        String txt="nil";
        switch(type){
            case CONTRACT:
            case LIBRARY:
                txt=ctx.getChild(SolidityParser.IdentifierContext.class,0).getText();
                break;
            case FUNCTION: {
                if(ctx.getChild(SolidityParser.ConstructorIdentifierContext.class,0)!=null){
                    txt="constructor";
                }else{
                    txt=ctx.getChild(SolidityParser.FunctionIdentifierContext.class,0).identifier().getText();
                }
            }
            break;
            case USINGFOR: {
                txt=ctx.getText();
            }
            break;
            case FALLBACK:
                txt=ctx.getChild(SolidityParser.FallbackIdentifierContext.class,0).getText();
                break;
            case EXPRESSION:
                txt=ctx.getText();
                break;
            case MODIFIER:
                txt=ctx.getChild(SolidityParser.IdentifierContext.class,0).getText();
                break;
            default: break;
        }
        return txt;
    }
}
