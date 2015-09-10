package br.ufes.inf.nemo.mlt.web.reasoner.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class FileUtil {
	public static String chooseFile(String message1, String path, String ext, String message2, int optional) throws Exception{
		String[] files = FileUtil.getFileList(path, ext);
		List<String> filesAux = Arrays.asList(files);
		
		Integer owlFileIndex = chooseOne(filesAux, ext + " files", message1, optional, true, false);
        
		if(optional == 1 && owlFileIndex <= 0){
			return "";
		}else{
			System.out.println(message2 + files[owlFileIndex]);
	        System.out.println();

	        return path + files[owlFileIndex];
		}        
	}
	
	// inner class, generic extension filter
	public static class GenericExtFilter implements FilenameFilter {
 
		private String ext;
 
		public GenericExtFilter(String ext) {
			this.ext = ext;
		}
 
		public boolean accept(File dir, String name) {
			return (name.endsWith(ext));
		}
	}
		
	public static String[] getFileList(String path, String ext){
		GenericExtFilter filter = new GenericExtFilter(ext);
		 
		File emptyFile = new File("");
		File dir = new File(emptyFile.getAbsolutePath()+"/"+path);
 
		if( ! dir.isDirectory() ){
			System.out.println("Directory does not exists : " + dir.getPath());
			return null;
		}
 
		// list out all the file name and filter by the extension
		String[] list = dir.list(filter);
		
		System.out.println("No files end with: " + ext);
		
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> int chooseOne(List<T> list, String listName, String message, int optional, boolean sortList, boolean allowNoLimit) throws Exception{
		List<Comparable> comparableList = (List<Comparable>) list;
		System.out.println();
		System.out.println("--- " + listName + " ---");
		
		if(list.size() == 0){
			throw new Exception("MLT-Reasoner could not be performed. A list is empty. Please try again using another path.");
		}
		
		if(sortList) Collections.sort(comparableList);
		
		for (int i = 0; i < list.size(); i+=1) {
			int id = (i+1);
			System.out.println(id + " - " + list.get(i));
		}
		
		Integer index = getOptionFromConsole(list, message, optional, allowNoLimit);
		
		return index;
	}
	
	public static <T> int getOptionFromConsole(List<T> list, String message, int optional, boolean allowNoLimit){
		return getOptionFromConsole(list, message, list.size(), optional, allowNoLimit);
	}
	
	public static <T> int getOptionFromConsole(List<T> list, String message, int highestOption, int optional, boolean allowNoLimit){
		Integer index = getOptionFromConsole(message, 1, highestOption, optional, allowNoLimit);	
		index -= 1;
		return index;
	}
	
	public static int getOptionFromConsole(String message, int lowestOption, int highestOption, int optional, boolean allowNoLimit){
		Integer index = 0;
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		boolean ok;
		do {
			ok = true;
			try {
				if(optional == 1 && index == 0){
					message.replace(":", "");
					message += "(this step is optional. Choose 0 to skip): ";
				}
				System.out.print(message);
				index = Integer.valueOf(bufferRead.readLine());
			} catch (Exception e) {
				ok = false;
			}
			if(optional == 1 && index == 0){
				return index;
			}
			if(((index < lowestOption || index > highestOption) && (!allowNoLimit || (allowNoLimit && index != -1))) || !ok){
				String highestOptionText;
				if(highestOption < Integer.MAX_VALUE){
					highestOptionText = " to " + highestOption;
				}else{
					highestOptionText = "";
				}
				
				System.out.print("You have to choose a number from " + lowestOption + highestOptionText);
				if(allowNoLimit){
					System.out.print(" or -1 for no limit");
				}
				System.out.println(".");
			}
		} while (((index < lowestOption || index > highestOption) && (!allowNoLimit || (allowNoLimit && index != -1))) || !ok);
		
		return index;
	}
}
