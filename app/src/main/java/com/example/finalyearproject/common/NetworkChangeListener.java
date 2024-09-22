package com.example.finalyearproject.common;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.example.finalyearproject.R;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!NeetworkDetails.isConnectedToInternet(context))
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(context);
            View layoutdialogue = LayoutInflater.from(context).inflate(R.layout.check_internet_connection_dialogue, null);
            ad.setView(layoutdialogue);

            AppCompatButton btnRetry = layoutdialogue.findViewById(R.id.btncheckinternetconnection);

            AlertDialog alertDialog = ad.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);

            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    onReceive(context,intent);
                }
            });



        }
        else
        {
            Toast.makeText(context, "Your internet is connected",Toast.LENGTH_SHORT).show();
        }


    }
}
