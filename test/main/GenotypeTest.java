package main;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class GenotypeTest {
    @Test
    public void testGenotype(){
        for(int i=0; i<50; i++){
            boolean[] check = new boolean[8];
            Arrays.fill(check, false);

            Genotype genotype = new Genotype();
            String gen = genotype.toString();
            for(int j=0; j<gen.length(); j++){
                check[Character.getNumericValue(gen.charAt(j))] = true;
            }

            for(boolean b : check)
                if(b == false)
                    Assert.assertTrue(gen, false);
            Assert.assertTrue(true);
        }
    }
    @Test
    public void testGenotype2(){
        for(int i=0; i<50; i++){
            Genotype gen1 = new Genotype();
            Genotype gen2 = new Genotype();

            Genotype gen = new Genotype(gen1,gen2);
            String genStr = gen.toString();

            boolean[] check = new boolean[8];
            Arrays.fill(check, false);
            for(int j=0; j<genStr.length(); j++){
                check[Character.getNumericValue(genStr.charAt(j))] = true;
            }

            for(boolean b : check)
                if(b == false)
                    Assert.assertTrue(genStr, false);
            Assert.assertTrue(true);
        }
    }
}
