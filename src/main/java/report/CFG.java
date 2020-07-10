package report;

import org.antlr.v4.runtime.misc.MultiMap;
import org.antlr.v4.runtime.misc.OrderedHashSet;
import utils.Content.ContractNodeType.BaseNode;
import utils.Content.ContractNodeType.SolidityClassDefinition.Contract;
import utils.Content.ContractNodeType.SolidityClassDefinition.Instance;
import utils.Content.ContractNodeType.SolidityClassDefinition.Interface;
import utils.Content.ContractNodeType.SolidityClassDefinition.Library;
import utils.File.FileNode;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.*;

public class CFG {
    public CFG(FileNode root) throws Exception {
        initFileStructure(root);
        initSourceStructure(root);
        try (PrintWriter out = new PrintWriter("filename.txt")) {
            out.println(toDOT(root));
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    static Graph g = new Graph();
    static class Graph{
        Set<BaseNode> baseNodes = new OrderedHashSet<>();
        MultiMap<BaseNode,BaseNode> edges = new MultiMap<>();
        public void setEdges(BaseNode source, BaseNode target){
            edges.map(source, target);
        }
    }

    private static String toDOT(FileNode root) throws Exception{

        StringBuilder buf = new StringBuilder();
        buf.append("digraph G{\n");
        for(BaseNode src: g.edges.keySet()){
            for(BaseNode trg: g.edges.get(src)){
                buf.append(" ");
                buf.append(src.nodeName);
                buf.append(" ["+src.label+"]");
                buf.append(" -> ");
                buf.append(trg.nodeName);
                buf.append(" ["+trg.label+"]");
                buf.append("\n");
            }
        }

        buf.append("}");
        return buf.toString();
    }

    private static void initFileStructure(FileNode root){
        Stack<FileNode> s=new Stack<FileNode>();
        Path p;
        s.push(root);
        FileNode f;
        while(!s.isEmpty()){
            f=s.pop();
            if(f.path!=null&&!(new File(f.path).isDirectory())){
                for(int i=0;i<f.fileContent.contractList.size();i++){
                    g.baseNodes.add(f.fileContent.contractList.get(i));
                }
                for(int i=0;i<f.fileContent.libraryList.size();i++){
                    g.baseNodes.add(f.fileContent.libraryList.get(i));
                }
                for(int i=0;i<f.fileContent.interfaceList.size();i++){
                    g.baseNodes.add(f.fileContent.interfaceList.get(i));
                }
            }
            f.children.forEach(x->s.push(x));
        }
    }

    private static void initSourceStructure(FileNode root){
        Stack<FileNode> s=new Stack<FileNode>();
        Path p;
        s.push(root);
        FileNode f;
        Contract c;
        Library li;
        Interface in;
        while(!s.isEmpty()){
            f=s.pop();
            for(int i=0;i<f.fileContent.contractList.size();i++){
                c=f.fileContent.contractList.get(i);
                System.out.println(c.inheritanceTypeList.size());
                for(int j=0;j<c.inheritanceTypeList.size();j++){
                    g.setEdges(c, c.inheritanceTypeList.get(j).getInstance());
                }
            }
            for(int i=0;i<f.fileContent.libraryList.size();i++){
                li=f.fileContent.libraryList.get(i);
                for(int j=0;j<li.inheritanceTypeList.size();j++){
                    g.setEdges(li, li.inheritanceTypeList.get(j).getInstance());
                }
            }
            for(int i=0;i<f.fileContent.interfaceList.size();i++){
                in=f.fileContent.interfaceList.get(i);
                for(int j=0;j<in.inheritanceTypeList.size();j++){
                    g.setEdges(in, in.inheritanceTypeList.get(j).getInstance());
                }
            }
            f.children.forEach(x->s.push(x));
        }
    }

}
