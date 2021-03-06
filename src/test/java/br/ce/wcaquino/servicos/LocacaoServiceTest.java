package br.ce.wcaquino.servicos;


import static br.ce.wcaquino.buiders.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.buiders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matcher.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.matcher.MatchersProprios.ehHoje;
import static br.ce.wcaquino.matcher.MatchersProprios.ehHojeComDiferencadeDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.ce.wcaquino.buiders.FilmeBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.runners.ParallelRunner;
import br.ce.wcaquino.utils.DataUtils;

@RunWith(ParallelRunner.class)
public class LocacaoServiceTest {
	
	@InjectMocks @Spy
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
		System.out.println("iniciando2...");
	}
	
	@After
	public void fim() {
		System.out.println("finalizando2...");
	}
	
	@Test
	public void teste_alugaFilme() throws LocadoraException, FilmeSemEstoqueException {
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilmeComValor().agora());
		
		Mockito.doReturn(DataUtils.obterData(4, 11, 2020)).when(LS).obterData();

		//acao
		Locacao locacao = LS.alugarFilme(usuario, filmes);
				
		//validacao
		error.checkThat(locacao.getValor(),is(equalTo(10.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(4, 11, 2020)), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(5, 11, 2020)), is(true));
	
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
	public void teste_naoDevolverFilmeNoDomingo() throws Exception {	
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilmeComValor().agora());
		
		Mockito.doReturn(DataUtils.obterData(31, 10, 2020)).when(LS).obterData();
		
		//acao
		Locacao retorno = LS.alugarFilme(usuario, filmes);
		
		//verificacao		
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
	}
	
	@Test
	public void naoAlugaFilmeParaNegativadoSPC() throws Exception {
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
	
	@Test
	public void tratarErrosSPC() throws Exception {
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes  = Arrays.asList(FilmeBuilder.umFilmeComValor().agora());
		
		when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catastrofica"));
		
		//verificacao
		exception.expect(LocadoraException.class);
		exception.expectMessage("Problemas com SPC,tente novamente");

		//acao
		LS.alugarFilme(usuario, filmes);		
	}
	
	@Test
	public void prorrogaLocacao() {
		//cenario
		Locacao locacao = umLocacao().agora();
		
		//acao
		LS.prorrogarLocacao(locacao, 3);
		
		//verificacao
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).salvar(argCapt.capture());
		Locacao locacaoRetornada = argCapt.getValue();
		
		error.checkThat(locacaoRetornada.getValor(),is(30.0));
		error.checkThat(locacaoRetornada.getDataLocacao(),ehHoje());
		error.checkThat(locacaoRetornada.getDataRetorno(),ehHojeComDiferencadeDias(3));
	}
	
	
	@Test
	public void calculaValorLocacao() throws Exception {
		//cenario
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilmeComValor().agora());
				
		//acao
		Class<LocacaoService> clazz = LocacaoService.class;
		Method metodo = clazz.getDeclaredMethod("calcularValorLocacao", List.class);
		metodo.setAccessible(true);
		Double valor = (Double) metodo.invoke(LS, filmes);
				
		//verificacao
		Assert.assertThat(valor, is(10.0));

	}
	
	
}

