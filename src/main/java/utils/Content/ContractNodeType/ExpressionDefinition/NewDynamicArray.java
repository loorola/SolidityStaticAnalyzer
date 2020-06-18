package utils.Content.ContractNodeType.ExpressionDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;

public class NewDynamicArray extends Expression {
    String dataType;
    boolean isDynamicType;

    public NewDynamicArray(String dataType, boolean isDynamicType, SolidityParser.ExpressionContext ctx) {
        super(ctx, 2);
        this.dataType = dataType;
        this.isDynamicType=isDynamicType;
    }

    /*
    public NewDynamicArray(String dataType, boolean isDynamicType, SolidityParser.ExpressionContext ctx){
        //super();

    }

     */
}
