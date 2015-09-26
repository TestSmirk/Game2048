package com.testsmirk.game2048;

import java.util.ArrayList;
import java.util.List;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.Toast;

public class GameView extends GridLayout {
	private Card[][] cardMap = new Card[4][4];
	private LayoutTransition layoutTransition;
	private List<Point> emptyPoint = new ArrayList<Point>();

	public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		initGameView();
		initTransition();

	}

	private void initTransition() {
		layoutTransition = new LayoutTransition();
		setLayoutTransition(layoutTransition);
		layoutTransition.setStagger(LayoutTransition.CHANGE_APPEARING, 20);
		layoutTransition.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 20);
		ValueAnimator animator = ObjectAnimator.ofFloat(90)
				.setDuration(layoutTransition.getDuration(LayoutTransition.CHANGE_APPEARING));
		layoutTransition.setAnimator(LayoutTransition.CHANGE_APPEARING, animator);

	}

	private void initGameView() {
		setBackgroundColor(0xffbbada0);
		setColumnCount(4);
		setRowCount(4);
		// TODO Auto-generated method stub
		setOnTouchListener(new OnTouchListener() {
			private float startX, startY, offsetX, offsetY;

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				switch (arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = arg1.getX();
					startY = arg1.getY();
					break;
				case MotionEvent.ACTION_UP:
					offsetX = arg1.getX() - startX;
					offsetY = arg1.getY() - startY;
					if (Math.abs(offsetX) > Math.abs(offsetY)) {
						if (offsetX < -5) {
							swipeLeft();
						} else if (offsetX > 5) {
							swipeRight();
						}
					} else {
						if (offsetY < -5) {
							swipeUp();
						} else if (offsetY > 5) {
							swipeDown();
						}
					}
					break;

				default:
					break;
				}
				return true;
			}
		});

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		int cardWidth = (Math.min(w, h) - 10) / 4;
		addCard(cardWidth, cardWidth);
		startGame();
	}

	private void addCard(int cardWidth, int cardHeight) {

		Card c;
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				c = new Card(getContext());
				c.setNum(0);
				addView(c, cardHeight, cardWidth);
				cardMap[x][y] = c;
			}
		}

	}

	private void addRandomNum() {
		emptyPoint.clear();
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (cardMap[x][y].getNum() <= 0) {
					emptyPoint.add(new Point(x, y));
				}
			}
		}
		Point p = emptyPoint.remove((int) (Math.random() * emptyPoint.size()));
		cardMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);
		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_up_to_down);
		cardMap[p.x][p.y].startAnimation(animation);

	}

	private void startGame() {
		MainActivity.getMainActivity().clearScore();
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				cardMap[x][y].setNum(0);

			}

		}
		addRandomNum();
		addRandomNum();
	}

	private void swipeLeft() {
		boolean merge = false;
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				for (int x1 = x + 1; x1 < 4; x1++) {
					if (cardMap[x1][y].getNum() > 0) {
						if (cardMap[x][y].getNum() <= 0) {
							cardMap[x][y].setNum(cardMap[x1][y].getNum());
							cardMap[x1][y].setNum(0);
							x--;
							Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_up_to_down);
							merge = true;
						} else if (cardMap[x][y].equals(cardMap[x1][y])) {
							cardMap[x][y].setNum(cardMap[x][y].getNum() * 2);
							cardMap[x1][y].setNum(0);
							MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
							merge = true;
						}
						break;
					}
				}
			}
		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}

	private void swipeRight() {
		boolean merge = false;
		for (int y = 0; y < 4; y++) {
			for (int x = 3; x >= 0; x--) {
				for (int x1 = x - 1; x1 >= 0; x1--) {
					if (cardMap[x1][y].getNum() > 0) {
						if (cardMap[x][y].getNum() <= 0) {
							cardMap[x][y].setNum(cardMap[x1][y].getNum());
							cardMap[x1][y].setNum(0);
							x++;
							merge = true;
						} else if (cardMap[x][y].equals(cardMap[x1][y])) {
							cardMap[x][y].setNum(cardMap[x][y].getNum() * 2);
							cardMap[x1][y].setNum(0);
							MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
							merge = true;
						}
						break;
					}
				}

			}

		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}

	private void swipeUp() {
		boolean merge = false;
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				for (int y1 = y + 1; y1 < 4; y1++) {
					if (cardMap[x][y1].getNum() > 0) {
						if (cardMap[x][y].getNum() <= 0) {
							cardMap[x][y].setNum(cardMap[x][y1].getNum());
							cardMap[x][y1].setNum(0);
							y--;
							merge = true;
						} else if (cardMap[x][y].equals(cardMap[x][y1])) {
							cardMap[x][y].setNum(cardMap[x][y].getNum() * 2);
							cardMap[x][y1].setNum(0);
							MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
							merge = true;
						}
						break;
					}
				}

			}

		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}

	private void swipeDown() {
		boolean merge = false;
		for (int x = 0; x < 4; x++) {
			for (int y = 3; y >= 0; y--) {
				for (int y1 = y - 1; y1 >= 0; y1--) {
					if (cardMap[x][y1].getNum() > 0) {
						if (cardMap[x][y].getNum() <= 0) {
							cardMap[x][y].setNum(cardMap[x][y1].getNum());
							cardMap[x][y1].setNum(0);
							y++;
							merge = true;
						} else if (cardMap[x][y].equals(cardMap[x][y1])) {
							cardMap[x][y].setNum(cardMap[x][y].getNum() * 2);
							cardMap[x][y1].setNum(0);
							MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
							merge = true;
						}
						break;
					}
				}

			}

		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}

	private void checkComplete() {
		boolean complete = true;
		ALL: for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (cardMap[x][y].getNum() == 0 || (x > 0 && cardMap[x][y].equals(cardMap[x - 1][y])
						|| (x < 3 && cardMap[x][y].equals(cardMap[x + 1][y])
								|| (y > 0 && cardMap[x][y].equals(cardMap[x][y - 1])
										|| (y < 3 && cardMap[x][y].equals(cardMap[x][y + 1])))))) {
					complete = false;
					break ALL;

				}

			}

		}
		if (complete) {
			new AlertDialog.Builder(getContext()).setTitle("你好").setMessage("游戏结束")
					.setPositiveButton("1", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							startGame();
						}
					}).show();
		}
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGameView();
		// TODO Auto-generated constructor stub
	}

	public GameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initGameView();
	}

}
