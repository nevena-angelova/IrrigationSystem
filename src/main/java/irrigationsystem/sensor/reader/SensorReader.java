package irrigationsystem.sensor.reader;

/*
Represents a base class for simulated sensor reader that
generates random values in some interval. This min - max
interval is different for each successor
 */

public abstract class SensorReader {
    protected double minValue;
    protected double maxValue;

    public SensorReader() {
    }

    public double read() {
        return generateRandomData();
    }

    private double generateRandomData() {
        return minValue + (maxValue - minValue) * Math.random();
    }
}
