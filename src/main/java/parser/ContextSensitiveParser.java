package parser;

import javafx.util.Pair;
import parser.Base.SolidityBaseListener;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.Content.ContractNodeType.SolidityClassDefinition.Contract;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;
import utils.Content.ContractNodeType.SolidityClassDefinition.Interface;
import utils.Content.ContractNodeType.SolidityClassDefinition.Library;
import utils.Content.ContractNodeType.StateVariableDeclaration.StateVariableDeclaration;
import utils.Context.FunctionCall;
import utils.File.FileNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ContextSensitiveParser extends SolidityBaseListener{
    FileNode fn;

    public ContextSensitiveParser(FileNode fn){
        this.fn=fn;
    }

    public void resolveStateVariable(Instance instance){
        List<Pair<Instance,String>>newContractList = new ArrayList<>();
        for(int i=0;i<instance.stateVariableDeclarationList.size();i++){
            if(instance.stateVariableDeclarationList.get(i).expression!=null&&instance.stateVariableDeclarationList.get(i).expression.functionCall!=null){
                if(instance.stateVariableDeclarationList.get(i).expression.functionCall.expressionContext.functionCall().functionName().newContract()!=null){ //new contract
                    Contract c = findLocalContract(instance.stateVariableDeclarationList.get(i).alias);
                    if(c!=null) {   //same file
                        Pair<Instance,String> p = new Pair<>(c, instance.stateVariableDeclarationList.get(i).alias);
                        newContractList.add(p);
                    }else{

                    }

                }else{

                }
            }
        }
    }

    public Contract findGlobalContract(){
        Contract c =null;
        return c;
    }

    public Contract findSourceContract(String alias){
        Contract c = null;

        for(int i=0;i<fn.localSourceList.size();i++){
            if(fn.localSourceList.get(i).alias!=null&&fn.localSourceList.get(i).alias==alias){  // alias local file
                if(fn.localSourceList.get(i).srcModule.size()!=0){
                    for(int j=0;j<fn.localSourceList.get(i).srcModule.size();j++){
                        if(fn.localSourceList.get(i).srcModule.get(j).alias!=null){

                        }
                    }
                }else{

                }
            }
        }


        return c;
    }



    public Contract findLocalContract(String alias){
        Contract c=null;
        for(int i=0;i<fn.fileContent.contractList.size();i++){
            if(alias==fn.fileContent.contractList.get(i).alias) {return fn.fileContent.contractList.get(i);}
        }
        return c;
    }





}
