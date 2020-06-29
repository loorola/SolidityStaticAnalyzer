package utils.Content;

import javafx.util.Pair;
import utils.Content.ContractNodeType.BasicContractDefinition.Enum;
import utils.Content.ContractNodeType.BasicContractDefinition.Struct;
import utils.Content.ContractNodeType.SolidityClassDefinition.Contract;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;
import utils.Content.ContractNodeType.SolidityClassDefinition.Interface;
import utils.Content.ContractNodeType.SolidityClassDefinition.Library;

import java.util.ArrayList;
import java.util.List;


public class FileContent {
    public List<Contract> contractList = new ArrayList<>();
    public List<Library> libraryList = new ArrayList<>();
    public List<Interface> interfaceList = new ArrayList<>();
    public List<Struct> structList = new ArrayList<>();
    public List<Enum> enumList = new ArrayList<utils.Content.ContractNodeType.BasicContractDefinition.Enum>();
}
