/*************************************************************************************************************
*  A Visualization Tool For Signals or Data		  Developed by Wanwan Li, UCF
*  Version 1, Feb 1, 2015
**************************************************************************************************************/
import java.awt.*;
class Oscillogram
{
//	public static final int X=1,Y=2;
	private double[] x;
	private double[] y;
	private double baseY;
	private int length,left,top,width,height,row,column;
	private double minX,maxX,minY,maxY,scaleX,scaleY;
	private int edge=70,dX=30,dY=20,lineWidth=2,pointSize=10;
	private int mode=0;
	private Color backgroundColor=Color.black;
	private Color gridColor=Color.white;
	private Color dataColor=Color.green;
	private int charWidth=7;
	private boolean formatPI=false;
	public Oscillogram(double[] x,double[] y,int left,int top,int width,int height,int row,int column)
	{
		this.left=left;
		this.top=top;
		this.width=width;
		this.height=height;
		this.row=row;
		this.column=column;
		this.setCoordinates(x,y);
	}
	public void setCoordinates(double[] x,double[] y)
	{
		this.length=x.length<y.length?x.length:y.length;
		this.x=new double[length];
		this.y=new double[length];
		this.maxX=x[0];
		this.minX=x[0];
		this.maxY=y[0];
		this.minY=y[0];
		for(int i=0;i<length;i++)
		{
			this.x[i]=x[i];
			if(x[i]>maxX)this.maxX=x[i];
			if(x[i]<minX)this.minX=x[i];
			this.y[i]=y[i];
			if(y[i]>maxY)this.maxY=y[i];
			if(y[i]<minY)this.minY=y[i];
		}
		this.scaleX=(0.0+width-2*edge)/(maxX-minX);
		this.scaleY=(0.0+height-2*edge)/(maxY-minY);
		this.baseY=minY;
	}
	private int transformToCoordinateX(double xi)
	{
		return (int)(left+edge+(xi-minX)*scaleX);
	}
	private int transformToCoordinateY(double yi)
	{
		return (int)(top+height-edge-(yi-minY)*scaleY);
	}
	public void setFormat(double PI)
	{
		if(PI==Math.PI)this.formatPI=true;
	}
	private String format(double x)
	{
		if(formatPI)x/=Math.PI;
		String string=x+"";
		String formatString="";
		int i=0,j=0,len=string.length();
		while(i<len&&string.charAt(i)!='.')formatString+=string.charAt(i++);
		for(;j<3&&i<len;j++,i++)formatString+=string.charAt(i);
		boolean hasE=false;
		for(;i<len;i++)
		{
			if(string.charAt(i)=='E'){formatString+="E";hasE=true;continue;};
			if(hasE)formatString+=string.charAt(i);
		}
		if(formatPI)formatString+="PI";
		return formatString;
	}
	private void drawGrid(Graphics g)
	{
		double intervalY=(0.0+height-2*edge)/(row-1);
		double coordinateIntervalY=(maxY-minY)/(row-1);
		for(int i=0;i<row;i++)
		{
			int x0=left+edge;
			int x1=left+width-edge;
			int y=(int)(top+edge+i*intervalY);
			g.drawLine(x0,y,x1,y);
			double coordinateY=maxY-i*coordinateIntervalY;
			String text=format(coordinateY);
			g.drawString(text,x0-charWidth*(text.length()+1),y+2*charWidth/3);
		}
		double intervalX=(0.0+width-2*edge)/(column-1);
		double coordinateIntervalX=(maxX-minX)/(column-1);
		for(int j=0;j<column;j++)
		{
			int x=(int)(left+edge+j*intervalX);
			int y1=top+edge;
			int y2=top+height-edge;
			g.drawLine(x,y1,x,y2);
			double coordinateX=minX+j*coordinateIntervalX;
			String text=format(coordinateX);
			g.drawString(text,x-charWidth*text.length()/2,y2+3*charWidth);
		}
	}
	public void setColors(Color backgroundColor,Color gridColor,Color dataColor)
	{
		this.backgroundColor=backgroundColor;
		this.gridColor=gridColor;
		this.dataColor=dataColor;
	}
	public void setRange(double minX,double maxX)
	{
		this.minX=minX;
		this.maxX=maxX;
		this.scaleX=(0.0+width-2*edge)/(maxX-minX);
	}
	public void setMinMaxLines(double minY,double maxY)
	{
		this.minY=minY;
		this.maxY=maxY;
		this.scaleY=(0.0+height-2*edge)/(maxY-minY);
	}
	public void setBaseLine(double y)
	{
		if(y>minY&&y<maxY)this.baseY=y;
	}
	public void setLineWidth(int lineWidth)
	{
		this.lineWidth=lineWidth;
	}
	public void setPointSize(int pointSize)
	{
		this.pointSize=pointSize;
	}
	public void drawBackground(Graphics g)
	{
		g.setColor(backgroundColor);
		g.fillRect(left,top,width,height);
		g.setColor(gridColor);
		this.drawGrid(g);
	}
	public void drawStems(Graphics g)
	{
		g.setColor(dataColor);
		for(int i=0;i<length;i++)
		{
			int xi=transformToCoordinateX(x[i]);
			int y0=transformToCoordinateY(baseY);
			int y1=transformToCoordinateY(y[i]);
			for(int j=0;j<lineWidth;j++)
			{
				int x=xi+j-lineWidth/2;
				g.drawLine(x,y0,x,y1);
			}
		}
		int x0=transformToCoordinateX(minX);
		int x1=transformToCoordinateX(maxX);
		int yb=transformToCoordinateY(baseY);
		for(int j=0;j<lineWidth;j++)
		{
			int y=yb+j-lineWidth/2;
			g.drawLine(x0,y,x1,y);
		}

	}
	public void drawCurve(Graphics g)
	{
		g.setColor(dataColor);
		for(int i=0;i<length-1;i++)
		{
			int x0=transformToCoordinateX(x[i]);
			int y0=transformToCoordinateY(y[i]);
			int x1=transformToCoordinateX(x[i+1]);
			int y1=transformToCoordinateY(y[i+1]);
			for(int j=0;j<lineWidth;j++)
			{
				int dy=j-lineWidth/2;
				g.drawLine(x0,y0+dy,x1,y1+dy);
			}
		}
	}
	public void drawPoints(Graphics g)
	{
		g.setColor(dataColor);
		for(int i=0;i<length;i++)
		{
			int x=transformToCoordinateX(this.x[i]);
			int y=transformToCoordinateY(this.y[i]);
			g.fillOval(x-pointSize/2,y-pointSize/2,pointSize,pointSize);
		}
	}
	private double[][] X;
	private double[][] Y;
	private int[] dataLengths;
	private boolean[] isColumns;
	private int dataNumber;
	private int maxDataLength;
	private Color[] dataColors;
	private String[] dataLabels;
	private String xLabel,yLabel;
	private int max(int[] x)
	{
		int Max=x[0];
		for(int i=0;i<x.length;i++)if(x[i]>Max)Max=x[i];
		return Max;
	}
	public Oscillogram(double[][] X,double[][] Y,int[] dataLengths,boolean[] isColumns,int left,int top,int width,int height,int row,int column)
	{
		this.left=left;
		this.top=top;
		this.width=width;
		this.height=height;
		this.row=row;
		this.column=column;
		this.setCoordinatesAndAttributes(X,Y,dataLengths,isColumns);
	}
	public void setCoordinatesAndAttributes(double[][] X,double[][] Y,int[] dataLengths,boolean[] isColumns)
	{
		this.dataNumber=(dataLengths.length<=isColumns.length?dataLengths.length:isColumns.length);
		this.maxDataLength=max(dataLengths);
		this.X=new double[dataNumber][maxDataLength];
		this.Y=new double[dataNumber][maxDataLength];
		this.dataLengths=new int[dataNumber];
		this.isColumns=new boolean[dataNumber];
		this.maxX=X[0][0];
		this.minX=X[0][0];
		this.maxY=Y[0][0];
		this.minY=Y[0][0];
		for(int i=0;i<dataNumber;i++)
		{
			this.dataLengths[i]=dataLengths[i];
			this.isColumns[i]=isColumns[i];
			for(int j=0;j<dataLengths[i];j++)
			{
				this.X[i][j]=X[i][j];
				if(X[i][j]>maxX)this.maxX=X[i][j];
				if(X[i][j]<minX)this.minX=X[i][j];
				this.Y[i][j]=Y[i][j];
				if(Y[i][j]>maxY)this.maxY=Y[i][j];
				if(Y[i][j]<minY)this.minY=Y[i][j];
			}
		}
		this.scaleX=(0.0+width-2*edge)/(maxX-minX);
		this.scaleY=(0.0+height-2*edge)/(maxY-minY);
		this.baseY=minY;
	}
	private void drawDatasAndColumns(Graphics g,int i)
	{
		for(int j=0;j<dataLengths[i];j++)
		{
			if(isColumns[i])
			{
				int Xij=transformToCoordinateX(X[i][j]);
				int Y0=transformToCoordinateY(minY);
				int Y1=transformToCoordinateY(Y[i][j]);
				g.drawLine(Xij,Y0,Xij,Y1);
			}
			else
			{
				if(j>=dataLengths[i]-1)continue;
				int X0=transformToCoordinateX(X[i][j]);
				int Y0=transformToCoordinateY(Y[i][j]);
				int X1=transformToCoordinateX(X[i][j+1]);
				int Y1=transformToCoordinateY(Y[i][j+1]);
				for(int k=0;k<lineWidth;k++)
				{
					int dK=k-lineWidth/2;
					g.drawLine(X0+dK,Y0+dK,X1+dK,Y1+dK);
				}
			}
		}
	}
	public void setDataColors(Color[] dataColors)
	{
		this.dataColors=dataColors;
	}
	public void drawMultiCurves(Graphics g)
	{
		for(int i=0;i<dataNumber;i++)
		{
			g.setColor(dataColors[i%dataColors.length]);
			this.drawDatasAndColumns(g,i);
		}
	}
	public void setLabels(String[] dataLabels,String xLabel,String yLabel)
	{
		this.dataColors=dataColors;
		this.dataLabels=dataLabels;
		this.xLabel=xLabel;
		this.yLabel=yLabel;
	}
	public void drawLabels(Graphics g)
	{
		double intervalX=(0.0+width-2*edge)/(column-1);
		int x=left+edge;
		int y=top+edge/2;
		String text=this.yLabel;
		g.setColor(gridColor);
		g.drawString(text,x-charWidth*(text.length()+1),y+2*charWidth/3);

		x=left+width-edge/2;
		y=top+height-edge;
		text=this.xLabel;
		g.drawString(text,x-charWidth*text.length()/2,y+3*charWidth);

		y=top+edge/2;
		x=left+width-edge/2;
		for(int i=dataLabels.length-1;i>=0;i--)
		{
			g.setColor(dataColors[i%dataColors.length]);
			text=dataLabels[i]+" : _______   ";
			x-=(int)(charWidth*text.length()/1.2);
			g.drawString(text,x,y+3*charWidth);
		}
	}
}
