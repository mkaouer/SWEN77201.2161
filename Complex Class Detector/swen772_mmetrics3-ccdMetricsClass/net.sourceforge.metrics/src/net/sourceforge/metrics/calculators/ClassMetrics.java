package net.sourceforge.metrics.calculators;

import java.util.ArrayList;
import java.util.List;


class ClassMetrics {
	String className;
	
	/** List to hold the metrics name and value */
	List<MetricsValues> classMetrics;

	public ClassMetrics(String className) {
		super();
		this.className = className;
		
		classMetrics = new ArrayList<>();
	}
	
	public void addMetric(MetricsValues mValues) {
		classMetrics.add(mValues);			
	}
	
	public MetricsValues getMetric(String metricId){
		for(MetricsValues m : classMetrics){
			if(m.metricName.equals(metricId)){
				return m;
			}
		}
		return null;
	}
	
	
}

/**
 * Class to keep track of metrics and value
 * @author apetitfr
 *
 */
class MetricsValues implements Comparable<MetricsValues>{
	String metricName;
	Double metricValue;
	
	// Maybe we can switch this to the name of the metrics 
	// a list of classes and a list of values associated with classes (map)
	// because we only have 6 metrics, but an unknown number of class
	// When I come later I'll investigate that separately. 
	// That might make it easier
	
	@Override
	public String toString() {
		return "MetricsValues [metricName=" + metricName + ", metricValue=" + metricValue + "]";
	}

	/**
	 * Constructor 
	 * @param metricName
	 * @param metricValue
	 */
	public MetricsValues(String metricName, Double metricValue) {
		super();
		this.metricName = metricName;
		this.metricValue = metricValue;
	}
	
	
	@Override
	public int compareTo(MetricsValues o) {
		int comparator = -13;
		if (metricValue < o.getMetricValue()) {
			comparator = -1;
		}
		if (metricValue > o.getMetricValue()) {
			comparator = 1;
		}
		if (metricValue == o.getMetricValue()) {
			comparator = 0;
		}
		return comparator;
	}
	public String getMetricName() {
		return metricName;
	}
	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}
	public Double getMetricValue() {
		return metricValue;
	}
	public void setMetricValue(Double metricValue) {
		this.metricValue = metricValue;
	}
	
	public Double getValue(String name) {
		if (name.equals(metricName)) {
			return metricValue;
		} else {
			return 0.0;
		}
	}
	
	
}