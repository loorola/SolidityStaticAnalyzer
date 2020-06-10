package utils.FunctionCall.ContractNodeType;

import parser.Base.SolidityParser;

import java.util.ArrayList;
import java.util.List;

public class StateVariable extends BaseNode{
    String dataTypeName;
    String constantType;
    String visibleType;
    List<SolidityParser.ExpressionContext> expressionContextList = new ArrayList<>();

    public StateVariable(String alias, String dataTypeName, String constantType, String visibleType){
        this.alias = alias;
        this.dataTypeName = dataTypeName;
        this.constantType = constantType;
        this.visibleType=visibleType;
    }

    public void addExpressionContext(SolidityParser.ExpressionContext ctx){
        expressionContextList.add(ctx);
    }
}