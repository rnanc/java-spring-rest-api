package br.com.alura.forum.config;

public class ErrorFormularioDTO {
	private String campo;
	private String erro;
	
	public ErrorFormularioDTO(String campo, String erro) {
		super();
		this.campo = campo;
		this.erro = erro;
	}

	public String getCampo() {
		return campo;
	}

	public String getErro() {
		return erro;
	}
	
	
}
