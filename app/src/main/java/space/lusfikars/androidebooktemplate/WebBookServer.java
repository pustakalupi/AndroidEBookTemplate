package space.lusfikars.androidebooktemplate;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.Map;
import java.util.logging.Logger;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by laptop.baru.bagus on 1/5/2018.
 */

public class WebBookServer extends NanoHTTPD {
    private static final Logger LOG = Logger.getLogger(WebBookServer.class.getName());
    Context context;

    public WebBookServer(Context context) throws IOException {
        super(8080);
        this.context = context;
        doStart();
    }

    public WebBookServer(Context context, int port){
        super(port);
        this.context = context;
    }

    public void doStart() throws IOException{
        start();
        WebBookServer.LOG.info( "Web book server is started" );
    }

    public void doStop(){
        stop();
        WebBookServer.LOG.info( "Web book server is stopped.." );
    }

    @Override
    public Response serve(IHTTPSession session) {
        InputStream fis;
        try {
            fis = context.getResources().getAssets().open(Utils.namaBuku);
        } catch (IOException e) {
            e.printStackTrace();
            return newFixedLengthResponse( "ERROR: Cannot open the pdf asset!" );
        }
        return newChunkedResponse(Response.Status.OK, "application/pdf", fis);
    }
}
