package com.nppgks.dkipia.service;

import com.nppgks.dkipia.entity.*;

import java.util.List;

public interface SensorService {
    public List<SensorTypeEntity> getSensorTypes();
//    public List<SelectionEntity> getSelections(SensorTypeEntity sensorTypeEntity, String mlfb, String mlfbB, String mlfbC, String group, String option);
    public List<SelectionEntity> getSelections(SensorTypeEntity sensorTypeEntity, String group, String option, String mlfbCText);
//    public String[] getMlfbSeparate(String mlfb);
    public SensorTypeEntity getSensorTypeById(int id);

    public List<Sensors> getSensors();
    public Sensors getSensorById(int idSensor);
    public List<SensorsLabels> getSensorLabels(int idSensor);
//    public void setActiveLabelById(List<SensorsLabels> sensorsLabels, int idLabel);
    public void setActiveLabelByPosition(List<SensorsLabels> sensorsLabels, String position);
    public void setActiveLabelByPosition(List<SensorsLabels> sensorsLabels, String position, String mlfb);
    public void setSelectedPositionsByMlfb(List<SensorsLabels> sensorsLabels, String mlfb);
    public void setMlfbWithOptions(Sensors sensor, String mlfb, String mlfbB, String mlfbC);
    public void setSelectedPositionsByMlfbB(List<SensorsLabels> sensorsLabels, int idSensor, String mlfbB);
    public void setYoptions(List<SensorsLabels> sensorsLabels, String mlfbC);

    public String[] getMlfbBAndMlfbC(List<SensorsLabels> sensorsLabels, String mlfbB, String mlfbC, String group, String option, String mlfbCText);
    public List<SensorStatus> getSensorStatus(String mlfb);
    public void setSelectTypeByStatus(List<SensorsLabels> sensorsLabels, List<SensorStatus> sensorStatus);
    public String getRussianMlfb(String mlfb);
}
