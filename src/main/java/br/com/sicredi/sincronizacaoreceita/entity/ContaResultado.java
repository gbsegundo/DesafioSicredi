package br.com.sicredi.sincronizacaoreceita.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContaResultado implements Serializable {

	private static final long serialVersionUID = -6433314638709193615L;

	private String agencia;

	private String conta;

	private String saldo;

	private String status;

	private Boolean statusRetornoReceita;

	
}
