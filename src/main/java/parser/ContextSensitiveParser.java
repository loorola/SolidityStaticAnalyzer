package parser;

import javafx.util.Pair;
import parser.Base.SolidityBaseListener;
import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.BaseFunction;
import utils.Content.ContractNodeType.SolidityClassDefinition.Contract;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;
import utils.Content.ContractNodeType.StateVariableDeclaration.StateVariableDeclaration;
import utils.Context.InheritanceType;
import utils.File.FileNode;
import utils.Source.LocalSource;
import utils.Source.SourceModule;

import java.util.ArrayList;
import java.util.List;

public class ContextSensitiveParser extends SolidityBaseListener{
    FileNode fn;

    public ContextSensitiveParser(FileNode fn){
        this.fn=fn;
    }

    public void resolveContractInheritance(Instance instance){
        if(instance.contract!=null){
            for(int i=0;i<instance.contract.inheritanceList.size();i++){

            }
        }
    }


    public List<Pair<Instance, BaseFunction>> getInheritanceItem(SolidityParser.InheritanceSpecifierContext ctx){
        List<Pair<Instance, BaseFunction>> inheritanceList = new ArrayList<>();
        InheritanceType inheritance=mapInheritance(ctx.userDefinedTypeName().identifier());
        for(int i=0;i<fn.fileContent.contractList.size();i++){

        }
        return inheritanceList;
    }

    public InheritanceType mapInheritance(List<SolidityParser.IdentifierContext> ctxList){
        InheritanceType inheritanceType=null;
        if(ctxList.size()==1){
            inheritanceType=matchThisFileContract(ctxList.get(0).getText());    //this file contracts
            if(inheritanceType==null){
                inheritanceType = matchLocalSource(ctxList);
            }
        }
        return inheritanceType;
    }



    public InheritanceType matchLocalSource(List<SolidityParser.IdentifierContext> ctxList){
        InheritanceType localSource = null;
        Contract c = null;
        LocalSource ls;
        if(ctxList.size()==1){    //C c = new C(); ->C
            for(int i=0;i<fn.localSourceList.size();i++){
                ls = fn.localSourceList.get(i);
                if(ls.alias==null&&ls.srcModule.size()==0){
                    //'import' stringLiteral
                    c=FindContract(ls.fn, ctxList.get(i).getText());
                }else if(ls.alias==null){
                    // 'import' *|identifier ('as' identifier)? 'from' stringLiteral
                    // 'import' '{' importDeclaration (',' importDeclaration)* '}' 'from' stringLiteral ';' ;
                    c = matchSourceModule(ls, ctxList.get(i).getText());
                }
                if(c!=null) {
                    localSource = new InheritanceType(ls.fn, c);
                }
            }
        }else{  //C.B c = new C.B();
            String alias = ctxList.get(0).getText();
            String contractName = ctxList.get(1).getText();
            for(int i=0;i<fn.localSourceList.size();i++){
                ls=fn.localSourceList.get(i);
                if(ls.alias!=null&&ls.alias==alias){
                    /*
                    'import' stringLiteral ('as' identifier) ';' ;
                    'import' importAll ('as' identifier) 'from' stringLiteral ';'
                     */
                    if(ls.srcModule.size()==0&&ls.alias==null){
                        c=FindContract(ls.fn, contractName);
                    }else if(ls.srcModule.size()==0){

                    }
                }
            }
        }

        return localSource;
    }

    public Contract matchSourceModule(LocalSource ls, String alias){
        Contract c = null;
        if(ls.srcModule.size()!=0){
            for(int i=0;i<ls.srcModule.size();i++){
                SourceModule srm = ls.srcModule.get(i);
                if(srm.alias==null){
                    c = FindContract(ls.fn, ls.srcModule.get(i).moduleName);
                }else if(ls.srcModule.get(i).alias == alias){
                    c = FindContract(ls.fn, ls.srcModule.get(i).moduleName);
                }
                if(c!=null) return c;

            }
        }

        return c;
    }

    public Contract FindContract(FileNode fn, String alias){
        Contract c = null;
        for(int i=0;i<fn.fileContent.contractList.size();i++){
            if(alias==fn.fileContent.contractList.get(i).alias) {
                c=fn.fileContent.contractList.get(i);
                break;
            }
        }
        return c;
    }

    public InheritanceType matchThisFileContract(String alias){
        Contract c;
        InheritanceType inheritanceType = null;

        for(int i=0;i<fn.fileContent.contractList.size();i++){
            c = fn.fileContent.contractList.get(i);
            if(c.alias == alias) {
                inheritanceType = new InheritanceType(c);
                return inheritanceType;
            }
        }
        return inheritanceType;
    }

    public void resolveStateVariable(Instance instance){
        for(int i=0;i<instance.stateVariableDeclarationList.size();i++){
            StateVariableDeclaration s = instance.stateVariableDeclarationList.get(i);
            if(s.primaryVariable!=null&&s.primaryVariable.dataType.functionTypeName()!=null){

            }
        }

    }





}
