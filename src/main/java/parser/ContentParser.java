package parser;

import javafx.util.Pair;
import org.antlr.v4.runtime.ParserRuleContext;
import parser.Base.SolidityBaseListener;
import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.FunctionDefinition.FallbackFunction;
import utils.Content.ContractNodeType.FunctionDefinition.Function;
import utils.Content.ContractNodeType.ExpressionDefinition.*;
import utils.Content.ContractNodeType.FunctionDefinition.FunctionInheritance;
import utils.Content.ContractNodeType.StateVariableDeclaration.FunctionVariable;
import utils.Content.ContractNodeType.StateVariableDeclaration.MappingVariable;
import utils.Content.ContractNodeType.StateVariableDeclaration.PrimaryVariable;
import utils.Content.ContractNodeType.StateVariableDeclaration.StateVariableDeclaration;
import utils.Content.ContractNodeType.Statement.*;
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
import java.util.List;

public class ContentParser extends SolidityBaseListener {
    FileNode fn;


/*    Source Parser Part    */
    public ContentParser(FileNode fn){
        this.fn=fn;
    }

    @Override
    public void enterSourceUnit(SolidityParser.SourceUnitContext ctx) {
        Enum e;
        for(int i=0;i<ctx.enumDefinition().size();i++){
            e=new Enum(ctx.enumDefinition(i).identifier().getText(), ctx.enumDefinition(i));
            fn.fileContent.enumList.add(e);
        }

        Struct s;
        for(int i=0;i<ctx.structDefinition().size();i++){
            s = new Struct(ctx.structDefinition(i).identifier().getText(), ctx.structDefinition(i));
            String storage = null;
            for(int j=0;j<ctx.structDefinition(i).variableDeclaration().size();j++){
                if(ctx.structDefinition(i).variableDeclaration(j).storageLocation()!=null) storage = ctx.structDefinition(i).variableDeclaration(j).storageLocation().getText();

                    s.addVariableDeclaration(ctx.structDefinition(i).variableDeclaration(j).typeName().getText(), storage, ctx.structDefinition(i).variableDeclaration(j).identifier().getText(), ctx.structDefinition(i).variableDeclaration(j));
            }
            fn.fileContent.structList.add(s);
        }
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


    @Override
    public void enterContractDefinition(SolidityParser.ContractDefinitionContext ctx) {
        Contract c = new Contract(ctx.identifier().getText(),ctx);
        c.initContract(c);
        fn.fileContent.contractList.add(c);
        ctx.inheritanceSpecifier().forEach(x->{
            c.addInheritance(x);
        });

        parseInsideContract(c, ctx);
    }

    @Override
    public void enterLibraryDefinition(SolidityParser.LibraryDefinitionContext ctx) {
        Library l = new Library(ctx.identifier().getText(),ctx);
        l.initLibrary(l);
        fn.fileContent.libraryList.add(l);

        parseInsideContract(l,ctx);
    }

    @Override
    public void enterInterfaceDefinition(SolidityParser.InterfaceDefinitionContext ctx) {
        Interface in = new Interface(ctx.identifier().getText(),ctx);
        in.initInterface(in);
        fn.fileContent.interfaceList.add(in);
        ctx.inheritanceSpecifier().forEach(x->{
            in.addInheritance(x);
        });
        parseInsideContract(in,ctx);

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
                Struct s = new Struct(tmp.structDefinition().identifier().getText(), tmp.structDefinition());
                String storage = null;
                for(int j=0;j<tmp.structDefinition().variableDeclaration().size();j++){
                    if( tmp.structDefinition().variableDeclaration(j).storageLocation()!=null)storage = tmp.structDefinition().variableDeclaration(j).storageLocation().getText();
                    s.addVariableDeclaration(tmp.structDefinition().variableDeclaration(j).typeName().getText(), storage, tmp.structDefinition().variableDeclaration(j).identifier().getText(), tmp.structDefinition().variableDeclaration(j));
                }
                fn.fileContent.structList.add(s);
                n.addStruct(s);
                System.out.println(s.alias);
            }else if(tmp.stateVariableDeclaration()!=null){
                n.stateVariableDeclarationList = initMultipleStateVariable(tmp.stateVariableDeclaration());
                System.out.println(tmp.stateVariableDeclaration().identifier(0).getText());
            }else if(tmp.modifierDefinition()!=null){
                Modifier m = new Modifier(tmp.modifierDefinition().identifier().getText(),BlockContext2Statement(tmp.modifierDefinition().block()));
                if(tmp.modifierDefinition().parameterList()!=null){
                    m.parameterList = parameterContextList2ParameterList(tmp.modifierDefinition().parameterList());
                }
                n.addModifier(m);
                System.out.println(m.alias);
            }else if(tmp.functionDefinition()!=null&&tmp.functionDefinition().functionIdentifier()!=null){  //normal function
                Function function;
                String stateMutability = null;
                if(tmp.functionDefinition().stateMutability().size()!=0){
                    stateMutability = tmp.functionDefinition().stateMutability(0).getText();
                }
                function=new Function(tmp.functionDefinition().functionIdentifier().identifier().getText(),BlockContext2Statement(tmp.functionDefinition().block()),stateMutability,tmp.functionDefinition().visibleType(0).getText());

                function.parameterList=parameterContextList2ParameterList(tmp.functionDefinition().parameterList());
                if(tmp.functionDefinition().identifier().size()!=0) function.modifierList=functionIdentifier2ModifierList(tmp.functionDefinition());
                if(tmp.functionDefinition().inheritance()!=null&&tmp.functionDefinition().inheritance().size()!=0){
                    InheritanceList2FunctionInheritanceList(tmp, function);
                }

                if(tmp.functionDefinition().expression()!=null) function.modifierWithParameterList=functionExpression2ModifierList(tmp.functionDefinition());
                if(tmp.functionDefinition().returnsParameters()!=null)function.returnParameterList=functionReturnsParameterListContext2ParameterList(tmp.functionDefinition().returnsParameters().parameterList());
                n.addFunction(function);
                System.out.println(function.alias);

            }else if(tmp.functionDefinition()!=null){   //constructor

                Function f;
                String stateMutability=null;
                Block b = null;
                String visibleType = null;
                if(tmp.functionDefinition().visibleType()!=null&&tmp.functionDefinition().visibleType().size()!=0){
                    visibleType=tmp.functionDefinition().visibleType(0).getText();
                }
                if(tmp.functionDefinition().stateMutability()!=null&&tmp.functionDefinition().stateMutability().size()!=0){
                    stateMutability=tmp.functionDefinition().getText();
                }
                if(tmp.functionDefinition().block()!=null) b = BlockContext2Statement(tmp.functionDefinition().block());

                f=new Function("constructor",b,stateMutability,visibleType);
                SolidityParser.ParameterListContext parameterListContext = tmp.functionDefinition().parameterList();
                f.parameterList=parameterContextList2ParameterList(parameterListContext);

                if(tmp.functionDefinition().identifier()!=null) f.modifierList=functionIdentifier2ModifierList(tmp.functionDefinition());
                if(tmp.functionDefinition().inheritance()!=null&&tmp.functionDefinition().inheritance().size()!=0){
                    InheritanceList2FunctionInheritanceList(tmp, f);
                }
                if(tmp.functionDefinition().expression()!=null) f.modifierWithParameterList=functionExpression2ModifierList(tmp.functionDefinition());  //derived constructor as modifier

                n.addFunction(f);
                System.out.println("constructor");
            }else if(tmp.functionFallBackDefinition()!=null){   //fallback function
                FallbackFunction f;
                String stateMutability=null;
                Block b = null;

                if(tmp.functionFallBackDefinition().stateMutability()!=null&&tmp.functionFallBackDefinition().stateMutability().size()!=0){
                    stateMutability=tmp.functionFallBackDefinition().stateMutability(0).getText();
                }

                if(tmp.functionFallBackDefinition().block()!=null){
                    b=BlockContext2Statement(tmp.functionFallBackDefinition().block());
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
                Enum e = new Enum(tmp.enumDefinition().identifier().getText(), tmp.enumDefinition());
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

    private static void InheritanceList2FunctionInheritanceList(SolidityParser.ContractPartDefinitionContext tmp, Function function) {
        for(int i=0;i<tmp.functionDefinition().inheritance().size();i++){
            for(int j=0;j<tmp.functionDefinition().inheritance(i).userDefinedTypeName().size();j++){
                function.functionInheritanceList.add(userDefinedTypeName2FunctionInheritance(tmp.functionDefinition().inheritance(i).userDefinedTypeName(j)));
            }
        }
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
        if(ctx.expression()!=null)e=expressionContext2Expression(ctx.expression()).getValue();

        for(int i=0;i<ctx.identifier().size();i++){
            if(ctx.typeName().mappingSt()!=null){
                v=new MappingVariable(ctx.identifier(i).getText(),ctx.typeName().mappingSt().typeName(0).getText(), ctx.typeName().mappingSt().typeName(1).getText(),visible, isConstant,e);
            }else if(ctx.typeName().functionTypeName()!=null){
                SolidityParser.FunctionTypeNameContext t = ctx.typeName().functionTypeName();
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

    private static Block BlockContext2Statement(SolidityParser.BlockContext ctx){
        Block b = new Block();
        for(int i=0;i<ctx.statement().size();i++){
            b.statementList.add(StatementContext2Statement(ctx.statement(i)));
        }
        return b;
    }

    private static Statement StatementContext2Statement(SolidityParser.StatementContext ctx){
        Statement s=null;
        if(ctx.block()!=null){
            s = new BlockStatement(ctx);
            for(int i=0;i<ctx.block().statement().size();i++){
                s.statementList.add(StatementContext2Statement(ctx.block().statement(i)));
            }
        }else if(ctx.ifStatement()!=null){
            s=new ConditionalStatement(expressionContext2Expression(ctx.ifStatement().condition().expression()).getValue(), ctx);
            for(int i=0;i<ctx.ifStatement().statement().size();i++){
                s.statementList.add(StatementContext2Statement(ctx.ifStatement().statement(i)));
            }
        }else if(ctx.whileStatement()!=null){
            Expression e = expressionContext2Expression(ctx.whileStatement().condition().expression()).getValue();
            s=new ConditionalStatement(e, ctx);
            s.statementList.add(StatementContext2Statement(ctx.whileStatement().statement()));
        }else if(ctx.forStatement()!=null){
            Expression e = null;
            if(ctx.forStatement().condition()!=null) e = expressionContext2Expression(ctx.forStatement().condition().expression()).getValue() ;
            s = new ConditionalStatement(e, ctx);
            if(ctx.forStatement().expression()!=null&&ctx.forStatement().expression().size()!=0){
                for(int i=0;i<ctx.forStatement().expression().size();i++){
                    s.expressionList.add(expressionContext2Expression(ctx.forStatement().expression(i)).getValue());
                }
            }
            s.statementList.add(StatementContext2Statement(ctx.forStatement().statement()));
        }else if(ctx.doWhileStatement()!=null){
           s = new ConditionalStatement(expressionContext2Expression(ctx.doWhileStatement().condition().expression()).getValue(), ctx);
        }else if(ctx.tryCatchStatement()!=null){
            s = new TryCatchStatement(expressionContext2Expression(ctx.tryCatchStatement().expression()).getValue(),BlockContext2Statement(ctx.tryCatchStatement().block(0)), BlockContext2Statement(ctx.tryCatchStatement().block(1)),ctx);
            if(ctx.tryCatchStatement().returnsParameters()!=null){
                ((TryCatchStatement)s).returnParameterList = parameterContextList2ParameterList(ctx.tryCatchStatement().returnsParameters().parameterList());
            }
            if(ctx.tryCatchStatement().catchStatement()!=null&&ctx.tryCatchStatement().catchStatement().size()!=0){
                Pair<String, List<Parameter>> catchParameter;
                List<Parameter> parameterList;
                String alias = "nil";
                for(int i=0;i<ctx.tryCatchStatement().catchStatement().size();i++){
                    if(ctx.tryCatchStatement().catchStatement(i).identifier()!=null) alias = ctx.tryCatchStatement().catchStatement(i).identifier().getText();
                    parameterList = parameterContextList2ParameterList(ctx.tryCatchStatement().catchStatement(i).parameterList());
                    catchParameter = new Pair(alias, parameterList);
                    ((TryCatchStatement) s).catchParameterList.add(catchParameter);
                    alias = "nil";
                }
            }
        }else if(ctx.inlineAssemblyStatement()!=null){
            s=new InlineAssemblyStatement(ctx);
        }else if(ctx.placeholderStatement()!=null ||ctx.continueStatement()!=null||ctx.breakStatement()!=null||ctx.returnStatement()!=null||ctx.deleteStatement()!=null){
            s = new SingleStatement(ctx);
        }else if(ctx.throwRevertStatement()!=null){
            s = new ThrowRevertStatement(ctx);
        }else if(ctx.emitEventStatement()!=null){
            s = new EmitEventStatement(ctx);
            ((EmitEventStatement) s).nameValueList = callArgument2NameValueList(ctx.emitEventStatement().callArguments());
            if(ctx.emitEventStatement().identifier().size()==1){
                ((EmitEventStatement) s).alias = new Pair(ctx.emitEventStatement().identifier(0).getText(), null);
            }else ((EmitEventStatement) s).alias = new Pair(ctx.emitEventStatement().identifier(0).getText(), ctx.emitEventStatement().identifier(1).getText());
        }else if(ctx.requireStatement()!=null){
            s = new RequireStatement(expressionContext2Expression(ctx.requireStatement().expression()).getValue(),ctx);
            if(ctx.requireStatement().stringLiteral()!=null) ((RequireStatement) s).msg = ctx.requireStatement().stringLiteral().getText();
        }else if(ctx.expressionStatement()!=null){
            s=new ExpressionStatement(expressionContext2Expression(ctx.expressionStatement().expression()).getValue(),ctx);
        }
        return s;
    }

    private static Pair<Integer, Expression> expressionContext2Expression(SolidityParser.ExpressionContext ctx){
        Pair<Integer, Expression> result=null;
        System.out.println(ctx.getText());
        Expression e=null;

        if(ctx.twoPlusMinusOperator()!=null){
            Expression t = expressionContext2Expression(ctx.expression(0)).getValue();
            if(ctx.twoPlusMinusOperator().getAltNumber()<ctx.expression(0).getAltNumber()){
                e=new Equality(ctx, t, null);
            }else{
                e=new Equality(ctx,null, t);
            }
            e.initEquality((Equality) e);
            result = new Pair<Integer, Expression>(0,e);

        }else if(ctx.equalOperator()!=null||ctx.lvalueOperator()!=null){
            Expression t1 = expressionContext2Expression(ctx.expression(0)).getValue();
            Expression t2 = expressionContext2Expression(ctx.expression(1)).getValue();
            e=new Equality(ctx, t1,t2);
            e.initEquality((Equality) e);
            result = new Pair<Integer, Expression>(0,e);
        }else if(ctx.arrayRange()!=null){
            if(ctx.arrayRange().colonOperator()!=null){
                Expression t1 = null;
                Expression t2 = null;
                if(ctx.arrayRange().expression().size()==1){
                    if(ctx.arrayRange().expression(0).getAltNumber()<ctx.arrayRange().colonOperator().getAltNumber()){
                        t1 = expressionContext2Expression(ctx.arrayRange().expression(0)).getValue();
                        e = new StaticArray(true,t1, t2, expressionContext2Expression(ctx.expression(0)).getValue(), ctx);
                    }else{
                        t2 = expressionContext2Expression(ctx.arrayRange().expression(0)).getValue();
                        e = new StaticArray(true,t1, t2, expressionContext2Expression(ctx.expression(0)).getValue(), ctx);
                    }
                }else{
                    t1=expressionContext2Expression(ctx.arrayRange().expression(0)).getValue();
                    t2 = expressionContext2Expression(ctx.arrayRange().expression(1)).getValue();
                    e = new StaticArray(true,t1, t2, expressionContext2Expression(ctx.expression(0)).getValue(), ctx);
                }
            }else{
                e = new StaticArray(false, null, null, expressionContext2Expression(ctx.expression(0)).getValue(), ctx);
            }
            e.initStaticArray((StaticArray) e);
            result = new Pair<Integer, Expression>(1,e);

        }else if (ctx.newDynamicArray()!=null){
            if(ctx.newDynamicArray().dynamicType()!=null){
                e = new NewDynamicArray(ctx.newDynamicArray().dynamicType().getText(), true, ctx);
            }else{
                e= new NewDynamicArray(ctx.newDynamicArray().typeName().getText(), false, ctx);
            }
            result = new Pair<Integer, Expression>(2,e);
            e.initDynamicArray((NewDynamicArray) e);
        }else if(ctx.environmentalVariable()!=null){
            if(ctx.environmentalVariable().expression()!=null){
                e = new EnvironmentalVariable(ctx, expressionContext2Expression(ctx.environmentalVariable().expression()).getValue());
            }else e = new EnvironmentalVariable(ctx, null);
            result = new Pair<Integer, Expression>(3,e);
            e.initEnvironmentalVariable((EnvironmentalVariable)e);
        }else if(ctx.callArguments()!=null){
            e = new FunctionCall(ctx.callArguments().getText(), expressionContext2Expression(ctx.expression(0)).getValue(),ctx);
            if(ctx.callArguments().tupleExpression()!=null){
                e.expressionList=tupleExpression2ExpressionList(ctx.callArguments().tupleExpression());
            }else{
                ((FunctionCall) e).nameValueList=callArgument2NameValueList(ctx.callArguments());
            }
            result = new Pair<Integer, Expression>(4,e);
            e.initFunctionCall((FunctionCall) e);
        }else if(ctx.functionCall()!=null){
            e = new FunctionCall(ctx.functionCall().functionName().getText(), expressionContext2Expression(ctx.expression(0)).getValue(),ctx);
            if(ctx.functionCall().callArguments().tupleExpression()!=null){
                Expression expression = new TupleExpression(ctx);
                e.expressionList.add(expression);
            }else{
                ((FunctionCall) e).nameValueList=callArgument2NameValueList(ctx.callArguments());
            }
            result = new Pair<Integer, Expression>(4,e);
            e.initFunctionCall((FunctionCall) e);
        }else if(ctx.lengthOrBalanceStringLiteral()!=null){
            e = new FunctionIdentifier(ctx.lengthOrBalanceStringLiteral().getText(), ctx, expressionContext2Expression(ctx.expression(0)).getValue());
            result = new Pair(5,e);
        }else if(ctx.identifier()!=null){
            e = new FunctionIdentifier(ctx.identifier().getText(), ctx, expressionContext2Expression(ctx.expression(0)).getValue());
            result = new Pair(5,e);
            e.initFunctionIdentifier((FunctionIdentifier) e);
        }else if(ctx.tupleExpression()!=null){
            e = new TupleExpression(ctx);
            for(int i=0;i<ctx.tupleExpression().expression().size();i++){
                e.expressionList.add(expressionContext2Expression(ctx.tupleExpression().expression(i)).getValue());
            }
            result = new Pair<Integer, Expression>(5,e);
            e.initTupleExpression((TupleExpression) e);
        }else if(ctx.typeExpression()!=null){
            e = new TypeExpression(expressionContext2Expression(ctx.typeExpression().expression()).getValue(), ctx);
            result = new Pair<Integer, Expression>(7,e);
            e.initTypeExpression((TypeExpression) e);
        }else if(ctx.primaryExpression()!=null){
            e = new PrimaryExpression(ctx);
            result = new Pair<Integer, Expression>(8,e);
            e.initPrimaryExpression((PrimaryExpression) e);
        }else if(ctx.typeConversion()!=null){
            e = new TypeConversion(ctx.typeConversion().expression().getText(), ctx);
            result = new Pair<Integer, Expression>(9,e);
            e.initTypeConversion((TypeConversion) e);
        }else if(ctx.powerOperator()!=null){
            e = new OperatorExpression(ctx.powerOperator().getText(), expressionContext2Expression(ctx.expression(0)).getValue(), expressionContext2Expression(ctx.expression(1)).getValue(), ctx);
            result = new Pair<Integer, Expression>(10,e);
            e.initOperatorExpression((OperatorExpression) e);
        }else if(ctx.bitOperator()!=null){
            e = new OperatorExpression(ctx.bitOperator().getText(), expressionContext2Expression(ctx.expression(0)).getValue(), expressionContext2Expression(ctx.expression(1)).getValue(), ctx);
            result = new Pair<Integer, Expression>(10,e);
            e.initOperatorExpression((OperatorExpression) e);
        }else if(ctx.conditionalOperator()!=null){
            e = new OperatorExpression(ctx.conditionalOperator().getText(), expressionContext2Expression(ctx.expression(0)).getValue(), expressionContext2Expression(ctx.expression(1)).getValue(), ctx);
            result = new Pair<Integer, Expression>(10,e);
            e.initOperatorExpression((OperatorExpression) e);
        }else if(ctx.shiftOperator()!=null){
            e = new OperatorExpression(ctx.shiftOperator().getText(), expressionContext2Expression(ctx.expression(0)).getValue(), expressionContext2Expression(ctx.expression(1)).getValue(), ctx);
            result = new Pair<Integer, Expression>(10,e);
            e.initOperatorExpression((OperatorExpression) e);
        }else if(ctx.plusminusOperator()!=null){
            e = new OperatorExpression(ctx.plusminusOperator().getText(), expressionContext2Expression(ctx.expression(0)).getValue(), expressionContext2Expression(ctx.expression(1)).getValue(), ctx);
            result = new Pair<Integer, Expression>(10,e);
            e.initOperatorExpression((OperatorExpression) e);
        }else if(ctx.muldivOperator()!=null){
            e = new OperatorExpression(ctx.muldivOperator().getText(), expressionContext2Expression(ctx.expression(0)).getValue(), expressionContext2Expression(ctx.expression(1)).getValue(), ctx);
            result = new Pair<Integer, Expression>(10,e);
            e.initOperatorExpression((OperatorExpression) e);
        }else if(ctx.varDeclaration()!=null){
            e = new VarDeclaration(expressionContext2Expression(ctx.varDeclaration().expression()).getValue(),ctx);
            result = new Pair<Integer, Expression>(12,e);
            e.initVarDeclaration((VarDeclaration) e);
        }else if(ctx.variableDeclaration()!=null){
            String storageLocation = null;
            String alias = null;
            if(ctx.variableDeclaration().identifier()!=null){
                alias = ctx.variableDeclaration().identifier().getText();
                if(ctx.variableDeclaration().storageLocation()!=null){
                    storageLocation = ctx.variableDeclaration().storageLocation().getText();
                }
            }
            e=new VariableDeclaration(ctx.variableDeclaration().typeName().getText(), storageLocation, alias, ctx);
            result = new Pair<Integer, Expression>(11,e);
            e.initVariableDeclaration((VariableDeclaration) e);
        }else if(ctx.tupleExpression()!=null){
            e=new TupleExpression(ctx);
            e.expressionList = tupleExpression2ExpressionList(ctx.tupleExpression());
            result = new Pair<>(6, e);
            e.initTupleExpression((TupleExpression) e);
        }else if(ctx.conditionalExpression()!=null){
            e = new ConditionalExpression(ctx);
            e.expressionList.add(expressionContext2Expression(ctx.conditionalExpression().expression()).getValue());
            result = new Pair<>(14, e);
            e.initConditionalExpression((ConditionalExpression) e);
        }else if(ctx.conditionalStatementShortFormOperator()!=null&&ctx.conditionalStatementShortFormOperator().size()!=0){
            e = new ConditionalDeclaration(expressionContext2Expression(ctx.expression(0)).getValue(),expressionContext2Expression(ctx.expression(1)).getValue(),expressionContext2Expression(ctx.expression(2)).getValue(),ctx);
            result = new Pair<>(13,e);
            e.initConditionalDeclaration((ConditionalDeclaration) e);
        }
        return result;
    }

    private static FunctionInheritance userDefinedTypeName2FunctionInheritance(SolidityParser.UserDefinedTypeNameContext ctx){
        FunctionInheritance f = new FunctionInheritance();
        for(int i =0; i< ctx.identifier().size();i++){
            f.alias.add(ctx.identifier(i).getText());
        }
        return f;
    }

    private static List<Pair<String, Expression>> callArgument2NameValueList(SolidityParser.CallArgumentsContext ctx){
        List<Pair<String, Expression>>l = new ArrayList<>();
        Pair<String, Expression> p;
        for(int i=0;i<ctx.nameValueList().expression().size();i++){
            p = new Pair<String, Expression>(ctx.nameValueList().identifier(i).getText(), expressionContext2Expression(ctx.nameValueList().expression(i)).getValue());
            l.add(p);
        }
        return l;
    }

    private static List<Expression> tupleExpression2ExpressionList(SolidityParser.TupleExpressionContext ctx){
        List<Expression>l = new ArrayList<>();
        Expression e;
        for(int i=0;i<ctx.expression().size();i++){
            e=expressionContext2Expression(ctx.expression(i)).getValue();
            l.add(e);
        }
        return l;
    }

}
