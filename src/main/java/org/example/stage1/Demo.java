package org.example.stage1;

import ajs.printutils.PrettyPrintTree;

import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Demo  {


    public static void main(String[] args) {



        Tree23<Integer> tree = new Tree23<>();
        Scanner scanner = new Scanner(System.in);
        String cmd = "";
        while (!"exit".equals(cmd)){
            System.out.print("> ");
            cmd = scanner.nextLine();
            String[] cmds = cmd.split(" ");
            if(cmds.length == 1){
                if(cmds[0].equals("inorder")){
                    System.out.println(tree.inOrder());
                }
                if(cmds[0].equals("clear")){
                    tree.clear();
                }

            }
            if(cmds.length == 2){
                if(cmds[0].equals("+")){
                    tree.add(Integer.valueOf(cmds[1]));
                }
                if(cmds[0].equals("-")){
                    tree.remove(Integer.valueOf(cmds[1]));
                }
                if(cmds[0].equals("?")){
                    System.out.println(tree.contains(Integer.valueOf(cmds[1])));
                }

                if(cmds[0].equals("inc")){
                    String[] range = cmds[1].split(":");
                    Integer minValues = Integer.valueOf(range[0]);
                    Integer maxValues = Integer.valueOf(range[1]);
                    for (int i = minValues; i < maxValues; i++) {
                        tree.add(i);
                    }
                }
                if(cmds[0].equals("incStep")){
                    String[] range = cmds[1].split(":");
                    Integer minValues = Integer.valueOf(range[0]);
                    Integer maxValues = Integer.valueOf(range[1]);
                    for (int i = minValues; i < maxValues; i++) {
                        System.out.println("Adding " + i );
                        tree.add(i);
                        tree.print();

                    }
                }
                if(cmds[0].equals("dec")){
                    String[] range = cmds[1].split(":");
                    Integer minValues = Integer.valueOf(range[0]);
                    Integer maxValues = Integer.valueOf(range[1]);
                    for (int i = maxValues; i > minValues; i--) {
                        tree.add(i);
                    }
                }
                if(cmds[0].equals("decStep")){
                    String[] range = cmds[1].split(":");
                    Integer minValues = Integer.valueOf(range[0]);
                    Integer maxValues = Integer.valueOf(range[1]);
                    for (int i = maxValues; i > minValues; i--) {
                        System.out.println("Adding " + i );
                        tree.add(i);
                        tree.print();
                    }
                }
            }

            tree.print();
            System.out.println(tree.inOrder());
        }
    }

}
