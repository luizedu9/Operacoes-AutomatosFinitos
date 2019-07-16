/*
 * LUIZ EDUARDO PEREIRA - 0021619
 * MARCOS MARTINS VIEIRA - 0026756
 * 07/11/2017
*/

public class Estado {
	
	private int id;
	private String nome;
	private boolean bInicial;
	private boolean bFinal;

	public Estado() {
		bInicial = false;
		bFinal = false;
	}
		
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setInicial(boolean bInicial) {
		this.bInicial = bInicial;
	}
	
	public boolean getInicial() {
		return bInicial;
	}
	
	public void setFinal(boolean bFinal) {
		this.bFinal = bFinal;
	}
	
	public boolean getFinal() {
		return bFinal;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Estado)) {
			return false;
		}
		Estado aux = (Estado) obj;
		if (id != aux.id) {
			return false;
		}
		if (bInicial != aux.bInicial) {
			return false;
		}
		if (bFinal != aux.bFinal) {
			return false;
		}
		if (nome == null) {
			if (aux.nome != null) {
				return false;
			}
		} else if (!nome.equals(aux.nome)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Estado [id = " + id + ", nome = " + nome + ", Inicial = " + bInicial + ", Final = " + bFinal + "]";
	}

}