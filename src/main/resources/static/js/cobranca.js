$('#confirmacaoExclusaoModal').on('show.bs.modal', function(event){//toda vez que show modal acontecer executa a função
	var button = $(event.relatedTarget);//pega o botao clicado que disparou o evento
	var codigoTitulo= button.data('codigo');
	var descricaoTitulo = button.data('descricao');
	
	//código para pegar o cód do item da lista para excluir
	var modal = $(this);//pegar o modal e a partir dele pegar o form
	var form = modal.find('form');	
	var action = form.data('url-base');//pegando a string action do form
	
	if(!action.endsWith('/')){
		action += '/';
	}
	form.attr('action', action + codigoTitulo);
	
	modal.find('.modal-body').html('Tem certeza que deseja excluir o título <strong>' + descricaoTitulo + '</strong>?');
});

//Toda vez que a pagina recarregar, executar algumas funcionalidades
$(function(){
	$('[rel="tooltip"]').tooltip();//procure os componentes rel-tooltip e faça o tooltip
	$('.js-currency').maskMoney({format: "DD/MM/yy",language: "pt-BR", decimal: ',', thousands: '.',alloZero: false});
	$('.js-atualizar-status').on('click', function(event){
		event.preventDefault();//nao fazer o comportamento default do link
		var botaoReceber = $(event.currentTarget);
		var urlReceber = botaoReceber.attr('href');
		//console.log('urlReceber',urlReceber);
		
		var response = $.ajax({
			url: urlReceber,
			type: 'PUT'
		});
		
		response.done(function(e){
			var codigoTitulo = botaoReceber.data('codigo');
			$('[data-role=' + codigoTitulo + ']').html('<span class="label label-success">' + e + '</span>');
			botaoReceber.hide();
		});
		
		response.fail(function(e){
			console.log(e);
			alert('Erro recebendo cobrança');
		});
		
		
	});
});


