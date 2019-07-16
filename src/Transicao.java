/*
 * LUIZ EDUARDO PEREIRA - 0021619
 * MARCOS MARTINS VIEIRA - 0026756
 * 07/11/2017
*/

public class Transicao {
	
	private Estado origem;
	private Estado destino;
	private String simbolo;
	
	public Transicao() {
		this(null, null, "null");
	}
	
	public Transicao(Estado origem, Estado destino, String simbolo){
		setOrigem(origem);
		setDestino(destino);
		setSimbolo(simbolo);
	}
	
	public Estado getOrigem() {
		return origem;
	}
	
	public void setOrigem(Estado origem) {
		this.origem = origem;
	}
	
	public Estado getDestino() {
		return destino;
	}
	
	public void setDestino(Estado destino) {
		this.destino = destino;
	}
	
	public String getSimbolo() {
		return simbolo;
	}
	
	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Transicao))
			return false;
		Transicao aux = (Transicao) obj;
		if (simbolo == null) {
			if (aux.simbolo != null)
				return false;
		} else if (!simbolo.equals(aux.simbolo))
			return false;
		if (destino == null) {
			if (aux.destino != null)
				return false;
		} else if (!destino.equals(aux.destino))
			return false;
		if (origem == null) {
			if (aux.origem != null)
				return false;
		} else if (!origem.equals(aux.origem))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Transição [origem=" + origem + ", destino=" + destino + ",Simbolo=" + simbolo + "]";
	}
	
}