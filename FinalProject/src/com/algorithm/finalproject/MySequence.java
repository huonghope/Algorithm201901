package com.algorithm.finalproject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MySequence {
	static BufferedWriter bufferedWriter;
	private static final int N = 30000; // Length genome
	private static final int M = 3000; // Reads to map
	private static final int L = 30; // Reads are of length
	int D = 2; // Number of mismatches allowed is 2

	private static String HumanGenome = new String(); // MAX_String 2 147 483 647
	private static String MyGenome = new String(); // 나의 Genome 저장함
	private static String MyGenomeAfter = new String();
	private static final String genome = "AGTC";

	//데이터를 생선
	private static void createData() {
		String Genome = "";
		int rdGenome = 0;
		do {
			Random rand = new Random();
			rdGenome = rand.nextInt(4); 
			Genome += genome.charAt(rdGenome);
			//System.out.println(Genome.length());
			MyGenomeAfter += "0";
		} while (Genome.length() != N);
		HumanGenome = Genome;
		StringBuilder myName = null;
		// 오차 5%로 만들어서 MyGenome에서 저장.
		for (int i = 0; i < (N * 5)/100; i++) {
			Random rand = new Random();
			int indexChange = rand.nextInt(N); 
			rdGenome = rand.nextInt(4);
			myName = new StringBuilder(Genome);
			myName.setCharAt(indexChange, genome.charAt(rdGenome));
		}
		MyGenome = myName.toString();
	}
	// 파일에서 전잘된 String를 저장
	private static void writeSequence(String fileName, String saveGenome) throws IOException {
		File textFile = new File(fileName);
		if (textFile.exists() == false) { // 파일 존재하지 않면 파일 생선하고 저장함
			FileWriter fileWriter = new FileWriter(textFile);
			bufferedWriter = new BufferedWriter(fileWriter);
		} else { // 파일 이미 존재하면 원래 파일의 내용을 지우고 새로 씀
			FileWriter fileWriter = new FileWriter(textFile, false);
			bufferedWriter = new BufferedWriter(fileWriter);
		}
		bufferedWriter.write(saveGenome);
		bufferedWriter.close();
	}

	// 데이블의 리스트 값을 출력함
	private static void saveTableList() throws IOException {
		String strSave[] = new String[] { "List A   Index - Positions \r\n", "List C    Index - Positions \r\n", "List G    Index - Positions \r\n", "List T    Index - Positions \r\n" };
		ArrayList<List<Integer>> listTemp = new ArrayList<List<Integer>>();
		for (int j = 0; j < 4; j++) {
			switch (j) {
			case 0:
				listTemp = listA;
				break;
			case 1:
				listTemp = listC;
				break;
			case 2:
				listTemp = listG;
				break;
			case 3:
				listTemp = listT;
				break;
			}
			for (int i = 0; i < 262143; i++) {
				if (listTemp.get(i).size() != 0) {
					strSave[j] += "      \t" + i + ":  ";
					String temp = "";
					for (int m = 0; m < listTemp.get(i).size(); m++) {
						temp += (listTemp.get(i).get(m) + ",");
					}
					strSave[j] += temp + "\r\n";
				}
			}
			writeSequence("List" + j + ".txt", strSave[j]);
		}
	}

	private static void madeGenome() throws InterruptedException, IOException {

		System.out.println("Human genome and My genome creating...");
		createData();
		writeSequence("HumanGenome.txt", HumanGenome);
		cutAndSaveToTable();
		saveTableList();

		writeSequence("MyGenome.txt", MyGenome);
	

	}
	// 길이 30개로 HumanGenome를 잘라서 Table에서 저장
	private static void cutAndSaveToTable() {
		String saveSequence = "";
		// String HumanGenome 에서 0부터 길이 - 30까지 잘라서 저장함
		for (int i = 0; i < HumanGenome.length() - 10; i++) {
			saveSequence = HumanGenome.substring(i, i + 10);
			saveTable(saveSequence, i);
		}
	}

	//MyGenome에서 길이 30 자른 Sequence 저장 리스트
	private static ArrayList<String> arrMySequence = new ArrayList<>();

	//MyGenome부터 길기 30인 String는 random 위치를 자름
	private static void cutFullSequence(String fileOutput, String strGenome) throws IOException {
		File textFile = new File(fileOutput);
		if (textFile.exists() == false) { // 파일 존재하지 않면 파일 생선하고 저장함
			FileWriter fileWriter = new FileWriter(textFile);
			bufferedWriter = new BufferedWriter(fileWriter);
		} else { // 파일 이미 존재하면 원래 파일의 내용을 지우고 새로 씀
			FileWriter fileWriter = new FileWriter(textFile, false);
			bufferedWriter = new BufferedWriter(fileWriter);
		}
		int countLength = 0; // M를 잘리는 M의 세는 변수
		String write = ""; // 파일에 다가 쓸 String
		do {
			Random rand = new Random();
			int position = rand.nextInt(N - L); // between [0 - 3billion- L] - result random
			write = strGenome.substring(position, position + L);
			arrMySequence.add(write);
			bufferedWriter.write(write + "\r\n");
			write = "";
			countLength++;
		} while (countLength != M);
		bufferedWriter.close();
	}

	//Hash의 값을 만드는 함수
	private static int getHashValue(String strSequence) {
		int result = 0;
		String strHashValue = covertSequence(strSequence);
		int intSequence = Integer.parseInt(strHashValue);
		int length = String.valueOf(intSequence).length();

		int IntegerAt[] = new int[9];
		for (int i = 9, j = 0; i > 0; i--, j++) {
			IntegerAt[j] = Integer.parseInt(String.valueOf(strHashValue.charAt(i)));
		}

		int childSum = 0;
		switch (length) {
		case 1:
			result = intSequence;
			break;
		case 2:
			result = (intSequence - ((IntegerAt[1] * 2) - IntegerAt[0])) / 2;
			break;
		case 3:
			childSum = IntegerAt[2] * 36 - IntegerAt[1] * 6 - IntegerAt[0] * 3;
			result = (intSequence - childSum) / 4;
			break;
		case 4:
			childSum = IntegerAt[3] * 232 - IntegerAt[2] * 92 - IntegerAt[1] * 38 - IntegerAt[0] * 11;
			result = (intSequence - childSum) / 12;
			break;
		case 5:
			childSum = IntegerAt[4] * 4880 - IntegerAt[3] * 280 - IntegerAt[2] * 220 - IntegerAt[1] * 70
					- IntegerAt[0] * 19;
			result = (intSequence - childSum) / 20;
			break;
		case 6:
			childSum = IntegerAt[5] * 59040 - IntegerAt[4] * 240 - IntegerAt[3] * 1560 - IntegerAt[2] * 540
					- IntegerAt[1] * 150 - IntegerAt[0] * 39;
			result = (intSequence - childSum) / 40;
			break;
		case 7:
			childSum = IntegerAt[6] * 590400 - IntegerAt[5] * 2400 - IntegerAt[4] * 15600 - IntegerAt[3] * 5400
					- IntegerAt[2] * 1500 - IntegerAt[1] * 390 - IntegerAt[0] * 99;
			result = (intSequence - childSum) / 100;
			break;
		case 8:
			childSum = IntegerAt[7] * 5904000 - IntegerAt[6] * 24000 - IntegerAt[5] * 156000 - IntegerAt[4] * 54000
					- IntegerAt[3] * 15000 - IntegerAt[2] * 3900 - IntegerAt[1] * 990 - IntegerAt[0] * 249;
			result = (intSequence - childSum) / 250;
			break;
		case 9:
			childSum = IntegerAt[8] * 59367680 - IntegerAt[7] * 158080 - IntegerAt[6] * 1539520 - IntegerAt[5] * 534880
					- IntegerAt[4] * 148720 - IntegerAt[3] * 38680 - IntegerAt[2] * 9820 - IntegerAt[1] * 2470
					- IntegerAt[0] * 619;
			result = (intSequence - childSum) / 620;
			break;
		default:
			System.out.println("Error");
			break;
		}
		return result;
	}
	//해당하는 Sequence 표시후에 Hash 값을 만듦
	private static String covertSequence(String sequence) {
		String result = "";
		char firstChar = sequence.charAt(0);

		String value[];
		switch (firstChar) {
		case 'A':
			value = new String[] { "0", "1", "2", "3" };// A로 시작하는 String
			break;
		case 'C':
			value = new String[] { "1", "0", "2", "3" };// C로 시작하는 String
			break;
		case 'G':
			value = new String[] { "1", "2", "0", "3" };// G로 시작하는 String
			break;
		default: 
			value = new String[] { "1", "2", "3", "0" };
			break;
		}
		for (int i = 0; i < sequence.length(); i++) {
			switch (sequence.charAt(i)) {
			case 'A':
				result += value[0];
				break;
			case 'C':
				result += value[1];
				break;
			case 'G':
				result += value[2];
				break;
			default:
				result += value[3];
				break;
			}
		}
		return result;
	}

	//첫번째 문자 기준으로 테이블에서 4개 리스트로 나눠서 저장함
	private static ArrayList<List<Integer>> listA = new ArrayList<List<Integer>>();
	private static ArrayList<List<Integer>> listC = new ArrayList<List<Integer>>();
	private static ArrayList<List<Integer>> listG = new ArrayList<List<Integer>>();
	private static ArrayList<List<Integer>> listT = new ArrayList<List<Integer>>();

	//태아불 리스트 생선
	static void madeTabelNull() {
		for (int i = 0; i <= 262143; i++) {
			listA.add(new ArrayList<Integer>());
			listC.add(new ArrayList<Integer>());
			listG.add(new ArrayList<Integer>());
			listT.add(new ArrayList<Integer>());
		}
	}
	//첫번째 문자 기준으로 어떤 리스트에서 저장하든가?
	public static void saveTable(String sequence, int indexCut) {
		
			int indexList = getHashValue(sequence);
			switch (sequence.charAt(0))
			{
			case 'A':
					listA.get(indexList).add(indexCut);
				break;
			case 'C':
					listC.get(indexList).add(indexCut);
				break;
			case 'G':
					listG.get(indexList).add(indexCut);
				break;
			case 'T':
					listT.get(indexList).add(indexCut);
				break;
			}
	}

	//테이블이랑 데이터를 준비한 후에 다시 조합하는 함수
	public static void reConstruct() throws IOException {
		
		for (int i = 0; i < M; i++) {
			String sequence = arrMySequence.get(i);

			String[] arrSequence = new String[3];
			arrSequence[0] = sequence.substring(0, 10);
			arrSequence[1] = sequence.substring(10, 20);
			arrSequence[2] = sequence.substring(20, 30);

			for (int j = 0; j < 3; j++) {
  				int index = getHashValue(arrSequence[j]);
				switch (arrSequence[j].charAt(0)) {
				case 'A':
					if (listA.get(index).size() == 0) {
						break;
					} else {
						childReConstruct(listA, arrSequence,index,j,sequence);
					}
					break;
				case 'C':
					if (listC.get(index).size() == 0) 
					{
						break;
					} else {
						childReConstruct(listC, arrSequence,index,j,sequence);
					}
					break;
				case 'G':
					if (listG.get(index).size() == 0) 
					{
						break;
					} else {
						childReConstruct(listG, arrSequence,index,j,sequence);
					}
				case 'T':
					if (listT.get(index).size() == 0) {
						break;
					} else {
						childReConstruct(listT, arrSequence,index,j,sequence);
					}
				}
			}
		}
		writeSequence("MyGenomeAfter.txt", MyGenomeAfter);
	}
	private static void childReConstruct(ArrayList<List<Integer>> list, String[] arrSequence,int index ,int j,String sequence) {
		for (int k = 0; k < list.get(index).size(); k++) {
			int position = list.get(index).get(k);
			//perform의 위치에 따른 가져온 Sequence 달라짐
			switch (j) {
			case 0:
				// HumanGenome에서 String를 가져옴 L2 - L3의 값을 얻는다
				String Sequence = HumanGenome.substring(position + 10,position + 30);
				int mis = findMisMatches(Sequence,arrSequence[1] + arrSequence[2]);
				if(mis == 0 || mis == 1 || mis == 2)
				{
					MyGenomeAfter = saveToMyGenome(sequence,position);
				}
				break;
			case 1:
				String firstSequence = HumanGenome.substring(position - 10,position);
				String secondSequence = HumanGenome.substring(position + 10,position + 20);
				int misfrist = findMisMatches(firstSequence,arrSequence[0]);
				int misthird = findMisMatches(firstSequence,arrSequence[2]);
				
				int misSum = misfrist + misthird;
				if(misSum == 0 || misSum == 1 || misSum == 2)
				{
					MyGenomeAfter = saveToMyGenome(sequence,position);
				}
				break;
			case 2:
				String frscSequence = HumanGenome.substring(position - 20,position);
				int mis2 = findMisMatches(frscSequence,arrSequence[0] + arrSequence[1]);
				if(mis2 == 0 || mis2 == 1 || mis2 == 2)
				{
					MyGenomeAfter = saveToMyGenome(sequence,position);
				}
				break;
			}

		}
	}
	private static String saveToMyGenome(String strSequence,int location)
	{
		int start = location + strSequence.length();
		if(start > N)
			start = N - (30 - (start - N));
		String newNameResult  = MyGenomeAfter.substring(0, location) + strSequence + MyGenomeAfter.substring(start,N);
		return newNameResult;
	}

	//mismatches를 구하는 함수
	private static int findMisMatches(String strA, String strB) {
		int result = 0;
		int[][] distanceBetweenPrefixes = new int[strA.length()][strB.length()];
		  for (int[] row : distanceBetweenPrefixes) {
		    Arrays.fill(row, -1);
		  }
		  result=  computeDistanceBetweenPrefixes(strA, strA.length() - 1, strB, strB.length() - 1, distanceBetweenPrefixes);
		return result;
	}
	private static int computeDistanceBetweenPrefixes(String strA, int strA_index, String strB, int strB_index,
			int[][] distanceBetweenPrefixes) {
		// TODO Auto-generated method stub
		if (strA_index < 0) {
		    return strB_index + 1;
		  } else if (strB_index < 0) {
		    return strA_index + 1;
		  }
		 if (distanceBetweenPrefixes[strA_index][strB_index] == -1) {
			 if (strA.charAt(strA_index) == strB.charAt(strB_index))
			 {
			      distanceBetweenPrefixes[strA_index][strB_index] = computeDistanceBetweenPrefixes(strA, strA_index - 1, strB, strB_index - 1, distanceBetweenPrefixes);
			  } else {
			    	 int substituteLast = computeDistanceBetweenPrefixes(strA, strA_index - 1, strB, strB_index - 1, distanceBetweenPrefixes);
				     int addLast = computeDistanceBetweenPrefixes(strA, strA_index, strB, strB_index - 1, distanceBetweenPrefixes);
				     int deleteLast = computeDistanceBetweenPrefixes(strA, strA_index - 1, strB, strB_index, distanceBetweenPrefixes);
				     
				     distanceBetweenPrefixes[strA_index][strB_index] = 1 + Math.min(substituteLast , Math.min(addLast, deleteLast));
			    }
	  }
		return distanceBetweenPrefixes[strA_index][strB_index];
	}
	public static void main(String[] args) throws InterruptedException, IOException {
		System.out.println("원본 ADN 조합하는 프로그램");
		System.out.println("Length Genome – N " + N);
		System.out.println("Reads of map – M  " + M);
		System.out.println("Reads are of length – L  " + L);
		long startTime = System.nanoTime();

		MySequence.madeTabelNull();
		MySequence.madeGenome();
		MySequence.cutFullSequence("MyReads.txt", MyGenome);
		MySequence.reConstruct();
		System.out.println("Complete.");
		long endTime = System.nanoTime();
		long timeElapsed = endTime - startTime;

		System.out.println("Execution time in milliseconds : " + timeElapsed / 1000000);

	}

}
