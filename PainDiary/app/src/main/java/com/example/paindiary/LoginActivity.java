package com.example.paindiary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.paindiary.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ActivityLoginBinding binding;
    private String TAG="tag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // WorkRequest uploadWorkRequest =
         //       new OneTimeWorkRequest.Builder(UploadWorker.class)
           //             .build();
        //WorkManager
          //      .getInstance(LoginActivity.this)
            //    .enqueue(uploadWorkRequest);
        PeriodicWorkRequest saveRequest =
                new PeriodicWorkRequest.Builder(UploadWorker.class, 24, TimeUnit.HOURS)
                        .build();
        //KEEP->Replace IF you want to test WorkManager
        WorkManager.getInstance().enqueueUniquePeriodicWork("uoload", ExistingPeriodicWorkPolicy.KEEP,saveRequest);

        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();
        setContentView(view);
        binding.register.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 Intent intent = new Intent();
                                                 intent.setClass(LoginActivity.this, RegActivity.class);
                                                 startActivity(intent);
                                             }
                                         });
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=binding.name.getText().toString();
                String password=binding.pass.getText().toString();
                if(email.isEmpty()||email== null)
                {
                    Toast.makeText(LoginActivity.this,"signInWithEmail:EmptyEmail",Toast.LENGTH_LONG ).show();

                }
                else if(password.isEmpty()||password== null)
                {
                    Toast.makeText(LoginActivity.this,"signInWithEmail:EmptyPassword",Toast.LENGTH_LONG ).show();

                }
               else {

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information

                                                ToastUtil.newToast(LoginActivity.this,"signInWithEmail:success");
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                Intent intent = new Intent();
                                                intent.setClass(LoginActivity.this, MainActivity.class);
                                                startActivity(intent);

                                            } else {
                                                // If sign in fails, display a message to the user.

                                                ToastUtil.newToast(LoginActivity.this,"signInWithEmail:"+task.getException().getMessage());

                                            }
                                        }
                                    }

                            );
                }
            } });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.d("tag","OK!");
            ToastUtil.newToast(LoginActivity.this,"signInWithEmail:success");
             Intent intent = new Intent();
             intent.setClass(LoginActivity.this, MainActivity.class);
             startActivity(intent);
        }
        else
        {
            Log.d("tag","NOTOK!");
        }

    }


}


