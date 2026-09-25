package com.biblioteca.auth;

import com.biblioteca.usuario.Perfil;

public record AuthResponse(
        Long id,
        String nome,
        String email,
        Perfil perfil,
        String token
) {}
