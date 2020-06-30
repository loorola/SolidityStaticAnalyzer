package utils.Context;

import javafx.util.Pair;
import utils.Content.ContractNodeType.SolidityClassDefinition.Contract;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;
import utils.File.FileNode;


public class InheritanceType {
    public Pair<FileNode,Instance> otherFileContract = null;
    public Contract thisFileContract = null;

    public InheritanceType(FileNode fn , Instance instance){
        this.otherFileContract=new Pair(fn, instance);
    }

    public InheritanceType(Contract thisFileContract){
        this.thisFileContract=thisFileContract;
    }
}
