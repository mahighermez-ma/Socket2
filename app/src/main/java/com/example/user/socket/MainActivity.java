package com.example.user.socket;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {
    EditText edtmsg;
    private Socket socket;
    Handler handler;
    LinearLayout line123;

    {
        try {
            socket = IO.socket("http://192.168.56.1:8000/");
        } catch (URISyntaxException e) {}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtmsg=(EditText)findViewById(R.id.edtmsg);
        line123=(LinearLayout)findViewById(R.id.line123);
        handler=new Handler();
        socket.connect();
        socket.on("msg",handlerIncomingMessage);

    }
    public Emitter.Listener handlerIncomingMessage=new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject=(JSONObject)args[0];
                    try {
                        Log.e("Exereeee", jsonObject.toString() );
                        String msg="",send="";

                        msg=jsonObject.getString("msg").toString();
                        send=jsonObject.getString("send").toString();
                        Log.e("Exereeee", send+":"+msg);
                        TextView txt=new TextView(getApplicationContext());
                        txt.setText(msg);
                        txt.setTextSize(22);
                        //Log.e("Exereeee21", msg );
                        if (send.equals("me")){
                            txt.setBackgroundColor(Color.YELLOW);
                            txt.setGravity(Gravity.LEFT);
                        }else {
                            txt.setBackgroundColor(Color.BLUE);
                            txt.setGravity(Gravity.RIGHT);
                        }
                        line123.addView(txt);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        socket.disconnect();
        super.onDestroy();
    }
    public void sendmsg(String msg){
        socket.emit("msg",msg);
    }
    public void btnSend(View view){
        sendmsg(edtmsg.getText().toString());
    }
}
