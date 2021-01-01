package spring.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class NoEncodingPasswordEncoder implements PasswordEncoder {
	@Override
	public String encode(CharSequence rawPassword) {
		return rawPassword.toString(); // 인코딩 알고리즘
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return rawPassword.toString().equals(encodedPassword); // 인코딩 후 비교 알고리즘
	}
}
