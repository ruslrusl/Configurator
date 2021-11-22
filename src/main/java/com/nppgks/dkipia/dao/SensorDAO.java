package com.nppgks.dkipia.dao;

import com.nppgks.dkipia.entity.*;

import java.util.List;

public interface SensorDAO {
    public List<SensorTypeEntity> getSensorTypes();
    public List<SelectionEntity> getSelections(int idSensor);
    public List<SelectOptionEntity> getSelectionOptions(int idSensor, String mlfb);

    public List<Sensors> getSensors();
    public Sensors getSensorById(int idSensor);
    public List<SensorsLabels> getSensorLabels(int idSensor);
    public List<SensorsOptionNames> getSensorOptions(int idSensorLabels);
    public List<SensorsOptionNames> getSensorOptionsByRule(String mlfb, int idSensorLabels);
    public List<SensorsOptionNames> getSensorOptionsByMlfb(String mlfb);
    public List<SensorsOptionNames> getSensorOptionsByMlfbB(int idSensor, String mlfbB);
    public List<SensorStatus> getSensorStatus(String mlfb);
    public String getRussianMlfb(String mlfb);
}
