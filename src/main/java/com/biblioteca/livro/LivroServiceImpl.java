package com.biblioteca.livro;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class LivroServiceImpl implements LivroService{

    private static final List<LivroCadastroResponse> LIVROS = List.of(
            new LivroCadastroResponse(
                    1L,
                    "JAVA",
                    "Livro do Java",
                    "Machado de Assis",
                    1540
            ),
            new LivroCadastroResponse(
                    2L,
                    "Eng. Software",
                    "???",
                    "Marcus Miranda",
                    1999
            )
    );

    private final LivroRepository repository;
    private final LivroMapper mapper;

    public LivroServiceImpl(LivroRepository repository, LivroMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public LivroCadastroResponse criar(LivroCadastroRequest request) {
        Livro livro = mapper.paraEntidade(request);

        Livro livroSalvo = repository.save(livro);

        return mapper.paraResposta(livroSalvo);
    }

    @Override
    public List<LivroCadastroResponse> listarTodos() {
        return LIVROS;
    }

    @Override
    public Optional<LivroCadastroResponse> buscarPorId(Long id) {
        return LIVROS.stream()
                .filter(livro -> livro.id() == id)
                .findFirst();
    }
}
