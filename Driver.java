package market;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import java.awt.Color;

import edu.rutgers.cs112.LL.LLNode;

public class Driver extends JFrame {

    private static int WIDTH = 650;
    private static int HEIGHT = 550; 
    private static int HEADER_HEIGHT = 75;
    private static int STOCK_PANEL_HEIGHT = 80;

    private static double HEADER_FONT_SIZE = 1.2;

    private StockMarket market;
    private JPanel mainPanel; 

    private JPanel headerPanel;
    private JLabel daysLabel;
    private JButton nextDayButton;
    private JSpinner daysSpinner;
    private JLabel balanceLabel;
    private JLabel portfolioValueLabel;

    // Tabbed pane and its 3 different sections
    private JTabbedPane tabPanel; 

    private JScrollPane stocksScrollPanel; 
    private JPanel stockList;

    private JScrollPane portfolioScrollPane; 
    private JPanel portfolioPanel;

    private JPanel statsPanel;

    private Timer actionTimer;
    private static final int TIMEOUT = 1000 * 10; // milliseconds

    private static final String[] dates = {"7/9/2020","7/10/2020","7/13/2020","7/14/2020","7/15/2020","7/16/2020","7/17/2020","7/20/2020","7/21/2020","7/22/2020","7/23/2020","7/24/2020","7/27/2020","7/28/2020","7/29/2020","7/30/2020","7/31/2020","8/3/2020","8/4/2020","8/5/2020","8/6/2020","8/7/2020","8/10/2020","8/11/2020","8/12/2020","8/13/2020","8/14/2020","8/17/2020","8/18/2020","8/19/2020","8/20/2020","8/21/2020","8/24/2020","8/25/2020","8/26/2020","8/27/2020","8/28/2020","8/31/2020","9/1/2020","9/2/2020","9/3/2020","9/4/2020","9/8/2020","9/9/2020","9/10/2020","9/11/2020","9/14/2020","9/15/2020","9/16/2020","9/17/2020","9/18/2020","9/21/2020","9/22/2020","9/23/2020","9/24/2020","9/25/2020","9/28/2020","9/29/2020","9/30/2020","10/1/2020","10/2/2020","10/5/2020","10/6/2020","10/7/2020","10/8/2020","10/9/2020","10/12/2020","10/13/2020","10/14/2020","10/15/2020","10/16/2020","10/19/2020","10/20/2020","10/21/2020","10/22/2020","10/23/2020","10/26/2020","10/27/2020","10/28/2020","10/29/2020","10/30/2020","11/2/2020","11/3/2020","11/4/2020","11/5/2020","11/6/2020","11/9/2020","11/10/2020","11/11/2020","11/12/2020","11/13/2020","11/16/2020","11/17/2020","11/18/2020","11/19/2020","11/20/2020","11/23/2020","11/24/2020","11/25/2020","11/27/2020","11/30/2020","12/1/2020","12/2/2020","12/3/2020","12/4/2020","12/7/2020","12/8/2020","12/9/2020","12/10/2020","12/11/2020","12/14/2020","12/15/2020","12/16/2020","12/17/2020","12/18/2020","12/21/2020","12/22/2020","12/23/2020","12/24/2020","12/28/2020","12/29/2020","12/30/2020","12/31/2020","1/4/2021","1/4/2021","1/5/2021","1/5/2021","1/6/2021","1/6/2021","1/7/2021","1/7/2021","1/8/2021","1/8/2021","1/11/2021","1/11/2021","1/12/2021","1/12/2021","1/13/2021","1/13/2021","1/14/2021","1/14/2021","1/15/2021","1/15/2021","1/19/2021","1/19/2021","1/20/2021","1/20/2021","1/21/2021","1/21/2021","1/22/2021","1/22/2021","1/25/2021","1/25/2021","1/26/2021","1/26/2021","1/27/2021","1/27/2021","1/28/2021","1/28/2021","1/29/2021","1/29/2021","2/1/2021","2/1/2021","2/2/2021","2/2/2021","2/3/2021","2/3/2021","2/4/2021","2/4/2021","2/5/2021","2/5/2021","2/8/2021","2/8/2021","2/9/2021","2/9/2021","2/10/2021","2/10/2021","2/11/2021","2/11/2021","2/12/2021","2/12/2021","2/16/2021","2/16/2021","2/17/2021","2/17/2021","2/18/2021","2/18/2021","2/19/2021","2/19/2021","2/22/2021","2/22/2021","2/23/2021","2/23/2021","2/24/2021","2/24/2021","2/25/2021","2/25/2021","2/26/2021","2/26/2021","3/1/2021","3/1/2021","3/2/2021","3/2/2021","3/3/2021","3/3/2021","3/4/2021","3/4/2021","3/5/2021","3/5/2021","3/8/2021","3/8/2021","3/9/2021","3/9/2021","3/10/2021","3/10/2021","3/11/2021","3/11/2021","3/12/2021","3/12/2021","3/15/2021","3/15/2021","3/16/2021","3/16/2021","3/17/2021","3/17/2021","3/18/2021","3/18/2021","3/19/2021","3/19/2021","3/22/2021","3/22/2021","3/23/2021","3/23/2021","3/24/2021","3/24/2021","3/25/2021","3/25/2021","3/26/2021","3/26/2021","3/29/2021","3/29/2021","3/30/2021","3/30/2021","3/31/2021","3/31/2021","4/1/2021","4/1/2021","4/5/2021","4/5/2021","4/6/2021","4/6/2021","4/7/2021","4/7/2021","4/8/2021","4/8/2021","4/9/2021","4/9/2021","4/12/2021","4/12/2021","4/13/2021","4/13/2021","4/14/2021","4/14/2021","4/15/2021","4/15/2021","4/16/2021","4/16/2021","4/19/2021","4/19/2021","4/20/2021","4/20/2021","4/21/2021","4/21/2021","4/22/2021","4/22/2021","4/23/2021","4/23/2021","4/26/2021","4/26/2021","4/27/2021","4/27/2021","4/28/2021","4/28/2021","4/29/2021","4/29/2021","4/30/2021","4/30/2021","5/3/2021","5/3/2021","5/4/2021","5/4/2021","5/5/2021","5/5/2021","5/6/2021","5/6/2021","5/7/2021","5/7/2021","5/10/2021","5/10/2021","5/11/2021","5/11/2021","5/12/2021","5/12/2021","5/13/2021","5/13/2021","5/14/2021","5/14/2021","5/17/2021","5/17/2021","5/18/2021","5/18/2021","5/19/2021","5/19/2021","5/20/2021","5/20/2021","5/21/2021","5/21/2021","5/24/2021","5/24/2021","5/25/2021","5/25/2021","5/26/2021","5/26/2021","5/27/2021","5/27/2021","5/28/2021","5/28/2021","6/1/2021","6/1/2021","6/2/2021","6/2/2021","6/3/2021","6/3/2021","6/4/2021","6/4/2021","6/7/2021","6/7/2021","6/8/2021","6/8/2021","6/9/2021","6/9/2021","6/10/2021","6/10/2021","6/11/2021","6/11/2021","6/14/2021","6/14/2021","6/15/2021","6/15/2021","6/16/2021","6/16/2021","6/17/2021","6/17/2021","6/18/2021","6/18/2021","6/21/2021","6/21/2021","6/22/2021","6/22/2021","6/23/2021","6/23/2021","6/24/2021","6/24/2021","6/25/2021","6/25/2021","6/28/2021","6/28/2021","6/29/2021","6/29/2021","6/30/2021","6/30/2021","7/1/2021","7/1/2021","7/2/2021","7/2/2021","7/6/2021","7/6/2021","7/7/2021","7/7/2021","7/8/2021","7/8/2021","7/9/2021","7/9/2021","7/12/2021","7/12/2021","7/13/2021","7/13/2021","7/14/2021","7/14/2021","7/15/2021","7/15/2021","7/16/2021","7/16/2021","7/19/2021","7/19/2021","7/20/2021","7/20/2021","7/21/2021","7/21/2021","7/22/2021","7/22/2021","7/23/2021","7/23/2021","7/26/2021","7/26/2021","7/27/2021","7/27/2021","7/28/2021","7/28/2021","7/29/2021","7/29/2021","7/30/2021","7/30/2021","8/2/2021","8/2/2021","8/3/2021","8/3/2021","8/4/2021","8/4/2021","8/5/2021","8/5/2021","8/6/2021","8/6/2021","8/9/2021","8/9/2021","8/10/2021","8/10/2021","8/11/2021","8/11/2021","8/12/2021","8/12/2021","8/13/2021","8/13/2021","8/16/2021","8/16/2021","8/17/2021","8/17/2021","8/18/2021","8/18/2021","8/19/2021","8/19/2021","8/20/2021","8/20/2021","8/23/2021","8/23/2021","8/24/2021","8/24/2021","8/25/2021","8/25/2021","8/26/2021","8/26/2021","8/27/2021","8/27/2021","8/30/2021","8/30/2021","8/31/2021","8/31/2021","9/1/2021","9/1/2021","9/2/2021","9/2/2021","9/3/2021","9/3/2021","9/7/2021","9/7/2021","9/8/2021","9/8/2021","9/9/2021","9/9/2021","9/10/2021","9/10/2021","9/13/2021","9/13/2021","9/14/2021","9/14/2021","9/15/2021","9/15/2021","9/16/2021","9/16/2021","9/17/2021","9/17/2021","9/20/2021","9/20/2021","9/21/2021","9/21/2021","9/22/2021","9/22/2021","9/23/2021","9/23/2021","9/24/2021","9/24/2021","9/27/2021","9/27/2021","9/28/2021","9/28/2021","9/29/2021","9/29/2021","9/30/2021","9/30/2021","10/1/2021","10/1/2021","10/4/2021","10/4/2021","10/5/2021","10/5/2021","10/6/2021","10/6/2021","10/7/2021","10/7/2021","10/8/2021","10/8/2021","10/11/2021","10/11/2021","10/12/2021","10/12/2021","10/13/2021","10/13/2021","10/14/2021","10/14/2021","10/15/2021","10/15/2021","10/18/2021","10/18/2021","10/19/2021","10/19/2021","10/20/2021","10/20/2021","10/21/2021","10/21/2021","10/22/2021","10/22/2021","10/25/2021","10/25/2021","10/26/2021","10/26/2021","10/27/2021","10/27/2021","10/28/2021","10/28/2021","10/29/2021","10/29/2021","11/1/2021","11/1/2021","11/2/2021","11/2/2021","11/3/2021","11/3/2021","11/4/2021","11/4/2021","11/5/2021","11/5/2021","11/8/2021","11/8/2021","11/9/2021","11/9/2021","11/10/2021","11/10/2021","11/11/2021","11/11/2021","11/12/2021","11/12/2021","11/15/2021","11/15/2021","11/16/2021","11/16/2021","11/17/2021","11/17/2021","11/18/2021","11/18/2021","11/19/2021","11/19/2021","11/22/2021","11/22/2021","11/23/2021","11/23/2021","11/24/2021","11/24/2021","11/26/2021","11/26/2021","11/29/2021","11/29/2021","11/30/2021","11/30/2021","12/1/2021","12/1/2021","12/2/2021","12/2/2021","12/3/2021","12/3/2021","12/6/2021","12/6/2021","12/7/2021","12/7/2021","12/8/2021","12/8/2021","12/9/2021","12/9/2021","12/10/2021","12/10/2021","12/13/2021","12/13/2021","12/14/2021","12/14/2021","12/15/2021","12/15/2021","12/16/2021","12/16/2021","12/17/2021","12/17/2021","12/20/2021","12/20/2021","12/21/2021","12/21/2021","12/22/2021","12/22/2021","12/23/2021","12/23/2021","12/27/2021","12/27/2021","12/28/2021","12/28/2021","12/29/2021","12/29/2021","12/30/2021","12/30/2021","12/31/2021","12/31/2021","1/3/2022","1/3/2022","1/4/2022","1/5/2022","1/6/2022","1/7/2022","1/10/2022","1/11/2022","1/12/2022","1/13/2022","1/14/2022","1/18/2022","1/19/2022","1/20/2022","1/21/2022","1/24/2022","1/25/2022","1/26/2022","1/27/2022","1/28/2022","1/31/2022","2/1/2022","2/2/2022","2/3/2022","2/4/2022","2/7/2022","2/8/2022","2/9/2022","2/10/2022","2/11/2022","2/14/2022","2/15/2022","2/16/2022","2/17/2022","2/18/2022","2/22/2022","2/23/2022","2/24/2022","2/25/2022","2/28/2022","3/1/2022","3/2/2022","3/3/2022","3/4/2022","3/7/2022","3/8/2022","3/9/2022","3/10/2022","3/11/2022","3/14/2022","3/15/2022","3/16/2022","3/17/2022","3/18/2022","3/21/2022","3/22/2022","3/23/2022","3/24/2022","3/25/2022","3/28/2022","3/29/2022","3/30/2022","3/31/2022","4/1/2022","4/4/2022","4/5/2022","4/6/2022","4/7/2022","4/8/2022","4/11/2022","4/12/2022","4/13/2022","4/14/2022","4/18/2022","4/19/2022","4/20/2022","4/21/2022","4/22/2022","4/25/2022","4/26/2022","4/27/2022","4/28/2022","4/29/2022","5/2/2022","5/3/2022","5/4/2022","5/5/2022","5/6/2022","5/9/2022","5/10/2022","5/11/2022","5/12/2022","5/13/2022","5/16/2022","5/17/2022","5/18/2022","5/19/2022","5/20/2022","5/23/2022","5/24/2022","5/25/2022","5/26/2022","5/27/2022","5/31/2022","6/1/2022","6/2/2022","6/3/2022","6/6/2022","6/7/2022","6/8/2022","6/9/2022","6/10/2022","6/13/2022","6/14/2022","6/15/2022","6/16/2022","6/17/2022","6/21/2022","6/22/2022","6/23/2022","6/24/2022","6/27/2022","6/28/2022","6/29/2022","6/30/2022","7/1/2022","7/5/2022","7/6/2022","7/7/2022","7/8/2022","7/11/2022","7/12/2022","7/13/2022","7/14/2022","7/15/2022","7/18/2022","7/19/2022","7/20/2022","7/21/2022","7/22/2022","7/25/2022","7/26/2022","7/27/2022","7/28/2022","7/29/2022","8/1/2022","8/2/2022","8/3/2022","8/4/2022","8/5/2022","8/8/2022","8/9/2022","8/10/2022","8/11/2022","8/12/2022","8/15/2022","8/16/2022","8/17/2022","8/18/2022","8/19/2022","8/22/2022","8/23/2022","8/24/2022","8/25/2022","8/26/2022","8/29/2022","8/30/2022","8/31/2022","9/1/2022","9/2/2022","9/6/2022","9/7/2022","9/8/2022","9/9/2022","9/12/2022","9/13/2022","9/14/2022","9/15/2022","9/16/2022","9/19/2022","9/20/2022","9/21/2022","9/22/2022","9/23/2022","9/26/2022","9/27/2022","9/28/2022","9/29/2022","9/30/2022","10/3/2022","10/4/2022","10/5/2022","10/6/2022","10/7/2022","10/10/2022","10/11/2022","10/12/2022","10/13/2022","10/14/2022","10/17/2022","10/18/2022","10/19/2022","10/20/2022","10/21/2022","10/24/2022","10/25/2022","10/26/2022","10/27/2022","10/28/2022","10/31/2022","11/1/2022","11/2/2022","11/3/2022","11/4/2022","11/7/2022","11/8/2022","11/9/2022","11/10/2022","11/11/2022","11/14/2022","11/15/2022","11/16/2022","11/17/2022","11/18/2022","11/21/2022","11/22/2022","11/23/2022","11/25/2022","11/28/2022","11/29/2022","11/30/2022","12/1/2022","12/2/2022","12/5/2022","12/6/2022","12/7/2022","12/8/2022","12/9/2022","12/12/2022","12/13/2022","12/14/2022","12/15/2022","12/16/2022","12/19/2022","12/20/2022","12/21/2022","12/22/2022","12/23/2022","12/27/2022","12/28/2022","12/29/2022","12/30/2022","1/3/2023","1/4/2023","1/5/2023","1/6/2023","1/9/2023","1/10/2023","1/11/2023","1/12/2023","1/13/2023","1/17/2023","1/18/2023","1/19/2023","1/20/2023","1/23/2023","1/24/2023","1/25/2023","1/26/2023","1/27/2023","1/30/2023","1/31/2023","2/1/2023","2/2/2023","2/3/2023","2/6/2023","2/7/2023","2/8/2023","2/9/2023","2/10/2023","2/13/2023","2/14/2023","2/15/2023","2/16/2023","2/17/2023","2/21/2023","2/22/2023","2/23/2023","2/24/2023","2/27/2023","2/28/2023","3/1/2023","3/2/2023","3/3/2023","3/6/2023","3/7/2023","3/8/2023","3/9/2023","3/10/2023","3/13/2023","3/14/2023","3/15/2023","3/16/2023","3/17/2023","3/20/2023","3/21/2023","3/22/2023","3/23/2023","3/24/2023","3/27/2023","3/28/2023","3/29/2023","3/30/2023","3/31/2023","4/3/2023","4/4/2023","4/5/2023","4/6/2023","4/10/2023","4/11/2023","4/12/2023","4/13/2023","4/14/2023","4/17/2023","4/18/2023","4/19/2023","4/20/2023","4/21/2023","4/24/2023","4/25/2023","4/26/2023","4/27/2023","4/28/2023","5/1/2023","5/2/2023","5/3/2023","5/4/2023","5/5/2023","5/8/2023","5/9/2023","5/10/2023","5/11/2023","5/12/2023","5/15/2023","5/16/2023","5/17/2023","5/18/2023","5/19/2023","5/22/2023","5/23/2023","5/24/2023","5/25/2023","5/26/2023","5/30/2023","5/31/2023","6/1/2023","6/2/2023","6/5/2023","6/6/2023","6/7/2023","6/8/2023","6/9/2023","6/12/2023","6/13/2023","6/14/2023","6/15/2023","6/16/2023","6/20/2023","6/21/2023","6/22/2023","6/23/2023","6/26/2023","6/27/2023","6/28/2023","6/29/2023","6/30/2023","7/3/2023","7/5/2023","7/6/2023","7/7/2023","7/10/2023","7/11/2023","7/12/2023","7/13/2023","7/14/2023","7/17/2023","7/18/2023","7/19/2023","7/20/2023","7/21/2023","7/24/2023","7/25/2023","7/26/2023","7/27/2023","7/28/2023","7/31/2023","8/1/2023","8/2/2023","8/3/2023","8/4/2023","8/7/2023","8/8/2023","8/9/2023","8/10/2023","8/11/2023","8/14/2023","8/15/2023","8/16/2023","8/17/2023","8/18/2023","8/21/2023","8/22/2023","8/23/2023","8/24/2023","8/25/2023","8/28/2023","8/29/2023","8/30/2023","8/31/2023","9/1/2023","9/5/2023","9/6/2023","9/7/2023","9/8/2023","9/11/2023","9/12/2023","9/13/2023","9/14/2023","9/15/2023","9/18/2023","9/19/2023","9/20/2023","9/21/2023","9/22/2023","9/25/2023","9/26/2023","9/27/2023","9/28/2023","9/29/2023","10/2/2023","10/3/2023","10/4/2023","10/5/2023","10/6/2023","10/9/2023","10/10/2023","10/11/2023","10/12/2023","10/13/2023","10/16/2023","10/17/2023","10/18/2023","10/19/2023","10/20/2023","10/23/2023","10/24/2023","10/25/2023","10/26/2023","10/27/2023","10/30/2023","10/31/2023","11/1/2023","11/2/2023","11/3/2023","11/6/2023","11/7/2023","11/8/2023","11/9/2023","11/10/2023","11/13/2023","11/14/2023","11/15/2023","11/16/2023","11/17/2023","11/20/2023","11/21/2023","11/22/2023","11/24/2023","11/27/2023","11/28/2023","11/29/2023","11/30/2023","12/1/2023","12/4/2023","12/5/2023","12/6/2023","12/7/2023","12/8/2023","12/11/2023","12/12/2023","12/13/2023","12/14/2023","12/15/2023","12/18/2023","12/19/2023","12/20/2023","12/21/2023","12/22/2023","12/26/2023","12/27/2023","12/28/2023","12/29/2023","1/2/2024","1/3/2024","1/4/2024","1/5/2024","1/8/2024","1/9/2024","1/10/2024","1/11/2024","1/12/2024","1/16/2024","1/17/2024","1/18/2024","1/19/2024","1/22/2024","1/23/2024","1/24/2024","1/25/2024","1/26/2024","1/29/2024","1/30/2024","1/31/2024","2/1/2024","2/2/2024","2/5/2024","2/6/2024","2/7/2024","2/8/2024","2/9/2024","2/12/2024","2/13/2024","2/14/2024","2/15/2024","2/16/2024","2/20/2024","2/21/2024","2/22/2024","2/23/2024","2/26/2024","2/27/2024","2/28/2024","2/29/2024","3/1/2024","3/4/2024","3/5/2024","3/6/2024","3/7/2024","3/8/2024","3/11/2024","3/12/2024","3/13/2024","3/14/2024","3/15/2024","3/18/2024","3/19/2024","3/20/2024","3/21/2024","3/22/2024","3/25/2024","3/26/2024","3/27/2024","3/28/2024","4/1/2024","4/2/2024","4/3/2024","4/4/2024","4/5/2024","4/8/2024","4/9/2024","4/10/2024","4/11/2024","4/12/2024","4/15/2024","4/16/2024","4/17/2024","4/18/2024","4/19/2024","4/22/2024","4/23/2024","4/24/2024","4/25/2024","4/26/2024","4/29/2024","4/30/2024","5/1/2024","5/2/2024","5/3/2024","5/6/2024","5/7/2024","5/8/2024","5/9/2024","5/10/2024","5/13/2024","5/14/2024","5/15/2024","5/16/2024","5/17/2024","5/20/2024","5/21/2024","5/22/2024","5/23/2024","5/24/2024","5/28/2024","5/29/2024","5/30/2024","5/31/2024","6/3/2024","6/4/2024","6/5/2024","6/6/2024","6/7/2024","6/10/2024","6/11/2024","6/12/2024","6/13/2024","6/14/2024","6/17/2024","6/18/2024","6/20/2024","6/21/2024","6/24/2024","6/25/2024","6/26/2024","6/27/2024","6/28/2024","7/1/2024","7/2/2024","7/3/2024","7/5/2024","7/8/2024","7/9/2024","7/10/2024","7/11/2024","7/12/2024","7/15/2024","7/16/2024","7/17/2024","7/18/2024","7/19/2024","7/22/2024","7/23/2024","7/24/2024","7/25/2024","7/26/2024","7/29/2024","7/30/2024","7/31/2024","8/1/2024","8/2/2024","8/5/2024","8/6/2024","8/7/2024","8/8/2024","8/9/2024","8/12/2024","8/13/2024","8/14/2024","8/15/2024","8/16/2024","8/19/2024","8/20/2024","8/21/2024","8/22/2024","8/23/2024","8/26/2024","8/27/2024","8/28/2024","8/29/2024","8/30/2024","9/3/2024","9/4/2024","9/5/2024","9/6/2024","9/9/2024","9/10/2024","9/11/2024","9/12/2024","9/13/2024","9/16/2024","9/17/2024","9/18/2024","9/19/2024","9/20/2024","9/23/2024","9/24/2024","9/25/2024","9/26/2024","9/27/2024","9/30/2024","10/1/2024","10/2/2024","10/3/2024","10/4/2024","10/7/2024","10/8/2024","10/9/2024","10/10/2024","10/11/2024","10/14/2024","10/15/2024","10/16/2024","10/17/2024","10/18/2024","10/21/2024","10/22/2024","10/23/2024","10/24/2024","10/25/2024","10/28/2024","10/29/2024","10/30/2024","10/31/2024","11/1/2024","11/4/2024","11/5/2024","11/6/2024","11/7/2024","11/8/2024","11/11/2024","11/12/2024","11/13/2024","11/14/2024","11/15/2024","11/18/2024","11/19/2024","11/20/2024","11/21/2024","11/22/2024","11/25/2024","11/26/2024","11/27/2024","11/29/2024","12/2/2024","12/3/2024","12/4/2024","12/5/2024","12/6/2024","12/9/2024","12/10/2024","12/11/2024","12/12/2024","12/13/2024","12/16/2024","12/17/2024","12/18/2024","12/19/2024","12/20/2024","12/23/2024","12/24/2024","12/26/2024","12/27/2024","12/30/2024","12/31/2024","1/2/2025","1/3/2025","1/6/2025","1/7/2025","1/8/2025","1/10/2025","1/13/2025","1/14/2025","1/15/2025","1/16/2025","1/17/2025","1/21/2025","1/22/2025","1/23/2025","1/24/2025","1/27/2025","1/28/2025","1/29/2025","1/30/2025","1/31/2025","2/3/2025","2/4/2025","2/5/2025","2/6/2025","2/7/2025","2/10/2025","2/11/2025","2/12/2025","2/13/2025","2/14/2025","2/18/2025","2/19/2025","2/20/2025","2/21/2025","2/24/2025","2/25/2025","2/26/2025","2/27/2025","2/28/2025","3/3/2025","3/4/2025","3/5/2025","3/6/2025","3/7/2025","3/10/2025","3/11/2025","3/12/2025","3/13/2025","3/14/2025","3/17/2025","3/18/2025","3/19/2025","3/20/2025","3/21/2025","3/24/2025","3/25/2025","3/26/2025","3/27/2025","3/28/2025","3/31/2025","4/1/2025","4/2/2025","4/3/2025","4/4/2025","4/7/2025","4/8/2025","4/9/2025","4/10/2025","4/11/2025","4/14/2025","4/15/2025","4/16/2025","4/17/2025","4/21/2025","4/22/2025","4/23/2025","4/24/2025","4/25/2025","4/28/2025","4/29/2025","4/30/2025","5/1/2025","5/2/2025","5/5/2025","5/6/2025","5/7/2025","5/8/2025","5/9/2025","5/12/2025","5/13/2025","5/14/2025","5/15/2025","5/16/2025","5/19/2025","5/20/2025","5/21/2025","5/22/2025","5/23/2025","5/27/2025","5/28/2025","5/29/2025","5/30/2025","6/2/2025","6/3/2025","6/4/2025","6/5/2025","6/6/2025","6/9/2025","6/10/2025","6/11/2025","6/12/2025","6/13/2025","6/16/2025","6/17/2025","6/18/2025","6/20/2025","6/23/2025","6/24/2025","6/25/2025","6/26/2025","6/27/2025","6/30/2025","7/1/2025","7/2/2025","7/3/2025","7/7/2025","7/8/2025","7/9/2025"};
    private static double[] netWorthHistory;

