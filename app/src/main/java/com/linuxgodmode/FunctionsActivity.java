package com.linuxgodmode;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.linuxgodmode.business.ConnectionManager;
import com.jcraft.jsch.JSchException;
import com.linuxgodmode.business.exception.ConnectionNotInitializedException;

import java.io.IOException;

import de.lsa.logic.LinuxCommand;

/**
 * Created by sandro on 10.04.17 for project androidapp.
 */

public class FunctionsActivity extends AppCompatActivity {

    private Handler popupHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functions);
        popupHandler = new Handler();
    }

    public void reboot(View view) {
        try {
            ConnectionManager.getConnectionManager().getCommandManager().executeCommand(LinuxCommand.REBOOT);
        } catch (IOException e) {
            e.printStackTrace();
            this.sendPopup("Error");
            return;
        } catch (JSchException e) {
            e.printStackTrace();
            this.sendPopup("Error with the SSH Connection");
            return;
        } catch (ConnectionNotInitializedException e) {
            e.printStackTrace();
            this.sendPopup("The connection is not initialized");
            return;
        }
        sendPopup("executed Command");
    }

    public void update(View view) {
        try {
            ConnectionManager.getConnectionManager().getCommandManager().executeCommand(LinuxCommand.UPDATE);
        } catch (IOException e) {
            e.printStackTrace();
            this.sendPopup("Error");
            return;
        } catch (JSchException e) {
            e.printStackTrace();
            this.sendPopup("Error with the SSH Connection");
            return;
        } catch (ConnectionNotInitializedException e) {
            e.printStackTrace();
            this.sendPopup("The connection is not initialized");
            return;
        }
        sendPopup("executed Command");
    }

    public void upgrade(View view) {
        try {
            ConnectionManager.getConnectionManager().getCommandManager().executeCommand(LinuxCommand.UPGRADE);
        } catch (IOException e) {
            e.printStackTrace();
            this.sendPopup("Error");
            return;
        } catch (JSchException e) {
            e.printStackTrace();
            this.sendPopup("Error with the SSH Connection");
            return;
        } catch (ConnectionNotInitializedException e) {
            e.printStackTrace();
            this.sendPopup("The connection is not initialized");
            return;
        }
        sendPopup("executed Command");
    }

    private void sendPopup(String message) {
        final Context context = this;
        this.popupHandler.post(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(context).setMessage(message).create().show();
            }
        });

    }
}
