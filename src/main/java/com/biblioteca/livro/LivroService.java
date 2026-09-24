package com.biblioteca.livro;

import java.util.List;

public interface LivroService {

    LivroCadastroResponse criar(LivroCadastroRequest request);

    List<LivroCadastroResponse> listarTodos();

    void deletar(Long id);
}