    public Driver() { 
        super("Stock Market");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // show dialog box with input for starting balance
        String input = JOptionPane.showInputDialog(this, "Enter starting balance:", "10000.00");
        double startingBalance = 10000.00;
        try {
            startingBalance = Double.parseDouble(input);
            if (startingBalance < 0) { 
                JOptionPane.showMessageDialog(this, "Negative balance not allowed. Starting balance set to $10000.00", "Error", JOptionPane.ERROR_MESSAGE);
                startingBalance = 10000.00;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Starting balance set to $10000.00", "Error", JOptionPane.ERROR_MESSAGE);
            startingBalance = 10000.00;
        }
 
        String filepath = JOptionPane.showInputDialog(this, "Enter input file:", "stock_data.csv");
        if (!Files.exists(Paths.get(filepath))) {
            JOptionPane.showMessageDialog(this, "File not found. Using default file 'stock_data.csv' in project root.", "Error", JOptionPane.ERROR_MESSAGE);
            filepath = "stock_data.csv";
            if (!Files.exists(Paths.get(filepath))) {
                JOptionPane.showMessageDialog(this, "stock_data.csv not found. Make sure you've opened the innermost 'StockMarket' folder directly in VSCode.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }

        String file = filepath;
        market = new StockMarket(startingBalance); 
        market.readStockData(file); 
        if (market.getStockData() == null || market.getStockData().length == 0) {
            JOptionPane.showMessageDialog(this, "No stock data loaded. Make sure to implement readStockData() before running the Driver.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } 
        if (!market.nextDay()) {
            JOptionPane.showMessageDialog(this, "nextDay() returned false. There may be an error in your solution to this method.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        netWorthHistory = new double[market.getStockData()[0].getPriceHistory().length];
        netWorthHistory[0] = startingBalance;

        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        mainPanel.setLayout(new BorderLayout());

        headerPanel = createHeaderPanel();
        tabPanel = createTabPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabPanel, BorderLayout.CENTER);

        this.add(mainPanel);
        this.pack();
        this.setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(); 
        panel.setPreferredSize(new Dimension(WIDTH, HEADER_HEIGHT));
        panel.setLayout(new GridLayout(2, 4));
        panel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
 
        JPanel daysWrapperPanel = new JPanel();
        daysLabel = new JLabel("Date: "+ dates[market.getCurrentDay()]); 
        daysWrapperPanel.add(daysLabel);
        nextDayButton = new JButton("Advance Day(s)");    
        daysSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 365, 1)); 
        JPanel buttonWrapperPanel = new JPanel();
        buttonWrapperPanel.add(daysSpinner);
        buttonWrapperPanel.add(nextDayButton); 
        nextDayButton.addActionListener(e -> nextDay());
        balanceLabel = new JLabel("Balance: $" + String.format("%.2f", market.getBalance()));

        double totalPortfolioValue = getPortfolioValue();
        portfolioValueLabel = new JLabel (String.format("Portfolio: $%,.2f", totalPortfolioValue));
  
        FontMetrics fm = daysLabel.getFontMetrics(daysLabel.getFont());
        int fontHeight = fm.getHeight();    
        double increaseFactor = HEADER_FONT_SIZE;
        daysLabel.setFont(daysLabel.getFont().deriveFont((float)(fontHeight * increaseFactor)));
        balanceLabel.setFont(balanceLabel.getFont().deriveFont((float)(fontHeight * increaseFactor)));
        portfolioValueLabel.setFont(portfolioValueLabel.getFont().deriveFont((float)(fontHeight * increaseFactor)));
 
        panel.add(portfolioValueLabel);  
        panel.add(daysWrapperPanel); 
        panel.add(balanceLabel);
        panel.add(buttonWrapperPanel);

        return panel;
    }

    private double getPortfolioValue() {
        double total = 0.0;
        try {
            LLNode<Holding> current = market.getPortfolio();
            while (current != null) {
                Holding holding = current.getData();
                if (market.getCurrentDay() < holding.getStock().getPriceHistory().length) {
                    total += holding.getStock().getPriceHistory()[market.getCurrentDay()] * holding.getQuantity();
                }
                current = current.getNext();
            }
        } catch (Exception e) { 
            System.err.println("Error calculating portfolio value: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error calculating portfolio value. The displayed value may be inaccurate.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return total;
    }

    private void nextDay() {
        if (market == null) {
            JOptionPane.showMessageDialog(this, "Stock Market is null. Make sure ", "Error", JOptionPane.ERROR_MESSAGE);
        } 
        for (int i = 0; i < (int)daysSpinner.getValue(); i++) {
            netWorthHistory[market.getCurrentDay()] = market.getBalance() + getPortfolioValue();
            if (!market.nextDay()) {
                endGame();
            }
        }
        updateStockPanel();
        updatePortfolioPanel();
        updateStatsPanel();
        daysLabel.setText("Date: " + dates[market.getCurrentDay()]);
        balanceLabel.setText("Balance: $" + String.format("%.2f", market.getBalance()));
        portfolioValueLabel.setText(String.format("Portfolio: $%,.2f", getPortfolioValue())); 
    }

    private HashMap<Stock, String> findHoldingInfo() {
        HashMap<Stock, String> stockROIs = new HashMap<>();
        LLNode<Holding> current = market.getPortfolio();
        HashMap<Stock, ArrayList<Holding>> stockHoldingsMap = new HashMap<>();
        while (current != null) {
            Holding holding = current.getData();
            Stock stock = holding.getStock();
            stockHoldingsMap.putIfAbsent(stock, new ArrayList<>());
            stockHoldingsMap.get(stock).add(holding);
            current = current.getNext();
        }
        for (Stock s : stockHoldingsMap.keySet()) {
            ArrayList<Holding> holdings = stockHoldingsMap.get(s);
            int totalQuantity = 0;
            double totalCost = 0.0;
            for (Holding h : holdings) {
                totalQuantity += h.getQuantity();
                totalCost += h.getQuantity() * h.getStock().getPriceHistory()[h.getPurchaseDay()];
            }
            double avgCost = totalCost / totalQuantity;
            double currentPrice = s.getPriceHistory()[market.getCurrentDay() - 1];
            double roi = ((currentPrice - avgCost) / avgCost) * 100;
            String info = String.format("Qty: %d | Total Cost: $%.2f | Profit: $%.2f | ROI: %.2f%%", 
                                        totalQuantity, totalCost, (currentPrice*totalQuantity) - totalCost, roi);
            stockROIs.put(s, info);
        }
        return stockROIs;
    }
    
    private void endGame() {
        HashMap<Stock, String> stockROIs = findHoldingInfo();
        int numHoldings = market.getHoldings();
        int uniqueHoldings = stockROIs.size();

        SwingUtilities.invokeLater(() -> {
            JFrame endFrame = new JFrame("Simulation Over");
            endFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            endFrame.setSize(475, 375);
            endFrame.setLocationRelativeTo(null);

            String startDate = dates[0];
            String endDate = dates[market.getCurrentDay() - 1];
            double startBalance = netWorthHistory[0];

            double total = 0.0;
            LLNode<Holding> current = market.getPortfolio();
            while (current != null) {
                Holding holding = current.getData();
                total += holding.getStock().getPriceHistory()[market.getCurrentDay() - 1] * holding.getQuantity();
                current = current.getNext();
            }
            double endBalance = market.getBalance() + total;
 
            JComboBox<String> roiComboBox = new JComboBox<>();
            for (Map.Entry<Stock, String> entry : stockROIs.entrySet()) { 
                String roiInfo = entry.getValue();
                roiComboBox.addItem(roiInfo);
            }
            String message = String.format(
                "<html><div style='text-align:center;'>" +
                "<h2>Simulation Over!</h2>" +
                "<div style='margin-top:18px; margin-bottom:10px;'>" +
                "<b>Start Date:</b> %s<br/>" +
                "<b>End Date:</b> %s<br/>" +
                "</div>" +
                "<div style='margin-top:10px; margin-bottom:10px;'>" +
                "<b>Start Net Worth:</b> $%,.2f<br/>" +
                "<b>End Net Worth:</b> $%,.2f" +
                "</div>" +
                "<div style='margin-top:10px; margin-bottom:10px;'>" +
                "<b>Num Holdings:</b> %d<br/>" + 
                "<b>Unique Holdings:</b> %d<br/>" + 
                "</div>" +
                "</div></html>",
                startDate, endDate, startBalance, endBalance, numHoldings, uniqueHoldings
            );

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(28, 24, 28, 24));

            JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
            Font font = messageLabel.getFont();
            messageLabel.setFont(font.deriveFont(Font.PLAIN, font.getSize() + 4f));
            messageLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

            contentPanel.add(Box.createVerticalGlue());
            contentPanel.add(messageLabel);
            contentPanel.add(Box.createVerticalGlue());
            contentPanel.add(roiComboBox);
            contentPanel.add(Box.createVerticalGlue());

            endFrame.setContentPane(contentPanel);
            endFrame.setMinimumSize(new Dimension(340, 260));
            endFrame.setVisible(true);
        }); 
        this.dispose();
    }
    private JTabbedPane createTabPanel() {
        JTabbedPane tabbedPane = new JTabbedPane(); 
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
                int tabCount = tabbedPane.getTabCount() < 3 ? 3 : tabbedPane.getTabCount(); // three tabs by default, so set the min as 3
                return (mainPanel.getPreferredSize().width / tabCount) - 4; // magic number to fit the tabs in one row
            }
            @Override
            protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
                return 20;  
            }
        });

        stockList = new JPanel();
        stockList.setLayout(new BoxLayout(stockList, BoxLayout.Y_AXIS)); 
        stocksScrollPanel = new JScrollPane(stockList);      
        stocksScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        stocksScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 

        portfolioPanel = new JPanel();
        portfolioPanel.setLayout(new BoxLayout(portfolioPanel, BoxLayout.Y_AXIS));
        portfolioScrollPane = new JScrollPane(portfolioPanel);
        portfolioScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        portfolioScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        statsPanel = new JPanel();
        statsPanel.setLayout(new BorderLayout());

        updateStockPanel();
        updatePortfolioPanel(); 
        updateStatsPanel();

        tabbedPane.addTab("View Stock Market", stocksScrollPanel);
        tabbedPane.addTab("View Portfolio", portfolioScrollPane);
        tabbedPane.addTab("Analytics", statsPanel);

        return tabbedPane;
    } 
 
    private void updateStockPanel() {  
    
        stockList.removeAll();

        Stock[] data = market.getStockData(); 
        for (int i = 0; data != null && i < data.length; i++) {
            Stock stock = data[i];
            if (stock == null) continue;
            StockPanel stockPanel = new StockPanel(stock, market.getCurrentDay());
            JButton buyButton = new JButton("Buy");
            buyButton.addActionListener(e -> buyStock(stock)); 
            stockPanel.setPreferredSize(new Dimension(WIDTH - 30, STOCK_PANEL_HEIGHT));
            stockPanel.add(buyButton); 
            stockList.add(stockPanel);
        } 
     
        //scrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT - HEADER_HEIGHT - 50)); // Adjust as needed
 
        stocksScrollPanel.revalidate();
        stocksScrollPanel.repaint(); 
    }

    private void buyStock(Stock stock) {
        String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity to buy for " + stock.getStockID() + ":", "0");
        int qty = 0;
        try {
            qty = Integer.parseInt(qtyStr);
            if (qty <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            final int finalQty = qty;
            runStudentCode(() -> {
                market.buyStock(stock.getStockID(), finalQty);
                updateStockPanel();
                updatePortfolioPanel(); 
                updateStatsPanel();
                balanceLabel.setText("Balance: $" + String.format("%.2f", market.getBalance()));
                portfolioValueLabel.setText(String.format("Portfolio: $%,.2f", getPortfolioValue()));
            }); 
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sellStock(Stock stock) {
        String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity to sell for " + stock.getStockID() + "):", "0");
        int qty = 0;
        try {
            qty = Integer.parseInt(qtyStr); 
            final int finalQty = qty;
            runStudentCode(() -> {
                market.sellStock(stock.getStockID(), finalQty); 
                updateStockPanel();
                updatePortfolioPanel();
                updateStatsPanel();
                balanceLabel.setText("Balance: $" + String.format("%.2f", market.getBalance()));
                portfolioValueLabel.setText(String.format("Portfolio: $%,.2f", getPortfolioValue()));
            });  
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePortfolioPanel() { 
        portfolioPanel.removeAll();

        HashMap<String, ArrayList<Holding>> groupedHoldings = new HashMap<>(); 

        LLNode<Holding> current = market.getPortfolio();
        while (current != null) {
            Holding holding = current.getData();
            String stockID = holding.getStock().getStockID();
            groupedHoldings.putIfAbsent(stockID, new ArrayList<>());
            groupedHoldings.get(stockID).add(holding);
            current = current.getNext();
        } 

        for (String stockID : groupedHoldings.keySet()) {
            ArrayList<Holding> holdings = groupedHoldings.get(stockID);
            HoldingPanel holdingPanel = new HoldingPanel(holdings, market.getCurrentDay());
            portfolioPanel.add(holdingPanel);
        }

        portfolioScrollPane.revalidate();
        portfolioScrollPane.repaint();  
    }

    private void updateStatsPanel() {
        statsPanel.removeAll();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));

        int day = market.getCurrentDay(); 
         
        JPanel chartSelectorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        chartSelectorPanel.setBorder(BorderFactory.createTitledBorder("Price Chart Viewer"));
        
        JComboBox<String> stockSelector = new JComboBox<>();
        stockSelector.setPreferredSize(new Dimension(120, 25)); // Control combo box size
        for (Stock stock : market.getStockData()) {
            if (stock != null) {
                stockSelector.addItem(stock.getStockID());
            }
        }
        JButton viewChartButton = new JButton("View Price History Chart");
        viewChartButton.addActionListener(e -> {
            String selectedStockID = (String) stockSelector.getSelectedItem();
            if (selectedStockID != null) {
                Stock selectedStock = null;
                for (Stock stock : market.getStockData()) {
                    if (stock != null && stock.getStockID().equals(selectedStockID)) {
                        selectedStock = stock;
                        break;
                    }
                }
                if (selectedStock != null) {
                    HistoricalPriceChartPanel chart = new HistoricalPriceChartPanel(Arrays.copyOf(selectedStock.getPriceHistory(), day + 1), Color.BLUE, Color.WHITE);
                    JFrame chartFrame = new JFrame("Price History for " + selectedStockID);
                    chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    chartFrame.add(chart);
                    chartFrame.setSize(600, 400);
                    chartFrame.setVisible(true);
                }
            }
        }); 
        chartSelectorPanel.add(new JLabel("Stock: "));
        chartSelectorPanel.add(stockSelector);
        chartSelectorPanel.add(viewChartButton);
        statsPanel.add(chartSelectorPanel); 
 
        statsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        statsPanel.add(new JSeparator());
        statsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
 
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Portfolio Summary"));
        infoPanel.setAlignmentX(CENTER_ALIGNMENT);

        String infoText = String.format("<html><div style='text-align:center;'><b>Current Day:</b> %d (%s)<br/><b>Balance:</b> $%.2f  |  <b>Portfolio Value:</b> $%,.2f  |  <b>Total Net Worth:</b> $%,.2f</div></html>", 
            day, 
            dates[day], 
            market.getBalance(), 
            getPortfolioValue(), 
            market.getBalance() + getPortfolioValue()
        );
        JLabel infoLabel = new JLabel(infoText, JLabel.CENTER);
        infoLabel.setAlignmentX(CENTER_ALIGNMENT);
        infoLabel.setFont(infoLabel.getFont().deriveFont(16f));
        infoPanel.add(infoLabel);
         
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
         
        JButton viewNetWorthChartButton = new JButton("View Net Worth History");
        viewNetWorthChartButton.addActionListener(e -> {
            String selectedStockID = (String) stockSelector.getSelectedItem();
            if (selectedStockID != null) {
                Stock selectedStock = null;
                for (Stock stock : market.getStockData()) {
                    if (stock != null && stock.getStockID().equals(selectedStockID)) {
                        selectedStock = stock;
                        break;
                    }
                }
                if (selectedStock != null) {
                    HistoricalPriceChartPanel netWorthChart = new HistoricalPriceChartPanel(Arrays.copyOf(netWorthHistory, day), Color.GREEN, Color.LIGHT_GRAY);
                    netWorthChart.setBorder(BorderFactory.createEtchedBorder());
                    JFrame chartFrame = new JFrame("Net Worth History");
                    chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    chartFrame.add(netWorthChart);
                    chartFrame.setSize(600, 400);
                    chartFrame.setVisible(true);
                }
            }
        });  
        infoPanel.add(viewNetWorthChartButton);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        statsPanel.add(infoPanel);
         
        statsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        statsPanel.add(new JSeparator());
        statsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
 
        JPanel roiPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        roiPanel.setBorder(BorderFactory.createTitledBorder("Return on Investment"));
        
        JComboBox<String> roiStockSelector = new JComboBox<>();
        roiStockSelector.setPreferredSize(new Dimension(120, 25));  
        LLNode<Holding> portfolio = market.getPortfolio();
        HashSet<Stock> ownedStocks = new HashSet<>();
        while (portfolio != null) {
            ownedStocks.add(portfolio.getData().getStock());
            portfolio = portfolio.getNext();
        } 
        for (Stock stock : ownedStocks) {
            roiStockSelector.addItem(stock.getStockID());
        }
        
        JLabel roiLabel = new JLabel("Select Stock for ROI");
        roiStockSelector.addActionListener(e -> {
            String selectedStockID = (String) roiStockSelector.getSelectedItem();
            if (selectedStockID != null) {
                Stock selectedStock = null;
                for (Stock stock : ownedStocks) {
                    if (stock != null && stock.getStockID().equals(selectedStockID)) {
                        selectedStock = stock;
                        break;
                    }
                }
                if (selectedStock != null) {
                    double roi = market.calculateROI(selectedStock.getStockID());
                    roiLabel.setText(String.format("ROI for %s: %.2f%%", selectedStockID, roi));
                }
            }
        });
        roiPanel.add(roiLabel);
        roiPanel.add(roiStockSelector);
        statsPanel.add(roiPanel);
 
        statsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        statsPanel.add(new JSeparator());
        statsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
 
        JPanel extremaPanel = new JPanel();
        extremaPanel.setLayout(new BoxLayout(extremaPanel, BoxLayout.Y_AXIS));
        extremaPanel.setBorder(BorderFactory.createTitledBorder("Portfolio Analysis"));

        String[] extrema = market.findExtrema();
        JLabel extremaLabel = new JLabel(String.format("<html><div style='text-align:center;'><b>Highest Value Holding:</b> $%s<br/><b>Lowest Value Holding:</b> $%s</div></html>",
        extrema[0], extrema[1]), JLabel.CENTER);
        extremaLabel.setAlignmentX(CENTER_ALIGNMENT);
        extremaLabel.setFont(extremaLabel.getFont().deriveFont(16f)); // Make extrema label larger
        extremaPanel.add(extremaLabel);
        statsPanel.add(extremaPanel);
 
        statsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        statsPanel.add(new JSeparator());
        statsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
 
        JPanel transactionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        transactionsPanel.setBorder(BorderFactory.createTitledBorder("Transaction History"));
        
        JButton viewTransactionsButton = new JButton("View All Transactions");
        viewTransactionsButton.addActionListener(e -> {
            JFrame transactionsFrame = new JFrame("All Transactions");
            transactionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            LLNode<Transaction> current = market.getTransactions();
            while (current != null) {
                Transaction transaction = current.getData();
                String info = String.format("<html><b>%s</b> | Day: %d | Qty: %d | Price/Stock: $%.2f | Total: $%.2f | Type: %s</html>", 
                    transaction.getStock().getStockID(),
                    transaction.getDate(),
                    transaction.getQuantity(),
                    transaction.getPricePerStock(),
                    transaction.getTotalPrice(),
                    transaction.getType()
                );
                JLabel label = new JLabel(info); 
                panel.add(label); 
                current = current.getNext();
            }
            transactionsFrame.add(new JScrollPane(panel));
            transactionsFrame.setSize(470, 400);
            transactionsFrame.setVisible(true);
        });
        transactionsPanel.add(viewTransactionsButton);
        statsPanel.add(transactionsPanel);
         
        statsPanel.add(Box.createVerticalGlue());
    }


            private void runStudentCode(Runnable task) {
        this.runStudentCode(Executors.callable(task));
    }

    private <T> void runStudentCode(Callable<T> task) {  
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    task.call();
                } 
                catch (NullPointerException npe){
                    SwingUtilities.invokeLater(() ->  npe.printStackTrace());
                    JOptionPane.showMessageDialog(null, "Your code threw a NullPointerException, see the terminal for the error stack trace", "Error", JOptionPane.ERROR_MESSAGE);
                }
                catch (Exception e) {
                    SwingUtilities.invokeLater(() ->  e.printStackTrace());
                }
                this.done();
                actionTimer = null;
                return null;
            } 
        }; 
        actionTimer = new Timer(TIMEOUT, e -> {
            if (!worker.isDone()) {
                worker.cancel(true);
                actionTimer = null;
                int choice = JOptionPane.showConfirmDialog( null, 
                    "Student code is taking more time than expected -- if you're not using the debugger right now, there may be an infinite loop.\nWould you like to close the Driver?", 
                    "Warning", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.WARNING_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    this.dispose(); 
                    System.exit(0);
                }
            }
        });
        actionTimer.start();
        worker.execute(); 
    }

    public static class StockPanel extends JPanel {  
        public StockPanel(Stock stock, int day) {
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            String stockInfo = String.format("<html><b>%s</b><br/>Current Price: $%.2f</html>", 
                stock.getStockID(),  
                stock.getPriceHistory()[day] 
            );
            JLabel label = new JLabel(stockInfo); 
            this.add(label);
            this.setBorder(BorderFactory.createEtchedBorder());
        }
    }

    
    public static class HoldingPanel extends JPanel {  
        private ArrayList<Holding> sameStockHoldings;
        private int day;

        public HoldingPanel(ArrayList<Holding> sameStockHoldings, int day) {
            this.day = day;
            this.sameStockHoldings = sameStockHoldings;
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            String holdingInfo = String.format("<html><b>%s</b><br/>Total Quantity: %d<br/>Total Cost: $%.2f<br/>Current Price: $%.2f<br/>Total Value: $%.2f</html>", 
                sameStockHoldings.get(0).getStock().getStockID(),  
                sameStockHoldings.stream().mapToInt(Holding::getQuantity).sum(),
                sameStockHoldings.stream().mapToDouble(Holding::getCost).sum(),
                sameStockHoldings.get(0).getStock().getPriceHistory()[day],
                sameStockHoldings.stream().mapToDouble(h -> h.getQuantity() * h.getStock().getPriceHistory()[day]).sum()
            );
            JLabel label = new JLabel(holdingInfo); 
            this.add(label);
            JButton viewHoldingsButton = new JButton("View Holdings");
            viewHoldingsButton.addActionListener(e -> viewHoldings());
            this.add(viewHoldingsButton);
            JButton sellButton = new JButton("Sell");
            sellButton.addActionListener(e -> {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                if (topFrame instanceof Driver) {
                    ((Driver) topFrame).sellStock(sameStockHoldings.get(0).getStock());
                }
            });
            this.add(sellButton); 
            this.setBorder(BorderFactory.createEtchedBorder());
        }

        private void viewHoldings() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            for (Holding holding : sameStockHoldings) {
                HoldingDetailPanel detailPanel = new HoldingDetailPanel(holding,day);
                panel.add(detailPanel);
            }
            JScrollPane scrollPane = new JScrollPane(panel);
            JFrame frame = new JFrame("Holdings for " + sameStockHoldings.get(0).getStock().getStockID());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(scrollPane);
            frame.setSize(180, 300);
            frame.setVisible(true);
        } 
    }

