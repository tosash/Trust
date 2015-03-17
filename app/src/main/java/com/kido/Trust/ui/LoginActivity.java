package com.kido.Trust.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kido.Trust.R;
import com.kido.Trust.parse.ParseErrorResolver;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends Activity {

    private EditText usernameEdit;
    private EditText passwordEdit;
    private Button signInButton;
    private Button signUpButton;
    private Intent todoListActivityIntent;
    private ParseErrorResolver parseErrResolver;
    private Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_DeviceDefault_Light_Dialog);
        setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_login);
        usernameEdit = (EditText) findViewById(R.id.login_username);
        passwordEdit = (EditText) findViewById(R.id.login_password);
        signInButton = (Button) findViewById(R.id.sign_in);
        signUpButton = (Button) findViewById(R.id.sign_up);
        todoListActivityIntent = new Intent(this, FragmentTreeList.class);
        parseErrResolver = new ParseErrorResolver();
    }

    @Override
    public void onBackPressed() {
        // prevent "back" from leaving this activity
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.exit_title)
                .setMessage(R.string.exit_quetion)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        System.exit(RESULT_OK);
                        finishAffinity();
                    }

                })
                .setNegativeButton(R.string.no, null)
                .show();
    }


    public void onButtonClicked(final View v) {
        String user = usernameEdit.getText().toString();
        String pass = passwordEdit.getText().toString();
        if (user.length() == 0)
            perror(this.getString(R.string.username_missing));
        else if (pass.length() == 0)
            perror(this.getString(R.string.password_missing));

        else if (v == signInButton) {
            signIn(v, user, pass);
        } else if (v == signUpButton) {
            signUp(v, user, pass);
        }
    }

    private void signIn(final View viewToToggle, String user, String pass) {
        viewToToggle.setEnabled(false);
        ParseUser.logInInBackground(user, pass, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                viewToToggle.setEnabled(true);
                if (user != null) {
                    finish();
                } else {
                    perror(parseErrResolver.resolve(LoginActivity.this, e));
                }
            }
        });
    }

    private void signUp(final View viewToToggle, String user, String pass) {
        viewToToggle.setEnabled(false);
        ParseUser paUser = new ParseUser();
        paUser.setUsername(user);
        paUser.setPassword(pass);

        paUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                viewToToggle.setEnabled(true);
                if (e == null) {
                    finish();
                } else {
                    perror(parseErrResolver.resolve(LoginActivity.this, e));
                }
            }
        });
    }


    protected void perror(String msg) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

}
