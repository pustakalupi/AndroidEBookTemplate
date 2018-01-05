package space.lusfikars.androidebooktemplate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

public class ActivityMain extends AppCompatActivity {

    private static final Logger LOG = Logger.getLogger(ActivityMain.class.getName());

    private Button btnReadMobile;
    private Button btnReadPCLaptop;
    private TextView txvServerInfo;

    private WebBookServer server;
    int randomPort = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvServerInfo = (TextView)findViewById(R.id.txvServerInfo);
        btnReadMobile = (Button)findViewById(R.id.btnReadMobileDevice);

        btnReadMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent( getApplicationContext(), ActivityReadMobile.class);
                startActivity(myIntent);
            }
        });

        btnReadPCLaptop = (Button)findViewById(R.id.btnReadPCLaptop);
        btnReadPCLaptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnReadPCLaptop.getText().equals("Baca di PC/Laptop")){

                    randomPort = getRandomPort();
                    if(randomPort == -1){
                        Toast.makeText(getApplicationContext(), "Error: No port available!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    server = new WebBookServer(getApplicationContext(), randomPort);
                    try {
                        server.doStart();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }

                    String msg = "Buka PC/Laptop Browser Anda ke: " + "http://" + Utils.getIPAddress(true) + ":" + Integer.toString(randomPort);

                    txvServerInfo.setText(msg);

                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    btnReadPCLaptop.setText("Berhenti Baca di PC/Laptop");

                } else if(btnReadPCLaptop.getText().equals("Berhenti Baca di PC/Laptop")){

                    if(server != null) {
                        server.doStop();

                        String msg = "Server tidak berjalan";

                        txvServerInfo.setText(msg);

                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                        btnReadPCLaptop.setText("Baca di PC/Laptop");
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(getApplicationContext(),"onResume", Toast.LENGTH_SHORT).show();
        if(server == null){
            //Toast.makeText(getApplicationContext(),"server is null", Toast.LENGTH_SHORT).show();
            return;
        }

        if(server.isAlive() == true){
            if(btnReadPCLaptop != null && txvServerInfo != null){
                String msg = "Buka PC/Laptop Browser Anda ke: " + "http://" + Utils.getIPAddress(true) + ":" + Integer.toString(randomPort);

                txvServerInfo.setText(msg);

                btnReadPCLaptop.setText("Berhenti Baca di PC/Laptop");
            }
        } else {
            if(btnReadPCLaptop != null && txvServerInfo != null) {
                String msg = "Server tidak berjalan";

                txvServerInfo.setText(msg);

                btnReadPCLaptop.setText("Baca di PC/Laptop");
            }
        }
        //Toast.makeText(getApplicationContext(),"on resume", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(getApplicationContext(),"onDestroy", Toast.LENGTH_SHORT).show();

        if(server != null){
            server.doStop();
        }
    }

    private int getRandomPort(){
        //return 8080;

        ServerSocket socket;
        int port = 0;
        try {
            socket = new ServerSocket(0);
            port = socket.getLocalPort();
            socket.close();
        } catch (IOException e) {
            port = -1;
        }
        return port;
    }
}
