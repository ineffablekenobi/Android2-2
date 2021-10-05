package com.example.rough;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class continue_with_google extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    public static GoogleSignInAccount account;
    public static GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_with_google);
        createLoginRequest();
    }


    private void createLoginRequest() {

        // Configure Google Sign In
        String default_web_client_id = "424911167483-d0vnsbb8ua07aqec07gba63euo53nqfd.apps.googleusercontent.com";
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(default_web_client_id)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        account = GoogleSignIn.getLastSignedInAccount(this);

        if(account == null){
            // not signed in
            findViewById(R.id.signingooglebtn).setVisibility(View.VISIBLE);
            findViewById(R.id.signingooglebtn).setOnClickListener(V->signInWithGoogle());
        }else{
            // already signed in
            findViewById(R.id.signingooglebtn).setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Already logged in", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, frontPage.class);

            Bundle bundle = new Bundle();
            bundle.putString("startUp", "no");

            //startActivity(new Intent(getApplicationContext(), rules.class).putExtras(bundle));
            startActivity(intent.putExtras(bundle));
            finish();
        }
    }


    private void signInWithGoogle() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
            Toast.makeText(this, "Log in successful", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, rules.class);
            Bundle bundle = new Bundle();
            bundle.putString("startUp", "yes");
            startActivity(intent.putExtras(bundle));
            finish();
            // Signed in successfully, show authenticated UI.
            findViewById(R.id.signingooglebtn).setVisibility(View.GONE);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("Sing in Failed: ", e.getStatusCode() + "");
        }
    }

}