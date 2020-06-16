package utils.FunctionCall;

import utils.FunctionCall.ContractNodeType.SolidityClassDefinition.Contract;
import utils.FunctionCall.ContractNodeType.SolidityClassDefinition.Interface;
import utils.FunctionCall.ContractNodeType.SolidityClassDefinition.Library;

import java.util.ArrayList;
import java.util.List;


public class Content {
    public List<Contract> contractList = new ArrayList<>();
    public List<Library> libraryList = new ArrayList<>();
    public List<Interface> interfaceList = new ArrayList<>();

}
