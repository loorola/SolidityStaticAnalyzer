package parser;

import org.antlr.v4.runtime.ParserRuleContext;
import parser.Base.SolidityBaseListener;
import parser.Base.SolidityParser;
import utils.File.FileNode;
import utils.FunctionCall.ContentNode;
import utils.FunctionCall.ContractNodeType.*;
import utils.FunctionCall.ContractNodeType.Enum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContentParser extends SolidityBaseListener {
    FileNode fn;
    ContentNode contentNode = new ContentNode();

    public ContentParser(FileNode fn){
        this.fn=fn;
    }

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
                n.stateVariableList = Stream.concat(n.stateVariableList.stream(),initMultipleStateVariable(tmp.stateVariableDeclaration()).stream()).collect(Collectors.toList());
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
        contentNode.add(c);
        ctx.inheritanceSpecifier().forEach(x->{
            c.addInheritance(x.getText());
        });

        parseInsideContract(c, ctx);
    }

    @Override
    public void enterLibraryDefinition(SolidityParser.LibraryDefinitionContext ctx) {
        Library l = new Library(ctx.identifier().getText(),ctx);
        contentNode.add(l);

        parseInsideContract(l,ctx);
    }

    @Override
    public void enterInterfaceDefinition(SolidityParser.InterfaceDefinitionContext ctx) {
        Interface in = new Interface(ctx.identifier().getText(),ctx);
        contentNode.add(in);
        ctx.inheritanceSpecifier().forEach(x->{
            in.addInheritance(x.getText());
        });

    }

    private static List<Parameter> parameterContextList2ParameterList( SolidityParser.ParameterListContext ctx){
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


    private static List<StateVariable> initMultipleStateVariable(SolidityParser.StateVariableDeclarationContext ctx){
        List<StateVariable>stateVariableList= new ArrayList<>();
        StateVariable v;

        String visible=null;
        if(ctx.visibleType()!=null&&ctx.visibleType().size()!=0){
            visible=ctx.visibleType(0).getText();
        }

        boolean isConstant=false;
        if(ctx.constantType()!=null&&ctx.constantType().size()!=0){
            isConstant=true;
        }

        SolidityParser.ExpressionContext e = null;
        if(ctx.expression()!=null){
            e=ctx.expression();
        }

        for(int i=0;i<ctx.identifier().size();i++){
            v=new StateVariable(ctx.identifier(i).getText(),visible,isConstant,e);
            if(ctx.typeName().mappingSt()!=null){
                v.initMapping(ctx.typeName().mappingSt().typeName(0).getText(), ctx.typeName().mappingSt().typeName(1).getText());
            }else if(ctx.typeName().functionTypeName()!=null){
                SolidityParser.FunctionTypeNameContext t = ctx.typeName().functionTypeName();
                String vt = null;
                String stateMutability = null;
                if(t.visibleType()!=null&&t.visibleType().size()!=0){
                    vt=t.visibleType(0).getText();
                }
                if(t.stateMutability()!=null&&t.stateMutability().size()!=0){
                    stateMutability = t.stateMutability(0).getText();
                }
                List<SolidityParser.ParameterListContext> parameterListContexts = ctx.typeName().functionTypeName().parameterList();
                v.initFunction(stateMutability,vt);
                if(ctx.typeName().functionTypeName().parameterList()!=null&&ctx.typeName().functionTypeName().parameterList().size()!=0) v.f.returnParameterList=functionReturnsParameterListContext2ParameterList(ctx.typeName().functionTypeName().parameterList(1));
                v.f.parameterList=parameterContextList2ParameterList(ctx.typeName().functionTypeName().parameterList(0));
            }else{
                v.initNormal(ctx.typeName().getText());
            }
            stateVariableList.add(v);
        }
        return stateVariableList;

    }

}
