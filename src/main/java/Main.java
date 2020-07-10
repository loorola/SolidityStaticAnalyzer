import config.FileDirectory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import parser.ProjectParser;
import report.CFG;
import report.ExposureReport;

import utils.File.FileNode;
import utils.File.FileTree;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class Main {
    static FileTree ft;

    public static void main(String []args){
        try{
            init(); //create report and tmp folders
            Unzip(args[0]); //unzip the files
            ProjectParser.init(ft);
            ProjectParser.initContextSensitiveParser(ft);
            ProjectParser.initCFG(ft);
            //CFG.generateCFG(ft.root);
            //ExposureReport.generateCFG(ft.root);
            /*
            ZipFile zip = new ZipFile(args[0]);
            zip.stream().map(ZipEntry::getName).forEach((n)->{
                Path tmp = Paths.get(n);
                if(Files.isDirectory(tmp)){
                }
                if(FilenameUtils.isExtension(n,"sol")&&!tmp.subpath(0,1).toString().equals("__MACOSX"))System.out.println(n);
            });
            */

        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    private static void init() throws Exception{
        if(!FileDirectory.tmp_root.exists()) FileDirectory.tmp_root.mkdir();
        else {
            FileUtils.deleteDirectory(FileDirectory.tmp_root);
            FileDirectory.tmp_root.mkdir();
        }
        if(!FileDirectory.report_root.exists()) FileDirectory.report_root.mkdir();
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
}
