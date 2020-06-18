package utils.Content;

import utils.Content.ContractNodeType.SolidityClassDefinition.Contract;
import utils.Content.ContractNodeType.SolidityClassDefinition.Interface;
import utils.Content.ContractNodeType.SolidityClassDefinition.Library;

import java.util.ArrayList;
import java.util.List;


public class FileContent {
    public List<Contract> contractList = new ArrayList<>();
    public List<Library> libraryList = new ArrayList<>();
    public List<Interface> interfaceList = new ArrayList<>();

}
