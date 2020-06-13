package utils.FunctionCall;

import utils.FunctionCall.ContractNodeType.BaseNode;

import java.util.ArrayList;
import java.util.List;


public class ContentNode {
    List<BaseNode> nodeList = new ArrayList<>();

    public void add(BaseNode n){
        nodeList.add(n);
    }
}
