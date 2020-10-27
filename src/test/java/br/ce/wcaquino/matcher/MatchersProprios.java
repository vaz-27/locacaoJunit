package br.ce.wcaquino.matcher;

import java.util.Calendar;

public class MatchersProprios {
	
	public static DiaSemanaMatcher caiEm(Integer diaSemana) {
		return new DiaSemanaMatcher(diaSemana);
	}
	
	public static DiaSemanaMatcher caiNumaSegunda() {
		return new DiaSemanaMatcher(Calendar.MONDAY);
	}
	
	public static DataLocacaoMatcher ehHojeComDiferencadeDias (Integer qtdDias) {
		return new DataLocacaoMatcher(qtdDias);
	}

	public static DataLocacaoMatcher ehHoje() {
		return new DataLocacaoMatcher(0);
	}
	
}
