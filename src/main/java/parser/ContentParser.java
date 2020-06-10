package parser;

import org.antlr.v4.runtime.ParserRuleContext;
import parser.Base.SolidityBaseListener;
import parser.Base.SolidityParser;
import utils.File.FileNode;
import utils.FunctionCall.ContractNode;
import utils.FunctionCall.ContractNodeType.*;

import java.util.ArrayList;
import java.util.List;

public class ContentParser extends SolidityBaseListener {
    FileNode fn;
    ContractNode contractNode = new ContractNode();

    public ContentParser(FileNode fn){
        this.fn=fn;
    }

    private void parseInsideContract(Instance n, ParserRuleContext ctx){
        int count=0;

        //cn.alias=ctx.getChild(SolidityParser.IdentifierContext.class,0).getText();
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
            }else if(tmp.structDefinition()!=null){
                Struct s = new Struct(tmp.structDefinition().identifier().getText());
                tmp.structDefinition().variableDeclaration().forEach(x->{
                    s.addVariableDeclaration(x);
                });
            }else if(tmp.stateVariableDeclaration()!=null){
                StateVariable s;
                if(tmp.stateVariableDeclaration().constantType()!=null){
                    s=new StateVariable(tmp.stateVariableDeclaration().identifier().getText(),tmp.stateVariableDeclaration().typeName().getText(),null, tmp.stateVariableDeclaration().visibleType(0).getText());
                }else{
                    s=new StateVariable(tmp.stateVariableDeclaration().identifier().getText(),tmp.stateVariableDeclaration().typeName().getText(),tmp.stateVariableDeclaration().constantType(0).getText(), tmp.stateVariableDeclaration().visibleType(0).getText());
                }
                n.addStateVariable(s);
            } else if(tmp.modifierDefinition()!=null){
                Modifier m = new Modifier(tmp.modifierDefinition().identifier().getText(),tmp.modifierDefinition().modifierBlock());
                if(tmp.modifierDefinition().parameterList()!=null){
                    m.parameterList = parameterContextList2ParameterList(tmp.modifierDefinition().parameterList());
                }
                n.addModifier(m);
                //cn.modifierList.add(m);


            }else if(tmp.functionDefinition()!=null&&tmp.functionDefinition().functionIdentifier()!=null){  //normal function
                Function f;

                /*
                if(tmp.functionDefinition().functionIdentifier().identifier()!=null){
                    t = cn.addChild(tmp.functionDefinition(),2);
                    t.alias=tmp.functionDefinition().functionIdentifier().identifier().getText();
                }else{  //deprecated default or fallback function after v0.4.2
                    cn.addChild(tmp.functionDefinition(),3);
                }
                Function

                 */
            }else if(tmp.functionDefinition()!=null){   //constructor
                //t = cn.addChild(tmp.functionDefinition(),2);
            }else if(tmp.functionFallBackDefinition()!=null){   //fallback function
                //t = cn.addChild(tmp.functionDefinition(),3);
            }else if(tmp.eventDefinition()!=null){

            }else if(tmp.enumDefinition()!=null){

            }

            count++;
            tmp=ctx.getChild(SolidityParser.ContractPartDefinitionContext.class,count);
        }


    }

    @Override
    public void enterContractDefinition(SolidityParser.ContractDefinitionContext ctx) {
        Contract c = new Contract(ctx.identifier().getText(),ctx);
        contractNode.add(c);
        ctx.inheritanceSpecifier().forEach(x->{
            c.addInheritance(x.getText());
        });

        parseInsideContract(c, ctx);
    }

    @Override
    public void enterLibraryDefinition(SolidityParser.LibraryDefinitionContext ctx) {
        Library l = new Library(ctx.identifier().getText(),ctx);
        contractNode.add(l);

        parseInsideContract(l,ctx);
    }

    @Override
    public void enterInterfaceDefinition(SolidityParser.InterfaceDefinitionContext ctx) {
        Interface in = new Interface(ctx.identifier().getText(),ctx);
        contractNode.add(in);
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

}
