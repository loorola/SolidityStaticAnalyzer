package utils.Content.ContractNodeType.BasicContractDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BaseNode;

import java.util.ArrayList;
import java.util.List;

public class Expression extends BaseNode {
    public List<Expression> expressionList = new ArrayList<>();
    public int type;
    /*
    0: Equality
    1: Static Array /
    2: New Dynamic Array /
    3: EnvironmentalVariable /
    4: FunctionCall /
    5: FunctionIdentifier /
    6: tupleExpression /
    7: TypeExpression /
    8: PrimaryExpression /
    9: TypeConversion /
    10: OperatorExpression /
    11: VariableDeclaration /
     */
    public SolidityParser.ExpressionContext expressionContext = null;

    public Expression(SolidityParser.ExpressionContext ctx, int type){
        expressionContext = ctx;
        this.type=type;
    }


}
