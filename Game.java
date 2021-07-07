import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel {
	JButton special1 = new JButton(new ImageIcon("1.gif"));
	JButton special2 = new JButton(new ImageIcon("2.png"));
	JButton special3 = new JButton(new ImageIcon("3.gif"));
	JButton special4 = new JButton(new ImageIcon("4.gif"));
	JButton special5 = new JButton(new ImageIcon("5.gif"));
	JButton special6 = new JButton(new ImageIcon("6.gif"));


	private final Point[][][] Tetraminos = {
			// I-Piece
			{ { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) } },

			// J-Piece
			{ { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) } },

			// L-Piece
			{ { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) } },

			// O-Piece
			{ { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) } },

			// S-Piece
			{ { new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
					{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) } },

			// T-Piece
			{ { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
					{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
					{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) } },

			// Z-Piece
			{ { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
					{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
					{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
					{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) } } };

	private final Color[] tetraminoColors = { Color.cyan, Color.blue, Color.orange, Color.yellow, Color.green,
			Color.pink, Color.red };

	private Point pieceOrigin, pieceOrigin2;
	private int currentPiece, currentPiece2;
	private int rotation, rotation2;
	private ArrayList<Integer> nextPieces = new ArrayList<Integer>();
	private ArrayList<Integer> nextPieces2 = new ArrayList<Integer>();
	private Color[][] well;
	public boolean Awin = false;
	public boolean Bwin = false;
	public boolean lose_dialog = false;
	public int Abase = 0;
	public int Bbase = 0;
	public int Aswitch = 3;
	public int Bswitch = 3;
	public int game_width = 34;
	public int game_height = 25;
	public String Aname = "";
	public String Bname = "";
	public long starttime = System.currentTimeMillis();
	public int time_step = 0;

	// Put a new, random piece into the dropping position
	public void newPiece() {
		pieceOrigin = new Point(5, 2);
		rotation = 0;
		if (nextPieces.isEmpty()) {
			Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
			Collections.shuffle(nextPieces);
		}
		currentPiece = nextPieces.get(0);
		nextPieces.remove(0);
	}

	public void newPiece2() {
		pieceOrigin2 = new Point(22, 2);
		rotation2 = 0;
		if (nextPieces2.isEmpty()) {
			Collections.addAll(nextPieces2, 0, 1, 2, 3, 4, 5, 6);
			Collections.shuffle(nextPieces2);
		}
		currentPiece2 = nextPieces2.get(0);
		nextPieces2.remove(0);
	}

	private boolean collidesAt(int x, int y, int rotation) {
		for (Point p : Tetraminos[currentPiece][rotation]) {
			if (well[p.x + x][p.y + y] != Color.BLACK) {
				return true;
			}
		}
		return false;
	}

	private boolean collidesAt2(int x, int y, int rotation) {
		for (Point p : Tetraminos[currentPiece2][rotation]) {
			if (well[p.x + x][p.y + y] != Color.BLACK) {
				return true;
			}
		}
		return false;
	}

	// Rotate the piece clockwise or counterclockwise
	public void rotate(int i) {
		int newRotation = (rotation + i) % 4;
		if (newRotation < 0) {
			newRotation = 3;
		}
		if (!collidesAt(pieceOrigin.x, pieceOrigin.y, newRotation)) {
			rotation = newRotation;
		}
		repaint();
	}

	public void rotate2(int i) {
		int newRotation = (rotation2 + i) % 4;
		if (newRotation < 0) {
			newRotation = 3;
		}
		if (!collidesAt2(pieceOrigin2.x, pieceOrigin2.y, newRotation)) {
			rotation2 = newRotation;
		}
		repaint();
	}

	// Move the piece left or right
	public void move(int i) {
		if (!collidesAt(pieceOrigin.x + i, pieceOrigin.y, rotation)) {
			pieceOrigin.x += i;
		}
		repaint();
	}

	public void move2(int i) {
		if (!collidesAt2(pieceOrigin2.x + i, pieceOrigin2.y, rotation2)) {
			pieceOrigin2.x += i;
		}
		repaint();
	}

	public void dropDown() {
		if (!collidesAt(pieceOrigin.x, pieceOrigin.y + 1, rotation)) {
			pieceOrigin.y += 1;
		} else {
			fixToWell();
		}
		repaint();
	}

	public void dropDown2() {
		if (!collidesAt2(pieceOrigin2.x, pieceOrigin2.y + 1, rotation2)) {
			pieceOrigin2.y += 1;
		} else {
			fixToWell2();
		}
		repaint();
	}

	public void gameover(String name) {
		if (!lose_dialog) {
			lose_dialog = true;
			int response = JOptionPane.showConfirmDialog(null, name + "輸了!", "提示", JOptionPane.YES_NO_OPTION);
			System.exit(0);
		}
	}

	public void fixToWell() {
		for (Point p : Tetraminos[currentPiece][rotation]) {
			well[pieceOrigin.x + p.x][pieceOrigin.y + p.y] = tetraminoColors[currentPiece];
			if (pieceOrigin.y + p.y == time_step) {
				gameover(Aname);
				Bwin = true;
			}
		}

		clearRows();
		newPiece();
	}

	public void fixToWell2() {
		for (Point p : Tetraminos[currentPiece2][rotation2]) {
			well[pieceOrigin2.x + p.x][pieceOrigin2.y + p.y] = tetraminoColors[currentPiece2];
			if (pieceOrigin2.y + p.y == time_step) {
				gameover(Bname);
				Awin = true;
			}
		}
		clearRows2();
		newPiece2();
	}

	public void deleteRow(int row) {
		for (int j = row - 1; j > 0; j--) {
			for (int i = 1; i < 11; i++) {
				well[i][j + 1] = well[i][j];
			}
		}
	}

	public void deleteRow2(int row) {
		for (int j = row - 1; j > 0; j--) {
			for (int i = 18; i < 29; i++) {
				well[i][j + 1] = well[i][j];
			}
		}
	}

	// 消除增加對方高度
	public void clearRows() {
		boolean gap;
		for (int j = game_height - 2 - Abase; j > 0; j--) {
			gap = false;
			for (int i = 1; i < 12; i++) {
				if (well[i][j] == Color.BLACK) {
					gap = true;
					break;
				}
			}
			if (!gap) {
				deleteRow(j);
				j += 1;
				Bbase += 1;
				for (int m = 0; m < game_height - 1; m++) {
					for (int n = 18; n < 30; n++) {
						well[n][m] = well[n][m + 1];
					}
				}
			}
		}
	}

	public void clearRows2() {
		boolean gap;
		for (int j = game_height - 2 - Bbase; j > 0; j--) {
			gap = false;
			for (int i = 18; i < 29; i++) {
				if (well[i][j] == Color.BLACK) {
					gap = true;
					break;
				}
			}
			if (!gap) {
				deleteRow2(j);
				j += 1;
				Abase += 1;
				for (int m = 0; m < game_height - 1; m++) {
					for (int n = 1; n < 12; n++) {
						well[n][m] = well[n][m + 1];
					}
				}
			}
		}
	}

	private void drawPiece(Graphics g) {
		g.setColor(tetraminoColors[currentPiece]);
		for (Point p : Tetraminos[currentPiece][rotation]) {
			g.fillRect((p.x + pieceOrigin.x) * 26, (p.y + pieceOrigin.y) * 26, 25, 25);
		}
	}

	private void drawPiece2(Graphics g) {
		g.setColor(tetraminoColors[currentPiece2]);
		for (Point p : Tetraminos[currentPiece2][rotation2]) {
			g.fillRect((p.x + pieceOrigin2.x) * 26, (p.y + pieceOrigin2.y) * 26, 25, 25);
		}
	}

	private void init() {
		Awin = false;
		Bwin = false;
		lose_dialog = false;
		Abase = 0;
		Bbase = 0;
		Aswitch = 3;
		Bswitch = 3;
		time_step = 0;
		starttime = System.currentTimeMillis();

		this.setLayout(null);
		this.add(special1);
		special1.setBounds(340, 250, 100, 100);
		this.add(special2);
		special2.setBounds(340, 400, 100, 100);
		this.add(special3);
		special3.setBounds(340, 550, 100, 100);
		this.add(special4);
		special4.setBounds(782, 250, 100, 100);
		this.add(special5);
		special5.setBounds(782, 400, 100, 100);
		this.add(special6);
		special6.setBounds(782, 550, 100, 100);

		well = new Color[game_width][game_height];
		for (int i = 0; i < game_width; i++) {
			for (int j = 0; j < game_height; j++) {
				if ((i >= 13 && i <= 16) || (i >= 30 && i <= 33))
					well[i][j] = Color.WHITE;
				else if ((i == 0) || (i == 12) || (i == 17) || (i == 29) || (j == game_height - 1))
					well[i][j] = Color.GRAY;
				else
					well[i][j] = Color.BLACK;

			}
		}
		newPiece();
		newPiece2();
	}

	@Override
	public void paintComponent(Graphics g) {

		g.fillRect(0, 0, 26 * game_width, 26 * game_height);
		g.setColor(Color.WHITE);
		g.fillRect(26 * 13, 0, 26 * 4, 26 * game_height);
		g.fillRect(26 * 30, 0, 26 * 4, 26 * game_height);

		for (int i = 0; i < game_width; i++) {
			for (int j = 0; j < game_height; j++) {
				g.setColor(well[i][j]);
				g.fillRect(26 * i, 26 * j, 25, 25);
			}
		}

		// 頂端會隨時間往下
		g.setColor(Color.RED);
		long clicktime = System.currentTimeMillis();
		time_step = (int) (clicktime - starttime) / 5000;
		for (int i = 0; i < 13 * 26; i += 26) {
			int[] x = { 0 + i, 13 + i, 26 + i };
			int[] y = { 26 * time_step, 26 + 26 * time_step, 26 * time_step };
			g.fillPolygon(x, y, 3);
		}
		for (int i = 17 * 26; i < 26 * 30; i += 26) {
			int[] x = { 0 + i, 13 + i, 26 + i };
			int[] y = { 26 * time_step, 26 + 26 * time_step, 26 * time_step };
			g.fillPolygon(x, y, 3);
		}

		drawPiece(g);
		drawPiece2(g);

		if (nextPieces.isEmpty()) {
			Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
			Collections.shuffle(nextPieces);
		}

		if (nextPieces2.isEmpty()) {
			Collections.addAll(nextPieces2, 0, 1, 2, 3, 4, 5, 6);
			Collections.shuffle(nextPieces2);
		}

		// 預覽下個方塊

		g.setColor(Color.BLACK);
		g.drawString("玩家1：" + Aname, 14 * 26, 32);
		g.setColor(tetraminoColors[nextPieces.get(0)]);
		g.drawString("下個方塊", 14 * 26, 32 * 3);
		for (Point p : Tetraminos[nextPieces.get(0)][0]) {
			g.fillRect((p.x + 13) * 26, (p.y + 4) * 26, 25, 25);
		}

		g.setColor(Color.BLACK);
		g.drawString("替換次數" + Integer.toString(Aswitch), 14 * 25, 32 * 7);

		g.setColor(Color.BLACK);
		g.drawString("玩家2：" + Bname, 31 * 26, 32);
		g.setColor(tetraminoColors[nextPieces2.get(0)]);
		g.drawString("下個方塊", 31 * 26, 32 * 3);
		for (Point p : Tetraminos[nextPieces2.get(0)][0]) {
			g.fillRect((p.x + 30) * 26, (p.y + 4) * 26, 25, 25);
		}

		g.setColor(Color.BLACK);
		g.drawString("替換次數" + Integer.toString(Bswitch), 32 * 25, 32 * 7);
	}

	int speed = 1000;

	public static void main(String[] args) {
		final JFrame f = new JFrame("Game");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBounds(330, 200, 1300, 700);
		f.setVisible(true);

		String Aname = JOptionPane.showInputDialog("請輸入P1名稱");
		String Bname = JOptionPane.showInputDialog("請輸入P2名稱");

		// 左遊戲
		final Game game = new Game();
		game.init();
		game.Aname = Aname;
		game.Bname = Bname;
		f.add(game);

		// 右面板
		JPanel jpane1 = new JPanel();
		jpane1.setLayout(new BorderLayout());
		jpane1.setBorder(BorderFactory.createEmptyBorder(50, 10, 10, 50));
		JButton newGame = new JButton("重置");
		newGame.setFont(new Font("Monospaced", Font.PLAIN, 30));
		newGame.setPreferredSize(new Dimension(100, 100));
		jpane1.add(newGame, BorderLayout.NORTH);

		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				game.init();
				f.requestFocus();
			}
		});

		JSlider slider;
		slider = new JSlider(JSlider.VERTICAL, 0, 10, 5);
		slider.setPaintTicks(true);
		slider.setMinorTickSpacing(5);
		slider.setPreferredSize(new Dimension(10, 30));
		// 自訂掉落速度
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				JSlider s = (JSlider) evt.getSource();
				if (!s.getValueIsAdjusting()) {
					game.speed = 1000 - (int) s.getValue() * 90;
				}
				f.requestFocus();
			}
		});
		jpane1.add(slider, BorderLayout.CENTER);

		JLabel myspeed = new JLabel("速度調整");
		myspeed.setFont(new Font("Monospaced", Font.PLAIN, 30));
		// jpane1.add(myspeed, BorderLayout.SOUTH);

		JTextArea description = new JTextArea("遊戲說明：\n" + "★P1 透過WASD鍵控制\n" + "★P2 透過上下左右鍵控制\n" + "★有3次機會點擊方塊替換下個方塊\n"
				+ "★點擊重置按鈕重新開始遊戲\n" + "★拖動滑塊改變掉落速度\n" + "★可以透過消除列增加對方高度\n" + "★尖刺會隨著時間往下\n" + "★先碰到尖刺者為失敗", 10, 10);
		description.setFont(new Font("PMingLiU", Font.PLAIN, 20));
		jpane1.add(description, BorderLayout.SOUTH);
		f.add(jpane1, BorderLayout.EAST);

		/* JFrame會被JPanel蓋過，導致無法使用KeyListener，加了才能控制 */
		f.setVisible(true);
		f.requestFocus();

		f.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
					case 87:
						game.rotate(-1);
						break;
					case 65:
						game.move(-1);
						break;
					case 68:
						game.move(+1);
						break;
					case 83:
						game.dropDown();
						break;
					case KeyEvent.VK_UP:
						game.rotate2(-1);
						break;
					case KeyEvent.VK_LEFT:
						game.move2(-1);
						break;
					case KeyEvent.VK_RIGHT:
						game.move2(+1);
						break;
					case KeyEvent.VK_DOWN:
						game.dropDown2();
						break;
				}
			}

			public void keyReleased(KeyEvent e) {
			}
		});

		// 可以自選下個方塊
		game.special1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (game.Aswitch > 0) {
					game.nextPieces.set(0, 4);
					game.Aswitch -= 1;
					f.requestFocus();
				}
			}
		});
		game.special2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (game.Aswitch > 0) {
					game.nextPieces.set(0, 3);
					game.Aswitch -= 1;
					f.requestFocus();
				}
			}
		});
		game.special3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (game.Aswitch > 0) {
					game.nextPieces.set(0, 0);
					game.Aswitch -= 1;
					f.requestFocus();
				}
			}
		});
		game.special4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (game.Bswitch > 0) {
					game.nextPieces2.set(0, 5);
					game.Bswitch -= 1;
					f.requestFocus();
				}
			}
		});
		game.special5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (game.Bswitch > 0) {
					game.nextPieces2.set(0, 2);
					game.Bswitch -= 1;
					f.requestFocus();
				}
			}
		});
		game.special6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (game.Bswitch > 0) {
					game.nextPieces2.set(0, 6);
					game.Bswitch -= 1;
					f.requestFocus();
				}
			}
		});

		// Make the falling piece drop every second
		new Thread() {
			@Override
			public void run() {
				while (!game.Awin && !game.Bwin) {
					try {
						Thread.sleep(game.speed);
						game.dropDown();
						game.dropDown2();
					} catch (InterruptedException e) {
					}
				}
			}
		}.start();
	}
}