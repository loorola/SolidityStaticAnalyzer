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
    1: Static Array
    2: New Dynamic Array
    3: EnvironmentalVariable
    4: ArrayLength|ContractBalance
    5: FunctionCall
    6: FunctionIdentifier
    7: tupleExpression
    8: TypeExpression
    9: PrimaryExpression
    10: TypeConversion
    11: OperatorExpression
    12: ConditionalDeclaration
    13: VariableDeclaration
     */
    public SolidityParser.ExpressionContext expressionContext = null;

    public Expression(SolidityParser.ExpressionContext ctx, int type){
        expressionContext = ctx;
        this.type=type;
    }




}
