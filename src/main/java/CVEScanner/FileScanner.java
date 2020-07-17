package CVEScanner;

import CVEScanner.CVE.*;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import config.FileDirectory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;
import utils.File.FileNode;
import utils.File.FileTree;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileScanner {
    public static FileTree ft;
    public static List<BaseCVE> cveList = new ArrayList<>();

    public FileScanner(FileTree ft){
        this.ft = ft;
        Reentrancy reentrancy = new Reentrancy(ft.root);
        cveList.add(reentrancy);
        CallStackDepth callStackDepth = new CallStackDepth(ft.root);
        cveList.add(callStackDepth);
        TXOrigin txOrigin = new TXOrigin(ft.root);
        cveList.add(txOrigin);
        DynamicNumber dynamicNumber = new DynamicNumber(ft.root);
        cveList.add(dynamicNumber);
        InlineAssembly inlineAssembly = new InlineAssembly(ft.root);
        cveList.add(inlineAssembly);
        initScanning();
    }

    public static void mapScanningModule(String index, String enabled){
        int idx = Integer.parseInt(index);
        boolean isEnabled = Integer.parseInt(enabled) != 0;
        switch(idx){
            case 1:
                cveList.get(0).enabled=isEnabled;
                break;
            case 2:
                cveList.get(1).enabled=isEnabled;
                break;
            case 3:
                cveList.get(2).enabled=isEnabled;
                break;
            case 4:
                cveList.get(3).enabled=isEnabled;
                break;
            case 5:
                cveList.get(4).enabled=isEnabled;
                break;
        }
    }

    public static void initScanning(){
        try{
            File scanning_config = new File(FileDirectory.scanning_config_root.toPath().resolve("scanning.xml").toString());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(scanning_config);
            doc.getDocumentElement().normalize();
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("cve");
            for(int i=0;i<nodeList.getLength();i++){
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;
                    mapScanningModule(eElement.getElementsByTagName("index").item(0).getTextContent(),eElement.getElementsByTagName("enabled").item(0).getTextContent());
                }
            }
        }catch(Exception e){
            for(int i=0;i<cveList.size();i++){
                cveList.get(i).enabled=true;
            }
        }
    }

    public static void scan() throws FileNotFoundException {
        List<String> errorMsg = new ArrayList<>();
        for(int i=0;i<cveList.size();i++){
            if(cveList.get(i).enabled){
                reset();
                List<String> err = cveList.get(i).scan();
                if(err!=null&&err.size()!=0) errorMsg.addAll(err);
            }
        }
        generateCVEReport(errorMsg);
    }


    public static void generateCVEReport(List<String> errorMsg) throws FileNotFoundException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Path cveReportPath = Paths.get(FileDirectory.cve_report_root.getPath()).resolve(timeStamp+"_cveReport.pdf");
        PdfDocument pdf = new PdfDocument(new PdfWriter(cveReportPath.toString()));
        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf);
        if(errorMsg.size()==0) document.add(new Paragraph("No CVE has been found, great!"));
        for(int i=0;i<errorMsg.size();i++){
            document.add(new Paragraph(errorMsg.get(i)));
        }
        document.close();

    }

    public static int FileMatchLine(String txt,FileNode fn) throws Exception{
        Path p= FileDirectory.tmp_root.toPath().resolve(fn.path);
        int count = 1;
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(p.toString()));
        String line;
        while((line=lineNumberReader.readLine())!=null){
            line=line.replaceAll(" ","");
            if(line.contains(txt)) {
                break;
            }
            count++;
        }
        if (lineNumberReader != null){
            lineNumberReader.close();
        }
        return count;
    }

    private static void resetInstanceFunctionModifier(Instance in){
        for(int i=0;i<in.functionList.size();i++){
            in.functionList.get(i).visited=false;
        }

        for(int i=0;i<in.modifierList.size();i++){
            in.modifierList.get(i).visited=false;
        }
    }

    public static void reset(){
        Stack<FileNode> s=new Stack<FileNode>();
        s.push(ft.root);
        FileNode fn;
        File tf;
        while(!s.isEmpty()){
            fn=s.pop();
            if(fn.path!=null){
                tf=new File(fn.path);
                if(!tf.isDirectory()){
                    for(int i=0;i<fn.fileContent.contractList.size();i++){
                        resetInstanceFunctionModifier(fn.fileContent.contractList.get(i));
                    }
                    for(int i=0;i<fn.fileContent.libraryList.size();i++){
                        resetInstanceFunctionModifier(fn.fileContent.libraryList.get(i));
                    }
                }
            }
            Iterator<FileNode> f = fn.children.iterator();
            while(f.hasNext()){
                s.push(f.next());
            }
        }
    }


}
