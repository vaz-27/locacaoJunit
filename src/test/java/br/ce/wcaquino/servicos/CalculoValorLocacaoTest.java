package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

public class CalculoValorLocacaoTest {
	
	private List<Filme> filmes;
	private Double valorLocacao;
	private LocacaoService LS;
	
	@Before
	public void cenario() {
		LS = new LocacaoService();
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

}
