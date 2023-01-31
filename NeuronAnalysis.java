import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Graphics; 
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.*;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.JPopupMenu;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.JComboBox;
import javax.swing.JToolBar;

import javax.swing.event.*;
import javax.swing.text.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.text.*;

public class NeuronAnalysis extends JFrame{
  static GenerateDisplay graphs;
  static int maxlen, maxwid, curlen, curwid, mx, my, mb;
  static int globalLTime, globalRTime, binWidth, graphWid, graphLen, numNeurons;
  static JPanel panel;//, psthWindow, rateWindow, DRWindow;
  static analysisWindow countWindow, psthWindow, rateWindow, DRWindow;
  static JButton updateButton;
  static JFrame frame;
  static JMenuBar menubar;
  static JToolBar globalToolBar;
  static JFormattedTextField enterLTime,enterRTime,enterBinWid;
  static NumberFormat binWidFormat, lGlobalTime, rGlobalTime;
  static File[] angleFilesL0, angleFilesH0,sf;

  static String saveDir = ".";
  static String openDir = ".";
  static String experiment = "";
  static boolean newFrame, closeFrame, menuActivated, binWidChanged;
  static boolean updateGraphics;// = true;
  static int curTab;
  static JMenu NeuLab, file, edit, analyze, format, help;
  static JMenu exp, imp, expAna, clear, modPSTH, exper, fiveAngle;
  static JMenuItem about, quit, allExp, drExp, psthExp, rateExp, countExp, NeuHelp, clearAll, clearSingle, fiveAngleOdd, fiveAngleRep;
  static JMenuItem alldrExp, allpsthExp, allrateExp, allcountExp;
  static JMenu ssaExpMenu, psthExpMenu,drExpMenu,rateExpMenu,countExpMenu;
  static JMenuItem allAna, allssaExp,ssaExp, fslExp, peakExp, allImp, psthImp, drImp, rateImp;
  static JMenuItem fileNew, fileOpen, fileOpenPrevious, fileClose, fileSave, filePrint;
  static JMenuItem modPSTHyaxis, modDRdot,modBinWid, DR, rate, PSTH; 
  static JMenu modRate,modDR, modSpont, fslAnalysismenu;
  static JMenuItem fileProperties, fslAnalysis,allfslAnalysis,modSpontP, modSpontG, modSpontReset;
  static Dimension screenSize;