    public static class HoldingDetailPanel extends JPanel {
        private Holding holding;
        private int day;

        public HoldingDetailPanel(Holding holding, int day) {
            this.holding = holding; 
            this.day = day;
            String info = String.format("<html><b>%s</b><br/>Quantity: %d<br/>Total Cost: $%.2f<br/>Value: $%.2f<br/>Purchase Day: %d</html>", 
                holding.getStock().getStockID(),  
                holding.getQuantity(),
                holding.getCost(),
                holding.getQuantity() * holding.getStock().getPriceHistory()[day],
                holding.getPurchaseDay()
            );
            JLabel label = new JLabel(info); 
            this.add(label); 
        }
    }

    class HistoricalPriceChartPanel extends JPanel {
        private final double[] prices;
        private final Color lineColor;
        private final Color bgColor;
        private final int PADDING = 30;

        public HistoricalPriceChartPanel(double[] prices, Color lineColor, Color bgColor) {
            this.prices = prices;
            this.lineColor = lineColor;
            this.bgColor = bgColor;
            setBackground(bgColor);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(245, 245, 245));

            if (prices == null || prices.length < 2) {
                g2d.drawString("Not enough price data to draw chart.", 20, 20);
                return;
            }

            double maxPrice = 0;
            double minPrice = Double.MAX_VALUE;
            for (double price : prices) {
                if (price > maxPrice) maxPrice = price;
                if (price < minPrice) minPrice = price;
            }

