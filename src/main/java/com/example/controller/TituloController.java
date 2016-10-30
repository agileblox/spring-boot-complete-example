package com.example.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.model.StatusTitulo;
import com.example.model.Titulo;
import com.example.repository.TituloRepository;
import com.example.repository.filter.TituloFilter;
import com.example.service.CadastroTituloService;

@Controller
@RequestMapping(value = "/titulos")
public class TituloController {
	
	@Autowired
	private TituloRepository tituloRepository;
	
	@Autowired
	private CadastroTituloService cadastroTituloService;
	
	private static final String CADASTRO_VIEW = "CadastroTitulo";

	@RequestMapping(value = "/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		//passar obj titulo para o form para ser validado depois.
		mv.addObject(new Titulo());
		return mv;
	}
	
	//use RedirectAttributes because when use redirect,
	//is a new request and in new request the datas about ModelAndView are lost
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, Errors erros, RedirectAttributes attributes) {
		try {
			if(erros.hasErrors()){
				return CADASTRO_VIEW;
			}
			cadastroTituloService.salvar(titulo);
			attributes.addFlashAttribute("mensagem", "Titulo salvo com sucesso!");
			return "redirect:/titulos/novo";
		} catch (IllegalArgumentException ex) {
			erros.rejectValue("dataVencimento",null, ex.getMessage());
			return CADASTRO_VIEW;
		}
	}
	
	@RequestMapping
//	public ModelAndView pesquisar(@RequestParam(defaultValue="%") String descricao){//ou colocar required ="false" no requestParam
	public ModelAndView pesquisar(@ModelAttribute("filtro")TituloFilter filtro,@RequestParam(defaultValue = "0", required = false) int page){	
		//List<Titulo> listTitulos = tituloRepository.findAll();
		PageRequest pageRequest = new PageRequest(page, 5);
		Page<Titulo> listTitulos = cadastroTituloService.filtrar(filtro,pageRequest);
		ModelAndView mv = new ModelAndView("PesquisaTitulos");
		mv.addObject("list",listTitulos);
		return mv;
	}
	
	//ou no lugar do findOne usar o Titulo como param passando código assim o spring faz o findOne por de baixo dos panos
	//exemple: @PathVariable("codigo")Titulo titulo
	@RequestMapping("{codigo}")
	public ModelAndView edit(@PathVariable Long codigo){
		Titulo titulo = tituloRepository.findOne(codigo);
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(titulo);
		return mv;
		
	}

	@RequestMapping(value="{codigo}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		cadastroTituloService.excluir(codigo);
		
		attributes.addFlashAttribute("mensagem", "Título excluído com sucesso!");
		return "redirect:/titulos";
	}
	
	//@ResponseBody diz que o metodo nao vai retornar uma nova tela e sim uma msg simples
	@RequestMapping(value="/{codigo}/receber", method = RequestMethod.PUT)
	public @ResponseBody String receber(@PathVariable Long codigo){
		return cadastroTituloService.receber(codigo);
	}
	
	@ModelAttribute("statusTitulo")
	public List<StatusTitulo> statusTitulo(){
		return Arrays.asList(StatusTitulo.values());
	}
}
