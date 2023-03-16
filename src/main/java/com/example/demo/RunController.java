package com.example.demo;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class RunController {
    
    private static final int STRING_LENGTH = 16;
    public static String generateRandomString() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[STRING_LENGTH];
        random.nextBytes(bytes);
        return Base64.encodeBase64URLSafeString(bytes).substring(0, STRING_LENGTH);
    }
    
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        System.out.println("****");
        if (session.getAttribute("message") != null && session.getAttribute("key") != null) {
        	System.out.println("Session on");
            model.addAttribute("message", session.getAttribute("message"));
            model.addAttribute("key", session.getAttribute("key"));
            session.removeAttribute("message");
            session.removeAttribute("key");
        }
        return "index.jsp";
    }
    
    @PostMapping("/inputMessage")
    public String encrypt(@RequestParam("message") String message, RedirectAttributes redirectAttrs) {
        // Generate encryption key and encrypt the message
        String key = generateRandomString();
        String encryptedMessage = encryptMessage(message, key);
        
        // Add encrypted message and key to session
        redirectAttrs.addFlashAttribute("message", encryptedMessage);
        redirectAttrs.addFlashAttribute("key", key);
        
        // Redirect to index page
        return "redirect:/";
    }
    
    @PostMapping("/inputKey")
    public String decrypt(@RequestParam("message") String message,
            @RequestParam("key") String key, RedirectAttributes redirectAttrs) {
        // Decrypt the message using the key
        String decryptedMessage = decryptMessage(message, key);
        
        // Add decrypted message to session
        redirectAttrs.addFlashAttribute("message", decryptedMessage);
        
        // Redirect to index page
        return "redirect:/";
    }
    
    // Function to encrypt a message using a key
    private String encryptMessage(String message, String key) {
        try {
            // Create a key specification object from the encryption key
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            
            // Create a cipher object and initialize it with the key for encryption
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            
            // Encrypt the message and encode it as a base64 string
            byte[] encryptedBytes = cipher.doFinal(message.getBytes());
            String encryptedString = Base64.encodeBase64String(encryptedBytes);
            
            return encryptedString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Function to decrypt a message using a key
    private String decryptMessage(String encryptedMessage, String key) {
        try {
            // Create a key specification object from the encryption key
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            
            // Create a cipher object and initialize it with the key for decryption
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            
            // Decode the encrypted message from base64 and decrypt it
            byte[] encryptedBytes = Base64.decodeBase64(encryptedMessage);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            
            // Convert the decrypted bytes to a string and return it
            String decryptedString = new String(decryptedBytes);
            return decryptedString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
   
}