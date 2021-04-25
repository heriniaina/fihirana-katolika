package org.katolika.fihirana.lib;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FooterLayout extends RelativeLayout {

	private LayoutInflater inflater;
	public FooterLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.footer_layout, this, true);
        TextView t = (TextView)this.findViewById(R.id.footer_text);
        t.setOnClickListener(searchOnClickListener);
        
    }
	
	private OnClickListener searchOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.katolika.org")));
        }
    };
    

}
