package com.example.scontz.carrent;

/**
 * Created by scOnTz on 22/11/2559.
 */
public class ListAllCar {
    private String strCID, strCNAME, strDEATAIL, strCIMG;
    private int intCAMOUNT;
    private double doubFEEPERDAY, doubFEEPERMONTH;

    public String getStrCID() {
        return strCID;
    }

    public void setStrCID(String strCID) {
        this.strCID = strCID;
    }

    public String getStrCNAME() {
        return strCNAME;
    }

    public void setStrCNAME(String strCNAME) {
        this.strCNAME = strCNAME;
    }

    public String getStrDEATAIL() {
        return strDEATAIL;
    }

    public void setStrDEATAIL(String strDEATAIL) {
        this.strDEATAIL = strDEATAIL;
    }

    public String getStrCIMG() {
        return strCIMG;
    }

    public void setStrCIMG(String strCIMG) {
        this.strCIMG = strCIMG;
    }

    public int getIntCAMOUNT() {
        return intCAMOUNT;
    }

    public void setIntCAMOUNT(int intCAMOUNT) {
        this.intCAMOUNT = intCAMOUNT;
    }

    public double getDoubFEEPERDAY() {
        return doubFEEPERDAY;
    }

    public void setDoubFEEPERDAY(double doubFEEPERDAY) {
        this.doubFEEPERDAY = doubFEEPERDAY;
    }

    public double getDoubFEEPERMONTH() {
        return doubFEEPERMONTH;
    }

    public void setDoubFEEPERMONTH(double doubFEEPERMONTH) {
        this.doubFEEPERMONTH = doubFEEPERMONTH;
    }

    public String toString() {
        return strCID + "," + strCIMG + "," + strCNAME + "," +
                strDEATAIL + "," + intCAMOUNT + "," + doubFEEPERDAY + "," + doubFEEPERDAY;
    }
}
