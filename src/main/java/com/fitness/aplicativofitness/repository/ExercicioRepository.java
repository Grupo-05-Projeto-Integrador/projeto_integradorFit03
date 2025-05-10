package com.fitness.aplicativofitness.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.fitness.aplicativofitness.model.Exercicio;

public interface ExercicioRepository extends JpaRepository<Exercicio, Long>{
	
	public List <Exercicio> findAllByNomeContainingIgnoreCase(@Param("nome") String nome);

}
