package br.ce.wcaquino.servicos;


import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {
	
	@Test
	public void teste() {
		
		//cenario
		LocacaoService LS = new LocacaoService();
		Usuario usuario = new Usuario("Ronaldo");
		Filme filme = new Filme("A espera de um milagre",2,55.00);

		//acao
		Locacao locacao = LS.alugarFilme(usuario, filme);
		
		
		//validacao
		Assert.assertEquals(55.0,locacao.getValor(), 0.01);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		
	}
}


