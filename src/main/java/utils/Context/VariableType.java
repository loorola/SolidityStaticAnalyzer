package utils.Context;

import javafx.util.Pair;
import utils.Content.ContractNodeType.SolidityClassDefinition.Contract;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;
import utils.Context.VariableContext.InstanceType;
import utils.File.FileNode;

import java.util.ArrayList;
import java.util.List;

public class VariableType {
    public InstanceType instanceType=null;
    public String primaryType;
    public boolean isArray=false;



    public VariableType(InstanceType it, String primaryType, boolean isArray){
        this.instanceType=it;
        this.primaryType=primaryType;
        this.isArray=isArray;
    }

    public VariableType(Contract c) {
        instanceType = new InstanceType(c);
        primaryType="instance";
    }

    public VariableType(Contract c, boolean isArray) {
        instanceType = new InstanceType(c);
        this.isArray=isArray;
        primaryType="instance";
    }

    public VariableType(FileNode fn, Instance in){
        instanceType = new InstanceType(fn,in);
        primaryType = "instance";
    }

    public VariableType(FileNode fn, Instance in, boolean isArray){
        instanceType = new InstanceType(fn,in);
        primaryType = "instance";
        this.isArray=isArray;
    }

    public VariableType(Pair<FileNode, Instance> p){
        instanceType = new InstanceType(p);
        primaryType = "instance";
    }

    public VariableType(Pair<FileNode, Instance> p, boolean isArray){
        instanceType = new InstanceType(p);
        primaryType = "instance";
        this.isArray=isArray;
    }

    public VariableType(String primaryType){
        this.primaryType=primaryType;
    }

    public VariableType(String primaryType, boolean isArray){
        this.primaryType=primaryType;
        this.isArray=isArray;
    }

    public Instance getInstance(){
        if(instanceType!=null) {
            if(instanceType.thisFileContract!=null) return instanceType.thisFileContract;
            else return instanceType.otherFileContract.getValue();
        }
        return null;
    }
}
