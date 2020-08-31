// Caio Boni
// 3/14/2019
// CSE 143 - TA: Yael Goldin
// Assignment #8

//This program uses the Huffman algorith which is able to compress file into binary
// which uses less binary digits. It also the user to convert the binary file back
// into the standard text it was before. All that is needed is an appropriate txt
// file in the same folder as the program in order to deocompress/ translate the 
// file.

import java.util.*;
import java.io.*;

public class HuffmanCode{

   private HuffmanNode mainRoot;
   
   //As long as the frequencie is not empty the HuffmanCode contructs a sorted 
   // path in which the program is able to read where the most freuquent characters
   // have the shortest path and less frequent shortest path.
   public HuffmanCode(int[] frequencies){
      Queue<HuffmanNode> qNode = new PriorityQueue<HuffmanNode>();
      for(int i = 0; i < frequencies.length; i++){
         if(frequencies[i] > 0){
            HuffmanNode letter = new HuffmanNode(i,frequencies[i]);
            qNode.add(letter);
         }
      }
      HuffmanNode otherNode = qNode.peek();
      while(qNode.size() > 1){
         HuffmanNode temp1 = qNode.remove();
         HuffmanNode temp2 = qNode.remove();
         int freq = temp1.occurrence + temp2.occurrence;
         otherNode = new HuffmanNode(0, freq, temp1, temp2);
         qNode.add(otherNode);
      }
      mainRoot = otherNode;
   }
   
   //Creates a HuffmanTree that is able to go through the bonary code.
   public HuffmanCode(Scanner input){
      while(input.hasNextLine()){
         int asciiValue = Integer.parseInt(input.nextLine());
         String binary = input.nextLine();
         mainRoot = compress(asciiValue , binary, mainRoot);       
      }
   }
   
   //Stores the frequency along with the binary code for it
   private HuffmanNode compress(int ascii, String binary, HuffmanNode root){
      if(binary.isEmpty()){
         return new HuffmanNode((ascii), 0);
      }else{
         if(root == null){
            root = new HuffmanNode(0,0);
         }
         if(binary.charAt(0) == '0'){ 
         root.left = compress(ascii, binary.substring(1), root.left);
         } else{
         root.right = compress(ascii , binary.substring(1), root.right);
         }
       }
       return root;
   }
         
   
   //Creates line pairs for the file where the the character data is printed
   public void save(PrintStream output){
      saveHelper(output, mainRoot, "");
   }
   
   //Helps the save method by going through the HuffmanTree of the mainRoot that
   // then prints the ascii value of the charater and the binary code associated
   // with it.
   private void saveHelper(PrintStream output, HuffmanNode root, String binary){
      if(root != null){
      saveHelper(output, root.left, binary + "0");
      saveHelper(output, root.right, binary + "1");//

         if(root.right == null && root.left == null){
            output.println(root.ascii);
            output.println(binary);
         }
      }
   }
   
   //Read the binary compressed file and prints the associated ascii character
   // that the binary code associated in the tree has.
   public void translate(BitInputStream input, PrintStream output){
      HuffmanNode root = mainRoot;
      while(input.hasNextBit() || root.left == null && root.right == null){
         if(root.left == null && root.right == null){
            output.write(root.ascii);
            root = mainRoot;
         } else {
            int bit = input.nextBit();
            if(bit == 1){
               root = root.right;
            } else {
               root = root.left;
            }
          }
      
       }
   }

   //Stores the ascii value and occurances of the charcter provided in the file
   // this making it possible to find it later and more efficiently.
   private class HuffmanNode implements Comparable<HuffmanNode>{
   
      public int ascii;
      public int occurrence;
      public HuffmanNode left;
      public HuffmanNode right;
      
      //Creates a HuffmanTree with given ascii value and frequncy of the associated 
      // ascii
      public HuffmanNode(int ascii, int occurrence, HuffmanNode left, 
                         HuffmanNode right){
         this.ascii = ascii;
         this.occurrence = occurrence;
         this.left = left;
         this.right = right;
      }
      
      //Contructs a HuffmanNode with the given ascii and frequency
      public HuffmanNode(int ascii, int occurrence){
         this(ascii,occurrence,null,null);
      }
      
      //Compares the frequncy of this HuffmanNode with another one 
      public int compareTo(HuffmanNode other){
         return this.occurrence - other.occurrence;
      }
   }
}