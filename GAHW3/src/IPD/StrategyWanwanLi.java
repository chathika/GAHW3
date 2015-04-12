

class StrategyWanwanLi extends Strategy
{
  double[][] weights;
  double[] bias;
  int[] opponentDecisions;
  int[] myDecisions;
  int time=0;
  int size=6;
  int historyTimes=size/2;
  public StrategyWanwanLi()
  {
	this.name="Wanwan Li";
	this.weights=new double[][]
	{
	        {0.07135352206879297,   0.4029602955910142,     0.17577933009447944,    0.31598513629587843,    0.06598611399925924,    -0.1676521483249912},
	        {0.41568552455718755,   0.04962884282144864,    -0.24412382600000193,   0.33624911922803125,    -0.05468132319905106,   -0.081298460049673},
	        {0.14751619410481953,   0.15001843926674374,    -0.1032281236923357,    -0.4880945761304708,    0.477774512415852,      0.013264675728338449},
	        {0.2775853249011503,    0.3812341466428907,     0.28590054376398444,    0.2514803109281749,     0.19971182066207127,    0.4220298264632425},
	        {-0.04673364047554396,  0.3747612701802494,     0.15545712084374252,    0.4417771247682667,     0.22698830822494465,    -0.28315844699692116},
	        {-0.26797243833676554,  -0.3828723890832716,    -0.12263857513016063,   -0.31448225964515264,   -0.31926877871892023,   0.10212444973370893}
	};
	this.bias=new double[]
	{
	        0.9020394207878325,             0.5123204398266488,             0.9062711563426495,             0.9476619234705961,             0.5512484124275285,             0.7991019590940887
	};
	this.opponentDecisions=new int[historyTimes];
	this.myDecisions=new int[historyTimes];
  }
  private int makeSimpleDecision()
  {
	return 0;
  }
  private int makeNeuralNetworkDecision()
  {
	int c=0;
	double[] input= new double[size];				// the input values of Neural Networks are make up of the decision history
	for(int i=historyTimes-1;i>=0;i--)
	{
		input[c++]=this.signal(this.opponentDecisions[i]);
		input[c++]=this.signal(this.myDecisions[i]);
	}
	double[] output=new double[size];			// the output values are calculated from the weighted accumulation of inputs
	for (int i=0; i<size; i++)
	{ 
		output[i]=0.0;
		for (int j=0; j<size; j++)
		{
			output[i]+=this.weights[i][j]*input[j];
		}
	}
	double result=0.0;
	for (int i=0; i<size; i++)				// the result value are calculated from the biased accumulation of outputs
	{ 
		result+=output[i]*this.bias[i];
	}
	int myDecision=result>0.0?1:0;
	return myDecision;
  }
  private double signal(int x)
  {
	return x==0?-1.0:1.0;
  }
  public int nextMove()
  {
	int myDecision=0;
	if(time<historyTimes)myDecision=this.makeSimpleDecision();
	else myDecision=this.makeNeuralNetworkDecision();
	return myDecision;
  }
  public void saveMyMove(int move)
  {
	myLastMove = move;
	if(time<historyTimes)
	{
		this.myDecisions[time]=move;
	}
	else 
	{
		int i=0;
		for(;i<historyTimes-1;i++)
		{
			this.myDecisions[i]=this.myDecisions[i+1];
		}
		this.myDecisions[i]=move;
	}
  }
  public void saveOpponentMove(int move)
  { 
	opponentLastMove = move;
	if(time<historyTimes)
	{
		this.opponentDecisions[time]=move;
	}
	else 
	{
		int i=0;
		for(;i<historyTimes-1;i++)
		{
			this.opponentDecisions[i]=this.opponentDecisions[i+1];
		}
		this.opponentDecisions[i]=move;
	}
	this.time++;
  }
}
