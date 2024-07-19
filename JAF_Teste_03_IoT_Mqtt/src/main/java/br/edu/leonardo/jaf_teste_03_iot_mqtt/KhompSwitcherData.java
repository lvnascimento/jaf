/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.leonardo.jaf_teste_03_iot_mqtt;

/**
 * A state of a switcher connected to a <a href="https://www.khomp.com/iot/pt/produto/endpoint-ieee-802-15-4/">Khomp NIT 21ZI</a> 
 * sensor.
 * 
 * @author Leonardo Vianna do Nascimento (lvianna@gmail.com)
 */
public class KhompSwitcherData {
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This constructor builds a KhompSwitcherData that is off and with count equals to zero.
     */
    public KhompSwitcherData() {
    }

    /**
     * This constructor builds a KhompSwitcherData with the given values.
     * 
     * @param on true if the switcher state is on; false otherwise.
     * @param count The number of times that the switcher changes to on state.
     */
    public KhompSwitcherData(boolean on, int count) {
        this.on = on;
        this.count = count;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // G E T T E R S   A N D   S E T T E R S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * This method checks if the switcher is on.
     * 
     * @return true if the switcher state is on; false otherwise.
     */
    public boolean isOn() {
        return on;
    }

    /**
     * This method sets the switcher state.
     * 
     * @param on true if the switcher state is on; false otherwise.
     */
    public void setOn(boolean on) {
        this.on = on;
    }

    /**
     * This method obtains the number of times that the switcher changes to on state.
     * 
     * @return The state change count. 
     */
    public int getCount() {
        return count;
    }

    /**
     * This method sets the number of times that the switcher changes to on state.
     * 
     * @param count The state change count. 
     */
    public void setCount(int count) {
        this.count = count;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // T O   S T R I N G ,   E Q U A L S ,   A N D   H A S H C O D E
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public String toString() {
        return "KhompSwitcherData{" + "on=" + on + ", count=" + count + '}';
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The switcher state: true if the switcher state is on; false otherwise.
     */
    private boolean on;
    
    /**
     * The number of times that the switcher changes to on state.
     */
    private int count;
}
