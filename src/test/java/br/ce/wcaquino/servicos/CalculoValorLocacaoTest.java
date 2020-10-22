package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {
	
	private LocacaoService LS;
	
	@Parameter
	public List<Filme> filmes;
	
	@Parameter(value=1)
	public Double valorLocacao;
	
	@Parameter(value=2)
	public String cenario;
	
	@Before
	public void cenario() {
		LS = new LocacaoService();
	}
	
	private static Filme filme1 = new Filme("A espera de um milagre",2,55.00);
	private static Filme filme2 = new Filme("Orgulho e Preconceito", 1, 40.00);
	private static Filme filme3 = new Filme("Diario de uma paixão", 2, 54.00);
	private static Filme filme4 = new Filme("Matrix", 2, 50.00);
	private static Filme filme5 = new Filme("Sherk",2,100.00);
	private static Filme filme6 = new Filme("Scoby Doo",3,50.00);
	private static Filme filme7 = new Filme("Um amor para recordar",4,100.00);
	
	
	@Parameters(name ="{2}")
	public static Collection<Object[]> getParametros(){
		return Arrays.asList(new Object[][] {
			{Arrays.asList(filme1, filme2), 95.0, "2 Filme: Sem desconto"},
			{Arrays.asList(filme1, filme2,filme3), 135.5, "3 Filme: 25%"},
			{Arrays.asList(filme1, filme2,filme3, filme4), 160.5, "4 Filme: 50%"},
			{Arrays.asList(filme1, filme2,filme3, filme4, filme5), 185.5, "5 Filme: 75%"},
			{Arrays.asList(filme1, filme2,filme3, filme4, filme5, filme6), 185.5, "6 Filme: 100%"},
			{Arrays.asList(filme1, filme2,filme3, filme4, filme5, filme6, filme7), 285.5, "7 Filme: Sem desconto"}
		});
	}
	
	@Test
	public void teste_deveCalcularValorLocacao() throws LocadoraException, FilmeSemEstoqueException {
		//cenario
		Usuario usuario = new Usuario("Wanda");
		
		//acao
		Locacao result = LS.alugarFilme(usuario, filmes);

		//verificacao
		Assert.assertThat(result.getValor(), is(valorLocacao));
				
	}
	
	@Test
	public void print() {
		System.out.println(valorLocacao);
	}

}
