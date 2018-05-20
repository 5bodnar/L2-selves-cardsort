package test.ru.fasop.motivation;

import junit.framework.TestCase;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class StatsTest extends TestCase {
	public void testCalculateZScore(){
	
		double[] data = new double[]{1, 3, 4, 5, 8, 8, 8, 9, 55, 102};
		
		DescriptiveStatistics ds = new DescriptiveStatistics(data);
		
		assertEquals(1, Math.round(ds.getMin()));
		assertEquals(102, Math.round(ds.getMax()));
		assertEquals(20, Math.round(ds.getMean()));
		assertEquals(33, Math.round(ds.getStandardDeviation()));
		
		assertEquals(-1.0, Math.floor((1.0 - ds.getMean()) / ds.getStandardDeviation()));
		assertEquals(-1.0, Math.floor((8.0 - ds.getMean()) / ds.getStandardDeviation()));
		
		assertEquals(1.0, Math.ceil((39 - ds.getMean()) / ds.getStandardDeviation()));
		assertEquals(1.0, Math.ceil((53 - ds.getMean()) / ds.getStandardDeviation()));
		
		assertEquals(2.0, Math.ceil((54 - ds.getMean()) / ds.getStandardDeviation()));
		assertEquals(2.0, Math.ceil((85 - ds.getMean()) / ds.getStandardDeviation()));
		
		assertEquals(3.0, Math.ceil((86 - ds.getMean()) / ds.getStandardDeviation()));
		
		assertEquals(1.0, ds.getMin());
		assertEquals(3.75, ds.getPercentile(25));
		assertEquals(8.0, ds.getPercentile(50));
		assertEquals(20.5, ds.getPercentile(75));
		assertEquals(102.0, ds.getPercentile(100));
		
		assertEquals(3.0, getZScore(data, 86));
	}
	
	double getZScore(double[] data, double val){
		DescriptiveStatistics ds = new DescriptiveStatistics(data);
		
		double signedSDProportion = (val - ds.getMean()) / ds.getStandardDeviation();
		
		if(0 < val - ds.getMean()){
			return Math.ceil(signedSDProportion);	
		}
		else {
			return Math.floor(signedSDProportion);
		}
	}
	
}
