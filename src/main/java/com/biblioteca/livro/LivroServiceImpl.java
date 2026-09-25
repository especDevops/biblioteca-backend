package com.biblioteca.livro;

import java.util.List;
import java.util.Optional;

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
        return repository.findAll().stream()
                .map(mapper::paraResposta)
                .toList();
    }

    @Override
    public Optional<LivroCadastroResponse> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::paraResposta);
    }

    @Override
    public Optional<LivroCadastroResponse> atualizar(Long id, LivroCadastroRequest request) {
        return repository.findById(id).map(livro -> {
            mapper.atualizarEntidade(request, livro);
            return mapper.paraResposta(repository.save(livro));
        });
    }

    @Override
    public Optional<LivroCadastroResponse> atualizarParcialmente(Long id, LivroAtualizacaoRequest request) {
        return repository.findById(id).map(livro -> {
            if (request.titulo() != null) {
                livro.setTitulo(request.titulo());
            }
            if (request.descricao() != null) {
                livro.setDescricao(request.descricao());
            }
            if (request.autor() != null) {
                livro.setAutor(request.autor());
            }
            if (request.anoPublicacao() != null) {
                livro.setAnoPublicacao(request.anoPublicacao());
            }
            return mapper.paraResposta(repository.save(livro));
        });
    }

    @Override
    public boolean excluir(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
