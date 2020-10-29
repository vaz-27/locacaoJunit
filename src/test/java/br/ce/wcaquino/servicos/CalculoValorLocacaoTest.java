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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.buiders.FilmeBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {
	
	@InjectMocks
	private LocacaoService LS;
	
	@Mock
	LocacaoDAO dao = Mockito.mock(LocacaoDAO.class);
	@Mock
	SPCService spc = Mockito.mock(SPCService.class);

	@Parameter
	public List<Filme> filmes;
	
	@Parameter(value=1)
	public Double valorLocacao;
	
	@Parameter(value=2)
	public String cenario;
	
	@Before
	public void cenario() {
		MockitoAnnotations.initMocks(this);
	}
	
	private static Filme filme1 = FilmeBuilder.umFilmeComValor().agora();
	private static Filme filme2 = FilmeBuilder.umFilmeComValor().agora();
	private static Filme filme3 = FilmeBuilder.umFilmeComValor().agora();
	private static Filme filme4 = FilmeBuilder.umFilmeComValor().agora();
	private static Filme filme5 = FilmeBuilder.umFilmeComValor().agora();
	private static Filme filme6 = FilmeBuilder.umFilmeComValor().agora();
	private static Filme filme7 = FilmeBuilder.umFilmeComValor().agora();
	
	
	@Parameters(name ="{2}")
	public static Collection<Object[]> getParametros(){
		return Arrays.asList(new Object[][] {
			{Arrays.asList(filme1, filme2), 20.0, "2 Filme: Sem desconto"},
			{Arrays.asList(filme1, filme2,filme3), 27.5, "3 Filme: 25%"},
			{Arrays.asList(filme1, filme2,filme3, filme4), 32.5, "4 Filme: 50%"},
			{Arrays.asList(filme1, filme2,filme3, filme4, filme5), 35.0, "5 Filme: 75%"},
			{Arrays.asList(filme1, filme2,filme3, filme4, filme5, filme6), 35.00, "6 Filme: 100%"},
			{Arrays.asList(filme1, filme2,filme3, filme4, filme5, filme6, filme7), 45.0, "7 Filme: Sem desconto"}
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
}
