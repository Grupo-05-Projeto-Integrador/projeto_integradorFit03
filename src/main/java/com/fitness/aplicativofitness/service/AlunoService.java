package com.fitness.aplicativofitness.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitness.aplicativofitness.model.Aluno;
import com.fitness.aplicativofitness.repository.AlunoRepository;

@Service
public class AlunoService {

	@Autowired
	private AlunoRepository alunoRepository;

	/**
	 * Calcula o IMC de um aluno com base no ID e define a categoria do IMC.
	 *
	 * @param id ID do aluno.
	 * @return Optional contendo o aluno com IMC e categoria atualizados ou vazio se
	 *         o aluno não existir.
	 */
	public Optional<Aluno> calcularImc(Long id) {
		return alunoRepository.findById(id).map(aluno -> {
			// Validação de dados
			if (aluno.getAltura() == null || aluno.getPeso() == null || aluno.getAltura() <= 0
					|| aluno.getPeso() <= 0) {
				throw new IllegalArgumentException("Altura e peso devem ser valores positivos e não nulos.");
			}

			// Cálculo do IMC
			double imc = aluno.getPeso() / (aluno.getAltura() * aluno.getAltura());
			aluno.setImc(imc);

			// Define a categoria do IMC
			aluno.setCategoriaImc(definirCategoriaImc(imc));

			// Atualiza os dados do aluno no banco
			alunoRepository.save(aluno);

			return aluno;
		});
	}

	/**
	 * Determina a categoria do IMC com base no valor calculado.
	 *
	 * @param imc Valor do IMC.
	 * @return String representando a categoria do IMC.
	 */
	private String definirCategoriaImc(double imc) {
		if (imc < 18.5) {
			return "Baixo peso";
		} else if (imc >= 18.5 && imc < 24.9) {
			return "Peso normal";
		} else if (imc >= 25 && imc < 29.9) {
			return "Sobrepeso";
		} else if (imc >= 30 && imc < 34.9) {
			return "Obesidade grau 1";
		} else if (imc >= 35 && imc < 39.9) {
			return "Obesidade grau 2";
		} else {
			return "Obesidade grau 3";
		}
	}
}
