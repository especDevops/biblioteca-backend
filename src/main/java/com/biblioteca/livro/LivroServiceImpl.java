package com.biblioteca.livro;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LivroServiceImpl implements LivroService{

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
        List<Livro> livros = repository.findAll();
        return mapper.paraListaResposta(livros);
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
