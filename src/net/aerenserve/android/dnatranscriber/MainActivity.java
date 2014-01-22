package net.aerenserve.android.dnatranscriber;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "net.aerenserve.android.dnatranscriber.MESSAGE";
	public Map<String, String> proteins = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		proteins = initializeProteinTable();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public static Map<String, String> initializeProteinTable() {
		Map<String, String> proteins = new HashMap<String, String>();
		
		proteins.put("uuu", "phenylalanine");
		proteins.put("uuc", "phenylalanine");
		proteins.put("uua", "leucine");
		proteins.put("uug", "leucine");
		
		proteins.put("ucu", "serine");
		proteins.put("ucc", "serine");
		proteins.put("uca", "serine");
		proteins.put("ucg", "serine");
		
		proteins.put("uau", "tyrosine");
		proteins.put("uac", "tyrosine");
		proteins.put("uaa", "STOP");
		proteins.put("uag", "STOP");
		
		proteins.put("ugu", "cysteine");
		proteins.put("ugu", "cysteine");
		proteins.put("ugu", "STOP");
		proteins.put("ugu", "tryptophan");
		
		/* ---- */
		
		proteins.put("cuu", "leucine");
		proteins.put("cuc", "leucine");
		proteins.put("cua", "leucine");
		proteins.put("cug", "leucine");
		
		proteins.put("ccu", "proline");
		proteins.put("ccc", "proline");
		proteins.put("cca", "proline");
		proteins.put("cug", "proline");
		
		proteins.put("cau", "histidine");
		proteins.put("cac", "histidine");
		proteins.put("caa", "glutamine");
		proteins.put("cag", "glutamine");
		
		proteins.put("cgu", "arginine");
		proteins.put("cgc", "arginine");
		proteins.put("cga", "arginine");
		proteins.put("cgg", "arginine");
		
		/* ---- */
		
		proteins.put("auu", "isoleucine");
		proteins.put("auc", "isoleucine");
		proteins.put("aua", "isoleucine");
		proteins.put("aug", "methionine");
		
		proteins.put("acu", "threonine");
		proteins.put("acc", "threonine");
		proteins.put("aca", "threonine");
		proteins.put("acg", "threonine");
		
		proteins.put("aau", "asparagine");
		proteins.put("aac", "asparagine");
		proteins.put("aaa", "lysine");
		proteins.put("aag", "lysine");
		
		proteins.put("agu", "serine");
		proteins.put("agc", "serine");
		proteins.put("aga", "arginine");
		proteins.put("agg", "arginine");
		
		/* ---- */
		
		proteins.put("guu", "valine");
		proteins.put("guc", "valine");
		proteins.put("gua", "valine");
		proteins.put("gug", "valine");
		
		proteins.put("gcu", "alanine");
		proteins.put("gcc", "alanine");
		proteins.put("gca", "alanine");
		proteins.put("gcg", "alanine");
		
		proteins.put("gau", "aspartate");
		proteins.put("gac", "aspartate");
		proteins.put("gaa", "glutamate");
		proteins.put("gag", "glutamate");
		
		proteins.put("ggu", "glycine");
		proteins.put("ggc", "glycine");
		proteins.put("gga", "glycine");
		proteins.put("ggg", "glycine");
		
		return proteins;
	}
	
	public void sendMessage(View view) {
	    Intent intent = new Intent(this, DisplayMessageActivity.class);
	    EditText editText = (EditText) findViewById(R.id.edit_message);
	    String message = editText.getText().toString();
	    	    
		if(!isDNA(message)) {
			System.out.println("The string you entered is not a valid DNA sequence!");
		    intent.putExtra(EXTRA_MESSAGE, "The string you entered is not a valid DNA sequence!");	    
		    startActivity(intent);
		    return;
		}
		
		String rna = transcribeDNA(message);
		System.out.println("RNA = " + rna);
	    
	    String entire = ("RNA : " + rna + "\r\n" + iterateThroughRNA(rna, proteins));
	    intent.putExtra(EXTRA_MESSAGE, entire);
	    
	    startActivity(intent);
	}
	
	public static boolean isDNA(String s) {
		s = s.replaceAll(" ", "");
		if(!s.matches("[actg]+")) {
			return false;	
		} else {
			return true;
		}
	}
	
	public static String transcribeDNA(String dna) {
		dna = dna.replaceAll("a", "u");
		dna = dna.replaceAll("t", "a");
		dna = dna.replaceAll("c", "q");
		dna = dna.replaceAll("g", "c");
		dna = dna.replaceAll("q", "g");
		return dna;
	}
	
	public static String iterateThroughRNA(String rna, Map<String, String> map) {
		int i = rna.indexOf("aug");
		StringBuilder builder = new StringBuilder();
		if(i != -1) {
			if(rna.length() % 3 != 0) {
				rna = rna.substring(0, rna.length()-1);
				if(rna.length() % 3 != 0) {
					rna = rna.substring(0, rna.length()-1);
				}
			}
			System.out.println("Length : " + rna.length());
			while(i < rna.length()) {
				if(i+3 <= rna.length()) {
					//System.out.println("Substring : " + rna.substring(i, i+3));
					//System.out.println("Protein : " + map.get(rna.substring(i, i+3)));
					builder.append(", " + map.get(rna.substring(i, i+3)));
					if(map.get(rna.substring(i, i+3)).equalsIgnoreCase("STOP")) {
						return builder.toString().replaceFirst(", ", "");
					}
				}
				i = i+3;
			}	
		} else {
			System.out.println("AUG could not be found in the RNA");
			return "AUG could not be found in the RNA";
		}
		return builder.toString().replaceFirst(", ", "");
	}
}
