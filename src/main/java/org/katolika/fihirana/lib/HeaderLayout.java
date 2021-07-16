package org.katolika.fihirana.lib;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HeaderLayout extends RelativeLayout {

	private LayoutInflater inflater;
	private static final String TAG = "HeaderLayout";

	public HeaderLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.header_layout, this, true);

		this.findViewById(R.id.search)
				.setOnClickListener(searchOnClickListener);
		this.findViewById(R.id.home)
				.setOnClickListener(homeOnClickListener);
		this.findViewById(R.id.logo_title).setOnClickListener(homeOnClickListener);


	}

	private OnClickListener searchOnClickListener = v -> getContext().startActivity(
			new Intent(getContext(), SearchFormActivity.class));

	private OnClickListener homeOnClickListener = v -> getContext().startActivity(
			new Intent(getContext(), MainActivity.class));

}
