package irrigationsystem.sensor.reader;

public abstract class SensorReader {
    protected double minValue;
    protected double maxValue;

    public SensorReader() {
    }

    public double read(){
        return generateRandomData();
    }

    private double generateRandomData() {
        return minValue + (maxValue - minValue) * Math.random();
    }
}
