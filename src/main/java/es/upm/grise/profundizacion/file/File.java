package es.upm.grise.profundizacion.file;

import java.util.ArrayList;
import java.util.List;

public class File {

    private FileType type;
    private List<Character> content;

	/*
	 * Constructor
	 */
    public File() {
        this.content = new ArrayList<>();
    }

	/*
	 * Method to code / test
	 */
    public void addProperty(char[] newcontent) {
    	// If newcontent is null, throw InvalidContentException
        if (newcontent == null) {
            throw new InvalidContentException("Content cannot be null");
        }
        
        // If the file type is IMAGE, throw WrongFileTypeException
        if (this.type == FileType.IMAGE) {
            throw new WrongFileTypeException("Cannot add property to an IMAGE file");
        }
        
        // Add newcontent to existing content
        for (char c : newcontent) {
            this.content.add(c);
        }
    }

	/*
	 * Method to code / test
	 */
    public long getCRC32() {
    	// If content is empty, return 0
        if (this.content.isEmpty()) {
            return 0L;
        }
        
        // Transform ArrayList<Character> to byte[]
        byte[] bytes = new byte[this.content.size() * 2];
        int index = 0;
        
        for (Character c : this.content) {
            // Get least significant byte
            bytes[index++] = (byte) (c & 0x00FF);
            // Get most significant byte
            bytes[index++] = (byte) ((c >>> 8) & 0xFF);
        }
        
        // Calculate and return CRC32 using FileUtils
        FileUtils fileUtils = new FileUtils();
        return fileUtils.calculateCRC32(bytes);
    }
    
    
	/*
	 * Setters/getters
	 */
    public void setType(FileType type) {
    	
    	this.type = type;
    	
    }
    
    public List<Character> getContent() {
    	
    	return content;
    	
    }
    
    /*
     * Custom Exceptions
     */
    public static class InvalidContentException extends RuntimeException {
        public InvalidContentException(String message) {
            super(message);
        }
    }
    
    public static class WrongFileTypeException extends RuntimeException {
        public WrongFileTypeException(String message) {
            super(message);
        }
    }
    
}
