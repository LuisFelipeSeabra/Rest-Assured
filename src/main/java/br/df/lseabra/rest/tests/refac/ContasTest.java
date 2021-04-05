package br.df.lseabra.rest.tests.refac;

import static io.restassured.RestAssured.given;

import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import br.df.lseabra.rest.tests.SistemaTest;

public class ContasTest extends SistemaTest {
		
	@BeforeClass
	public static void produzirMassa() {
		String token = fazerLogin();
			
		given()
			.header("Authorization", "JWT " + token)
		.when()
			.get("/reset")
		.then()
	 		.statusCode(200);
	}
	
	@Test
	public void naoDeveAcessarSemToken() {
		
		given()
		.when()
			.get("/contas")
		.then()
		 	.log().all()
		 	.statusCode(401);
		
	}
	
	@Test
	public void deveIncluirContaComToken() {
		//Login na API
		//Receber Token
		String token = fazerLogin();
		
		Map<String, String> conta = criarConta("nome", "nova contas");
		
		//Criar conta
		given()
		.header("Authorization", "JWT " + token)
			.body(conta)
		.when()
			.post("/contas")
		.then()
	 		.statusCode(201);
		
	}
	
	@Test
	public void deveAlterarContaComToken() {
		//Login na API
		//Receber Token
		String token = fazerLogin();
		
		String depois =  "conta alterada";
		Map<String, String> contaDepois = criarConta("nome", depois);
		String antes =  "Conta para alterar";
		Map<String, String> contaAntes = criarConta("nome", antes);		
		
		//buscar id conta
		Integer ttt = buscarID(token, antes, contaAntes);
		
		//Alterar conta
		given()
			.header("Authorization", "JWT " + token)
			.body(contaDepois)
		.when()
			.put("/contas/"+ ttt)
		.then()
 			.statusCode(200)
 			.body("nome", Matchers.is(depois));
		
	}
	
	@Test
	public void naoDeveIncluirContaComNomeRepetido() {
		//Login na API
		//Receber Token
		String token = fazerLogin();
		Map<String, String> conta = criarConta("nome", "Conta mesmo nome");
	
		//Criar conta
		given()
		.header("Authorization", "JWT " + token)
			.body(conta)
		.when()
			.post("/contas")
		.then()
	 		.statusCode(400)
	 		.body("error", Matchers.is("Já existe uma conta com esse nome!"))
			.log().all();
		
	}
	
	@Test
	public void DeveCalcularSaldoDaConta() {
		//Login na API
		//Receber Token
		String token  = fazerLogin();

		String antes = "Conta para saldo";
		
		//Verificar saldo Conta
		given()
			.header("Authorization", "JWT " + token)
		.when()
			.get("/saldo")
		.then()
	 		.statusCode(200)
	 		.body("find{it.conta == '"+antes+"'}.saldo", Matchers.is("534.00"))
	 		;
		
	}
	

}
