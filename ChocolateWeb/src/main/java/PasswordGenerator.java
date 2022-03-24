import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
/**
 * Класс использовался для кодировки пароля
 * добавляемых в базу данных пользователей при старте 
 * приложения через файл data.sql
 *
 */
public class PasswordGenerator {

	public static void main(String[] args) {
		BCryptPasswordEncoder en = new BCryptPasswordEncoder();
		String pass = "user";
		String encp = en.encode(pass);
		System.out.println(encp);
	}
}
