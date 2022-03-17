package control;

import java.util.Random;
import view.ButtonPlay;
import view.ButtonSmile;
import view.GamePanel;
import view.LabelNumber;

public class World {

	private Random rd;
	private int boom;
	
	private ButtonPlay[][] arrayButton;
	private int[][] arrayMin; //boom la -1
	
	private boolean[][] arrayBoolean;
	private int co;
	private boolean[][] arrayCamCo;
	
	private boolean isComplete; //hoan thanh--thua
	private boolean isEnd; // chien thang
	
	private ButtonSmile buttonSmile;
	private LabelNumber lbTime, lbBoom;
	
	private GamePanel game;
	
	public World(int w, int h, int boom, GamePanel game) {
		this.game = game;
		this.boom = boom;
		arrayButton = new ButtonPlay[w][h];
		arrayMin = new int[w][h];
		arrayBoolean = new boolean[w][h];
		arrayCamCo = new boolean[w][h];
		
		rd = new Random();
		
		createArrayMin(boom, w, h);
		dienSo();
		
	}
	
	public void dienSo() {
		for (int i = 0; i < arrayMin.length; i++) {
			for (int j = 0; j < arrayMin[i].length; j++) {
				if(arrayMin[i][j] == 0) { // neu o dang xet ko phai -1: bom
					int count =0; 
					for (int l = i-1; l <= i+1; l++) {// duyet quanh o do
						for (int k = j-1; k <= j+1; k++) {
							if(l>=0 && l<=arrayMin.length-1 && k>=0 && k<=arrayMin[i].length-1)
							if(arrayMin[l][k] == -1) { //neu quanh o co bom
								count++; //dem so bom
							}
						}
					}
					arrayMin[i][j] = count; //gan o dang xet thanh so bom
				}
			}
		}
	}
	
	public void createArrayMin(int boom, int w, int h) {
		int locationX = rd.nextInt(w);
		int locationY = rd.nextInt(h);
		
		arrayMin[locationX][locationY] = -1;
		int count = 1; //1 o vua dc gan bom
		while(count != boom) {
			locationX = rd.nextInt(w);
			locationY = rd.nextInt(h);
			if(arrayMin[locationX][locationY] != -1) {
				
				arrayMin[locationX][locationY] = -1;
				
				count = 0; //reset lai count vi hanh dong phia duoi dem lai bom cua ca mang
							//neu ko reset se cong ca 1 tu luc dau => sai
				for (int i = 0; i < arrayMin.length; i++) {
					for (int j = 0; j < arrayMin[i].length; j++) {
						if(arrayMin[i][j] == -1 ) {
							count++;
						}
					}
				}
			}
		}
		
	}
	
	public boolean clickDouble(int i, int j) {
		boolean isBoom = false;//khong co boom
		for (int l = i-1; l <= i+1; l++) {
			for (int k = j-1; k <= j+1; k++) {
				if(l>=0 && l<=arrayMin.length-1 && k>=0 && k<=arrayMin[i].length-1) {
					if(!arrayCamCo[l][k]) {
						if(arrayMin[l][k] == -1) {
							isBoom = true; //co boom
							arrayButton[l][k].setNumber(12);
							arrayButton[l][k].repaint();
							arrayBoolean[l][k] = true;
						}else if(!arrayBoolean[l][k]) {
							if (arrayMin[l][k] == 0) {
								open(l, k);
							}else {
								arrayButton[l][k].setNumber(arrayMin[l][k]);
								arrayButton[l][k].repaint();
								arrayBoolean[l][k] = true;
							}
						}
					}
					
				}
			}
		}
		if(isBoom) {// co boom
			for (int j2 = 0; j2 < arrayBoolean.length; j2++) {
				for (int k = 0; k < arrayBoolean[i].length; k++) {
					if(arrayMin[j2][k] == -1 && !arrayBoolean[j2][k]) {
						arrayButton[j2][k].setNumber(10);
						arrayButton[j2][k].repaint();
					}
				}
			}
			return false;
		}
		return true;
	}

