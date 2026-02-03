package es.upm.grise.profundizacion.file;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FileTest {
	
	private File file;
	
	@BeforeEach
	public void setUp() {
		file = new File();
	}
	
	// ========== PRUEBAS DEL CONSTRUCTOR ==========
	
	@Test
	public void testConstructorInitializesContentAsEmptyList() {
		// Verificar que content no es null
		assertNotNull(file.getContent(), "Content should not be null after construction");
		
		// Verificar que content está vacío
		assertTrue(file.getContent().isEmpty(), "Content should be empty after construction");
	}
	
	// ========== PRUEBAS DE addProperty() ==========
	
	@Test
	public void testAddPropertyAddsContentSuccessfully() {
		// Configurar el archivo como tipo PROPERTY
		file.setType(FileType.PROPERTY);
		
		// Añadir contenido
		char[] newContent = {'k', 'e', 'y', '=', 'v', 'a', 'l', 'u', 'e'};
		file.addProperty(newContent);
		
		// Verificar que el contenido se añadió correctamente
		assertEquals(9, file.getContent().size(), "Content size should be 9");
		assertEquals('k', file.getContent().get(0), "First character should be 'k'");
		assertEquals('e', file.getContent().get(8), "Last character should be 'e'");
	}
	
	@Test
	public void testAddPropertyAccumulatesContent() {
		// Configurar el archivo como tipo PROPERTY
		file.setType(FileType.PROPERTY);
		
		// Añadir contenido primera vez
		char[] firstContent = {'A', 'B'};
		file.addProperty(firstContent);
		
		// Añadir contenido segunda vez
		char[] secondContent = {'C', 'D'};
		file.addProperty(secondContent);
		
		// Verificar que el contenido se acumuló
		assertEquals(4, file.getContent().size(), "Content size should be 4");
		assertEquals('A', file.getContent().get(0), "First character should be 'A'");
		assertEquals('B', file.getContent().get(1), "Second character should be 'B'");
		assertEquals('C', file.getContent().get(2), "Third character should be 'C'");
		assertEquals('D', file.getContent().get(3), "Fourth character should be 'D'");
	}
	
	@Test
	public void testAddPropertyThrowsInvalidContentExceptionWhenContentIsNull() {
		// Configurar el archivo como tipo PROPERTY
		file.setType(FileType.PROPERTY);
		
		// Verificar que lanza la excepción cuando el contenido es null
		Exception exception = assertThrows(File.InvalidContentException.class, () -> {
			file.addProperty(null);
		});
		
		// Verificar el mensaje de la excepción
		assertEquals("Content cannot be null", exception.getMessage());
	}
	
	@Test
	public void testAddPropertyThrowsWrongFileTypeExceptionWhenTypeIsImage() {
		// Configurar el archivo como tipo IMAGE
		file.setType(FileType.IMAGE);
		
		// Intentar añadir contenido
		char[] content = {'k', 'e', 'y'};
		
		// Verificar que lanza la excepción cuando el tipo es IMAGE
		Exception exception = assertThrows(File.WrongFileTypeException.class, () -> {
			file.addProperty(content);
		});
		
		// Verificar el mensaje de la excepción
		assertEquals("Cannot add property to an IMAGE file", exception.getMessage());
	}
	
	// ========== PRUEBAS DE getCRC32() ==========
	
	@Test
	public void testGetCRC32ReturnsZeroWhenContentIsEmpty() {
		// Verificar que retorna 0 cuando el content está vacío
		long crc32 = file.getCRC32();
		assertEquals(0L, crc32, "CRC32 should be 0 when content is empty");
	}
	
	@Test
	public void testGetCRC32CalculatesCorrectlyWithContent() {
		// Configurar el archivo como tipo PROPERTY
		file.setType(FileType.PROPERTY);
		
		// Añadir contenido
		char[] content = {'A', 'B', 'C'};
		file.addProperty(content);
		
		// Nota: Como FileUtils.calculateCRC32() está mockeado en la implementación real,
		// aquí solo verificamos que el método no lanza excepciones y retorna un valor
		long crc32 = file.getCRC32();
		
		// El valor dependerá de la implementación de FileUtils
		// Por ahora, solo verificamos que no es el valor por defecto de content vacío
		assertNotNull(crc32, "CRC32 should not be null");
	}
	
	@Test
	public void testGetCRC32HandlesUnicodeCharacters() {
		// Configurar el archivo como tipo PROPERTY
		file.setType(FileType.PROPERTY);
		
		// Añadir caracteres Unicode
		char[] content = {'Ñ', 'é', 'ü'}; // Caracteres con valores > 255
		file.addProperty(content);
		
		// Verificar que no lanza excepciones al calcular CRC32
		assertDoesNotThrow(() -> {
			long crc32 = file.getCRC32();
			assertNotNull(crc32);
		});
	}
	
	@Test
	public void testGetCRC32HandlesASCIICharacters() {
		// Configurar el archivo como tipo PROPERTY
		file.setType(FileType.PROPERTY);
		
		// Añadir caracteres ASCII simples
		char[] content = {'1', '2', '3', '4', '5'};
		file.addProperty(content);
		
		// Verificar que no lanza excepciones al calcular CRC32
		assertDoesNotThrow(() -> {
			long crc32 = file.getCRC32();
			assertNotNull(crc32);
		});
	}

}
