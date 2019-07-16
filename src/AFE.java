/*
 * LUIZ EDUARDO PEREIRA - 0021619
 * MARCOS MARTINS VIEIRA - 0026756
 * 07/11/2017
*/

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class AFE {
	
	private List<String> alfabeto;
	private List<Estado> estado;
	private List<Estado> estadoInicial;
	private List<Estado> estadoFinal;
	private List<Transicao> transicao;
	
	public AFE() {
		alfabeto = new ArrayList<String>();
		estado = new ArrayList<Estado>();
		estadoInicial = new ArrayList<Estado>();
		estadoFinal = new ArrayList<Estado>();
		transicao = new ArrayList<Transicao>();
	}

/* ADD ***************************************************************************************************************************************** */
	public void addState(int idEstado, String nome, boolean bInicial, boolean bFinal) {
		Estado estado = new Estado();
		estado.setId(idEstado);
		estado.setNome(nome);
		estado.setInicial(bInicial);
		estado.setFinal(bFinal);		
		if (!existeEstado(idEstado)) {
			this.estado.add(estado);
			if (bInicial) {
				setInicial(estado);
			}
			if (bFinal) {
				setFinal(estado);
			}
		}
	}
	
	public void addTransition(int IdOrigem, int IdDestino, String simbolo) {
		Transicao transicao = new Transicao(getEstado(IdOrigem), getEstado(IdDestino), simbolo);
		if (!existeTransicao(IdOrigem, IdDestino, simbolo)) {
			this.transicao.add(transicao);
			setAlfabeto(simbolo);
		}	
		
	}
/* ********************************************************************************************************************************************* */
	
/* DELETA ************************************************************************************************************************************** */	
	public void deleteState(int idEstado) {
		int i, j;
		if(!existeEstado(idEstado)) {
			return;			
		}
		if (verificaInicial(idEstado)) {
			return;
		}	
		for (i = 0; i < 10; i++) { 
			for (j = 0; j < transicao.size(); j++){
				if ((transicao.get(j).getOrigem().getId() == idEstado) || (transicao.get(j).getDestino().getId() == idEstado)) {
					transicao.remove(transicao.get(j));
				}
			}
		}
		if (verificaFinal(idEstado)){
			estadoFinal.remove(getEstado(idEstado));
		}
		estado.remove(getEstado(idEstado));
	}

	public void deleteTransition(int IdOrigem, int IdDestino, String simbolo) {
		Transicao transicao;
		if(!existeTransicao(IdOrigem, IdDestino, simbolo)) {
			return;
		}
		transicao = getTransicao(IdOrigem, IdDestino, simbolo);
		this.transicao.remove(transicao);
	}
	
	private AFE deletaEstadoInutil(AFE automato) {		
		int tamanho = automato.estado.size();
		int i, j;
		List<Estado> filaEstados = new LinkedList<Estado>();
		boolean[] validos = new boolean[tamanho];
		Estado estadoAtual = automato.getInicial(0);
		filaEstados.add(estadoAtual);
		for (i = 0; i < tamanho; i++) {
			validos[i] = false;
		}
		do {
			for (j = 0; j < automato.transicao.size(); j++) {
				if ((automato.transicao.get(j).getOrigem().equals(estadoAtual)) && (validos[automato.transicao.get(j).getDestino().getId()] != true)) {
					filaEstados.add(automato.transicao.get(j).getDestino());
				}
			}
			validos[estadoAtual.getId()] = true;
			filaEstados.remove(0);
			if (!filaEstados.isEmpty()){
				estadoAtual = filaEstados.get(0);
			}
		} while (!filaEstados.isEmpty());	
		for (i = 0; i < tamanho; i++) {
			if (validos[i] == false) {
				automato.deleteState(i);
			}
		}		
		return automato;
	}
/* ********************************************************************************************************************************************* */	

/* SET/GET ************************************************************************************************************************************* */
	public Estado getEstado(int id) {
		for (Estado estado : this.estado) {
			if (estado.getId() == id) {
				return estado;
			}
		}
		return null;
	}
	
	public Estado getEstado(String nome) {
		for (Estado estado : this.estado) {
			if (estado.getNome().equals(nome)) {
				return estado;
			}
		}
		return null;
	}
	
	public void setInicial(Estado estado) {
		estadoInicial.add(estado);
	}

	public Estado getInicial(int id) {
		for (Estado estado : estadoInicial) {
			if (estado.getId() == id) {
				return estado;
			}
		}
		return null;
	}
	
	public Estado initial() {
		return estadoInicial.get(0);
	}
	
	public void setFinal(Estado estado) {
		estadoFinal.add(estado);
	}
	
	public Estado getFinal(int id) {
		for (Estado estado : estadoFinal) {
			if (estado.getId() == id) {
				return estado;
			}
		}
		return null;
	}
	
	public List<Estado> finals() {
		return estadoFinal;
	}
	
	public Transicao getTransicao(int idOrigem, int idDestino, String simbolo) {
		for (Transicao transicao : this.transicao){
			if ((transicao.getOrigem() == getEstado(idOrigem)) && (transicao.getDestino() == getEstado(idDestino)) && (transicao.getSimbolo() == simbolo)) {
				return transicao;
			}
		}
		return null;
	}
	
	public void setAlfabeto(String letra) {
		int i;
		if (alfabeto.size() == 0) {
			alfabeto.add(letra);
		}
		else {
			boolean existe = false;
			for (i = 0; i < alfabeto.size(); i++) {
				if ((alfabeto.get(i).equals(letra)) || (letra == null)) {
					existe = true;
				}
			}
			if (!existe) {
				alfabeto.add(letra);
				Collections.sort(alfabeto);
			}
		}
	}
	
	public List<String> getAlfabeto() {
		return alfabeto;
	}
	
	public String getAlfabeto(int pos) {
		return alfabeto.get(pos);
	}
/* ********************************************************************************************************************************************* */

/* EXISTE ************************************************************************************************************************************** */
	public boolean existeEstado(int idEstado) {
		if (getEstado(idEstado) != null) {
			return true;
		}
		return false;
	}
	
	public boolean existeNomeEstado(String nome) {
		for (Estado iterator : estado) {
			if (iterator.getNome() == nome) {
				return true;
			}
		}
		return false;
	}
	
	public boolean existeTransicao(int idOrigem, int idDestino, String simbolo) {
		if (getTransicao(idOrigem, idDestino, simbolo) != null) {
			return true;
		}
		return false;
	}
/* ********************************************************************************************************************************************* */
	
/* VERIFICA ************************************************************************************************************************************ */		
	public boolean verificaInicial(int id) {
		if (getEstado(id) == null) {
			return false;
		}
		if (getEstado(id).getInicial()) {
			return true;
		}
		return false;
	}
	
	public boolean verificaFinal(int id) {
		if (getEstado(id) == null) {
			return false;
		}
		if (getEstado(id).getFinal()) {
			return true;
		}
		return false;
	}
/* ********************************************************************************************************************************************* */
	
/* MOVE **************************************************************************************************************************************** */	
	public boolean accept(String palavra) {
		Estado estado;
		estado = move(estadoInicial.get(0), palavra);
		if (estado != null) {
			if (verificaFinal(estado.getId())) {
				return true;
			}
		}
		return false;
	}
	
	public Estado move(Estado origem, String palavra) {
		Estado destino;
		do {
			destino = moveSimbolo(origem, palavra.substring(0, 1));
			palavra = palavra.substring(1);
			if (destino == null){
				return null;
			}
		} while(!palavra.isEmpty());
		return destino;
	}
	
	public Estado moveSimbolo(Estado origem, String simbolo) {
		int i;
		for (i = 0; i < transicao.size(); i++) {
			if ((transicao.get(i).getOrigem().getId() == origem.getId()) && (transicao.get(i).getSimbolo().equals(simbolo))) {
				return transicao.get(i).getDestino();
			}
		}
		return null;	
	}
/* ********************************************************************************************************************************************* */	

/* SALVAR / CARREGAR *************************************************************************************************************************** */	
	public void save(String saida) {
		Document doc = new Document();
		Element root = new Element("structure");
		Element root2 = new Element("automaton");
		Element type = new Element("type");
		type.setText("fa");
		for (Estado iterator : this.estado) {
			Element estado = new Element("state");
			Attribute id = new Attribute("id", Integer.toString(iterator.getId()));
			Attribute nome = new Attribute("name",iterator.getNome());
			estado.setAttribute(id);
			estado.setAttribute(nome);
			Element inicial = new Element("initial");
			Element finale = new Element("final");
				if (iterator.getInicial()) {
					estado.addContent(inicial);
				}
				if (iterator.getFinal()){
					estado.addContent(finale);
				}
				root2.addContent(estado);
		}
		for (Transicao iterator: this.transicao){
			Element transicao = new Element("transition");			
			Element from = new Element("from");
			from.setText(Integer.toString(iterator.getOrigem().getId()));
			transicao.addContent(from);
			Element to = new Element("to");
			to.setText(Integer.toString(iterator.getDestino().getId()));
			transicao.addContent(to);
			Element read = new Element("read");
			read.setText(iterator.getSimbolo());
			transicao.addContent(read);	
			root2.addContent(transicao);
		}      
		root.addContent(type);
		root.addContent(root2);
		doc.setRootElement(root);         
		XMLOutputter xout = new XMLOutputter();
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(new File(saida));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			xout.output(doc , out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load(String entrada) {
		File f = new File(entrada);
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build(f);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}        
		Element root = (Element) doc.getRootElement();   
		Element automaton = (Element) root.getChild("automaton");        
		List estados = automaton.getChildren("state");
		Iterator iteratorE = estados.iterator();             
		while(iteratorE.hasNext()) {
			Element estado = (Element) iteratorE.next();
		    Estado novoEstado = new Estado();
		    novoEstado.setId(Integer.parseInt((estado.getAttributeValue("id"))));
		    novoEstado.setNome((estado.getAttributeValue("name")));
		    if (estado.getChild("initial") != null) {
		    	novoEstado.setInicial(true);
		    }
		    if (estado.getChild("final") != null) {
		    	novoEstado.setFinal(true);
		    }
		    addState(novoEstado.getId(), novoEstado.getNome(), novoEstado.getInicial(), novoEstado.getFinal()); 
		}		
		List transicoes = automaton.getChildren("transition");
		Iterator iteratorT = transicoes.iterator();	
		while(iteratorT.hasNext()) {
	        Element transicao = (Element) iteratorT.next();
	        Transicao novaTransicao = new Transicao();
	        novaTransicao.setOrigem(getEstado(Integer.parseInt(transicao.getChildText("from"))));
	        novaTransicao.setDestino(getEstado(Integer.parseInt(transicao.getChildText("to"))));
	        novaTransicao.setSimbolo(transicao.getChildText("read"));
	        addTransition(novaTransicao.getOrigem().getId(), novaTransicao.getDestino().getId() , novaTransicao.getSimbolo());	 
		}	
	}
/* ********************************************************************************************************************************************* */	
	
/* PRIVATE ************************************************************************************************************************************* */	
	private int maiorID(AFE automato){
		int i, maior = -1;
		for (i = 0; i < automato.estado.size(); i++) {
			if (automato.estado.get(i).getId() > maior) {
				maior = automato.estado.get(i).getId();
			}
		}
		return maior;
	}
	
	private int idEquivalenteOriginal(String nome) {
		int i;
		for (i = 0; i < estado.size(); i++) {
			if (nome.equals(Integer.toString(estado.get(i).getId()))) {
				return estado.get(i).getId();
			}
		}
		return 0;
	}
	
	private int idEquivalenteMinimizado(String nome, AFE automato) {
		String aux = "";
		String nomeEstado = "";
		String ponto = "";
		int i;
		for(i = 0; i < automato.estado.size(); i++) {
			nomeEstado = automato.estado.get(i).getNome();
			if (nomeEstado.length() > 1) {
				do {
					aux = nomeEstado.substring(0, 1);
					nomeEstado = nomeEstado.substring(1);
					if (nomeEstado.length() > 1) {
						ponto = nomeEstado.substring(0, 1);
						if (ponto.equals(".")) {
							nomeEstado = nomeEstado.substring(1);
						}
					}
					if (nome.equals(aux)) {
						return automato.estado.get(i).getId();
					}
					aux = "";
					ponto = "";
				} while(!nomeEstado.isEmpty());
			} else {
				if(nomeEstado.equals(nome)){
					return automato.estado.get(i).getId();
				}
			}
		}
		return 0;
	}
	
	private String concatenaNome(TreeSet<Integer> id) {
		String nome = "";
		Iterator<Integer> iterator = id.iterator();
		nome = Integer.toString(iterator.next());
		while (iterator.hasNext()) {
			nome = nome + "." + Integer.toString(iterator.next());
		}
		return nome;
	}
	
	private TreeSet<Integer> desconcatenaNome(String nome) {
		TreeSet<Integer> id = new TreeSet<Integer>();
		String aux = "";
		int i;
		for (i = 0; i < nome.length(); i++) {
			if (nome.substring(i, i + 1).equals(".")) {
				id.add(Integer.parseInt(aux));
				aux = "";
			} else {
				aux = aux + nome.substring(i, i + 1);
			}
		}
		id.add(Integer.parseInt(aux));
		return id;
	}
	
	private boolean verificaFinalConjunto(TreeSet<Integer> idOrigem) {
		for (Estado iterator : estadoFinal) {
			if (idOrigem.contains(iterator.getId()) && iterator.getFinal()) {
				return true;
			}
		}
		return false;
	}

/* ********************************************************************************************************************************************* */

/* COPIAR ************************************************************************************************************************************** */	
	public AFE copy() {
		AFE copia = new AFE();
		int i;
		
		for (i = 0; i < alfabeto.size(); i++) {
			copia.setAlfabeto(alfabeto.get(i));
		}
		for (i = 0; i < estado.size(); i++) {
			copia.addState(estado.get(i).getId(), estado.get(i).getNome(), estado.get(i).getInicial(), estado.get(i).getFinal());
		}
		for (i = 0; i < transicao.size(); i++) {
			copia.addTransition(transicao.get(i).getOrigem().getId(), transicao.get(i).getDestino().getId(), transicao.get(i).getSimbolo());
		}
		return copia;
	}
/* ********************************************************************************************************************************************* */	

/* EQUIVALENCIA DE ESTADOS ********************************************************************************************************************* */	
	public List<String> equivalents() {
		AFE minimo = new AFE();
		int i;
		minimo = conv2MINIMUM();
		List<String> equivalente = new ArrayList<>();
		for (i = 0; i < minimo.estado.size(); i++) {
			equivalente.add(minimo.getEstado(i).getNome());
		}
		return equivalente;
	}
/* ********************************************************************************************************************************************* */

/* EQUIVALENCIA DE AUTOMATO********************************************************************************************************************* */	
	public boolean equivalents(AFE automato) {
		AFE conversao1, conversao2;
		conversao1 = difference(automato);
		conversao2 = automato.difference(this);
		if ((conversao1.estadoFinal.size() == 0) && (conversao2.estadoFinal.size() == 0)) {
			return true;
		}
		return false;
	}
/* ********************************************************************************************************************************************* */

/* OPERAÇÕES *********************************************************************************************************************************** */
	private AFE multiplicacao(AFE automato) { 
		AFE resultado = new AFE();
		Estado estadoAtual1 = getInicial(0);
		Estado estadoAtual2 = automato.getInicial(0);
		Estado estadoProx1, estadoProx2;
		Estado estadoInicial = null;
		int i, j, k, l, cont = 0;
		int tamanho = estado.size() * automato.estado.size();
		
		/* CRIA NOVOS ESTADOS */
		for (i = 0; i < estado.size(); i++) {
			for (j = 0; j < automato.estado.size(); j++) {
				resultado.addState(cont, Integer.toString(i) + "." + Integer.toString(j), false, false);
				if ((i == getInicial(0).getId()) && (j == automato.getInicial(0).getId())) {
					estadoInicial = resultado.getEstado(cont);
					estadoInicial.setInicial(true);
				}
				cont++;
			}
		}		
		resultado.setInicial(estadoInicial);
		
		/* CRIA TRANSIÇÕES */
		for (i = 0; i < estado.size(); i++) {
			for (j = 0; j < automato.estado.size(); j++) {
				for(k = 0; k < alfabeto.size(); k++) {
					estadoAtual1 = getEstado(i);
					estadoAtual2 = automato.getEstado(j);
					estadoProx1 = moveSimbolo(estadoAtual1, alfabeto.get(k));
					estadoProx2 = automato.moveSimbolo(estadoAtual2, alfabeto.get(k));
					if ((estadoProx2 != null) && (estadoProx1 != null)) {
						int target = 0;
						int inicial = 0;
						String nomeInicial = Integer.toString(estadoAtual1.getId()) + "." + Integer.toString(estadoAtual2.getId());
						String nomeTarget = Integer.toString(estadoProx1.getId()) + "." + Integer.toString(estadoProx2.getId());
						for (l = 0; l < tamanho; l++) {
							if (nomeInicial.equals(resultado.getEstado(l).getNome())) {
								inicial = l;
							}
							if (nomeTarget.equals(resultado.getEstado(l).getNome())) {
								target = l;
							}
						}
						resultado.addTransition(inicial, target, alfabeto.get(k));
					}
				}
			}
		}
		resultado =  deletaEstadoInutil(resultado);
		return resultado;
	}	
	
	public AFE complement() {
		AFE complemento;
		complemento = copy();
		
		/* INVERTE ESTADO FINAL/NÃO FINAL */
		for (Estado estado : complemento.estado) {
			if (estado.getFinal()) {
				estado.setFinal(false);
				complemento.estadoFinal.remove(estado);
			}
			else {
				estado.setFinal(true);
				complemento.setFinal(estado);
			}
		}
		return complemento;
	}
	
	public AFE union(AFE automato) {
		int i, j, k;
		AFE uniao = multiplicacao(automato);
		
		/* TRANSFORMA O ESTADO DO AUTOMATO MULTIPLICADO EM FINAL SE PELO MENOS UM DOS ESTADOS ORIGINAIS ERA FINAL */
		for (k = 0; k < uniao.estado.size(); k++) {
			for (i = 0; i < estado.size(); i++) {
				for (j = 0; j < automato.estado.size(); j++) {
					String e1 = Integer.toString(estado.get(i).getId());
					String e2 = Integer.toString(automato.estado.get(j).getId());
					String nome = e1 + "." + e2;
					if (nome.equals(uniao.estado.get(k).getNome())) {
						if (verificaFinal(estado.get(i).getId()) || automato.verificaFinal(automato.estado.get(j).getId())) {
							uniao.estado.get(k).setFinal(true);
							uniao.setFinal(uniao.estado.get(k));
						}
					}
				}
			}
		}
		return uniao;
	}
	
	public AFE intersection(AFE automato) {
		int i, j, k;
		AFE intersecao = multiplicacao(automato);	
		
		/* TRANSFORMA O ESTADO DO AUTOMATO MULTIPLICADO EM FINAL SE OS DOIS ESTADOS ORIGINAIS ERAM FINAIS */
		for (k = 0; k < intersecao.estado.size(); k++) {
			for (i = 0; i < estado.size(); i++) {
				for (j = 0; j < automato.estado.size(); j++) {
					String e1 = Integer.toString(estado.get(i).getId());
					String e2 = Integer.toString(automato.estado.get(j).getId());
					if ((e1 + "." + e2).equals(intersecao.estado.get(k).getNome())) {
						if ((verificaFinal(estado.get(i).getId())) && (automato.verificaFinal(automato.estado.get(j).getId()))) {
							intersecao.estado.get(k).setFinal(true);;
							intersecao.setFinal(intersecao.estado.get(k));;
						}
					}
				}
			}
		}
		return intersecao;
	}
	
	public AFE difference(AFE automato) {
		automato = automato.complement();
		AFE diferenca = intersection(automato);
		return diferenca;
	}
/* ********************************************************************************************************************************************* */

/* CONVERSÕES ********************************************************************************************************************************** */
	public void conv2AFV() {
		AFE automato = new AFE();
		Estado estadoO, estadoD;
		Transicao transicao;
		List<Transicao> fila = new ArrayList<Transicao>();
		List<String> alfabeto =  new ArrayList<String>();
		int j, id;
		
		/* QUEBRA A STRING EM SIMBOLOS, CRIANDO NOVOS ESTADOS E TRANSIÇÕES */
		for (Transicao iterator : this.transicao) {
			fila.add(iterator);
		}		
		automato = this.copy();
		
		while (!fila.isEmpty()) {
			transicao = fila.remove(0);
			if (transicao.getSimbolo().length() > 1) {
				id = 0;
				estadoO = transicao.getOrigem();
				for (j = 0; j < transicao.getSimbolo().length() - 1; j++) {
					automato.addState(automato.estado.size(), transicao.getOrigem().getNome() + "-" + Integer.toString(id), false, false);
					id++;
					estadoD = automato.getEstado(automato.estado.size() - 1);
					automato.addTransition(estadoO.getId(), estadoD.getId(), transicao.getSimbolo().substring(j, j + 1));
					estadoO = estadoD;
				}
				automato.addTransition(estadoO.getId(), transicao.getDestino().getId(), transicao.getSimbolo().substring(j, j + 1));
				automato.deleteTransition(transicao.getOrigem().getId(), transicao.getDestino().getId(), transicao.getSimbolo());
			}
		}
		
		for (Transicao iterator : automato.transicao) {
			if (!alfabeto.contains(iterator.getSimbolo())) {
				alfabeto.add(iterator.getSimbolo());
			}
		}
		
		this.alfabeto = alfabeto;
		this.estado = automato.estado;
		this.estadoInicial = automato.estadoInicial;
		this.estadoFinal = automato.estadoFinal;
		this.transicao = automato.transicao;
	}
	
	public void conv2AFN() {
		AFE automato = new AFE();
		Estado estado;
		List<TreeSet<Integer>> fecho1 = new ArrayList<TreeSet<Integer>>();
		List<TreeSet<Integer>> fecho2 = new ArrayList<TreeSet<Integer>>();
		List<TreeSet<Integer>> move = new ArrayList<TreeSet<Integer>>();
		int i, j, k, id, index, cont;
		TreeSet<Integer> nome;
		
		/* COPIA ALFABETO IGNORANDO O VAZIO */
		index = 0;
		for (i = 0; i < alfabeto.size(); i++) {
			if (alfabeto.get(i).equals("")) {
				i++;
			}
			automato.setAlfabeto(alfabeto.get(i));
		}
		
		/* TODOS OS ESTADOS PERTENCENTES AO FECHO VAZIO DO ESTADO INICIAL TAMBEM SE TORNAM INICIAL */
		for (i = 0; i < this.estadoInicial.size(); i++) {
			estado = this.estadoInicial.get(i);
			while ((estado = moveSimbolo(estado, "")) != null) {
				estado.setInicial(true);;
			}
		}
		
		/* PRIMEIRO FECHO VAZIO */
		for (i = 0; i < this.estado.size(); i++) { 
			nome = new TreeSet<Integer>();
			estado = this.estado.get(i);
			automato.addState(estado.getId(), estado.getNome(), estado.getInicial(), estado.getFinal());
			nome.add(estado.getId());
			while (((estado = moveSimbolo(estado, "")) != null) && (moveSimbolo(estado, "") != estado)) {
				nome.add(estado.getId());
			}
			fecho1.add(nome);
		}
		
		/* MOVE */
		for (i = 0; i < this.estado.size(); i++) { 
			for (j = 0; j < automato.alfabeto.size(); j++) {
				nome = new TreeSet<Integer>();
				for (Transicao iterator : transicao) {
					if ((fecho1.get(i).contains(iterator.getOrigem().getId())) 
							&& (automato.alfabeto.get(j).equals(iterator.getSimbolo()))) {
						nome.add(iterator.getDestino().getId());
					}
				}
				move.add(nome);
			}
		}
		
		/* SEGUNDO FECHO VAZIO */
		index = 0;
		for (i = 0; i < this.estado.size(); i++) { 
			for (j = 0; j < automato.alfabeto.size(); j++) {
				nome = new TreeSet<Integer>();
				cont = move.get(index).size();
				for (k = 0; k < cont; k++) {
					id = move.get(index).first();
					move.get(index).remove(id);
					estado = this.estado.get(id);
					nome.add(estado.getId());
					while (((estado = moveSimbolo(estado, "")) != null) && (moveSimbolo(estado, "") != estado)) {
						nome.add(estado.getId());
					}
					
				}
				index++;
				fecho2.add(nome);
			}
		}
		
		/* ADICIONA AS TRANSIÇÕES */
		index = 0;
		for (i = 0; i < this.estado.size(); i++) {
			for (j = 0; j < automato.alfabeto.size(); j++) {
				while (!fecho2.get(index).isEmpty()) {
					id = fecho2.get(index).first();
					fecho2.get(index).remove(id);
					automato.addTransition(this.estado.get(i).getId(), this.estado.get(id).getId(), automato.alfabeto.get(j));
				}
				index++;
			}
		}	
		
		this.alfabeto = automato.alfabeto;
		this.estado = automato.estado;
		this.transicao = automato.transicao;	
	}
	
	public void conv2AFD() {
		AFE automato = new AFE();
		List<Estado> fila = new ArrayList<Estado>();
		TreeSet<Integer> idOrigem;
		TreeSet<Integer> idDestino = new TreeSet<Integer>();
		int i, j, id = 0;
		String nome = "";
		boolean achou, bFinal = false;
		
		automato.alfabeto = alfabeto;
		
		/* PEGA ESTADO INICIAL */
		i = 0; 
		for (Estado iterator : estadoInicial) {
			if (i == 0) {
				nome = Integer.toString(iterator.getId());
				i++;
			}
			else {
				nome = nome + "." + Integer.toString(iterator.getId());
			}
			if (iterator.getFinal()) {
				bFinal = true;
			}
		}
		automato.addState(id, nome, true, bFinal);
		id++;
		fila.add(automato.estado.get(0));	
		
		/* CONVERTE AUTOMATO */
		while (!fila.isEmpty()) {
			idOrigem = desconcatenaNome(fila.remove(0).getNome());
			for (i = 0; i < automato.alfabeto.size(); i++) {
				nome = "";
				/* ENCONTRA TRANSIÇÕES */
				for (Transicao iterator : transicao) {
					if (idOrigem.contains(iterator.getOrigem().getId())	&& (automato.getAlfabeto(i).equals(iterator.getSimbolo()))) {
						idDestino.add(iterator.getDestino().getId());
					}
				}
				if (!idDestino.isEmpty()) { /* GAMBIARRA DE ULTIMA HORA PARA FUNCIONAR */
					nome = automato.concatenaNome(idDestino);
					achou = false;
					/* ADICIONA NOVO ESTADO, SE JA NÃO EXISTIR */
					for (j = 0; j < automato.estado.size(); j++) {
						if (automato.estado.get(j).getNome().equals(nome)) {
							achou = true;
							break;
						}
					}
					if (!achou) {
						automato.addState(id, nome, false, verificaFinalConjunto(idDestino));
						fila.add(automato.getEstado(id));
						id++;
					}
					/* ADICIONA TRANSICAO */
					automato.addTransition(automato.getEstado(automato.concatenaNome(idOrigem)).getId(), 
							automato.getEstado(nome).getId(),
							automato.alfabeto.get(i));
					idDestino.clear();
				}
			}
			idOrigem.clear();
		}
		this.alfabeto = automato.alfabeto;
		this.estado = automato.estado;
		this.estadoInicial = automato.estadoInicial;
		this.estadoFinal = automato.estadoFinal;
		this.transicao = automato.transicao;
	}
	
	public void conv2COMPLETO() {
		AFE automato = new AFE();
		Estado estado = new Estado();
		int i, j;
		boolean[][] achou = new boolean[this.estado.size()][alfabeto.size()];
		
		for (i = 0; i < this.estado.size(); i++) {
			for (j = 0; j < alfabeto.size(); j++) {
				achou[i][j] = false;
			}
		}
		
		/* ADICIONA ESTADO DE ERRO E SUAS TRANSIÇÕES PARA ELE MESMO */
		automato = copy();
		
		if (getEstado("ERRO") != null) {
			automato.addState(this.estado.size(), "ERRO", false, false);
			estado = automato.getEstado("ERRO");
			for (i = 0; i < alfabeto.size(); i++) {
				automato.addTransition(estado.getId(), estado.getId(), automato.alfabeto.get(i));
			}
		}
		
		/* ENCONTRA AS TRANSIÇÕES EXISTENTES E MARCA COMO TRUE */
		for (i = 0; i < this.estado.size() - 1; i++) { /* -1 POIS ADICIONOU O ESTADO DE ERRO */
			for (j = 0; j < alfabeto.size(); j++) {
				for (Transicao iterator : transicao) {
					if ((iterator.getOrigem().getId() == this.estado.get(i).getId()) && (iterator.getSimbolo().equals(alfabeto.get(j)))) {
						achou[i][j] = true;
					}
				}
			}
		}
		
		/* APONTA PARA O ESTADO DE ERRO AS TRANSIÇÕES QUE NÃO FORAM ENCONTRADAS */
		for (i = 0; i < this.estado.size() - 1; i++) {
			for (j = 0; j < alfabeto.size(); j++) {
				if (achou[i][j] == false) {
					automato.addTransition(this.estado.get(i).getId(), estado.getId(), getAlfabeto(j));
				}
			}
		}
		this.alfabeto = automato.alfabeto;
		this.estado = automato.estado;
		this.estadoInicial = automato.estadoInicial;
		this.estadoFinal = automato.estadoFinal;
		this.transicao = automato.transicao;
	}
	
	public AFE conv2MINIMUM() {
		int i, j, k;
		AFE automato = new AFE();
		
		deletaEstadoInutil(this);
		conv2COMPLETO();
		
		String[][] matriz = new String[estado.size()][estado.size()];		
		Estado[][] moveMatriz = new Estado[estado.size()][alfabeto.size()];
		for (i = 0; i < estado.size(); i++) {
			for (j = 0; j < alfabeto.size(); j++) {
				moveMatriz[i][j] = moveSimbolo(estado.get(i), alfabeto.get(j));
			}
		}

		/* TABELA DE EQUIVALENCIA */
		boolean continua = true;
		while(continua) {
			continua = false;
			for (i = 1; i < estado.size(); i++) {
				int idI = estado.get(i).getId();
				for (j = 0; j < i; j++) {
					int idJ = estado.get(j).getId();
					if ((verificaFinal(idI) && !verificaFinal(idJ)) || (!(verificaFinal(idI)) && verificaFinal(idJ))) {
						matriz[i][j] = "X";
					} else {
						for (k = 0; k < alfabeto.size(); k++) {
							if ((moveMatriz[i][k] != null) && (moveMatriz[j][k] != null)) {
								if (moveMatriz[i][k].getId() == moveMatriz[j][k].getId()) {
									matriz[i][j] = "E";
								} else if ((matriz[moveMatriz[i][k].getId()][moveMatriz[j][k].getId()] == "E") || (matriz[moveMatriz[j][k].getId()][moveMatriz[i][k].getId()] == "E")) {
									matriz[i][j] = "E";
								} else if ((matriz[moveMatriz[i][k].getId()][moveMatriz[j][k].getId()] == "X") || (matriz[moveMatriz[j][k].getId()][moveMatriz[i][k].getId()] == "X")) {
									matriz[i][j] = "X";
								} else {
									continua = true;
									matriz[i][j] = Integer.toString(moveMatriz[i][k].getId()) + "."	+ Integer.toString(moveMatriz[j][k].getId()) + "|";
								}
							}
						}
					}
				}
			}
		}
		
		/* ESTADOS MINIMIZADOS */
		boolean[] achou = new boolean[estado.size()];
		int id;
		boolean edit, bInicial, bFinal;
		String nome;
		for (j = 0; j < estado.size(); j++) {
			edit = false;
			bInicial = false;
			bFinal = false;
			nome = "";
			for (i = j; i < estado.size(); i++) {				
				if (nome.isEmpty() && !achou[j]) {
					nome = Integer.toString(getEstado(j).getId());
					bInicial = verificaInicial(getEstado(j).getId());
					bFinal = verificaFinal(getEstado(j).getId());
					achou[j] = true;
					edit = true;
				} else if (!achou[i]) {
					if (matriz[i][j].equals("E")) {
						nome = nome + "." + Integer.toString(getEstado(i).getId());
						if (!bInicial) {
							bInicial = verificaInicial(getEstado(i).getId());
						}
						achou[i] = true;
					}
				}
			}
			if (edit) {
				id = maiorID(automato) + 1;	
				automato.addState(id, nome, bInicial, bFinal);
			}
		}
	
		/* TRANSIÇÕES */
		int idEstado;
		int idNovoEstado;
		Estado destino;
		List<String> letra = new ArrayList<String>();
		List<Estado> listaEstado = new ArrayList<Estado>();

		for (i = 0; i < automato.estado.size(); i++) {
			String nomeEstado = automato.estado.get(i).getNome();
			if (nomeEstado.length() > 1) {
				String aux;
				String ponto;
				do {
					aux = nomeEstado.substring(0, 1);
					nomeEstado = nomeEstado.substring(1);
					if (nomeEstado.length() > 1) {
						ponto = nomeEstado.substring(0, 1);
						if (ponto.equals(".")) {
							nomeEstado = nomeEstado.substring(1);
						}
					}
					idEstado = idEquivalenteOriginal(aux);
					for (j = 0; j < alfabeto.size(); j++) {
						destino = moveSimbolo(getEstado(idEstado), alfabeto.get(j));
						if(destino != null){
							listaEstado.add(destino);
							letra.add(alfabeto.get(j));
						}
					}
					for(j = 0; j < listaEstado.size(); j++){
						idNovoEstado = idEquivalenteMinimizado(Integer.toString(listaEstado.get(j).getId()), automato);
						automato.addTransition(automato.estado.get(i).getId(), idNovoEstado, letra.get(j));
					}
					listaEstado.clear();
					letra.clear();
					aux = "";
					ponto = "";
				} while(!nomeEstado.isEmpty());
			} else {
				idEstado = idEquivalenteOriginal(nomeEstado);
				for (j = 0; j < alfabeto.size(); j++) {
					destino = moveSimbolo(getEstado(idEstado), alfabeto.get(j));
					if (destino != null){
						listaEstado.add(destino);
						letra.add(alfabeto.get(j));
					}
				}
				for (j = 0; j < listaEstado.size(); j++) {
					idNovoEstado = idEquivalenteMinimizado(Integer.toString(listaEstado.get(j).getId()), automato);
					automato.addTransition(automato.estado.get(i).getId(), idNovoEstado, letra.get(j));
				}
				listaEstado.clear();
				letra.clear();
			}
		}
		return automato;
	}
/* ********************************************************************************************************************************************* */

/* VERIFICAÇÃO ********************************************************************************************************************************* */	
	public boolean afv() {
		for (Transicao iterator : transicao) {
			if (iterator.getSimbolo().length() > 1) {
				return false;
			}
		}
		return true;
	}
	
	public boolean afn() {
		for (Transicao iterator : transicao) {
			if (iterator.getSimbolo().length() != 1) {
				return false;
			}
		}
		return true;
	}
	
	public boolean afd() {
		List<Integer> cont = new ArrayList<Integer>();
		int i;
		if (alfabeto.contains("")) {
			return false;
		}
		if (estadoInicial.size() > 1) {
			return false;
		}
		if (transicao.size() > estado.size() * alfabeto.size()) {
			return false;
		}
		for (i = 0; i < estado.size(); i++) {
			cont.add(0);
		}
		for (Transicao iterator : transicao) {
			cont.set(iterator.getOrigem().getId(), cont.get(iterator.getOrigem().getId()) + 1);
		}
		for (i = 0; i < estado.size(); i++) {
			if (cont.get(i) > alfabeto.size()) {
				return false;
			}
		}	
		return true;
	}
	
	public boolean completo() {
		if (transicao.size() == estado.size() * alfabeto.size()) {
			return true;
		}
		return false;
	}
/* ********************************************************************************************************************************************* */
	
}