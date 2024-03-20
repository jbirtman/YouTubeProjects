package com.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintImageOptions;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
        FingerprintTemplate probe = new FingerprintTemplate(new FingerprintImage(Files.readAllBytes(Paths.get("s1.png")), new FingerprintImageOptions()
        .dpi(500)));
        FingerprintTemplate candidate = new FingerprintTemplate(new FingerprintImage(Files.readAllBytes(Paths.get("sx.png")), new FingerprintImageOptions()
        .dpi(500)));
        FingerprintMatcher matcher = new FingerprintMatcher(probe);
        double similarity = matcher.match(candidate);
        System.out.println("Similarity Between Prints: " + similarity);
        if (similarity > 40) { //this is 0.01 false positive rating
            System.out.println("MATCH!!!");
        }
        System.out.println( findMostSimilar("J2/IMG_3717.png"));
    }

    public static String findMostSimilar(String base) throws IOException{
        FingerprintTemplate probe = new FingerprintTemplate(new FingerprintImage(Files.readAllBytes(Paths.get(base)), new FingerprintImageOptions()
        .dpi(1000)));
        FingerprintMatcher matcher = new FingerprintMatcher(probe);

        String currentDirectory = System.getProperty("user.dir");
        double highestSimilarity = -1;
        String ans = "";

        for (int i = 1; i < 3; i++) {
            File directory = new File(currentDirectory+"/J" + i);
            File[] files = directory.listFiles();

            double sum = 0;
            for (File file : files) {
                 if (file.isFile() && file.getName().toLowerCase().endsWith(".png") && !file.getName().equals(base.substring(3))) {
                    FingerprintTemplate candidate = new FingerprintTemplate(new FingerprintImage(Files.readAllBytes(Paths.get(file.getAbsolutePath())), new FingerprintImageOptions().dpi(1000)));
                    double similarity = matcher.match(candidate);
                    sum += similarity;
                }
            }
            sum /= files.length;
            if (sum > highestSimilarity) {
                highestSimilarity = sum;
                ans = directory.getName();
            }
        }
        return ans;
    }
}
