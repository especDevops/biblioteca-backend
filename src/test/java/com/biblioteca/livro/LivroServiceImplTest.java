package com.biblioteca.livro;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LivroServiceImplTest {

    @Mock
    private LivroRepository repository;

    @Mock
    private LivroMapper mapper;

    @InjectMocks
    private LivroServiceImpl service;

    @Test
    void deveCriarLivroERetornarRespostaMapeada() {
        LivroCadastroRequest request = new LivroCadastroRequest(
                "Clean Code",
                "Praticas para codigo limpo",
                "Robert C. Martin",
                "Tecnologia",
                2008
        );
        Livro livro = new Livro(null, request.titulo(), request.descricao(), request.autor(), request.genero(), request.anoPublicacao());
        Livro livroSalvo = new Livro(1L, request.titulo(), request.descricao(), request.autor(), request.genero(), request.anoPublicacao());
        LivroCadastroResponse resposta = new LivroCadastroResponse(
                1L,
                request.titulo(),
                request.descricao(),
                request.autor(),
                request.genero(),
                request.anoPublicacao()
        );

        when(mapper.paraEntidade(request)).thenReturn(livro);
        when(repository.save(livro)).thenReturn(livroSalvo);
        when(mapper.paraResposta(livroSalvo)).thenReturn(resposta);

        LivroCadastroResponse resultado = service.criar(request);

        assertThat(resultado).isEqualTo(resposta);
        verify(mapper).paraEntidade(request);
        verify(repository).save(livro);
        verify(mapper).paraResposta(livroSalvo);
    }

    @Test
    void deveListarTodosOsLivros() {
        Livro livro = new Livro(1L, "Clean Code", "Descricao", "Robert C. Martin", "Tecnologia", 2008);
        LivroCadastroResponse resposta = new LivroCadastroResponse(
                1L,
                "Clean Code",
                "Descricao",
                "Robert C. Martin",
                "Tecnologia",
                2008
        );
        List<Livro> livros = List.of(livro);
        List<LivroCadastroResponse> respostas = List.of(resposta);

        when(repository.findAll()).thenReturn(livros);
        when(mapper.paraResposta(livro)).thenReturn(resposta);

        List<LivroCadastroResponse> resultado = service.listarTodos();

        assertThat(resultado).isEqualTo(respostas);
        verify(repository).findAll();
        verify(mapper).paraResposta(livro);
    }

    @Test
    void deveDeletarLivroPorId() {
        Long id = 1L;

        when(repository.existsById(id)).thenReturn(true);

        boolean resultado = service.excluir(id);

        assertThat(resultado).isTrue();
        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }
}
