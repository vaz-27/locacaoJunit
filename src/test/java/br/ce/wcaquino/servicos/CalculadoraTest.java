package br.ce.wcaquino.servicos;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import br.ce.wcaquino.runners.ParallelRunner;

@RunWith(ParallelRunner.class)
public class CalculadoraTest {
	
	private Calculadora calc;
	
	@Before
	public void inicia() {
		calc = new Calculadora();
		System.out.println("iniciando1...");
	}
	
	@After
	public void fim() {
		System.out.println("finalizando1...");
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
