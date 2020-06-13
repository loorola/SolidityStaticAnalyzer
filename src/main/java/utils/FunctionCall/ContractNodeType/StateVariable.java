package utils.FunctionCall.ContractNodeType;

import parser.Base.SolidityParser;

import java.util.ArrayList;
import java.util.List;

public class StateVariable extends BaseNode{
    public SolidityParser.ExpressionContext expressionContext;
    public String alias;
    public String visibleType;
    boolean isConstant;
    public Normal n = null;
    public Mapping m = null;
    public Function f = null;

    public class Normal  {
        String dataType;
        public Normal(String dataType){
            this.dataType=dataType;
        }
    }

    public class Mapping{
        String type1,type2;
        public Mapping(String type1, String type2){
            this.type1=type1;
            this.type2=type2;
        };
    }

    public class Function  {
        public String stateMutability = null;
        public String visibleType;
        public List<String> returnParameterList = new ArrayList<>();
        public List<Parameter> parameterList = new ArrayList<>();

        public Function(String stateMutability, String visibleType){
            this.visibleType=visibleType;
            this.stateMutability=stateMutability;
        }

    }


    public StateVariable(String alias, String visibleType, boolean isConstant, SolidityParser.ExpressionContext ctx){
        this.alias=alias;
        this.visibleType=visibleType;
        this.isConstant=isConstant;

        this.expressionContext=ctx;
    }

    public void initMapping(String type1, String type2){
        m=new Mapping(type1,type2);
    }

    public void initFunction(String stateMutability, String visibleType){
        f=new Function(stateMutability,visibleType);
    }

    public void initNormal(String dataType){
        n=new Normal(dataType);
    }
}