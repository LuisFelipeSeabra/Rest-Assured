package br.df.lseabra.rest.tests;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;


import br.df.lseabra.rest.core.BaseTest;



public class SistemaTest extends BaseTest {
		
	public Movimentacao getMovimentacaoValida() {
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setConta_id(510707);
		movimentacao.setDescricao("Descrição");
		movimentacao.setEnvolvido("envolvido");
		movimentacao.setTipo("REC");
		movimentacao.setData_transacao("01/01/2009");
		movimentacao.setData_pagamento("10/05/2010");
		movimentacao.setValor(100f);
		movimentacao.setStatus(true);
		return movimentacao;
		
	}
	
	public static String fazerLogin() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "felipeseabra2405@gmail.com");
		login.put("senha", "felipe");
		
		String token = given()
				.body(login)
				.contentType(APP_CONTENT_TYPE)
			.when()
				.post("/signin")
			.then()
			 	.log().all()
			 	.statusCode(200)
			 	.extract().path("token");
		
		return token;
	}
	
	public Map<String,String > criarConta(String key,String value ) {
		Map<String, String> conta = new HashMap<String, String>();
		conta.put(key, value);
		return conta;
	}

	public Integer buscarID(String token,String antes, Map<String, String> contaAntes ) {
		Integer ttt = given()
		.header("Authorization", "JWT " + token)
			.body(contaAntes)
		.when()
			.get("/contas")
		.then()
	 		.statusCode(200)
	 		.extract().path("find{it.nome == '"+antes+"'}.id");
	 	
		return ttt;
	}
	
	public Integer buscarID(String token,String antes) {
		Integer ttt = given()
		.header("Authorization", "JWT " + token)
		.when()
			.get("/contas")
		.then()
	 		.statusCode(200)
	 		.extract().path("find{it.nome == '"+antes+"'}.id");
	 	
		return ttt;
	}
	
	public Integer buscarIDMov(String token,String desc) {
		Integer ttt = given()
		.header("Authorization", "JWT " + token)
		.when()
			.get("/transacoes")
		.then()
	 		.statusCode(200)
	 		.extract().path("find{it.descricao == '"+desc+"'}.id");
	 	
		return ttt;
	}
}
