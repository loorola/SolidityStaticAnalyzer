package utils.Context;

import javafx.util.Pair;
import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.Content.ContractNodeType.SolidityClassDefinition.Contract;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;
import utils.File.FileNode;

import java.util.ArrayList;
import java.util.List;


public class InheritanceType {
    public Pair<FileNode,Instance> otherFileContract = null;
    public Contract thisFileContract = null;
    public boolean notSolved = true;
    public List<Expression> expressionList = new ArrayList<>();
    public List<SolidityParser.IdentifierContext> identifierContextList = new ArrayList<>();

    public InheritanceType(Pair<FileNode,Instance>source){
        otherFileContract=source;
    }

    public InheritanceType(FileNode fn , Instance instance){
        this.otherFileContract=new Pair(fn, instance);
        notSolved = false;
    }

    public InheritanceType(Contract thisFileContract){
        this.thisFileContract=thisFileContract;
        notSolved=false;
    }

    public Instance getInstance(){
        if(thisFileContract!=null) return thisFileContract;
        else return otherFileContract.getValue();
    }

    public InheritanceType(){

    }
}
