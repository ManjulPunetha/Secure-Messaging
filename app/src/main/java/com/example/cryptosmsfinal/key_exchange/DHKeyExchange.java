package com.example.cryptosmsfinal.key_exchange;

import com.example.cryptosmsfinal.key_exchange.model.Diffie;
import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigInteger;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DHKeyExchange
{
    private static final String BASE_URL = "http://3.110.104.53:8083/diffie/";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    // Helper method to fetch Diffie object
    private static Diffie fetchRecordFromDiffieApi(String phoneNumber) throws IOException
    {
        HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                .addPathSegment(phoneNumber)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response;
        try
        {
            response = client.newCall(request).execute();
        }
        catch (IOException e)
        {
            throw new IOException("Unable to connect to the endpoint. This could be due to network issues.");
        }

        if (response.code() != 200)
        {
            throw new RuntimeException("User not authorized / Incorrect details.");
        }

        String responseString = response.body().string();
        return gson.fromJson(responseString, Diffie.class);
    }

    /**
     * This method is used to get the public key of the user.
     *
     * @param phoneNumber - Phone number of the user
     * @param privateKey  - Private key of the user
     * @return - Public key to be shared
     */
    public static String getPublicKey(String phoneNumber, String privateKey)
    {
        try
        {
            Diffie diffie = fetchRecordFromDiffieApi(phoneNumber);
            BigInteger publicKey = diffie.getGenerator().modPow(new BigInteger(privateKey), diffie.getPrimeNum());
            return publicKey.toString();
        }
        catch (IOException | RuntimeException e)
        {
            return e.getMessage();
        }
    }


    /**
     * This method is used to get the shared secret of the user.
     *
     * @param phoneNumber     - Phone number of the user
     * @param senderPublicKey - Public key sent by the sender
     * @param privateKey      - Private key of the user
     * @return - Shared secret
     */
    public static String getSharedSecret(String phoneNumber, String senderPublicKey, String privateKey)
    {
        try
        {
            Diffie diffie = fetchRecordFromDiffieApi(phoneNumber);
            BigInteger sharedSecret = new BigInteger(senderPublicKey).modPow(new BigInteger(privateKey), diffie.getPrimeNum());
            return sharedSecret.toString();
        }
        catch (IOException | RuntimeException e)
        {
            return e.getMessage();
        }
    }
}