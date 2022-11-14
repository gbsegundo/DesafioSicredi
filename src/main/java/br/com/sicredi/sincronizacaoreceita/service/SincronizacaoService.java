package br.com.sicredi.sincronizacaoreceita.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.opencsv.CSVWriter;

import br.com.sicredi.sincronizacaoreceita.entity.Conta;
import br.com.sicredi.sincronizacaoreceita.entity.ContaResultado;
import br.com.sicredi.sincronizacaoreceita.utils.Utils;

@Service
public class SincronizacaoService {

	private static Logger log = LoggerFactory.getLogger(SincronizacaoService.class);

	public ReceitaService receitaService = new ReceitaService();

	/**
	 * 
	 * @param csvFileName
	 */
	public void processaArquivo(String fileCSV) {
		List<ContaResultado> listContaResultado = new ArrayList<ContaResultado>();
		try {
			List<Conta> listContas = leitorArquivoCSV(fileCSV);
			for (Conta conta : listContas) {
				listContaResultado.add(validacaoReceitaFederal(conta));
			}
			geraCSVRespostaReceitaFederal(listContaResultado);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param csvFileName
	 * @return
	 * @throws IOException
	 */
	public List<Conta> leitorArquivoCSV(String fileCSV) throws Exception {
		List<Conta> listaContas = new ArrayList<Conta>();
		log.info("Iniciando a leitura do arquivo .csv...");
		CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader();
		try (Reader reader = Files.newBufferedReader(Paths.get(fileCSV));
				CSVParser linhas = new CSVParser(reader, format);) {
			for (CSVRecord linha : linhas) {
				Conta conta = new Conta();
				conta.setAgencia(linha.get(0));
				conta.setConta(Utils.formatadorConta(linha.get(1)));
				conta.setSaldo(Utils.formatadorSaldo(linha.get(2)));
				conta.setStatus(linha.get(3));
				listaContas.add(conta);
			}
		}
		log.info("Aguarde finalizando leitura do arquivo .csv...");
		return listaContas;
	}

	/**
	 * 
	 * @param conta
	 * @throws InterruptedException
	 */
	public ContaResultado validacaoReceitaFederal(Conta conta) throws Exception {

		// Metodo responsável por sincronizar as contas com a Receita Federal
		boolean flag = receitaService.atualizarConta(conta.getAgencia(), conta.getConta(),
				Double.parseDouble(conta.getSaldo()), conta.getStatus());

		ContaResultado resultado = new ContaResultado();
		resultado.setAgencia(conta.getAgencia());
		resultado.setConta(conta.getConta());
		resultado.setSaldo(conta.getSaldo());
		resultado.setStatus(conta.getStatus());
		resultado.setStatusRetornoReceita(flag);

		return resultado;
	}

	/**
	 * 
	 * @param contasInstanciadas
	 */
	public void geraCSVRespostaReceitaFederal(List<ContaResultado> contasInstanciadas) {
		log.info("Gerando arquivo de retorno .csv...");
		List<String[]> linhas = new ArrayList<>();
		for (ContaResultado linha : contasInstanciadas) {
			linhas.add(new String[] { linha.getAgencia(), Utils.formatadorConta(linha.getConta()), linha.getSaldo(),
					linha.getStatus(), Utils.resultado(linha.getStatusRetornoReceita()) });
		}
		String[] cabecalho = { "agencia", "conta", "saldo", "status", "resultado" };
		try {
			FileWriter escrever = new FileWriter(new File("arquivo_receita_resposta.csv"));

			@SuppressWarnings("resource")
			CSVWriter csvWriter = new CSVWriter(escrever, ';', CSVWriter.NO_QUOTE_CHARACTER,
					CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
			csvWriter.writeNext(cabecalho);
			csvWriter.writeAll(linhas);
			csvWriter.flush();
			escrever.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		log.info("Finalizado arquivo de retorno .csv...");
	}

}
