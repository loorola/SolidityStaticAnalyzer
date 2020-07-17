import CVEScanner.FileScanner;
import config.FileDirectory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import parser.ProjectParser;
import report.CFG;

import utils.Content.ContractNodeType.BasicContractDefinition.Expression;
import utils.File.FileNode;
import utils.File.FileTree;

import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Scanner;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class Main {
    static FileTree ft;

    public static void main(String []args){
        try{
            boolean exit = false;
            boolean exitProgram = false;
            System.out.println("Welcome to Solidity Static Analyzer!");
            while(!exitProgram){
                System.out.println("Please enter a .sol file name or zip file name!");
                exit = false;
                Scanner in=new Scanner(System.in);
                String filePath = in.next();
                if(filePath==null||filePath.length()==0) {
                    System.out.println("Please enter a .sol file name or zip file name!");
                    continue;
                }
                System.out.println("initializing...");
                init(); //create report and tmp folders

                if(filePath.toString().toLowerCase().endsWith(".zip")){
                    Unzip(filePath); //unzip the files
                }else if(filePath.toString().toLowerCase().endsWith(".sol")){
                    MoveSolFile(filePath);

                }else{
                    System.out.println("Please enter a .sol file name or zip file name!");
                    continue;
                }


                System.out.println("Generating AST...");
                ProjectParser.init(ft);
                System.out.println("Generating CFG...");
                ProjectParser.initContextSensitiveParser(ft);
                ProjectParser.initCFG(ft);
                while(!exit){
                    System.out.println("Select your option: ");
                    System.out.println("\t0: Generate AST");
                    System.out.println("\t1: Generate CFG");
                    System.out.println("\t2: Scan Exposure");
                    System.out.println("\t3: Scanning Option");
                    System.out.println("\t4: New File/Zip File...");
                    System.out.println("\tOther: Exit the Program");
                    Scanner scanner = new Scanner(System.in);
                    int command = scanner.nextInt();
                    if(command>=0&&command<=3){
                        userOption(command);
                    }else if(command==4){
                        exit =true;
                        break;
                    }else {
                        exitProgram=true;
                        System.exit(0);
                    }

                }

            }

        }catch(Exception e){
            System.out.println(e.toString());
        }
        System.exit(0);
    }

    private static void init() throws Exception{
        if(!FileDirectory.tmp_root.exists()) FileDirectory.tmp_root.mkdir();
        else {
            FileUtils.deleteDirectory(FileDirectory.tmp_root);
            FileDirectory.tmp_root.mkdir();
        }
        if(!FileDirectory.report_root.exists()) FileDirectory.report_root.mkdir();
        if(!FileDirectory.ast_root.exists()) FileDirectory.ast_root.mkdir();
        if(!FileDirectory.cfg_root.exists()) FileDirectory.cfg_root.mkdir();
        if(!FileDirectory.scanning_config_root.exists()) FileDirectory.scanning_config_root.mkdir();

    }

    private static void MoveSolFile(String filePath) throws Exception{
        Path fileName = Paths.get(filePath).getFileName();
        try{
            InputStream in = new BufferedInputStream(
                    new FileInputStream(filePath));
            String path = FileDirectory.tmp_root.toPath().resolve(fileName).toString();
            OutputStream out = new BufferedOutputStream(
                    new FileOutputStream(path));
                byte[] buffer = new byte[1024];
                int lengthRead;
                while ((lengthRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, lengthRead);
                    out.flush();
                }
            ft = new FileTree();
            ft.addFileNode(ft.root,fileName.toString());
        }catch(Exception e){
            System.out.println(e);
        }
    }

    private static void Unzip(String zipIn) throws Exception{
        Stack<FileNode>s = new Stack();

        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipIn));
        ZipEntry zipEntry = zis.getNextEntry();

        ft = new FileTree();

        while(zipEntry!=null){
            String path = Paths.get(zipEntry.getName()).normalize().toString();
            Path extractPath =FileDirectory.tmp_root.toPath().normalize().resolve(zipEntry.getName());
            if(!Paths.get(zipEntry.getName()).subpath(0,1).toString().equals("__MACOSX")){
                int cur_layer_count = Paths.get(path).getNameCount();   //no of current dir layer
                int prev_layer_count;
                if(cur_layer_count==1){
                    prev_layer_count = 0;
                }else prev_layer_count = Paths.get(s.lastElement().path).getNameCount();

                if(!zipEntry.isDirectory()&& FilenameUtils.isExtension(zipEntry.getName(),"sol")){    //if it is .sol file, no need to push it to stack
                    if(cur_layer_count==1){
                        ft.addFileNode(ft.root,path);
                    }else if((cur_layer_count-prev_layer_count==1)||(cur_layer_count==prev_layer_count)){
                        ft.addFileNode(s.lastElement(),path);
                    }else{
                        add2FileTree(s, path);
                    }

                    //extract file then...
                    new File(extractPath.toString()).getParentFile().mkdirs();
                    extractFile(zis,extractPath.toString());
                }else{
                    //if dir is directory
                    if(cur_layer_count==1){ //1st layer handling
                        s.push(ft.addFileNode(ft.root,path));
                    }else if((cur_layer_count-prev_layer_count==1)||(cur_layer_count==prev_layer_count)){
                        //check if it is subfolder to the previous one or in the same folder to the previous one
                        s.push(ft.addFileNode(s.lastElement(),path));
                    }else {   //check if directory does not related to the previous one
                        add2FileTree(s, path);
                    }

                    //make directory then...
                    File dir = new File(extractPath.toString());
                    dir.mkdirs();
                }
            }
            zipEntry=zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();


    }

    private static void add2FileTree(Stack<FileNode> s, String path) {
        FileNode tmp = s.lastElement();  //pre operation
        while(!Paths.get(tmp.path).normalize().equals(Paths.get(path).getParent().normalize())){ //if the last element in stack is not the same of parent of current dir, do pop
            s.pop();
            tmp=s.lastElement();    //pop until parent is same as the popped element
        }
        ft.addFileNode(tmp, path);
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    private static void userOption(int cmd) throws Exception {
        switch(cmd){
            case 0:
                System.out.println("Building AST...");
                ProjectParser.viewTree();
                System.out.println("Done!");

                break;
            case 1:
                System.out.println("Building CFG...");
                CFG cfg = new CFG(ft.root);
                System.out.println("Done!");

                break;
            case 2:
                System.out.println("Scanning...");
                FileScanner f = new FileScanner(ft);
                FileScanner.scan();
                System.out.println("Done!");

                break;
            case 3:
                System.out.println("Opening File...");
                File file = new File(FileDirectory.scanning_config_root.toPath().resolve("scanning.xml").toString());
                if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                    String command = "rundll32 url.dll,FileProtocolHandler " + file.getCanonicalPath();
                    Runtime.getRuntime().exec(command);
                }
                else {
                    Desktop.getDesktop().edit(file);
                }
                break;
        }
    }
}
