package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.List;
import java.util.Date;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;


public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws LocadoraException, FilmeSemEstoqueException {
		
		if(usuario == null) {
			throw new LocadoraException("Usuario vazio");
		}
		
		if(filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filmes vazio");
		}
		
		for(Filme filme : filmes) {
			if (filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException();
			}
		}
				
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		Double valorTotal = 0d;
		for(int i=0; i<filmes.size(); i++) {
			Filme filme = filmes.get(i);
			Double valorFilme = filme.getPrecoLocacao();
			if(i == 2) {
				valorFilme *= 0.75;
			}
			if(i == 3) {
				valorFilme *= 0.5;
			}
			if(i == 4) {
				valorFilme *= 0.25;
			}
			if(i == 5) {
				valorFilme = 0.0;
			}
			valorTotal += valorFilme;
		}
		
		locacao.setValor(valorTotal);

		//Entrega no dia seguinte
		Date dataLocacao = new Date();
		dataLocacao = adicionarDias(dataLocacao, 1);
		locacao.setDataRetorno(dataLocacao);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}
	
}