package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {


    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.help_fragment, container, false);
        final WebView webView = (WebView) rootView.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        initialWebKit(webView, this.getActivity().getApplicationContext());
        webView.bringToFront();

        return rootView;

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.help_fragment, container, false);
    }

    private void initialWebKit(WebView webview, Context context) {
        final String mimetype = "text/html";
        final String encoding = "UTF-8";
        String htmlData;

        int contextMenuId = R.raw.help_complete;
        InputStream is = context.getResources().openRawResource(contextMenuId);

        try {
            if (is != null && is.available() > 0) {
                final byte[] bytes = new byte[is.available()];
                is.read(bytes);
                htmlData = new String(bytes);
                webview.loadDataWithBaseURL(null, htmlData, mimetype, encoding, null);
            }
        } catch (IOException ioe) {

        }
    }

}
