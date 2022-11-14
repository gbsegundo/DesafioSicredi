package br.com.sicredi.sincronizacaoreceita.utils;

import java.util.Calendar;
import java.util.Date;

public class Utils {

	public static String formatadorConta(String conta) {
		String contaFormatada = conta.replace("-", "");
		return contaFormatada;
	}

	public static String formatadorSaldo(String saldo) {
		String saldoString = String.valueOf(saldo);
		String saldoFormatado = saldoString.replace(",", ".");
		return saldoFormatado;
	}

	public static String resultado(Boolean retorno) {
		return retorno ? "Valida" : "Invalida";
	}

	public static Boolean checarFDS() {
		Calendar data = Calendar.getInstance();
		data.setTime(new Date());

		if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			data.add(Calendar.DATE, 1);
			return false;
		}

		else if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			data.add(Calendar.DATE, 2);
			return false;
		} else {
			return true;
		}
	}
}