	public boolean open(int i, int j) {
		
			if(!isComplete && !isEnd) {
				if( !arrayBoolean[i][j]) {
					if(arrayMin[i][j] == 0) {
						
						arrayBoolean[i][j] = true;
						arrayButton[i][j].setNumber(0);
						arrayButton[i][j].repaint();
						if(checkWin()) {
							isEnd = true;
							return false;
						}
						
						for (int l = i-1; l <= i+1; l++) {
							for (int k = j-1; k <= j+1; k++) {
								if(l>=0 && l<=arrayMin.length-1 && k>=0 && k<=arrayMin[i].length-1) {
									if(!arrayBoolean[l][k]) {
										open(l,k);
										
									}
								}
							}
						}
						if (checkWin()) {
							isEnd = true;
							return false;
						}
					}else {
						int number = arrayMin[i][j];
						
						if(number != -1) {
							arrayBoolean[i][j] = true;
							
							arrayButton[i][j].setNumber(number);
							arrayButton[i][j].repaint();
							if(checkWin()) {
								isEnd = true;
								return false;
							}
							return true;
						}
						
					}			
				}
				if(arrayMin[i][j] == -1) {
					arrayButton[i][j].setNumber(11);
					arrayButton[i][j].repaint();
					isComplete = true;
					for (int j2 = 0; j2 < arrayBoolean.length; j2++) {
						for (int k = 0; k < arrayBoolean[i].length; k++) {
							if(arrayMin[j2][k] == -1 && !arrayBoolean[j2][k]) {
								if (j2 != i || k != j) {//khac o co boom da nhan trung
									arrayButton[j2][k].setNumber(10);
									arrayButton[j2][k].repaint();									
								}
							}
						}
					}
					
					return false;
				}else {
					if(checkWin()) {
						isEnd = true;
						
						return false;
					}
					return true;
				}
			}else {
				return false;
			}
		
	}
	
	public boolean checkWin() {
		int count = 0;//dem so boom
		for (int i = 0; i < arrayBoolean.length; i++) {
			for (int j = 0; j < arrayBoolean[i].length; j++) {
				if(!arrayBoolean[i][j]) {
					count ++;
				}
			}
		}
		if(count == boom) {//neu dem het so boom thi tra ra true, tuc la chien thang
			return true;
		}else {
			return false;
		}
	}
	
	
	public void fullTrue() {
		for (int i = 0; i < arrayBoolean.length; i++) {
			for (int j = 0; j < arrayBoolean[i].length; j++) {
				if(!arrayBoolean[i][j]) {
					arrayBoolean[i][j] = true;
				}
			}
		}
	}
	
	public void camCo(int i, int j) {
		if(!arrayBoolean[i][j]) {
			if(arrayCamCo[i][j]) {
				co--;
				arrayCamCo[i][j] = false;
				arrayButton[i][j].setNumber(-1);
				arrayButton[i][j].repaint();
				game.getP1().updateLbBoom();
			}else if(co < boom){
				co++;
				arrayCamCo[i][j] = true;
				arrayButton[i][j].setNumber(9);
				arrayButton[i][j].repaint();
				game.getP1().updateLbBoom();
			}
		}
	}
	
	public ButtonPlay[][] getArrayButton() {
		return arrayButton;
	}

	public void setArrayButton(ButtonPlay[][] arrayButton) {
		this.arrayButton = arrayButton;
	}

	public ButtonSmile getButtonSmile() {
		return buttonSmile;
	}

	public void setButtonSmile(ButtonSmile buttonSmile) {
		this.buttonSmile = buttonSmile;
	}

	public LabelNumber getLbTime() {
		return lbTime;
	}

	public void setLbTime(LabelNumber lbTime) {
		this.lbTime = lbTime;
	}

	public LabelNumber getLbBoom() {
		return lbBoom;
	}

	public void setLbBoom(LabelNumber lbBoom) {
		this.lbBoom = lbBoom;
	}

	public boolean[][] getArrayBoolean() {
		return arrayBoolean;
	}

	public void setArrayBoolean(boolean[][] arrayBoolean) {
		this.arrayBoolean = arrayBoolean;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	public boolean[][] getArrayCamCo() {
		return arrayCamCo;
	}

	public void setArrayCamCo(boolean[][] arrayCamCo) {
		this.arrayCamCo = arrayCamCo;
	}

	public int getCo() {
		return co;
	}

	public void setCo(int co) {
		this.co = co;
	}
	
	
	
}