            g2d.drawLine(PADDING, getHeight() - PADDING, getWidth() - PADDING, getHeight() - PADDING); // X-axis
            g2d.drawLine(PADDING, getHeight() - PADDING, PADDING, PADDING); // Y-axis

            g2d.drawString(String.format("$%.2f", maxPrice), 5, PADDING + 5);
            g2d.drawString(String.format("$%.2f", minPrice), 5, getHeight() - PADDING);
            g2d.drawString("Day 0", PADDING, getHeight() - PADDING + 15);
            g2d.drawString("Day " + (prices.length - 1), getWidth() - PADDING - 30, getHeight() - PADDING + 15);

            g2d.setColor(lineColor);
            g2d.setStroke(new BasicStroke(2));

            double priceRange = (maxPrice - minPrice == 0) ? 1 : maxPrice - minPrice;

            for (int i = 0; i < prices.length - 1; i++) {
                int x1 = PADDING + (int)(i * (double)(getWidth() - 2 * PADDING) / (prices.length - 1));
                int y1 = getHeight() - PADDING - (int)(((prices[i] - minPrice) / priceRange) * (getHeight() - 2 * PADDING));
                int x2 = PADDING + (int)((i + 1) * (double)(getWidth() - 2 * PADDING) / (prices.length - 1));
                int y2 = getHeight() - PADDING - (int)(((prices[i + 1] - minPrice) / priceRange) * (getHeight() - 2 * PADDING));
                g2d.drawLine(x1, y1, x2, y2);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Driver();
        });
    }
    
}
