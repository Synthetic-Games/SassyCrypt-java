package com.sassycrypt.sassycrypt.util;

import com.google.common.hash.Hashing;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Bryce
 */
public class Helpers {

    public static String getHWID() {

        String UUID = null;
        try {
            UUID = getCSProductUUID();
            System.out.println("MOBO SERIAL: " + UUID);
        } catch (IOException ex) {
            System.out.println("Error starting HWID Process: " + ex.getMessage());
        }

        String sha256hex = Hashing.sha256().hashString(UUID, StandardCharsets.UTF_8).toString();
        System.out.println("Encrypted: " + sha256hex);
        return sha256hex;

    }

    public static String getCSProductUUID() throws IOException {

        String command = "wmic csproduct get uuid";

        String serialNumber = null;

        Process SerialNumberProcess = Runtime.getRuntime().exec(command);
        try (InputStreamReader in = new InputStreamReader(SerialNumberProcess.getInputStream());
                BufferedReader buffer = new BufferedReader(in)) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] tokens = line.split("\\s");
                for (String str : tokens) {
                    if (!(str.equals("") || str.contains("UUID"))) {
                        serialNumber = str;
                    }
                }
            }

            SerialNumberProcess.waitFor();

            buffer.close();
        } catch (Exception e) {
            System.out.println("Error reading system machine code: " + e.getMessage());
        }
        return serialNumber;

    }

}
