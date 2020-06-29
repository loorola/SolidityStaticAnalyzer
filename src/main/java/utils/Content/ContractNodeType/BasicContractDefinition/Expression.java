package utils.Content.ContractNodeType.BasicContractDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BaseNode;
import utils.Content.ContractNodeType.ExpressionDefinition.*;
import utils.Content.ContractNodeType.Statement.ConditionalStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;

public class Expression extends BaseNode {
    public List<Expression> expressionList = new ArrayList<>();
    public ConditionalDeclaration conditionalDeclaration = null;
    public ConditionalExpression conditionalExpression = null;
    public EnvironmentalVariable environmentalVariable = null;
    public Equality equality = null;
    public FunctionCall functionCall = null;
    public FunctionIdentifier functionIdentifier = null;
    public NewDynamicArray dynamicArray = null;
    public OperatorExpression operatorExpression = null;
    public PrimaryExpression primaryExpression = null;
    public StaticArray staticArray = null;
    public TupleExpression tupleExpression = null;
    public TypeExpression typeExpression = null;
    public VarDeclaration varDeclaration = null;
    public VariableDeclaration variableDeclaration = null;
    public TypeConversion typeConversion = null;
    public int type;
    /*
    0: Equality /
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
    12: VarDeclaration /
    13: ConditionalDeclaration /
    14: ConditionalExpression /
     */
    public SolidityParser.ExpressionContext expressionContext = null;

    public Expression(SolidityParser.ExpressionContext ctx, int type){
        expressionContext = ctx;
        this.type=type;
    }

    public void initEquality(Equality e){
        equality = e;
    }

    public void initStaticArray(StaticArray s){
        staticArray = s;
    }

    public void initDynamicArray(NewDynamicArray a){
        dynamicArray = a;
    }

    public void initEnvironmentalVariable(EnvironmentalVariable e){
        environmentalVariable = e;
    }

    public void initFunctionCall(FunctionCall f){
        functionCall = f;
    }

    public void initFunctionIdentifier(FunctionIdentifier f){
    functionIdentifier = f;
    }

    public void initTupleExpression(TupleExpression t){
        tupleExpression = t;
    }

    public void initTypeExpression(TypeExpression t){
        typeExpression = t;
    }

    public void initOperatorExpression(OperatorExpression o){
        operatorExpression = o;
    }

    public void initVariableDeclaration(VariableDeclaration v){
        variableDeclaration = v;
    }

    public void initVarDeclaration(VarDeclaration v){
        varDeclaration = v;
    }

    public void initPrimaryExpression(PrimaryExpression p){
        primaryExpression = p;
    }

    public void initTypeConversion(TypeConversion t){
        typeConversion = t;
    }

    public void initConditionalExpression(ConditionalExpression c){
        conditionalExpression=c;
    }

    public void initConditionalDeclaration(ConditionalDeclaration c){
        conditionalDeclaration=c;
    }
}
