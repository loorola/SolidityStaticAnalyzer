package utils.Content.ContractNodeType.BasicContractDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BaseNode;

import java.util.ArrayList;
import java.util.List;

public class Statement {
    public List<Expression> expressionList = new ArrayList<>();
    public List<Statement> statementList = new ArrayList<>();
    public SolidityParser.StatementContext ctx;
    public int type;

    /*
    0: if/while/for/doWhile -> ConditionalStatement //
    1: tryCatch /
    2: inlineAssembly /
    3: Continue/Break/Return/Delete -> SingleStatement /
    4: ThrowRevertStatement /
    5: emitEvent /
    6: Require /
    7: FunctionBlock /
    8: ConditionalBlock
     */
    public Statement(SolidityParser.StatementContext ctx, int type){
        this.ctx=ctx;
        this.type=type;
    }

    public Statement(){}
}
