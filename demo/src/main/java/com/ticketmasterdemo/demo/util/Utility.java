package com.ticketmasterdemo.demo.util;

import java.util.UUID;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.mindrot.jbcrypt.BCrypt;

public class Utility {
    
    private static final Pattern DANGEROUS_PATTERN = Pattern.compile(
        "(<script.*?>.*?</script>)|(DROP\\s+TABLE)|(SELECT\\s+.*?FROM)|(&#)|(%3C)|(%3E)|(%20)",
        Pattern.CASE_INSENSITIVE
    );

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    );

    /**
     * Validates an email address to ensure it matches a standard format.
     *
     * @param email The email address to validate.
     * @return true if the email is in a valid format, false otherwise.
     */
    public  boolean emailWhitelist(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        System.out.println(matcher.matches());
        return matcher.matches();
    }

    public boolean isInputSafe(String input) {
    // A regex pattern to detect any script tags, SQL keywords, or other potentially harmful strings.
    // This is a very basic pattern and might need adjustments based on your specific needs.
    

    /**
     * Validates user input to ensure there's no dangerous content.
     *
     * @param input The user input to validate.
     * @return true if the input is safe, false otherwise.
     */
        if (input == null) {
            return false;
        }
        return !DANGEROUS_PATTERN.matcher(input).find();
    }

    public String generateRandomUUID() {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString();
    }

    public String generateUserId() {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString();
    }

    public String generateEmailVerificationToken() {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString();
    }

    public String hashPassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    public boolean checkPassword(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }

}

