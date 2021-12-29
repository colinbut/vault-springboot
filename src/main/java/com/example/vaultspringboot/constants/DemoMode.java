package com.example.vaultspringboot.constants;

public enum DemoMode {
    ENV_VAR("ENV-VAR"), APP_CONFIG("APP-CONFIG");

    private String demoModeConf;

    private DemoMode(String demoModeConf){
        this.demoModeConf = demoModeConf;
    }

    public String getDemoModeConf(){
        return demoModeConf;
    }
}
