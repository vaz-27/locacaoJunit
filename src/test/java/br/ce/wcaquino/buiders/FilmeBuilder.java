package br.ce.wcaquino.buiders;

import br.ce.wcaquino.entidades.Filme;

public class FilmeBuilder {
	
	private Filme filme;
	private FilmeBuilder() {}
	
	public static FilmeBuilder umFilmeComValor() {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme = new Filme();
		builder.filme.setEstoque(2);
		builder.filme.setNome("Nana");
		builder.filme.setPrecoLocacao(10.0);		
		return builder;
	}
	
	public static FilmeBuilder umFilmeSemEstoque() {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme = new Filme();
		builder.filme.setEstoque(0);
		builder.filme.setNome("Nana");
		builder.filme.setPrecoLocacao(10.0);		
		return builder;
	}
	
	public Filme agora() {
		return filme;
	}

}
