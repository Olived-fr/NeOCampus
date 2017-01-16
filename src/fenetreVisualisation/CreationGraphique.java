package fenetreVisualisation;

import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset; 
import org.jfree.ui.RefineryUtilities; 
import org.jfree.chart.plot.XYPlot; 
import org.jfree.chart.ChartFactory; 
import org.jfree.data.xy.XYSeriesCollection; 
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.JCommon;

public class CreationGraphique extends JFrame{

	public CreationGraphique( String applicationTitle, String chartTitle, String nomCapteurs, List<ValeursTempsCapteurs> liste )
		   {
		      super(applicationTitle);
		      final TimeSeries capteurs = new TimeSeries( "capteur " + nomCapteurs );
				for(Iterator<ValeursTempsCapteurs> iter = liste.iterator(); iter.hasNext();){
					ValeursTempsCapteurs tmp = iter.next();
					Calendar c = Calendar.getInstance(); 
					c.setTime(tmp.getTemps());
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH);
					int day = c.get(Calendar.DAY_OF_MONTH);
					
					capteurs.add( new Second(tmp.getSecondes(), tmp.getMinutes(), tmp.getHeures(), day, month+1, year),(float)tmp.getValeurs());
				}
				
				TimeSeriesCollection dataset = new TimeSeriesCollection();
				dataset.addSeries(capteurs);
		      
		      JFreeChart xylineChart = ChartFactory.createTimeSeriesChart(
		         chartTitle ,
		         "Date/heure envoie de la valeur" ,
		         "Valeur" ,
		         dataset ,
		         true ,true, false);
		         
		      ChartPanel chartPanel = new ChartPanel( xylineChart );
		      final XYPlot plot = xylineChart.getXYPlot( );
		      
		      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
		      renderer.setSeriesPaint( 0 , Color.RED );
		      renderer.setSeriesStroke( 0 , new BasicStroke( 2.0f ) );
		      plot.setRenderer( renderer ); 
		      setContentPane( chartPanel ); 
		   }
		// TODO Auto-generated constructor stub

	
}