  public NeuronAnalysis() throws IOException{
    super("NeuLab Analysis");
    frame = this;
    screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.maxlen = (int)screenSize.getWidth();
    this.maxwid = (int)screenSize.getHeight();
    this.mb = 0;
    this.binWidth = 5;
    this.globalLTime = 0;
    this.globalRTime = 250;
    this.newFrame = false;
    this.closeFrame = false;
    this.graphs = new GenerateDisplay(20,510,binWidth);
    this.sf = graphs.getFiles();
    this.angleFilesL0 = new File[5];
    this.angleFilesH0 = new File[5];
    this.binWidChanged = false;
    this.updateGraphics = false;
    this.menuActivated = false;
  
    /*binWidFormat = NumberFormat.getNumberInstance();
    enterBinWid = new JFormattedTextField(binWidFormat);
    enterBinWid.setValue(new Integer(binWidth));
         enterBinWid.setColumns(5);
         //enterBinWid.addPropertyChangeListener(new BinTextListener());
         enterBinWid.setToolTipText("Change Bin-Width");
         enterBinWid.setMaximumSize(enterBinWid.getPreferredSize());
         enterBinWid.setPreferredSize(enterBinWid.getPreferredSize());*/
    graphs.setSize(maxlen,maxwid);					
    numNeurons = graphs.getNumNeurons();
    initUI();
    setSize(maxlen, maxwid);
    setLocationRelativeTo(null);
    setVisible(true);
    
    this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
			closeFrame = true;
			//if (menuActivated){
                		int n = JOptionPane.showConfirmDialog(frame, "Do you want to save your analysis?","",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
                		if (n == JOptionPane.YES_OPTION){
                        		JFileChooser chooser = new JFileChooser(saveDir);
                        		chooser.addChoosableFileFilter(new fileTypeFilter());
                        		int retrival = chooser.showSaveDialog(null);
                        		if (retrival == JFileChooser.APPROVE_OPTION) {
                                		try {
                                        		graphs.saveFile(chooser.getSelectedFile(),experiment);
                                        		saveDir = chooser.getSelectedFile().getPath().toString();//.getAbsolutePath().toString();
                                		}
                                		catch (IOException ex) {
                                			ex.printStackTrace();
                                		}
                        		}
                		}
                		else if (n == JOptionPane.NO_OPTION){
                	        	System.exit(0);
                		}
        		/*}
        		else{
                		System.exit(0);
        		}*/
		}
    });

    /*addMouseListener(this);
    addKeyListener(this);
    addMouseMotionListener(this);*/
  }

  private void initUI() throws IOException{
    panel = new JPanel();
    panel.setLayout(new GridLayout(2,2));
    psthWindow = new analysisWindow("psth");
    rateWindow = new analysisWindow("rate");
    DRWindow = new analysisWindow("DR");
    countWindow = new analysisWindow("count");
    panel.add(countWindow);
    panel.add(psthWindow);
    panel.add(rateWindow);
    panel.add(DRWindow);
    
    setJMenuBar(createMenuBar());
    createToolBar();
    getContentPane().add(panel);
  }

  private void createToolBar(){
	Image img;

	int NEW_WIDTH;
	int NEW_HEIGHT;

	globalToolBar = new JToolBar();

	//ImageIcon newIcon = new ImageIcon("newPic.png");
	//JButton new_tool = new JButton(newIcon);
	JButton new_tool = new JButton(new ImageIcon(getClass().getResource("NewPic.png")));
	new_tool.setToolTipText("New Window");					
        new_tool.setBorder(null);
        new_tool.addActionListener(new NewButtonListener());
	new_tool.setRolloverEnabled(true);
	globalToolBar.add(new_tool);

	//URL url = NeuronAnalysis.getClass.getResource("openIcon.png"); 
	//Image image = Toolkit.getDefaultToolkit().getImage(url);
	//ImageIcon openIcon = new ImageIcon(image);
	/*img = openIcon.getImage();
        NEW_WIDTH = (int)(46.0);//(int)(46.0*(2.0/3.0));//img.getWidth()/2;
        NEW_HEIGHT = (int)(46.0);//(int)(40.0*(2.0/3.0));//img.getHeight()/2;
        Image openImg = img.getScaledInstance( NEW_WIDTH, NEW_HEIGHT,  java.awt.Image.SCALE_SMOOTH ) ;
	openIcon = new ImageIcon(openImg);*/
	JButton open_tool = new JButton(new ImageIcon(getClass().getResource("openIcon.png")));
	//JButton open_tool = new JButton(openIcon);
	open_tool.setToolTipText("Open File");
        open_tool.setBorder(null);			
	open_tool.addActionListener(new OpenListener(true));
	globalToolBar.add(open_tool);
	
	//ImageIcon saveIcon = new ImageIcon("saveIcon.png");
	//JButton save_tool = new JButton(saveIcon);
	JButton save_tool = new JButton(new ImageIcon(getClass().getResource("saveIcon.png")));
	save_tool.setToolTipText("Save Analysis");
        save_tool.setBorder(null);
        save_tool.addActionListener(new SaveListener());
        globalToolBar.add(save_tool);

	ImageIcon clearIcon = new ImageIcon(getClass().getResource("clearIcon.png"));//new ImageIcon("clearIcon.png");
	img = clearIcon.getImage();
	NEW_WIDTH = (int)(40.0);//(int)(46.0*(2.0/3.0));//img.getWidth()/2;
        NEW_HEIGHT = (int)(40.0);//(int)(40.0*(2.0/3.0));//img.getHeight()/2;
        Image closeImg = img.getScaledInstance( NEW_WIDTH, NEW_HEIGHT,  java.awt.Image.SCALE_SMOOTH ) ;  
        clearIcon = new ImageIcon(closeImg);
	
	JButton clear_tool = new JButton(clearIcon);
	clear_tool.setToolTipText("Clear All Files");
        clear_tool.setBorder(null);
        clear_tool.addActionListener(new ClearButtonListener(true));
	
	//ImageIcon printIcon = new ImageIcon("printIcon.png");
	JButton print_tool = new JButton(new ImageIcon(getClass().getResource("printIcon.png")));;//new JButton(printIcon);
	print_tool.setToolTipText("Print");
        print_tool.setBorder(null);
	print_tool.addActionListener(new PrintUIWindow(this));
	globalToolBar.add(print_tool);

	globalToolBar.add(clear_tool);

	globalToolBar.addSeparator();
						
	JLabel binLabel = new JLabel("PSTH Bin-Width (ms):");
         binWidFormat = NumberFormat.getNumberInstance();
         enterBinWid = new JFormattedTextField(binWidFormat);
         enterBinWid.setValue(new Integer(binWidth));
         enterBinWid.setColumns(5);
         enterBinWid.addPropertyChangeListener(new BinTextListener());
         enterBinWid.setToolTipText("Change Bin-Width");
         enterBinWid.setMaximumSize(enterBinWid.getPreferredSize());
         enterBinWid.setPreferredSize(enterBinWid.getPreferredSize());
         binLabel.setLabelFor(enterBinWid);
        globalToolBar.add(binLabel);
        globalToolBar.add(enterBinWid);
	globalToolBar.addSeparator();

	lGlobalTime = NumberFormat.getNumberInstance();
        enterLTime = new JFormattedTextField(lGlobalTime);
        enterLTime.setValue(new Integer(globalLTime));
        enterLTime.setColumns(5);
        enterLTime.addPropertyChangeListener(new GlobalTimeListener());
        enterLTime.setMaximumSize(enterLTime.getPreferredSize());
	enterLTime.setPreferredSize(enterLTime.getPreferredSize());
	globalToolBar.add(new JLabel("Left-Time (ms):"));
	globalToolBar.add(enterLTime);
        rGlobalTime = NumberFormat.getNumberInstance();
        enterRTime = new JFormattedTextField(rGlobalTime);
        enterRTime.setValue(new Integer(globalRTime));
        enterRTime.setColumns(5);
        enterRTime.addPropertyChangeListener(new GlobalTimeListener());
        enterRTime.setMaximumSize(enterRTime.getPreferredSize());
	enterRTime.setPreferredSize(enterRTime.getPreferredSize());
	globalToolBar.add(new JLabel(" Right-Time (ms):"));
	globalToolBar.add(enterRTime);
	globalToolBar.addSeparator();

	updateButton = new JButton("UPDATE FILES");
	updateButton.addActionListener(new updateButtonListener());
	updateButton.setMaximumSize(updateButton.getPreferredSize());
	updateButton.setPreferredSize(updateButton.getPreferredSize());
	globalToolBar.add(updateButton);
	

	this.add(globalToolBar,BorderLayout.PAGE_START);
	globalToolBar.setFloatable(false);
  }

  private JMenuBar createMenuBar(){
    menubar = new JMenuBar();
    NeuLab = new JMenu("NeuLab");
    file = new JMenu("File");
    file.setMnemonic(KeyEvent.VK_F);
    edit = new JMenu("Edit");
    edit.setMnemonic(KeyEvent.VK_E);
    format = new JMenu("Format");
    format.setMnemonic(KeyEvent.VK_F);
    analyze = new JMenu("Analyze");
    analyze.setMnemonic(KeyEvent.VK_A);
    help = new JMenu("Help");
    help.setMnemonic(KeyEvent.VK_H);
    
    //'NeuLab' MENU ITEMS:
    about = new JMenuItem("About NeuLab"); 
    about.addActionListener(new AboutListener());
    quit = new JMenuItem("Quit NeuLab");
    quit.setMnemonic(KeyEvent.VK_C);
    quit.setToolTipText("Exit Application");
    quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,ActionEvent.CTRL_MASK));
    quit.addActionListener(new ExitListener());
    NeuLab.add(about);
    NeuLab.addSeparator();
    NeuLab.add(quit);

    //'FILE' MENU ITEMS:
    exp = new JMenu("Export");
    exp.setMnemonic(KeyEvent.VK_E);
    allExp = new JMenuItem("Export All...");
    psthExpMenu = new JMenu("PSTH");
    drExpMenu = new JMenu("Dot-Raster");
    rateExpMenu = new JMenu("Rate-Graph");
    countExpMenu = new JMenu("Count-Histogram");
    //JMenuItem allExp = new JMenuItem("All Neurons");
    //JMenuItem allOneExp = new JMenuItem("Current Neuron");
    psthExp = new JMenuItem("Current...");
    allpsthExp = new JMenuItem("All...");
    drExp = new JMenuItem("Current...");
    alldrExp = new JMenuItem("All...");
    countExp = new JMenuItem("Current...");
    allcountExp = new JMenuItem("All....");
    rateExp = new JMenuItem("Current...");
    allrateExp = new JMenuItem("All...");

    psthExpMenu.add(allpsthExp);
    psthExpMenu.add(psthExp);
    drExpMenu.add(alldrExp);
    drExpMenu.add(drExp);
    countExpMenu.add(allcountExp);
    countExpMenu.add(countExp);
    rateExpMenu.add(allrateExp);
    rateExpMenu.add(rateExp);
    allExp.addActionListener(new ExportListener("all",false));
    psthExp.addActionListener(new ExportListener("psth",false));
    drExp.addActionListener(new ExportListener("DR",false));
    rateExp.addActionListener(new ExportListener("rate",false));
    countExp.addActionListener(new ExportListener("count",false));
    allpsthExp.addActionListener(new ExportListener("psth",true));
    alldrExp.addActionListener(new ExportListener("DR",true));
    allrateExp.addActionListener(new ExportListener("rate",true));
    allcountExp.addActionListener(new ExportListener("count",true));
    allcountExp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,ActionEvent.CTRL_MASK));
    exp.add(allExp);
    exp.add(countExpMenu);
    exp.add(psthExpMenu);
    exp.add(drExpMenu);
    exp.add(rateExpMenu);

    expAna = new JMenu("Export Analysis");
    allAna = new JMenuItem("Export All...");
    ssaExpMenu = new JMenu("SSA Analysis");
    allssaExp = new JMenuItem("All...");
    allssaExp.addActionListener(new ExportListener("ssa",true));
    ssaExp = new JMenuItem("Current PSTH...");
    ssaExp.addActionListener(new ExportListener("ssa",false));
    ssaExpMenu.add(allssaExp);
    ssaExpMenu.add(ssaExp);
    fslExp = new JMenuItem("First-Spike Latency...");
    peakExp = new JMenuItem("Peak Latency...");
    expAna.add(allAna);
    expAna.add(ssaExpMenu);
    expAna.add(fslExp);
    expAna.add(peakExp);

    /*imp = new JMenu("Import");
    imp.setMnemonic(KeyEvent.VK_I);
    allImp = new JMenuItem("Import All...");
    psthImp = new JMenuItem("Import PSTH...");
    drImp = new JMenuItem("Import Dot-Raster...");
    rateImp = new JMenuItem("Import Rate-Graph...");
    imp.add(allImp);
    imp.add(psthImp);
    imp.add(drImp);
    imp.add(rateImp);*/

    fileNew = new JMenuItem("New");
    fileNew.setMnemonic(KeyEvent.VK_C);
    fileNew.addActionListener(new NewButtonListener());
    fileNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
    fileNew.setToolTipText("Open New Analysis Window");
    fileOpen = new JMenuItem("Open file...");
    fileOpen.setMnemonic(KeyEvent.VK_C);
    fileOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
    fileOpen.addActionListener(new OpenListener(true));
    fileOpenPrevious = new JMenuItem("Open analysis...");
    fileOpenPrevious.addActionListener(new OpenRecentListener());
    fileOpenPrevious.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,ActionEvent.CTRL_MASK));
    JMenuItem fileOpenOldPrevious = new JMenuItem("Open older analysis...");
    fileOpenOldPrevious.addActionListener(new OpenOldRecentListener());
    fileSave = new JMenuItem("Save");
    fileSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
    fileSave.addActionListener(new SaveListener());
    fileClose = new JMenuItem("Close");
    fileClose.setMnemonic(KeyEvent.VK_C);
    fileClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,ActionEvent.CTRL_MASK));
    fileClose.addActionListener(new CloseButtonListener());
    filePrint = new JMenuItem("Print");
    filePrint.setMnemonic(KeyEvent.VK_C);
    filePrint.addActionListener(new PrintUIWindow(this));
    filePrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,ActionEvent.CTRL_MASK));
    fileProperties = new JMenuItem("File Properties...");
    fileProperties.setToolTipText("Set File-Opening Settings");
    fileProperties.addActionListener(new FilePropertyListener());

    file.add(fileNew);
    file.add(fileOpen);
    file.add(fileOpenPrevious);
    file.add(fileOpenOldPrevious);
    file.addSeparator();
    file.add(fileClose);
    file.add(fileSave);
    file.addSeparator();
    file.add(exp);
    file.add(expAna);
    //file.add(imp);
    file.addSeparator();
    file.add(filePrint);
    file.addSeparator();
    file.add(fileProperties);

    //'EDIT' MENU ITEMS:
    clear = new JMenu("Clear");
    clearAll = new JMenuItem("All");
    //clearAll.setMnemonic(KeyEvent.VK_C);
    clearAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));
    clearAll.addActionListener(new ClearButtonListener(true));
    clearSingle = new JMenuItem("Single");
    clearSingle.addActionListener(new ClearButtonListener(false));	
    clear.add(clearAll);
    clear.add(clearSingle);
    edit.add(clear);
    //edit.addSeparator();

    //'FORMAT' MENU ITEMS:
    modPSTH = new JMenu("PSTH");
    modPSTHyaxis = new JMenuItem("Y-Axis...");
    modPSTHyaxis.addActionListener(new PSTHyaxisListener());
    modPSTH.add(modPSTHyaxis);
    modBinWid = new JMenuItem("Bin-Width...");
    modBinWid.addActionListener(new ChangeWidListener());
    modPSTH.add(modBinWid);
    modDR = new JMenu("Dot-Raster");
    modDRdot = new JMenuItem("Dot Settings...");
    modDRdot.addActionListener(new DRDotListener());
    modDR.add(modDRdot);
    modRate = new JMenu("Rate-Line Graph");
    modSpont = new JMenu("Generate Spontaneous Activity");
    modSpontP = new JMenuItem("Poisson-Distributed...");
    modSpontG = new JMenuItem("Gaussian-Distributed...");
    modSpontReset = new JMenuItem("Reset");
    modSpontP.addActionListener(new GenerateSpontListener("poisson"));
    modSpontG.addActionListener(new GenerateSpontListener("gaussian"));
    modSpontReset.addActionListener(new GenerateSpontListener("reset"));
    modSpont.add(modSpontP);
    modSpont.add(modSpontG);
    modSpont.add(modSpontReset);
    modRate.add(modSpont);

    format.add(modPSTH);
    format.add(modDR);
    format.add(modRate);

    //'ANALYZE' MENU ITEMS:
    DR = new JMenuItem("Dot-Raster");
    rate = new JMenuItem("Rate-line Graph");
    PSTH = new JMenuItem("PST Histogram");
    exper = new JMenu("Experiment");
    fiveAngle = new JMenu("5-Angle Set-Up");
    fiveAngleOdd = new JMenuItem("Odd-ball Paradigm...");
    fiveAngleOdd.addActionListener(new FiveAngleListener("oddball"));
    fiveAngleRep = new JMenuItem("Repetitive Stimulation...");
    fiveAngleRep.addActionListener(new FiveAngleListener("repetitive"));
    fiveAngle.add(fiveAngleOdd);
    fiveAngle.add(fiveAngleRep);
    
    exper.add(fiveAngle);

    fslAnalysis = new JMenuItem("Single...");
    fslAnalysis.addActionListener(new calculateFSLListener(false));
    allfslAnalysis = new JMenuItem("All...");
    allfslAnalysis.addActionListener(new calculateFSLListener(true));
    fslAnalysismenu = new JMenu("First-Spike Latency");
    fslAnalysismenu.add(allfslAnalysis);
    fslAnalysismenu.add(fslAnalysis);

    analyze.add(exper);
    analyze.addSeparator();
    analyze.add(fslAnalysismenu);
    analyze.addSeparator();
    analyze.add(DR);
    analyze.add(rate);
    analyze.add(PSTH);
    
    //'HELP' MENU ITEMS:
    NeuHelp = new JMenuItem("NeuLab Help");
    NeuHelp.addActionListener(new HelpListener());
    help.add(NeuHelp);

    menubar.add(NeuLab);
    menubar.add(file);
    menubar.add(edit);
    menubar.add(format);
    menubar.add(analyze);
    menubar.add(help);
    
    deactivateMenu();
    //panel.add(statusbar);
    return menubar;
  }

  private void deactivateMenu(){
    	modPSTH.setEnabled(false);
        modDR.setEnabled(false);
        modRate.setEnabled(false);
	fileSave.setEnabled(false);
	//fileClose.setEnabled(false);
    	filePrint.setEnabled(false);
	allExp.setEnabled(false);
    	psthExpMenu.setEnabled(false);
    	drExpMenu.setEnabled(false);
    	rateExpMenu.setEnabled(false);
    	countExpMenu.setEnabled(false);
	DR.setEnabled(false);
    	rate.setEnabled(false);
    	PSTH.setEnabled(false);
	allAna.setEnabled(false);
   	fslExp.setEnabled(false);
  	peakExp.setEnabled(false);
  	clear.setEnabled(false);
	ssaExpMenu.setEnabled(false);
	modSpont.setEnabled(false);
	fslAnalysismenu.setEnabled(false);
	menuActivated = false;
  }
  private void activateMenu(boolean act){
	if (act==false){
		modPSTH.setEnabled(true);
		modDR.setEnabled(true);
		modRate.setEnabled(true);
		modSpont.setEnabled(true);
		fileSave.setEnabled(true);
		//fileClose.setEnabled(true);
        	filePrint.setEnabled(true);
		allExp.setEnabled(true);
        	psthExpMenu.setEnabled(true);
        	drExpMenu.setEnabled(true);
        	rateExpMenu.setEnabled(true);
        	countExpMenu.setEnabled(true);
        	DR.setEnabled(true);
       	 	rate.setEnabled(true);
       	 	PSTH.setEnabled(true);
        	allAna.setEnabled(true);
        	fslExp.setEnabled(true);
        	peakExp.setEnabled(true);
  		clear.setEnabled(true);
		ssaExpMenu.setEnabled(true);
		fslAnalysismenu.setEnabled(true);
		this.menuActivated = true;
	}
  }


  private class analysisWindow extends JPanel{// implements MouseListener, MouseMotionListener{
    private String onZero,type, title;
    private JTabbedPane tabPane;
    private tab newTab;
    private ArrayList<tab> tabList;
    private int curTab;
    public analysisWindow(String type){
      setLayout(new GridLayout(1, 1));
      //setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "type"));
      this.type = type;
      this.tabPane = new JTabbedPane();
      this.tabList = new ArrayList<tab>();

      Border empty = BorderFactory.createEmptyBorder();
      TitledBorder panelTitle;
      Border blackline = BorderFactory.createLineBorder(Color.black,2);
      if (type.equals("psth")){
      	panelTitle = BorderFactory.createTitledBorder("Peristimulus Time Histogram");
      }										
      else if (type.equals("DR")){
        panelTitle = BorderFactory.createTitledBorder("Dot-Raster");
      }
      else if (type.equals("rate")){
        panelTitle = BorderFactory.createTitledBorder("Rate-Line Graph (1-ms Bin-Width)");
      }										
      else if (type.equals("count")){
	panelTitle = BorderFactory.createTitledBorder(blackline,"Count Histogram & Global Control Window");
      }										
      else{
	panelTitle = BorderFactory.createTitledBorder("PSTH");
      }
      //panelTitle.setTitlePosition(TitledBorder.ABOVE_TOP);
      //panelTitle.setTitleJustification(TitledBorder.CENTER);
      this.setBorder(panelTitle);
      for (int i=0; i<numNeurons ;i++){
	if (experiment.equals("")){
                title = "FILE "+(i+1);
        	onZero = "";
	}
        else if (experiment.equals("fiveangle")){
                if (i==0 || i==5){
                        title = "C90";
                }
                else if (i==1 || i==6){
                        title = "C45";
                }
                else if (i==2 || i==7){
                        title = "MID (0)";
                }
                else if (i==3 || i==8){
                        title = "I45";
                }
                else if (i==4 || i==9){
                        title = "I90";
                }
                else{
                        title = "EXTRA";
                }
		if (i<5){
			onZero = "fL-0 ";
		}
		else{
			onZero = "fH-0 ";
		}
        }
	newTab = new tab(type,i);
	tabPane.addTab(onZero+title,null,newTab,sf[i].getName());
	tabPane.setTabComponentAt(i,new ButtonTabComponent(tabPane));
	tabList.add(newTab);
      }
      add(tabPane);
      tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    public void addNeuronTabs(){
	
    }
    public void newNeuronTabs(){
	tabPane.removeAll();
	if (type.equals("count") && experiment.equals("fiveangle")){
		for (int i=0; i<2; i++){
			if (i==0){
				title = "fL-0";
				onZero = "";
			}
			else{
				title = "fH-0";
				onZero = "";
			}
			newTab = new tab(type,i);
			tabPane.addTab(onZero+title,null,newTab, "Angle-dependent Count Graph ("+title+")");
			tabPane.setTabComponentAt(i,new ButtonTabComponent(tabPane));
			tabList.add(newTab);
		}
	}
	else{
		for (int i=0; i<numNeurons ;i++){
        		if (experiment.equals("")){
                		title = "FILE "+(i+1);
        			onZero = "";
			}
        		else if (experiment.equals("fiveangle")){
				if (i==0 || i==5){
                		        title = "C90";
               			}
               			else if (i==1 || i==6){
       	        	      		 title = "C45";
       	      			}
                	 	else if (i==2 || i==7){
 	      			      title = "MID (0)";
 	             	 	}
  	             		else if (i==3 || i==8){
  	              		        title = "I45";
  	       	    		}
  	        	      	else if (i==4 || i==9){
  	       			        title = "I90";
  	      			}
            			else{
  	      			        title = "EXTRA";
  	      			}
  	       			if (i<5){
  	       			        onZero = "fL-0 ";
  	       			}
  	       			else{
  	       			        onZero = "fH-0 ";
  	       			}
    			}
			tab newTab = new tab(type,i);
			tabPane.addTab(onZero+title,null,newTab, sf[i].getName());
			tabPane.setTabComponentAt(i,new ButtonTabComponent(tabPane));
			tabList.add(newTab);
		}
	}
    }
    public void remove(int i){
	tabPane.remove(i);
    }

    public String getType(){
    	return this.type;
    }
    public int getCurrentTab(){
	return tabPane.getSelectedIndex();
    }
    public void setCurrentTab(int a){
	tabPane.setSelectedIndex(a);
    }

    public void updatePSTH(){
	//tabList.get(i).updatePSTH();
    }
  }

  private class tab extends JPanel implements MouseListener, MouseMotionListener{
    private String type, curMode, curParam;
    private JComboBox mode, param;
    private JButton data, edit;
    private String[] modes = { "Plot", "Data", "Overview" };
    private String[] basicparams = { "Frequency", "Intensity" };
    private String[] allparams = { "Frequency", "Intensity", "Ipsi-Intensity","ILD"};
    private String[] params;// = { "Frequency", "Intensity" };
    private String[] freqList, intenList, ipsiIntenList, ILDList;
    private JPopupMenu popup;							
    private DataTable datatable;
    private JTable table;
    private JScrollPane scrollPane;
    private JPanel blank;				
    private JMenu popFormat;
    private boolean closedField;
    private JMenuItem popAdvanced, popBin, popClose, popSelect;
    private int ind, i, timeR, timeL;
    private int numFreqs, numIntens, numIpsiIntens, numILD;
    private GridBagConstraints c;
    private JToolBar toolbar;
    public tab(String gType, int index){
      this.type = gType;
      this.curMode = modes[0];
      this.curParam = basicparams[0];
      this.i = index;
      this.timeR = 250;
      this.timeL = 0;
      this.closedField = false;
      this.numFreqs = graphs.getNumFreqs(i);
      this.numIntens = graphs.getNumIntens(i);
      if (graphs.isClosedField(i)){
      	this.closedField = true;
	this.params = allparams;//{ "Frequency", "Intensity", "Ipsi-Intensity","ILD"};//allparams;
	this.numILD = graphs.getNumILD(i);
	this.numIpsiIntens = graphs.getNumIpsiIntens(i);
       	this.ILDList = new String[numILD];
      	this.ipsiIntenList = new String[numIpsiIntens];
      }
      else{
	this.params = basicparams;
      }
      this.freqList = new String[numFreqs];
      this.intenList = new String[numIntens];
      activateMenu(menuActivated);
      setSubParamList();
      setTabPopup();
      setToolBar();
      addComponentsToPane(this);
      addMouseListener(this);
      addMouseMotionListener(this);
    }
    public void paintComponent(Graphics g){
      super.paintComponent(g);
      if (curMode.equals("Plot")){
	//if (updateGraphics){
		if (type.equals("psth")){
			graphs.movePSTH(mx,my,mb,i);
        		g = graphs.drawPSTH(g,i,curParam);
		}
      		else if (type.equals("rate")){
        		g = graphs.drawRate(g,i,curParam);
      		}
      		else if (type.equals("DR")){
			graphs.moveDR(mx,my,mb,i);
        		g = graphs.drawDR(g,i,curParam);
      		}
      		else if (type.equals("count")){
			if (experiment.equals("fiveangle")){
        			g = graphs.drawAngleCount(g,i);
      			}
			else{
				g = graphs.drawCount(g,i,curParam);
				graphs.moveCount(mx,my,mb,i,curParam);
			}
		}
		//updateTable();
	}
	else if (curMode.equals("Overview")){
		g = graphs.drawOverview(g,i);
		//System.out.println("Overview");	
	}
	//else if (curMode.equals("data")){
		updateTable();
	//}
	/*if (mb==1){
                updateGraphics = true;
        }
        else{
                updateGraphics = false;
        }
      }*/
    }
    public void updateTable(){
	datatable.modifyData();
	if (type.equals("count")){
		if (timeR!=graphs.getrTime(i) || timeL!=graphs.getlTime(i)){
			timeL = graphs.getlTime(i);
                	timeR = graphs.getrTime(i);
			table.getColumnModel().getColumn(1).setHeaderValue("Spike Count (Time Range: "+graphs.getlTime(i)+"ms - "+graphs.getrTime(i)+"ms)");
		}
	}
	/*else if (type.equals("psth")){
		if (binWidChanged){
			table.getColumnModel().getColumn(0).setHeaderValue("Bin-Number (Bin-Width="+binWidth+"ms)");
		}
	}*/
    }
    public void updatePSTH(){
	enterBinWid.setValue(new Integer(binWidth));
	//datatable.modifyData();
	table.getColumnModel().getColumn(0).setHeaderValue("Bin-Number (Bin-Width="+binWidth+"ms)");
    }
    private void setSubParamList(){
	for (int k=0; k<numFreqs; k++){
		ind = graphs.getFreqIndex(i,k);
		if (graphs.getFreqValue(i,k) > 0.0d){
			freqList[k] = "f"+(ind)+" ("+(int)graphs.getFreqValue(i,k)+"Hz)";
		}
		else{
			freqList[k] = "f"+(ind)+" (Activity Outside Stimulus Duration)";
		}
	}
	for (int k=0; k<numIntens; k++){
		ind = graphs.getFreqIndex(i,k);
		intenList[k] = "I"+(ind)+" ("+(int)graphs.getIntenValue(i,k)+"dB)";
	}
	if (closedField){
		for (int k=0; k<numIpsiIntens; k++){
			ind = graphs.getFreqIndex(i,k);
			ipsiIntenList[k] = "ips-I"+(ind)+" ("+(int)graphs.getIpsiIntenValue(i,k)+"dB)";
    		}
		for (int k=0; k<numILD; k++){
        	        ind = graphs.getFreqIndex(i,k);
        	        ILDList[k] = "ILD"+(ind)+" ("+(int)graphs.getILDValue(i,k)+"dB)";
        	}
	}   													
    }
    private void setTabPopup(){
	popup = new JPopupMenu();
	popSelect = new JMenuItem("Data-View Options...");
	popSelect.addActionListener(new SubParamListener());
	popup.add(popSelect);
	popup.addSeparator();
	if (type.equals("psth")){
                popFormat = new JMenu("Format");
		popBin = new JMenuItem("Bin-Width...");
		popAdvanced = new JMenuItem("Advanced...");
                popBin.addActionListener(new ChangeWidListener());
		popFormat.add(popBin);
		popFormat.add(popAdvanced);
		popup.add(popFormat);
                //popup.addSeparator();
        }
	popup.addSeparator();
	popClose = new JMenuItem("Close");
        //popItem2.addActionListener(this);
        popup.add(popClose);
        this.setComponentPopupMenu(popup);
    }
    private void setToolBar(){
	toolbar = new JToolBar(type);
	//toolbar.setBorder(BorderFactory.createLoweredBevelBorder());
      //boolean b = toolbar.isRollover();

	mode = new JComboBox(modes);
      //mode.setFont("Lucida Grande",Font.PLAIN,8);
      mode.addActionListener(new ModeListener());
	mode.setToolTipText("Viewing Mode");
      /*c.weightx = 0.4;
      c.anchor = GridBagConstraints.PAGE_START;
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridwidth = 1;
      c.gridx = 0;
      c.gridy = 0;
      pane.add(mode, c);*/
      toolbar.add(new JLabel("Mode:"));
      toolbar.add(mode);
	if (type.equals("psth")){
         /*JLabel binLabel = new JLabel(" Bin-Width:");
	 toolbar.addSeparator();
	 //binWidFormat = NumberFormat.getNumberInstance();
         enterBinWid = new JFormattedTextField(binWidFormat);
         enterBinWid.setValue(new Integer(binWidth));
         enterBinWid.setColumns(5);
         enterBinWid.addPropertyChangeListener(new BinTextListener());
         enterBinWid.setToolTipText("Change Bin-Width");
	 enterBinWid.setMaximumSize(enterBinWid.getPreferredSize());
	 enterBinWid.setPreferredSize(enterBinWid.getPreferredSize());
	 binLabel.setLabelFor(enterBinWid);
         toolbar.add(binLabel);
	 toolbar.add(enterBinWid);
        */}				
	if (!(type.equals("count") && experiment.equals("fiveangle"))){
		toolbar.addSeparator();
		param = new JComboBox(params);
		param.addActionListener(new ParamListener());
		toolbar.add(new JLabel("Parameter:"));
		param.setToolTipText("Choose Parameter");
		toolbar.add(param);
	}
      /*if (!type.equals("count")){
         subparam = new JComboBox(freqList);
         subparam.addItemListener(new SubParamListener());
         toolbar.add(subparam);
      }*/
      
	toolbar.addSeparator();
        data = new JButton("View");
        data.addActionListener(new SubParamListener());
        toolbar.add(data);
        edit = new JButton("Format");
	if (type.equals("DR")){
		edit.addActionListener(new DRDotListener());
	}
	else if (type.equals("psth")){
		edit.addActionListener(new PSTHyaxisListener());
	}							
	
      	toolbar.add(edit);

	toolbar.setFloatable(false);
 	toolbar.setRollover(true);
    }
    private void addComponentsToPane(Container pane) {
      pane.setLayout(new BorderLayout());//(new GridBagLayout());
      /*c = new GridBagConstraints();

      c.anchor = GridBagConstraints.PAGE_START;
         c.fill = GridBagConstraints.HORIZONTAL;
         c.weightx = 1;
         c.gridwidth = 1;
         c.gridx = 0;
         c.gridy = 0;
         pane.add(toolbar, c);
*/
      this.add(toolbar,BorderLayout.PAGE_START);
      datatable = new DataTable(type,i);
      table = new JTable(datatable);
      scrollPane = new JScrollPane(table);
      /*c.fill = GridBagConstraints.BOTH;
      //c.ipady = 40;      //make this component tall
      c.weightx = 1.0;
      c.weighty = 1.0;
      /*if (type.equals("psth")){
      	c.gridwidth = 4;
      }
      else{
 	c.gridwidth = 3;
      }
      c.gridx = 0;
      c.gridy = 0;
      pane.add(scrollPane, c);*/
      this.add(scrollPane, BorderLayout.CENTER);
      scrollPane.setVisible(false); 

    }
    private class SubParamListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		JPanel view = new JPanel();
		view.setPreferredSize(new Dimension(240,200));
		JTabbedPane parOptions = new JTabbedPane();
		JCheckBox box;
		
		JPanel freqOption = new JPanel();
		freqOption.setPreferredSize(new Dimension(220,140));
		freqOption.setLayout(new GridLayout(numFreqs,3));
		for (int k=0; k<numFreqs; k++){
			box = new JCheckBox(freqList[k]);
			if (graphs.getFreqSelected(i,k)>0){	
				box.setSelected(true);
			}
			else{
				box.setSelected(false);
			}
			box.addItemListener(new SelectParamListener("freq",k));
			freqOption.add(box);
		}

		JPanel intenOption = new JPanel();
		intenOption.setPreferredSize(new Dimension(220,140));
		intenOption.setLayout(new GridLayout(numIntens,3));
		for (int k=0; k<numIntens; k++){
                        box = new JCheckBox(intenList[k]);
                        if (graphs.getIntenSelected(i,k)>0){
                                box.setSelected(true);
                        }
                        else{
                                box.setSelected(false);
                        }
			box.addItemListener(new SelectParamListener("inten",k));
                        intenOption.add(box);
                }
			
		//if (closedField){
			//IPSI-INTENS OPTIONS:
			JPanel ipsiIntenOption = new JPanel();
                	ipsiIntenOption.setPreferredSize(new Dimension(220,140));
                	ipsiIntenOption.setLayout(new GridLayout(numIpsiIntens,3));
                	for (int k=0; k<numIpsiIntens; k++){
                        	box = new JCheckBox(ipsiIntenList[k]);
                        	if (graphs.getIpsiIntenSelected(i,k)>0){
                        	        box.setSelected(true);
                        	}
                        	else{
                        	        box.setSelected(false);
                        	}
                        	box.addItemListener(new SelectParamListener("ipsiInten",k));
                        	ipsiIntenOption.add(box);
                	}

			//ILD OPTIONS:
			JPanel ILDOption = new JPanel();
                	ILDOption.setPreferredSize(new Dimension(220,140));
                	ILDOption.setLayout(new GridLayout(numILD,3));
                	for (int k=0; k<numILD; k++){	
                        	box = new JCheckBox(ILDList[k]);
                        	if (graphs.getILDSelected(i,k)>0){
                        	        box.setSelected(true);
                        	}
                        	else{
                        	        box.setSelected(false);
                        	}
                        	box.addItemListener(new SelectParamListener("ILD",k));
                        	ILDOption.add(box);
                	}
			
		//}
		
		JPanel otherOption = new JPanel();
		otherOption.setPreferredSize(new Dimension(220,140));
		otherOption.setLayout(new GridLayout(2,2));
		box = new JCheckBox("First-Spike Latency");
		if (graphs.getFSLSelected(i)){
		        box.setSelected(true);
                }
                else{
                        box.setSelected(false);
                }
		box.addItemListener(new SelectParamListener("fsl",0));
		otherOption.add(box);				
		box = new JCheckBox("Peak Latency");
                if (graphs.getPeakSelected(i)){
                        box.setSelected(true);
                }
                else{
                        box.setSelected(false);
                }
                box.addItemListener(new SelectParamListener("peak",0));
                otherOption.add(box);				
		
		parOptions.addTab("Frequency",null,freqOption, "Frequency viewing options");
		parOptions.addTab("Intensity",null,intenOption, "Intensity viewing options");
		if (closedField){
			parOptions.addTab("Ipsi-Intensity",null,ipsiIntenOption, "Ipsi-Intensity viewing options");
			parOptions.addTab("ILD",null,ILDOption, "ILD viewing options");
		}
		parOptions.addTab("Other",null,otherOption, "Other viewing options");
		view.add(parOptions);
		int result = JOptionPane.showConfirmDialog(null, view,"Data View Options", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);	
	}
	private class SelectParamListener implements ItemListener{
		private String p;
		private int k;
		public SelectParamListener(String p, int k){
			this.p = p;
			this.k = k;
		}
		public void itemStateChanged(ItemEvent e){
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				if (p.equals("freq")){
					graphs.setFreqSelected(i,k,0);
				}
				else if (p.equals("inten")){
                                        graphs.setIntenSelected(i,k,0);
                                }
				else if (p.equals("ipsiInten")){
                                        graphs.setIpsiIntenSelected(i,k,0);
                                }
                                else if (p.equals("ILD")){
                                        graphs.setILDSelected(i,k,0);
                                }				
				else if (p.equals("fsl")){
					graphs.setFSLSelected(i,false);
				}
				else if (p.equals("peak")){
					graphs.setPeakSelected(i,false);
				}
			}
			else if (e.getStateChange() == ItemEvent.SELECTED) {
				if (p.equals("freq")){
					graphs.setFreqSelected(i,k,1);
				}
				else if (p.equals("inten")){
                                        graphs.setIntenSelected(i,k,1);
                                }
				else if (p.equals("ipsiInten")){
					graphs.setIpsiIntenSelected(i,k,1);
				}
				else if (p.equals("ILD")){
					graphs.setILDSelected(i,k,1);
				}
				else if (p.equals("fsl")){
                                        graphs.setFSLSelected(i,true);
                                }
                                else if (p.equals("peak")){
                                        graphs.setPeakSelected(i,true);
                                }
                        }
		}
	}
    }
    private class ModeListener implements ActionListener{
   	public void actionPerformed(ActionEvent e){
        	JComboBox cb = (JComboBox)e.getSource();
        	curMode = (String)cb.getSelectedItem();
		if (curMode.equals("Plot")){
                	scrollPane.setVisible(false);
		}
        	else if (curMode.equals("Data")){
                	scrollPane.setVisible(true);
		}
    	}
    }
    private class ParamListener implements ActionListener{
	public void actionPerformed(ActionEvent e){
		JComboBox cb = (JComboBox)e.getSource();
		if (!curParam.equals((String)cb.getSelectedItem())){
                	curParam = (String)cb.getSelectedItem();
		}
	}
    }/*
    private class BinTextListener implements PropertyChangeListener{
    	public void propertyChange(PropertyChangeEvent e){
        	Object source = e.getSource();
        	if (source == enterBinWid){
        	    binWidth = ((Number)enterBinWid.getValue()).intValue();
        		psthWindow.updatePSTH();
        		resetPSTHBin();
		}
		updateGraphics = true;
    	}
    }*/
    public void mousePressed (MouseEvent e){
     //mx = e.getX();
     //my = e.getY();
     mb = e.getButton();
     if (mb==0){
	updateGraphics = false;
     }
	//System.out.println("mx="+mx+", my="+my+ ", mb="+mb);
  	/*if (mb==1 && graphs.inPanel(mx,my)){
                updateGraphics = true;
	}
        else if (mb==0){
                updateGraphics = false;
        }
        else{
                updateGraphics = false;
        }
        if (updateGraphics){
                if (mb==0){
                        updateGraphics = false;
                }
        }
        if (mb==0){
                updateGraphics = false;
        }*/
  }
  public void mouseMoved (MouseEvent e){
     mx = e.getX();
     my = e.getY();
	/*if (mb==1 && graphs.inPanel(mx,my)){
                updateGraphics = true;
		//System.out.println("inPanel");
        }
        else if (mb==0){
                updateGraphics = false;
        }
        else{
                updateGraphics = false;
        }
        if (updateGraphics){
                if (mb==0){
                        updateGraphics = false;
                }
        }
        if (mb==0){
                updateGraphics = false;
        }*/
  }
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){}
  public void mouseClicked(MouseEvent e){}
  public void mouseReleased(MouseEvent e){
        mb=0;
	//updateGraphics = false;
  }
  public void mouseDragged(MouseEvent e){
     mx = e.getX();
     my = e.getY();
     mb = e.getButton();
	if (mb==1 && graphs.inPanel(mx,my)){
                updateGraphics = true;
		//System.out.println("inPanel");
        }
	else if (mb==0){
		updateGraphics = false;
	}
	else{
		updateGraphics = false;
	}
        if (updateGraphics){
        	if (mb==0){
			updateGraphics = false;
		}
	}
	if (mb==0){
		updateGraphics = false;
	}
  }
  }

  public class ButtonTabComponent extends JPanel {
    private final JTabbedPane pane;
 
    public ButtonTabComponent(final JTabbedPane pane) {
        //unset default FlowLayout' gaps
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.pane = pane;
        setOpaque(false);
         
        //make JLabel read titles from JTabbedPane
        JLabel label = new JLabel() {
            public String getText() {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    return pane.getTitleAt(i);
                }
                return null;
            }
        };
         
        add(label);
        //add more space between the label and the button
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        //tab button
        JButton button = new TabButton();
        add(button);
        //add more space to the top of the component
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }
 
    private class TabButton extends JButton implements ActionListener {
        public TabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("Close Tab");
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(this);
        }
 
        public void actionPerformed(ActionEvent e) {
            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1) {
                //pane.remove(i);
            	psthWindow.remove(i);
		countWindow.remove(i);
		DRWindow.remove(i);
		rateWindow.remove(i);
	    }
        }
 
        //(If needed) Update UI for this button
        public void updateUI() {
	}
 				
        //Paint the cross ('X')
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            //shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) {
		//if (pane.indexOfTabComponent(ButtonTabComponent.this) == pane.getCurrentTab()){
                	g2.setColor(Color.WHITE);
		/*}
		else{
			g2.setColor(new Color(100,100,100));
            	}*/
	    }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }
 
    private final MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }
 
        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };
  }

  private class DataTable extends AbstractTableModel {
        private String[] columnNames;
	private String type;
	private ArrayList<Integer> psthData;
	private ArrayList<Double> rateData;
	private int count, numColumns, numFreqs, i;
        private Object[][] data;// = {{new Integer(0), new Integer(5), new Integer(10)},{new Integer(1), new Integer(5), new Integer(5)}};

	public DataTable(String type, int index){
		this.i = index;
		this.type = type;
		numFreqs = graphs.getNumFreqs(i);
		if (type.equals("psth")){
			numColumns = (graphs.getNumFreqs(i) + 2);
			columnNames = new String[numColumns];
			generatePSTHData();
		}
		else if (type.equals("count")){
			numColumns = 2;
			columnNames = new String[numColumns];
                        generateCountData();
		}
		else if (type.equals("rate")){
			numColumns = (graphs.getNumFreqs(i) + 2);
                        columnNames = new String[numColumns];
                        generateRateData();
		}
		else if (type.equals("DR")){
			numColumns = (graphs.getNumFreqs(i) + 2);
                        columnNames = new String[numColumns];
                        generatePSTHData();
		}
		else{
			numColumns = (graphs.getNumFreqs(i) + 2);
                        columnNames = new String[numColumns];
                        generatePSTHData();
		}
	}
	
	private void generatePSTHData(){
		columnNames[0] = "Bin-Number (Bin-Width="+binWidth+"ms)";
		columnNames[1] = "Time Range (ms)";
		psthData = graphs.getPSTH(i,0);
		data = new Object[psthData.size()+1][numColumns];
		for (int j=0; j<numFreqs;j++){
			columnNames[j+2] = "Frequency "+(j+1);
			psthData = graphs.getPSTH(i,j);
			for (int l=-1; l<psthData.size();l++){
				if (l == -1){
					data[0][0] = new Integer(0);
                        		data[0][1] = new Integer(0);
					data[0][(j+2)] = new Integer(0);
				}
				else{
					data[l+1][0] = new Integer((l+1));
                        		data[l+1][1] = new Integer((l+1)*binWidth);
					data[l+1][(j+2)] = new Integer(psthData.get(l));
				}
			}
		}
	}
	private void generateRateData(){
                columnNames[0] = "Bin-Number";
                columnNames[1] = "Time (ms)";
                rateData = graphs.getRate(i,0);
                data = new Object[rateData.size()+1][numColumns];
                for (int j=0; j<numFreqs;j++){
                        columnNames[j+2] = "Frequency "+(j+1);
                        rateData = graphs.getRate(i,j);
                        for (int l=-1; l<rateData.size();l++){
                                if (l == -1){
					data[0][0] = new Integer(0);
                                        data[0][1] = new Integer(0);
                                        data[0][(j+2)] = new Integer(0);
				}
				else{
					data[(l+1)][0] = new Integer((l+1));
                                	data[(l+1)][1] = new Integer((l+1));
                                	data[(l+1)][(j+2)] = new Double(rateData.get(l));
                        	}
			}
                }
        }
	private void generateCountData(){
                columnNames[0] = "Frequency";
                columnNames[1] = "Spike Count (Time Range: "+graphs.getlTime(i)+"ms - "+graphs.getrTime(i)+"ms)";
                data = new Object[numFreqs][numColumns];
                for (int j=0; j<numFreqs;j++){
			count = graphs.getCount(i,j);
                	data[j][0] = new Integer(graphs.getFreqIndex(i,j));
			data[j][1] = new Integer(count);
		}
        }
/*
	private void generateDRData(){
		columnNames[0] = "Sweep";
		columnNames[1] = "Time";
		columnNames[2] = "Frequency";
		drData = graphs.getDR(i,0);
		rows = numFreqs*((drData.length)*(drData[0].length))
		data = new Object[rows][numColumns];
		for (int j=0; j<numFreqs; j++){
			drData = graphs.getDR(i,j);
			for (int k=0; k<drData.length;k++){
				for (int l=0; l<drData[0].length;l++){
					data[	
		}
	}*/

	public void modifyData(){
		if (type.equals("count")){
			modifyCountData();
		}
		else if (type.equals("psth")){
			modifyPSTHData();
		}
	}

	private void modifyCountData(){
		for (int j=0; j<numFreqs;j++){
			count = graphs.getAPCount(i,j);
			setValueAt(new Integer(count),j,1);
		}
	}
	private void modifyPSTHData(){
		//if (binWidChanged){
                        psthData = graphs.getPSTH(i,0);
                	data = new Object[psthData.size()+1][numColumns];
                	for (int j=0; j<numFreqs;j++){
                        	psthData = graphs.getPSTH(i,j);
                        	for (int l=-1; l<psthData.size();l++){
                        		if (l == -1){
                                	        /*data[0][0] = new Integer(0);
                                	        data[0][1] = new Integer(0);
                                	        data[0][(j+2)] = new Integer(0);
                                		*/
						setValueAt(new Integer(0),0,0);
						setValueAt(new Integer(0),0,1);
						setValueAt(new Integer(0),0,(j+2));
					}
                                	else{
                                        	/*data[(l+1)][0] = new Integer((l));
                                        	data[(l+1)][1] = new Integer((l)*binWidth);
                                        	data[(l+1)][(j+2)] = new Integer(psthData.get(l));
                                		*/
						setValueAt(new Integer(l),(l+1),0);
						setValueAt(new Integer(l*binWidth),(l+1),1);
						setValueAt(new Integer(psthData.get(l)),(l+1),(j+2));
					}
				}
			}
			//binWidChanged = false;
                //}
	}

        public int getColumnCount() {
            return columnNames.length;
        }
 
        public int getRowCount() {
            return data.length;
        }
 
        public String getColumnName(int col) {
            return columnNames[col];
        }
 
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }
 
        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
 
        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 2) {
                return false;
            } else {
                return true;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            /*if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                                   + " to " + value
                                   + " (an instance of "
                                   + value.getClass() + ")");
            }*/
 
            data[row][col] = value;
            fireTableCellUpdated(row, col);
 
            /*if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }*/
        }
	
        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();
 
            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }

  private class FiveAngleListener implements ActionListener{
	String type;
	public FiveAngleListener(String type){
		this.type = type;
	}
	public void actionPerformed(ActionEvent e){
		if (type.equals("oddball")){
		angleFilesL0 = new File[5];
		angleFilesH0 = new File[5];
		JPanel setup = new JPanel();
		JPanel L0 = new JPanel();
		JPanel H0 = new JPanel();
		JTabbedPane options = new JTabbedPane();		
		ButtonGroup oddb = new ButtonGroup();		
		JRadioButton hOdd = new JRadioButton("High-f Odd-Ball");
		hOdd.setToolTipText("High-f Odd-Ball, Low-f Standard");
		JRadioButton lOdd = new JRadioButton("Low-f Odd-Ball");
		lOdd.setToolTipText("Low-f Odd-Ball, High-f Standard");
		hOdd.setSelected(true);
		oddb.add(hOdd);
		oddb.add(lOdd);

		L0.setPreferredSize(new Dimension(600,200));
                H0.setPreferredSize(new Dimension(600,200));
                setup.setPreferredSize(new Dimension(650,275));
		
		setup.add(hOdd);
		setup.add(lOdd);
		setup.add(options);
		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);  
                Dimension d = separator.getPreferredSize();  
                d.width = L0.getPreferredSize().width;
                d.height = 3;
		separator.setPreferredSize(d);
		separator.setVisible(true);
		setup.add(separator);
		/*L0.setSize(1500,400);
		H0.setSize(1500,400);
		options.setSize(1500,400);
		setup.setSize(1500,400);*/
		L0.setLayout(new GridLayout(6,2));
		H0.setLayout(new GridLayout(6,2));
		//setup.setLayout(new GridLayout(6,2));
                JButton i90 = new JButton("Ipsilateral-90");
                JButton i45 = new JButton("Ipsilateral-45");
                JButton zero = new JButton("Mid-Line (0)");
                JButton c45 = new JButton("Contralateral-45");
                JButton c90 = new JButton("Contralateral-90");
		JLabel info = new JLabel("Choose Files:");
		JLabel i90status = new JLabel(": No File Selected");
		JLabel i45status = new JLabel(": No File Selected");
		JLabel zerostatus = new JLabel(": No File Selected");
		JLabel c45status = new JLabel(": No File Selected");
		JLabel c90status = new JLabel(": No File Selected");
		i90.addActionListener(new ExpOpenListener(i90status,angleFilesL0,4));
		i45.addActionListener(new ExpOpenListener(i45status,angleFilesL0,3));
		zero.addActionListener(new ExpOpenListener(zerostatus,angleFilesL0,2));
		c45.addActionListener(new ExpOpenListener(c45status,angleFilesL0,1));
		c90.addActionListener(new ExpOpenListener(c90status,angleFilesL0,0));
		i90status.setLabelFor(i90);
		i45status.setLabelFor(i45);
		zerostatus.setLabelFor(zero);
		c45status.setLabelFor(c45);
		c90status.setLabelFor(c90);
		L0.add(new JLabel(" Angle:"));
		L0.add(new JLabel(" File:"));
		L0.add(c90);
                L0.add(c90status);
		L0.add(c45);
                L0.add(c45status);
		L0.add(zero);
                L0.add(zerostatus);
		L0.add(i45);
                L0.add(i45status);
		L0.add(i90);
                L0.add(i90status);

		i90 = new JButton("Ipsilateral-90");
                i45 = new JButton("Ipsilateral-45");
                zero = new JButton("Mid-Line (0)");
                c45 = new JButton("Contralateral-45");
                c90 = new JButton("Contralateral-90");
                info = new JLabel("Choose Files:");
                i90status = new JLabel(": No File Selected");
                i45status = new JLabel(": No File Selected");
                zerostatus = new JLabel(": No File Selected");
                c45status = new JLabel(": No File Selected");
                c90status = new JLabel(": No File Selected");
                i90.addActionListener(new ExpOpenListener(i90status,angleFilesH0,4));
                i45.addActionListener(new ExpOpenListener(i45status,angleFilesH0,3));
                zero.addActionListener(new ExpOpenListener(zerostatus,angleFilesH0,2));
                c45.addActionListener(new ExpOpenListener(c45status,angleFilesH0,1));
                c90.addActionListener(new ExpOpenListener(c90status,angleFilesH0,0));
                i90status.setLabelFor(i90);
                i45status.setLabelFor(i45);
                zerostatus.setLabelFor(zero);
                c45status.setLabelFor(c45);
                c90status.setLabelFor(c90);
                H0.add(new JLabel(" Angle:"));
                H0.add(new JLabel(" File:"));
                H0.add(c90);
                H0.add(c90status);
                H0.add(c45);
                H0.add(c45status);
                H0.add(zero);
                H0.add(zerostatus);
                H0.add(i45);
                H0.add(i45status);
                H0.add(i90);
                H0.add(i90status);
                
		options.addTab("Low-f On-Zero",null,L0, "Low-f on-zero, High-f off-zero");
		options.addTab("High-f On-Zero",null,H0, "High-f on-zero, Low-f off-zero");
		/*
		L0.setPreferredSize(new Dimension(600,200));
                H0.setPreferredSize(new Dimension(600,200));
                setup.setPreferredSize(new Dimension(650,275));
		*/
		int result = JOptionPane.showConfirmDialog(null, setup,"5-Angle Experimental Setup", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			//if (
			int n = JOptionPane.showConfirmDialog(frame, "Clear all files & begin new 5-angle analysis?","New 5-Angle Analysis",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
      			if (n == JOptionPane.YES_OPTION) {
				try{
					experiment = "fiveangle";
					sf = new File[0];
					addNewFiles(angleFilesL0);	
					addNewFiles(angleFilesH0);
					graphs.setNewFiles(sf);
                			numNeurons = graphs.getNumNeurons();
                			psthWindow.newNeuronTabs();
                			rateWindow.newNeuronTabs(); //new analysisWindow("rate");
                			DRWindow.newNeuronTabs();// = new analysisWindow("DR");
                			countWindow.newNeuronTabs();
                			globalToolBar.setVisible(true);
				}
				catch (IOException ex){
                			//report
          			}
			}
		}
		}
	
		else if (type.equals("repetitive")){
			File[] angleFiles = new File[5];
		JPanel setup = new JPanel();
		setup.setLayout(new GridLayout(6,2));
                JButton i90 = new JButton("Ipsilateral-90");
                JButton i45 = new JButton("Ipsilateral-45");
                JButton zero = new JButton("Mid-Line (0)");
                JButton c45 = new JButton("Contralateral-45");
                JButton c90 = new JButton("Contralateral-90");
		JLabel info = new JLabel("Choose Files:");
		JLabel i90status = new JLabel(": No File Selected");
		JLabel i45status = new JLabel(": No File Selected");
		JLabel zerostatus = new JLabel(": No File Selected");
		JLabel c45status = new JLabel(": No File Selected");
		JLabel c90status = new JLabel(": No File Selected");
		i90.addActionListener(new ExpOpenListener(i90status,angleFiles,4));
		i45.addActionListener(new ExpOpenListener(i45status,angleFiles,3));
		zero.addActionListener(new ExpOpenListener(zerostatus,angleFiles,2));
		c45.addActionListener(new ExpOpenListener(c45status,angleFiles,1));
		c90.addActionListener(new ExpOpenListener(c90status,angleFiles,0));
		i90status.setLabelFor(i90);
		i45status.setLabelFor(i45);
		zerostatus.setLabelFor(zero);
		c45status.setLabelFor(c45);
		c90status.setLabelFor(c90);
		setup.add(new JLabel(" Angle:"));
		setup.add(new JLabel(" File:"));
		setup.add(c90);
                setup.add(c90status);
		setup.add(c45);
                setup.add(c45status);
		setup.add(zero);
                setup.add(zerostatus);
		setup.add(i45);
                setup.add(i45status);
		setup.add(i90);
                setup.add(i90status);
                setup.setPreferredSize(new Dimension(500,260));
		int result = JOptionPane.showConfirmDialog(null, setup,"5-Angle Experimental Setup", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			int n = JOptionPane.showConfirmDialog(frame, "Clear all files & begin new 5-angle analysis?","New 5-Angle Analysis",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
      			if (n == JOptionPane.YES_OPTION) {
				try{
					experiment = "fiveangle";
					sf = angleFiles;	
					graphs.setNewFiles(sf);
                			numNeurons = graphs.getNumNeurons();
                			psthWindow.newNeuronTabs();
                			rateWindow.newNeuronTabs(); //new analysisWindow("rate");
                			DRWindow.newNeuronTabs();// = new analysisWindow("DR");
                			countWindow.newNeuronTabs();
                			globalToolBar.setVisible(true);
				}
				catch (IOException ex){
                			//report
          			}
			}
		}
		}
	}
  }
  private class GlobalTimeListener implements PropertyChangeListener{
    public void propertyChange(PropertyChangeEvent e){
        Object source = e.getSource();
        if (source == enterRTime){
            if (globalRTime != ((Number)enterRTime.getValue()).intValue()){
	    	globalRTime = ((Number)enterRTime.getValue()).intValue();
	    	graphs.setTimeWindow(globalLTime, globalRTime);
	    	updateGraphics = true;
	    }
        }
        else if (source == enterLTime){
	    if (globalLTime != ((Number)enterLTime.getValue()).intValue()){
	    	globalLTime = ((Number)enterLTime.getValue()).intValue();
            	graphs.setTimeWindow(globalLTime, globalRTime);
	    	updateGraphics = true;
	    }
	}
    }
  }

  private class ExportListener implements ActionListener{
    String type;//,name;
    boolean all;
    public ExportListener(String type, boolean all){
      this.type = type;
      /*this.name = type.subString(0,type.length()-1);
      name.replaceFirst(name.charAt(0).toString(),Character.toUpperCase(name.charAt(0)).toString());
      */this.all = all;
    }
    public void actionPerformed(ActionEvent ae){
      if (type.equals("ssa")){
	if (graphs.getNumFreqs(psthWindow.getCurrentTab()) <2 ){
		JOptionPane.showMessageDialog(frame,"Selected file is not an SSA file","SSA File Error",JOptionPane.ERROR_MESSAGE);
      		return;
	}
      }

      JFileChooser chooser = new JFileChooser(saveDir);
      //chooser.setCurrentDirectory(new File(curDir.toAbsolutePath().toString()));
      chooser.setDialogTitle("Export");
      int retrival = chooser.showSaveDialog(null);
      if (retrival == JFileChooser.APPROVE_OPTION) {
        try {
	    FileWriter fw = new FileWriter(chooser.getSelectedFile()+".csv");
	    if (type.equals("psth")){
	      	if (all==false){
			int psthIndex=psthWindow.getCurrentTab();
	    		graphs.getPSTHFile(fw,psthIndex);
	    	}
		else{
			graphs.getAllPSTHFile(fw);
		}
	    }
	    else if (type.equals("count")){
		if (all==false){
	      		int countIndex = countWindow.getCurrentTab();
			graphs.getCountFile(fw,countIndex);
   	    	}
		else{
			graphs.getAllCountFile(fw);
		}
	    }
	    else if (type.equals("ssa")){
		if (all==false){
			int psthIndex = psthWindow.getCurrentTab();
			if (graphs.getNumFreqs(psthIndex) >=2 ){
				graphs.getSSAFile(fw,psthIndex);
			}
			else{
				JOptionPane.showMessageDialog(frame,"Selected file is not an SSA file","SSA File Error",JOptionPane.ERROR_MESSAGE);
			}
		}
		else{
			//graphs.getAllSSAFile(fw);
		}
	    }
            saveDir = chooser.getSelectedFile().getPath().toString();//.getAbsolutePath().toString();
        } 
        catch (IOException ex) {
            ex.printStackTrace();
        }
      }
    }
  }

  private class OpenListener implements ActionListener{
    private boolean multi;
    public OpenListener(boolean multi){
	this.multi = multi;
    }
    public void actionPerformed(ActionEvent ae){
	JFileChooser chooser = new JFileChooser(openDir);
	chooser.setAcceptAllFileFilterUsed(false);
	chooser.addChoosableFileFilter(new fileTypeFilter());
	chooser.setMultiSelectionEnabled(multi);
        int option = chooser.showOpenDialog(NeuronAnalysis.this);
        if (option == JFileChooser.APPROVE_OPTION) {
          File[] newFiles = chooser.getSelectedFiles();
	  try{
		addNewFiles(newFiles);
		graphs.addNewFiles(newFiles);
          	//graphs.setNewFiles(sf);
		numNeurons = graphs.getNumNeurons();
		psthWindow.newNeuronTabs(); 
		rateWindow.newNeuronTabs(); //new analysisWindow("rate");
		DRWindow.newNeuronTabs();// = new analysisWindow("DR");
		countWindow.newNeuronTabs();
		openDir = chooser.getSelectedFile().getPath().toString();
          	globalToolBar.setVisible(true);
          }
	  catch (Exception e){
		JOptionPane.showMessageDialog(frame,"A file could not be opened.","File Error",JOptionPane.ERROR_MESSAGE);
	  }
	  catch (Error e){
		JOptionPane.showMessageDialog(frame,e.getMessage(),"File Error",JOptionPane.ERROR_MESSAGE);
	  }/*
	  catch (IOException ex){
		JOptionPane.showMessageDialog(frame,"A file could not be opened.","File Error",JOptionPane.ERROR_MESSAGE);
		//report
	  }*/
      	}
    }
  }

  private class OpenRecentListener implements ActionListener{
    public void actionPerformed(ActionEvent ae){
        JFileChooser chooser = new JFileChooser(openDir);
        chooser.setAcceptAllFileFilterUsed(true);
        chooser.addChoosableFileFilter(new openRecentTypeFilter());
        chooser.setMultiSelectionEnabled(false);
        chooser.setDialogTitle("Open Previous Analysis");
	int option = chooser.showOpenDialog(NeuronAnalysis.this);
        if (option == JFileChooser.APPROVE_OPTION) {
          File newFile = chooser.getSelectedFile();
          int[][] times;
	  String[] basTitle, advTitle;
	  String[][][] allTitles;
	  try{
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(newFile));
		numNeurons = in.readInt();
		allTitles = new String[numNeurons][2][];
		try{
			experiment = (String)in.readObject();
		}
		catch (ClassNotFoundException e){
                }
                catch (EOFException e){
                }

		sf = new File[0];
		times = new int[numNeurons][2];
		for (int i=0; i<numNeurons; i++){
			try {
				addNewFile( (File) in.readObject());
				times[i][0] = in.readInt();
				times[i][1] = in.readInt();
				basTitle = (String[])(in.readObject());
				advTitle = (String[])(in.readObject());
				/*if (i==0){
					for (int j=0; j<basTitle.length;j++){
						System.out.print(basTitle[j]+",");
					}
				}						
				System.out.println("\n");
				if (i==0){
                                        for (int j=0; j<advTitle.length;j++){
                                                System.out.print(advTitle[j]+",");
                                        }
                                }*/
				allTitles[i][0] = basTitle;
				allTitles[i][1] = advTitle;
			}
			catch (ClassNotFoundException e){
				break;
			}
			catch (EOFException e){
				break;
			}
		}
		in.close();
		graphs.setNewFiles(sf,allTitles);
		numNeurons = graphs.getNumNeurons();
                psthWindow.newNeuronTabs();
                rateWindow.newNeuronTabs(); //new analysisWindow("rate");
                DRWindow.newNeuronTabs();// = new analysisWindow("DR");
                countWindow.newNeuronTabs();
                openDir = chooser.getSelectedFile().getPath().toString();
                for (int i=0; i<numNeurons; i++){
                        graphs.setIndivWindow(i,times[i][0],times[i][1]);
		}
		globalToolBar.setVisible(true);
          }
          catch (IOException ex){
                //report
          }
        }
    }
  }

  private class OpenOldRecentListener implements ActionListener{
    public void actionPerformed(ActionEvent ae){
        JFileChooser chooser = new JFileChooser(openDir);
        chooser.setAcceptAllFileFilterUsed(true);
        chooser.addChoosableFileFilter(new openRecentTypeFilter());
        chooser.setMultiSelectionEnabled(false);
        chooser.setDialogTitle("Open Previous Analysis");
	int option = chooser.showOpenDialog(NeuronAnalysis.this);
        if (option == JFileChooser.APPROVE_OPTION) {
          File newFile = chooser.getSelectedFile();
          int[][] times;
	  String[] basTitle = {"time","swep","freq","levl"};
	  String[] advTitle = {"oddb","ilevl","clevl"};
	  String[][][] allTitles;
	  try{
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(newFile));
		numNeurons = in.readInt();
		allTitles = new String[numNeurons][2][];
		try{
			experiment = (String)in.readObject();
		}
		catch (ClassNotFoundException e){
                }
                catch (EOFException e){
                }

		sf = new File[0];
		times = new int[numNeurons][2];
		for (int i=0; i<numNeurons; i++){
			try {
				addNewFile( (File) in.readObject());
				times[i][0] = in.readInt();
				times[i][1] = in.readInt();
				//basTitle = (String[])(in.readObject());
				//advTitle = (String[])(in.readObject());
				allTitles[i][0] = basTitle;
				allTitles[i][1] = advTitle;
			}
			catch (ClassNotFoundException e){
				break;
			}
			catch (EOFException e){
				break;
			}
		}
		in.close();
		graphs.setNewFiles(sf,allTitles);
		numNeurons = graphs.getNumNeurons();
                psthWindow.newNeuronTabs();
                rateWindow.newNeuronTabs(); //new analysisWindow("rate");
                DRWindow.newNeuronTabs();// = new analysisWindow("DR");
                countWindow.newNeuronTabs();
                openDir = chooser.getSelectedFile().getPath().toString();
                for (int i=0; i<numNeurons; i++){
                        graphs.setIndivWindow(i,times[i][0],times[i][1]);
		}
		globalToolBar.setVisible(true);
          }
          catch (IOException ex){
                //report
          }
        }
    }
  }


  private class SaveListener implements ActionListener{
    public void actionPerformed(ActionEvent ae){
      JFileChooser chooser = new JFileChooser(saveDir);
      //chooser.setCurrentDirectory(new File(curDir.toAbsolutePath().toString()));
      chooser.addChoosableFileFilter(new fileTypeFilter());
      int retrival = chooser.showSaveDialog(null);
      if (retrival == JFileChooser.APPROVE_OPTION) {
        try {
            graphs.saveFile(chooser.getSelectedFile(),experiment);
            saveDir = chooser.getSelectedFile().getPath().toString();//.getAbsolutePath().toString();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
      }
    }
  }

  private class ExpOpenListener implements ActionListener{
    private JLabel label;
    private File[] files;
    private int ind;
    public ExpOpenListener(JLabel label, File[] files, int ind){
	this.label = label;
    	this.files = files;
	this.ind = ind;
    }
    public void actionPerformed(ActionEvent ae){
        JFileChooser chooser = new JFileChooser(openDir);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(new fileTypeFilter());
        chooser.setMultiSelectionEnabled(false);
	chooser.setDialogTitle("Open 5-Angle File");
        int option = chooser.showOpenDialog(NeuronAnalysis.this);
        if (option == JFileChooser.APPROVE_OPTION) {
          	File newFile = chooser.getSelectedFile();
                files[ind] = newFile;
		label.setText(": "+newFile.getName());
		openDir = chooser.getSelectedFile().getPath().toString();
        }
    }
  }

  private class FilePropertyListener implements ActionListener{
	//@Override
	String[] pars = {"Time","Sweep","Frequency","Intensity"};
        String[] advPars = {"Odd-Ball","Ipsi-Level","Contra-Level"};
	public JFormattedTextField TB;
	public JFormattedTextField[] basTBoxes = new JFormattedTextField[pars.length];
	public JFormattedTextField[] advTBoxes = new JFormattedTextField[advPars.length];
	//public ArrayList<JFormattedTextField> basTBoxes, advTBoxes;
	public void actionPerformed(ActionEvent e){
		//String[] pars = {"Time","Sweep","Frequency","Intensity"};
		//String[] advPars = {"Odd-Ball","Ipsi-Level","Contra-Level"};
		String par;
		String[] basTitles = new String[pars.length];
		String[] advTitles = new String[advPars.length];
		//basTBoxes = new JFormattedTextField[pars.length];//new ArrayList<JFormattedtextField>();//[pars.length];
		//advTBoxes = new JFormattedTextField[advPars.length];//ArrayList<JFormattedtextField>();//[advPars.length];
		
		JPanel setup = new JPanel();
		JPanel basic = new JPanel();
		JPanel adv = new JPanel();
		JTabbedPane parOptions = new JTabbedPane();
    		basic.setLayout(new GridLayout(pars.length+1,2));
		adv.setLayout(new GridLayout(advPars.length+1,2));
        	
		//BASIC PARAMS:
		basic.add(new JLabel("Parameter"));
        	basic.add(new JLabel("Column Title"));
		for (int i=0; i<pars.length; i++){
			par = pars[i];
			TB = new JFormattedTextField();
			TB.setValue(graphs.getColumnTitle(i,"basic"));
			TB.setColumns(10);
                	TB.addPropertyChangeListener(new ColumnTitleListener(basTitles,i,"basic"));
                	TB.setMaximumSize(TB.getPreferredSize());
                	TB.setPreferredSize(TB.getPreferredSize());
			basTBoxes[i] = TB;
			basic.add(new JLabel(par+":"));
			basic.add(TB);
		}
		//ADV PARAMS:
		adv.add(new JLabel("Parameter"));
                adv.add(new JLabel("Column Title"));
		for (int i=0; i<advPars.length; i++){
                        par = advPars[i];
                        TB = new JFormattedTextField();
			TB.setValue(graphs.getColumnTitle(i,"adv"));
                        TB.setColumns(10);
                        TB.addPropertyChangeListener(new ColumnTitleListener(advTitles,i,"adv"));
                        TB.setMaximumSize(TB.getPreferredSize());
                        TB.setPreferredSize(TB.getPreferredSize());
			advTBoxes[i] = TB;
                        adv.add(new JLabel(par+":"));
                        adv.add(TB);
                }
		
		parOptions.addTab("Basic",null,basic, "Basic File Properties");
		parOptions.addTab("Advanced",null,adv, "Advanced File Properties");
		
		setup.add(parOptions);

		basic.setPreferredSize(basic.getPreferredSize());
                adv.setPreferredSize(adv.getPreferredSize());
		setup.setPreferredSize(setup.getPreferredSize());

		int result = JOptionPane.showConfirmDialog(null, setup,"File Properties", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
  		if (result == JOptionPane.OK_OPTION){
			int n = JOptionPane.showConfirmDialog(frame, "Apply new File properties to all new files opened?","Apply Settings",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (n == JOptionPane.YES_OPTION){
				for (int i=0; i<pars.length;i++){
					graphs.setColumnTitle(i,basTitles[i],"basic");
					System.out.println(basTitles[i]);
				}
				for (int i=0; i<advPars.length;i++){
					graphs.setColumnTitle(i,advTitles[i],"adv");
					System.out.println(advTitles[i]);
				}
			}
		}
	}
	
	public class ColumnTitleListener implements PropertyChangeListener{
  		private int i;
		private String[] titles;
		private String type;
		public ColumnTitleListener(String[] titles,int i,String type){
			this.titles = titles;		
			this.i = i;
			this.type = type;
		}		
		public void propertyChange(PropertyChangeEvent e){
			Object source = e.getSource();
			//JFormattedTextField text = (JFormattedTextField)(e);
			String entry = "";
			if (type.equals("basic")){
				if (source == basTBoxes[i]){
					entry = (String)(basTBoxes[i].getValue().toString());
				}
			}
			else if (type.equals("adv")){
				if (source == advTBoxes[i]){
					entry = (String)(advTBoxes[i].getValue().toString());
				}
			} 	
			if (entry == null || entry.equals("")){
				titles[i] = graphs.getColumnTitle(i,type);
			}
			else{
				titles[i] = entry;
			}
		}	
	}
  }

  private class ClearButtonListener implements ActionListener{
    boolean all;
    public ClearButtonListener(boolean all){
	this.all = all;
    }
    public void actionPerformed(ActionEvent event){
      if (all){
      	int n = JOptionPane.showConfirmDialog(frame, "Clear all files & begin new analysis?","Clear All Files",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
      	if (n == JOptionPane.YES_OPTION) {
        	try{
        		experiment = "";
	 		sf = new File[0];
         		graphs.setNewFiles(sf);
         		numNeurons = graphs.getNumNeurons();
         		psthWindow.newNeuronTabs();
         		rateWindow.newNeuronTabs(); //new analysisWindow("rate");
         		DRWindow.newNeuronTabs();
         		countWindow.newNeuronTabs();
  	 		deactivateMenu();
  		}
        	catch (IOException ex){
         		//report
        	}
      	}
      }
      else{
	if (experiment == ""){
		int n = JOptionPane.showConfirmDialog(frame, "Close current file and remove from analysis window?","Close Current File",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        	if (n == JOptionPane.YES_OPTION) {
			int tabToRemove = psthWindow.getCurrentTab();
			graphs.removeFile(tabToRemove);
			psthWindow.remove(tabToRemove);
			DRWindow.remove(tabToRemove);
			rateWindow.remove(tabToRemove);
			countWindow.remove(tabToRemove);			
			numNeurons = graphs.getNumNeurons();
		}
	}
      }
    }
  }

  private class updateButtonListener implements ActionListener{
    public void actionPerformed(ActionEvent ae){
    	int n = JOptionPane.showConfirmDialog(frame, "Re-load All Files?","Update Files",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
	if (n == JOptionPane.YES_OPTION) {
		try{
			//File[] newFiles = Arrays.copyOf(sf, sf.length);
			//sf = new File[0];
			graphs.setNewFiles(sf);
			//addNewFiles(newFiles);
    			//graphs.addNewFiles(newFiles);
    			numNeurons = graphs.getNumNeurons();
    			psthWindow.newNeuronTabs();
    			rateWindow.newNeuronTabs(); //new analysisWindow("rate");
    			DRWindow.newNeuronTabs();// = new analysisWindow("DR");
   			countWindow.newNeuronTabs();
    			globalToolBar.setVisible(true);
    		}
		catch (IOException ex){
                        //report
                }
		catch (Exception e){
                	JOptionPane.showMessageDialog(frame,"A file could not be opened.","File Error",JOptionPane.ERROR_MESSAGE);
          	}
          	catch (Error e){
                	JOptionPane.showMessageDialog(frame,e.getMessage(),"File Error",JOptionPane.ERROR_MESSAGE);
          	}
	}
    }
  }
  
  private class calculateFSLListener implements ActionListener{
    boolean all;
    public calculateFSLListener(boolean all){
	this.all = all;
    }
    public void actionPerformed(ActionEvent e){
	if (all){
		String s = (String)JOptionPane.showInputDialog(frame,"Enter Spontaneous Firing Rate:\n","Calculate First-Spike Latency",JOptionPane.PLAIN_MESSAGE,null,null,0+"");
		graphs.setAllSpont(Double.parseDouble(s));
	}
	else{
		String s = (String)JOptionPane.showInputDialog(frame,"Enter Neuron "+psthWindow.getCurrentTab()+"'s Spontaneous Firing Rate:\n","Calculate First-Spike Latency",JOptionPane.PLAIN_MESSAGE,null,null,graphs.getSpont(psthWindow.getCurrentTab())+"");
    		graphs.setSpont(psthWindow.getCurrentTab(),Double.parseDouble(s));
	}
    }
  }

  private class GenerateSpontListener implements ActionListener{
    String type;
    public GenerateSpontListener(String type){
	this.type = type;
    }
    public void actionPerformed(ActionEvent e){
      if (!type.equals("reset")){
      	String s = (String)JOptionPane.showInputDialog(frame,"Enter New Spontaneous Rate:\n","Change Spontaneous Activity",JOptionPane.PLAIN_MESSAGE,null,null,graphs.getAddedSpont()+"");	
      	if ((s != null) && (s.length() > 0)) {
        	double r = Double.parseDouble(s);
		graphs.setAddedSpont(r,type);
        	return;
      	}
      }
      else{
	graphs.setAddedSpont(0.0d,type);
        return;
      }
    }
  }

  private class PSTHyaxisListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
	String s = (String)JOptionPane.showInputDialog(frame,"Enter New Y-Axis Size:\n","Format PSTH Y-Axis",JOptionPane.PLAIN_MESSAGE,null,null,graphs.getCustomPSTHy(psthWindow.getCurrentTab())+"");
    	if ((s != null) && (s.length() > 0)) {
		int r = Integer.parseInt(s);
		int n = JOptionPane.showConfirmDialog(frame, "Apply PSTH-Settings to All Files?","Apply To All",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (n == JOptionPane.YES_OPTION){
			graphs.setCustomPSTHyAll(r);
			graphs.setIsCustomPSTHyAll(true);
		}
		else{
			graphs.setCustomPSTHy(psthWindow.getCurrentTab(),r);
			graphs.setIsCustomPSTHy(psthWindow.getCurrentTab(),true);
		}
	}
    }
  }
  
  private class DRDotListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
	String s = (String)JOptionPane.showInputDialog(frame,"Enter New Dot-Size:\n","Format Dot-Raster",JOptionPane.PLAIN_MESSAGE,null,null,graphs.getDotSize(DRWindow.getCurrentTab())+"");
	if ((s != null) && (s.length() > 0)) {
                int r = Integer.parseInt(s);
		int n = JOptionPane.showConfirmDialog(frame, "Apply Dot-Settings to All Files?","Apply To All",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (n == JOptionPane.YES_OPTION){
			graphs.setDotSizeAll(r);
		}
		else{
			graphs.setDotSize(DRWindow.getCurrentTab(),r);
                }
		return;
        }
    }
  }

  private class AboutListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
    	//int n = JOptionPane.showConfirmDialog(frame, "     NeuLab\n     Version 1.0\n     © 2014 Arjun Balachandar","About NeuLab",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
    	JOptionPane.showMessageDialog(frame,"     NeuLab\n     Version 1.0\n     © 2014 Arjun Balachandar","About NeuLab",JOptionPane.PLAIN_MESSAGE);
    }
  }

  private class HelpListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
	JOptionPane.showMessageDialog(frame,"Please contact Arjun Balachandar by email at arjunb07@hotmail.com","Help",JOptionPane.PLAIN_MESSAGE);
    }
  }
 
  private class NewButtonListener implements ActionListener{
    public void actionPerformed(ActionEvent event){
      newFrame = true;
    }
  }

  private class CloseButtonListener implements ActionListener{
    public void actionPerformed(ActionEvent event){
      closeFrame = true;
    }
  }

  private class ChangeWidListener implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent event){
      String s = (String)JOptionPane.showInputDialog(frame,"Enter Bin-Width:\n","Change PSTH Bin-Width",JOptionPane.PLAIN_MESSAGE,null,null,binWidth+"");
 
      if ((s != null) && (s.length() > 0)) {
	binWidth = Integer.parseInt(s);	
	enterBinWid.setValue(new Integer(binWidth));
	//psthWindow.setBinWidth(binWidth);
	psthWindow.updatePSTH();
	resetPSTHBin();
	return;
      }
    }
  }

  private class BinTextListener implements PropertyChangeListener{
        public void propertyChange(PropertyChangeEvent e){
                Object source = e.getSource();
                if (source == enterBinWid){
                    binWidth = ((Number)enterBinWid.getValue()).intValue();
                        psthWindow.updatePSTH();
                        resetPSTHBin();
                }
                updateGraphics = true;
        }
    }

  private class ExitListener implements ActionListener{
    @Override
      public void actionPerformed(ActionEvent event) {
       	if (menuActivated){
		int n = JOptionPane.showConfirmDialog(frame, "Do you want to save your analysis?","",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
		if (n == JOptionPane.YES_OPTION){
			JFileChooser chooser = new JFileChooser(saveDir);
      			chooser.addChoosableFileFilter(new fileTypeFilter());
      			int retrival = chooser.showSaveDialog(null);
      			if (retrival == JFileChooser.APPROVE_OPTION) {
        			try {
            				graphs.saveFile(chooser.getSelectedFile(),experiment);
            				saveDir = chooser.getSelectedFile().getPath().toString();//.getAbsolutePath().toString();
        			}
        			catch (IOException ex) {
        			    ex.printStackTrace();
        			}
      			}
		}
		else if (n == JOptionPane.NO_OPTION){
			System.exit(0);
		}
	}
	else{
		System.exit(0);
	}
      }
  }

  private class PrintUIWindow implements Printable, ActionListener {
        JFrame frameToPrint;

  	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {

    		if (page > 0) { /* We have only one page, and 'page' is zero-based */
     			return NO_SUCH_PAGE;
    		}

    		/*
    		 * User (0,0) is typically outside the imageable area, so we must translate
    		 * by the X and Y values in the PageFormat to avoid clipping
    		 */

		Dimension compSize = new Dimension(maxlen,maxwid);
            	// Make sure we size to the preferred size
            	frameToPrint.setSize(compSize);
            	// Get the the print size
            	Dimension printSize = new Dimension();
            	printSize.setSize(pf.getImageableWidth(), pf.getImageableHeight());

            	// Calculate the scale factor
            	double scaleFactor = getScaleFactorToFit(compSize, printSize);
            	// Don't want to scale up, only want to scale down
            	if (scaleFactor > 1d) {
                	scaleFactor = 1d;
            	}
            	// Calcaulte the scaled size...
            	double scaleWidth = maxlen * scaleFactor;
            	double scaleHeight = maxwid * scaleFactor;

		// Create a clone of the graphics context.  This allows us to manipulate
            	// the graphics context without begin worried about what effects
            	// it might have once we're finished
            	Graphics2D g2 = (Graphics2D) g.create();
            	// Calculate the x/y position of the component, this will center
            	// the result on the page if it can
            	double x = ((pf.getImageableWidth() - scaleWidth) / 2d) + pf.getImageableX();
            	double y = ((pf.getImageableHeight() - scaleHeight) / 2d) + pf.getImageableY();
            	// Create a new AffineTransformation
            	AffineTransform at = new AffineTransform();
            	// Translate the offset to out "center" of page
            	at.translate(x, y);
            	// Set the scaling
            	at.scale(scaleFactor, scaleFactor);
            	// Apply the transformation
            	g2.transform(at);
            	// Print the component
            	frameToPrint.printAll(g2);
            	// Dispose of the graphics context, freeing up memory and discarding
            	// our changes
            	g2.dispose();
		//frameToPrint.revalidate();

    		return PAGE_EXISTS;
  	}

  	public void actionPerformed(ActionEvent e) {
    		PrinterJob job = PrinterJob.getPrinterJob();
    		PageFormat pf = job.defaultPage();
		pf.setOrientation(PageFormat.LANDSCAPE);
		
		job.setPrintable(this,pf);
    		boolean ok = job.printDialog();
    		if (ok) {
      			try {
        			job.print();
      			} 		
			catch (PrinterException ex) {
        			/* The job did not successfully complete */
      			}
    		}
  	}

  	public PrintUIWindow(JFrame f) {
    		frameToPrint = f;
  	}
  }
  
  /*public void propertyChange(PropertyChangeEvent e){
        Object source = e.getSource();
        if (source == enterBinWid){
            binWidth = ((Number)enterBinWid.getValue()).intValue();
 	}
        //double payment = computePayment(amount, rate, numPeriods);
        //paymentField.setValue(new Double(payment));
  }*/
  
  public void setNewFrame(boolean a){
	this.newFrame = a;
  }
  public boolean getNewFrame(){
        return this.newFrame;
  }

  public boolean getCloseFrame(){
	return this.closeFrame;
  }
  public void setCloseFrame(boolean a){
  	this.closeFrame = a;
  }

  public void resetPSTHBin(){
 	if (graphs.getPSTHWid()!=binWidth){
  		if (binWidth>0 && binWidth<=graphs.totTime){
			graphs.resetBinWidth(binWidth);
  			//binWidChanged = true;
			//for (int i=0; i<numNeurons; i++){
			//psthWindow.updatePSTH();
			//}
		}
		else if (binWidth <= 0){
			binWidth = 1;
			enterBinWid.setValue(new Integer(binWidth));
			graphs.resetBinWidth(binWidth);
		}
		else if (binWidth > graphs.totTime){
			binWidth = graphs.totTime;
			enterBinWid.setValue(new Integer(binWidth));
			graphs.resetBinWidth(binWidth);
		}
	}
  }

  public boolean getUpdate(){
	return updateGraphics;
  }
  public void setUpdate(boolean a){
	this.updateGraphics = a;
  }
  
  public void move() throws IOException{
    graphWid = (int)psthWindow.getSize().getHeight()-125;
    graphLen = (int)psthWindow.getSize().getWidth()-100;
    //curlen = (int)this.getSize().getWidth();
    //curwid = (int)this.getSize().getHeight();
    //System.out.println("height="+graphWid+" , length="+graphLen);
    //System.out.println("
    if (curlen != graphLen){
	this.maxlen = (int)screenSize.getWidth();
    	this.maxwid = (int)screenSize.getHeight();
    	graphLen = 50*(((graphLen)+49)/50);
	graphs.setLen(graphLen);
	curlen = graphLen;
    }
    if (curwid != graphWid){
	this.maxlen = (int)screenSize.getWidth();
    	this.maxwid = (int)screenSize.getHeight();
	graphs.setStartingY(graphWid+40);
	graphWid = 5*(((graphWid)+4)/5);
	graphs.setWid(graphWid);
	curwid = graphWid;
    }
    //resetPSTHBin();
  }
  
  public void initLinePos(){
    graphWid = (int)psthWindow.getSize().getHeight()-110;
    graphLen = (int)psthWindow.getSize().getWidth()-100;
    curlen = graphLen;//(int)this.getSize().getWidth();
    curwid = graphWid;//(int)this.getSize().getHeight();
    //System.out.println("height="+graphWid+" , length="+graphLen);
    graphLen = 50*(((graphLen)+49)/50);
    graphWid = 10*(((graphWid)+9)/10);
    graphs.setLen(graphLen);
    graphs.setWid(graphWid);
    //graphs.setStartingY(0);
    graphs.update();
  }
  
  public void paint(Graphics g){
    graphs.updateCount();
    if (experiment.equals("")){
	if (countWindow.getCurrentTab() != curTab){
		curTab = countWindow.getCurrentTab();
		psthWindow.setCurrentTab(curTab);
		/*if (experiment.equals("")){
			countWindow.setCurrentTab(curTab);
    		}*/
		rateWindow.setCurrentTab(curTab);
    		DRWindow.setCurrentTab(curTab);
    	}   
    }
    else{
    	if (psthWindow.getCurrentTab() != curTab){
		curTab = psthWindow.getCurrentTab();
		if (experiment.equals("")){
			countWindow.setCurrentTab(curTab);
    		}
		rateWindow.setCurrentTab(curTab);
    		DRWindow.setCurrentTab(curTab);
    	}
    }
    super.paint(g);
  }

  public static void delay(long len){
    try{
      Thread.sleep(len);
     }
    catch(InterruptedException ex){
      System.out.println(ex);
     }
  }

  public void addNewFiles(File[] newFiles){
    ArrayList<File> allFiles =new ArrayList<File>(Arrays.asList(sf));
    for (int i=0;i<newFiles.length;i++){
      allFiles.add(newFiles[i]);
    }
    sf = allFiles.toArray(new File[allFiles.size()]);
    allFiles.clear();
  }
  public void addNewFile(File newFile){
	ArrayList<File> allFiles =new ArrayList<File>(Arrays.asList(sf));
      	allFiles.add(newFile);
    	sf = allFiles.toArray(new File[allFiles.size()]);
    	allFiles.clear();
  }

  public static double getScaleFactorToFit(Dimension original, Dimension toFit) {
        double dScale = 1d;
        if (original != null && toFit != null) {
            double dScaleWidth = getScaleFactor(original.width, toFit.width);
            double dScaleHeight = getScaleFactor(original.height, toFit.height);
            dScale = Math.min(dScaleHeight, dScaleWidth);
        }
        return dScale;
  }
  public static double getScaleFactor(int iMasterSize, int iTargetSize) {
        double dScale = 1;
        if (iMasterSize > iTargetSize) {
            dScale = (double) iTargetSize / (double) iMasterSize;
        } else {
            dScale = (double) iTargetSize / (double) iMasterSize;
        }
        return dScale;
  }
  
}


