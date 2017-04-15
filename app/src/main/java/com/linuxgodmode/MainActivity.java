package com.linuxgodmode;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.linuxgodmode.business.ConnectionManager;
import com.linuxgodmode.business.exception.ConnectionAlreadyInitializedException;
import com.linuxgodmode.business.exception.ConnectionNotInitializedException;
import com.linuxgodmode.business.exception.InitialisationFailException;
import com.linuxgodmode.login.LoginTextChangedListener;

import de.lsa.logic.CommandUpdateRegistry;
import de.lsa.logic.ICommandManager;
import de.lsa.logic.LinuxCommand;

public class MainActivity extends AppCompatActivity {
    private Button buttonStatistics, buttonTerminal, buttonFunctions;
    private EditText inputIpAdress, inputPort, inputUsername, inputPassword;
    public Button connectButton, disconnectButton;


    @Override
    protected void onStart() {
        super.onStart();

    }

    private void enableButtons(boolean enable) {
        this.buttonStatistics.setEnabled(enable);
        this.buttonTerminal.setEnabled(enable);
        this.buttonFunctions.setEnabled(enable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init
        this.buttonStatistics = (Button) findViewById(R.id.button_statistics);
        this.buttonTerminal = (Button) findViewById(R.id.button_terminal);
        this.buttonFunctions = (Button) findViewById(R.id.button_functions);

        this.connectButton = (Button) findViewById(R.id.bn_start);
        this.disconnectButton = (Button) findViewById(R.id.bn_stop);

        this.inputIpAdress = (EditText) findViewById(R.id.in_ip);
        this.inputPassword = (EditText) findViewById(R.id.in_pw);
        this.inputPort = (EditText) findViewById(R.id.in_port);
        this.inputUsername = (EditText) findViewById(R.id.in_user);

        this.connectButton.setEnabled(false);

        TextWatcher textChangeListener = new LoginTextChangedListener(this);

        this.inputIpAdress.addTextChangedListener(textChangeListener);
        this.inputPassword.addTextChangedListener(textChangeListener);
        this.inputPort.addTextChangedListener(textChangeListener);
        this.inputUsername.addTextChangedListener(textChangeListener);


        this.enableButtons(false);

        // Thread.setDefaultUncaughtExceptionHandler((paramThread, paramThrowable) -> {
        //    Log.e("Error" + Thread.currentThread().getStackTrace()[2], paramThrowable.getLocalizedMessage());
        //});

        buttonStatistics.setOnClickListener(v -> {
            if (!ConnectionManager.getConnectionManager().isConnected()) {
                sendPopup(getString(R.string.not_connected));
                return;
            }

            Intent button2 = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(button2);
        });

        buttonTerminal.setOnClickListener(v -> {
            if (!ConnectionManager.getConnectionManager().isConnected()) {
                sendPopup(getString(R.string.not_connected));
                return;
            }
            Intent button = new Intent(MainActivity.this, TerminalActivity.class);
            startActivity(button);
        });


        buttonFunctions.setOnClickListener(v -> {
            if (!ConnectionManager.getConnectionManager().isConnected()) {
                sendPopup(getString(R.string.not_connected));
                return;
            }
            Intent functionsButton = new Intent(MainActivity.this, FunctionsActivity.class);
            startActivity(functionsButton);
        });


        CommandUpdateRegistry.registerListener(new StatisticsActivity());

    }

    public boolean checkInputVadility() {
        String host = this.inputIpAdress.getText().toString();
        String raw_port = this.inputPort.getText().toString();
        String user = this.inputUsername.getText().toString();
        String password = this.inputPassword.getText().toString();

        if (host.isEmpty()) {
            return false;
        }
        if (raw_port.isEmpty()) {
            return false;
        }
        if (user.isEmpty()) {
            return false;
        }
        if (password.isEmpty()) {
            return false;
        }

        try {
            Integer.valueOf(raw_port);
        } catch (NumberFormatException exc) {
            return false;
        }
        return true;
    }

    public void saveVar(View view) {
        if (view == null) {
            return;
        }
        String host = this.inputIpAdress.getText().toString();
        int port = Integer.valueOf(this.inputPort.getText().toString());
        String user = this.inputUsername.getText().toString();
        String password = this.inputPassword.getText().toString();

        if (ConnectionManager.getConnectionManager().isConnected()) {
            sendPopup("Please close the running connection first!");
            return;
        }

        new Thread(() -> {
            try {
                ConnectionManager.getConnectionManager().initConnection(user, host, password, port);
            } catch (ConnectionAlreadyInitializedException e) {
                sendPopup(e.getMessage());
            } catch (InitialisationFailException e) {
                sendPopup(e.getMessage());
            }
            setConnected(true);
            runOnUiThread(() -> {
                this.connectButton.setEnabled(false);
                this.disconnectButton.setEnabled(true);
            });
            try {
                initLoopCommands(ConnectionManager.getConnectionManager().getCommandManager());
            } catch (ConnectionNotInitializedException e) {
                sendPopup(e.getMessage());
            }
        }).start();

    }

    private void initLoopCommands(ICommandManager commandManager) {
        commandManager.loopCommand(LinuxCommand.HOSTNAME);
        commandManager.loopCommand(LinuxCommand.CPU_USAGE);
        commandManager.loopCommand(LinuxCommand.UPTIME);
        commandManager.loopCommand(LinuxCommand.TEMPERATURE);
        commandManager.loopCommand(LinuxCommand.DATE);
        commandManager.loopCommand(LinuxCommand.LANGUAGE);
        commandManager.loopCommand(LinuxCommand.TRAFFIC);
    }

    public void disconnect(View view) {
        ConnectionManager connectionManager = ConnectionManager.getConnectionManager();
        if (!connectionManager.isConnected()) {
            sendPopup(getString(R.string.not_connected));
        } else {

            try {
                connectionManager.getCommandManager().clearLoopCommands();
                connectionManager.uninitConnection();
            } catch (ConnectionNotInitializedException e) {
                sendPopup(e.getMessage());
            }
            setConnected(false);
            runOnUiThread(() -> {
                this.disconnectButton.setEnabled(false);
                this.connectButton.setEnabled(true);
            });
        }
    }

    private void sendPopup(String message) {
        final Context context = this;
        runOnUiThread(() -> new AlertDialog.Builder(context).setMessage(message).create().show());

    }

    public void setConnected(boolean status) {
        runOnUiThread(() -> {
            this.enableButtons(status);
            final TextView connectionTextView = (TextView) findViewById(R.id.connectedView);
            connectionTextView.setText(status ? getString(R.string.connected) : getString(R.string.disconnected));
        });
    }
}