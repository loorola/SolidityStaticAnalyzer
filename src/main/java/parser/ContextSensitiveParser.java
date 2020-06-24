package parser;

import parser.Base.SolidityBaseListener;
import parser.Base.SolidityParser;
import utils.Context.VariableDataType.InstanceVariableDataType;
import utils.File.FileNode;

public class ContextSensitiveParser extends SolidityBaseListener{
    FileNode fn;

    public ContextSensitiveParser(FileNode fn){
        this.fn=fn;
    }

    public void resolveVariableTypeInSameContract(){
    }

    public void resolveVariableTypeInOtherContract(){
    }


}
