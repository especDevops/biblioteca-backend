package com.biblioteca.livro;

import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LivroMapper {

    Livro paraEntidade(LivroCadastroRequest request);

    LivroCadastroResponse paraResposta(Livro livro);

    List<LivroCadastroResponse> paraListaResposta(List<Livro> livros);
}
