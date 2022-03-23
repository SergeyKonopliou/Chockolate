import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

	public static void main(String[] args) {
		BCryptPasswordEncoder en = new BCryptPasswordEncoder();
		String pass = "user";
		String encp = en.encode(pass);
		System.out.println(encp);

	}

}
