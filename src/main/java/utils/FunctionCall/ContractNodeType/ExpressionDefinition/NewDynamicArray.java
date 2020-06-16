package utils.FunctionCall.ContractNodeType.ExpressionDefinition;

import parser.Base.SolidityParser;
import utils.FunctionCall.ContractNodeType.Expression;

public class NewDynamicArray extends Expression {
    String dataType;
    boolean isDynamicType;

    public NewDynamicArray(String dataType, boolean isDynamicType, SolidityParser.ExpressionContext ctx) {
        super(ctx, 2);
    }

    /*
    public NewDynamicArray(String dataType, boolean isDynamicType, SolidityParser.ExpressionContext ctx){
        //super();

    }

     */
}
