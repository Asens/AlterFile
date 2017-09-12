package cn.asens.controller.componet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Asens
 * create 2017-09-09 11:17
 **/

public class TreeNode {
    private TreeNode parent;
    private String name;
    private List<TreeNode> list=new ArrayList<>();

    public TreeNode(String name) {
        this.name=name;
    }


    public void add(TreeNode node){
        list.add(node);
        node.setParent(this);
    }

    public List<TreeNode> children(){
        return list;
    }

    private void setParent(TreeNode parent){
        this.parent=parent;
    }

    public void display() {
        System.out.println();
        TreeNode p=parent;
        while(p!=null){
            p=p.parent;
            System.out.print("--");
        }
        System.out.print(name);
        list.forEach(TreeNode::display);
    }
}
