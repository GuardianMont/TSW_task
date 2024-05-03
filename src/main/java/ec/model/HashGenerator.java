package ec.model;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.management.BadAttributeValueExpException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

public class HashGenerator {
    public static byte[] generateHash(String string, byte[] salt)  throws BadAttributeValueExpException {

        if (salt == null || salt.length != 16)
            throw new BadAttributeValueExpException("Bad salt: " + salt);

        KeySpec spec = new PBEKeySpec(string.toCharArray(), salt, 65536, 128);

        byte[] result = null;

        try{
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            result = factory.generateSecret(spec).getEncoded();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    // da gestire con salt unico
    public static byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return salt;
    }

}
