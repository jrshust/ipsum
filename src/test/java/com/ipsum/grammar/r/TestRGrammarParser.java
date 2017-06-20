package com.ipsum.grammar.r;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class TestRGrammarParser {
	
	@Test
	public void shouldFindDefinedVariables() throws Exception {
		String code = readResource("/R/one_line_defined.R");
		RGrammarParser parser = new RGrammarParser();
		Set<String> defined = parser.getDefinedVariables(code);
		Set<String> undefined = parser.getUndefinedVariables(code);
		
		Assert.assertEquals(1, defined.size());
		Assert.assertEquals("test", defined.iterator().next());
		Assert.assertTrue(undefined.isEmpty());
	}
	
	@Test
	public void shouldFindUndefinedVariables() throws Exception {
		String code = readResource("/R/one_line_undefined.R");
		RGrammarParser parser = new RGrammarParser();
		Set<String> defined = parser.getDefinedVariables(code);
		Set<String> undefined = parser.getUndefinedVariables(code);
		
		Assert.assertEquals(1, defined.size());
		Assert.assertEquals("test", defined.iterator().next());
		Assert.assertEquals(1, undefined.size());
		Assert.assertEquals("iris", undefined.iterator().next());
	}
	
	@Test
	public void simpleReadData() throws Exception {
		String code = readResource("/R/simple_read_data.R");
		RGrammarParser parser = new RGrammarParser();
		Set<String> defined = parser.getDefinedVariables(code);
		Set<String> undefined = parser.getUndefinedVariables(code);
		
		Assert.assertEquals(2, defined.size());
		// Assert.assertEquals("test", defined.iterator().next());
		// Assert.assertEquals(1, undefined.size());
		// Assert.assertEquals("iris", undefined.iterator().next());
		System.out.println(defined);
		System.out.println(undefined);
	}
	
	@Test
	public void simpleReadModifySaveData() throws Exception {
		String code = readResource("/R/simple_read_modify_save.R");
		RGrammarParser parser = new RGrammarParser();
		Set<String> defined = parser.getDefinedVariables(code);
		Set<String> undefined = parser.getUndefinedVariables(code);
		
		Assert.assertEquals(4, defined.size());
		// Assert.assertEquals("test", defined.iterator().next());
		// Assert.assertEquals(1, undefined.size());
		// Assert.assertEquals("iris", undefined.iterator().next());
		System.out.println(defined);
		System.out.println(undefined);
	}
	
	private String readResource(String path) throws Exception {
        URL url = TestRGrammarParser.class.getResource(path);
        Path resPath = java.nio.file.Paths.get(url.toURI());
        return new String(Files.readAllBytes(resPath), "UTF8"); 
	}
}
