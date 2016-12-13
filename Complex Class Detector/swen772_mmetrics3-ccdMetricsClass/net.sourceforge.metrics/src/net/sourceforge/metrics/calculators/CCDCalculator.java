package net.sourceforge.metrics.calculators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.IType;

import net.sourceforge.metrics.calculators.qmood.CohesionAmongMethodsOfClass;
import net.sourceforge.metrics.calculators.qmood.DirectClassCoupling;
import net.sourceforge.metrics.core.Avg;
import net.sourceforge.metrics.core.Constants;
import net.sourceforge.metrics.core.Metric;
import net.sourceforge.metrics.core.sources.AbstractMetricSource;

/**
 * Requires WMC, TCC, ATFD
 * 
 * WMC = Weighted Method Count TCC = Cohesion Among Method Class = CAM
 * 
 * For ATFD we will use RegEx to identify getters and setters
 */
public class CCDCalculator extends Calculator implements Constants {

	List<String> complexClasses;
	List<String> potentialComplexClasses;
	String[] ccdMetricsStrs = { "WMC" /** someof VG */
			, LCOM, CAM, INHERITANCE_DEPTH, NUM_METHODS, DCC };
	// Each of these list will hold a metric value
	static List<MetricsValues> wmcList;
	static List<MetricsValues> lcomList;
	static List<MetricsValues> camList;
	static List<MetricsValues> ditList;
	static List<MetricsValues> nomList;
	static List<MetricsValues> dccList;

	// The aggregate will hold class metrics
	static List<ClassMetrics> aggregateMetrics;
	static List<String> classTracker;
	static List<ClassMetrics> prevAggregateMetrics;

	public CCDCalculator() {
		super(CCD);
		complexClasses = new ArrayList<>();
		potentialComplexClasses = new ArrayList<>();
		wmcList = new ArrayList<>();
		lcomList = new ArrayList<>();
		camList = new ArrayList<>();
		ditList = new ArrayList<>();
		nomList = new ArrayList<>();
		dccList = new ArrayList<>();

		aggregateMetrics = new ArrayList<>();
		classTracker = new ArrayList<>();
		prevAggregateMetrics = new ArrayList<>();
	}
	

	@Override
	public void calculate(AbstractMetricSource source) throws InvalidSourceException {
		if (source.getLevel() != TYPE) {
			throw new InvalidSourceException("Wrong Java Element. Expecting Type type.");
		}
		
		///////
		boolean isFirstRun = true;
		
		//check to make sure it's the same project
		if(!classTracker.isEmpty()){
			String prevProj = classTracker.get(0).split("/")[0];
			String curProj = source.getHandle().split("/")[0];
			if(!prevProj.equals(curProj)){
				prevAggregateMetrics.clear();
				aggregateMetrics.clear();
				classTracker.clear();
			}
		}
		
		classTracker.add(source.getHandle());

			//count the existing classes
		if(classTracker.size() > 0){
			String firstClassStr = classTracker.get(0);
			int firstClassCount = 0;
			for(String s : classTracker){
				if(s.equals(firstClassStr)){
					firstClassCount++;
				}
			}
			//check if all classes have the same # -1
			for(String s1 : classTracker){
				if(!s1.equals(classTracker.get(0))){
					int otherClassCount = 0;
					for(String s2 : classTracker){
						if(s1.equals(s2)){
							otherClassCount++;
						}
					}
					if (firstClassCount - otherClassCount != 1){
						isFirstRun = false;
						//break;
					}
				}
			}
			if(isFirstRun){
				isFirstRun = false;
				prevAggregateMetrics.clear();
				for(ClassMetrics cm : aggregateMetrics){
					prevAggregateMetrics.add(cm);
				}
				//prevAggregateMetrics = aggregateMetrics;
				aggregateMetrics.clear();
				classTracker.clear();
				classTracker.add(source.getHandle());
			}
		}
		
			
			
			///////

		Map<String, Metric> metrics = source.getValues();
		Map<String, Avg> avgs = source.getAverages();

		ClassMetrics cm = new ClassMetrics(source.getJavaElement().getElementName());
		
		for (String s : metrics.keySet()) {
			if (s.equals(WMC)) {
				cm.addMetric(new MetricsValues(WMC, metrics.get(WMC).getValue()));
			}
			if(s.equals(LCOM)){
				cm.addMetric(new MetricsValues(LCOM, metrics.get(LCOM).getValue()));
			}
			if(s.equals(CAM)){
				cm.addMetric(new MetricsValues(CAM, metrics.get(CAM).getValue()));
			}
			if(s.equals(INHERITANCE_DEPTH)){
				cm.addMetric(new MetricsValues(INHERITANCE_DEPTH, metrics.get(INHERITANCE_DEPTH).getValue()));
			}
			if(s.equals(NUM_METHODS)){
				cm.addMetric(new MetricsValues(NUM_METHODS, metrics.get(NUM_METHODS).getValue()));
			}
			if(s.equals(DCC)){
				cm.addMetric(new MetricsValues(DCC, metrics.get(DCC).getValue()));
			}
		}
		
		aggregateMetrics.add(cm);
		
		
		
		
		//getBottom25(aggregateMetrics, metricId);
		
//		aggregateMetrics.add(new ClassMetrics().getJavaElement().getElementName(), wmcList);

		// IType type = ((IType)source.getJavaElement());

		//List<ClassMetrics> b25 = getBottom25(prevAggregateMetrics, WMC);
		
		//double p = getPercentile(prevAggregateMetrics, INHERITANCE_DEPTH, cm.className);
		
		double ccr = getComplexClassRating(prevAggregateMetrics, cm.className, false);
		source.setValue(new Metric(CCD, ccr));
		
		//source.setValue(new Metric("CCD1", getTotalMetricBreaks(prevAggregateMetrics, cm.className)));
		
		double p = getPercentile(prevAggregateMetrics, "WMC", cm.className);
		int tmb = getTotalMetricBreaks(prevAggregateMetrics, cm.className);
		source.setValue(new Metric("CCD1", tmb));
		
		
		int test = 0;


	}
	
