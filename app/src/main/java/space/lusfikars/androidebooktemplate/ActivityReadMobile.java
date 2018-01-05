package space.lusfikars.androidebooktemplate;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.util.logging.Logger;

/**
 * Created by laptop.baru.bagus on 1/5/2018.
 */

public class ActivityReadMobile extends Activity implements OnPageChangeListener, OnLoadCompleteListener {
    private static final Logger LOG = Logger.getLogger(ActivityReadMobile.class.getName());

    PDFView pdfView;

    Integer pageNumber = 0;

    private Button btnDownloadSC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_mobile);

        btnDownloadSC = (Button)findViewById(R.id.btnDownloadSC);

        pdfView = (PDFView)findViewById(R.id.pdfView);
        pdfView.setBackgroundColor(Color.DKGRAY);
        pdfView.fromAsset(Utils.namaBuku)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .load();

        btnDownloadSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlString=Utils.downloadSCLink;
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(urlString));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    }
}
