/*
 	Win FMT, Copyright 2012, Raymond C. Borges Hink
    This file is part of WinFMT.

    WinFMT is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    WinFMT is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with WinFMT.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeSet;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.ScrollPaneConstants;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;


public class GUItimeline {
	static ArrayList<String> filePaths = new ArrayList<String>();
	static ArrayList<String> directoryPaths = new ArrayList<String>();
	private JTextArea txtrWinfmtCopyright = new JTextArea();
	private JFrame frmFileMetadataTimeline;
	private JComboBox<String> comboBox;
	private JComboBox<String> comboBox_1;
	private JButton btnNewButton;
	public JList<String> fileTypesList = new JList<String>();

	//Method to sort hashmap, from http://www.lampos.net/how-to-sort-hashmap
		public static HashMap<String, Long> sortHashMap(HashMap<String, Long> input){
		    Map<String, Long> tempMap = new HashMap<String, Long>();
		    for (String wsState : input.keySet()){
		        tempMap.put(wsState,input.get(wsState));
		    }

		    List<String> mapKeys = new ArrayList<String>(tempMap.keySet());
		    List<Long> mapValues = new ArrayList<Long>(tempMap.values());
		    HashMap<String, Long> sortedMap = new LinkedHashMap<String, Long>();
		    TreeSet<Long> sortedSet = new TreeSet<Long>(mapValues);
		    Object[] sortedArray = sortedSet.toArray();
		    int size = sortedArray.length;
		    for (int i=0; i<size; i++){
		        sortedMap.put(mapKeys.get(mapValues.indexOf(sortedArray[i])), 
		                      (Long)sortedArray[i]);
		    }
		    return sortedMap;
		}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUItimeline window = new GUItimeline();
					window.frmFileMetadataTimeline.setVisible(true);
					window.redirectSystemStreams();
					window.frmFileMetadataTimeline.setVisible(true);			
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the application.
	 */
	public GUItimeline() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFileMetadataTimeline = new JFrame();
		frmFileMetadataTimeline.setResizable(false);
		frmFileMetadataTimeline.setTitle("File Metadata Timeline by IMF");
		frmFileMetadataTimeline.setBounds(100, 100, 950, 600);
		frmFileMetadataTimeline.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{125, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		frmFileMetadataTimeline.getContentPane().setLayout(gridBagLayout);
		JButton btnGo = new JButton("Find content");
		
		
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtrWinfmtCopyright.setText("");
				final contentFinder startParsing = new contentFinder();
				
				Thread t = new Thread() {
					
					public void run() {
						try {
							startParsing.main();
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					}
				};
				t.start();
				
			}//Button action performed
			
		});//End of action listener
		GridBagConstraints gbc_btnGo = new GridBagConstraints();
		gbc_btnGo.anchor = GridBagConstraints.SOUTH;
		gbc_btnGo.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnGo.insets = new Insets(0, 0, 5, 5);
		gbc_btnGo.gridx = 0;
		gbc_btnGo.gridy = 0;
		frmFileMetadataTimeline.getContentPane().add(btnGo, gbc_btnGo);
		
		JLabel lblFrom = new JLabel("From:");
		GridBagConstraints gbc_lblFrom = new GridBagConstraints();
		gbc_lblFrom.insets = new Insets(0, 0, 5, 5);
		gbc_lblFrom.anchor = GridBagConstraints.EAST;
		gbc_lblFrom.gridx = 1;
		gbc_lblFrom.gridy = 0;
		frmFileMetadataTimeline.getContentPane().add(lblFrom, gbc_lblFrom);
		
		JLabel lblMonth = new JLabel("Month");
		GridBagConstraints gbc_lblMonth = new GridBagConstraints();
		gbc_lblMonth.insets = new Insets(0, 0, 5, 5);
		gbc_lblMonth.anchor = GridBagConstraints.EAST;
		gbc_lblMonth.gridx = 2;
		gbc_lblMonth.gridy = 0;
		frmFileMetadataTimeline.getContentPane().add(lblMonth, gbc_lblMonth);
		
		final JComboBox<String> comboBoxFromMonth = new JComboBox<String>();
		comboBoxFromMonth.setModel(new DefaultComboBoxModel<String>(new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"}));
		GridBagConstraints gbc_comboBoxFromMonth = new GridBagConstraints();
		gbc_comboBoxFromMonth.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxFromMonth.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxFromMonth.gridx = 3;
		gbc_comboBoxFromMonth.gridy = 0;
		frmFileMetadataTimeline.getContentPane().add(comboBoxFromMonth, gbc_comboBoxFromMonth);
		
		JLabel lblDay = new JLabel("Day");
		GridBagConstraints gbc_lblDay = new GridBagConstraints();
		gbc_lblDay.anchor = GridBagConstraints.EAST;
		gbc_lblDay.insets = new Insets(0, 0, 5, 5);
		gbc_lblDay.gridx = 4;
		gbc_lblDay.gridy = 0;
		frmFileMetadataTimeline.getContentPane().add(lblDay, gbc_lblDay);
		
		final JComboBox<String> comboBoxFromDay = new JComboBox<String>();
		comboBoxFromDay.setModel(new DefaultComboBoxModel<String>(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
		GridBagConstraints gbc_comboBoxFromDay = new GridBagConstraints();
		gbc_comboBoxFromDay.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxFromDay.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxFromDay.gridx = 5;
		gbc_comboBoxFromDay.gridy = 0;
		frmFileMetadataTimeline.getContentPane().add(comboBoxFromDay, gbc_comboBoxFromDay);
		
		JLabel lblYear = new JLabel("Year");
		GridBagConstraints gbc_lblYear = new GridBagConstraints();
		gbc_lblYear.anchor = GridBagConstraints.EAST;
		gbc_lblYear.insets = new Insets(0, 0, 5, 5);
		gbc_lblYear.gridx = 6;
		gbc_lblYear.gridy = 0;
		frmFileMetadataTimeline.getContentPane().add(lblYear, gbc_lblYear);
		
		final JComboBox<String> comboBoxFromYear = new JComboBox<String>();
		comboBoxFromYear.setModel(new DefaultComboBoxModel<String>(new String[] {"1970", "1971", "1972", "1973", "1974", "1975", "1976", "1977", "1978", "1979", "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1989", "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036", "2037", "2038", "2039", "2040", "2041", "2042", "2043", "2044", "2045", "2046", "2047", "2048", "2049", "2050"}));
		GridBagConstraints gbc_comboBoxFromYear = new GridBagConstraints();
		gbc_comboBoxFromYear.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxFromYear.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxFromYear.gridx = 7;
		gbc_comboBoxFromYear.gridy = 0;
		frmFileMetadataTimeline.getContentPane().add(comboBoxFromYear, gbc_comboBoxFromYear);
		
		JLabel lblSelectAttributesTo = new JLabel("Sort timeline by:");
		GridBagConstraints gbc_lblSelectAttributesTo = new GridBagConstraints();
		gbc_lblSelectAttributesTo.anchor = GridBagConstraints.EAST;
		gbc_lblSelectAttributesTo.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelectAttributesTo.gridx = 8;
		gbc_lblSelectAttributesTo.gridy = 0;
		frmFileMetadataTimeline.getContentPane().add(lblSelectAttributesTo, gbc_lblSelectAttributesTo);
		
		final JComboBox<String> comboBoxTime = new JComboBox<String>();
		comboBoxTime.setModel(new DefaultComboBoxModel<String>(new String[] {"Creation date", "Last Accessed date", "Last Modified date"}));
		GridBagConstraints gbc_comboBoxTime = new GridBagConstraints();
		gbc_comboBoxTime.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxTime.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxTime.gridx = 9;
		gbc_comboBoxTime.gridy = 0;
		frmFileMetadataTimeline.getContentPane().add(comboBoxTime, gbc_comboBoxTime);
		
		JButton btnAboutFmt = new JButton("About FMT");
		btnAboutFmt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {		
				
				JOptionPane.showMessageDialog(null, "This tool was designed by the IMF team for the DC3 2012 competition."+"\n"
			+"\tIMF Team Members:\n\nMain designer:\nRaymond C. Borges Hink\nwww.raymondborges.com\n\nMain tester:\nJarilyn M. Hernandez Jimenez\nwww.jarilynhernandez.com\n");
			}
		});
		GridBagConstraints gbc_btnAboutFmt = new GridBagConstraints();
		gbc_btnAboutFmt.gridwidth = 6;
		gbc_btnAboutFmt.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAboutFmt.insets = new Insets(0, 0, 5, 5);
		gbc_btnAboutFmt.gridx = 10;
		gbc_btnAboutFmt.gridy = 0;
		frmFileMetadataTimeline.getContentPane().add(btnAboutFmt, gbc_btnAboutFmt);
		
		JButton btnListVolumes = new JButton("List Volumes");
		btnListVolumes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				//Clear textArea
				txtrWinfmtCopyright.setText("");
				System.out.println("Program Started");
				System.out.println("====================");
				
				//Lists all drives
		        FileSystemView fsv = FileSystemView.getFileSystemView();
		        File[] f = File.listRoots();
		        System.out.println("\nAvailable physical and logical drives: ");
		        for (int i = 0; i < f.length; i++)
		        {
		        	System.out.println("\nDrive: " + f[i]);
		            System.out.println("Display name: " + fsv.getSystemDisplayName(f[i]));
		            System.out.println("Is drive: " + fsv.isDrive(f[i]));
		            System.out.println("Readable: " + f[i].canRead());
		            System.out.println("Total space: " + f[i].getTotalSpace()+"bytes\n");
		        }
		        //End of listing drives
		        System.out.println("\n\t***Finished***");
			}
		});
		GridBagConstraints gbc_btnListVolumes = new GridBagConstraints();
		gbc_btnListVolumes.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnListVolumes.insets = new Insets(0, 0, 5, 5);
		gbc_btnListVolumes.gridx = 0;
		gbc_btnListVolumes.gridy = 1;
		frmFileMetadataTimeline.getContentPane().add(btnListVolumes, gbc_btnListVolumes);
		
		JLabel lblTo = new JLabel("To:");
		GridBagConstraints gbc_lblTo = new GridBagConstraints();
		gbc_lblTo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTo.gridx = 1;
		gbc_lblTo.gridy = 1;
		frmFileMetadataTimeline.getContentPane().add(lblTo, gbc_lblTo);
		
		JLabel lblMonth_1 = new JLabel("Month");
		GridBagConstraints gbc_lblMonth_1 = new GridBagConstraints();
		gbc_lblMonth_1.anchor = GridBagConstraints.EAST;
		gbc_lblMonth_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblMonth_1.gridx = 2;
		gbc_lblMonth_1.gridy = 1;
		frmFileMetadataTimeline.getContentPane().add(lblMonth_1, gbc_lblMonth_1);
		
		final JComboBox<String> comboBoxToMonth = new JComboBox<String>();
		comboBoxToMonth.setModel(new DefaultComboBoxModel<String>(new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"}));
		GridBagConstraints gbc_comboBoxToMonth = new GridBagConstraints();
		gbc_comboBoxToMonth.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxToMonth.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxToMonth.gridx = 3;
		gbc_comboBoxToMonth.gridy = 1;
		frmFileMetadataTimeline.getContentPane().add(comboBoxToMonth, gbc_comboBoxToMonth);
		
		JLabel lblDay_1 = new JLabel("Day");
		GridBagConstraints gbc_lblDay_1 = new GridBagConstraints();
		gbc_lblDay_1.anchor = GridBagConstraints.EAST;
		gbc_lblDay_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblDay_1.gridx = 4;
		gbc_lblDay_1.gridy = 1;
		frmFileMetadataTimeline.getContentPane().add(lblDay_1, gbc_lblDay_1);
		
		final JComboBox<String> comboBoxToDay = new JComboBox<String>();
		comboBoxToDay.setModel(new DefaultComboBoxModel<String>(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
		GridBagConstraints gbc_comboBoxToDay = new GridBagConstraints();
		gbc_comboBoxToDay.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxToDay.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxToDay.gridx = 5;
		gbc_comboBoxToDay.gridy = 1;
		frmFileMetadataTimeline.getContentPane().add(comboBoxToDay, gbc_comboBoxToDay);
		
		JLabel lblYear_1 = new JLabel("Year");
		GridBagConstraints gbc_lblYear_1 = new GridBagConstraints();
		gbc_lblYear_1.anchor = GridBagConstraints.EAST;
		gbc_lblYear_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblYear_1.gridx = 6;
		gbc_lblYear_1.gridy = 1;
		frmFileMetadataTimeline.getContentPane().add(lblYear_1, gbc_lblYear_1);
		
		final JComboBox<String> comboBoxToYear = new JComboBox<String>();
		comboBoxToYear.setModel(new DefaultComboBoxModel<String>(new String[] {"1970", "1971", "1972", "1973", "1974", "1975", "1976", "1977", "1978", "1979", "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1989", "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036", "2037", "2038", "2039", "2040", "2041", "2042", "2043", "2044", "2045", "2046", "2047", "2048", "2049", "2050"}));
		GridBagConstraints gbc_comboBoxToYear = new GridBagConstraints();
		gbc_comboBoxToYear.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxToYear.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxToYear.gridx = 7;
		gbc_comboBoxToYear.gridy = 1;
		frmFileMetadataTimeline.getContentPane().add(comboBoxToYear, gbc_comboBoxToYear);
		
		JLabel lblSortTimelineBy = new JLabel("Attributes to include: ");
		GridBagConstraints gbc_lblSortTimelineBy = new GridBagConstraints();
		gbc_lblSortTimelineBy.anchor = GridBagConstraints.EAST;
		gbc_lblSortTimelineBy.insets = new Insets(0, 0, 5, 5);
		gbc_lblSortTimelineBy.gridx = 8;
		gbc_lblSortTimelineBy.gridy = 1;
		frmFileMetadataTimeline.getContentPane().add(lblSortTimelineBy, gbc_lblSortTimelineBy);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 9;
		gbc_scrollPane_1.gridy = 1;
		frmFileMetadataTimeline.getContentPane().add(scrollPane_1, gbc_scrollPane_1);
		
		final JList<String> attributeList = new JList<String>();
		attributeList.setToolTipText("Hold Ctrl button down while clicking on items to select multiple items \r\nor use Shift and arrow keys.");
		attributeList.setModel(new AbstractListModel<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			String[] values = new String[] {"Filename", "File extension", "Filepath", "Logical size", "Creation date", "Last Accessed date", "Last Modified date", "Hidden file"};
			public int getSize() {
				return values.length;
			}
			public String getElementAt(int index) {
				return values[index];
			}
		});
		scrollPane_1.setViewportView(attributeList);
		
		JLabel lblExportResultsAs = new JLabel("Export as: ");
		GridBagConstraints gbc_lblExportResultsAs = new GridBagConstraints();
		gbc_lblExportResultsAs.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblExportResultsAs.insets = new Insets(0, 0, 5, 5);
		gbc_lblExportResultsAs.gridx = 10;
		gbc_lblExportResultsAs.gridy = 1;
		frmFileMetadataTimeline.getContentPane().add(lblExportResultsAs, gbc_lblExportResultsAs);
		
		final JComboBox<String> comboBoxExport = new JComboBox<String>();
		comboBoxExport.setModel(new DefaultComboBoxModel<String>(new String[] {"CSV", "Text Report"}));
		comboBoxExport.setSelectedIndex(0);
		GridBagConstraints gbc_comboBoxExport = new GridBagConstraints();
		gbc_comboBoxExport.gridwidth = 5;
		gbc_comboBoxExport.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxExport.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxExport.gridx = 11;
		gbc_comboBoxExport.gridy = 1;
		frmFileMetadataTimeline.getContentPane().add(comboBoxExport, gbc_comboBoxExport);
		
		JButton btnExportResults = new JButton("Export Results");
		btnExportResults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(attributeList.isSelectionEmpty()||fileTypesList.isSelectionEmpty()){
					JOptionPane.showMessageDialog(null,"Please use find content and filter first.");
				}else{
				
				String extension="";
				try {
					int selectedExportType = comboBoxExport.getSelectedIndex();
					 if(selectedExportType==0){
						extension = ".csv";
				//Reformat to CSV and print to textArea as setText
					//***START***		
						 //JFile Chooser for selecting the folder
						JFileChooser chooser = new JFileChooser();
						
						FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV", "csv");
						chooser.setFileFilter(filter);
						int returnVal = chooser.showSaveDialog(chooser);
						
						if(returnVal == JFileChooser.APPROVE_OPTION) {
						   String path =chooser.getSelectedFile().getAbsolutePath();   
						   BufferedWriter out = new BufferedWriter(new FileWriter(path+extension));
						  
						
						//Sets from until dates to filter
						GregorianCalendar gcFrom = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
						GregorianCalendar gcUntil = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
					    gcFrom.clear();
					    gcUntil.clear();
					    //Format (year, month, date, hourOfDay, minute)
					    gcFrom.set(Integer.parseInt(comboBoxFromYear.getSelectedItem().toString()), comboBoxFromMonth.getSelectedIndex(), comboBoxFromDay.getSelectedIndex(), comboBox.getSelectedIndex(), 0);
					    gcUntil.set(Integer.parseInt(comboBoxToYear.getSelectedItem().toString()), comboBoxToMonth.getSelectedIndex(), comboBoxToDay.getSelectedIndex(), comboBox_1.getSelectedIndex(), 0);
					    
					    //Sets from until dates
					    long utcTimeFrom = gcFrom.getTimeInMillis();
					    long utcTimeUntil = gcUntil.getTimeInMillis();
					    //done setting from until dates
					    
					    //Print out time
					    
						//Initialize local hashmaps
						 HashMap<String,Long> createdTimeUTCGUI = new HashMap<String,Long>();
						 HashMap<String,Long> lastAccTimeUTCGUI = new HashMap<String,Long>();
						 HashMap<String,Long> lastModTimeUTCGUI = new HashMap<String,Long>();
						 				 
						 //UTC times
						 createdTimeUTCGUI=contentFinder.createdTimeUTC;
						 lastAccTimeUTCGUI=contentFinder.lastAccTimeUTC;
						 lastModTimeUTCGUI=contentFinder.lastModTimeUTC;
						 
						  
						// Determine if the item is selected at index()
						 boolean isSe1, isSe2, isSe3, isSe4, isSe5, isSe6, isSe7, isSe8;
						 isSe1 =  attributeList.isSelectedIndex(0);
						 isSe2 =  attributeList.isSelectedIndex(1);
						 isSe3 =  attributeList.isSelectedIndex(2);
						 isSe4 =  attributeList.isSelectedIndex(3);
						 isSe5 =  attributeList.isSelectedIndex(4);
						 isSe6 =  attributeList.isSelectedIndex(5);
						 isSe7 =  attributeList.isSelectedIndex(6);
						 isSe8 =  attributeList.isSelectedIndex(7);
							
						
						// Get the index of all the selected items
						 int[] selectedIndx =  fileTypesList.getSelectedIndices();

						 HashSet<String> fileTypesHS = new HashSet<String>();

			            
						 // Get all the selected items using the indices
						 for (int i=0; i<selectedIndx.length; i++) {
							 fileTypesHS.add(fileTypesList.getModel().getElementAt(selectedIndx[i]));
						 }
						
						 	//Gets time to SORT-By from GUI from user
							int timeAttributeSort = comboBoxTime.getSelectedIndex();
							
							//Prints out titles
							if(isSe1){
					        	  out.write("Filename, ");
					          }
					          if(isSe2){
					        	  out.write("File type, ");
					          }
					          if(isSe3){
					        	  out.write("File path, ");
					          }
					          if(isSe4){
					        	  out.write("File size, ");	
					          }
					          if(isSe5){
					        	  out.write("Created time, ");
					          }
					          if(isSe6){
					        	  out.write("Last accessed time, ");
					          }
					          if(isSe7){
					        	  out.write("Last modified time, ");
						       }
					          if(isSe8){
					        	  out.write("Hidden file, ");
					          }
					          out.write("\n");
					          
								if(timeAttributeSort==0){
									//Sorts by created time
									createdTimeUTCGUI=sortHashMap(createdTimeUTCGUI);	
									
									//Prints out metadata 
									for (String key : createdTimeUTCGUI.keySet()) {
									  if(fileTypesHS.contains(contentFinder.hmFileExt.get(key))){
										if((contentFinder.createdTimeUTC.get(key)>=utcTimeFrom)&&(contentFinder.createdTimeUTC.get(key)<=utcTimeUntil)){
								          if(isSe1){
								        	  out.write(key+", ");
								          }
								          if(isSe2){
								        	  out.write(contentFinder.hmFileExt.get(key)+", ");
								          }
								          if(isSe3){
								        	  out.write(contentFinder.filePath.get(key)+", ");
								          }
								          if(isSe4){
								        	  out.write(contentFinder.logicalSize.get(key)+"bytes"+", ");	
								          }
								          if(isSe5){
								        	  out.write(contentFinder.createdTime.get(key)+", ");
								          }
								          if(isSe6){
								        	  out.write(contentFinder.lastAccTime.get(key)+", ");
								          }
								          if(isSe7){
								        	  out.write(contentFinder.lastModTime.get(key)+", ");
									       }
								          if(isSe8){
								        	  out.write(contentFinder.hiddenFile.get(key)+", ");
								          }
								          out.write("\n");
								        }//end dates between if statement
						
									  }//end file types verification
									}//end for loop	
								}else if(timeAttributeSort==1){
									
									//Sorts by last access time
									lastAccTimeUTCGUI=sortHashMap(lastAccTimeUTCGUI);	
									
									//Prints out metadata
									for (String key : lastAccTimeUTCGUI.keySet()) {
									  if(fileTypesHS.contains(contentFinder.hmFileExt.get(key))){
										if((contentFinder.lastAccTimeUTC.get(key)>=utcTimeFrom)&&(contentFinder.lastAccTimeUTC.get(key)<=utcTimeUntil)){
											if(isSe1){
									        	  out.write(key+", ");
									          }
									          if(isSe2){
									        	  out.write(contentFinder.hmFileExt.get(key)+", ");
									          }
									          if(isSe3){
									        	  out.write(contentFinder.filePath.get(key)+", ");
									          }
									          if(isSe4){
									        	  out.write(contentFinder.logicalSize.get(key)+"bytes"+", ");	
									          }
									          if(isSe5){
									        	  out.write(contentFinder.createdTime.get(key)+", ");
									          }
									          if(isSe6){
									        	  out.write(contentFinder.lastAccTime.get(key)+", ");
									          }
									          if(isSe7){
									        	  out.write(contentFinder.lastModTime.get(key)+", ");
										       }
									          if(isSe8){
									        	  out.write(contentFinder.hiddenFile.get(key)+", ");
									          }
										          out.write("\n");
										}//end dates between if statement
									  }//end file types verification
								     }//end for loop	
								}else if(timeAttributeSort==2){
									
									//Sorts by last modified time
									lastModTimeUTCGUI=sortHashMap(lastModTimeUTCGUI);
									
									//Prints out metadata
									for (String key : lastModTimeUTCGUI.keySet()) {
									  if(fileTypesHS.contains(contentFinder.hmFileExt.get(key))){	
										  if((contentFinder.lastModTimeUTC.get(key)>=utcTimeFrom)&&(contentFinder.lastModTimeUTC.get(key)<=utcTimeUntil)){
											  if(isSe1){
									        	  out.write(key+", ");
									          }
									          if(isSe2){
									        	  out.write(contentFinder.hmFileExt.get(key)+", ");
									          }
									          if(isSe3){
									        	  out.write(contentFinder.filePath.get(key)+", ");
									          }
									          if(isSe4){
									        	  out.write(contentFinder.logicalSize.get(key)+"bytes"+", ");	
									          }
									          if(isSe5){
									        	  out.write(contentFinder.createdTime.get(key)+", ");
									          }
									          if(isSe6){
									        	  out.write(contentFinder.lastAccTime.get(key)+", ");
									          }
									          if(isSe7){
									        	  out.write(contentFinder.lastModTime.get(key)+", ");
										       }
									          if(isSe8){
									        	  out.write(contentFinder.hiddenFile.get(key)+", ");
									          }
										          out.write("\n");
										}//end dates between if statement
									  }//end file types verification
								     }//end for loop	
								}//end sort by time if													
								 out.close();
								 System.out.println("\n\t***Succesfully copied***");
							}
						//END
						
					}else{
						extension = ".txt";
						//JFile Chooser for selecting the folder
						JFileChooser chooser = new JFileChooser();
						
						FileNameExtensionFilter filter = new FileNameExtensionFilter("Formated Report txt", "txt");
						chooser.setFileFilter(filter);
						int returnVal = chooser.showSaveDialog(chooser);
						
						if(returnVal == JFileChooser.APPROVE_OPTION) {
						   String path =chooser.getSelectedFile().getAbsolutePath();   
						   BufferedWriter out = new BufferedWriter(new FileWriter(path+extension));
						   out.write(txtrWinfmtCopyright.getText());
						   out.close();
						   System.out.println("\n\t***Succesfully copied***");
						}
					}
					
					  
				} catch (IOException e1) {
					System.out.println("Failed to export!");
				}
				
				}//End of filter used verification
				
				
			}
		});
		
		
		
		btnNewButton = new JButton("Filter results");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(attributeList.isSelectionEmpty()||fileTypesList.isSelectionEmpty() || (comboBoxToYear.getSelectedIndex()==0 && comboBoxToMonth.getSelectedIndex()==0 && comboBoxToDay.getSelectedIndex()==0)){
					JOptionPane.showMessageDialog(null,"Either the date range has not been selected or no file type and file attribute are included.");
				}else{
				
				txtrWinfmtCopyright.setText("");			
				System.out.println("Program Started");
				System.out.println("====================");
			
				//Sets from until dates to filter
				GregorianCalendar gcFrom = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
				GregorianCalendar gcUntil = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
			    gcFrom.clear();
			    gcUntil.clear();
			    //Format (year, month, date, hourOfDay, minute)
			    gcFrom.set(Integer.parseInt(comboBoxFromYear.getSelectedItem().toString()), comboBoxFromMonth.getSelectedIndex(), comboBoxFromDay.getSelectedIndex(), comboBox.getSelectedIndex(), 0);
			    gcUntil.set(Integer.parseInt(comboBoxToYear.getSelectedItem().toString()), comboBoxToMonth.getSelectedIndex(), comboBoxToDay.getSelectedIndex(), comboBox_1.getSelectedIndex(), 0);
			    
			    //Sets from until dates
			    long utcTimeFrom = gcFrom.getTimeInMillis();
			    long utcTimeUntil = gcUntil.getTimeInMillis();
			    //done setting from until dates
			    
			    //Print out time
			    
				//Initialize local hashmaps
				 HashMap<String,Long> createdTimeUTCGUI = new HashMap<String,Long>();
				 HashMap<String,Long> lastAccTimeUTCGUI = new HashMap<String,Long>();
				 HashMap<String,Long> lastModTimeUTCGUI = new HashMap<String,Long>();
				 				 
				 //UTC times
				 createdTimeUTCGUI=contentFinder.createdTimeUTC;
				 lastAccTimeUTCGUI=contentFinder.lastAccTimeUTC;
				 lastModTimeUTCGUI=contentFinder.lastModTimeUTC;
				 
				  
				// Determine if the item is selected at index()
				 boolean isSe1, isSe2, isSe3, isSe4, isSe5, isSe6, isSe7, isSe8;
				 isSe1 =  attributeList.isSelectedIndex(0);
				 isSe2 =  attributeList.isSelectedIndex(1);
				 isSe3 =  attributeList.isSelectedIndex(2);
				 isSe4 =  attributeList.isSelectedIndex(3);
				 isSe5 =  attributeList.isSelectedIndex(4);
				 isSe6 =  attributeList.isSelectedIndex(5);
				 isSe7 =  attributeList.isSelectedIndex(6);
				 isSe8 =  attributeList.isSelectedIndex(7);
					
				
				// Get the index of all the selected items
				 int[] selectedIndx =  fileTypesList.getSelectedIndices();

				 HashSet<String> fileTypesHS = new HashSet<String>();

	            
				 // Get all the selected items using the indices
				 for (int i=0; i<selectedIndx.length; i++) {
					 fileTypesHS.add(fileTypesList.getModel().getElementAt(selectedIndx[i]));
				 }
				
				 	//Gets time to SORT-By from GUI from user
					int timeAttributeSort = comboBoxTime.getSelectedIndex();
					
						if(timeAttributeSort==0){
							//Sorts by created time
							createdTimeUTCGUI=sortHashMap(createdTimeUTCGUI);	
							
							//Prints out metadata 
							for (String key : createdTimeUTCGUI.keySet()) {
							  if(fileTypesHS.contains(contentFinder.hmFileExt.get(key))){
								if((contentFinder.createdTimeUTC.get(key)>=utcTimeFrom)&&(contentFinder.createdTimeUTC.get(key)<=utcTimeUntil)){
						          if(isSe1){
								   System.out.println("Filename: "+key);
						          }
						          if(isSe2){
						           System.out.println("File extension: "+contentFinder.hmFileExt.get(key));
						          }
						          if(isSe3){
						           System.out.println("File Path: "+contentFinder.filePath.get(key));
						          }
						          if(isSe4){
						           System.out.println("Logical Size: "+contentFinder.logicalSize.get(key)+"bytes");	
						          }
						          if(isSe5){
						           System.out.println("File creation time: "+contentFinder.createdTime.get(key));
						          }
						          if(isSe6){
							       System.out.println("Last accessed time:" +contentFinder.lastAccTime.get(key));
						          }
						          if(isSe7){
						           System.out.println("Last Modified time: "+contentFinder.lastModTime.get(key));
							       }
						          if(isSe8){
						           System.out.println("Hidden file: "+contentFinder.hiddenFile.get(key));
						          }
						           System.out.println("\n");
						        }//end dates between if statement
				
							  }//end file types verification
							}//end for loop	
						}else if(timeAttributeSort==1){
							
							//Sorts by last access time
							lastAccTimeUTCGUI=sortHashMap(lastAccTimeUTCGUI);	
							
							//Prints out metadata
							for (String key : lastAccTimeUTCGUI.keySet()) {
							  if(fileTypesHS.contains(contentFinder.hmFileExt.get(key))){
								if((contentFinder.lastAccTimeUTC.get(key)>=utcTimeFrom)&&(contentFinder.lastAccTimeUTC.get(key)<=utcTimeUntil)){
										  if(isSe1){
										   System.out.println("Filename: "+key);
								          }
								          if(isSe2){
								           System.out.println("File extension: "+contentFinder.hmFileExt.get(key));
								          }
								          if(isSe3){
								           System.out.println("File Path: "+contentFinder.filePath.get(key));
								          }
								          if(isSe4){
								           System.out.println("Logical Size: "+contentFinder.logicalSize.get(key)+"bytes");	
								          }
								          if(isSe5){
								           System.out.println("File creation time: "+contentFinder.createdTime.get(key));
								          }
								          if(isSe6){
									       System.out.println("Last accessed time:" +contentFinder.lastAccTime.get(key));
								          }
								          if(isSe7){
								           System.out.println("Last Modified time: "+contentFinder.lastModTime.get(key));
									       }
								          if(isSe8){
								           System.out.println("Hidden file: "+contentFinder.hiddenFile.get(key));
								          }
								          System.out.println("\n");
								}//end dates between if statement
							  }//end file types verification
						     }//end for loop	
						}else if(timeAttributeSort==2){
							
							//Sorts by last modified time
							lastModTimeUTCGUI=sortHashMap(lastModTimeUTCGUI);
							
							//Prints out metadata
							for (String key : lastModTimeUTCGUI.keySet()) {
							  if(fileTypesHS.contains(contentFinder.hmFileExt.get(key))){	
								  if((contentFinder.lastModTimeUTC.get(key)>=utcTimeFrom)&&(contentFinder.lastModTimeUTC.get(key)<=utcTimeUntil)){
									      if(isSe1){
										   System.out.println("Filename: "+key);
								          }
								          if(isSe2){
								           System.out.println("File extension: "+contentFinder.hmFileExt.get(key));
								          }
								          if(isSe3){
								           System.out.println("File Path: "+contentFinder.filePath.get(key));
								          }
								          if(isSe4){
								           System.out.println("Logical Size: "+contentFinder.logicalSize.get(key)+"bytes");	
								          }
								          if(isSe5){
								           System.out.println("File creation time: "+contentFinder.createdTime.get(key));
								          }
								          if(isSe6){
									       System.out.println("Last accessed time:" +contentFinder.lastAccTime.get(key));
								          }
								          if(isSe7){
								           System.out.println("Last Modified time: "+contentFinder.lastModTime.get(key));
									       }
								          if(isSe8){
								           System.out.println("Hidden file: "+contentFinder.hiddenFile.get(key));
								          }
								          System.out.println("\n");
								}//end dates between if statement
							  }//end file types verification
						     }//end for loop	
						}//end sort by time if
						 System.out.println("\n\t***Finished***");
						 
				}//End if verifying a selection of attributes and file types to display	 
						 
			}//End of Filter button method		
			
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 2;
		frmFileMetadataTimeline.getContentPane().add(btnNewButton, gbc_btnNewButton);
		
		JLabel lblFromHour = new JLabel("From:");
		GridBagConstraints gbc_lblFromHour = new GridBagConstraints();
		gbc_lblFromHour.insets = new Insets(0, 0, 5, 5);
		gbc_lblFromHour.gridx = 1;
		gbc_lblFromHour.gridy = 2;
		frmFileMetadataTimeline.getContentPane().add(lblFromHour, gbc_lblFromHour);
		
		JLabel lblHour = new JLabel("Hour");
		GridBagConstraints gbc_lblHour = new GridBagConstraints();
		gbc_lblHour.anchor = GridBagConstraints.EAST;
		gbc_lblHour.insets = new Insets(0, 0, 5, 5);
		gbc_lblHour.gridx = 2;
		gbc_lblHour.gridy = 2;
		frmFileMetadataTimeline.getContentPane().add(lblHour, gbc_lblHour);
		
		comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 3;
		gbc_comboBox.gridy = 2;
		frmFileMetadataTimeline.getContentPane().add(comboBox, gbc_comboBox);
		
		JLabel lblTo_1 = new JLabel("To:");
		GridBagConstraints gbc_lblTo_1 = new GridBagConstraints();
		gbc_lblTo_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblTo_1.gridx = 5;
		gbc_lblTo_1.gridy = 2;
		frmFileMetadataTimeline.getContentPane().add(lblTo_1, gbc_lblTo_1);
		
		JLabel lblHour_1 = new JLabel("Hour");
		GridBagConstraints gbc_lblHour_1 = new GridBagConstraints();
		gbc_lblHour_1.anchor = GridBagConstraints.EAST;
		gbc_lblHour_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblHour_1.gridx = 6;
		gbc_lblHour_1.gridy = 2;
		frmFileMetadataTimeline.getContentPane().add(lblHour_1, gbc_lblHour_1);
		
		comboBox_1 = new JComboBox<String>();
		comboBox_1.setModel(new DefaultComboBoxModel<String>(new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_1.gridx = 7;
		gbc_comboBox_1.gridy = 2;
		frmFileMetadataTimeline.getContentPane().add(comboBox_1, gbc_comboBox_1);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
			}
		});
		
		JButton btnDetectFiletypes = new JButton("Detect file types to include");
		btnDetectFiletypes.addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			private int x = 0;

			public void actionPerformed(ActionEvent arg0) {
				
				ArrayList<String> listItems = new ArrayList<String>();
		
				for(String key : contentFinder.hmFileExt.keySet()){
					listItems.add(contentFinder.hmFileExt.get(key));
					x++;
				}
				HashSet<String> hs = new HashSet<String>();
				//eliminates duplicates by using hashSet
				hs.addAll(listItems);
				listItems.clear();
				listItems.addAll(hs);
				
				//Puts types into a string array
				String[] typeList = new String[hs.size()];
				typeList = hs.toArray(typeList);
				//Finally places items into type list on GUI
				fileTypesList.setListData(typeList);
				if(fileTypesList.getModel().getSize() == 0){
					System.out.println("No file types found or no drives/directories selected.");
				}
			}
		});
		GridBagConstraints gbc_btnDetectFiletypes = new GridBagConstraints();
		gbc_btnDetectFiletypes.insets = new Insets(0, 0, 5, 5);
		gbc_btnDetectFiletypes.gridx = 8;
		gbc_btnDetectFiletypes.gridy = 2;
		frmFileMetadataTimeline.getContentPane().add(btnDetectFiletypes, gbc_btnDetectFiletypes);
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_2.gridx = 9;
		gbc_scrollPane_2.gridy = 2;
		frmFileMetadataTimeline.getContentPane().add(scrollPane_2, gbc_scrollPane_2);
		
		fileTypesList = new JList<String>();
		fileTypesList.setToolTipText("Press detect file types to populate list. \r\nHold Ctrl button down to select multiple items\r\n or use Shift and arrow keys.");
		scrollPane_2.setViewportView(fileTypesList);
		GridBagConstraints gbc_btnExportResults = new GridBagConstraints();
		gbc_btnExportResults.gridwidth = 6;
		gbc_btnExportResults.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnExportResults.insets = new Insets(0, 0, 5, 5);
		gbc_btnExportResults.gridx = 10;
		gbc_btnExportResults.gridy = 2;
		frmFileMetadataTimeline.getContentPane().add(btnExportResults, gbc_btnExportResults);
		
		JList<String> list = new JList<String>();
		list.setLayoutOrientation(JList.VERTICAL_WRAP);
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.insets = new Insets(0, 0, 5, 5);
		gbc_list.fill = GridBagConstraints.HORIZONTAL;
		gbc_list.gridx = 9;
		gbc_list.gridy = 3;
		frmFileMetadataTimeline.getContentPane().add(list, gbc_list);
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 6;
		gbc_scrollPane.gridwidth = 16;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 4;
		frmFileMetadataTimeline.getContentPane().add(scrollPane, gbc_scrollPane);
		txtrWinfmtCopyright.setText("WinFMT, Copyright 2012, Raymond C. Borges Hink\r\nThis program comes with ABSOLUTELY NO WARRANTY.\r\nThis is free software, and you are welcome to redistribute it\r\nunder certain conditions; see COPYING for details.");
		txtrWinfmtCopyright.setRows(1);
		txtrWinfmtCopyright.setEditable(false);
		txtrWinfmtCopyright.setWrapStyleWord(true);
		scrollPane.setViewportView(txtrWinfmtCopyright);
		//Sets text area to not have a horizontal scroll bar
		txtrWinfmtCopyright.setLineWrap(true);
		
		
	}
	
	private void updateTextArea(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				txtrWinfmtCopyright.append(text);
			}
		});
	}
	
	private void redirectSystemStreams() {
		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				updateTextArea(String.valueOf((char) b));
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				updateTextArea(new String(b, off, len));
			}

			@Override
			public void write(byte[] b) throws IOException {
				write(b, 0, b.length);
			}
		};

		System.setOut(new PrintStream(out, true));
		System.setErr(new PrintStream(out, true));
	}
	
}
