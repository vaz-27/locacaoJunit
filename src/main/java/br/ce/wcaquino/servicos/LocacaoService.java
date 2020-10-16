package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.text.ParseException;
import java.util.Date;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;


public class LocacaoService {
	
	
	public Locacao alugarFilme(Usuario usuario, Filme filme) {
		Locacao locacao = new Locacao();
		locacao.setFilme(filme);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(filme.getPrecoLocacao());

		//Entrega no dia seguinte
		Date dataLocacao = new Date();
		dataLocacao = adicionarDias(dataLocacao, 1);
		locacao.setDataRetorno(dataLocacao);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}

	public static void main(String[] args) throws ParseException {
		
		//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		//cenario
		LocacaoService LS = new LocacaoService();
		Usuario usuario = new Usuario("Ronaldo");
		Filme filme = new Filme("A espera de um milagre",2,55.00);

		//acao
		Locacao locacao = LS.alugarFilme(usuario, filme);
		
		
		//validacao
		System.out.println(locacao.getValor() == 55.0);
		System.out.println(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		System.out.println(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		
	}
}