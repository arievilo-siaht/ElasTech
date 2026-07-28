package br.com.elastech.ms_publicacoes.service;

import br.com.elastech.ms_publicacoes.dto.request.CriarComentarioRequest;
import br.com.elastech.ms_publicacoes.dto.request.EditarComentarioRequest;
import br.com.elastech.ms_publicacoes.dto.request.ExcluirComentarioRequest;
import br.com.elastech.ms_publicacoes.dto.response.ComentarioResponse;
import br.com.elastech.ms_publicacoes.dto.response.CriarComentarioResponse;
import br.com.elastech.ms_publicacoes.enums.ErrorEnum;
import br.com.elastech.ms_publicacoes.enums.StatusPublicacao;
import br.com.elastech.ms_publicacoes.exception.BaseException;
import br.com.elastech.ms_publicacoes.mapper.requestMapper.CriarComentarioRequestMapper;
import br.com.elastech.ms_publicacoes.mapper.responseMapper.ComentarioResponseMapper;
import br.com.elastech.ms_publicacoes.mapper.responseMapper.CriarComentarioResponseMapper;
import br.com.elastech.ms_publicacoes.model.Comentario;
import br.com.elastech.ms_publicacoes.model.Publicacao;
import br.com.elastech.ms_publicacoes.repository.ComentarioRepository;
import br.com.elastech.ms_publicacoes.repository.PublicacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComentarioServiceTest {
    @InjectMocks
    private ComentarioService service;

    @Mock
    private ComentarioRepository comentarioRepository;

    @Mock
    private PublicacaoRepository publicacaoRepository;

    @Mock
    private CriarComentarioRequestMapper criarComentarioRequestMapper;

    @Mock
    private CriarComentarioResponseMapper criarComentarioResponseMapper;

    @Mock
    private ComentarioResponseMapper comentarioResponseMapper;

    private Publicacao publicacao;
    private Comentario comentario;

    @BeforeEach
    void setUp() {
        publicacao = new Publicacao();
        publicacao.setId(1);
        publicacao.setStatus(StatusPublicacao.PUBLICADA);

        comentario = new Comentario();
        comentario.setId(1);
        comentario.setUsuarioId(1);
        comentario.setConteudo("Comentário");
        comentario.setAtivo(true);
        comentario.setPublicacao(publicacao);
    }

    @Test
    void deveCriarComentario() {

        CriarComentarioRequest request =
                new CriarComentarioRequest(
                        1,
                        "Comentário"
                );

        CriarComentarioResponse response = CriarComentarioResponse.builder().usuarioId(1).build();

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        when(criarComentarioRequestMapper.map(request))
                .thenReturn(comentario);

        when(criarComentarioResponseMapper.map(comentario))
                .thenReturn(response);

        CriarComentarioResponse resultado =
                service.criar(request, 1);

        verify(comentarioRepository).save(comentario);

        assertThat(resultado).isEqualTo(response);
    }

    @Test
    void deveLancarExcecaoQuandoConteudoForVazio() {

        CriarComentarioRequest request =
                new CriarComentarioRequest(
                        1,
                        ""
                );

        assertThatThrownBy(() ->
                service.criar(request,1))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.CONTEUDO_INVALIDO.getErrorMessage());

        verifyNoInteractions(comentarioRepository);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioForNulo() {

        CriarComentarioRequest request =
                new CriarComentarioRequest(
                        null,
                        "teste"
                );

        assertThatThrownBy(() ->
                service.criar(request,1))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.USUARIO_NAO_AUTORIZADO.getErrorMessage());
    }

    @Test
    void deveLancarExcecaoQuandoPublicacaoNaoExiste() {

        CriarComentarioRequest request =
                new CriarComentarioRequest(
                        1,
                        "teste"
                );

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.criar(request,1))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.PUBLICACAO_NAO_ENCONTRADA.getErrorMessage());
    }

    @Test
    void deveLancarExcecaoQuandoPublicacaoArquivada() {

        publicacao.setStatus(StatusPublicacao.ARQUIVADA);

        CriarComentarioRequest request =
                new CriarComentarioRequest(
                        1,
                        "teste"
                );

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        assertThatThrownBy(() ->
                service.criar(request,1))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.PUBLICACAO_NAO_ENCONTRADA.getErrorMessage());
    }

    @Test
    void deveListarComentariosDaPublicacao() {

        ComentarioResponse response = ComentarioResponse.builder().build();

                when(publicacaoRepository.findById(1))
                        .thenReturn(Optional.of(publicacao));

        when(comentarioRepository
                .findByPublicacaoIdAndAtivoTrueOrderByDataCriacaoAsc(1))
                .thenReturn(List.of(comentario));

        when(comentarioResponseMapper.map(comentario))
                .thenReturn(response);

        List<ComentarioResponse> resultado =
                service.listarComentariosPorPublicacao(1);

        assertThat(resultado).hasSize(1);
    }

    @Test
    void deveLancarExcecaoAoListarComentariosDePublicacaoInexistente() {

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.listarComentariosPorPublicacao(1))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.PUBLICACAO_NAO_ENCONTRADA.getErrorMessage());
    }

    @Test
    void deveEditarComentario() {

        EditarComentarioRequest request =
                new EditarComentarioRequest(
                        1,
                        1,
                        1,
                        "Novo comentário"
                );

        ComentarioResponse response = ComentarioResponse.builder().build();

                when(publicacaoRepository.findById(1))
                        .thenReturn(Optional.of(publicacao));

        when(comentarioRepository.findById(1))
                .thenReturn(Optional.of(comentario));

        when(comentarioResponseMapper.map(comentario))
                .thenReturn(response);

        ComentarioResponse resultado =
                service.editar(request);

        assertThat(comentario.getConteudo())
                .isEqualTo("Novo comentário");

        assertThat(resultado)
                .isEqualTo(response);
    }

    @Test
    void deveLancarExcecaoQuandoEditarComConteudoVazio() {

        EditarComentarioRequest request =
                new EditarComentarioRequest(
                        1,
                        1,
                        1,
                        ""
                );

        assertThatThrownBy(() ->
                service.editar(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.CONTEUDO_INVALIDO.getErrorMessage());
    }

    @Test
    void deveLancarExcecaoQuandoComentarioNaoExiste() {

        EditarComentarioRequest request =
                new EditarComentarioRequest(
                        1,
                        1,
                        1,
                        "novo"
                );

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        when(comentarioRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.editar(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.COMENTARIO_NAO_ENCONTRADO.getErrorMessage());
    }

    @Test
    void deveLancarExcecaoQuandoComentarioEstiverInativo() {

        comentario.setAtivo(false);

        EditarComentarioRequest request =
                new EditarComentarioRequest(
                        1,
                        1,
                        1,
                        "novo"
                );

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        when(comentarioRepository.findById(1))
                .thenReturn(Optional.of(comentario));

        assertThatThrownBy(() ->
                service.editar(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.COMENTARIO_NAO_ENCONTRADO.getErrorMessage());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForAutorDoComentario() {

        comentario.setUsuarioId(99);

        EditarComentarioRequest request =
                new EditarComentarioRequest(
                        1,
                        1,
                        1,
                        "novo"
                );

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        when(comentarioRepository.findById(1))
                .thenReturn(Optional.of(comentario));

        assertThatThrownBy(() ->
                service.editar(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.USUARIO_NAO_AUTORIZADO.getErrorMessage());
    }

    @Test
    void deveExcluirComentario() {

        ExcluirComentarioRequest request =
                new ExcluirComentarioRequest(
                        1,
                        1,
                        1
                );

        when(comentarioRepository.findById(1))
                .thenReturn(Optional.of(comentario));

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        service.excluir(request);

        assertThat(comentario.isAtivo()).isFalse();
    }

    @Test
    void deveLancarExcecaoAoExcluirComentarioDeOutroUsuario() {

        comentario.setUsuarioId(2);

        ExcluirComentarioRequest request =
                new ExcluirComentarioRequest(
                        1,
                        1,
                        1
                );

        when(comentarioRepository.findById(1))
                .thenReturn(Optional.of(comentario));

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        assertThatThrownBy(() ->
                service.excluir(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.USUARIO_NAO_AUTORIZADO.getErrorMessage());
    }

    @Test
    void deveLancarExcecaoQuandoComentarioJaEstiverInativo() {

        comentario.setAtivo(false);

        ExcluirComentarioRequest request =
                new ExcluirComentarioRequest(
                        1,
                        1,
                        1
                );

        when(comentarioRepository.findById(1))
                .thenReturn(Optional.of(comentario));

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        assertThatThrownBy(() ->
                service.excluir(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.COMENTARIO_NAO_ENCONTRADO.getErrorMessage());
    }
}
