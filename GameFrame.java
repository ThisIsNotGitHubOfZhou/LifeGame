package life;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

public class GameFrame extends JFrame implements ActionListener {
    private static GameFrame frame;//主面板面板
    private JPanel backPanel, centerPanel, bottomPanel;//面板
    private JButton btnOK, btnNextGeneration, btnStart, btnStop, btnExit;//按钮
    private JButton[][] btnBlock;
    private JLabel lblRow, lblCol;
    private JComboBox rowList, colList;
    private boolean[][] isSelected;
    private int maxRow, maxCol;
    private Life life;
    private boolean isRunning;
    private Thread thread;
    private boolean isDead;
 
    public static void main(String arg[]) {//主函数
        frame = new GameFrame("生命游戏V1.0");
    }
 
    public int getMaxRow() {
        return maxRow;
    }
 
    public void setMaxRow(int maxRow) {
        this.maxRow = maxRow;
    }
 
    public int getMaxCol() {
        return maxCol;
    }
 
    public void setMaxCol(int maxCol) {
        this.maxCol = maxCol;
    }
 
    public void initGUI() {
        /**
         * 设计地图生成器界面 *
         */
        if (maxRow == 0) {
            maxRow = 20;
        }
 
        if (maxCol == 0) {
            maxCol = 60;
        }
 
        life = new Life(maxRow, maxCol);
 
        backPanel = new JPanel(new BorderLayout());//边界布局  背景
        centerPanel = new JPanel(new GridLayout(maxRow, maxCol));//网络布局  中央
        bottomPanel = new JPanel();//流水布局  底部
        rowList = new JComboBox();//下拉框
        for (int i = 3; i <= 20; i++) {//向下拉框中添加内容
            rowList.addItem(String.valueOf(i));
        }
        colList = new JComboBox();
        for (int i = 3; i <= 60; i++) {
            colList.addItem(String.valueOf(i));
        }
        rowList.setSelectedIndex(maxRow - 3);
        colList.setSelectedIndex(maxCol - 3);
        btnOK = new JButton("确定");
        btnNextGeneration = new JButton("下一代");
        btnBlock = new JButton[maxRow][maxCol];
        btnStart = new JButton("开始演化");
        btnStop = new JButton("停止演化");
        btnExit = new JButton("退出");
        isSelected = new boolean[maxRow][maxCol];//默认赋值为false
        lblRow = new JLabel("设置行数：");
        lblCol = new JLabel("设置列数：");
        this.setContentPane(backPanel);//将背景面板添加入frame中
 
        backPanel.add(centerPanel, "Center");
        backPanel.add(bottomPanel, "South");
 
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxCol; j++) {
                btnBlock[i][j] = new JButton("");
                btnBlock[i][j].setBackground(Color.WHITE);
                centerPanel.add(btnBlock[i][j]);
            }
        }
 
        bottomPanel.add(lblRow);
        bottomPanel.add(rowList);
        bottomPanel.add(lblCol);
        bottomPanel.add(colList);
        bottomPanel.add(btnOK);
        bottomPanel.add(btnNextGeneration);
        bottomPanel.add(btnStart);
        bottomPanel.add(btnStop);
        bottomPanel.add(btnExit);
 
        // 设置窗口
        this.setSize(900, 620);
        this.setResizable(false);//resizeable值为false时，表示生成的窗体大小是由程序员决定的，用户不可以自由改变该窗体的大小。
        this.setLocationRelativeTo(null); // 让窗口在屏幕居中
 
        // 将窗口设置为可见的
        this.setVisible(true);
 
        this.addWindowListener(new WindowAdapter() {//Frame不能关闭窗口必须加监听
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
        //this指本身这个对象，这个类会实现监听器这个接口
        btnOK.addActionListener(this);
        btnNextGeneration.addActionListener(this);
        btnStart.addActionListener(this);
        btnStop.addActionListener(this);
        btnExit.addActionListener(this);
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxCol; j++) {
                btnBlock[i][j].addActionListener(this);
            }
        }
    }
 
    public GameFrame(String name) {//构造函数
        super(name);
        initGUI();//重新建立了界面?
    }
 
    public void actionPerformed(ActionEvent e) {
    	//getSource得到的组件的名称
        if (e.getSource() == btnOK) {//确定按钮
            frame.setMaxRow(rowList.getSelectedIndex() + 3);//序号加三等于序号对应的值
            frame.setMaxCol(colList.getSelectedIndex() + 3);
            initGUI();//重新生成一下
            life = new Life(getMaxRow(), getMaxCol());
        } else if (e.getSource() == btnNextGeneration) {//下一代按钮
            makeNextGeneration();
        } else if (e.getSource() == btnStart) {//开始演化按钮
            isRunning = true;
            thread = new Thread(new Runnable() {//创造新的进程
                @Override
                public void run() {
                    while (isRunning) {
                        makeNextGeneration();
                        boolean isSame = true;
                        try {
                            Thread.sleep(500);//间隔为500毫秒
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        isDead = true;
                        for(int row = 1; row <= maxRow; row++) {//检查是否结束游戏
                            for (int col = 1; col <= maxCol; col++) {
                                if (life.getGrid()[row][col] != 0) {
                                    isDead = false;
                                    break;
                                }
                            }
                            if (!isDead) {
                                break;
                            }
                        }
                        if (isDead) {
                            JOptionPane.showMessageDialog(null, "生命消失了~");//JOptionPane是一种对话框的便捷使用形式
                            isRunning = false;
                            thread = null;
                        }
                    }
                }
            });
            thread.start();
        } else if (e.getSource() == btnStop) {
            isRunning = false;
            thread = null;//关闭进程
        } else if (e.getSource() == btnExit) {
            System.exit(0);
        } else {//对网格块的操作
            int[][] grid = life.getGrid();
            for (int i = 0; i < maxRow; i++) {
                for (int j = 0; j < maxCol; j++) {
                    if (e.getSource() == btnBlock[i][j]) {//点到哪个块？
                        isSelected[i][j] = !isSelected[i][j];
                        if (isSelected[i][j]) {
                            btnBlock[i][j].setBackground(Color.BLACK);
                            grid[i + 1][j + 1] = 1;
                        } else {
                            btnBlock[i][j].setBackground(Color.WHITE);
                            grid[i + 1][j + 1] = 0;
                        }
                        break;
                    }
                }
            }
            life.setGrid(grid);
        }
    }
 
    private void makeNextGeneration() {
        life.update();
        int[][] grid = life.getGrid();
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxCol; j++) {
                if (grid[i + 1][j + 1] == 1) {
                    btnBlock[i][j].setBackground(Color.BLACK);
                } else {
                    btnBlock[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }
}
