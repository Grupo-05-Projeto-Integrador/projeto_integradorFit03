package com.fitness.aplicativofitness.model;


import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "tb_categorias")
public class Categoria {

		@Id //CHAVE PRIVARIA 
		@GeneratedValue (strategy = GenerationType.IDENTITY) //AUTOICRMENT
		private Long id;
		
		@NotNull(message = "O Atributo Descrição é obrigatorio") //NÃO DEIXA O CAMPO SER NULO
		private String tipo;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getTipo() {
			return tipo;
		}

		public void setTipo(String tipo) {
			this.tipo = tipo;
		}
		

}