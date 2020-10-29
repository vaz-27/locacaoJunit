package br.ce.wcaquino.servicos;


import static br.ce.wcaquino.buiders.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.buiders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matcher.MatchersProprios.caiNumaSegunda;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.buiders.FilmeBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.matcher.MatchersProprios;
import br.ce.wcaquino.utils.DataUtils;


public class LocacaoServiceTest {
	
	@InjectMocks
	private LocacaoService LS;
	
	@Mock
	private LocacaoDAO dao;
	@Mock
	private SPCService spc;
	@Mock
	private EmailService emailService;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void cenario() {
		MockitoAnnotations.initMocks(this);
	}	
	
	@Test
	public void teste_alugaFilme() throws LocadoraException, FilmeSemEstoqueException {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilmeComValor().agora());

		//acao
		Locacao locacao = LS.alugarFilme(usuario, filmes);
				
		//validacao
		error.checkThat(locacao.getValor(),is(equalTo(10.0)));
		error.checkThat(locacao.getDataLocacao(), MatchersProprios.ehHoje());
		error.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeComDiferencadeDias(1));
	
	}
	
	@Test (expected = FilmeSemEstoqueException.class)
	public void teste_filmeSemEstoque() throws Exception {
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilmeSemEstoque().agora());

		//acao
		LS.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void teste_usuarioVazio() throws FilmeSemEstoqueException {
		//cenario
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilmeComValor().agora());

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
		Usuario usuario = umUsuario().agora();
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filmes vazio");

		//acao
		LS.alugarFilme(usuario, null);		
	}
	
	
	@Test
	public void teste_naoDevolverFilmeNoDomingo() throws LocadoraException, FilmeSemEstoqueException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilmeComValor().agora());
		
		//acao
		Locacao retorno = LS.alugarFilme(usuario, filmes);
		
		//verificacao		
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
	}
	
	@Test
	public void naoAlugaFilmeParaNegativadoSPC() throws FilmeSemEstoqueException {
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilmeComValor().agora());
		
		when(spc.possuiNegativacao(usuario)).thenReturn(true);
		
		//acao
		try {
			LS.alugarFilme(usuario, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuario Negativado"));
		}
	
		//verificacao
		verify(spc).possuiNegativacao(usuario);
	}
	
	@Test
	public void enviaEmailParaLocacoesAtrasadas() {
		//cenario
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Jojo").agora();
		Usuario usuario3 = umUsuario().comNome("Carlos").agora();
		List<Locacao> locacoes = Arrays.asList(
				umLocacao().atrasado().comUsuario(usuario).agora(),
				umLocacao().comUsuario(usuario2).agora(),
				umLocacao().atrasado().comUsuario(usuario3).agora());
		
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		//acao
		LS.notificarAtrasos();
		
		//verificacao
		verify(emailService).notificarAtraso(usuario);
		verify(emailService, never()).notificarAtraso(usuario2);
		verify(emailService).notificarAtraso(usuario3);
		verifyNoMoreInteractions(emailService);
	}
}