	double getPercentile(List<ClassMetrics> prevAggregateMetrics, String metricId, String className){
		
		List<ClassMetrics> validClasses = new ArrayList<>();
		
		for(ClassMetrics cm : prevAggregateMetrics){
			if(cm.getMetric(metricId) != null){
				validClasses.add(cm);
			}
		}
		
		int nonnull = 0;
		for(ClassMetrics cm : validClasses){
			if(cm.getMetric(metricId).metricValue != null){
				nonnull++;
			}
		}
		int validCount = validClasses.size();
		
		Collections.sort(validClasses, new Comparator<ClassMetrics>(){
			public int compare(ClassMetrics o1, ClassMetrics o2){
				//TODO deal with nulls
				double m1 = o1.getMetric(metricId).metricValue;
				double m2 = o2.getMetric(metricId).metricValue;
				/*
				if(m1 == m2){
					return 0;
				}
				return m1 < m2 ? -1 : 1;
				*/
				if(m1 < m2) return -1;
				if(m2 < m1) return 1;
				return 0;
			}
		});
		
		ClassMetrics curCM = null;
		for(ClassMetrics cm : validClasses){
			if(cm.className.equals(className)){
				curCM = cm;
				break;
			}
		}
		
		double percentile = (validClasses.indexOf(curCM) + 1) * 100.0f / ((double)validClasses.size());
		
		return percentile;
		
	}
	
	int getComplexClassRating(List<ClassMetrics> prevAggregateMetrics, String className, boolean limitTo3){
		boolean breaksWMC = false;
		boolean breaksDIT = false;
		boolean breaksLCOM = false;
		boolean breaksCAM = false;
		boolean breaksDCC = false;
		
		if(getPercentile(prevAggregateMetrics, WMC, className) >= 75) breaksWMC = true;
		if(getPercentile(prevAggregateMetrics, INHERITANCE_DEPTH, className) >= 50) breaksDIT = true;
		if(getPercentile(prevAggregateMetrics, LCOM, className) <= 25) breaksLCOM = true;
		if(getPercentile(prevAggregateMetrics, CAM, className) <= 25) breaksCAM = true;
		if(getPercentile(prevAggregateMetrics, DCC, className) >= 75) breaksDCC = true;
		
		int rating = 0;
		if(breaksDIT && breaksWMC) rating += 2;
		if(breaksLCOM && breaksWMC) rating += 3;
		if(breaksLCOM && breaksDIT) rating += 2;
		if(breaksCAM && breaksWMC) rating += 3;
		if(breaksCAM && breaksDIT) rating += 2;
		if(breaksCAM && breaksLCOM) rating += 1;
		if(breaksDCC && breaksWMC) rating += 3;
		if(breaksDCC && breaksDIT) rating += 2;
		if(breaksDCC && breaksLCOM) rating += 2;
		if(breaksDCC && breaksCAM) rating += 2;
		
		if(limitTo3 && rating > 3){
			rating = 3;
		}
		
		return rating;
	}
	
	int getTotalMetricBreaks(List<ClassMetrics> prevAggregateMetrics, String className){
		int total = 0;
		
		if(getPercentile(prevAggregateMetrics, WMC, className) >= 75) {
			total++;
		}
		if(getPercentile(prevAggregateMetrics, INHERITANCE_DEPTH, className) >= 50) total++;
		if(getPercentile(prevAggregateMetrics, LCOM, className) <= 25) total++;
		if(getPercentile(prevAggregateMetrics, CAM, className) <= 25) total++;
		if(getPercentile(prevAggregateMetrics, DCC, className) >= 75) total++;
		
		return total;
		
		
	}

	
}
