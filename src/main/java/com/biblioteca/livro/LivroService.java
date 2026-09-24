package com.biblioteca.livro;

import java.util.List;
import java.util.Optional;

public interface LivroService {

    LivroCadastroResponse criar(LivroCadastroRequest request);

    List<LivroCadastroResponse> listarTodos();

    Optional<LivroCadastroResponse> buscarPorId(Long id);
}
