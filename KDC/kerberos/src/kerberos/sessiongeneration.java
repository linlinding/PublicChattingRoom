package kerberos;

import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.apache.commons.codec.binary.Base64;  

public class sessiongeneration {

	public sessiongeneration() {
		// TODO Auto-generated constructor stub
	}
	
	public String aeskey(){
		String sessionkey = "";
		try {
			KeyGenerator kg = KeyGenerator.getInstance("AES"); 
			kg.init(256);
			SecretKey sk = kg.generateKey();  
			byte[] b = sk.getEncoded();
			String s = Base64.encodeBase64String(b);
			sessionkey = s.substring(0, 16);

			return sessionkey;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sessionkey;
	} 
	
	

}
