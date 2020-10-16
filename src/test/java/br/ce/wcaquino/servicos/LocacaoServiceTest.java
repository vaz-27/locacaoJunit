package br.ce.wcaquino.servicos;


import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.Assert;

public class LocacaoServiceTest {
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Test
	public void teste() throws Exception {
		
		//cenario
		LocacaoService LS = new LocacaoService();
		Usuario usuario = new Usuario("Ronaldo");
		Filme filme = new Filme("A espera de um milagre",2,55.00);

		//acao
		Locacao locacao = LS.alugarFilme(usuario, filme);
		
		
		//validacao
		error.checkThat(locacao.getValor(),is(equalTo(55.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
	
	}
	
	@Test (expected = FilmeSemEstoqueException.class)
	public void teste_filmeSemEstoque() throws Exception {
		 //cenario
		LocacaoService LS = new LocacaoService();
		Usuario usuario = new Usuario("Ronaldo");
		Filme filme = new Filme("A espera de um milagre",0,55.00);

		//acao
		LS.alugarFilme(usuario, filme);
	}
	
	@Test
	public void teste_usuarioVazio() throws FilmeSemEstoqueException {
		 //cenario
		LocacaoService LS = new LocacaoService();
		Filme filme = new Filme("A espera de um milagre",1,55.00);

		//acao
		try {
			LS.alugarFilme(null, filme);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuario vazio"));
		}
		
	}
}


