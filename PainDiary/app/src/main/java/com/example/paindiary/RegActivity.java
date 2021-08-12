package com.example.paindiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.paindiary.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegActivity  extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ActivityRegisterBinding binding;
    private String TAG="tag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();
        setContentView(view);
        binding.Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(RegActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=binding.name.getText().toString();
                String password=binding.pass.getText().toString();
                String psb=binding.passconfirm.getText().toString();
                if(email.isEmpty()||email== null)
                {

                    ToastUtil.newToast(RegActivity.this,"RegisterWithEmail:EmptyEmail");
                }
                else if(password.isEmpty()||password==null)
                {
                    ToastUtil.newToast(RegActivity.this,"RegisterWithEmail:EmptyPassword");
                }
                else if(!password.equals(psb))
                {
                    ToastUtil.newToast(RegActivity.this,"RegisterWithEmail:PasswordNotEqual");
                }
                else {

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        ToastUtil.newToast(RegActivity.this,"RegisterWithEmail:success");
                                        Intent intent = new Intent();
                                        intent.setClass(RegActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        ToastUtil.newToast(RegActivity.this,"RegisterWithEmail:"+task.getException().getMessage());
                                    }
                                }
                            });
                }


            }
        });

    }
}
