/*
 * LUIZ EDUARDO PEREIRA - 0021619
 * MARCOS MARTINS VIEIRA - 0026756
 * 07/11/2017
*/

import java.util.List;

public class Main {

	public static void main(String[] args){
		
		AFE m = new AFE();
		AFE m1 = new AFE();
		AFE mm = new AFE();
		
		m1.load("Exemplo01.jff");
		m.load("Exemplo02.jff");
	
		List<String> eqv;
		
		eqv = m.equivalents();
		System.out.println("Equivalentes:" + eqv);
		mm = m.conv2MINIMUM();
		mm.save("Exemplo02Minimizado.jff");
		
		if (m.equivalents(m1)){
			System.out.println("Equivalente");
		}
		else {
			System.out.println("Não Equivalente");
		}
		
		AFE m2 = new AFE();
		AFE m3 = new AFE();
		AFE m4 = new AFE();
		AFE m5 = new AFE();
		AFE m6 = new AFE();
		AFE m7 = new AFE();
		
		m2.load("Exemplo01.jff");
		m3.load("Exemplo02.jff");
		m4 = m2.complement();
		m5 = m2.union(m3) ;
		m6 = m2.intersection(m3) ;
		m7 = m2.difference(m3);
		
		m4.save("Complemento-T1.jff");
		m5.save("Uniao-T1T2.jff");
		m6.save("Intersecao-T1T2.jff");
		m7.save("Diferenca-T1T2.jff");
		
		Estado estado;
		
		if (m.accept("aaabbbaa")){
			System.out.println("Aceita");
		}
		else{
			System.out.println("Não Aceita");
		}
		
		estado = m.getInicial(0);
		estado = m.move(estado, "aaab");
		if (m.verificaFinal(estado.getId())){
			System.out.println("Aceita");
		}
		else {
			System.out.println("Não Aceita");
		}
		
		m.addState(10, "q10", false, true);
		m.addTransition(1, 2, "c") ;
		m.deleteState(3);
		m.deleteTransition(1,4,"a") ;
		
		m.save("Exemplo2Modificado");
		
		AFE afe = new AFE();
		afe.load("AFE.jff");
	
		afe.conv2AFV();
		System.out.println(afe.afv());
		afe.conv2AFN();
		System.out.println(afe.afn());
		afe.conv2AFD();
		System.out.println(afe.afd());
		afe.conv2COMPLETO();
		afe.conv2MINIMUM();
		afe.save("AFESaida");
	}

}