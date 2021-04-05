package br.df.lseabra.rest.tests.refac;

import static io.restassured.RestAssured.given;

import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Test;

import br.df.lseabra.rest.tests.Movimentacao;
import br.df.lseabra.rest.tests.SistemaTest;

public class MovimentacaoTest extends SistemaTest {
	@Test
	public void deveInserirMovimentacaooComSucesso() {
		//Login na API
		//Receber Token
		String token = fazerLogin();
		
		Movimentacao movimentacao = getMovimentacaoValida();
		Integer id = buscarID(token, "Conta para movimentacoes");
		movimentacao.setConta_id(id);
	
		//Criar conta
		given()
			.header("Authorization", "JWT " + token)
			.body(movimentacao)
		.when()
			.post("/transacoes")
		.then()
	 		.statusCode(201);
		
	}
	
	@Test
	public void deveValidarCamposObrigatorios() {
		//Login na API
		//Receber Token
		String token = fazerLogin();
		
		Movimentacao movimentacao = new Movimentacao();
		
		//Criar conta
		given()
			.header("Authorization", "JWT " + token)
			.body(movimentacao)
		.when()
			.post("/transacoes")
		.then()
			.log().all()
	 		.statusCode(400)
	 		.body("$", Matchers.hasSize(8))
			.body("msg", Matchers.hasItems("Data da Movimentação é obrigatório",
					"Data do pagamento é obrigatório",
					"Descrição é obrigatório",
					"Interessado é obrigatório",
					"Valor é obrigatório",
					"Valor deve ser um número",
					"Conta é obrigatório",
					"Situação é obrigatório"))
		;
	}
	@Test
	public void naoDeveInserirMovimentacaoComDataFutura() {
		//Login na API
		//Receber Token
		String token = fazerLogin();
		
		Movimentacao movimentacao = getMovimentacaoValida();
		movimentacao.setData_transacao("01/01/2022");
		
		//Criar conta
		given()
			.header("Authorization", "JWT " + token)
			.body(movimentacao)
		.when()
			.post("/transacoes")
		.then()
	 		.statusCode(400)
	 		.body("msg[0]", Matchers.is("Data da Movimentação deve ser menor ou igual à data atual"));
		
	}
	
	@Test
	public void naoDeveRemoverContaComMovimentacao() {
		//Login na API
		//Receber Token
		String token  = fazerLogin();
		String antes = "Movimentacao de conta";
		Map<String, String> contaAntes = criarConta("nome", antes);
					
		//buscar id conta
		Integer ttt = buscarID(token, antes, contaAntes);
		
		//Deletar Conta
		given()
			.header("Authorization", "JWT " + token)
		.when()
			.delete("/contas/" + ttt)
		.then()
	 		.statusCode(500)
	 		;
		
	}

	
	@Test
	public void DeveRemoverMovimentacao() {
		//Login na API
		//Receber Token
		String token  = fazerLogin();
		
		String movimentacao = "Movimentacao para exclusao";
		
		//localizar elemento
		//buscar id conta
		Integer ttt = buscarIDMov(token, movimentacao);
				
		//Verificar saldo Conta
		given()
			.header("Authorization", "JWT " + token)
		.when()
			.delete("/transacoes/"+ ttt)
		.then()
	 		.statusCode(204)
	 		;
		
	}
	
}
