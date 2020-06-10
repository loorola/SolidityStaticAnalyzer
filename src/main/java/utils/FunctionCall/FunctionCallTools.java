package utils.FunctionCall;

import parser.Base.SolidityParser;

import java.util.LinkedList;
import java.util.Queue;

public class FunctionCallTools {
    public static boolean expressionHasFunctionCall(SolidityParser.ExpressionContext e){
        boolean has = false;

        Queue<SolidityParser.ExpressionContext> q = new LinkedList<>();
        q.add(e);
        SolidityParser.ExpressionContext tmp;
        while(q!=null){
            tmp=q.remove();
            if(tmp.functionCall()!=null) {
                has = true;
                q.clear();
                break;
            }
            if(tmp.expression()!=null){
                tmp.expression().forEach(x->q.add(x));
            }
            if(tmp.tupleExpression()!=null){
                tmp.tupleExpression().expression().forEach(x->q.add(x));
            }
        }
        return has;
    }

}
