package com.fitness.aplicativofitness.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_alunos")
public class Aluno {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "O atributo Nome é obrigatório!")
	private String nome;

	@NotNull(message = "O atributo Email é obrigatório!")
	private String email;

	@Column(nullable = false)
	private Double altura;

	@Column(nullable = false)
	private Double peso;

	@Column
	private Double imc;

	@Column
	private String categoriaImc; // Novo atributo para armazenar a categoria do IMC
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "aluno", cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties("aluno")
	private List<Exercicio> exercicio;


	public List<Exercicio> getExercicio() {
		return exercicio;
	}

	public void setExercicio(List<Exercicio> exercicio) {
		this.exercicio = exercicio;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getAltura() {
		return altura;
	}

	public void setAltura(Double altura) {
		this.altura = altura;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public Double getImc() {
		return imc;
	}

	public void setImc(Double imc) {
		this.imc = imc;
	}

	public String getCategoriaImc() {
		return categoriaImc;
	}

	public void setCategoriaImc(String categoriaImc) {
		this.categoriaImc = categoriaImc;
	}
	
	

}