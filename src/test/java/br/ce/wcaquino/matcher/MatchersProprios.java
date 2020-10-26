package br.ce.wcaquino.matcher;

import java.util.Calendar;

import br.ce.wcaquino.matcher.DiaSemanaMatcher;

public class MatchersProprios {
	
	public static DiaSemanaMatcher caiEm(Integer diaSemana) {
		return new DiaSemanaMatcher(diaSemana);
	}
	
	public static DiaSemanaMatcher caiNumaSegunda() {
		return new DiaSemanaMatcher(Calendar.MONDAY);
	}

}
