package utils.Content.ContractNodeType.BasicContractDefinition;

import parser.Base.SolidityParser;
import utils.Content.ContractNodeType.BaseNode;
import utils.Content.ContractNodeType.ExpressionDefinition.VariableDeclaration;
import utils.Content.ContractNodeType.Statement.*;

import java.util.ArrayList;
import java.util.List;

public class Statement extends BaseNode{
    public List<VariableDeclaration> variableDeclarationList = new ArrayList<>();
    public List<Expression> expressionList = new ArrayList<>();
    public List<Statement> statementList = new ArrayList<>();
    public SolidityParser.StatementContext ctx;
    public int type;

    public BlockStatement blockStatement;
    public ConditionalStatement conditionalStatement;
    public EmitEventStatement emitEventStatement;
    public ExpressionStatement expressionStatement;
    public InlineAssemblyStatement inlineAssemblyStatement;
    public RequireStatement requireStatement;
    public SingleStatement singleStatement;
    public ThrowRevertStatement throwRevertStatement;
    public TryCatchStatement tryCatchStatement;

    /*
    0: if/while/for/doWhile -> ConditionalStatement //
    1: tryCatch //
    2: inlineAssembly //
    3: Continue/Break/Return/Delete -> SingleStatement //
    4: ThrowRevertStatement //
    5: emitEvent //
    6: Require //
    7: BlockStatement //
    8: ExpressionStatement //
     */
    public Statement( SolidityParser.StatementContext ctx, int type){
        this.ctx=ctx;
        this.type=type;
    }

    public Statement(){}
}
