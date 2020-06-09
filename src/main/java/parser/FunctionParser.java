package parser;

import org.antlr.v4.runtime.ParserRuleContext;
import parser.Base.SolidityBaseListener;
import parser.Base.SolidityParser;
import utils.functionCall.FunctionCallNode;
import utils.functionCall.FunctionCallTree;

public class FunctionParser extends SolidityBaseListener {
    public FunctionCallTree ct;

    public FunctionParser(){
        ct=new FunctionCallTree();
    }

    private void parseInsideContract(ParserRuleContext ctx, boolean isLibrary){
        FunctionCallNode cn,t;
        int count=0;
        if(isLibrary){
            cn = ct.root.addChild(ctx,1);
        }else{
            cn = ct.root.addChild(ctx,0);
        }

        cn.alias=ctx.getChild(SolidityParser.IdentifierContext.class,0).getText();
        SolidityParser.ContractPartDefinitionContext tmp = ctx.getChild(SolidityParser.ContractPartDefinitionContext.class,count);


        while(tmp!=null){
            if(tmp.usingForDeclaration()!=null){    //using for keyword
                t=cn.addChild(tmp.usingForDeclaration(),3);
            }else if(tmp.modifierDefinition()!=null){
                t = cn.addChild(tmp.modifierDefinition(),6);
                t.alias=tmp.modifierDefinition().identifier().getText();
            }else if(tmp.functionDefinition()!=null&&tmp.functionDefinition().functionIdentifier()!=null){  //normal function

                if(tmp.functionDefinition().functionIdentifier().identifier()!=null){
                    t = cn.addChild(tmp.functionDefinition(),2);
                    t.alias=tmp.functionDefinition().functionIdentifier().identifier().getText();
                }else{  //deprecated default or fallback function after v0.4.2
                    t=cn.addChild(tmp.functionDefinition(),4);
                }
            }else if(tmp.functionDefinition()!=null){   //constructor
                t = cn.addChild(tmp.functionDefinition(),2);
            }else if(tmp.functionFallBackDefinition()!=null){   //fallback function
                t = cn.addChild(tmp.functionDefinition(),4);
            }

            count++;
            tmp=ctx.getChild(SolidityParser.ContractPartDefinitionContext.class,count);
        }


    }

    @Override
    public void enterContractDefinition(SolidityParser.ContractDefinitionContext ctx) {
        parseInsideContract(ctx,false);
    }

    @Override
    public void enterLibraryDefinition(SolidityParser.LibraryDefinitionContext ctx) {
        parseInsideContract(ctx,true);
    }
}
