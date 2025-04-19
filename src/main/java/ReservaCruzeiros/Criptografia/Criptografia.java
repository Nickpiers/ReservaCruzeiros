package ReservaCruzeiros.Criptografia;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class Criptografia {
    public static String criptografaMensagem(String mensagem) throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchProviderException, SignatureException, InvalidKeyException {

        byte[] privateBytes = Files.readAllBytes(Paths.get("private.key"));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        // Assinar a mensagem
        Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
        dsa.initSign(privateKey);
        byte[] mensagemBytes = mensagem.getBytes("UTF-8");
        dsa.update(mensagemBytes);
        byte[] assinatura = dsa.sign();

        // Codificar em Base64
        String mensagemBase64 = Base64.getEncoder().encodeToString(mensagemBytes);
        String assinaturaBase64 = Base64.getEncoder().encodeToString(assinatura);

        // Montar JSON
        JSONObject json = new JSONObject();
        json.put("mensagem", mensagemBase64);
        json.put("assinatura", assinaturaBase64);

        return json.toString();
    }
}
