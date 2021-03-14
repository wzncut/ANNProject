package ann;

import util.ConsoleHelper;
import util.DataUtil;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * 
 * @author wangzhe
 * 
 */
public class MainClass
{
	public static void main(String[] args) throws Exception
	{
//		if (args.length < 5)
//		{
//			System.out
//					.println("Usage: \n\t-train trainfile\n\t-test predictfile\n\t-sep separator, default:','\n\t-eta eta, default:0.5\n\t-iter iternum, default:5000\n\t-out outputfile");
//			return;
//		}
		ConsoleHelper helper = new ConsoleHelper(args);
		String trainfile = helper.getArg("-train", "src/main/resources/ANNtrain.txt");
		String testfile = helper.getArg("-test", "src/main/resources/ANNtest.txt");
		String separator = helper.getArg("-sep", " ");
		String outputfile = helper.getArg("-out", "src/main/resources/ANNoutput.txt");
		float eta = helper.getArg("-eta", 0.02f);
		int nIter = helper.getArg("-iter", 1000);
		DataUtil util = DataUtil.getInstance();
		List<DataNode> trainList = util.getDataList(trainfile, separator);
		List<DataNode> testList = util.getDataList(testfile, separator);
		BufferedWriter output = new BufferedWriter(new FileWriter(new File(
				outputfile)));
		int typeCount = util.getTypeCount();
		AnnClassifier annClassifier = new AnnClassifier(trainList.get(0)
				.getAttribList().size(), new Double(Math.floor(Math.sqrt(trainList.get(0).getAttribList()
				.size() + typeCount))).intValue()+8, typeCount);
		annClassifier.setTrainNodes(trainList);
		annClassifier.train(eta, nIter);
		for (int i = 0; i < testList.size(); i++)
		{
			DataNode test = testList.get(i);
			int type = annClassifier.test(test);
			List<Float> attribs = test.getAttribList();
			for (int n = 0; n < attribs.size(); n++)
			{
				output.write(attribs.get(n) + ",");
				output.flush();
			}
			output.write(util.getTypeName(type) + "\n");
			output.flush();
		}
		output.close();

		//统计准确度
		float result=util.getResultList("src/main/resources/ANNoutput.txt","src/main/resources/ANNtest.txt"," ");
		System.out.println("准确度为:"+result*100+"%");
	}

}
