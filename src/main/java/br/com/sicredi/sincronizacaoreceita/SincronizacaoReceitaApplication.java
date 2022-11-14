package br.com.sicredi.sincronizacaoreceita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.sicredi.sincronizacaoreceita.service.SincronizacaoService;
import br.com.sicredi.sincronizacaoreceita.utils.Utils;

@SpringBootApplication
public class SincronizacaoReceitaApplication {


	public static void main(String[] args) {
	    SpringApplication.run(SincronizacaoReceitaApplication.class, args);
		if (args.length > 0) {
			String[] caminho;
			caminho = args[0].toString().split(",");
			// Verifica se e dia util
			if (Utils.checarFDS()) {
				SincronizacaoService sincronizacaoService = new SincronizacaoService();
				sincronizacaoService.processaArquivo(caminho[0]);
			}
		} else {
			System.out.println("Informe o caminho do arquivo .csv");
		}
		System.exit(0);
	}
	

	
	
}
