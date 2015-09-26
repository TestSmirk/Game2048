package com.testsmirk.game2048;

import android.animation.LayoutTransition;
import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {
	private TextView label;
private LayoutTransition  layoutTransition;
	private int num = 0;

	public Card(Context context) {
		super(context);
		label = new TextView(context);
		label.setTextSize(32);
		label.setGravity(Gravity.CENTER);
		label.setBackgroundColor(0x33ffffff);
		LayoutParams lp = new LayoutParams(-1, -1);
		lp.setMargins(10, 10, 0, 0);
		addView(label, lp);
		setNum(0);
		
		// TODO Auto-generated constructor stub
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
		if (num <= 0){
			label.setText("");

		} else {
			label.setText(num + "");
		}
	}

	public boolean equals(Card o) {
		// TODO Auto-generated method stub
		return getNum() == o.getNum();
	}

}
