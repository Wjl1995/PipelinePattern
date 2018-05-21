package pers.PipelineModel;

import java.util.Random;

public class RandomString {

	private static RandomString Instance = new RandomString();
	private static String string = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()_+{}-''<>?";
	
	private RandomString(){
		
	}
	
	static public RandomString getInstance()
	{
		return Instance;
	}
	
	public String createString(Random random, int length)
	{
		char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = string.charAt(random.nextInt(string.length()));
        }
        return new String(text);
	}
	
}
