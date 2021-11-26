package com.nppgks.dkipia.util;

public interface Constant {

    boolean ISTEST = true;

    interface DB {

        String URL = "jdbc:postgresql://localhost:5432/configurator";
        String USERNAME = "postgres";
        String PASSWORD = "123456";
    }

    interface MLFB {
        String DELIMETER_PLUS = "+";
        String DELIMETER_EMPTY = "";
        String DELIMETER_SPACE = " ";
        String DELIMETER_EQUAL = "=";
        String DELIMETER_OPEN_BRACKET = "{";
        String DELIMETER_CLOSE_BRACKET = "}";
        String OPTION_DELIMETER = "\\+";
        String DELIMETER_POINTS = "\\.\\.\\.";

        String[] Y01_UNITS = {"ATM", "BAR", "BAR_A", "BAR_G", "FTH2O", "G/CM2", "G/CM2_A", "G/CM2_G", "G/CM²", "HPA", "HPA_A", "HPA_G", "INH2O", "INH2O (4°C)", "INH2O@04", "INHG", "KG/CM2", "KG/CM2_A", "KG/CM2_G", "KG/CM²", "KGF/CM", "KGF/CM2", "KGF/CM²", "KPA", "KPA_A", "MBAR", "MBAR_A", "MBAR_G", "MH2O", "MH2O (4°C)", "MH2O@04", "MMH2O", "MMH2O (4°C)", "MMH2O@04", "MMHG", "MMHG_A", "MMHG_G", "MPA", "MPA_A", "MPA_G", "PA", "PA_A", "PA_G", "PSI", "PSI_A", "PSI_G", "TORR", "TORR_A", "TORR_G", "AT", "ATA", "CMCA", "CMCE", "CMH2O", "CMHG", "CMWC", "CMWG", "CMWS", "FTH2O@04", "FTH2O@20", "INH2O@20", "INWG", "KG/M2", "KG/M2_A", "KG/M2_G", "KGF/M2", "KP/CM2", "KP/CM2_A", "KP/CM2_G", "KP/M2", "KP/M2_A", "KP/M2_G", "LBF/FT2", "MCA", "MCE", "MHG", "MMAQ", "MMCA", "MMCE", "MMH2O@20", "MMWC", "MMWG", "MMWS", "MWC", "MWG", "MWS", "N/M2", "N/M2_A", "N/M2_G", "N/MM2", "N/MM2_A", "N/MM2_G", "NM3/H"};
        String[] Y21_UNITS = {"%", "ATM", "BAR", "BAR_A", "FTH2O", "FTH2O@20", "G/CM2", "INH2O", "INH2O@04", "INH2O@20", "INHG", "KG/CM2", "KGF/M2", "KPA", "KWH", "MBAR", "MBAR_A", "MMH2O", "MMH2O@04", "MMH2O@20", "MMHG", "MPA", "PA", "PSI", "TORR"};
        String[] NEW_Y01_UNITS = {"mbar", "bar", "psi", "Pa", "kPa", "MPa", "hPa", "g/cm²", "kg/cm²", "kgf/cm²", "atm", "torr", "mmH2O", "mmH2O (4°C)", "mH2O (4°C)", "inH2O", "inH2O (4°C)", "ftH2O", "mmHg", "inHg"};
        String[] NEW_Y02_UNITS = {"VSLN2","MSLN2"};
        String[] NEW_Y21_UNITS = {"%", "% abs.", "% gauge", "Unit", "Unit abs.", "Unit gauge", "mA"};
        String[] NEW_Y22_UNITS = {"m", "cm", "mm", "in", "ft", "m3", "l", "hl", "in3", "ft3", "yd3", "gal", "gal (UK)", "bu", "bbl", "bbl (US)", "SCF", "Nm3", "Nl", "m3/sec", "m3/h", "m3/d", "l/sec", "l/min", "l/h", "Ml/d", "ft3/sec", "ft3/h", "ft3/d", "SCF/min", "SCF/h", "Nl/h", "Nm3/h", "gal/sec", "gal/min", "gal/h", "gal/d", "Mgal/d", "gal (UK)/sec", "gal (UK)/min", "gal (UK)/h", "gal (UK)/d", "bbl/sec", "bbl/min", "bbl/h", "bbl/d", "kg/sec", "kg/min", "kg/h", "kg/d", "g/sec", "g/min", "g/h", "t/min", "t/h", "t/d", "lb/sec", "lb/min", "lb/h", "lb/d", "ton/min", "ton/h", "ton/d", "ton (UK)/h", "ton (UK)/d"};
        String[] NEW_Y26_UNITS = {"HART 5"};
        String[] NEW_Y30_UNITS = {"3.9 ... 20.8 mA","3.9 ... 22 mA","4 ... 20.8 mA","4 ... 22 mA"};
        String[] NEW_Y31_UNITS = {"3.75","21.75","22.5","22.6","22.8"};
        String[] NEW_Y38_UNITS = {"WARNINIG"};
    }

    interface SENSOR {
        int ELEMENT_RADIO = 1;
        int ELEMENT_CHECKBOX = 2;
    }

    interface STATUS {
        int OK = 1;
        int WARN = 2;
        int ERROR = 3;
        String NOT_COMPLETE = "Укажите, пожалуйста, свойства продукта";
    }

    interface FILE {
        String DIRECTORY = "/web/configurator/files/";
        String TEST_DIRECTORY = "C:\\java\\IdeaProjects\\Configurator\\files\\";

        String DIRECTORY_TEMPLATE = "/web/configurator/templates/";
        String TEST_DIRECTORY_TEMPLATE = "C:\\java\\IdeaProjects\\Configurator\\templates\\";

        String FILENAME_SPECIFICATION = "Закупочная спецификация 2.0.xlsx";

        String EXTENSION = ".xlsx";
        String EXPORT_FILE_MLFB = "code";
        String EXPORT_FILE_MLFB_DESC = "code_option";
        String EXPORT_FILE_TKP = "tkp";
        String EXPORT_FILE_MLFB_INDUSTRY = "mlfb";
        String EXPORT_FILE_SPECIFICATION = "specification";
    }
}
