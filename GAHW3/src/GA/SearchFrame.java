/*************************************************************************************************************
*  A Visualization Frame For Ploting Signals or Data			  Developed by Wanwan Li, UCF
*  Version 1, Feb 1, 2015
**************************************************************************************************************/
import java.awt.*;
import java.awt.event.*;
class SearchFrame extends Frame
{
	Oscillogram Oscillogram1;
//	public SearchFrame(double[] generations,double[] aveBestFitness,double[] aveAveFitness,double[] aveStdDeviation)
	public SearchFrame (double[][] X,double[][] Y,int[] dataLengths,boolean[] isColumns,Color[] dataColors,String[] dataLabels,String xLabel,String yLabel)
	{
		int length=1000;
		double[] x=new double[length];
		double[] y=new double[length];
		for(int i=0;i<length;i++){x[i]=i*0.1;y[i]=x[i]*x[i];}
		int left=30,top=50,width=1300,height=700,row=10,column=20;
	//	this.Oscillogram1=new Oscillogram(x,y,left,top,width,height,row,column);
	//	this.Oscillogram1=new Oscillogram(generations,aveBestFitness,left,top,width,height,row,column);
		this.Oscillogram1=new Oscillogram(X,Y,dataLengths,isColumns,left,top,width,height,row,column);
		this.Oscillogram1.setDataColors(dataColors);
		this.Oscillogram1.setLabels(dataLabels,xLabel,yLabel);
	//	this.Oscillogram1.setRange(-2,4);
	//	this.Oscillogram1.setMinMaxLines(50,100);
	//	this.Oscillogram1.setBaseLine(0);
	//	this.Oscillogram1.setPointSize(2);
		this.setBounds(0,0,1380,780);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	public void paint(Graphics g)
	{
		this.Oscillogram1.drawBackground(g);
//		this.Oscillogram1.drawCurve(g);
//		this.Oscillogram1.drawStems(g);
//		this.Oscillogram1.drawPoints(g);
		this.Oscillogram1.drawMultiCurves(g);
		this.Oscillogram1.drawLabels(g);
	}
}

