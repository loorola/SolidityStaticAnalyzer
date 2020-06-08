package parser;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import parser.Base.SolidityBaseListener;
import parser.Base.SolidityParser;
import utils.Call.CallNode;
import utils.Call.CallTree;

public class FunctionPreparser extends SolidityBaseListener {
    public CallTree ct;

    public FunctionPreparser(){
        ct=new CallTree();
    }

    private void parseInsideContract(ParserRuleContext ctx, boolean isLibrary){
        CallNode cn;
        int count=0;
        if(isLibrary){
            cn = ct.addCallNode(ct.root,ctx,4);
        }else{
            cn = ct.addCallNode(ct.root,ctx,0);
        }

        cn.alias=ctx.getChild(SolidityParser.IdentifierContext.class,0).getText();
        SolidityParser.ContractPartDefinitionContext tmp = ctx.getChild(SolidityParser.ContractPartDefinitionContext.class,count);
        while(tmp!=null){
            if(tmp.modifierDefinition()!=null){
                CallNode t = cn.addChild(tmp.modifierDefinition(),0);
                t.alias=tmp.modifierDefinition().identifier().getText();
            }else if(tmp.functionDefinition()!=null){
                CallNode t = cn.addChild(tmp.functionDefinition(),1);
                t.alias=tmp.functionDefinition().identifier(0).getText();
            }else if(tmp.functionFallBackDefinition()!=null){
                
            }
        }
    }

    @Override
    public void enterContractDefinition(SolidityParser.ContractDefinitionContext ctx) {
        parseInsideContract(ctx,false);
        CallNode cn = ct.addCallNode(ct.root,ctx,0);
        cn.alias=ctx.identifier().getText();
        //System.out.println(cn.alias);
        ctx.contractPartDefinition().forEach(x->{
            if(x.modifierDefinition()!=null){
                CallNode t = cn.addChild(x.modifierDefinition(),0);
                t.alias=x.modifierDefinition().identifier().getText();
            }else if(x.functionDefinition()!=null){
                CallNode t = cn.addChild(x.functionDefinition(),1);


            }else if(x.functionFallBackDefinition()!=null){

            }
        });
    }

    @Override
    public void enterLibraryDefinition(SolidityParser.LibraryDefinitionContext ctx) {
        parseInsideContract(ctx,true);

        CallNode cn = ct.addCallNode(ct.root,ctx,4);
        //cn.alias=ctx.identifier().getText();
        ctx.contractPartDefinition().forEach(x->{
            if(x.modifierDefinition()!=null){
                CallNode t = ct.addCallNode(cn,x.modifierDefinition(),3);
                t.alias=x.modifierDefinition().identifier().getText();
            }else if(x.functionDefinition()!=null){
                CallNode t = cn.addChild(x.functionDefinition(),1);

            }else if(x.functionFallBackDefinition()!=null){
            }
        });
    }
}
