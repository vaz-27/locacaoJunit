package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class CalculadoraTest {
	
	private Calculadora calc;
	
	@Before
	public void inicia() {
		calc = new Calculadora();
	}
	
	@Test
	public void deveSomarDoisValores() {
		//cenario
		int a =5;
		int b = 3;

		//acao
		int result = calc.somar(a,b);
		
		//verificacao
		Assert.assertEquals(8, result);
	}
	
	@Test
	public void deveSubtrairDoisValores() {
		//cenario
		int a =5;
		int b = 3;
		
		//acao
		int result = calc.subtrair(a,b);
		
		//verificacao
		Assert.assertEquals(2, result);
	}
	
	@Test
	public void deveDivdirDoisValores() throws NaoPodeDividirPorZeroException {
		//cenario
		int a = 6;
		int b = 3;
		
		//acao
		int result = calc.dividir(a,b);
		
		//verificacao
		Assert.assertEquals(2, result);
	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void dividirPorZero() throws NaoPodeDividirPorZeroException {
		//cenario
		int a =5;
		int b = 0;
		
		//acao
		calc.dividir(a,b);
	
	}
}
