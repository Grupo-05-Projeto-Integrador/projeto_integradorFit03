package com.fitness.aplicativofitness.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fitness.aplicativofitness.model.Aluno;
import com.fitness.aplicativofitness.repository.AlunoRepository;
import com.fitness.aplicativofitness.security.JwtService;
import com.generation.blogpessoal.model.UsuarioLogin;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public Optional<Aluno> cadastrarAluno(Aluno aluno) {
        if (alunoRepository.findByUsuario(aluno.getUsuario()).isPresent()) {
            return Optional.empty();
        }
        aluno.setSenha(criptografarSenha(aluno.getSenha()));
        return Optional.of(alunoRepository.save(aluno));
    }

    public Optional<Aluno> atualizarAluno(Aluno aluno) {
        if (alunoRepository.findById(aluno.getId()).isPresent()) {
            Optional<Aluno> buscaAluno = alunoRepository.findByUsuario(aluno.getUsuario());
            if ((buscaAluno.isPresent()) && (buscaAluno.get().getId() != aluno.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);
            }
            aluno.setSenha(criptografarSenha(aluno.getSenha()));
            return Optional.of(alunoRepository.save(aluno));
        }
        return Optional.empty();
    }

    public Optional<UsuarioLogin> autenticarAluno(Optional<UsuarioLogin> usuarioLogin) {
        var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha());
        Authentication authentication = authenticationManager.authenticate(credenciais);
        if (authentication.isAuthenticated()) {
            Optional<Aluno> aluno = alunoRepository.findByUsuario(usuarioLogin.get().getUsuario());
            if (aluno.isPresent()) {
                usuarioLogin.get().setId(aluno.get().getId());
                usuarioLogin.get().setNome(aluno.get().getNome());
                usuarioLogin.get().setFoto(aluno.get().getFoto());
                usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getUsuario()));
                usuarioLogin.get().setSenha("");
                return usuarioLogin;
            }
        }
        return Optional.empty();
    }

    public Optional<Aluno> calcularImc(Long id) {
        return alunoRepository.findById(id).map(aluno -> {
            if (aluno.getAltura() == null || aluno.getPeso() == null || aluno.getAltura() <= 0 || aluno.getPeso() <= 0) {
                throw new IllegalArgumentException("Altura e peso devem ser valores positivos e não nulos.");
            }
            double imc = aluno.getPeso() / (aluno.getAltura() * aluno.getAltura());
            aluno.setImc(imc);
            aluno.setCategoriaImc(definirCategoriaImc(imc));
            alunoRepository.save(aluno);
            return aluno;
        });
    }

    private String definirCategoriaImc(double imc) {
        if (imc < 18.5) {
            return "Baixo peso";
        } else if (imc < 24.9) {
            return "Peso normal";
        } else if (imc < 29.9) {
            return "Sobrepeso";
        } else if (imc < 34.9) {
            return "Obesidade grau 1";
        } else if (imc < 39.9) {
            return "Obesidade grau 2";
        } else {
            return "Obesidade grau 3";
        }
    }

    private String criptografarSenha(String senha) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(senha);
    }

    private String gerarToken(String usuario) {
        return "Bearer " + jwtService.generateToken(usuario);
    }
}
