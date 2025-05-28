package irrigationsystem.sensor.reader;

import irrigationsystem.model.MeasureTypeEnum;

public class ReaderFactory {
    public static SensorReader createSensorReader(MeasureTypeEnum type) {

        switch (type) {
            case Temperature:
                return new TemperatureReader();
            case Humidity:
                return new HumidityReader();
            case Pressure:
                return new PressureReader();
            default:
                throw new IllegalArgumentException("Unknown sensor type: " + type);
        }
    }
}
