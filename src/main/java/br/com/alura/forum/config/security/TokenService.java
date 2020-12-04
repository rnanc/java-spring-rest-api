package br.com.alura.forum.config.security;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	
	@Value("${forum.jwt.expiration}")
	private String expiration;
	
	@Value("${forum.jwt.secret}")
	private String secret;
	
	@Autowired
	private AutenticacaoService autenticacaoService;
	
	@Autowired
	private UsuarioRepository repository;
	
	public String gerarToken(Authentication authenticate) {
		
		Usuario principal = (Usuario) authenticate.getPrincipal();
		Date date = new Date();
		Date dateExp = new Date(date.getTime() + Long.parseLong(expiration));
		return Jwts.builder()
				.setIssuer("API do Forum")
				.setSubject(principal.getId().toString())
				.setIssuedAt(date)
				.setExpiration(dateExp)
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}

	public boolean isTokenValido(String token) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Optional<Usuario> getUsuario(String token) {
		Claims body = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();	
		Optional<Usuario> usuario = repository.findById(Long.parseLong(body.getSubject()));
		if (usuario.isPresent()) {
			return usuario;
			
		}
		return null;
	}


}
