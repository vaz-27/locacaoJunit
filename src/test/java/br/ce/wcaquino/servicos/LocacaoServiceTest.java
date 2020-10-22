package br.ce.wcaquino.servicos;


import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;


public class LocacaoServiceTest {
	
	private LocacaoService LS;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void cenario() {
		LS = new LocacaoService();
	}	
	
	@Test
	public void teste_alugaFilme() throws LocadoraException, FilmeSemEstoqueException {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		Usuario usuario = new Usuario("Ronaldo");
		List<Filme> filmes = Arrays.asList(new Filme("A espera de um milagre",2,55.00));

		//acao
		Locacao locacao = LS.alugarFilme(usuario, filmes);
				
		//validacao
		error.checkThat(locacao.getValor(),is(equalTo(55.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
	
	}
	
	@Test (expected = FilmeSemEstoqueException.class)
	public void teste_filmeSemEstoque() throws Exception {
		//cenario
		Usuario usuario = new Usuario("Ronaldo");
		List<Filme> filmes = Arrays.asList(new Filme("A espera de um milagre",0,55.00));

		//acao
		LS.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void teste_usuarioVazio() throws FilmeSemEstoqueException {
		//cenario
		List<Filme> filmes = Arrays.asList(new Filme("A espera de um milagre",2,55.00));

		//acao
		try {
			LS.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuario vazio"));
		}		
	}
	
	@Test
	public void teste_filmeVazio() throws LocadoraException, FilmeSemEstoqueException {
		//cenario
		Usuario usuario = new Usuario("Ronaldo");
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filmes vazio");

		//acao
		LS.alugarFilme(usuario, null);		
	}
	
	@Test
	public void teste_terceiroFilmePaga75() throws LocadoraException, FilmeSemEstoqueException {
		//cenario
		Usuario usuario = new Usuario("Wanda");
		List<Filme> filmes = Arrays.asList(new Filme("A espera de um milagre",2,55.00), 
				new Filme("Orgulho e Preconceito", 1, 40.00),
				new Filme("Diario de uma paixão", 2, 54.00)
				);
		
		//acao
		Locacao result = LS.alugarFilme(usuario, filmes);

		//verificacao
		Assert.assertThat(result.getValor(), is(135.50));
				
	}
	
	@Test
	public void teste_quartoFilmePaga50() throws LocadoraException, FilmeSemEstoqueException {
		//cenario
		Usuario usuario = new Usuario("Wanda");
		List<Filme> filmes = Arrays.asList(new Filme("A espera de um milagre",2,55.00), 
				new Filme("Orgulho e Preconceito", 1, 40.00),
				new Filme("Diario de uma paixão", 2, 54.00),
				new Filme("Matrix", 2, 50.00)
				);
		
		//acao
		Locacao result = LS.alugarFilme(usuario, filmes);

		//verificacao
		Assert.assertThat(result.getValor(), is(160.50));
				
	}
	
	@Test
	public void teste_quintoFilmePaga25() throws LocadoraException, FilmeSemEstoqueException {
		//cenario
		Usuario usuario = new Usuario("Wanda");
		List<Filme> filmes = Arrays.asList(new Filme("A espera de um milagre",2,55.00), 
				new Filme("Orgulho e Preconceito", 1, 40.00),
				new Filme("Diario de uma paixão", 2, 54.00),
				new Filme("Matrix", 2, 50.00),
				new Filme("Sherk",2,100.00)
				);
		
		//acao
		Locacao result = LS.alugarFilme(usuario, filmes);

		//verificacao
		Assert.assertThat(result.getValor(), is(185.50));			
	}
	
	@Test
	public void teste_sextoFilmeFree() throws LocadoraException, FilmeSemEstoqueException {
		//cenario
		Usuario usuario = new Usuario("Wanda");
		List<Filme> filmes = Arrays.asList(new Filme("A espera de um milagre",2,55.00), 
				new Filme("Orgulho e Preconceito", 1, 40.00),
				new Filme("Diario de uma paixão", 2, 54.00),
				new Filme("Matrix", 2, 50.00),
				new Filme("Sherk",2,100.00),
				new Filme("Scoby Doo",3,50.00)
				);
		
		//acao
		Locacao result = LS.alugarFilme(usuario, filmes);

		//verificacao
		Assert.assertThat(result.getValor(), is(185.50));			
	}
	
	@Test
	public void teste_naoDevolverFilmeNoDomingo() throws LocadoraException, FilmeSemEstoqueException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario = new Usuario("Bia");
		List<Filme> filmes = Arrays.asList(new Filme("Valente", 3, 80.00));
		
		//acao
		Locacao retorno = LS.alugarFilme(usuario, filmes);
		
		//verificacao
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		Assert.assertFalse(ehSegunda);
	}
}

