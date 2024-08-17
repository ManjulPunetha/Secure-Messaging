package com.example.cryptosmsfinal.ui.messaging;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cryptosmsfinal.databinding.FragmentMessagingBinding;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import static com.example.cryptosmsfinal.encryption.Decrypt.decrypt;
import static com.example.cryptosmsfinal.encryption.Encrypt.encrypt;

public class MessagingFragment extends Fragment
{

    private FragmentMessagingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        MessagingViewModel messagingViewModel =
                new ViewModelProvider(this).get(MessagingViewModel.class);
        binding = FragmentMessagingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //Encrypt button listener
        binding.encryptButton.setOnClickListener(v -> {
            if (validateInput())
            {
                String lsMessage = binding.messageText.getText().toString();
                String lsKey = binding.keyEditText.getText().toString();

                try
                {
                    binding.mainTextView.setText(encrypt(lsMessage, lsKey));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        //Decrypt button listener
        binding.decryptButton.setOnClickListener(v -> {
            if (validateInput())
            {
                String lsMessage = binding.messageText.getText().toString();
                String lsKey = binding.keyEditText.getText().toString();
                try
                {
                    binding.mainTextView.setText(decrypt(lsMessage, lsKey));
                }
                catch (BadPaddingException badPaddingException)
                {
                    Toast.makeText(getContext(), "Please enter the correct decryption key.", Toast.LENGTH_SHORT).show();
                    binding.keyEditText.setText("");
                    binding.mainTextView.setText("");
                    Log.e("decryptButton", "Exception during decryption: " + badPaddingException.getMessage());
                }
                catch (NegativeArraySizeException | IllegalBlockSizeException |
                       IllegalArgumentException decryptionException)
                {
                    Toast.makeText(getContext(), "Please enter the correct message.", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        //Clear button listeners to clear the text in the EditText
        binding.clearKeyButton.setOnClickListener(v -> binding.keyEditText.setText(""));
        binding.clearMessageButton.setOnClickListener(v -> binding.messageText.setText(""));

        binding.shareMainTextViewButton.setOnClickListener(v -> {
            Intent shareText = new Intent();
            shareText.setAction(Intent.ACTION_SEND);
            shareText.putExtra(Intent.EXTRA_TEXT, binding.mainTextView.getText().toString());
            shareText.setType("text/plain");
            startActivity(shareText);
        });

        /*
        To display the clear button only when the message text is not empty
         */
        binding.messageText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                boolean isNotEmptyMessageText = charSequence.length() > 0;
                binding.clearMessageButton.setVisibility(isNotEmptyMessageText
                        ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });

        /*
        To display the clear button only when the key text is not empty
         */
        binding.keyEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                boolean isNotEmptyMessageText = charSequence.length() > 0;
                binding.clearKeyButton.setVisibility(isNotEmptyMessageText
                        ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });

        /*
        To display the Share button only when the key text is not empty
         */
        binding.mainTextView.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                boolean isNotEmptyMessageText = charSequence.length() > 0;
                binding.shareMainTextViewButton.setVisibility(isNotEmptyMessageText
                        ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });
    }

    /**
     * Validates if user doesn't click on Encrypt/Decrypt button without entering a message or key
     *
     * @return true if validation is successful
     */
    private boolean validateInput()
    {
        if (binding.keyEditText.getText().toString().isEmpty())
        {
            Toast.makeText(getContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (binding.messageText.getText().toString().isEmpty())
        {
            Toast.makeText(getContext(), "Please enter a message", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}