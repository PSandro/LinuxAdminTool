package com.linuxgodmode;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.linuxgodmode.business.ConnectionManager;
import com.linuxgodmode.business.exception.ConnectionAlreadyInitializedException;
import com.linuxgodmode.business.exception.ConnectionNotInitializedException;
import com.linuxgodmode.business.exception.InitialisationFailException;

import de.lsa.logic.CommandUpdateRegistry;
import de.lsa.logic.ICommandManager;
import de.lsa.logic.LinuxCommand;

public class MainActivity extends AppCompatActivity {
    private ListView SwypeList;
    private ArrayAdapter<String> sAdapter;
    private Handler popupHandler;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //deklaration & initialisation
        this.popupHandler = new Handler();
        final Button buttonStatistics = (Button) findViewById(R.id.button_statistics);
        final Button buttonTerminal = (Button) findViewById(R.id.button_terminal);
        final Button functionsButton = (Button) findViewById(R.id.button_functions);


        buttonStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ConnectionManager.getConnectionManager().isConnected()) {
                    sendPopup("Not connected");
                    return;
                }

                Intent button2 = new Intent(MainActivity.this, StatisticsActivity.class);
                startActivity(button2);
            }
        });

        buttonTerminal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ConnectionManager.getConnectionManager().isConnected()) {
                    sendPopup("Not connected");
                    return;
                }
                Intent button = new Intent(MainActivity.this, TerminalActivity.class);
                startActivity(button);
            }

        });


        functionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ConnectionManager.getConnectionManager().isConnected()) {
                    sendPopup("Not connected");
                    return;
                }
                Intent functionsButton = new Intent(MainActivity.this, FunctionsActivity.class);
                startActivity(functionsButton);
            }

        });


        CommandUpdateRegistry.registerListener(new StatisticsActivity());

    }

    public void saveVar(View view) {
        if (view == null) {
            return;
        }
        EditText in_ip = (EditText) findViewById(R.id.in_ip);
        EditText in_port = (EditText) findViewById(R.id.in_port);
        EditText in_user = (EditText) findViewById(R.id.in_user);
        EditText in_pw = (EditText) findViewById(R.id.in_pw);

        String host = in_ip.getText().toString();
        String raw_port = in_port.getText().toString();
        String user = in_user.getText().toString();
        String password = in_pw.getText().toString();
        Integer port = null;

        if (host.isEmpty()) {
            sendPopup("The host cannot be empty!");
            return;
        }
        if (raw_port.isEmpty()) {
            sendPopup("The port cannot be empty!");
            return;
        }
        if (user.isEmpty()) {
            sendPopup("The user cannot be empty!");
            return;
        }
        if (password.isEmpty()) {
            sendPopup("The password cannot be empty!");
            return;
        }

        try {
            port = Integer.valueOf(raw_port);
        } catch (NumberFormatException exc) {
            sendPopup("The port has to be a number");
        }


        if (ConnectionManager.getConnectionManager().isConnected()) {
            sendPopup("Please close the running connection first!");
            return;
        }

        Integer finalPort = port;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ConnectionManager.getConnectionManager().initConnection(user, host, password, finalPort);
                } catch (ConnectionAlreadyInitializedException e) {
                    sendPopup(e.getMessage());
                    finish();
                } catch (InitialisationFailException e) {
                    sendPopup(e.getMessage());
                    finish();
                }
                setConnected(true);
                try {
                    initLoopCommands(ConnectionManager.getConnectionManager().getCommandManager());
                } catch (ConnectionNotInitializedException e) {
                    sendPopup(e.getMessage());
                    finish();
                }
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
            sendPopup("Not connected");
        } else {

            try {
                connectionManager.getCommandManager().clearLoopCommands();
                connectionManager.uninitConnection();
            } catch (ConnectionNotInitializedException e) {
                sendPopup(e.getMessage());
            }
            setConnected(false);
        }
    }

    private void sendPopup(String message) {
        final Context context = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                popupHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(context).setMessage(message).create().show();
                    }
                });
            }
        });

    }

    public void setConnected(boolean status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final TextView connectionTextView = (TextView) findViewById(R.id.connectedView);
                connectionTextView.setText(status ? "connected" : "disconnected");
            }
        });
    }
}