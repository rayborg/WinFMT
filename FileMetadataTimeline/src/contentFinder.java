/*
 	Win FMT, Copyright 2012, Raymond C. Borges Hink
    This file is part of WinFMT.

    WinFMT is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    WinFMT is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with WinFMT.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.JFileChooser;

public class contentFinder {
	static ArrayList<File> filePaths = new ArrayList<File>();
	static ArrayList<String> directoryPaths = new ArrayList<String>();
		
    public static HashMap<String,String> hmFileExt = new HashMap<String,String>();
    public static HashMap<String,String> filePath = new HashMap<String,String>();
    public static HashMap<String,Long> logicalSize = new HashMap<String,Long>();
    public static HashMap<String,FileTime> createdTime = new HashMap<String,FileTime>();
    public static HashMap<String,FileTime> lastAccTime = new HashMap<String,FileTime>();
    public static HashMap<String,FileTime> lastModTime = new HashMap<String,FileTime>();
    public static HashMap<String,Long> createdTimeUTC = new HashMap<String,Long>();
    public static HashMap<String,Long> lastAccTimeUTC = new HashMap<String,Long>();
    public static HashMap<String,Long> lastModTimeUTC = new HashMap<String,Long>();
    public static HashMap<String,Boolean> hiddenFile = new HashMap<String,Boolean>();
	
	// Process all files and directories under any directory recursively
	public static void visitAllDirsAndFiles(File dir) {
	String Path = ""; //temp variable that stores file paths

	    if (dir.isDirectory()) {
	        String[] children = dir.list();

        	//Records directory paths
        	directoryPaths.add(dir.getAbsolutePath());       
	        
        	if(children.length!=-1 && children.length!=0){
	        for (int i=0; i<children.length; i++) {
	        	Path=dir.getAbsolutePath();
	        	
	           	//Records all file paths
	        	filePaths.add(new File(Path+"/"+children[i]));
	        	
	        	//Recurses to visit files and folders contained within folders
	        	visitAllDirsAndFiles(new File(dir, children[i]));
	            
	        }//End for loop
          }//If children exist
	        
	    }//End if
	}// End of method
		
	//Main method
	public void main() throws Exception{
		
		System.out.println("Program Started");
		System.out.println("====================");
	  
		
	    //JFile Chooser for selecting the folder
		JFileChooser chooser = new JFileChooser();
		
		
		//Select root drive
		File[] roots;
		File dummy_file;
		try{
		roots = File.listRoots();
		dummy_file = new File("");
		if(roots[0].exists() && roots[0].isDirectory()){
	    dummy_file = new File(roots[0].getCanonicalPath());
		chooser.setCurrentDirectory(dummy_file);
        chooser.changeToParentDirectory();
		}//End of if file exists     
		}catch(Exception e){System.out.println("Navigate to drive or directory.");}
		
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showOpenDialog(chooser);
		
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	String path =chooser.getSelectedFile().getAbsolutePath();
	        System.out.println("You chose to open the directory: " + path);
	    //**END of File chooser**
	        System.out.println("Searching ...");
	        
	       File dir = new File(path);
	       //Runs method to visit all files and directories
	       visitAllDirsAndFiles(dir);
	       
	       //Prints directory count
	        int Directories = directoryPaths.size();
	        System.out.println("This folder contains "+Directories+" directories and "+filePaths.size()+" files.");
			    	    
			    //Prints out array list for directory Paths
		        for(int i=0;i<directoryPaths.size();i++){
		        	System.out.println("Directory number "+i+" "+directoryPaths.get(i));
		        }
		        System.out.println("\nFiles: ");
		        int tempExt, tempSize;
		        
		        //Initializing some temp variables
		        String ext;
		        Path file;
		        BasicFileAttributes attrs;
		        
			   //***Stores array list for filePaths in hashMap***
			       for(int i=0;i<filePaths.size();i++){
			        //Gets file extension
			           tempExt=filePaths.get(i).getName().lastIndexOf(".");
			           
			           //If file has an extension then
			    	   if(tempExt!=-1){
			        		tempSize=filePaths.get(i).getName().length();
			        		ext=filePaths.get(i).getName().substring(tempExt+1, tempSize);
			        		
			        		//***Place metadata into hashmaps*** 		
			        		//Puts file and file extension into a hashMap
			        		hmFileExt.put(filePaths.get(i).getName(), ext);	
			        		
			        	}//End of if file has extension
			   
			    	    filePath.put(filePaths.get(i).getName(),filePaths.get(i).getAbsolutePath());	
		        	    logicalSize.put(filePaths.get(i).getName(),filePaths.get(i).length());	
		        	    hiddenFile.put(filePaths.get(i).getName(),filePaths.get(i).isHidden());
		        		
		        	    //Using JNA nio to get file attributes
		        	    String currentFilePath = filePaths.get(i).getAbsolutePath();
		        	    
		        		file = Paths.get(currentFilePath);
		        		attrs = Files.readAttributes(file, BasicFileAttributes.class);  
		        		createdTime.put(filePaths.get(i).getName(), attrs.creationTime());
		        		lastAccTime.put(filePaths.get(i).getName(), attrs.lastAccessTime());
		        		lastModTime.put(filePaths.get(i).getName(), attrs.lastModifiedTime());
		        		
		        		//UTC times
		        		createdTimeUTC.put(filePaths.get(i).getName(), attrs.creationTime().toMillis());
		        		lastAccTimeUTC.put(filePaths.get(i).getName(), attrs.lastAccessTime().toMillis());
		        		lastModTimeUTC.put(filePaths.get(i).getName(), attrs.lastModifiedTime().toMillis());        		
			        }//End for loop get file metadata attributes and put into hashmaps
			       
			       
			       //Iterates through hashMap for filename extensions
			       //key = filePaths value=file extension
			        for (String key : hmFileExt.keySet()) {
			           System.out.println("Filename: "+key);
			           System.out.println("File extension: "+hmFileExt.get(key));
			           System.out.println("File Path: "+filePath.get(key));
			           System.out.println("Logical Size: "+logicalSize.get(key)+"bytes");			           
			           System.out.println("File creation time: "+createdTime.get(key));
				       System.out.println("Last accessed time:" +lastAccTime.get(key));			           
			           System.out.println("Last Modified time: "+lastModTime.get(key));
			           System.out.println("Hidden file: "+hiddenFile.get(key)+"\n");
			        }//End of for loop to print out all results
	    
			        //Displays available time ranges
				    FileTime minC = Collections.min(createdTime.values());
				    FileTime maxC = Collections.max(createdTime.values());
				    System.out.println("Created times range from: "+minC+" to "+maxC); 
				    
				    FileTime minA = Collections.min(lastAccTime.values());
				    FileTime maxA = Collections.max(lastAccTime.values());
				    System.out.println("Last accessed times range from: "+minA+" to "+maxA); 
				     
				    FileTime minM = Collections.min(lastModTime.values());
				    FileTime maxM = Collections.max(lastModTime.values());
				    System.out.println("Last modification times range from: "+minM+" to "+maxM+"\n"); 
				    
				    
				    System.out.println("\t***Finished***");
	    
	    
	    }else if(returnVal==1){//End of if user choose a directory
	    System.out.println("No directory chosen...");
	    System.out.println("\t***Finished***");
	    }
	    
	    
	}//End of main

}//End if class
