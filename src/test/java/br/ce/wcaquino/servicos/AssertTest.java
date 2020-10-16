package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class AssertTest {
	
	@Test
	public void test() {
		Assert.assertTrue(true);
		Assert.assertFalse(false);
		
		Assert.assertEquals(2, 2);
		Assert.assertEquals(1.123, 1.12, 0.01); //o terceiro termo é um delta para margem
		
		int i = 5;
		Integer i2 = 5;
		
		Assert.assertEquals(Integer.valueOf(i), i2);
		
		Assert.assertEquals("amora", "amora");
		Assert.assertTrue("amora".equalsIgnoreCase("Amora"));
		Assert.assertTrue("amora".startsWith("am"));
		
		Usuario u1 = new Usuario("Fernanda");
		Usuario u2 = u1;
		
		Assert.assertEquals(u2, u1);
	
	}

}
