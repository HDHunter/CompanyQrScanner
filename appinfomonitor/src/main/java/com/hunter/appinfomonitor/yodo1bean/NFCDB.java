package com.hunter.appinfomonitor.yodo1bean;

import java.io.Serializable;
import java.util.List;

public class NFCDB implements Serializable {


    private List<NfcUtils.NFC> nfcList;


    public List<NfcUtils.NFC> getNfcList() {
        return nfcList;
    }

    public void setNfcList(List<NfcUtils.NFC> nfcList) {
        this.nfcList = nfcList;
    }
}
