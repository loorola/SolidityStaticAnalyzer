package parser;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import parser.Base.SolidityBaseListener;
import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.BaseFunction;
import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.Content.ContractNodeType.BasicContractDefinition.Modifier;
import utils.Content.ContractNodeType.BasicContractDefinition.Statement;
import utils.Content.ContractNodeType.ExpressionDefinition.VariableDeclaration;
import utils.Content.ContractNodeType.FunctionDefinition.FallbackFunction;
import utils.Content.ContractNodeType.FunctionDefinition.Function;
import utils.Content.ContractNodeType.SolidityClassDefinition.Contract;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;
import utils.Content.ContractNodeType.SolidityClassDefinition.Library;
import utils.Content.ContractNodeType.StateVariableDeclaration.StateVariableDeclaration;
import utils.Content.ContractNodeType.Statement.Block;
import utils.Context.InheritanceType;
import utils.Context.VariableContext.FunctionCall;
import utils.Context.VariableType;
import utils.File.FileNode;
import utils.Source.LocalSource;
import utils.Source.SourceModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContextSensitiveParser extends SolidityBaseListener {
    FileNode fn;

    public ContextSensitiveParser(FileNode fn) {
        this.fn = fn;
    }

    public void initContextSensitive(){
        for (int i = 0; i < fn.fileContent.contractList.size(); i++) {
            resolveUsingFor(fn,fn.fileContent.contractList.get(i));
            fn.fileContent.contractList.get(i).inheritanceTypeList = getInheritanceList(fn.fileContent.contractList.get(i));
            fn.fileContent.contractList.get(i).stateVariableDeclarationList = getStateVariableDeclarationList(fn.fileContent.contractList.get(i), fn.fileContent.contractList.get(i).stateVariableDeclarationList);
            boolean hasConstructor = false;

            for(int j=0;j<fn.fileContent.contractList.get(i).functionList.size();j++){
                if(fn.fileContent.contractList.get(i).functionList.get(j).alias.equals("constructor")){
                    hasConstructor=true;
                    break;
                }
            }
            if(hasConstructor==false) {
                Function f = new Function(fn.fileContent.contractList.get(i).label,"constructor",null, null, null);
                fn.fileContent.contractList.get(i).functionList.add(f);
            }
        }

        for (int i = 0; i < fn.fileContent.libraryList.size(); i++) {
            resolveUsingFor(fn,fn.fileContent.libraryList.get(i));
            fn.fileContent.libraryList.get(i).stateVariableDeclarationList = getStateVariableDeclarationList(fn.fileContent.libraryList.get(i), fn.fileContent.libraryList.get(i).stateVariableDeclarationList);
        }
    }

    public void initCFG() {
        for (int i = 0; i < fn.fileContent.contractList.size(); i++) {
            System.out.println("contract: "+fn.fileContent.contractList.get(i).alias);
            resolveStateVariableDeclarationList(fn.fileContent.contractList.get(i));
            for (int j = 0; j < fn.fileContent.contractList.get(i).functionList.size(); j++) {
                resolveFunctionCall(fn.fileContent.contractList.get(i),fn.fileContent.contractList.get(i).functionList.get(j),null);
            }

            for (int j = 0; j < fn.fileContent.contractList.get(i).modifierList.size(); j++) {
                resolveFunctionCall(fn.fileContent.contractList.get(i),null,fn.fileContent.contractList.get(i).modifierList.get(j));
            }

        }

        for (int i = 0; i < fn.fileContent.libraryList.size(); i++) {
            System.out.println("Library: "+fn.fileContent.libraryList.get(i).alias);

            resolveStateVariableDeclarationList(fn.fileContent.libraryList.get(i));
            for (int j = 0; j < fn.fileContent.libraryList.get(i).functionList.size(); j++) {
                resolveFunctionCall(fn.fileContent.libraryList.get(i),fn.fileContent.libraryList.get(i).functionList.get(j),null);
            }
            for (int j = 0; j < fn.fileContent.libraryList.get(i).modifierList.size(); j++) {
                resolveFunctionCall(fn.fileContent.contractList.get(i),null,fn.fileContent.libraryList.get(i).modifierList.get(j));

            }
        }
    }

    public void resolveStateVariableDeclarationList(Instance in) {    //not finished
        for (int i = 0; i < in.stateVariableDeclarationList.size(); i++) {
            if (in.stateVariableDeclarationList.get(i).expression != null)
                resolveStateVariableFunctionCall(in, i, in.stateVariableDeclarationList.get(i).expression);
        }

    }

    public ArrayList<String> getfunctionCallFrontIdentifierList(Expression e, SolidityParser.ExpressionContext  exp) {    //done
        SolidityParser.ExpressionContext tmp = e.expressionContext;
        ArrayList<String> alias = new ArrayList<>();
        boolean added=false;
        if(exp!=null){
            if(exp.primaryExpression()!=null){
                alias.add(exp.primaryExpression().getText());
                added=true;

            }else{
                while(exp.identifier() == null){
                    if(exp.identifier()!=null){
                        alias.add(exp.identifier().getText());
                        exp = exp.expression(0);
                    }else if(exp.primaryExpression()!=null){
                        added=true;
                        alias.add(exp.primaryExpression().getText());
                        break;
                    }else {
                        alias = null;
                        break;
                    }
                }
            }
        }else{
            if (e.primaryExpression != null) {
                alias.add(e.primaryExpression.expressionContext.getText());
                added=true;
            } else {
                while (tmp.identifier() != null) {
                    if (tmp.identifier() != null) {
                        alias.add(tmp.identifier().getText());
                        tmp = tmp.expression(0);
                    } else if (tmp.primaryExpression() != null) {
                        alias.add(tmp.primaryExpression().getText());
                        added=true;

                        break;
                    } else {
                        alias = null;
                        break;
                    }

                }
            }
        }


        if (alias != null) {
            if(tmp!=null&&tmp.primaryExpression()!=null&&!added){
                alias.add(tmp.primaryExpression().getText());
            }else if(exp!=null&&exp.primaryExpression()!=null&&!added){
                alias.add(tmp.primaryExpression().getText());
            }
            Collections.reverse(alias);
        }
        return alias;

    }


    public Function matchFunctionFromInstance(Instance in, String alias) {    //done
        for (int i = 0; i < in.functionList.size(); i++) {
            if (in.functionList.get(i).alias.equals(alias)) return in.functionList.get(i);
        }
        return null;
    }


    public Library matchLibrary(FileNode fn, String alias) {    //done
        for (int i = 0; i < fn.localSourceList.size(); i++) {
            if (fn.localSourceList.get(i).alias == null && fn.localSourceList.get(i).srcModule.size() == 0) {
                for (int j = 0; j < fn.localSourceList.get(i).fn.fileContent.libraryList.size(); j++) {
                    if (fn.localSourceList.get(i).fn.fileContent.libraryList.get(j).alias.equals(alias)) {
                        return fn.localSourceList.get(i).fn.fileContent.libraryList.get(j);
                    }
                }

            } else if (fn.localSourceList.get(i).alias == null) {
                for (int j = 0; j < fn.localSourceList.get(i).srcModule.size(); j++) {
                    if (fn.localSourceList.get(i).srcModule.get(j).alias != null && fn.localSourceList.get(i).srcModule.get(j).alias.equals(alias)) {
                        return FindLibraryFromFileNode(fn.localSourceList.get(i).fn, fn.localSourceList.get(i).srcModule.get(j).moduleName);
                    } else if (fn.localSourceList.get(i).srcModule.get(j).moduleName.equals(alias)) {
                        return FindLibraryFromFileNode(fn.localSourceList.get(i).fn, fn.localSourceList.get(i).srcModule.get(j).moduleName);
                    }
                }
            }
        }
        return null;
    }

    public void resolveUsingFor(FileNode fn, Instance in){
        Library l=null;
        for(int i=0;i<in.usingForList.size();i++){
            if(in.usingForList.get(i).alias!=null) l=matchLibrary(fn,in.usingForList.get(i).alias);
            if(l!=null)in.usingForList.get(i).instance = l;
        }
    }

    public Pair<Instance,Function> matchUsingForFunction(@NotNull Instance in, String dataTypeName, String functionName){
        for(int i=0;i<in.usingForList.size();i++){
            if(in.usingForList.get(i).dataTypeName==null){
                for(int j=0;j<in.usingForList.get(i).instance.functionList.size();j++){
                    if(functionName.equals(in.usingForList.get(i).instance.functionList.get(j).alias)){
                        return new Pair<>(in.usingForList.get(i).instance, in.usingForList.get(i).instance.functionList.get(j));
                    }
                }
            }else if(in.usingForList.get(i).dataTypeName!=null&&in.usingForList.get(i).dataTypeName.equals(dataTypeName)){
                for(int j=0;j<in.usingForList.get(i).instance.functionList.size();j++){
                    if(functionName.equals(in.usingForList.get(i).instance.functionList.get(j).alias)){
                        return new Pair<>(in.usingForList.get(i).instance, in.usingForList.get(i).instance.functionList.get(j));
                    }
                }
            }
        }
        return null;
    }

    public FunctionCall matchFunctionCallFromMultiInstanceStateVariableDelcaration(Instance in, List<String> alias){
        boolean isFound;
        FunctionCall fc = null;
        for(int i=1;i<alias.size();i++){
            isFound=false;
            if(i==alias.size()-1) {
                Function f = matchFunctionFromInstance(in,alias.get(i));
                if(f!=null){
                    fc=new FunctionCall(in, f);
                    isFound=true;
                    break;
                }
            }
            for(int j=0;j<in.stateVariableDeclarationList.size();j++){
                if(in.stateVariableDeclarationList.get(j).alias.equals(alias.get(i))&&in.stateVariableDeclarationList.get(j).variableType.primaryType.equals("instance")){
                    in=in.stateVariableDeclarationList.get(j).variableType.getInstance();
                    break;
                }
            }
            if(isFound) break;
        }
        return fc;
    }

    public VariableType matchVariableTypeFromVariableDeclarationList(String alias , List<VariableDeclaration>parameterVariableDeclarationList, List<VariableDeclaration>variableDeclarationList){
        for (int i = 0; i<variableDeclarationList.size(); i++) {
            if (variableDeclarationList.get(i).alias.equals(alias)) {
                return variableDeclarationList.get(i).variableType;
            }
        }

        for(int i=0;i<parameterVariableDeclarationList.size();i++){
            if (parameterVariableDeclarationList.get(i).alias.equals(alias)) {
                return parameterVariableDeclarationList.get(i).variableType;
            }
        }
        return null;
    }

    public FunctionCall getFunctionCallFromFunctionCall(Instance in, BaseFunction baseFunction, Expression expression, SolidityParser.ExpressionContext expressionContext, List<StateVariableDeclaration> stateVariableDeclarationList, int index, List<VariableDeclaration> variableDeclarationList){
        FunctionCall f = null;
        if(expression!=null) System.out.println(expression.functionCall.expressionContext.getText());
        if(expressionContext!=null) System.out.println(expressionContext.getText());

        if((expression!=null&&expression.functionCall!=null&&expression.expressionContext.functionCall().functionName().newContract()!=null)||(expressionContext!=null&&expressionContext.functionCall()!=null&&expressionContext.functionCall().functionName().newContract()!=null)){
            Pair<FileNode, Instance> p = null;
            Contract c=null;
            List<SolidityParser.IdentifierContext> identifierList;
            if(expression!=null){
                identifierList = expression.expressionContext.functionCall().functionName().newContract().identifier();
            }else{
                identifierList = expressionContext.functionCall().functionName().newContract().identifier();
            }
            VariableType vt = resolveUserDefinedType(identifierList);
            Function ff=null;
            if(vt!=null) {
                ff = matchFunctionFromInstance(vt.getInstance(), "constructor");
            }

            if(ff!=null&&vt!=null){
                f=new FunctionCall(vt.getInstance(), ff);
            }else if(ff!=null&&p!=null){
                f=new FunctionCall(p.getValue(), ff);
            }
        }else{
            ArrayList<String> alias = new ArrayList<>();

            if(expression!=null&&expression.functionCall!=null&&expression.functionCall.front!=null){
                alias = getfunctionCallFrontIdentifierList(expression.functionCall.front,null);    //get x.y.z from x.y.z.a();

            }else if(expressionContext!=null&&expressionContext.functionCall()!=null&&expressionContext.functionCall().functionName()!=null){
                alias = getfunctionCallFrontIdentifierList(null, expressionContext);
            }
            if(alias!=null){
                if(expressionContext!=null) alias.add(expressionContext.functionCall().functionName().getText());
                else alias.add(expression.functionCall.functionName.getText());
            }
            VariableType vt = null;


            boolean isMatch = false;
            if(in.contract!=null){
                for(int i=0;i<in.contract.inheritanceList.size();i++){
                    if(alias!=null&&alias.size()!=1&&in.contract.inheritanceTypeList.get(i).identifierContextList.size()==2&&in.contract.inheritanceTypeList.get(i).identifierContextList.get(0).getText().equals(alias.get(0))&&in.contract.inheritanceTypeList.get(i).identifierContextList.get(1).getText().equals(alias.get(1))){
                        isMatch=true;
                    }else if(alias!=null&&in.contract.inheritanceTypeList.get(i).identifierContextList.size()==1&&in.contract.inheritanceTypeList.get(i).identifierContextList.get(0).getText().equals(alias.get(0))){
                        isMatch=true;
                    }
                    if(isMatch){
                        Instance instance = in.contract.inheritanceTypeList.get(i).getInstance();
                        f = matchFunctionCallFromMultiInstanceStateVariableDelcaration(instance,alias);
                        break;
                    }
                }
                if(!isMatch){
                    if(alias.size()==2&&alias.get(0).equals("super")){
                        for(int i=0;i<baseFunction.inheritanceTypeList.size();i++){
                            Instance instance = baseFunction.inheritanceTypeList.get(i).getInstance();
                            Function ff = matchFunctionFromInstance(instance, alias.get(1));
                            if(ff!=null){
                                f = new FunctionCall(instance,ff);
                                break;
                            }
                        }
                    }
                }
            }


            if(f==null&&vt==null&&baseFunction!=null&&variableDeclarationList!=null) vt = matchVariableTypeFromVariableDeclarationList(alias.get(0),baseFunction.variableDeclarationList,variableDeclarationList);
            if(f==null&&vt==null) vt = matchVariableTypeFromStateDeclarationList(stateVariableDeclarationList, index, alias.get(0));

            if(f==null&&vt!=null&&vt.instanceType!=null){
                if(alias.size()==2){
                    Function ff=matchFunctionFromInstance(vt.getInstance(),alias.get(1));    //no identifier
                    if(ff!=null)f=new FunctionCall(in,ff);
                }else{
                    f=matchFunctionCallFromMultiInstanceStateVariableDelcaration(in, alias);
                }
            }else if (f==null&&vt != null && vt.primaryType != null) {
                if(alias.size()==2){
                    Pair<Instance, Function> ff=matchUsingForFunction(in, vt.primaryType,alias.get(1));
                    if(ff!=null) f = new FunctionCall(ff.getKey(), ff.getValue());
                }
            } else if(f==null){
                Library l = matchLibrary(fn, alias.get(0));
                if (l != null) {
                    if (alias.size() == 2) {
                        Function ff = matchFunctionFromInstance(l, alias.get(1));
                        if(ff!=null) f = new FunctionCall(l, ff);
                    }

                }
            }
        }

        return f;
    }

    public void resolveStateVariableFunctionCall(Instance in, int index, Expression e) {    //not finished
        if (e.functionCall != null) {
            FunctionCall fc1 = getFunctionCallFromFunctionCall(in,null,e,null,in.stateVariableDeclarationList,index,null);
            if(fc1!=null) in.functionCallList.add(fc1);
            for(int i=0;i<e.functionCall.nameValueList.size();i++){
                FunctionCall fc2 = getFunctionCallFromFunctionCall(in,null, e.functionCall.nameValueList.get(i).getKey(),null, in.stateVariableDeclarationList,index, null);
                if(fc2!=null) in.functionCallList.add(fc2);
            }
        } else if (e.conditionalDeclaration != null) {
            for(int i=0;i<e.conditionalDeclaration.expressionList.size();i++){
                resolveStateVariableFunctionCall(in,index,e.conditionalDeclaration.expressionList.get(i));
            }
        } else if (e.conditionalExpression != null) {
            for(int i=0;i<e.conditionalExpression.expressionList.size();i++){
                resolveStateVariableFunctionCall(in,index,e.conditionalExpression.expressionList.get(i));
            }
        } else if (e.environmentalVariable != null) {
            for(int i=0;i<e.environmentalVariable.expressionList.size();i++){
                resolveStateVariableFunctionCall(in,index,e.environmentalVariable.expressionList.get(i));
            }
        } else if (e.equality != null) {
            if(e.equality.left!=null) resolveStateVariableFunctionCall(in,index,e.equality.left);
            if(e.equality.right!=null)resolveStateVariableFunctionCall(in,index,e.equality.right);
        } else if (e.functionIdentifier != null) {
            resolveStateVariableFunctionCall(in,index,e.functionIdentifier.front);
        }  else if (e.operatorExpression != null) {
            for(int i=0;i<e.operatorExpression.expressionList.size();i++){
                resolveStateVariableFunctionCall(in,index,e.operatorExpression.expressionList.get(i));
            }
        }  else if (e.staticArray != null) {
            if(e.staticArray.left!=null)resolveStateVariableFunctionCall(in,index,e.staticArray.left);
            if(e.staticArray.right!=null)resolveStateVariableFunctionCall(in,index,e.staticArray.right);
            if(e.staticArray.front!=null)resolveStateVariableFunctionCall(in,index,e.staticArray.front);
        } else if (e.tupleExpression != null) {
            for(int i=0;i<e.tupleExpression.expressionList.size();i++){
                resolveStateVariableFunctionCall(in,index,e.tupleExpression.expressionList.get(i));
            }
        } else if (e.typeExpression != null) {
            for(int i=0;i<e.typeExpression.expressionList.size();i++){
                resolveStateVariableFunctionCall(in,index,e.typeExpression.expressionList.get(i));
            }
        } else if (e.varDeclaration != null) {
            for(int i=0;i<e.varDeclaration.expressionList.size();i++){
                resolveStateVariableFunctionCall(in,index,e.varDeclaration.expressionList.get(i));
            }
        }else if(e.primaryExpression!=null){
            for(int i=0;i<e.primaryExpression.expressionList.size();i++){
                resolveStateVariableFunctionCall(in,index,e.primaryExpression.expressionList.get(i));
            }
        }
    }

    public void resolveFunctionCall(Instance in, BaseFunction baseFunction, Modifier modifier) {
        if(modifier==null){
            if(baseFunction.function!=null){
                if(baseFunction.function.block!=null){
                    resolveBlockVariableType(in, baseFunction.function.block);
                }
                if(baseFunction.function!=null) resolveFunctionFunctionCall(in,baseFunction.function);


            }else if(baseFunction.function.fallbackFunction!=null){
                resolveBlockVariableType(in, baseFunction.fallbackFunction.block);
            }
        } else{
            if(modifier.block!=null)
            resolveBlockVariableType(in, modifier.block);
        }
        if(baseFunction!=null) {
            resolveFunctionPassParameterVariableType(in, baseFunction);
            resolveBigBlock(in, baseFunction, null);
        } else {
            resolveBigBlock(in,null, modifier);
        }
    }

    public void resolveDerivedConstructorCallArguments(Instance in, Function function, SolidityParser.CallArgumentsContext callArgumentsContext){
        if(callArgumentsContext.nameValueList()!=null){
            for(int i=0;i<callArgumentsContext.nameValueList().expression().size();i++){
                FunctionCall fc = getFunctionCallFromFunctionCall(in,function,null,callArgumentsContext.nameValueList().expression(i), in.stateVariableDeclarationList, in.stateVariableDeclarationList.size()-1, null);
                if(fc!=null) in.functionCallList.add(fc);
            }
        }

    }

    public void resolveFunctionFunctionCall(Instance in,Function function){

        for(int i=0;i<function.modifierList.size();i++){

            for(int j=0;j<in.modifierList.size();j++){
                if(function.modifierList.get(i).equals(in.modifierList.get(j).alias)){
                    function.modifiers.add(in.modifierList.get(j));
                    break;
                }
            }
        }

        for(int i=0;i<in.inheritanceTypeList.size();i++){
            Function f = matchFunctionFromInstance(in.inheritanceTypeList.get(i).getInstance(), "constructor");
            if(f!=null) {
                FunctionCall fc = new FunctionCall(in.inheritanceTypeList.get(i).getInstance(),f);
                in.functionCallList.add(fc);
            }
        }

        for(int i = 0; i<function.derivedconstructorList.size(); i++){
            SolidityParser.ExpressionContext e = function.derivedconstructorList.get(i);

            resolveDerivedConstructorCallArguments(in,function,e.functionCall().callArguments());
        }
    }

    public void resolveFunctionPassParameterVariableType(Instance in, BaseFunction f){
        for(int i=0;i<f.parameterList.size();i++){
            VariableType v =null;
            if(f.parameterList.get(i).dataType.userDefinedTypeName()!=null){
                v = resolveUserDefinedType(f.parameterList.get(i).dataType.userDefinedTypeName());
            }else {
                v = new VariableType(f.parameterList.get(i).dataType.getText());
            }
            if(v!=null){
                VariableDeclaration vd = new VariableDeclaration(f.parameterList.get(i).dataType,null, f.parameterList.get(i).alias, null);
                f.variableDeclarationList.add(vd);
            }
        }
    }

    public void resolveBigBlock(Instance in, BaseFunction baseFunction, Modifier modifier){
        if(baseFunction!=null&&baseFunction.function!=null&&baseFunction.function.block!=null){
            Function f = baseFunction.function;
            resolveBlockFunctionCall(in, f, null, f.block);
        }else if(baseFunction!=null&&baseFunction.fallbackFunction!=null){
            FallbackFunction f = baseFunction.fallbackFunction;
            resolveBlockFunctionCall(in, f, null, f.block);
        }else if(modifier!=null){
            if(modifier.block!=null){
                resolveBlockFunctionCall(in, null, modifier, modifier.block);
            }
        }
    }

    public void resolveBlockFunctionCall(Instance instance, BaseFunction baseFunction, Modifier modifier,Block block){
        for(int i=0;i<block.statementList.size();i++){
            resolveStatementFunctionCall(instance,baseFunction,modifier, block,null,  block.statementList.get(i));
        }
    }

    public void resolveStatementFunctionCall(Instance instance,BaseFunction baseFunction, Modifier modifier, Block block, Statement statement, Statement thisStatement){
        if(thisStatement.blockStatement!=null){
            for(int i=0;i<thisStatement.blockStatement.statementList.size();i++){
                resolveStatementFunctionCall(instance,baseFunction,modifier, null,thisStatement.blockStatement, thisStatement.blockStatement.statementList.get(i));
            }
        }else if(thisStatement.conditionalStatement!=null){
            resolveExpressionFunctionCall(instance, baseFunction, modifier, null, thisStatement.conditionalStatement, thisStatement.conditionalStatement.condition);
            for(int i=0;i<thisStatement.statementList.size();i++)resolveStatementFunctionCall(instance,baseFunction,modifier,null,thisStatement.conditionalStatement, thisStatement.statementList.get(i));
        }else if(thisStatement.emitEventStatement!=null){
            for(int i=0;i<statement.statementList.size();i++) resolveExpressionFunctionCall(instance, baseFunction, modifier, block, thisStatement, thisStatement.emitEventStatement.nameValueList.get(i).getKey());
        }else if(thisStatement.expressionStatement!=null){
            resolveExpressionFunctionCall(instance, baseFunction, modifier, block, thisStatement, thisStatement.expressionStatement.e);
        }else if(thisStatement.requireStatement!=null){
            for(int i=0;i<thisStatement.statementList.size();i++) resolveExpressionFunctionCall(instance, baseFunction, modifier, block, thisStatement, thisStatement.expressionList.get(i));
        }else if(thisStatement.tryCatchStatement!=null){
            resolveBlockFunctionCall(instance, baseFunction, modifier, thisStatement.tryCatchStatement.block1);
            resolveBlockFunctionCall(instance, baseFunction, modifier, thisStatement.tryCatchStatement.block2);

        }

    }

    public void resolveExpressionFunctionCall(Instance instance,BaseFunction baseFunction, Modifier modifier, Block block, Statement statement, Expression e){
        List<VariableDeclaration> variableDeclarationList = null;
        if(block!=null) variableDeclarationList=block.variableDeclarationList;
        else variableDeclarationList=statement.variableDeclarationList;
        if(e.conditionalDeclaration!=null){
            resolveExpressionFunctionCall(instance, baseFunction, modifier, block, statement, e.conditionalDeclaration);
        }else if(e.conditionalExpression!=null){
            resolveExpressionFunctionCall(instance, baseFunction, modifier, block, statement, e.conditionalExpression.expressionList.get(0));
        }else if(e.environmentalVariable!=null){
            if(e.environmentalVariable.expressionList.size()!=0) resolveExpressionFunctionCall(instance, baseFunction, modifier, block, statement, e.environmentalVariable.expressionList.get(0));

        }else if(e.equality!=null){
            if(e.equality.left!=null) resolveExpressionFunctionCall(instance, baseFunction, modifier, block, statement, e.equality.left);
            if(e.equality.right!=null) resolveExpressionFunctionCall(instance, baseFunction, modifier, block, statement, e.equality.right);
        }else if(e.functionCall!=null){
            FunctionCall fc = getFunctionCallFromFunctionCall(instance,baseFunction, e, null, instance.stateVariableDeclarationList, instance.stateVariableDeclarationList.size()-1, variableDeclarationList);
            if(fc!=null){
                if(modifier!=null)modifier.functionCallList.add(fc);
                else baseFunction.functionCallList.add(fc);
            }
            for(int i=0;i<e.functionCall.nameValueList.size();i++){
                resolveExpressionFunctionCall(instance, baseFunction, modifier, block, statement, e.functionCall.nameValueList.get(i).getKey());

            }
        }else if(e.functionIdentifier!=null){
            resolveExpressionFunctionCall(instance, baseFunction, modifier, block, statement, e.functionIdentifier.front);
        }else if(e.operatorExpression!=null){
            for(int i=0;i<e.operatorExpression.expressionList.size();i++) resolveExpressionFunctionCall(instance, baseFunction, modifier, block, statement, e.operatorExpression.expressionList.get(i));
        }else if(e.staticArray!=null){
            if(e.staticArray.left!=null) resolveExpressionFunctionCall(instance, baseFunction, modifier, block, statement, e.staticArray.left);
            if(e.staticArray.right!=null) resolveExpressionFunctionCall(instance, baseFunction, modifier, block, statement, e.staticArray.right);
            if(e.staticArray.front!=null) resolveExpressionFunctionCall(instance, baseFunction, modifier, block, statement, e.staticArray.front);
        }else if(e.tupleExpression!=null){
            for(int i=0;i<e.tupleExpression.expressionList.size();i++){
                resolveExpressionFunctionCall(instance, baseFunction, modifier, block, statement, e.tupleExpression.expressionList.get(i));
            }
        }else if(e.typeExpression!=null){
            for(int i=0;i<e.typeConversion.expressionList.size();i++){
                resolveExpressionFunctionCall(instance, baseFunction, modifier, block, statement, e.typeConversion.expressionList.get(i));
            }
        }else if(e.varDeclaration!=null){
            for(int i=0;i<e.varDeclaration.expressionList.size();i++){
                resolveExpressionFunctionCall(instance, baseFunction, modifier, block, statement, e.varDeclaration.expressionList.get(i));
            }
        }
    }

    public void resolveStatementVariableType(Instance in, Statement s){
        if(s.blockStatement!=null){
            for(int i=0;i<s.blockStatement.statementList.size();i++){
                resolveStatementVariableType(in, s.blockStatement.statementList.get(i));
            }
        }else if(s.conditionalStatement!=null){
            for(int i=0;i<s.conditionalStatement.statementList.size();i++){
                resolveStatementVariableType(in, s.conditionalStatement.statementList.get(i));
            }
        }else if(s.expressionStatement!=null){
            if(s.expressionStatement.e.variableDeclaration!=null){    //List<VariableDeclaration> variableDeclarationList,StateVariableDeclaration s, VariableDeclaration vd

                s.expressionStatement.e.variableDeclaration.variableType= getVariableTypeofVariableDeclaration(in,null, s.expressionStatement.e.variableDeclaration);
            }else if(s.expressionStatement.e.equality!=null){
                if(s.expressionStatement.e.equality.left!=null&&s.expressionStatement.e.equality.left.variableDeclaration!=null){
                    s.expressionStatement.e.equality.left.variableDeclaration.variableType= getVariableTypeofVariableDeclaration(in,null, s.expressionStatement.e.equality.left.variableDeclaration);
                }
            }
        }else if(s.tryCatchStatement!=null){
            resolveBlockVariableType(in,s.tryCatchStatement.block1);
            resolveBlockVariableType(in,s.tryCatchStatement.block2);
        }
    }

    public void resolveBlockVariableType(Instance in , Block block){
        for(int i=0;i<block.statementList.size();i++){
            resolveStatementVariableType(in, block.statementList.get(i));
        }
    }

    public VariableType matchVariableTypeFromStateDeclarationList(List<StateVariableDeclaration> stateVariableDeclarationList, int index, String alias) {    //done
        for (int i = index; i >= 0; i--) {
            if (stateVariableDeclarationList.get(i).alias.equals(alias)) {
                return stateVariableDeclarationList.get(i).variableType;
            }
        }
        return null;
    }





    public List<StateVariableDeclaration> getStateVariableDeclarationList(Instance in, List<StateVariableDeclaration> stateVariableDeclarationList) {    //done
        for (int i = 0; i < stateVariableDeclarationList.size(); i++) {
            stateVariableDeclarationList.get(i).variableType = getVariableTypeofVariableDeclaration(in, stateVariableDeclarationList.get(i), null);
        }
        return stateVariableDeclarationList;
    }


    public VariableType getVariableTypeofVariableDeclaration(Instance in, StateVariableDeclaration s, VariableDeclaration vd) {    //done
        VariableType v = null;
        if (s != null) {
            if (s.primaryVariable != null && s.primaryVariable.dataType.userDefinedTypeName() != null) {
                v = resolveUserDefinedType(s.primaryVariable.dataType.userDefinedTypeName());

            } else if (s.mappingVariable != null) {
                v = new VariableType("mapping");
            } else if (s.primaryVariable != null) {
                v = new VariableType(s.primaryVariable.dataType.getText(), true);
            }
        } else if (vd != null) {
            if (vd.dataType.userDefinedTypeName() != null) {
                v = resolveUserDefinedType(vd.dataType.userDefinedTypeName());
            } else if (vd.dataType.mappingSt() != null) {
                v = new VariableType("mapping");
            }else if (vd.dataType.elementaryTypeName() != null) {
                v = new VariableType(vd.dataType.getText());
            }
        }

        return v;
    }

    public VariableType resolveUserDefinedType(List<SolidityParser.IdentifierContext> identifierContextList) {
        VariableType v = null;
        Contract c = null;
        if (identifierContextList.size() == 1) {
            c = matchThisFileContract(identifierContextList.get(0).getText());
        }

        if (c == null) {
            Pair<FileNode, Instance> otherFileContract = matchLocalSource(identifierContextList);
            if (otherFileContract != null) v = new VariableType(otherFileContract);
            if (v == null) {
                v = new VariableType("instance");
            }
        }else{
            v=new VariableType(c);
        }

        return v;
    }

    public VariableType resolveUserDefinedType(SolidityParser.UserDefinedTypeNameContext userDefinedTypeNameContext) {
        VariableType v = null;
        Contract c = null;
        if (userDefinedTypeNameContext.identifier().size() == 1) {
            c = matchThisFileContract(userDefinedTypeNameContext.getText());
            v = new VariableType(c);
        }

        if (c == null) {
            Pair<FileNode, Instance> otherFileContract = matchLocalSource(userDefinedTypeNameContext.identifier());
            if (otherFileContract != null) v = new VariableType(otherFileContract);
            if (v == null) {
                v = new VariableType("instance");
            }
        }
        return v;
    }


    public List<InheritanceType> getInheritanceList(Instance instance) {
        List<InheritanceType> inheritanceTypeList = new ArrayList<>();
        if (instance != null&&instance.contract!=null) {
            for (int i = 0; i < instance.contract.inheritanceList.size(); i++) {
                InheritanceType it = mapInheritance(instance.contract.inheritanceList.get(i).userDefinedTypeName().identifier());
                if (it!=null) {
                    inheritanceTypeList.add(it);
                }
            }
        }
        return inheritanceTypeList;
    }


    public InheritanceType mapInheritance(List<SolidityParser.IdentifierContext> ctxList) {
        InheritanceType inheritanceType = null;
        Pair p = null;
        if (ctxList.size() == 1) {
            Contract c = matchThisFileContract(ctxList.get(0).getText());    //this file contracts
            if (c == null) {
                p = matchLocalSource(ctxList);
            }else{
                inheritanceType=new InheritanceType(c);
            }


        } else if (ctxList.size() == 2) {
            p = matchLocalSource(ctxList);
        }
        if(p!=null) inheritanceType = new InheritanceType(p);

        if(inheritanceType!=null) inheritanceType.identifierContextList=ctxList;
        return inheritanceType;
    }


    public Pair<FileNode, Instance> matchLocalSource(List<SolidityParser.IdentifierContext> ctxList) {
        Pair<FileNode, Instance> localSource = null;
        Contract c = null;
        LocalSource ls;
        if (ctxList.size() == 1) {    //C c = new C(); ->C
            for (int i = 0; i < fn.localSourceList.size(); i++) {
                ls = fn.localSourceList.get(i);
                if (ls.alias == null && ls.srcModule.size() == 0) {
                    //'import' stringLiteral
                    c = FindContractFromFileNode(ls.fn, ctxList.get(0).getText());

                } else if (ls.alias == null) {
                    // 'import' *|identifier ('as' identifier)? 'from' stringLiteral
                    // 'import' '{' importDeclaration (',' importDeclaration)* '}' 'from' stringLiteral ';' ;
                    c = matchSourceModule(ls, ctxList.get(0).getText());
                }
                if (c != null) {
                    localSource = new Pair(ls.fn, c);
                    break;
                }
            }
        } else {  //C.B c = new C.B();
            String alias = ctxList.get(0).getText();
            System.out.println("alias " + alias);

            String contractName = ctxList.get(1).getText();
            for (int i = 0; i < fn.localSourceList.size(); i++) {
                ls = fn.localSourceList.get(i);
                if (ls.alias != null && ls.alias.equals(alias) && ls.srcModule.size() == 0) {
                    c = FindContractFromFileNode(ls.fn, contractName);
                } else if (ls.alias != null && ls.alias.equals(alias)) {
                    c = matchSourceModule(ls, contractName);
                }
                if (c != null) {
                    localSource = new Pair(ls.fn, c);
                    break;
                }
            }

        }
        return localSource;
    }


    public Contract matchSourceModule(LocalSource ls, String alias) {
        Contract c = null;
        if (ls.srcModule.size() != 0) {
            for (int i = 0; i < ls.srcModule.size(); i++) {
                SourceModule srm = ls.srcModule.get(i);
                if (srm.alias == null) {
                    c = FindContractFromFileNode(ls.fn, ls.srcModule.get(i).moduleName);
                } else if (ls.srcModule.get(i).alias.equals(alias)) {
                    c = FindContractFromFileNode(ls.fn, ls.srcModule.get(i).moduleName);
                }
                if (c != null) return c;

            }
        }

        return c;
    }

    public Library FindLibraryFromFileNode(FileNode fn, String alias) {
        for (int i = 0; i < fn.fileContent.libraryList.size(); i++) {
            if (alias.equals(fn.fileContent.libraryList.get(i).alias)) {
                return fn.fileContent.libraryList.get(i);
            }
        }
        return null;
    }

    public Contract FindContractFromFileNode(FileNode fn, String alias) {
        Contract c = null;
        for (int i = 0; i < fn.fileContent.contractList.size(); i++) {
            if (alias.equals(fn.fileContent.contractList.get(i).alias)) {
                c = fn.fileContent.contractList.get(i);
                break;
            }
        }
        return c;
    }

    public Contract matchThisFileContract(String alias) {
        Contract c;

        for (int i = 0; i < fn.fileContent.contractList.size(); i++) {
            c = fn.fileContent.contractList.get(i);
            if (c.alias.equals(alias)) {
                return c;
            }
        }
        return null;
    }


}
