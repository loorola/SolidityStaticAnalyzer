package CVEScanner.CVE;


import CVEScanner.BaseCVE;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;
import utils.File.FileNode;

public class Reentrancy extends BaseCVE {
    public Reentrancy(){
        name = "Reentrancy";
        description = "This exploit was missed in review so many times by so many different people: reviewers tend to review functions one at a time, and assume that calls to secure subroutines will operate securely and as intended.";
    }

    public static void scan(FileNode fn, Instance instance){

    }
}
