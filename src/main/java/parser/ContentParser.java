package parser;

import org.antlr.v4.runtime.ParserRuleContext;
import parser.Base.SolidityBaseListener;
import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BasicContractDefinition.Function;
import utils.Content.ContractNodeType.ExpressionDefinition.Equality;
import utils.Content.ContractNodeType.ExpressionDefinition.NewDynamicArray;
import utils.Content.ContractNodeType.ExpressionDefinition.StaticArray;
import utils.Content.ContractNodeType.ExpressionDefinition.VariableDeclaration;
import utils.Content.ContractNodeType.StateVariableDeclaration.FunctionVariable;
import utils.Content.ContractNodeType.StateVariableDeclaration.MappingVariable;
import utils.Content.ContractNodeType.StateVariableDeclaration.PrimaryVariable;
import utils.Content.ContractNodeType.StateVariableDeclaration.StateVariableDeclaration;
import utils.File.FileNode;
import utils.File.FileTree;
import utils.Content.ContractNodeType.BasicContractDefinition.*;
import utils.Content.ContractNodeType.BasicContractDefinition.Enum;
import utils.Content.ContractNodeType.SolidityClassDefinition.Contract;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;
import utils.Content.ContractNodeType.SolidityClassDefinition.Interface;
import utils.Content.ContractNodeType.SolidityClassDefinition.Library;
import utils.Source.GlobalSource;
import utils.Source.LocalSource;
import utils.Tools.Path.PathResolver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContentParser extends SolidityBaseListener {
    FileNode fn;


/*    Source Parser Part    */
    public ContentParser(FileNode fn){
        this.fn=fn;
    }

    @Override
    public void enterImportFile(SolidityParser.ImportFileContext ctx) {    //same as import all
        Path sourcePath = PathResolver.resolveImportedPath(Paths.get(fn.path),ctx.stringLiteral().getText());
        FileNode source_node= FileTree.findFileNode(sourcePath.toString());   //find if it is in the same directory
        GlobalSource gs;
        LocalSource s;
        String alias;
        if(ctx.identifier()==null){
            if(source_node==null){   //source not found from the directory which means it is from the internet
                gs = new GlobalSource(sourcePath,null);
                fn.globalSourceList.add(gs);
            } else {
                s = new LocalSource(source_node,null);
                fn.localSourceList.add(s);
            }

        }else { //with identifier
            alias=ctx.identifier().getText().replaceAll("^\"|\"$", "");
            if(source_node==null){
                gs = new GlobalSource(sourcePath,alias);
                fn.globalSourceList.add(gs);
            }else{
                s = new LocalSource(source_node,alias);
                fn.localSourceList.add(s);
            }
        }
    }

    @Override
    public void enterImportFileAsSymbol(SolidityParser.ImportFileAsSymbolContext ctx) {
        Path sourcePath = PathResolver.resolveImportedPath(Paths.get(fn.path),ctx.stringLiteral().getText());
        FileNode source_node=FileTree.findFileNode(sourcePath.toString());   //find if it is in the same directory
        GlobalSource gs;
        LocalSource s;

        if(ctx.identifier()==null){ //import file by default identifier
            if(source_node==null&&ctx.importAllOrNot().identifier()==null){
                //source not found from the directory which means it is from the internet
                //import all
                gs = new GlobalSource(sourcePath,null);
                fn.globalSourceList.add(gs);
            } else if(source_node==null){
                //source not found from the directory which means it is from the internet
                //import one contract or library only
                gs = new GlobalSource(sourcePath,null);
                gs.addModule(ctx.importAllOrNot().identifier().getText(),null);
                fn.globalSourceList.add(gs);
            } else if(ctx.importAllOrNot().identifier()==null){
                //source can be found from the directory
                //import all
                s = new LocalSource(source_node,null);
                fn.localSourceList.add(s);
            }else{
                //source can be found from the directory
                //import one contract or library only
                s = new LocalSource(source_node,null);
                s.addModule(ctx.importAllOrNot().identifier().getText(),null);
                fn.localSourceList.add(s);
            }
        }else{  //import file by using identifier
            if(source_node==null&&ctx.importAllOrNot().identifier()==null){
                //source not found from the directory which means it is from the internet
                //import all
                gs = new GlobalSource(sourcePath,ctx.identifier().getText());
                fn.globalSourceList.add(gs);
            } else if(source_node==null){
                //source not found from the directory which means it is from the internet
                //import one contract or library only
                gs = new GlobalSource(sourcePath,ctx.identifier().getText());
                gs.addModule(ctx.importAllOrNot().identifier().getText(),null);
                fn.globalSourceList.add(gs);
            } else if(ctx.importAllOrNot().identifier()==null){
                //source can be found from the directory
                //import all
                s = new LocalSource(source_node,ctx.identifier().getText());
                fn.localSourceList.add(s);
            }else{
                //source can be found from the directory
                //import one contract or library only
                s = new LocalSource(source_node,ctx.identifier().getText());
                s.addModule(ctx.importAllOrNot().identifier().getText(),null);
                fn.localSourceList.add(s);
            }
        }

    }

    @Override
    public void enterImportFileFrom(SolidityParser.ImportFileFromContext ctx) {
        Path sourcePath = PathResolver.resolveImportedPath(Paths.get(fn.path),ctx.stringLiteral().getText());
        FileNode source_node=FileTree.findFileNode(sourcePath.toString());   //find if it is in the same directory
        GlobalSource gs;
        LocalSource s;

        if(source_node==null){
            gs = new GlobalSource(sourcePath,null);
            ctx.importDeclaration().forEach(x->{
                if(x.identifier(1)==null){  //contract or library without identifier
                    gs.addModule(x.identifier(0).getText(),null);
                }else{
                    gs.addModule(x.identifier(0).getText(),x.identifier(1).getText());
                }
            });
            fn.globalSourceList.add(gs);
        }else{
            s=new LocalSource(source_node,null);
            ctx.importDeclaration().forEach(x->{
                if(x.identifier(1)==null){  //contract or library without identifier
                    s.addModule(x.identifier(0).getText(),null);
                }else{
                    s.addModule(x.identifier(0).getText(),x.identifier(1).getText());
                }
            });
            fn.localSourceList.add(s);
        }
    }


/*    Content Parser Part    */

    private void parseInsideContract(Instance n, ParserRuleContext ctx){
        int count=0;

        SolidityParser.ContractPartDefinitionContext tmp = ctx.getChild(SolidityParser.ContractPartDefinitionContext.class,count);
        while(tmp!=null){
            if(tmp.usingForDeclaration()!=null){
                UsingFor uf;
                if(tmp.usingForDeclaration().typeName()!=null){
                    uf = new UsingFor(tmp.usingForDeclaration().identifier().getText(),tmp.usingForDeclaration().typeName().getText());
                }else{
                    uf = new UsingFor(tmp.usingForDeclaration().identifier().getText(),null);
                }
                n.addUsingFor(uf);
                System.out.println("using for");
            }else if(tmp.structDefinition()!=null){
                Struct s = new Struct(tmp.structDefinition().identifier().getText());
                tmp.structDefinition().variableDeclaration().forEach(x->{
                    s.addVariableDeclaration(x);
                });
                n.addStruct(s);
                System.out.println(s.alias);
            }else if(tmp.stateVariableDeclaration()!=null){
                //n.stateVariableDeclarationList = Stream.concat(n.stateVariableDeclarationList.stream(),initMultipleStateVariable(tmp.stateVariableDeclaration()).stream()).collect(Collectors.toList());
            } else if(tmp.modifierDefinition()!=null){
                Modifier m = new Modifier(tmp.modifierDefinition().identifier().getText(),tmp.modifierDefinition().block());
                if(tmp.modifierDefinition().parameterList()!=null){
                    m.parameterList = parameterContextList2ParameterList(tmp.modifierDefinition().parameterList());
                }
                n.addModifier(m);
                System.out.println(m.alias);
            }else if(tmp.functionDefinition()!=null&&tmp.functionDefinition().functionIdentifier()!=null){  //normal function
                Function f;
                if(tmp.functionDefinition().stateMutability().size()!=0){
                    f=new Function(tmp.functionDefinition().functionIdentifier().identifier().getText(),tmp.functionDefinition().block(),tmp.functionDefinition().stateMutability(0).getText(),tmp.functionDefinition().visibleType(0).getText());
                }else f=new Function(tmp.functionDefinition().functionIdentifier().identifier().getText(),tmp.functionDefinition().block(),null,tmp.functionDefinition().visibleType(0).getText());
                f.parameterList=parameterContextList2ParameterList(tmp.functionDefinition().parameterList());
                if(tmp.functionDefinition().identifier().size()!=0) f.modifierList=functionIdentifier2ModifierList(tmp.functionDefinition());
                if(tmp.functionDefinition().inheritance().size()!=0){
                    f.inheritanceContext=tmp.functionDefinition().inheritance(0);
                }
                if(tmp.functionDefinition().expression()!=null) f.modifierWithParameterList=functionExpression2ModifierList(tmp.functionDefinition());
                if(tmp.functionDefinition().returnsParameters()!=null)f.returnParameterList=functionReturnsParameterListContext2ParameterList(tmp.functionDefinition().returnsParameters().parameterList());

                n.addFunction(f);
                System.out.println(f.alias);
                System.out.println("Identifier: ");
                //getFunctionCall(tmp.functionDefinition());
            }else if(tmp.functionDefinition()!=null){   //constructor
                Function f;
                String stateMutability=null;
                SolidityParser.BlockContext b = null;
                String visibleType = null;
                if(tmp.functionDefinition().visibleType()!=null&&tmp.functionDefinition().visibleType().size()!=0){
                    visibleType=tmp.functionDefinition().visibleType(0).getText();
                }
                if(tmp.functionDefinition().stateMutability()!=null&&tmp.functionDefinition().stateMutability().size()!=0){
                    stateMutability=tmp.functionDefinition().getText();
                }
                if(tmp.functionDefinition().block()!=null) b = tmp.functionDefinition().block();

                f=new Function("constructor",b,stateMutability,visibleType);
                SolidityParser.ParameterListContext parameterListContext = tmp.functionDefinition().parameterList();
                f.parameterList=parameterContextList2ParameterList(parameterListContext);

                if(tmp.functionDefinition().identifier()!=null) f.modifierList=functionIdentifier2ModifierList(tmp.functionDefinition());
                if(tmp.functionDefinition().inheritance()!=null&&tmp.functionDefinition().inheritance().size()!=0){
                    f.inheritanceContext=tmp.functionDefinition().inheritance(0);
                }
                if(tmp.functionDefinition().expression()!=null) f.modifierWithParameterList=functionExpression2ModifierList(tmp.functionDefinition());  //derived constructor as modifier

                n.addFunction(f);
                System.out.println("constructor");
            }else if(tmp.functionFallBackDefinition()!=null){   //fallback function
                FallbackFunction f;
                String stateMutability=null;
                SolidityParser.BlockContext b = null;

                if(tmp.functionFallBackDefinition().stateMutability()!=null&&tmp.functionFallBackDefinition().stateMutability().size()!=0){
                    stateMutability=tmp.functionFallBackDefinition().stateMutability(0).getText();
                }

                if(tmp.functionFallBackDefinition().block()!=null){
                    b=tmp.functionFallBackDefinition().block();
                }

                f=new FallbackFunction(b);
                f.alias = tmp.functionFallBackDefinition().fallbackIdentifier().getText();
                f.stateMutability=stateMutability;

                if(tmp.functionFallBackDefinition().inheritance()!=null){
                    tmp.functionFallBackDefinition().inheritance().forEach(x->f.inheritanceContext=x);
                }
                if(tmp.functionFallBackDefinition().returnsParameters()!=null){
                    SolidityParser.ParameterListContext parameterListContext = tmp.functionFallBackDefinition().returnsParameters().parameterList();
                    f.returnParameterList= functionReturnsParameterListContext2ParameterList(parameterListContext);
                }
                n.addFallbackFunction(f);
                System.out.println("Fallback: "+f.alias);
            }else if(tmp.eventDefinition()!=null){
                n.addEvent(tmp.eventDefinition().identifier().getText());
                System.out.println("Event: "+n.eventList.get(n.eventList.size()-1));
            }else if(tmp.enumDefinition()!=null){
                Enum e = new Enum(tmp.enumDefinition().identifier().getText());
                tmp.enumDefinition().enumValue().forEach(x->{
                    e.addValue(x.getText());
                });
                n.addEnum(e);
                System.out.println(n.enumList.get(n.enumList.size()-1).alias);
            }

            count++;
            tmp=ctx.getChild(SolidityParser.ContractPartDefinitionContext.class,count);
        }


    }

    @Override
    public void enterContractDefinition(SolidityParser.ContractDefinitionContext ctx) {
        Contract c = new Contract(ctx.identifier().getText(),ctx);
        fn.fileContent.contractList.add(c);
        ctx.inheritanceSpecifier().forEach(x->{
            c.addInheritance(x.getText());
        });

        parseInsideContract(c, ctx);
    }

    @Override
    public void enterLibraryDefinition(SolidityParser.LibraryDefinitionContext ctx) {
        Library l = new Library(ctx.identifier().getText(),ctx);
        fn.fileContent.libraryList.add(l);

        parseInsideContract(l,ctx);
    }

    @Override
    public void enterInterfaceDefinition(SolidityParser.InterfaceDefinitionContext ctx) {
        Interface in = new Interface(ctx.identifier().getText(),ctx);
        fn.fileContent.interfaceList.add(in);
        ctx.inheritanceSpecifier().forEach(x->{
            in.addInheritance(x.getText());
        });

    }


    /*
    * Function/Parameter/... Context to Object
     */
    private static List<Parameter> parameterContextList2ParameterList(SolidityParser.ParameterListContext ctx){
        List<Parameter> parameterList = new ArrayList<>();
        Parameter p;
        for(int i=0;i<ctx.parameter().size();i++){
            if(ctx.parameter(i).storageLocation()!=null&&ctx.parameter(i).identifier()!=null){
                p=new Parameter(ctx.parameter(i).identifier().getText(),  ctx.parameter(i).typeName().getText(), ctx.parameter(i).storageLocation().getText());
            }else if(ctx.parameter(i).storageLocation()!=null){
                p=new Parameter(null, ctx.parameter(i).typeName().getText(), ctx.parameter(i).storageLocation().getText());

            }else if(ctx.parameter(i).storageLocation()==null&&ctx.parameter(i).identifier()!=null){
                p=new Parameter(ctx.parameter(i).identifier().getText(),  ctx.parameter(i).typeName().getText(), null);

            }else{
                p=new Parameter(null,  ctx.parameter(i).typeName().getText(), null);
            }
            parameterList.add(p);
        }
        return parameterList;
    }

    private static List<String> functionIdentifier2ModifierList(SolidityParser.FunctionDefinitionContext ctx ){
        List<String>modifierList = new ArrayList<>();
        for(int i=1;i<ctx.identifier().size();i++){
            modifierList.add(ctx.identifier(i).getText());
        }
        return modifierList;
    }

    private static List<String> functionReturnsParameterListContext2ParameterList(SolidityParser.ParameterListContext parameterListContexts){
        List<String> returnList = new ArrayList<>();
        for(int i=0;i<parameterListContexts.parameter().size();i++){
            returnList.add(parameterListContexts.parameter(i).getText());
        }
        return returnList;
    }

    private static List<SolidityParser.ExpressionContext> functionExpression2ModifierList(SolidityParser.FunctionDefinitionContext ctx){
        List<SolidityParser.ExpressionContext> modifierList = new ArrayList<>();
        for(int i=0;i<ctx.expression().size();i++){
            modifierList.add(ctx.expression(i));
        }
        return modifierList;
    }


    private static List<StateVariableDeclaration> initMultipleStateVariable(SolidityParser.StateVariableDeclarationContext ctx){
        List<StateVariableDeclaration> stateVariableDeclarationList = new ArrayList<>();
        StateVariableDeclaration v;

        String visible=null;
        if(ctx.visibleType()!=null){
            visible=ctx.visibleType().getText();
        }

        boolean isConstant=false;
        if(ctx.constantType()!=null){
            isConstant=true;
        }

        Expression e = null;
        if(ctx.expression()!=null)e=expressionContext2Expression(ctx.expression());

        for(int i=0;i<ctx.identifier().size();i++){
            if(ctx.typeName().mappingSt()!=null){
                v=new MappingVariable(ctx.identifier(i).getText(),ctx.typeName().mappingSt().typeName(0).getText(), ctx.typeName().mappingSt().typeName(1).getText(),visible, isConstant,e);
            }else if(ctx.typeName().functionTypeName()!=null){
                SolidityParser.FunctionTypeNameContext t = ctx.typeName().functionTypeName();
                List<SolidityParser.ParameterListContext> parameterListContexts = t.parameterList();
                String fv = null;
                String fs = null;
                if(t.visibleType().size()!=0) fv = t.visibleType(0).getText();
                if(t.stateMutability().size()!=0) fs = t.stateMutability(0).getText();
                v=new FunctionVariable(ctx.identifier(i).getText(), visible, fs, fv, isConstant, e);
                if(ctx.typeName().functionTypeName().parameterList()!=null&&ctx.typeName().functionTypeName().parameterList().size()!=0) ((FunctionVariable) v).returnParameterList=functionReturnsParameterListContext2ParameterList(ctx.typeName().functionTypeName().parameterList(1));
                ((FunctionVariable) v).parameterList=parameterContextList2ParameterList(ctx.typeName().functionTypeName().parameterList(0));
            }else{
                v= new PrimaryVariable(ctx.identifier(i).getText(), ctx.typeName().getText(), visible, isConstant, e);//v.initNormal(ctx.typeName().getText());
            }
            stateVariableDeclarationList.add(v);
        }

        return stateVariableDeclarationList;

    }


    //Expression

    private static void getExpression(SolidityParser.ExpressionContext ctx){
        Stack<SolidityParser.ExpressionContext> exp = new Stack<>();
        SolidityParser.ExpressionContext e;
        exp.add(ctx);
        while(!exp.isEmpty()){
            e=exp.pop();
            System.out.println(e.getText());
            Iterator<SolidityParser.ExpressionContext>tmp = e.expression().iterator();
            while(tmp.hasNext()){
                exp.push(tmp.next());
            }
        }
        System.out.println("../../../../");
    }

    private static Expression expressionContext2Expression(SolidityParser.ExpressionContext ctx){
        Expression e=null;
        if(ctx.twoPlusMinusOperator()!=null){
            Expression t = expressionContext2Expression(ctx.expression(0));
            if(ctx.twoPlusMinusOperator().getAltNumber()<ctx.expression(0).getAltNumber()){
                e=new Equality(ctx, t, null);
            }else{
                e=new Equality(ctx,null, t);
            }

        }else if(ctx.equalOperator()!=null||ctx.lvalueOperator()!=null){
            Expression t1 = expressionContext2Expression(ctx.expression(0));
            Expression t2 = expressionContext2Expression(ctx.expression(1));
            e=new Equality(ctx, t1,t2);
        }else if(ctx.arrayRange()!=null){
            if(ctx.arrayRange().colonOperator()!=null){
            }
        }else if (ctx.newDynamicArray()!=null){

        }else if(ctx.environmentalVariable()!=null){

        }else if(ctx.functionCall()!=null||ctx.callArguments()!=null){

        }else if(ctx.lengthOrBalanceStringLiteral()!=null||ctx.identifier()!=null){

        }else if(ctx.tupleExpression()!=null){

        }else if(ctx.typeExpression()!=null){

        }else if(ctx.primaryExpression()!=null){

        }else if(ctx.typeConversion()!=null){

        }else if(ctx.powerOperator()!=null||ctx.muldivOperator()!=null||ctx.plusminusOperator()!=null||ctx.shiftOperator()!=null||ctx.bitOperator()!=null||ctx.conditionalOperator()!=null){

        }else if(ctx.varDeclaration()!=null||ctx.variableDeclaration()!=null){

        }
        return e;
    }

    private static int getExpressionType(SolidityParser.ExpressionContext exp){
        int type = 0;
        if(exp.twoPlusMinusOperator()!=null||exp.equalOperator()!=null||exp.lvalueOperator()!=null){
            return type;
        }else if (exp.arrayRange()!=null){
            type=1;
        }
        return type;
    }

}
