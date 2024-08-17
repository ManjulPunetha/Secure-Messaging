package com.example.cryptosmsfinal.ui.keyExchange;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cryptosmsfinal.databinding.FragmentKeyExchangeBinding;
import com.example.cryptosmsfinal.key_exchange.DHKeyExchange;

import java.math.BigDecimal;

import static androidx.core.content.ContextCompat.getSystemService;


public class KeyExchangeFragment extends Fragment
{

    private FragmentKeyExchangeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        KeyExchangeViewModel keyExchangeViewModel =
                new ViewModelProvider(this).get(KeyExchangeViewModel.class);

        binding = FragmentKeyExchangeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //Action when user clicks on Generate Public Key button
        binding.publicKeyButton.setOnClickListener(v -> {
            String phoneNumber = binding.phoneNumber.getText().toString();
            if (phoneNumber.isEmpty())
            {
                Toast.makeText(getContext(), "Enter Phone Number", Toast.LENGTH_SHORT).show();
            }
            else
            {
                String publicKey = DHKeyExchange.getPublicKey(phoneNumber, binding.privateKey.getText().toString());
                try
                {
                    new BigDecimal(publicKey);
                    binding.sharedKey.setText(publicKey);
                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(getContext(), publicKey, Toast.LENGTH_SHORT).show();
                    binding.sharedKey.setText("");
                }
            }
        });

        //Action when user clicks on share button
        binding.shareButton.setOnClickListener(v -> {
            Intent shareText = new Intent();
            shareText.setAction(Intent.ACTION_SEND);
            shareText.putExtra(Intent.EXTRA_TEXT, binding.sharedKey.getText().toString());
            shareText.setType("text/plain");
            startActivity(shareText);
        });

        //Action when user clicks on Generate Shared Secret button
        binding.sharedSecretButton.setOnClickListener(v -> {
            String phoneNumber = binding.phoneNumber.getText().toString();
            if (phoneNumber.isEmpty())
            {
                Toast.makeText(getContext(), "Enter Phone Number", Toast.LENGTH_SHORT).show();
            }
            else
            {
                String sharedSecret = DHKeyExchange.getSharedSecret(phoneNumber, binding.senderPublicKeyEditText.getText().toString(), binding.privateKey.getText().toString());
                try
                {
                    new BigDecimal(sharedSecret);
                    binding.sharedSecretTextView.setText(sharedSecret);
                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(getContext(), sharedSecret, Toast.LENGTH_SHORT).show();
                    binding.sharedSecretTextView.setText("");
                }
            }
        });

        //Copy content from sharedSecretTextView when the Copy button is clicked
        binding.copyButton.setOnClickListener(v -> {
            String lsMainTextViewText = binding.sharedSecretTextView.getText().toString();
            if (!lsMainTextViewText.isEmpty())
            {
                ClipboardManager clipboard = getSystemService(getContext(), ClipboardManager.class);
                ClipData clip = ClipData.newPlainText("label", lsMainTextViewText);
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        /*
         *To display the Copy button only when the sharedSecretTextView text is not empty
         */
        binding.sharedSecretTextView.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                boolean isNotEmptyMessageText = charSequence.length() > 0;
                binding.copyButton.setVisibility(isNotEmptyMessageText
                        ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });

        /*
         *To display the Share button only when the sharedKey text is not empty
         */
        binding.sharedKey.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                boolean isNotEmptyMessageText = charSequence.length() > 0;
                binding.shareButton.setVisibility(isNotEmptyMessageText
                        ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}