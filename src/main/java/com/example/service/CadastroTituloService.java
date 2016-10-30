package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.model.StatusTitulo;
import com.example.model.Titulo;
import com.example.repository.TituloRepository;
import com.example.repository.filter.TituloFilter;

@Service
public class CadastroTituloService {
	
	@Autowired
	private TituloRepository tituloRepository;
	
	public void salvar(Titulo titulo){
		try {
			tituloRepository.save(titulo);
		} catch (DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formato de data Inválido");
		}
	}
	
	public void excluir(Long codigo){
		tituloRepository.delete(codigo);
	}

	public String receber(Long codigo) {
		Titulo titulo = tituloRepository.findOne(codigo);
		titulo.setStatus(StatusTitulo.RECEBIDO);
		tituloRepository.save(titulo);
		
		return StatusTitulo.RECEBIDO.getDescricao();
		
	}
	
	public Page<Titulo> filtrar(TituloFilter filtro,PageRequest pageRequest){
		String descricao = filtro.getDescricao() == null ? "%" : filtro.getDescricao();
		return tituloRepository.findByDescricaoContainingIgnoreCase(descricao,pageRequest);
		
	}

}
