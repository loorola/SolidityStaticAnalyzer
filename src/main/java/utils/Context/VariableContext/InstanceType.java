package utils.Context.VariableContext;

import javafx.util.Pair;
import utils.Content.ContractNodeType.SolidityClassDefinition.Contract;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;
import utils.File.FileNode;

public class InstanceType {
    public Pair<FileNode, Instance>otherFileContract = null;
    public Contract thisFileContract = null;
    public boolean thisContract = false;

    public InstanceType(){thisContract=true;}

    public InstanceType(Contract thisFileContract){
        this.thisFileContract = thisFileContract;
    }

    public InstanceType(Pair<FileNode,Instance> p){
        otherFileContract = p;
    }

    public InstanceType(FileNode n, Instance i){
        otherFileContract = new Pair(n,i);
    }

}
