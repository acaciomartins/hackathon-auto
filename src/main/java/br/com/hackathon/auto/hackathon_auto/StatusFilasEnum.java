package br.com.hackathon.auto.hackathon_auto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author F0117677
 *
 */
public enum StatusFilasEnum {

	CALCULO_6(6, "TREINAMENTO.RQ"), 
	CALCULO_33(33, "TREINAMENTO.RQ"), 
	CALCULO_34(34, "TREINAMENTO.RQ"),
	CALCULO_55(55, "TREINAMENTO.RQ"),
	
	CONSISTENCIA_11(11, "FILA.CONSISTENCIA"), 
	CONSISTENCIA_31(31, "FILA.CONSISTENCIA"),
	CONSISTENCIA_32(32, "FILA.CONSISTENCIA"),
	CONSISTENCIA_35(35, "FILA.CONSISTENCIA"),
	CONSISTENCIA_36(36, "FILA.CONSISTENCIA"),

	FECHAMENTO_26(26, "FILA.FECHAMENTO"),
	
	CRITICA_7(7, "FILA.CRITICA");

	private Integer status;
	private String fila;

	private static Map<Integer, StatusFilasEnum> status_processos = new HashMap<Integer, StatusFilasEnum>();
	
	private StatusFilasEnum(final Integer status, String fila) {
		this.status = status;
		this.fila = fila;
	}
	
	static {
		for (StatusFilasEnum statusFilas : StatusFilasEnum.values()) {
			status_processos.put(statusFilas.getStatus(), statusFilas);
		}
	}

	public Integer getStatus() {
		return status;
	}
	


	public String getFila() {
		return fila;
	}

	public static Map<Integer, StatusFilasEnum> getStatus_processos() {
		return status_processos;
	}

	
	

}
