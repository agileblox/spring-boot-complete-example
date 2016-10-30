package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Titulo;

public interface TituloRepository extends JpaRepository<Titulo, Long>{

	 public List<Titulo> findAllByOrderByCodigoDesc();
	 
	 //faz um like pela descrição
	 public Page<Titulo> findByDescricaoContainingIgnoreCase(String descricao,Pageable pageAble);

}
